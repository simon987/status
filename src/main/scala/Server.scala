import javax.servlet.ServletContext
import org.json4s.{DefaultFormats, Formats}
import org.scalatra._
import org.scalatra.json._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {
    context.mount(new Server, "/*")
  }
}

case class AppInfo(name: String, version: String)

class Server extends ScalatraServlet with JacksonJsonSupport {

  private val _index: AppInfo = AppInfo("status", "0.1")

  protected implicit val jsonFormats: Formats = DefaultFormats

  before() {
    contentType = formats("json")
  }

  get("/api/") {
    _index
  }

  get("/api/sites") {
    Config.sites
  }

  get("/api/events/:site") {
    val start = params("start").toLong * 1000
    val end = params("end").toLong * 1000
    val site = Config.sites(params("site").toInt)

    Await.result(Store.events(site.url, start, end), Duration.Inf)
  }
}
