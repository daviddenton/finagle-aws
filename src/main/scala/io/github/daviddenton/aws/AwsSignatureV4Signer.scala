package io.github.daviddenton.aws

import java.io.UnsupportedEncodingException

import io.github.daviddenton.aws.AwsHmacSha256.hmacSHA256
import io.github.daviddenton.finagle.aws.AwsCanonicalRequest

case class AwsSignatureV4Signer(scope: AwsCredentialScope, credentials: AwsCredentials) {

  def sign(request: AwsCanonicalRequest, date: AwsRequestDate): String = {
    val awsStringToSign: AwsStringToSign = new AwsStringToSign(request, scope, date)
    val signatureKey = getSignatureKey(credentials.secretKey, date.basic, scope.region, scope.service)
    val signature = hmacSHA256(signatureKey, awsStringToSign.toString)
    AwsHmacSha256.hex(signature)
  }

  def authHeader(canonicalRequest: AwsCanonicalRequest, date: AwsRequestDate): String = {
    val signature: String = sign(canonicalRequest, date)
    String.format("%s Credential=%s/%s, SignedHeaders=%s, Signature=%s", AwsStringToSign.ALGORITHM, credentials.accessKey, scope.awsCredentialScope(date), canonicalRequest.signedHeaders, signature)
  }

  private def getSignatureKey(key: String, dateStamp: String, regionName: String, serviceName: String) = {
    try {
      val kSecret = ("AWS4" + key).getBytes("UTF8")
      val kDate = hmacSHA256(kSecret, dateStamp)
      val kRegion = hmacSHA256(kDate, regionName)
      val kService = hmacSHA256(kRegion, serviceName)
      hmacSHA256(kService, "aws4_request")
    }
    catch {
      case e: UnsupportedEncodingException => {
        throw new RuntimeException("Could not generate signature key", e)
      }
    }
  }
}
