import akka.actor.{ActorPath, ActorSystem}
import akka.cluster.client.{ClusterClient, ClusterClientSettings}
import com.typesafe.config.ConfigFactory
import scopt.OptionParser

object Client {
  case class Config(hostname: String = "127.0.0.1", port: Int = 0)

  val parser = new OptionParser[Config]("paxos-client") {
    opt[String]('h', "hostname").action((hostname, config) => config.copy(hostname = hostname))
      .text("IP address or resolvable name of the machine the process is on")
    opt[Int]('p', "port").action((port, config) => config.copy(port = port))
      .text("the port the actor system should listen on")
    help("help")
  }

  def main(args: Array[String]): Unit = {
    parser.parse(args, Config()) match {
      case Some(config) => run(config.hostname, config.port)
      case None => Unit
    }
  }

  def run(hostname: String, port: Int): Unit = {
    val config = ConfigFactory.parseString(
      s"""
         |akka.remote.netty.tcp {
         |  hostname=$hostname
         |  port=$port
         |}
         |""".stripMargin).withFallback(ConfigFactory.load("client.conf"))

    val system = ActorSystem("PaxosClient", config)

    val initialContacts = Set(
      ActorPath.fromString("akka.tcp://PaxosServer@127.0.0.1:20000/system/receptionist"))
    val settings = ClusterClientSettings(system)
      .withInitialContacts(initialContacts)
    val client = system.actorOf(ClusterClient.props(settings), "client")

    client ! ClusterClient.Send("/user/replica", "hello", localAffinity = true)
  }
}
