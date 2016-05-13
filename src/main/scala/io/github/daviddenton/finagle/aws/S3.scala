package io.github.daviddenton.finagle.aws

import java.net.URI
import java.time.Clock

import com.twitter.finagle.http.{Request, Response}
import com.twitter.finagle.{Http, Service}

object S3 {
  def client(region: AwsRegion, credentials: AwsCredentials, clock: Clock = Clock.systemUTC()): Service[Request, Response] = {
    val signatureV4Signer = AwsSignatureV4Signer(AwsCredentialScope(region, AwsService("s3")), credentials)
    val authority = "s3.amazonaws.com"

    AwsCredentialFilter(authority, clock, signatureV4Signer)
      .andThen(Http.client.newService(s"$authority:80"))
  }

  case class Bucket(name: String) extends AnyVal {
    def toUri = URI.create(s"$name.s3.amazonaws.com")
  }

  object Bucket {
    def client(bucket: Bucket, region: AwsRegion, credentials: AwsCredentials, clock: Clock = Clock.systemUTC()): Service[Request, Response] = {
      val signatureV4Signer = AwsSignatureV4Signer(AwsCredentialScope(region, AwsService("s3")), credentials)
      val authority = bucket.toUri.getAuthority

      AwsCredentialFilter(authority, clock, signatureV4Signer)
        .andThen(Http.newService(s"$authority:80"))
    }

    case class Key(value: String) extends AnyVal
  }
}
