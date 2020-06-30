package ar.com.flow.minesweeper

import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import slick.jdbc.JdbcBackend.Database

trait Persistence {
  val dataSource = new HikariDataSource(new HikariConfig("/app.properties"))
  val database = Database.forDataSource(dataSource, None)
}
