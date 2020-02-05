import java.sql.Timestamp

import org.openqa.selenium.InvalidArgumentException
import slick.jdbc.H2Profile.api._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

object Store {
  val pingEvents = TableQuery[PingEvents]

  private val _db = Database.forURL("jdbc:h2:file:./status", driver = "org.h2.Driver", keepAliveConnection = true)
  implicit val ec: ExecutionContext = ExecutionContext.global

  Await.ready(_db.run(
    DBIO.seq(
      Store.pingEvents.schema.create,
    )
  ), Duration.Inf)

  def +=(e: Object): Future[Int] = e match {
    case PingEvent(url, ts, latency, status) => _db.run(pingEvents += (url, ts, latency, status))
    case _ => throw new InvalidArgumentException("Unknown type")
  }

  def events(url: String): Seq[PingEvent] = {
    val q = for {
      e <- pingEvents if e.url === url
    } yield (e.ts, e.latency, e.status)

    Await.result(_db.run(q.result), Duration.Inf)
  }
}

case class PingEvent(url: String, ts: Timestamp, latency: Int, status: String)


class PingEvents(tag: Tag) extends Table[(String, Timestamp, Int, String)](tag, "PingEvent") {
  def ts = column[Timestamp]("ts")

  def latency = column[Int]("latency")

  def status = column[String]("status")

  def url = column[String]("url")

  def * = (url, ts, latency, status)
}
