import java.util.concurrent.TimeUnit._

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorSystem, Behavior}
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.webapp.WebAppContext
import org.scalatra.servlet.ScalatraListener

import scala.concurrent.duration.FiniteDuration


object ActorMain {

  final case class Init()

  final val interval = FiniteDuration(2, MINUTES)

  def apply(): Behavior[Init] =
    Behaviors.setup { context =>

      var counter = 1

      Config.sites.foreach({ conf =>
        val checker = context.spawn(SiteChecker(interval), "checker" + counter.toString)
        checker ! SiteChecker.Check(conf.url)
        counter += 1
      })

      Behaviors.empty
    }
}

object Main {
  def main(args: Array[String]) {

    val _: ActorSystem[ActorMain.Init] = ActorSystem(ActorMain(), "ActorMain")

    val port = if (System.getenv("PORT") != null) System.getenv("PORT").toInt else 8080

    val server = new Server(port)
    val context = new WebAppContext()

    context setContextPath "/"
    context.setResourceBase("src/main/webapp")
    context.setInitParameter(ScalatraListener.LifeCycleKey, "ScalatraBootstrap")
    context.addEventListener(new ScalatraListener)

    server.setHandler(context)

    server.start()
    server.join()
  }
}
