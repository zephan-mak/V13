package com.qf.v13.api;

import com.github.pagehelper.PageInfo;
import com.qf.v13.common.base.IBaseService;
import com.qf.v13.entity.TProduct;
import com.qf.v13.entity.TProductType;
import com.qf.v13.pojo.TProductVO;

import java.util.List;


/**
 * @author maizifeng
 * @Date 2019/6/11
 */
public interface IProductService extends IBaseService<TProduct> {
    PageInfo<TProduct> page(Integer indexPage,Integer pageSize);
    Long save(TProductVO productVO);
    long batchDel(List<Long> list);

    List<TProductType> getType();

    TProductVO selectAllById(Long id);

    long updateById(Long id, TProductVO productVO);
}
