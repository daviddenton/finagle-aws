package io.github.daviddenton.finagle.aws

import java.net.URI
import java.time.Clock

import com.twitter.finagle.http.{Request, Response}
import com.twitter.finagle.{Http, Service}

object S3 {

  private val awsDomain = "s3.amazonaws.com"

  def client(region: AwsRegion, credentials: AwsCredentials, clock: Clock = Clock.systemUTC(),
             http: Service[Request, Response] = Http.client
               .withTls(awsDomain).newService(s"$awsDomain:443")): Service[Request, Response] = {
    val signatureV4Signer = AwsSignatureV4Signer(AwsCredentialScope(region, AwsService("s3")), credentials)

    AwsCredentialFilter(awsDomain, clock, signatureV4Signer)
      .andThen(http)
  }

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

    case class Key(value: String) extends AnyVal

  }

}
