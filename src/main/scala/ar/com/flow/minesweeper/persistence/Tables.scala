package ar.com.flow.minesweeper.persistence

import java.sql.Timestamp
import java.time.LocalDateTime

import ar.com.flow.minesweeper.{Board, Cell, Dimensions}
import slick.jdbc.H2Profile.api._
import slick.lifted.Tag

import scala.collection.immutable.HashMap
import scala.collection.mutable

object Tables {
  implicit val localDateToDate = MappedColumnType.base[LocalDateTime, Timestamp](
    localDateTime => Timestamp.valueOf(localDateTime),
    sqlTimestamp => sqlTimestamp.toLocalDateTime
  )

  type GameTuple = (String, LocalDateTime)

  class Games(tag: Tag) extends Table[GameTuple](tag, "game") {
    def id = column[String]("id", O.PrimaryKey)
    def createdAt = column[LocalDateTime]("created_at")
    // Every table needs a * projection with the same type as the table's type parameter
    def * = (id, createdAt)
    def board = foreignKey("board", id, boards)(_.id)
  }

  type BoardTuple = (String, Int, Int, Int)

  class Boards(tag: Tag) extends Table[BoardTuple](tag, "board") {
    def id = column[String]("id", O.PrimaryKey)
    def rows = column[Int]("rows")
    def columns = column[Int]("columns")
    def bombs = column[Int]("bombs")
    def * = (id, rows, columns, bombs)
  }

  type CellTuple = (String, Int, Int, Boolean, Int, Boolean, String)

  class Cells(tag: Tag) extends Table[CellTuple](tag, "cell") {
    def id = column[String]("id")
    def row = column[Int]("row")
    def col = column[Int]("column")
    def hasBomb = column[Boolean]("has_bomb")
    def numberOfAdjacentBombs = column[Int]("adjacent_bombs")
    def isRevealed = column[Boolean]("is_revealed")
    def value = column[String]("value")
    def pk = primaryKey("cell_pk", (id, row, col))
    def * = (id, row, col, hasBomb, numberOfAdjacentBombs, isRevealed, value)
  }

  val games = TableQuery[Games]

  val boards = TableQuery[Boards]
  val cells = TableQuery[Cells]
  val createSchemaAction = (games.schema ++ boards.schema ++ cells.schema).create

  val createDatabase = DBIO.seq(createSchemaAction)
  val dropSchemaAction = (games.schema ++ boards.schema ++ cells.schema).drop

  def mapToBoard(result: Seq[((GameTuple, BoardTuple), CellTuple)]): Board = {
    val cellsByCoordinates = HashMap(result.map(c => Tables.mapToCell(c._2)).map(c => (c.row, c.column) -> c): _*)
    Board(Dimensions(result.head._1._2._2, result.head._1._2._3), result.head._1._2._4, cellsByCoordinates)
  }

  def mapFromCell(gameId: String, cell: Cell): CellTuple = {
    (gameId, cell.row, cell.column, cell.hasBomb, cell.numberOfAdjacentBombs, cell.isRevealed, cell.value)
  }

  def mapToCell(cellTuple: CellTuple) : Cell = {
    Cell(cellTuple._2, cellTuple._3, cellTuple._4, cellTuple._5, cellTuple._6, cellTuple._7)
  }
}