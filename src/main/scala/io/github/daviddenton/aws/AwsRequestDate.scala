package io.github.daviddenton.aws

import java.time.Instant
import java.time.format.DateTimeFormatter

case class AwsRequestDate(value: Instant) {

  lazy val basic = DateTimeFormatter.BASIC_ISO_DATE.format(value)
  lazy val full = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(value)
}



