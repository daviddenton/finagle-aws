package io.github.daviddenton.aws

import io.github.daviddenton.aws.AwsHmacSha256.hash
import io.github.daviddenton.finagle.aws.AwsCanonicalRequest

class AwsStringToSign(canonicalRequest: AwsCanonicalRequest, requestScope: AwsCredentialScope, requestDate: AwsRequestDate) {
  private val stringToSign =
    s"""${AwsStringToSign.ALGORITHM}
       |${requestDate.full}
       |${requestScope.awsCredentialScope(requestDate)}
       |${hash(canonicalRequest.toString)}""".stripMargin

  override def toString: String = stringToSign
}

object AwsStringToSign {
  val ALGORITHM = "AWS4-HMAC-SHA256"
}