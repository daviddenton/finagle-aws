package io.github.daviddenton.finagle.aws

import java.time.{ZoneId, ZonedDateTime}

import io.github.daviddenton.aws.AwsRequestDate
import org.scalatest.{FunSpec, ShouldMatchers}

class AwsRequestDateTest extends FunSpec with ShouldMatchers {

  private val date = AwsRequestDate(ZonedDateTime.of(2016, 1, 27, 15, 32, 50, 0, ZoneId.of("UTC")).toInstant)

  describe("AwsRequestDate") {

    it("basic") {
      date.basic shouldBe "20161225"
    }

    it("full") {
      date.full shouldBe "20161225T073549Z"
    }
  }

}
