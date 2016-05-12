package io.github.daviddenton.aws

import java.time.Instant
import java.time.ZoneId.systemDefault
import java.time.format.DateTimeFormatter

import io.github.daviddenton.aws.AwsRequestDate._

case class AwsRequestDate(value: Instant) {
  lazy val basic = basicFormatter.format(value)
  lazy val full = fullFormatter.format(value)
}

object AwsRequestDate {
  private val basicFormatter = DateTimeFormatter.ofPattern("yyyyMMdd").withZone(systemDefault())
  private val fullFormatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'").withZone(systemDefault())
}


