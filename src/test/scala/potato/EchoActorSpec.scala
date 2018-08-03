package potato

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.{MustMatchers, WordSpecLike}
import util.StopSystemAfterAll

class EchoActorSpec extends TestKit(ActorSystem("testSystem")) with WordSpecLike with ImplicitSender with StopSystemAfterAll {

  "Echo Actor" must {
    "Reply with the same message it receives without ask" in {
      val echo = system.actorOf(Props(new EchoActor), "test-echo-actor")

      echo ! "Hello World"
      expectMsg("Hello World")
    }

  }

}
