import akka.actor.{Actor, ActorLogging, Props}
import akka.cluster.pubsub.DistributedPubSub
import akka.cluster.pubsub.DistributedPubSubMediator.{Subscribe, SubscribeAck}

object Acceptor {
  def props: Props = Props[Acceptor]
}

class Acceptor extends Actor with ActorLogging {
  val mediator = DistributedPubSub(context.system).mediator
  mediator ! Subscribe("acceptor", self)

  override def receive = {
    case SubscribeAck(Subscribe("acceptor", None, `self`)) =>
      log.info("Subscribed to acceptor messages")
    case msg => log.debug(msg.toString)
  }
}
