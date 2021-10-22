package com.m3.play2.sentry

import java.io.IOException

import com.fasterxml.jackson.core.JsonGenerator
import io.sentry.marshaller.json.InterfaceBinding
import scala.collection.Seq

object PlayHttpInterfaceBinding {
  private val URL = "url"
  private val METHOD = "method"
  private val DATA = "data"
  private val QUERY_STRING = "query_string"
  private val COOKIES = "cookies"
  private val HEADERS = "headers"
  private val ENVIRONMENT = "env"
  private val ENV_REMOTE_ADDR = "REMOTE_ADDR"
  private val ENV_SERVER_NAME = "SERVER_NAME"
  private val ENV_LOCAL_ADDR = "LOCAL_ADDR"
  private val ENV_LOCAL_NAME = "LOCAL_NAME"
  private val ENV_REQUEST_SECURE = "REQUEST_SECURE"
}

class PlayHttpInterfaceBinding extends InterfaceBinding[PlayHttpInterface] {
  import PlayHttpInterfaceBinding._

  @throws[IOException]
  override def writeInterface(generator: JsonGenerator, playHttpInterface: PlayHttpInterface): Unit = {
    generator.writeStartObject()
    generator.writeStringField(URL, playHttpInterface.requestUrl)
    generator.writeStringField(METHOD, playHttpInterface.method)

    generator.writeFieldName(DATA)
    writeParameters(generator, playHttpInterface.parameters)

    generator.writeStringField(QUERY_STRING, playHttpInterface.queryString)

    generator.writeFieldName(COOKIES)
    writeCookies(generator, playHttpInterface.cookies)

    generator.writeFieldName(HEADERS)
    writeHeaders(generator, playHttpInterface.headers)

    generator.writeFieldName(ENVIRONMENT)
    writeEnvironment(generator, playHttpInterface)

    generator.writeEndObject()
  }

  @throws[IOException]
  private def writeEnvironment(generator: JsonGenerator, playHttpInterface: PlayHttpInterface): Unit = {
    generator.writeStartObject()
    generator.writeStringField(ENV_REMOTE_ADDR, playHttpInterface.remoteAddr)
    generator.writeStringField(ENV_SERVER_NAME, playHttpInterface.serverName)
    generator.writeStringField(ENV_LOCAL_ADDR, playHttpInterface.localAddr)
    generator.writeStringField(ENV_LOCAL_NAME, playHttpInterface.localName)
    generator.writeBooleanField(ENV_REQUEST_SECURE, playHttpInterface.secure)
    generator.writeEndObject()
  }

  @throws[IOException]
  private def writeHeaders(generator: JsonGenerator, headers: Map[String, Seq[String]]): Unit = {
    generator.writeStartObject()
    headers.foreach {case (key, values) =>
      generator.writeStringField(key, values.mkString(","))
    }
    generator.writeEndObject()
  }

  @throws[IOException]
  private def writeCookies(generator: JsonGenerator, cookies: Seq[(String, String)]): Unit = {
    if (cookies.isEmpty) {
      generator.writeNull()

    } else {
      generator.writeStartObject()
      cookies.foreach { case (key, value) =>
        generator.writeStringField(key, value)
      }
      generator.writeEndObject()
    }
  }

  @throws[IOException]
  private def writeParameters(generator: JsonGenerator, parameterMap: Map[String, Seq[String]]): Unit = {
    if (parameterMap.isEmpty) {
      generator.writeNull()

    } else {
      generator.writeStartObject()

      parameterMap.foreach { case (key, values) =>
        generator.writeArrayFieldStart(key)
        values.foreach { value => generator.writeString(value) }
        generator.writeEndArray()
      }

      generator.writeEndObject()
    }
  }
}