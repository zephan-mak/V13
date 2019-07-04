package com.qf.v13cartservice;

import com.qf.v13.api.ICartService;
import com.qf.v13.common.pojo.ResultBean;
import com.qf.v13.pojo.CartItem;
import com.qf.v13.vo.CartItemVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.TreeSet;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class V13CartServiceApplicationTests {

    @Autowired
    private ICartService cartService;

    @Test
    public void addTest() {
        String uuid="b14131d0-a122-43e7-bf99-24fdadbdee90";
        ResultBean resultBean = cartService.add(uuid, 3L, 100);
        System.out.println(uuid);
        System.out.println(resultBean);
    }

    @Test
    public void delTest(){
        String uuid="b14131d0-a122-43e7-bf99-24fdadbdee90";
        ResultBean resultBean = cartService.del(uuid, 1L);
        System.out.println(resultBean);
    }

    @Test
    public void updateTest(){
        String uuid="b14131d0-a122-43e7-bf99-24fdadbdee90";
        ResultBean resultBean = cartService.update(uuid, 1L, 100);
        System.out.println(resultBean);
    }

    @Test
    public void selectTest(){
        String uuid="b14131d0-a122-43e7-bf99-24fdadbdee90";
        ResultBean resultBean = cartService.select(uuid);

        TreeSet<CartItemVO> cartItems = (TreeSet<CartItemVO>) resultBean.getData();
        for (CartItemVO cartItem : cartItems) {
            System.out.println(cartItem);
        }
    }

}
