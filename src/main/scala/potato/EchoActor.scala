package potato

import akka.actor.{Actor, ActorLogging}

class EchoActor extends Actor with ActorLogging{
  override def receive: Receive = {
    case message =>
      sender() ! message
  }
}
