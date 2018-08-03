package potato

import akka.actor.{Actor, ActorRef}
object SilentActor {
  case class SilentMessage(data:String)
  case class GetState(receiver:ActorRef)
}

class SilentActor extends Actor {
  import SilentActor._

  var internalState = Vector.empty[String]
  def state = internalState

  override def receive: Receive = {
    case SilentMessage(data) =>
      internalState = internalState :+ data
    case GetState(receiver) =>
      receiver ! state
  }
}
