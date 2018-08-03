package potato

import akka.actor.{ActorSystem, Props}
import akka.testkit.{CallingThreadDispatcher, EventFilter, TestKit}
import com.typesafe.config.ConfigFactory
import org.scalatest.{MustMatchers, WordSpecLike}
import util.StopSystemAfterAll

object SideEffectingActorSpec {
  val testSystem = {
    val config = ConfigFactory.parseString(
      """
        |akka.loggers=[akka.testkit.TestEventListener]
      """.stripMargin
    )

    ActorSystem("testSystem", config)
  }
}

class SideEffectingActorSpec extends TestKit(SideEffectingActorSpec.testSystem) with WordSpecLike with StopSystemAfterAll {

  import SideEffectingActor._

  "Side Effecting Actor" must {
    "Say Hello MESSAGE on Logger when it received a message " in {
      val dispatcherId = CallingThreadDispatcher.Id
      val props = Props[SideEffectingActor].withDispatcher(dispatcherId)
      val sideEffectingActor = system.actorOf(props)

      EventFilter.info(message = "Hello World!", occurrences = 1).intercept {
        sideEffectingActor ! Greeting("World")
      }
    }
  }

}


