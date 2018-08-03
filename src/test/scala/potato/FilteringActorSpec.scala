package potato

import akka.actor.ActorSystem
import akka.testkit.TestKit
import akka.util.Timeout
import org.scalatest.{MustMatchers, WordSpecLike}
import util.StopSystemAfterAll

import scala.concurrent.duration.{Duration, FiniteDuration}

class FilteringActorSpec extends TestKit(ActorSystem("testSystem")) with WordSpecLike with MustMatchers with StopSystemAfterAll {
  "Filtering Actor must" must {
    "filter out particular messages" in {
      import FilteringActor._

      val props = FilteringActor.props(testActor, 5)
      val filter = system.actorOf(props, "Filter-1")

      filter ! Event(1)
      filter ! Event(2)
      filter ! Event(1)
      filter ! Event(3)
      filter ! Event(1)
      filter ! Event(4)
      filter ! Event(5)
      filter ! Event(5)
      filter ! Event(6)

      val eventIds = receiveWhile() {
        case Event(id) if (id <= 5) => id
      }

      eventIds must be(List(1, 2, 3, 4, 5))
      expectMsg(Event(6))
    }

    "filter out particular messages using expectMsg" in {
      import FilteringActor._
      import akka.pattern._

      val maxTimeoutDuration = Duration("1s")
      implicit val timeout:Timeout = FiniteDuration(maxTimeoutDuration.length, maxTimeoutDuration.unit)


      val props = FilteringActor.props(testActor, 5)
      val filter = system.actorOf(props, "filter-2")

      filter ! Event(1)
      filter ! Event(2)
      expectMsg(Event(1))
      expectMsg(Event(2))

      filter ! Event(1)
      expectNoMessage()

      filter ! Event(3)
      expectMsg(Event(3))

      filter ! Event(4)
      filter ! Event(5)
      filter.ask(Event(5))
      expectMsg(Event(4))
      expectMsg(Event(5))
      expectNoMessage()


    }
  }

}
