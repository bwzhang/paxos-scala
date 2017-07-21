import akka.actor.{Actor, ActorLogging, Props}
import akka.cluster.pubsub.DistributedPubSub
import types._
import scala.collection.immutable.HashMap

object Scout {
  def props: Props = Props[Scout]
  // Sent by an Acceptor to accept a value
  case class P1b(ballotNum: BallotNumber, accepted: Map[Slot, (BallotNumber, Value)])
}

class Scout(ballotNum: BallotNumber, processIDs: Set[ProcessID]) extends Actor with ActorLogging {
  import Scout._

  val mediator = DistributedPubSub(context.system).mediator

  override def receive: Receive =
    receiveWithState(processIDs, HashMap[Slot, (BallotNumber, Value)]())

  def receiveWithState(waitingFor: Set[ProcessID], acceptedProposals: AcceptedMap): Receive = {
    case P1b(bn, accepted) => {
      // If bn != ballotNum, send Preempted to parent and terminate.
      // Otherwise, update waitingFor and acceptedProposals. If |waitingFor| < 1/2 * |processIDs|
      // send Adopted to parent and terminate.
    }
    case msg => log.debug(msg.toString)
  }
}
