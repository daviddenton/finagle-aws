package io.github.daviddenton.finagle.aws

import java.time.Clock

import com.twitter.finagle.Filter
import com.twitter.finagle.http.{Request, Response}
import org.jboss.netty.handler.codec.http.HttpHeaders.Names
import org.jboss.netty.handler.codec.http.HttpHeaders.Names.{CONTENT_LENGTH, HOST}

object AwsCredentialFilter {
  def apply(host: String, clock: Clock, signer: AwsSignatureV4Signer): Filter[Request, Response, Request, Response] = Filter.mk {
    (req, svc) => {
      val date = AwsRequestDate(clock.instant())
      req.headerMap(HOST) = host
      req.headerMap(CONTENT_LENGTH) = req.content.length.toString
      req.headerMap(AwsHeaders.DATE) = date.full

      val canonicalRequest = AwsCanonicalRequest(req)
      req.headerMap(Names.AUTHORIZATION) = signer.authHeader(canonicalRequest, date)
      req.headerMap(AwsHeaders.CONTENT_SHA256) = canonicalRequest.payloadHash
      svc(req)
    }
  }
}
