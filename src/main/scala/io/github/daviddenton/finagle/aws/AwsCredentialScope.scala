package io.github.daviddenton.finagle.aws

case class AwsCredentialScope(region: String, service: String) {
  def awsCredentialScope(date: AwsRequestDate): String = s"${date.basic}/$region/$service/aws4_request"
}


