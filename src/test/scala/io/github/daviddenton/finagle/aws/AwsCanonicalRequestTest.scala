package io.github.daviddenton.finagle.aws

import com.twitter.finagle.http.Method.{Get, Put}
import com.twitter.finagle.http.Request
import org.scalatest.{FunSpec, ShouldMatchers}

class AwsCanonicalRequestTest extends FunSpec with ShouldMatchers {

  describe("AwsCanonicalRequest") {

    it("transform minimal request") {
      AwsCanonicalRequest(Request(Get, "/test")).toString shouldBe
        "GET\n" +
          "/test\n" +
          "\n" +
          "content-length:0\n\n" +
          "content-length\n" +
          "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855"
    }

    it("transform full request") {
      val request = Request(Put, "/put-path")
      request.headerMap.add("z-header", "  a   value  ")
      request.headerMap.add("a-header", "  another value  ")
      request.params + "z-param" -> "z value"
      request.params + "a-param" -> "a value"
      request.params + "S-param" -> "s value"
      request.contentString = "hello world"

      AwsCanonicalRequest(request).toString shouldBe "PUT\n" +
        "/put-path\n" +
        "S+param=s+value&a+param=a+value&z-param=z+value\n" +
        "a-header:another value\n" +
        "content-length:11\n" +
        "z-header:a value\n" + "\n" +
        "a-header;content-length;z-header\n" +
        "b94d27b9934d3e08a52e52d7da7dabfac484efe37a5380ee9088f7ace2efcde9"
    }
  }
}
