package io.github.daviddenton.finagle.aws

import io.github.daviddenton.finagle.aws.AwsHmacSha256.hmacSHA256

case class AwsSignatureV4Signer(scope: AwsCredentialScope, credentials: AwsCredentials) {

  def authHeader(canonicalRequest: AwsCanonicalRequest, date: AwsRequestDate): String =
    s"""${AwsStringToSign.ALGORITHM} Credential=${credentials.accessKey}/${scope.awsCredentialScope(date)}, SignedHeaders=${canonicalRequest.signedHeaders}, Signature=${sign(canonicalRequest, date)}""".stripMargin

  private def sign(request: AwsCanonicalRequest, date: AwsRequestDate): String = {
    val awsStringToSign = new AwsStringToSign(request, scope, date)
    val signatureKey = getSignatureKey(credentials.secretKey, date.basic, scope.region, scope.service)
    val signature = hmacSHA256(signatureKey, awsStringToSign.toString)
    AwsHmacSha256.hex(signature)
  }

  private def getSignatureKey(key: String, dateStamp: String, regionName: String, serviceName: String) = {
    val encodedSecret = ("AWS4" + key).getBytes("UTF8")
    val encodedDate = hmacSHA256(encodedSecret, dateStamp)
    val encodedRegion = hmacSHA256(encodedDate, regionName)
    val encodedService = hmacSHA256(encodedRegion, serviceName)
    hmacSHA256(encodedService, "aws4_request")
  }
}
