package potato

import akka.actor.AbstractActor.Receive
import akka.actor.{Actor, ActorRef, Props}
import potato.FilteringActor.Event

object FilteringActor {
  def props(nextActor: ActorRef, bufferSize: Int) = Props(new FilteringActor(nextActor, bufferSize))

  case class Event(id: Long)

}

class FilteringActor(nextActor: ActorRef, bufferSize: Int) extends Actor {

  import FilteringActor._

  var lastMessage = Vector.empty[Event]

  override def receive: Receive = {
    case message: Event =>
      if (!lastMessage.contains(message)) {
        lastMessage = lastMessage :+ message
        nextActor ! message

        if (lastMessage.size > bufferSize)
          lastMessage = lastMessage.tail
      }
  }
}
