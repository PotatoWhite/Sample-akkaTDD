package potato

import akka.actor.ActorSystem
import akka.event.Logging
import akka.testkit.TestKit
import org.scalatest.{MustMatchers, WordSpecLike}
import util.StopSystemAfterAll

import scala.util.Random

class SendingActorSpec extends TestKit(ActorSystem("testSystem")) with WordSpecLike with MustMatchers with StopSystemAfterAll {
  "A Sending Actor" must {
    "return sorted Vector when it receive SortEvent" in {
      import SendingActor._

      val log =  Logging(system.eventStream, "Ticket")
      val sendingActor = system.actorOf(props(testActor))

      val size = 1000
      val maxInclusive = 100000

      def randomEvent() = Vector.fill(size)(Event(Random.nextInt(maxInclusive)))

      val unsorted = randomEvent()
      log.debug(unsorted.mkString)

      val sortEvents = SortEvent(unsorted)
      sendingActor ! sortEvents

      expectMsgPF() {
        case SortedEvent(events) =>
          events.size must be(size)
          unsorted.sortBy(_.id) must be(events)
      }
    }
  }


}
