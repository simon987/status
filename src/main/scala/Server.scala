import javax.servlet.ServletContext
import org.json4s.{DefaultFormats, Formats}
import org.scalatra._
import org.scalatra.json._

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {
    context.mount(new MyScalatraServlet, "/*")
  }
}

case class Hello(name: String)

class MyScalatraServlet extends ScalatraServlet with JacksonJsonSupport {

  protected implicit val jsonFormats: Formats = DefaultFormats

  before() {
    contentType = formats("json")
  }

  get("/") {
    Hello("scalatra")
  }
}
