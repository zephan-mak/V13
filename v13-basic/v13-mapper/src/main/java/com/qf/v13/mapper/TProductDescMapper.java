package com.qf.v13.mapper;

import com.qf.v13.common.base.IBaseDao;
import com.qf.v13.entity.TProductDesc;

public interface TProductDescMapper extends IBaseDao<TProductDesc> {
    TProductDesc selectByProduct_id(long product_id);
}