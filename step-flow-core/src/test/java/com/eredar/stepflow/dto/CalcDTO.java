package com.eredar.stepflow.dto;

import com.eredar.stepflow.engine.aviator.OraDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CalcDTO {

    private OraDecimal num1;
    private OraDecimal num2;
    private OraDecimal num3;
    private OraDecimal num4;
}
