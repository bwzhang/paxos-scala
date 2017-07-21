import akka.actor.{Actor, ActorLogging, Props}
import akka.cluster.pubsub.DistributedPubSub
import akka.cluster.pubsub.DistributedPubSubMediator.{Publish, Subscribe, SubscribeAck}
import types._
import scala.collection.immutable.HashMap

object Proposer {
  def props: Props = Props[Proposer]
  // Message from Replica requesting a proposal
  case class Propose(slot: Int, value: Value)
  // Message from Scout indicated that the Proposer has been adopted by a majority of acceptors.
  // The accepted map contains the highest ballot number value accepted for each slot
  case class Adopted(acceptedProposals: SlotValueMap)
  // Message from Scout or Commander indicating that a higher ballot number was encountered among
  // the Acceptors
  case class Preempted(ballotNum: BallotNumber)
}

class Proposer extends Actor with ActorLogging {
  import Proposer._

  val mediator = DistributedPubSub(context.system).mediator
  mediator ! Subscribe("proposer", self)

  override def receive: Receive =
    receiveWithState(false, BallotNumber.bottom, HashMap[Slot, Value]())

  def receiveWithState(active: Boolean,
                       ballotNum: BallotNumber,
                       proposals: SlotValueMap): Receive = {
    case Propose(slot, value) => {
      // Update the proposals map with this proposal and start a Commander if active
    }
    case Adopted(acceptedProposals) => {
      // Update proposals to match the values accepted with the highest ballot numbers
      // Start a Commander for each proposal
    }
    case Preempted(bn) => {
      // Start new Scout and set state to inactive
    }
    case SubscribeAck(Subscribe("proposer", None, `self`)) =>
      log.info("Subscribed to proposer messages")
    case msg => log.debug(msg.toString)
  }
}
