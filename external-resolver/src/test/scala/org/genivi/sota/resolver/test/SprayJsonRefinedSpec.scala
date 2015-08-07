package org.genivi.sota.resolver.test

import org.genivi.sota.refined.SprayJsonRefined
import org.scalatest.{WordSpec, Matchers}

class SprayJsonRefinedSpec extends WordSpec with Matchers {

  def provide = afterWord("provide")

  import eu.timepit.refined._
  import shapeless.tag
  import shapeless.tag.@@
  import spray.json._
  import DefaultJsonProtocol._
  import SprayJsonRefined._

  "SprayJsonRefined" should {
    "format strings" in {
      import eu.timepit.refined.string._
      import eu.timepit.refined.collection._
      val value : String @@ NonEmpty = tag[NonEmpty]("str")

      value.toJson shouldBe JsString("str")

      JsString("str").convertTo[String @@ NonEmpty] shouldBe value
    }

    "format ints" in {
      import eu.timepit.refined.numeric._
      import eu.timepit.refined.boolean._
      import shapeless.nat._

      type GrZeroLessTwo = Greater[_0] And Less[_2]
      val value : Int @@ GrZeroLessTwo = tag[GrZeroLessTwo](1)

      value.toJson shouldBe JsNumber(1)
      JsNumber(1).convertTo[Int @@ GrZeroLessTwo] shouldBe value
    }

    "fail if predicate fails" in {
      import eu.timepit.refined.numeric._
      import eu.timepit.refined.boolean._
      import shapeless.nat._

      type GrZeroLessTwo = Greater[_0] And Less[_2]
      val exception = the [DeserializationException] thrownBy JsNumber(3).convertTo[Int @@ GrZeroLessTwo]
      exception.getCause() shouldBe a [RefinmentError[_]]
    }

    "format case classes" in {
      import eu.timepit.refined.collection._
      import eu.timepit.refined.numeric._
      import eu.timepit.refined.boolean._
      import shapeless.nat._

      case class Inner( str: String @@ NonEmpty, number: Int @@ Greater[_0] )
      implicit val InnerForamat = jsonFormat2(Inner)

      case class Outer( yesNo: Boolean, inner: Inner )
      implicit val OuterFormat = jsonFormat2(Outer)

      val expectedJson = JsObject(
        "yesNo" -> JsBoolean(true),
        "inner" -> JsObject(
          "str" -> JsString("str"),
          "number" -> JsNumber(2)
        )
      )
        

      val value = Outer( true, Inner(tag[NonEmpty]("str"), tag[Greater[_0]](2)))
      expectedJson.convertTo[Outer] shouldBe value

      value.toJson shouldBe expectedJson
    }
  }


}
