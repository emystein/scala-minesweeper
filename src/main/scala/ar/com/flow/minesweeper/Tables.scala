package ar.com.flow.minesweeper

import slick.jdbc.H2Profile.api._
import slick.lifted.Tag

object Tables {
  class Games(tag: Tag) extends Table[(String, String)](tag, "game") {
    def id = column[String]("id", O.PrimaryKey)
    def createdAt = column[String]("created_at")
    // Every table needs a * projection with the same type as the table's type parameter
    def * = (id, createdAt)
    def board = foreignKey("board", id, boards)(_.id)
  }

  class Boards(tag: Tag) extends Table[(String, Int, Int, Int)](tag, "board") {
    def id = column[String]("id", O.PrimaryKey)
    def rows = column[Int]("rows")
    def columns = column[Int]("columns")
    def bombs = column[Int]("bombs")
    def * = (id, rows, columns, bombs)
  }

  class Cells(tag: Tag) extends Table[(String, Int, Int, Boolean, Int, Boolean, String)](tag, "cell") {
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

  // DBIO Action which creates the schema
  val createSchemaAction = (games.schema ++ boards.schema ++ cells.schema).create

  // DBIO Action which drops the schema
  val dropSchemaAction = (games.schema ++ boards.schema ++ cells.schema).drop

  // Create database, composing create schema and insert sample data actions
  val createDatabase = DBIO.seq(createSchemaAction)
}