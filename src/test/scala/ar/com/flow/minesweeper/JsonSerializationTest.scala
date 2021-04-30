package ar.com.flow.minesweeper

import org.json4s.JsonAST.JObject
import org.json4s.native.Serialization.write
import org.json4s.{CustomSerializer, DefaultFormats, Formats, JString, _}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class JsonSerializationTest extends AnyFunSuite with TestObjects with Matchers {
  val cellFieldSerializer = FieldSerializer[Cell](
    {
      case ("board", _) => None
      case ("isEmpty", _) => None
      case ("isHidden", _) => None
      case ("isRevealed", _) => None
      case v => Option(v)
    }
  )

  implicit val formats: Formats = DefaultFormats + new VisibilitySerializer + new CellContentSerializer + new CellMarkSerializer + new BoardSerializer + cellFieldSerializer

  test("Serialize Hidden Visibility") {
    write(Visibility.Hidden) shouldBe "\"hidden\""
  }

  test("Serialize Revealed Visibility") {
    write(Visibility.Revealed) shouldBe "\"revealed\""
  }

  test("Serialize Empty CellContent") {
    write(CellContent.Empty) shouldBe "\"\""
  }

  test("Serialize Bomb CellContent as true") {
    write(CellContent.Bomb) shouldBe "\"bomb\""
  }

  test("Serialize empty, hidden Cell") {
    val cell = Cell(CartesianCoordinates(1, 1), CellContent.Empty, Visibility.Hidden, mark = None, board = None)

    val json = write(cell)

    json shouldBe ("{\"coordinates\":{\"x\":1,\"y\":1},\"content\":\"\",\"visibility\":\"hidden\"}")
  }

  test("Serialize bomb, revealed Cell") {
    val cell = Cell(CartesianCoordinates(1, 1), CellContent.Bomb, Visibility.Revealed, mark = None, board = None)

    val json = write(cell)

    json shouldBe ("{\"coordinates\":{\"x\":1,\"y\":1},\"content\":\"bomb\",\"visibility\":\"revealed\"}")
  }

  test("Serialize Flag mark") {
    val json = write(CellMark.Flag)

    json shouldBe "\"f\""
  }

  test("Serialize Dimensions") {
    val json = write(Dimensions(1, 2))

    json shouldBe "{\"rows\":1,\"columns\":2}"
  }

  test("Serialize 1 x 1 Board") {
    val board = Board(Dimensions(1, 1), 1)

    write(board) shouldBe "{\"dimensions\":{\"rows\":1,\"columns\":1},\"cells\":[{\"coordinates\":{\"x\":1,\"y\":1},\"content\":\"bomb\",\"visibility\":\"hidden\"}]}"
  }
}

class VisibilitySerializer extends CustomSerializer[Visibility](format => ( {
  case JString("hidden") => Visibility.Hidden
  case JString("revealed") => Visibility.Revealed
}, {
  case Visibility.Hidden => JString("hidden")
  case Visibility.Revealed => JString("revealed")
}
))

class CellContentSerializer extends CustomSerializer[CellContent](format => ( {
  case JString("") => CellContent.Empty
  case JString("bomb") => CellContent.Bomb
}, {
  case CellContent.Empty => JString("")
  case CellContent.Bomb => JString("bomb")
}
))

class CellMarkSerializer extends CustomSerializer[CellMark](format => ( {
  case JString("f") => CellMark.Flag
  case JString("?") => CellMark.Question
}, {
  case CellMark.Flag => JString("f")
  case CellMark.Question => JString("?")
}
))


class BoardSerializer extends CustomSerializer[Board](implicit formats => (
  {
    PartialFunction.empty
  },
  {
    case board: Board => {
      JObject(
        JField("dimensions", Extraction.decompose(board.dimensions)),
        JField("cells", JArray(board.allCells.map(Extraction.decompose).toList))
      )
    }
  }
))
