package io.github.daviddenton.finagle.aws

import com.twitter.finagle.Service
import com.twitter.finagle.http.Method.{Delete, Get, Put}
import com.twitter.finagle.http.Status.{NoContent, NotFound, Ok}
import com.twitter.finagle.http.{Request, Response}
import com.twitter.util.Future

import scala.xml.XML

object S3Api {
  def apply(client: Service[Request, Response]): S3Api = new S3Api {
    override def list(): Future[Seq[Bucket]] = client(Request(Get, "/")).map(_.contentString).map(XML.loadString).map(S3.buckets)

    override def +=(bucket: Bucket): Future[Bucket] = {
      val request = Request(Put, "/" + bucket.name)
      client(request).flatMap {
        rsp =>
          rsp.status match {
            case Ok => Future(bucket)
            case _ => Future.exception(new IllegalArgumentException("AWS returned " + rsp.status))
          }
      }
    }

    override def -=(bucket: Bucket): Future[Option[Bucket]] =
      client(Request(Delete, "/" + bucket.name)).flatMap {
        rsp =>
          rsp.status match {
            case NoContent => Future(Option(bucket))
            case NotFound => Future(None)
            case _ => Future.exception(new IllegalArgumentException("AWS returned " + rsp.status))
          }
      }
  }
}

trait S3Api {
  def list(): Future[Seq[Bucket]]

  def +=(bucket: Bucket): Future[Bucket]

  def -=(bucket: Bucket): Future[Option[Bucket]]
}