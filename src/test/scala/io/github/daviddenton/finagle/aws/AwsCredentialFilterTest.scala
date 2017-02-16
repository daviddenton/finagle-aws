package io.github.daviddenton.finagle.aws

import java.time.{Clock, Instant, ZoneId}

import com.twitter.finagle.Service
import com.twitter.finagle.http.Method.Get
import com.twitter.finagle.http.{Request, Response}
import com.twitter.util.Await.result
import com.twitter.util.Future
import org.jboss.netty.handler.codec.http.HttpHeaders.Names
import org.scalatest.{FunSpec, Matchers}

class AwsCredentialFilterTest extends FunSpec with Matchers {

  private val clock = Clock.fixed(Instant.ofEpochMilli(0), ZoneId.of("UTC"))

  private val filter = AwsCredentialFilter("somehost", clock,
    AwsSignatureV4Signer(AwsCredentialScope(AwsRegion.usEast, AwsService.s3), AwsCredentials("access", "secret")))

  private val mirrorHeaders = Service.mk {
    req: Request =>
      val response = Response()
      req.headerMap.foreach(response.headerMap += _)
      Future.value(response)
  }

  describe("AwsCredentialFilter") {
    it("adds host header") {
      result(filter(Request(Get, "/test"), mirrorHeaders)).headerMap(Names.HOST) shouldBe "somehost"
    }

    it("adds auth header") {
      result(filter(Request(Get, "/test"), mirrorHeaders)).headerMap(Names.AUTHORIZATION) shouldBe
        "AWS4-HMAC-SHA256 Credential=access/19700101/us-east/s3/aws4_request, SignedHeaders=content-length;host;x-amz-date, Signature=d7690ffa429d19877081491d5a325f564782998a3369a311a3dfcaa8cdeb9ecc"
    }

    it("adds time header") {
      result(filter(Request(Get, "/test"), mirrorHeaders)).headerMap(AwsHeaders.DATE) shouldBe "19700101T010000Z"
    }

    it("adds content sha256") {
      result(filter(Request(Get, "/test"), mirrorHeaders)).headerMap(AwsHeaders.CONTENT_SHA256) shouldBe "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855"
    }
  }
}
