import java.sql.Timestamp

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.{Behaviors, TimerScheduler}
import scalaj.http.{Http, HttpResponse}

import scala.concurrent.duration.FiniteDuration

object SiteChecker {

  final case class Check(url: String, script: String)

  def apply(interval: FiniteDuration): Behavior[Check] = {
    Behaviors.setup { context =>
      Behaviors.withTimers(timers => new SiteChecker(timers, interval).idle())
    }
  }
}

class SiteChecker(timers: TimerScheduler[SiteChecker.Check], interval: FiniteDuration) {

  private def idle(): Behavior[SiteChecker.Check] = {
    Behaviors.receiveMessage[SiteChecker.Check] { message =>
      timers.startTimerAtFixedRate(message, interval)
      active()
    }
  }

  private def active(): Behavior[SiteChecker.Check] = {
    Behaviors.receiveMessage[SiteChecker.Check] { message =>
      Store += check(message.url, message.script)
      active()
    }
  }

  def check(url: String, script: String): PingEvent = {

    val start = System.currentTimeMillis()
    var result = ""

    try {
      val res: HttpResponse[Array[Byte]] = Http(url).asBytes

      if (res.is2xx) {
        result = "ok"
      } else {
        result = "http" + res.code.toString
      }

    } catch {
      //TODO: Handle/log exception
      case e: Exception =>
        println(e)
        result = "err"
    }

    PingEvent(url, new Timestamp(start), (System.currentTimeMillis() - start).toInt, result)
  }
}