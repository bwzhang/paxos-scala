import akka.actor.{Actor, ActorLogging, Props}
import akka.cluster.pubsub.DistributedPubSub
import akka.cluster.pubsub.DistributedPubSubMediator.{Subscribe, SubscribeAck}
import types._
import scala.collection.immutable.HashMap

object Acceptor {
  def props: Props = Props[Acceptor]
  // Sent by a Scout to acquire a lease for a Proposer
  case class P1a(ballotNum: BallotNumber)
  // Sent by a Commander to request a vote on a value
  case class P2a(ballotNum: BallotNumber, slot: Slot, value: Value)
}

class Acceptor extends Actor with ActorLogging {
  import Acceptor._

  val mediator = DistributedPubSub(context.system).mediator
  mediator ! Subscribe("acceptor", self)

  override def receive: Receive =
    receiveWithState(BallotNumber.bottom, HashMap[Slot, (BallotNumber, Value)]())

  def receiveWithState(ballotNum: BallotNumber, accepted: AcceptedMap): Receive = {
    case P1a(bn) => {
      // Update the ballotNum and respond with the accepted values and ballotNum
    }
    case P2a(bn, slot, value) => {
      // Update the accepted map with this new proposal if bn >= ballotNum and respond with
      // ballotNum
    }
    case SubscribeAck(Subscribe("acceptor", None, `self`)) =>
      log.info("Subscribed to acceptor messages")
    case msg => log.debug(msg.toString)
  }
}
