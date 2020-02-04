import java.sql.Timestamp

import scalaj.http.{Http, HttpRequest}

class SiteChecker(url: String, script: String) {

  val request: HttpRequest = Http(url)

  //TODO: different implementation if script is not empty
  def check(): PingEvent = {

    val start = System.currentTimeMillis()

    try {
      //TODO: Check return code...
      val res = request.asBytes
      println(res.code)

      PingEvent(new Timestamp(start), (System.currentTimeMillis() - start).toInt, "ok")
    } catch {
      //TODO: Handle/log exception
      case e: Exception =>
        println(e)
        PingEvent(new Timestamp(start), (System.currentTimeMillis() - start).toInt, "err")
    }
  }
}
