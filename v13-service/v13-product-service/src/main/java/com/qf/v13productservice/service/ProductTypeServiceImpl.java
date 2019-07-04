package com.qf.v13productservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.v13.api.IProductTypeService;
import com.qf.v13.common.base.BaseServiceImpl;
import com.qf.v13.common.base.IBaseDao;
import com.qf.v13.entity.TProductType;
import com.qf.v13.mapper.TProductTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author maizifeng
 * @Date 2019/6/14
 */
@Service
public class ProductTypeServiceImpl extends BaseServiceImpl<TProductType> implements IProductTypeService{

    @Autowired
    private TProductTypeMapper tProductTypeMapper;
    @Override
    public IBaseDao<TProductType> getBaseDao() {
        return tProductTypeMapper;
    }

    public List<TProductType> list(){
        List<TProductType> productTypeList = tProductTypeMapper.list();
        return productTypeList;
    }
}
