package io.github.daviddenton.finagle.aws

import io.github.daviddenton.finagle.aws.AwsHmacSha256.hmacSHA256

case class AwsSignatureV4Signer(scope: AwsCredentialScope, credentials: AwsCredentials) {

  def authHeader(canonicalRequest: AwsCanonicalRequest, date: AwsRequestDate): String =
    s"""${AwsStringToSign.ALGORITHM} Credential=${credentials.accessKey}/${scope.awsCredentialScope(date)}, SignedHeaders=${canonicalRequest.signedHeaders}, Signature=${sign(canonicalRequest, date)}""".stripMargin

  private def sign(request: AwsCanonicalRequest, date: AwsRequestDate): String = {
    val awsStringToSign = new AwsStringToSign(request, scope, date)
    val signatureKey = getSignatureKey(credentials.secretKey, date.basic)
    val signature = hmacSHA256(signatureKey, awsStringToSign.toString)
    AwsHmacSha256.hex(signature)
  }

  private def getSignatureKey(key: String, dateStamp: String) = {
    val encodedSecret = ("AWS4" + key).getBytes("UTF8")
    val encodedDate = hmacSHA256(encodedSecret, dateStamp)
    val encodedRegion = hmacSHA256(encodedDate, scope.region.name)
    val encodedService = hmacSHA256(encodedRegion, scope.service.name)
    hmacSHA256(encodedService, "aws4_request")
  }
}
