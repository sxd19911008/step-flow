package com.eredar.stepflow.engine.aviator;

import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.time.Instant;

public class CalcUtilsTest {

    @Test
    public void oracleDaysBetweenTest() {
        this.runOracleDaysBetweenOneCase(
                Instant.parse("2023-03-11T10:43:26Z"),
                Instant.parse("2025-10-20T22:11:17Z"),
                "954.477673611111111111111111111111111111"
        );
        this.runOracleDaysBetweenOneCase(
                Instant.parse("2025-10-20T22:11:17Z"),
                Instant.parse("2023-03-11T10:43:26Z"),
                "-954.477673611111111111111111111111111111"
        );
    }

    private void runOracleDaysBetweenOneCase(Instant date1, Instant date2, String excepted) {
        String actual = CalcUtils.oracleDaysBetween(date1, date2).toString();
        Assert.isTrue(actual.equals(excepted), actual + " / " + excepted);
    }
}
