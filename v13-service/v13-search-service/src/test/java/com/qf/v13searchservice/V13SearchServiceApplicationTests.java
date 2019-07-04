package com.qf.v13searchservice;

import com.qf.v13.common.pojo.ResultBean;
import com.qf.v13.api.ISearchService;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class V13SearchServiceApplicationTests {

    @Autowired
    private SolrClient solrClient;
    @Autowired
    private ISearchService searchService;
    @Test
    public void synAllData(){
        ResultBean resultBean = searchService.synAllData();
        String statusCode = resultBean.getStatusCode();
        System.out.println(statusCode);
    }
    @Test
    public void queryTest(){
        ResultBean resultBean = searchService.searchKeyWord("笔记本");
        System.out.println(resultBean.getData());
    }


    @Test
    public void contextLoads() throws IOException, SolrServerException {
        SolrInputDocument document=new SolrInputDocument();
        document.setField("id", 1) ;
        document.setField("product_name", "小米笔记本");
        document.setField("product_price", "3333") ;
        document.setField("product_sale_point", "小");
        document.setField("product_images", "123");
        solrClient.add("collection2", document);
        solrClient.commit("collection2");
        System.out.println("添加索引库成功");
    }

    @Test
    public void qeuery() throws IOException, SolrServerException {
        SolrQuery query=new SolrQuery();
        query.setQuery("product_name:笔记本");

        QueryResponse response=solrClient.query(query);
        SolrDocumentList results = response.getResults();
        for (SolrDocument result : results) {
            System.out.println("product_name---"+result.get("product_name"));
        }
    }

    @Test
    public void delTest() throws IOException, SolrServerException {
//        UpdateResponse response = solrClient.deleteById("2");
        solrClient.deleteByQuery("product_name:小米笔记本");
        solrClient.commit();

    }
}
