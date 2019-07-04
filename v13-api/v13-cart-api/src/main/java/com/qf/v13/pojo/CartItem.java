package com.qf.v13.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author maizifeng
 * @Date 2019/6/29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItem implements Serializable{

    private static final long serialVersionUID = -398278048304325544L;
    private Long productId;
    private int count;
    private Date updateTime;
}
