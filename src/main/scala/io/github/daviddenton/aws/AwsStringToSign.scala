package io.github.daviddenton.aws

import io.github.daviddenton.finagle.aws.AwsCanonicalRequest

class AwsStringToSign(canonicalRequest: AwsCanonicalRequest, requestScope: AwsCredentialScope, date: AwsRequestDate) {
  private val stringToSign = AwsStringToSign.ALGORITHM + "\n" + date.full + "\n" + requestScope.awsCredentialScope(date) + "\n" + AwsHmacSha256.hash(canonicalRequest.toString)

  override def toString: String = stringToSign
}

object AwsStringToSign {
  val ALGORITHM = "AWS4-HMAC-SHA256"
}