package io.github.daviddenton.finagle.aws

import java.time.{ZoneId, ZonedDateTime}

import com.twitter.finagle.http.Request
import org.scalatest.{FunSpec, ShouldMatchers}

class AwsStringToSignTest extends FunSpec with ShouldMatchers {

  describe("AwsStringToSign") {
    it("encodes correctly") {
      val request = AwsCanonicalRequest(Request("/test"))
      val date = AwsRequestDate(ZonedDateTime.of(2016, 1, 27, 15, 32, 50, 0, ZoneId.of("UTC")).toInstant)

      val stringToSign = new AwsStringToSign(request, AwsCredentialScope(AwsRegion("us-east"), AwsService("s3")), date)

      stringToSign.toString shouldBe "AWS4-HMAC-SHA256\n" +
        "20160127T153250Z\n" +
        "20160127/us-east/s3/aws4_request\n" +
        AwsHmacSha256.hash(request.toString)
    }
  }

}
