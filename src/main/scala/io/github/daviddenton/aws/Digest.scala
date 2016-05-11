package io.github.daviddenton.aws

import java.security.MessageDigest
import java.util.Base64

import aws.Hex

case class Digest(bytes: Array[Byte]) {
  def toHex: String = Hex.encode(bytes)

  def toBase64: String = new String(Base64.getEncoder.encode(bytes))
}

object Digest {
  def md5(bytes: Array[Byte]): Digest = {
    digest(bytes, "MD5")
  }

  def sha256(bytes: Array[Byte]): Digest = {
    digest(bytes, "SHA-256")
  }

  def digest(bytes: Array[Byte], algorithm: String): Digest = new Digest(MessageDigest.getInstance(algorithm).digest(bytes))

  private def algorithm(name: String): MessageDigest = MessageDigest.getInstance(name)
}






