import akka.actor.{Actor, ActorLogging, Props}
import akka.cluster.client.ClusterClientReceptionist

object Replica {
  def props: Props = Props[Replica]
}

class Replica extends Actor with ActorLogging {
  override def preStart(): Unit = {
    // Receptionist for client communication
    ClusterClientReceptionist(context.system).registerService(self)
  }

  override def receive = {
    case msg => log.debug(msg.toString)
  }
}
