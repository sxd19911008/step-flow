package com.eredar.stepflow.engine.aviator.number;

import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

public class OraDecimalTest {

    @Test
    public void divideTest() {
        this.runDivideOneCase("678", "99", "6.84848484848484848484848484848484848485");
        this.runDivideOneCase("2710", "2880.6", "0.9407762271748941192807054085954315073249");
        this.runDivideOneCase("2.71", "2880.6", "0.000940776227174894119280705408595431507325");
        this.runDivideOneCase("0.271", "2880.6", "0.00009407762271748941192807054085954315073249");
    }

    private void runDivideOneCase(String decimal1, String decimal2, String excepted) {
        OraDecimal actual = divide(decimal1, decimal2);
        Assert.isTrue(actual.compareTo(new OraDecimal(excepted)) == 0, actual + " / " + excepted);
    }

    private OraDecimal divide(String decimal1, String decimal2) {
        return new OraDecimal(decimal1).divide(new OraDecimal(decimal2));
    }
}
