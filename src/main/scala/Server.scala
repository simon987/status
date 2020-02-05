import javax.servlet.ServletContext
import org.json4s.{DefaultFormats, Formats}
import org.scalatra._
import org.scalatra.json._

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
    val site = Config.sites(params("site").toInt)
    Store.events(site.url)
  }
}
