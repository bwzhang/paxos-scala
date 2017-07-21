import akka.actor.{Actor, ActorLogging, Props}
import akka.cluster.client.ClusterClientReceptionist
import types._
import scala.collection.immutable.HashMap
import akka.cluster.pubsub.DistributedPubSub
import akka.cluster.pubsub.DistributedPubSubMediator.{Publish, Subscribe, SubscribeAck}

object Replica {
  def props: Props = Props[Replica]
  // Write request from client
  case class Write(value: Value)
  // Message from a Commander indicating the decision value for a given slot
  case class Decision(slot: Slot, value: Value)
}

class Replica extends Actor with ActorLogging {
  import Replica._

  val mediator = DistributedPubSub(context.system).mediator
  mediator ! Subscribe("replica", self)

  override def preStart(): Unit = {
    // Receptionist for client communication
    ClusterClientReceptionist(context.system).registerService(self)
  }

  override def receive: Receive =
    receiveWithState(0, HashMap[Slot, Value](), HashMap[Slot, Value]())

  def receiveWithState(nextSlot: Slot,
                       proposals: SlotValueMap,
                       decisions: SlotValueMap): Receive = {
    case Write(value) => {
      // Add the value to the proposals map with the next available slot
      // Update the next available slot
    }
    case Decision(slot, value) => {
      // Add the decision to the decisions map and update nextSlot and proposals if necessary
    }
    case SubscribeAck(Subscribe("replica", None, `self`)) =>
      log.info("Subscribed to replica messages")
    case msg => log.debug(msg.toString)
  }
}
