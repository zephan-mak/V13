package com.qf.v13.vo;

import com.qf.v13.entity.TProduct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author maizifeng
 * @Date 2019/6/30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemVO implements Serializable,Comparable<CartItemVO> {

    private TProduct product;
    private Integer count;
    private Date updateTime;


    public int compareTo(CartItemVO o) {
        return (int) (o.getUpdateTime().getTime()-this.getUpdateTime().getTime());
    }
}
