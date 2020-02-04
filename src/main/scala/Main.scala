import scala.concurrent.Await
import scala.concurrent.duration.Duration

object Main extends App {

  val checker = new SiteChecker("https://simon987.net", "");

  println(Await.ready(Store += checker.check(), Duration.Inf))
  println(Await.ready(Store += checker.check(), Duration.Inf))
}
