# Minesweeper #

Minesweeper model implemented in Scala.

The model is hybrid object-oriented and functional. For a more functional model see: https://github.com/scala-minesweeper 

RESTful API implemented in [Scalatra](https://scalatra.org//).

Persistence implemented in [Slick](https://scala-slick.org/).


## Build & Run ##

```
$ sbt run
```

App root is http://localhost:8080

Swagger docs at http://localhost:8080/api-docs/swagger.json

Endpoints are defined in `ar.com.flow.minesweeper.rest.MinesweeperServlet`
