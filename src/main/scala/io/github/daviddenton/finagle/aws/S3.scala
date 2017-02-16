package io.github.daviddenton.finagle.aws

import java.time.Clock

import com.twitter.finagle.http.{Request, Response}
import com.twitter.finagle.{Http, Service}

import scala.xml.Elem

object S3 {

  private val awsDomain = "s3.amazonaws.com"

  def client(region: AwsRegion, credentials: AwsCredentials, clock: Clock = Clock.systemUTC(),
             http: Service[Request, Response] = Http.client
               .withTls(awsDomain).newService(s"$awsDomain:443")): Service[Request, Response] = {
    val signatureV4Signer = AwsSignatureV4Signer(AwsCredentialScope(region, AwsService("s3")), credentials)

    AwsCredentialFilter(awsDomain, clock, signatureV4Signer)
      .andThen(http)
  }

  def buckets(elem: Elem): Seq[Bucket] = {
    (elem \\ "ListAllMyBucketsResult" \ "Buckets" \ "Bucket" \ "Name").map(_.text).map(Bucket(_))
  }

  def keys(elem: Elem): Seq[Key] = (elem \\ "ListBucketResult" \ "Contents" \ "Key").map(_.text).map(Key)
}
