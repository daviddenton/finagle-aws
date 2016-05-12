package io.github.daviddenton.finagle.aws

import com.twitter.finagle.http.Method.{Get, Put}
import com.twitter.finagle.http.Request
import org.jboss.netty.handler.codec.http.QueryStringEncoder
import org.scalatest.{FunSpec, ShouldMatchers}

class AwsCanonicalRequestTest extends FunSpec with ShouldMatchers {

  describe("AwsCanonicalRequest") {

    it("transform minimal request") {
      val expected = "GET\n" +
        "/test\n" +
        "\n" +
        "content-length:0\n\n" +
        "content-length\n" +
        "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855"

      val request = Request(Get, "/test")
      request.contentLength = 0

      AwsCanonicalRequest(request).toString shouldBe expected
    }

    it("transform full request") {
      val uri = new QueryStringEncoder("/put-path")
      uri.addParam("z-param", "z value")
      uri.addParam("a-param", "a value")
      uri.addParam("S-param", "s value")

      val request = Request(Put, uri.toString)
      request.headerMap.add("z-header", "  a   value  ")
      request.headerMap.add("a-header", "  another value  ")
      request.contentString = "hello world"
      request.contentLength = request.contentString.length

      AwsCanonicalRequest(request).toString shouldBe "PUT\n" +
        "/put-path\n" +
        "S-param=s+value&a-param=a+value&z-param=z+value\n" +
        "a-header:another value\n" +
        "content-length:11\n" +
        "z-header:a value\n" + "\n" +
        "a-header;content-length;z-header\n" +
        "b94d27b9934d3e08a52e52d7da7dabfac484efe37a5380ee9088f7ace2efcde9"
    }
  }
}
