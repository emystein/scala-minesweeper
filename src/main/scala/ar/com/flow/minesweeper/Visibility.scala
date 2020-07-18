package ar.com.flow.minesweeper

sealed abstract class Visibility extends Product with Serializable

object Visibility {
  final case object Hidden extends Visibility
  final case object Shown extends Visibility

  def apply(shown: Boolean): Visibility =  if (shown) Shown else Hidden
}
