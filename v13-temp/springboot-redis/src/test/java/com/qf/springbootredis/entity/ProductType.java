package com.qf.springbootredis.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author maizifeng
 * @Date 2019/6/24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductType implements Serializable {
    private int id;
    private String name;
}
