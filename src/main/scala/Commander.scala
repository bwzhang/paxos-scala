import akka.actor.{Actor, ActorLogging, Props}
import akka.cluster.pubsub.DistributedPubSub
import types._

object Commander {
  def props: Props = Props[Commander]
  // Sent by an Acceptor indicating it accepted a value
  case class P2b(ballotNum: BallotNumber)
}

class Commander(ballotNum: BallotNumber, slot: Slot, value: Value, processIDs: Set[ProcessID])
    extends Actor with ActorLogging {
  import Commander._

  val mediator = DistributedPubSub(context.system).mediator

  override def receive: Receive = receiveWithState(processIDs)

  def receiveWithState(waitingFor: Set[ProcessID]): Receive = {
    case P2b(bn) => {
      // Update waitingFor. If |waitingFor| < 1/2 * |processIDs|, send Decision to all Replicas
    }
    case msg => log.debug(msg.toString)
  }
}
