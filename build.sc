import mill._, scalalib._, scalajslib._

object main extends RootModule with ScalaJSModule {

  def scalaVersion = "3.3.1"

  def scalaJSVersion = "1.14.0"

  def ivyDeps = Agg(
    ivy"io.github.iltotore::iron::2.3.0-RC2",
    ivy"io.indigoengine::tyrian::0.8.0",
    ivy"io.indigoengine::tyrian-io::0.8.0"
  )

  def moduleKind = T(mill.scalajslib.api.ModuleKind.ESModule)
}