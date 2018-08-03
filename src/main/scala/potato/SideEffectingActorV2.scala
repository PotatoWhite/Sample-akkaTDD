package potato

import akka.actor.{Actor, ActorLogging, ActorRef, Props}

object SideEffectingActorV2{
  def props(listener:Option[ActorRef] = None) = Props(new SideEffectingActorV2(listener))
}

class SideEffectingActorV2(listener:Option[ActorRef]) extends Actor with ActorLogging {

  import SideEffectingActor._

  override def receive: Receive = {
    case Greeting(who) =>
      val message = s"Hello $who!"
      log.info(message)
      listener.foreach(_ ! message)
  }
}
