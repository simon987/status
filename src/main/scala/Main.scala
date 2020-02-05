import java.util.concurrent.TimeUnit._

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorSystem, Behavior}

import scala.concurrent.duration.FiniteDuration


object ActorMain {

  final case class Init()

  final val interval = FiniteDuration(5, SECONDS)

  def apply(): Behavior[Init] =
    Behaviors.setup { context =>

      var counter = 1

      Config.sites.foreach({ conf =>
        val checker = context.spawn(SiteChecker(interval), "checker" + counter.toString)
        checker ! SiteChecker.Check(conf.url, conf.testScript)
        counter += 1
      })

      Behaviors.empty
    }
}

object Main extends App {
  val greeterMain: ActorSystem[ActorMain.Init] = ActorSystem(ActorMain(), "ActorMain")
}
