package ar.com.flow.minesweeper

import java.text.SimpleDateFormat

import slick.jdbc.H2Profile.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class GameRepository(val db: Database) {

  protected implicit def executor = scala.concurrent.ExecutionContext.Implicits.global

  def save(game: Game): Unit = {
    val sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    val cells = game.board.cells.flatten.map(c => (game.id, c.row, c.column, c.hasBomb, c.numberOfAdjacentBombs, c.isRevealed, c.value)).toSeq

    db.run(DBIO.seq(
      Tables.cells ++= cells,
      Tables.boards += (game.id, game.board.totalRows, game.board.totalColumns, game.board.totalBombs),
      Tables.games += (game.id, sdf.format(game.createdAt))
    ))
  }

  def findAll: Seq[Game] = {
    val sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    val query = Tables.games.join(Tables.boards).on(_.id === _.id)
      .join(Tables.cells).on(_._2.id === _.id)
      .result.map(r => r.groupBy(_._1._1))

    Await.result(
      db.run(query).map(gr => gr.map(gr2 => new Game(gr2._1._1, sdf.parse(gr2._1._2), BoardFactory(gr2._2.head._1._2._2, gr2._2.head._1._2._3, gr2._2.head._1._2._4))).toSeq)
    , Duration.Inf)
  }
}
