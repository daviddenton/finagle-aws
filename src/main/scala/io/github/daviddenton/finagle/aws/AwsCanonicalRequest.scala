package io.github.daviddenton.finagle.aws

import java.net.URLEncoder.encode

import com.twitter.finagle.http.Request
import io.github.daviddenton.finagle.aws.AwsHmacSha256.hash

case class AwsCanonicalRequest(request: Request) {
  val signedHeaders = signHeaders(request)
  val payloadHash = hashPayload(request)
  private val canonical = request.method + "\n" +
    request.path + "\n" +
    canonicalQueryString(request) + "\n" +
    canonicalHeaders(request) + "\n\n" +
    signedHeaders + "\n" +
    payloadHash

  private def signHeaders(request: Request) =
    request.headerMap.map(header => header._1.toLowerCase()).toSeq.sortBy(identity).mkString(";")

  private def canonicalHeaders(request: Request) = request.headerMap.
    map(header => (header._1.toLowerCase(), header._2.replaceAll("\\s+", " ").trim()))
    .map(header => header._1 + ":" + header._2).toSeq.sortBy(identity).mkString("\n")

  private def canonicalQueryString(request: Request) = request.params
    .toSeq
    .map(param => encode(param._1, "UTF-8") + "=" + encode(param._2, "UTF-8"))
    .sortBy(identity)
    .mkString("&")

  private def hashPayload(request: Request) = hash(request.contentString.getBytes())

  override def toString = canonical
}
