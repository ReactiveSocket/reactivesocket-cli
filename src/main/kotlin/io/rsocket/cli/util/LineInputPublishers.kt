package io.rsocket.cli.util

import com.google.common.io.Files
import io.rsocket.Payload
import io.rsocket.cli.OutputHandler
import io.rsocket.cli.UsageException
import io.rsocket.util.PayloadImpl
import reactor.core.publisher.Flux
import java.io.File
import java.nio.charset.StandardCharsets
import java.util.*

class LineInputPublishers(val outputHandler: OutputHandler) : InputPublisher {
  private fun filePublisher(filename: String): Flux<String> {
    return Flux.defer({
      val file = File(filename)

      if (!file.exists()) {
        Flux.error(UsageException("file not found: $filename"))
      } else {
        val r = Files.newReader(file, StandardCharsets.UTF_8)

        Flux.generate<String> { s ->
          val line = r.readLine()

          if (line != null) {
            s.next(line)
          } else {
            s.complete()
          }
        }.doFinally({ r.close() })
      }
    })
  }

  override fun singleInputPayload(input: List<String>, metadata: ByteArray?): Payload {
    return inputPublisher(input, metadata).blockFirst()!!
  }

  override fun inputPublisher(input: List<String>, metadata: ByteArray?): Flux<Payload> {
    val metadataPublisher = if (metadata != null) Flux.just(metadata) else Flux.empty()
    return Flux.fromIterable(input).flatMap {
      when {
        it == "-" -> systemInLines()
        it.startsWith("@") -> filePublisher(it.substring(1))
        else -> Flux.just(it)
      }
    }.zipWith(metadataPublisher.concatWith(Flux.just(NULL_BYTE_ARRAY).repeat())).map { tuple ->
      PayloadImpl(
          tuple.t1.toByteArray(StandardCharsets.UTF_8),
          if (tuple.t2 === NULL_BYTE_ARRAY) null else tuple.t2
      )
    }
  }

  private fun systemInLines(): Flux<String> {
    outputHandler.info("Type commands to send to the server.")

    val keyboard = Scanner(System.`in`)

    return Flux.generate { s ->
      if (keyboard.hasNext()) {
        s.next(keyboard.nextLine())
      } else {
        s.complete()
      }
    }
  }

  private val NULL_BYTE_ARRAY = ByteArray(0)
}
