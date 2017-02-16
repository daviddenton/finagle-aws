package io.github.daviddenton.finagle.aws

case class AwsService(name: String) extends AnyVal {
  override def toString = name
}

object AwsService {
  val s3 = AwsService("s3")
}


