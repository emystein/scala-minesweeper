package ar.com.flow.minesweeper.persistence

import java.sql.Timestamp
import java.time.LocalDateTime

import ar.com.flow.minesweeper.CellContent.{Bomb, Empty}
import ar.com.flow.minesweeper.CellMark.{Flag, Question}
import ar.com.flow.minesweeper.Visibility.{Hidden, Shown}
import ar.com.flow.minesweeper._
import slick.jdbc.H2Profile.api._
import slick.lifted.Tag

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

  type BoardTuple = (String, Int, Int)

  class Boards(tag: Tag) extends Table[BoardTuple](tag, "board") {
    def id = column[String]("id", O.PrimaryKey)
    def rows = column[Int]("rows")
    def columns = column[Int]("columns")
    def * = (id, rows, columns)
  }

  implicit val cellContentColumnType = MappedColumnType.base[CellContent, Boolean](
    {
      case CellContent.Bomb => true
      case CellContent.Empty => false
    },
    {
      case true => CellContent.Bomb
      case false => CellContent.Empty
    }
  )

  implicit val visibilityColumnType = MappedColumnType.base[Visibility, Boolean](
    {
      case Shown => true
      case Hidden => false
    },
    {
      case true => Shown
      case false => Hidden
    }
  )

  implicit val cellMarkColumnType = MappedColumnType.base[CellMark, String](
    {
      case Flag => "f"
      case Question => "?"
    },
    {
      case "f" => Flag
      case "?" => Question
    }
  )

  type CellTuple = (String, Int, Int, CellContent, Visibility, Option[CellMark])

  class Cells(tag: Tag) extends Table[CellTuple](tag, "cell") {
    def id = column[String]("id")
    def row = column[Int]("row")
    def col = column[Int]("column")
    def hasBomb = column[CellContent]("has_bomb")
    def isRevealed = column[Visibility]("is_revealed")
    def value = column[Option[CellMark]]("value")
    def pk = primaryKey("cell_pk", (id, row, col))
    def * = (id, row, col, hasBomb, isRevealed, value)
  }

  val games = TableQuery[Games]

  val boards = TableQuery[Boards]
  val cells = TableQuery[Cells]
  val createSchemaAction = (games.schema ++ boards.schema ++ cells.schema).create

  val createDatabase = DBIO.seq(createSchemaAction)
  val dropSchemaAction = (games.schema ++ boards.schema ++ cells.schema).drop

  def mapToBoard(result: Seq[((GameTuple, BoardTuple), CellTuple)]): Board = {
    val cellsByCoordinates = result.map(c => Tables.mapToCell(c._2)).toMap
    Board(Dimensions(result.head._1._2._2, result.head._1._2._3), cellsByCoordinates)
  }

  def mapFromCell(gameId: String, cell: Cell): CellTuple = {
    (gameId, cell.coordinates.x, cell.coordinates.y, cell.content, cell.visibility, cell.mark)
  }

  def mapToCell(cellTuple: CellTuple) : (CartesianCoordinates, CellState) = {
    CartesianCoordinates(cellTuple._2, cellTuple._3) ->  CellState(cellTuple._4, cellTuple._5, cellTuple._6)
  }
}