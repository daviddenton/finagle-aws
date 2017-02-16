package io.github.daviddenton.finagle.aws

import org.scalatest.{FunSpec, Matchers}

class S3Test extends FunSpec with Matchers {
  describe("S3") {
    it("parses Buckets from a bucket list") {
      val bucket1 = Bucket("bucket1")
      val bucket2 = Bucket("bucket2")
      S3.buckets(S3Xml.listBuckets(bucket1, bucket2)) shouldBe Seq(bucket1, bucket2)
    }

    it("parses keys from a bucket") {
      val key1 = Key("key1")
      val key2 = Key("key2")
      S3.keys(S3Xml.bucketContents(key1, key2)) shouldBe Seq(key1, key2)
    }
  }
}
