package com.qf.v13searchservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.v13.common.page.PageResultBean;
import com.qf.v13.common.pojo.ResultBean;
import com.qf.v13.entity.TProduct;
import com.qf.v13.mapper.TProductMapper;
import com.qf.v13.api.ISearchService;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author maizifeng
 * @Date 2019/6/17
 */
@Service
public class SearchServiceImpl implements ISearchService {
    @Autowired
    private TProductMapper productMapper;
    @Autowired
    private SolrClient solrClient;

    @Override
    public ResultBean synAllData() {
        List<TProduct> list = productMapper.list();
        for (TProduct product : list) {
            SolrInputDocument document=new SolrInputDocument();
            document.setField("id", product.getId());
            document.setField("product_name", product.getName());
            document.setField("product_price", product.getPrice()) ;
            document.setField("product_sale_point", product.getSalePoint());
            document.setField("product_sale_prive", product.getSalePrive());
            document.setField("product_images", product.getImages());
            try {
                solrClient.add(document);
            } catch (SolrServerException | IOException e ) {
                e.printStackTrace();
                return new ResultBean("404", "数据同步不成功");
            }
        }
            try {
                solrClient.commit();
            } catch (SolrServerException | IOException e) {
                e.printStackTrace();
                return new ResultBean("404", "数据添加不成功");
            }
        return new ResultBean("200", "数据同步成功");
    }

    @Override
    public ResultBean searchKeyWord(String keyWord) {
        SolrQuery condition=new SolrQuery();
        if(!StringUtils.isAnyEmpty(keyWord)){
            condition.setQuery("product_keywords:"+keyWord);
        }else {
            condition.setQuery("product_keywords:*");
        }
        condition.setHighlight(true);
        condition.addHighlightField("product_name");
        condition.setHighlightSimplePre("<font color='red'>");
        condition.setHighlightSimplePost("</font>");
        try {
            QueryResponse response = solrClient.query(condition);
            SolrDocumentList documents = response.getResults();
            List<TProduct> list=new ArrayList<>(documents.size());
            Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
            for (SolrDocument document : documents) {
                TProduct product=new TProduct();
                product.setId(Long.parseLong(document.get("id").toString()));
//                product.setName(document.get("product_name").toString());
                product.setPrice(Long.parseLong(document.get("product_price").toString()));
                product.setSalePoint(document.get("product_sale_point").toString());
                product.setSalePrive(Long.parseLong(document.get("product_sale_prive").toString()));
                product.setImages(document.get("product_images").toString());
                Map<String, List<String>> map = highlighting.get(document.getFieldValue("id"));
                List<String> product_name = map.get("product_name");
                if(product_name!=null&&product_name.size()>0){
                    product.setName(product_name.get(0));
                }else {
                    product.setName(document.getFieldValue("product_name").toString());
                }

                list.add(product);

            }
            return new ResultBean("200", list);
        } catch (SolrServerException |IOException e) {
            e.printStackTrace();
            return new ResultBean("404", "查询失败");
        }

    }

    @Override
    public ResultBean updateById(Long id) {
        TProduct product=productMapper.selectByPrimaryKey(id);
        SolrInputDocument document=new SolrInputDocument();
        document.setField("id", product.getId());
        document.setField("product_name", product.getName());
        document.setField("product_price", product.getPrice()) ;
        document.setField("product_sale_point", product.getSalePoint());
        document.setField("product_sale_prive", product.getSalePrive());
        document.setField("product_images", product.getImages());

        try {
            solrClient.add(document);
            solrClient.commit();
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
            return new ResultBean("404", "数据同步不成功");
        }
        return new ResultBean("200", "数据同步成功");
    }

    @Override
    public ResultBean searchKeyWord(String keyWord, int indexPage, int pageSize) {
        SolrQuery condition=new SolrQuery();
        if(!StringUtils.isAnyEmpty(keyWord)){
            condition.setQuery("product_keywords:"+keyWord);
        }else {
            condition.setQuery("product_keywords:*");
        }
        condition.setHighlight(true);
        condition.addHighlightField("product_name");
        condition.setHighlightSimplePre("<font color='red'>");
        condition.setHighlightSimplePost("</font>");
        condition.setStart((indexPage-1)*pageSize);
        condition.setRows(pageSize);
        List<TProduct> list=null;
        try {
            QueryResponse response = solrClient.query(condition);
            SolrDocumentList documents = response.getResults();
            list=new ArrayList<>(documents.size());
            Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
            for (SolrDocument document : documents) {
                TProduct product=new TProduct();
                product.setId(Long.parseLong(document.get("id").toString()));
                product.setPrice(Long.parseLong(document.get("product_price").toString()));
                product.setSalePoint(document.get("product_sale_point").toString());
                product.setSalePrive(Long.parseLong(document.get("product_sale_prive").toString()));
                product.setImages(document.get("product_images").toString());
                Map<String, List<String>> map = highlighting.get(document.getFieldValue("id"));
                List<String> product_name = map.get("product_name");
                if(product_name!=null&&product_name.size()>0){
                    product.setName(product_name.get(0));
                }else {
                    product.setName(document.getFieldValue("product_name").toString());
                }
                list.add(product);
            }
            PageResultBean<TProduct> page=new PageResultBean<>();
            page.setPageSize(pageSize);
            int total = (int)documents.getNumFound();
            page.setTotal(total);
            page.setPages(total%pageSize==0?total/pageSize:(total/pageSize+1));
            page.setNavigatePages(total%pageSize==0?total/pageSize:(total/pageSize+1));
            page.setPageNum(indexPage);
            page.setList(list);
            return new ResultBean("200", page);
        } catch (SolrServerException |IOException e) {
            e.printStackTrace();
            return new ResultBean("404", "查询失败");
        }
    }


}
