package com.qf.v13productservice;

import com.github.pagehelper.PageInfo;
import com.qf.v13.api.IProductService;
import com.qf.v13.api.IProductTypeService;
import com.qf.v13.entity.TProduct;
import com.qf.v13.entity.TProductType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class V13ProductServiceApplicationTests {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private IProductTypeService productTypeService;

    @Autowired
    private IProductService productService;

    @Test
    public void contextLoads() throws SQLException {

        /*List<TProductType> list = productTypeService.list();
        for (TProductType productType : list) {
            System.out.println(productType.getName());
        }*/

        PageInfo<TProduct> page = productService.page(1, 2);
        System.out.println(page.getList().size());
    }


}
