package ar.com.flow.minesweeper.persistence

import ar.com.flow.minesweeper.Visibility.Shown
import ar.com.flow.minesweeper.Game
import slick.jdbc.H2Profile.api._

import scala.concurrent.Future

class GameRepository(val db: Database) {
  protected implicit def executor = scala.concurrent.ExecutionContext.Implicits.global

  def save(game: Game): Unit = {
    val cells = game.board.cells.map(c => (game.id, c.coordinates.x, c.coordinates.y, c.content.isDefined, c.visibility == Shown, c.mark)).toSeq

    val cellsToBeInsertedOrUpdated = cells.map(cell => Tables.cells.insertOrUpdate(cell))

    db.run(DBIO.sequence(cellsToBeInsertedOrUpdated))

    db.run(DBIO.seq(
      Tables.boards += (game.id, game.board.dimensions.rows, game.board.dimensions.columns),
      Tables.games += (game.id, game.createdAt)
    ))
  }

  def findById(id: String): Future[Game] = {
    val query = Tables.games
      .filter(game => game.id === id)
      .join(Tables.boards)
      .join(Tables.cells).on{case ((game, board), cell) => game.id === board.id && board.id === cell.id}
      .result.map(r => r.groupBy(_._1._1)) // group by game id

    db.run(query).map(results => results.map(result => new Game(result._1._1, result._1._2, Tables.mapToBoard(result._2))).toSeq.head)
  }

  def findAll: Future[Seq[Game]] = {
    val query = Tables.games
      .join(Tables.boards)
      .join(Tables.cells).on{case ((game, board), cell) => game.id === board.id && board.id === cell.id}
      .result.map(r => r.groupBy(_._1._1)) // group by game id

    db.run(query).map(results => results.map(result => new Game(result._1._1, result._1._2, Tables.mapToBoard(result._2))).toSeq)
  }
}
