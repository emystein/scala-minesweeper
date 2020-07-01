# Minesweeper #

Minesweeper API and model implemented with *Scalatra* for RESTful services, *Slick* for persistence.

This is not a pure functional model yet. For a pure functional model see: https://github.com/scala-minesweeper 

## Build & Run ##

```
$ sbt
> jetty:start
```

App root is http://localhost:8080

Endpoints are defined in `ar.com.flow.minesweeper.rest.MinesweeperServlet`
