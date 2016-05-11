package io.github.daviddenton.finagle.aws

import java.nio.charset.StandardCharsets.UTF_8

import aws.Hex.encode
import com.twitter.finagle.http.Request
import io.github.daviddenton.aws.AwsHmacSha256
import org.jboss.netty.handler.codec.http.QueryStringDecoder

import scala.collection.JavaConverters._

case class AwsCanonicalRequest(request: Request) {
  val signedHeaders: String = signedHeaders(request)
  private val canonical = request.method + "\n" +
    request.path + "\n" +
    canonicalQueryString(request) + "\n" +
    canonicalHeaders(request) + "\n\n" +
    signedHeaders + "\n" +
    payloadHash(request)

  private def signedHeaders(request: Request) =
    request.headerMap.map(header => header._1.toLowerCase()).toSeq.sortBy(identity).mkString(";")

  private def canonicalHeaders(request: Request) = request.headerMap.
    map(header => (header._1.toLowerCase(), header._2.replaceAll("\\s+", " ").trim()))
    .map(header => header._1 + ":" + header._2).toSeq.sortBy(identity).mkString("\n")

  private def canonicalQueryString(request: Request) =
    new QueryStringDecoder(request.uri).getParameters.asScala
      .toSeq
      .map(param => encode(param._1.getBytes(UTF_8)) + "=" + encode(param._1.getBytes(UTF_8)))
      .sortBy(identity)
      .mkString("&")

  private def payloadHash(request: Request) = AwsHmacSha256.hash(request.contentString.getBytes())

  override def toString = canonical
}
