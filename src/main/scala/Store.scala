import java.sql.Timestamp

import org.openqa.selenium.InvalidArgumentException
import slick.jdbc.H2Profile.api._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

object Store {
  val pingEvents = TableQuery[PingEvents]

  private val _db = Database.forURL("jdbc:h2:file:./status", driver = "org.h2.Driver", keepAliveConnection = true)

  Await.ready(_db.run(
    DBIO.seq(
      Store.pingEvents.schema.create,
    )
  ), Duration.Inf)

  def +=(e: Object): Future[Int] = e match {
    case PingEvent(ts, latency, status) => _db.run(pingEvents += (ts, latency, status))
    case _ => throw new InvalidArgumentException("Unknown type")
  }
}

case class PingEvent(ts: Timestamp, latency: Int, status: String)


class PingEvents(tag: Tag) extends Table[(Timestamp, Int, String)](tag, "PingEvent") {
  def ts = column[Timestamp]("ts")

  def latency = column[Int]("latency")

  def status = column[String]("status")

  def site = column[String]("site")

  def * = (ts, latency, status)
}
