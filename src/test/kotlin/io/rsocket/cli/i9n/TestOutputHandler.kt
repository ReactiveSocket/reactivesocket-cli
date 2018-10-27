package io.rsocket.cli.i9n

import com.baulsupp.oksocial.output.OutputHandler
import com.baulsupp.oksocial.output.UsageException

class TestOutputHandler : OutputHandler<Any> {
  private val stdout = mutableListOf<String>()
  private val stderr = mutableListOf<String>()

  override suspend fun showOutput(response: Any) {
    stdout.add(response.toString())
  }

  override fun info(message: String) {
    stderr.add(message)
  }

  override suspend fun showError(message: String?, e: Throwable?) {
    if (e is UsageException) {
      stderr.add(e.message.toString())
    } else {
      stderr.add(message + ": " + e.toString())
    }
  }

  override fun hashCode(): Int {
    return stdout.hashCode() + stderr.hashCode()
  }

  override fun equals(other: Any?): Boolean {
    if (other is TestOutputHandler) {
      return stderr == other.stderr && stdout == other.stdout
    }

    return false
  }

  override fun toString(): String {
    val sb = StringBuilder(4096)

    if (!stdout.isEmpty()) {
      sb.append("STDOUT:\n")
      stdout.forEach { s -> sb.append(s).append("\n") }
    }

    if (!stderr.isEmpty()) {
      sb.append("STDERR:\n")
      stderr.forEach { s -> sb.append(s).append("\n") }
    }

    return sb.toString()
  }
}
