package io.github.daviddenton.finagle.aws

case class AwsRegion(name: String) extends AnyVal {
  override def toString = name
}

object AwsRegion {
  val usEast = AwsRegion("us-east")
}