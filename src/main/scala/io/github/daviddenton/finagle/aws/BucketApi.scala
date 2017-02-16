package io.github.daviddenton.finagle.aws

import com.twitter.finagle.Service
import com.twitter.finagle.http.Method.{Delete, Get}
import com.twitter.finagle.http.Status.{NoContent, NotFound, Ok}
import com.twitter.finagle.http.{Request, Response}
import com.twitter.io.Buf
import com.twitter.util.Future

import scala.xml.XML

trait BucketApi {
  def list(): Future[Seq[Key]]

  def apply(key: Key): Future[Option[Buf]]

  def +=(pair: (Key, Buf)): Future[Key]

  def -=(key: Key): Future[Option[Key]]
}


object BucketApi {
  def apply(bucket: Bucket, client: Service[Request, Response]): BucketApi = new BucketApi {

    override def list(): Future[Seq[Key]] =
      client(Request(Get, "/")).map(_.contentString).map(XML.loadString).map(S3.keys)

    override def apply(key: Key): Future[Option[Buf]] =
      client(Request(Get, "/" + key.value)).flatMap {
        rsp =>
          rsp.status match {
            case Ok => Future(Option(rsp.content))
            case NotFound => Future(None)
            case _ => Future.exception(new IllegalArgumentException("AWS returned " + rsp.status))
          }
      }

    override def +=(keyAndContent: (Key, Buf)): Future[Key] = {
      val (key, content) = keyAndContent
      val request = Request(Get, "/" + key.value)
      request.content = content
      client(request).flatMap {
        rsp =>
          rsp.status match {
            case Ok => Future(key)
            case _ => Future.exception(new IllegalArgumentException("AWS returned " + rsp.status))
          }
      }
    }

    override def -=(key: Key): Future[Option[Key]] =
      client(Request(Delete, "/" + key.value)).flatMap {
        rsp =>
          rsp.status match {
            case NoContent => Future(Option(key))
            case NotFound => Future(None)
            case _ => Future.exception(new IllegalArgumentException("AWS returned " + rsp.status))
          }
      }
  }
}
