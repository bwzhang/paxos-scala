import types._

object BallotNumber {
  val bottom = BallotNumber(0, "")
}
case class BallotNumber(i: Int, processID: ProcessID) extends Ordered[BallotNumber] {
  def compare(that: BallotNumber): Int = {
    if (i != that.i) i.compare(that.i) else processID.compare(that.processID)
  }
}
