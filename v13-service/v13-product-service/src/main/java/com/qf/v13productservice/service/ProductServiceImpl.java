package com.qf.v13productservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qf.v13.api.IProductService;
import com.qf.v13.common.base.BaseServiceImpl;
import com.qf.v13.common.base.IBaseDao;
import com.qf.v13.entity.TProduct;
import com.qf.v13.entity.TProductDesc;
import com.qf.v13.entity.TProductType;
import com.qf.v13.mapper.TProductDescMapper;
import com.qf.v13.mapper.TProductMapper;
import com.qf.v13.mapper.TProductTypeMapper;
import com.qf.v13.pojo.TProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author maizifeng
 * @Date 2019/6/11
 */
@Service
public class ProductServiceImpl extends BaseServiceImpl<TProduct> implements IProductService{
    @Autowired
    private TProductMapper tProductMapper;
    @Autowired
    private TProductDescMapper tProductDescMapper;
    @Autowired
    private TProductTypeMapper tProductTypeMapper;


    @Override
    public IBaseDao<TProduct> getBaseDao() {
        return tProductMapper;
    }

    public List<TProduct> list() {
        return tProductMapper.list();
    }

    @Override
    public PageInfo<TProduct> page(Integer indexPage, Integer pageSize) {
        PageHelper.startPage(indexPage, pageSize);
        List<TProduct> list = list();
        PageInfo<TProduct> pageInfo = new PageInfo<TProduct>(list, 10);
        return pageInfo;
    }

    @Override
    @Transactional
    public Long save(TProductVO productVO) {
        TProduct product = productVO.getProduct();
        product.setFlag(true);
        int count = tProductMapper.insert(product);
        String productDesc = productVO.getProductDesc();
        TProductDesc desc=new TProductDesc();
        desc.setProductDesc(productDesc);
        desc.setProductId(product.getId());
        tProductDescMapper.insert(desc);
        return product.getId();
    }

    @Override
    public long batchDel(List<Long> list) {

        return tProductMapper.batchDel(list);
    }

    @Override
    public List<TProductType>  getType() {
        List<TProductType> list = tProductTypeMapper.list();
        List<TProductType> typeIdList=new ArrayList();
        for (TProductType tProductType : list) {
            Long pid = tProductType.getPid();
            if(pid==0){
                typeIdList.add(tProductType);
            }
        }
        return typeIdList;
    }

    @Override
    public TProductVO selectAllById(Long id) {
        TProduct product = tProductMapper.selectByPrimaryKey(id);
        TProductType productType = tProductTypeMapper.selectByPrimaryKey(product.getTypeId());
        TProductDesc productDesc = tProductDescMapper.selectByProduct_id(product.getId());
        TProductVO productVO=new TProductVO(product, productDesc.getProductDesc(), productType);
        return productVO;
    }

    @Override
    public long updateById(Long id, TProductVO productVO) {
        tProductMapper.updateByPrimaryKeySelective(productVO.getProduct());
        TProductDesc desc = tProductDescMapper.selectByProduct_id(productVO.getProduct().getId());
        desc.setProductDesc(productVO.getProductDesc());
        desc.setProductId(productVO.getProduct().getId());
        tProductDescMapper.updateByPrimaryKeySelective(desc);
        return productVO.getProduct().getId();
    }

    @Override
    public int deleteByPrimaryKey(Long id) {
        TProduct product =new TProduct();
        product.setFlag(false);
        product.setId(id);
        return  tProductMapper.updateByPrimaryKeySelective(product);
    }
}
