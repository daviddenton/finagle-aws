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
    val bucket = S3.Bucket(UUID.randomUUID().toString)
    val key = S3.Bucket.Key(UUID.randomUUID().toString)
    val bucketClient = S3.Bucket.client(bucket, region, credentials)

    it("should not find our randomly named bucket in root listing") {
      val rsp = result(s3Client(Request("/")))
      println(rsp)
      rsp.statusCode shouldBe 200
    }

    it("can create a bucket") {
      val rsp = result(s3Client(Request(Put, "/" + bucket.name)))
      println(rsp)
      rsp.statusCode shouldBe 200
    }

    it("should find our new bucket in root listing") {
      val rsp = result(s3Client(Request("/")))
      println(rsp)
      rsp.statusCode shouldBe 200
      rsp.contentString should include(bucket.name)
    }

    it("randomly named key should not exist in bucket listing") {
      val rsp = result(bucketClient(Request("/")))
      println(rsp)
      rsp.statusCode shouldBe 200
      rsp.contentString should not include "random"
    }

    it("can put a new key into the bucket") {
      val request = Request(Put, "/" + key.value)
      request.contentString = key.toString
      val rsp = result(bucketClient(request))
      println(rsp)
      rsp.statusCode shouldBe 200
    }

    it("new key should exist in bucket listing") {
      val rsp = result(bucketClient(Request("/")))
      println(rsp)
      rsp.statusCode shouldBe 200
      rsp.contentString should include(key.value)
    }

    it("new key should have expected contents") {
      val rsp = result(bucketClient(Request("/" + key.value)))
      println(rsp)
      rsp.statusCode shouldBe 200
      rsp.contentString should include(key.value)
    }

    it("can delete the new key") {
      val rsp = result(bucketClient(Request(Delete, "/" + key.value)))
      println(rsp)
      rsp.statusCode shouldBe 204
    }

    it("new key should no longer exist in bucket listing") {
      val rsp = result(bucketClient(Request("/")))
      println(rsp)
      rsp.statusCode shouldBe 200
      rsp.contentString should not include key.value
    }

    it("can delete the new bucket") {
      val rsp = result(s3Client(Request(Delete, "/" + bucket.name)))
      println(rsp)
      rsp.statusCode shouldBe 204
    }

    it("should not find our new bucket in root listing") {
      val rsp = result(s3Client(Request("/")))
      println(rsp)
      rsp.statusCode shouldBe 200
      rsp.contentString should not include bucket.name
    }
  }
}
