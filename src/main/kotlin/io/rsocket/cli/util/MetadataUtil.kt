/**
 * Copyright 2015 Netflix, Inc.
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.rsocket.cli.util

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.cbor.CBORFactory
import com.google.common.base.Throwables
import io.rsocket.cli.UsageException

object MetadataUtil {

  fun encodeMetadataMap(headerMap: Map<String, String>, mimeType: String): ByteArray {
    return if (mimeType == "application/json") {
      MetadataUtil.jsonEncodeStringMap(headerMap)
    } else if (mimeType == "application/cbor") {
      MetadataUtil.cborEncodeStringMap(headerMap)
    } else {
      throw UsageException("headers not supported with mimetype '$mimeType'")
    }
  }

  fun jsonEncodeStringMap(headerMap: Map<String, String>): ByteArray {
    val m = ObjectMapper()

    try {
      return m.writeValueAsBytes(headerMap)
    } catch (e: JsonProcessingException) {
      throw Throwables.propagate(e)
    }

  }

  fun cborEncodeStringMap(headerMap: Map<String, String>): ByteArray {
    val m = ObjectMapper(CBORFactory())

    try {
      return m.writeValueAsBytes(headerMap)
    } catch (e: JsonProcessingException) {
      throw Throwables.propagate(e)
    }

  }
}
