package io.github.daviddenton.finagle.aws

import java.net.URI
import java.time.Clock

import com.twitter.finagle.http.{Request, Response}
import com.twitter.finagle.{Http, Service}

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