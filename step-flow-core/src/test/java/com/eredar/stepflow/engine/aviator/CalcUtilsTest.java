package com.eredar.stepflow.engine.aviator;

import com.eredar.stepflow.engine.aviator.CalcUtils;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

public class CalcUtilsTest {

    @Test
    public void oracleDaysBetweenTest() {
        this.runOracleDaysBetweenOneCase(
                LocalDateTime.of(2023, 3, 11, 10, 43, 26),
                LocalDateTime.of(2025, 10, 20, 22, 11, 17),
                "954.477673611111111111111111111111111111"
        );
        this.runOracleDaysBetweenOneCase(
                LocalDateTime.of(2025, 10, 20, 22, 11, 17),
                LocalDateTime.of(2023, 3, 11, 10, 43, 26),
                "-954.477673611111111111111111111111111111"
        );
    }

    private void runOracleDaysBetweenOneCase(LocalDateTime date1, LocalDateTime date2, String excepted) {
        String actual = CalcUtils.oracleDaysBetween(date1, date2).toString();
        Assert.isTrue(actual.equals(excepted), actual + " / " + excepted);
    }
}
