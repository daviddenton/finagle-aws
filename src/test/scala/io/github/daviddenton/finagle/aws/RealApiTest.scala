package io.github.daviddenton.finagle.aws

import java.util.UUID

import com.twitter.io.Bufs.utf8Buf
import com.twitter.util.Await.result
import org.scalatest.{FunSpec, Matchers}

class RealApiTest extends FunSpec with Matchers {

  private val region = AwsRegion("us-east-1")
  private val credentials = AwsCredentials(System.getenv("AWS_ACCESS_KEY"), System.getenv("AWS_SECRET_KEY"))
  private val s3Client = S3Api(S3.client(region, credentials))

  describe("S3") {
    val bucket = Bucket(UUID.randomUUID().toString)
    val key1 = Key(UUID.randomUUID().toString)
    val key2 = Key(UUID.randomUUID().toString)
    val bucketClient = BucketApi(bucket, Bucket.client(bucket, region, credentials))

    it("should not find our randomly named bucket in root listing") {
      result(s3Client.list()) should not contain bucket
    }

    it("can create a bucket") {
      result(s3Client += bucket) shouldBe Some(bucket)
    }

    it("should find our new bucket in root listing") {
      result(s3Client.list()) should contain(bucket)
    }

    it("randomly named key should not exist in bucket listing") {
      val list = result(bucketClient.list())
      list should not contain key1
      list should not contain key2
    }

    it("can put keys into the bucket") {
      result(bucketClient += key1 -> utf8Buf(key1.value)) shouldBe Some(key1)
      result(bucketClient += key2 -> utf8Buf(key2.value)) shouldBe Some(key2)
    }

    it("new keys should exist in bucket listing") {
      result(bucketClient.list()) shouldBe Seq(key1, key2)
    }

    it("new keys should have expected contents") {
      result(bucketClient(key1)) shouldBe utf8Buf(key1.value)
      result(bucketClient(key2)) shouldBe utf8Buf(key2.value)
    }

    it("can delete the new keys") {
      result(bucketClient -= key1) shouldBe Some(key1)
      result(bucketClient -= key2) shouldBe Some(key2)
    }

    it("new key should no longer exist in bucket listing") {
      val list = result(bucketClient.list())
      list should not contain key1
      list should not contain key2
    }

    it("can delete the new bucket") {
      result(s3Client -= bucket) shouldBe Some(bucket)
    }

    it("should not find our new bucket in root listing") {
      result(s3Client.list()) should not contain bucket
    }
  }
}
