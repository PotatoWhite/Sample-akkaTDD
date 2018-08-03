package potato

import akka.actor.{Actor, ActorRef, Props}

object SendingActor{
  def props(receiver:ActorRef) = Props(new SendingActor(receiver))

  case class Event(id:Long)
  case class SortEvent(unsorted:Vector[Event])
  case class SortedEvent(sorted:Vector[Event])

}

class SendingActor(receiver:ActorRef) extends Actor{
  import SendingActor._

  override def receive: Receive = {
    case SortEvent(unsorted) =>
      val sorted = unsorted.sortBy(_.id)
      receiver ! SortedEvent(sorted)
  }
}
