package io.rsocket.cli.i9n

import io.rsocket.cli.util.TimeUtil.parseShortDuration
import org.junit.Assert.assertEquals

import io.airlift.airline.ParseException
import java.time.Duration
import org.junit.Test

class TimeUtilTest {
  @Test
  fun parseMillis() {
    assertEquals(Duration.ofMillis(5), parseShortDuration("5ms"))
  }

  @Test
  fun parseSeconds() {
    assertEquals(Duration.ofSeconds(23), parseShortDuration("23s"))
  }

  @Test
  fun parseMinutes() {
    assertEquals(Duration.ofMinutes(0), parseShortDuration("0m"))
  }

  @Test(expected = ParseException::class)
  fun failOnBadFormat() {
    parseShortDuration("-10 minutes")
  }
}
