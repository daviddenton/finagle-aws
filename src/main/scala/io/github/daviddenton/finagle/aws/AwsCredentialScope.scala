package io.github.daviddenton.finagle.aws

case class AwsCredentialScope(region: AwsRegion, service: AwsService) {
  def awsCredentialScope(date: AwsRequestDate): String = s"${date.basic}/$region/$service/aws4_request"
}


