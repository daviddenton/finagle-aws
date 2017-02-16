package io.github.daviddenton.finagle.aws

import java.util.UUID

import com.twitter.finagle.http.Method.{Delete, Put}
import com.twitter.finagle.http.Request
import com.twitter.util.Await.result
import org.scalatest.{FunSpec, Matchers}

class RealAwsTest extends FunSpec with Matchers {

  private val region = AwsRegion("us-east-1")
  private val credentials = AwsCredentials(System.getenv("AWS_ACCESS_KEY"), System.getenv("AWS_SECRET_KEY"))
  private val s3Client = S3.client(region, credentials)

  describe("interact with real AWS S3") {
    val bucket = Bucket(UUID.randomUUID().toString)
    val key1 = Key(UUID.randomUUID().toString)
    val key2 = Key(UUID.randomUUID().toString)
    val bucketClient = Bucket.client(bucket, region, credentials)

    it("should not find our randomly named bucket in root listing") {
      val rsp = result(s3Client(Request("/")))
      rsp.statusCode shouldBe 200
    }

    it("can create a bucket") {
      val rsp = result(s3Client(Request(Put, "/" + bucket.name)))
      rsp.statusCode shouldBe 200
    }

    it("should find our new bucket in root listing") {
      val rsp = result(s3Client(Request("/")))
      rsp.statusCode shouldBe 200
      rsp.contentString should include(bucket.name)
    }

    it("randomly named key should not exist in bucket listing") {
      val rsp = result(bucketClient(Request("/")))
      rsp.statusCode shouldBe 200
      rsp.contentString should not include "random"
    }

    it("can put keys into the bucket") {
      val request1 = Request(Put, "/" + key1.value)
      request1.contentString = key1.toString
      val rsp1 = result(bucketClient(request1))
      rsp1.statusCode shouldBe 200

      val request2 = Request(Put, "/" + key2.value)
      request2.contentString = key1.toString

      val rsp2 = result(bucketClient(request2))
      rsp2.statusCode shouldBe 200
    }

    it("new keys should exist in bucket listing") {
      val rsp = result(bucketClient(Request("/")))
      rsp.statusCode shouldBe 200
      rsp.contentString should include(key1.value)
      rsp.contentString should include(key2.value)
    }

    it("new key should have expected contents") {
      val rsp = result(bucketClient(Request("/" + key1.value)))
      rsp.statusCode shouldBe 200
      rsp.contentString should include(key1.value)
    }

    it("can delete the new keys") {
      val rsp1 = result(bucketClient(Request(Delete, "/" + key1.value)))
      rsp1.statusCode shouldBe 204

      val rsp2 = result(bucketClient(Request(Delete, "/" + key2.value)))
      rsp2.statusCode shouldBe 204
    }

    it("new key should no longer exist in bucket listing") {
      val rsp = result(bucketClient(Request("/")))
      rsp.statusCode shouldBe 200
      rsp.contentString should not include key1.value
    }

    it("can delete the new bucket") {
      val rsp = result(s3Client(Request(Delete, "/" + bucket.name)))
      rsp.statusCode shouldBe 204
    }

    it("should not find our new bucket in root listing") {
      val rsp = result(s3Client(Request("/")))
      rsp.statusCode shouldBe 200
      rsp.contentString should not include bucket.name
    }
  }
}
