package potato

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import org.slf4j.LoggerFactory

object SideEffectingActor {
  def props = Props(new SideEffectingActor)

  case class Greeting(message: String)

}

class SideEffectingActor extends Actor with ActorLogging {

  import SideEffectingActor._

  override def receive: Receive = {
    case Greeting(message) =>
      log.info(s"Hello $message!")

  }
}
