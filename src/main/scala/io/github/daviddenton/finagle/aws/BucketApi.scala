package io.github.daviddenton.finagle.aws

import java.net.URI
import java.time.Clock

import com.twitter.finagle.http.{Request, Response}
import com.twitter.finagle.{Http, Service}
import com.twitter.io.Buf
import com.twitter.util.Future

trait BucketApi {
  def keys(): Future[Seq[Key]]

  def apply(key: Key): Future[Option[Buf]]

  def +=(pair: (Key, Buf)): Future[Option[Key]]

  def -=(key: Key): Future[Option[Key]]

}

object BucketApi {
//  def apply(bucket: Bucket, http: Service[Request, Response]): BucketApi = new BucketApi {
//
//    override def keys(): Future[Seq[Key]] = {
//      val request = Request("/")
//      request.headerMap("Accept", "application/json")
//      http(request).map()
//    }
//
//    override def apply(key: Key): Future[Option[Buf]] = ???
//
//    override def +=(pair: (Key, Buf)): Future[Option[Key]] = ???
//
//    override def -=(key: Key): Future[Option[Key]] = ???
//  }
//
//  private val api = BucketApi()
//  api -= Key("asd")
}


case class Key(value: String) extends AnyVal

case class Bucket(name: String) extends AnyVal {
  def toUri = URI.create(s"https://$name.s3.amazonaws.com")
}

object Bucket {
  def client(bucket: Bucket, region: AwsRegion, credentials: AwsCredentials,
             clock: Clock = Clock.systemUTC()): Service[Request, Response] = {
    val signatureV4Signer = AwsSignatureV4Signer(AwsCredentialScope(region, AwsService("s3")), credentials)
    val authority = bucket.toUri.getAuthority

    val http = Http.client.withTls(authority)
      .newService(s"$authority:443")
    AwsCredentialFilter(authority, clock, signatureV4Signer)
      .andThen(http)
  }
}