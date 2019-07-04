package com.qf.v13.pojo;

import com.qf.v13.entity.TProduct;
import com.qf.v13.entity.TProductDesc;
import com.qf.v13.entity.TProductType;

import java.io.Serializable;

/**
 * @author maizifeng
 * @Date 2019/6/12
 */
public class TProductVO implements Serializable {
    private TProduct Product;
    private String productDesc;
    private TProductType productType;

    public TProductVO(TProduct product, String productDesc, TProductType productType) {
        Product = product;
        this.productDesc = productDesc;
        this.productType = productType;
    }

    public TProductType getProductType() {
        return productType;
    }

    public void setProductType(TProductType productType) {
        this.productType = productType;
    }

    public TProductVO() {
    }

    public TProductVO(TProduct product, String productDesc) {
        Product = product;
        this.productDesc = productDesc;
    }

    public TProduct getProduct() {
        return Product;
    }

    public void setProduct(TProduct product) {
        Product = product;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }
}
