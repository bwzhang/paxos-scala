import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import scopt.OptionParser

object Server {
  case class Config(hostname: String = "127.0.0.1", port: Int = 0)

  val parser = new OptionParser[Config]("paxos-server") {
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
         |""".stripMargin).withFallback(ConfigFactory.load("server.conf"))

    val system = ActorSystem("PaxosServer", config)
    val replica = system.actorOf(Replica.props, "replica")
    val proposer = system.actorOf(Proposer.props, "proposer")
    val acceptor = system.actorOf(Acceptor.props, "acceptor")
  }
}
