package com.qf.v13payweb.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author maizifeng
 * @Date 2019/7/2
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayConnect {
    private String out_trade_no;
    private String product_code;
    private String total_amount;
    private String subject;
    private String body;
}
