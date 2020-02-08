import java.sql.Timestamp

import slick.jdbc.H2Profile.api._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

object Store {
  val pingEvents = TableQuery[PingEvents]

  private val _db = Database.forURL("jdbc:h2:file:./status", driver = "org.h2.Driver", keepAliveConnection = true)
  implicit val ec: ExecutionContext = ExecutionContext.global

  Await.ready(_db.run(
    DBIO.seq(
      pingEvents.schema.create,
    )
  ), Duration.Inf)

  def +=(e: Object): Future[Int] = e match {
    case p: PingEvent => _db.run(pingEvents += p)
    case _ => throw new IllegalArgumentException("Unknown type")
  }

  def events(url: String, start: Long, end: Long): Future[Seq[PingEvent]] = {
    _db.run(
      pingEvents
        .sortBy(_.ts.desc)
        .filter(e => e.url === url && e.ts >= new Timestamp(start) && e.ts <= new Timestamp(end))
        .result
    )
  }
}

case class PingEvent(url: String, ts: Timestamp, latency: Int, status: String)


class PingEvents(tag: Tag) extends Table[PingEvent](tag, "PingEvent") {
  def ts = column[Timestamp]("ts")

  def latency = column[Int]("latency")

  def status = column[String]("status")

  def url = column[String]("url")

  def * = (url, ts, latency, status) <> (PingEvent.tupled, PingEvent.unapply)
}
