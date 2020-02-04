import java.io.FileReader

import cats.syntax.either._
import io.circe.generic.auto._
import io.circe.{ParsingFailure, yaml}


case class SiteConfigList(sites: List[SiteConfig])

case class SiteConfig(url: String, testScript: String) {

}

object Config {
  val sites: List[SiteConfig] = yaml.parser.parse(new FileReader("config.yml"))
    .leftMap(err => err: ParsingFailure)
    .flatMap(_.as[SiteConfigList])
    .valueOr(throw _).sites
}
