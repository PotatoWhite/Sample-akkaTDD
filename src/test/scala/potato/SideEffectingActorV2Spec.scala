package potato

import akka.actor.{ActorSystem, UnhandledMessage}
import akka.japi.Option
import akka.testkit.TestKit
import org.scalatest.WordSpecLike
import potato.SideEffectingActor.Greeting
import util.StopSystemAfterAll

class SideEffectingActorV2Spec extends TestKit(ActorSystem("testSystem")) with WordSpecLike with StopSystemAfterAll{
  "the SideEffectingActorV2" must {
    val props = SideEffectingActorV2.props(Some(testActor))
    val sideEffectingActorV2 = system.actorOf(props,"SideEffectingActorV2")

    "say Hello World! when a Greeting(\"World\") is sent to it" in{

      sideEffectingActorV2 ! Greeting("World")
      expectMsg("Hello World!")
    }

    "say something else and see what happens" in {
      system.eventStream.subscribe(testActor, classOf[UnhandledMessage])
      sideEffectingActorV2 ! "World"
      expectMsg(UnhandledMessage("World", system.deadLetters, sideEffectingActorV2))
    }
  }

}
