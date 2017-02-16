package io.github.daviddenton.finagle.aws

import java.time.{ZoneId, ZonedDateTime}

import org.scalatest.{FunSpec, Matchers}

class AwsRequestDateTest extends FunSpec with Matchers {

  private val date = AwsRequestDate(ZonedDateTime.of(2016, 12, 25, 7, 35, 49, 0, ZoneId.systemDefault()).toInstant)

  describe("AwsRequestDate") {

    it("basic") {
      date.basic shouldBe "20161225"
    }

    it("full") {
      date.full shouldBe "20161225T073549Z"
    }
  }

}
