import akka.actor.{Actor, ActorLogging, Props}
import akka.cluster.pubsub.DistributedPubSub
import akka.cluster.pubsub.DistributedPubSubMediator.{Publish, Subscribe, SubscribeAck}

object Proposer {
  def props: Props = Props[Proposer]
}

class Proposer extends Actor with ActorLogging {
  val mediator = DistributedPubSub(context.system).mediator
  mediator ! Subscribe("proposer", self)

  override def receive = {
    case SubscribeAck(Subscribe("proposer", None, `self`)) =>
      log.info("Subscribed to proposer messages")
    case msg => log.debug(msg.toString)
  }
}
