package io.github.daviddenton.finagle.aws

import io.github.daviddenton.finagle.aws.AwsHmacSha256.{hex, hmacSHA256}
import org.scalatest.{FunSpec, ShouldMatchers}

class AwsHmacSha256Test extends FunSpec with ShouldMatchers {

  describe("AwsHmacSha256") {

    it("hash") {
      AwsHmacSha256.hash("test string") shouldBe "d5579c46dfcc7f18207013e65b44e4cb4e2c2298f4ac457ba8f82743f31e930b"
    }

    it("hex") {
      hex("test string".getBytes) shouldBe "7465737420737472696e67"
    }

    it("encrypt") {
      hex(hmacSHA256("test key".getBytes, "test string")) shouldBe "6864a9fdc9bc77190c4bc6d1d875a0afe19461907f486f4ba5213a1f15b71cc9"
    }
  }

}
