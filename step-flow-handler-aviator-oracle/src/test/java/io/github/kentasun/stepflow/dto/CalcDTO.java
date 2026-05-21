package io.github.kentasun.stepflow.dto;

import io.github.kentasun.aviatororacle.number.OraDecimal;

public class CalcDTO {

    private OraDecimal num1;
    private OraDecimal num2;
    private OraDecimal num3;
    private OraDecimal num4;

    public CalcDTO(OraDecimal num1, OraDecimal num2, OraDecimal num3, OraDecimal num4) {
        this.num1 = num1;
        this.num2 = num2;
        this.num3 = num3;
        this.num4 = num4;
    }

    public CalcDTO() {
    }

    public OraDecimal getNum1() {
        return this.num1;
    }

    public OraDecimal getNum2() {
        return this.num2;
    }

    public OraDecimal getNum3() {
        return this.num3;
    }

    public OraDecimal getNum4() {
        return this.num4;
    }

    public void setNum1(OraDecimal num1) {
        this.num1 = num1;
    }

    public void setNum2(OraDecimal num2) {
        this.num2 = num2;
    }

    public void setNum3(OraDecimal num3) {
        this.num3 = num3;
    }

    public void setNum4(OraDecimal num4) {
        this.num4 = num4;
    }

    public String toString() {
        return "CalcDTO(num1=" + this.getNum1() + ", num2=" + this.getNum2() + ", num3=" + this.getNum3() + ", num4=" + this.getNum4() + ")";
    }

    public static CalcDTOBuilder builder() {
        return new CalcDTOBuilder();
    }

    public static class CalcDTOBuilder {
        private OraDecimal num1;
        private OraDecimal num2;
        private OraDecimal num3;
        private OraDecimal num4;

        CalcDTOBuilder() {
        }

        public CalcDTOBuilder num1(OraDecimal num1) {
            this.num1 = num1;
            return this;
        }

        public CalcDTOBuilder num2(OraDecimal num2) {
            this.num2 = num2;
            return this;
        }

        public CalcDTOBuilder num3(OraDecimal num3) {
            this.num3 = num3;
            return this;
        }

        public CalcDTOBuilder num4(OraDecimal num4) {
            this.num4 = num4;
            return this;
        }

        public CalcDTO build() {
            return new CalcDTO(this.num1, this.num2, this.num3, this.num4);
        }

        public String toString() {
            return "CalcDTO.CalcDTOBuilder(num1=" + this.num1 + ", num2=" + this.num2 + ", num3=" + this.num3 + ", num4=" + this.num4 + ")";
        }
    }
}
