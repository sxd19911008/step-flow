package io.github.kentasun.stepflow.dto;

import io.github.kentasun.aviatororacle.number.OraDecimal;

import java.util.Date;

/**
 * 日期与费率综合计算场景 DTO
 * <p>
 * 对应 PLSQL 脚本 Oracle_PLSQL_1.sql 中的输入参数，用于覆盖：
 * <ul>
 *   <li>日期加减小数（{@code date + 1.5}）</li>
 *   <li>数字四则运算</li>
 *   <li>Oracle 函数：decode / nvl / coalesce / abs / floor / ceil / round /
 *       trunc / power / months_between / add_months / last_day</li>
 * </ul>
 */
public class DateCalcDTO {

    /**
     * 贷款开始日期（2023-03-15）
     */
    private Date startDate;

    /**
     * 贷款结束日期（2024-09-20）
     */
    private Date endDate;

    /**
     * 本金基数 = 100
     */
    private OraDecimal principal;

    /**
     * 费率系数 = 3.7
     */
    private OraDecimal rateInput;

    /**
     * 带负号的数值，用于测试 abs 函数（= -7.4325923421）
     */
    private OraDecimal signedValue;

    /**
     * 额外因子，故意置为 {@code null}，触发 nvl / coalesce / decode 的 NULL 分支。
     * <p>
     * Oracle 侧：{@code decode(nvl(null, 5), 5, coalesce(null, nvl(null, 5)), 0) = 5}
     */
    private OraDecimal extraFactor;

    public DateCalcDTO(Date startDate, Date endDate, OraDecimal principal, OraDecimal rateInput, OraDecimal signedValue, OraDecimal extraFactor) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.principal = principal;
        this.rateInput = rateInput;
        this.signedValue = signedValue;
        this.extraFactor = extraFactor;
    }

    public DateCalcDTO() {
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public Date getEndDate() {
        return this.endDate;
    }

    public OraDecimal getPrincipal() {
        return this.principal;
    }

    public OraDecimal getRateInput() {
        return this.rateInput;
    }

    public OraDecimal getSignedValue() {
        return this.signedValue;
    }

    public OraDecimal getExtraFactor() {
        return this.extraFactor;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setPrincipal(OraDecimal principal) {
        this.principal = principal;
    }

    public void setRateInput(OraDecimal rateInput) {
        this.rateInput = rateInput;
    }

    public void setSignedValue(OraDecimal signedValue) {
        this.signedValue = signedValue;
    }

    public void setExtraFactor(OraDecimal extraFactor) {
        this.extraFactor = extraFactor;
    }

    public String toString() {
        return "DateCalcDTO(startDate=" + this.getStartDate() + ", endDate=" + this.getEndDate() + ", principal=" + this.getPrincipal() + ", rateInput=" + this.getRateInput() + ", signedValue=" + this.getSignedValue() + ", extraFactor=" + this.getExtraFactor() + ")";
    }

    public static DateCalcDTOBuilder builder() {
        return new DateCalcDTOBuilder();
    }

    public static class DateCalcDTOBuilder {
        private Date startDate;
        private Date endDate;
        private OraDecimal principal;
        private OraDecimal rateInput;
        private OraDecimal signedValue;
        private OraDecimal extraFactor;

        DateCalcDTOBuilder() {
        }

        public DateCalcDTOBuilder startDate(Date startDate) {
            this.startDate = startDate;
            return this;
        }

        public DateCalcDTOBuilder endDate(Date endDate) {
            this.endDate = endDate;
            return this;
        }

        public DateCalcDTOBuilder principal(OraDecimal principal) {
            this.principal = principal;
            return this;
        }

        public DateCalcDTOBuilder rateInput(OraDecimal rateInput) {
            this.rateInput = rateInput;
            return this;
        }

        public DateCalcDTOBuilder signedValue(OraDecimal signedValue) {
            this.signedValue = signedValue;
            return this;
        }

        public DateCalcDTOBuilder extraFactor(OraDecimal extraFactor) {
            this.extraFactor = extraFactor;
            return this;
        }

        public DateCalcDTO build() {
            return new DateCalcDTO(this.startDate, this.endDate, this.principal, this.rateInput, this.signedValue, this.extraFactor);
        }

        public String toString() {
            return "DateCalcDTO.DateCalcDTOBuilder(startDate=" + this.startDate + ", endDate=" + this.endDate + ", principal=" + this.principal + ", rateInput=" + this.rateInput + ", signedValue=" + this.signedValue + ", extraFactor=" + this.extraFactor + ")";
        }
    }
}
