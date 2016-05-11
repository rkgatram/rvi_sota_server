package org.genivi.sota.core

import java.util.UUID

import akka.event.Logging
import akka.http.scaladsl.model.Uri.Path
import akka.http.scaladsl.model.{StatusCodes, Uri}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import io.circe.Encoder
import io.circe.generic.auto._
import org.genivi.sota.core.resolver.{ConnectivityClient, DefaultConnectivity}
import org.genivi.sota.core.data.UpdateSpec
import org.genivi.sota.core.data.client.ClientUpdateRequest
import org.genivi.sota.core.transfer.DefaultUpdateNotifier
import org.genivi.sota.marshalling.CirceMarshallingSupport
import org.joda.time.{DateTime, Interval}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{FunSuite, ShouldMatchers}
import akka.http.scaladsl.unmarshalling._


import scala.concurrent.Future

class UpdateRequestResourceSpec extends FunSuite
  with ScalatestRouteTest
  with ShouldMatchers
  with UpdateResourcesDatabaseSpec
  with ScalaFutures
  with DatabaseSpec
  with DefaultDBPatience {

  import CirceMarshallingSupport._
  import UpdateSpec._

  implicit val log = Logging(system, "UpdateRequestResourceSpec")

  val resolver = new FakeExternalResolver()

  implicit val rviClient = new ConnectivityClient {
    override def sendMessage[A](service: String, message: A, expirationDate: _root_.com.github.nscala_time.time.Imports.DateTime)(implicit encoder: Encoder[A]): Future[Int] = ???
  }

  implicit val connectivity = DefaultConnectivity

  val serve = new UpdateRequestsResource(db, resolver, new UpdateService(DefaultUpdateNotifier))

  test("accepts new updates with a Client specific format") {
    val now = DateTime.now
    val f = createUpdateSpec()

    whenReady(f) { case (packageModel, _, _) =>
      val req = ClientUpdateRequest(
        UUID.randomUUID(),
        packageModel.id,
        now,
        new Interval(now, now.plusDays(1)),
        10,
        "none",
        None,
        requestConfirmation = false
      )

      val uri = Uri.Empty.withPath(Path(s"/update_requests"))

      Post(uri, req) ~> serve.route ~> check {
        status shouldBe StatusCodes.OK

        val spec = responseAs[List[UpdateSpec]]
        spec should be(empty)
      }
    }
  }
}