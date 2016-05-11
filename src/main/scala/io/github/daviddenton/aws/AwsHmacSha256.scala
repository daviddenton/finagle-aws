package io.github.daviddenton.aws

import java.nio.charset.StandardCharsets
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

import aws.Hex

object AwsHmacSha256 {
  def hash(payload: String): String = hash(payload.getBytes(StandardCharsets.UTF_8))

  def hash(payload: Array[Byte]): String = Digest.sha256(payload).toHex

  def hmacSHA256(key: Array[Byte], data: String): Array[Byte] = {
      val algorithm = "HmacSHA256"
      val mac = Mac.getInstance(algorithm)
      mac.init(new SecretKeySpec(key, algorithm))
      mac.doFinal(data.getBytes(StandardCharsets.UTF_8))
  }

  def hex(data: Array[Byte]): String = Hex.encode(data)
}
