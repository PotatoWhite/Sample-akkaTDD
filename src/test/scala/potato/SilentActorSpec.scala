package potato

import akka.actor.{ActorSystem, Props}
import akka.testkit.{TestActorRef, TestKit}
import org.scalatest.{MustMatchers, WordSpecLike}
import util.StopSystemAfterAll

class SilentActorSpec extends TestKit(ActorSystem("testSystem")) with WordSpecLike with MustMatchers with StopSystemAfterAll {
  "A Silent Actor" must {
    "change state when receive a message, single thread" in {
      import SilentActor._
      val testMessage = "Hello World"
      val silentActor = TestActorRef[SilentActor]
      silentActor ! SilentMessage(testMessage)
      silentActor.underlyingActor.internalState must (contain(testMessage))
    }

    "change state when receive a message, multi thread" in {
      import SilentActor._
      val testMessage1 = "Hello World"
      val testMessage2 = "Hello World"
      val silentActor = system.actorOf(Props[SilentActor], "s3")
      silentActor ! SilentMessage(testMessage1)
      silentActor ! SilentMessage(testMessage2)
      silentActor ! GetState(testActor)
      expectMsg(Vector(testMessage1, testMessage2))
    }
  }
}