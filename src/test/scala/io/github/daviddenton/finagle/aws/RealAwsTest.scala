package io.github.daviddenton.finagle.aws

import java.util.UUID

import com.twitter.finagle.http.Method.{Delete, Put}
import com.twitter.finagle.http.Request
import com.twitter.util.Await.result
import org.scalatest.{FunSpec, Matchers}

class RealAwsTest extends FunSpec with Matchers {

  private val region = AwsRegion("")
  private val credentials = AwsCredentials("", "")
  private val s3Client = S3.client(region, credentials)

  describe("interact with real AWS S3") {
    val bucket = S3.Bucket(UUID.randomUUID().toString)
    val key = S3.Bucket.Key(UUID.randomUUID().toString)
    val bucketClient = S3.Bucket.client(bucket, region, credentials)

    ignore("should not find our randomly named bucket in root listing") {
      val rsp = result(s3Client(Request("/")))
      rsp.statusCode shouldBe 200
      println(rsp)
    }

    ignore("can create a bucket") {
      val rsp = result(s3Client(Request(Put, bucket.name)))
      rsp.statusCode shouldBe 200
      println(rsp)
    }

    ignore("should find our new bucket in root listing") {
      val rsp = result(s3Client(Request("/")))
      rsp.statusCode shouldBe 200
      println(rsp)
    }

    ignore("randomly named key should not exist in bucket listing") {
      val rsp = result(bucketClient(Request("/")))
      rsp.statusCode shouldBe 200
      println(rsp)
    }

    ignore("can put a new key into the bucket") {
      val request = Request(key.value)
      request.contentString = key.toString
      val rsp = result(bucketClient(request))
      rsp.statusCode shouldBe 200
      println(rsp)
    }

    ignore("new key should exist in bucket listing") {
      val rsp = result(bucketClient(Request("/")))
      rsp.statusCode shouldBe 200
      println(rsp)
    }

    ignore("new key should have expected contents") {
      val rsp = result(bucketClient(Request(key.value)))
      rsp.statusCode shouldBe 200
      println(rsp)
    }

    ignore("can delete the new key") {
      val rsp = result(bucketClient(Request(Delete, key.value)))
      rsp.statusCode shouldBe 200
      println(rsp)
    }

    ignore("new key should no longer exist in bucket listing") {
      val rsp = result(bucketClient(Request("/")))
      rsp.statusCode shouldBe 200
      println(rsp)
    }

    ignore("can delete the new bucket") {
      val rsp = result(s3Client(Request(Delete, bucket.name)))
      rsp.statusCode shouldBe 200
      println(rsp)
    }

    ignore("should not find our new bucket in root listing") {
      val rsp = result(s3Client(Request("/")))
      rsp.statusCode shouldBe 200
      println(rsp)
    }
  }
}
