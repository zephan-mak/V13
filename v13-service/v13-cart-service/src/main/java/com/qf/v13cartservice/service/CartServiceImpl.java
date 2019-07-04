package com.qf.v13cartservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.v13.api.ICartService;
import com.qf.v13.common.pojo.ResultBean;
import com.qf.v13.mapper.TProductMapper;
import com.qf.v13.pojo.CartItem;
import com.qf.v13.vo.CartItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author maizifeng
 * @Date 2019/6/29
 */
@Service
public class CartServiceImpl implements ICartService {

    public final String COOKIENAME="user:cart:";


    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private TProductMapper productMapper;
    @Override
    public ResultBean add(String uuid, Long productId, int count) {
        String key=COOKIENAME+uuid;
        CartItem cartItem = (CartItem) redisTemplate.opsForHash().get(key, productId.toString());
        if(cartItem!=null){
            cartItem.setCount(count);
            cartItem.setUpdateTime(new Date());
        }else {
            cartItem=new CartItem(productId, count, new Date());
        }
        redisTemplate.opsForHash().put(key, productId.toString(), cartItem);
        redisTemplate.expire(key, 7, TimeUnit.DAYS);
        return new ResultBean("200", "添加购物车成功");
    }

    @Override
    public ResultBean del(String uuid, Long productId) {
        String key=COOKIENAME+uuid;
        Long result=redisTemplate.opsForHash().delete(key, productId.toString());
        if(result>0){
            redisTemplate.expire(key, 7, TimeUnit.DAYS);
            return new ResultBean("200", "删除商品成功");
        }
        return new ResultBean("404", "删除商品失败");
    }

    @Override
    public ResultBean update(String uuid, Long productId, int count) {
        String key=COOKIENAME+uuid;
        CartItem cartItem = (CartItem) redisTemplate.opsForHash().get(key, productId.toString());
        if(cartItem!=null){
            cartItem.setCount(count);
            cartItem.setUpdateTime(new Date());
            redisTemplate.opsForHash().put(key, productId.toString(), cartItem);
            redisTemplate.expire(key, 7, TimeUnit.DAYS);
            return new ResultBean("200", "更新购物车成功");
        }
        return new ResultBean("404", "更新购物车失败");
    }

    @Override
    public ResultBean select(String uuid) {
        String key=COOKIENAME+uuid;
        Map<Object,Object> cartMap = redisTemplate.opsForHash().entries(key);

//        TreeSet<CartItemVO> carts=new TreeSet<>(new Comparator<CartItemVO>() {
//            @Override
//            public int compare(CartItemVO o1, CartItemVO o2) {
//                return (int) (o2.getUpdateTime().getTime()-o1.getUpdateTime().getTime());
//            }
//        });

        TreeSet<CartItemVO> carts=new TreeSet<>();
        Set<Map.Entry<Object, Object>> cartSet = cartMap.entrySet();
        for (Map.Entry<Object, Object> cart : cartSet) {
            CartItem cartItem = (CartItem) cart.getValue();
            CartItemVO cartItemVO=new CartItemVO();
            cartItemVO.setCount(cartItem.getCount());
            cartItemVO.setUpdateTime(cartItem.getUpdateTime());
            cartItemVO.setProduct(productMapper.selectByPrimaryKey(cartItem.getProductId()));
            carts.add(cartItemVO);
        }
        redisTemplate.expire(key, 7, TimeUnit.DAYS);
        return new ResultBean("200", carts);
    }

    @Override
    public ResultBean add(String uuid, Long productId, int count, String touristUuid) {
        String key=COOKIENAME+uuid;
        String touristKey=COOKIENAME+touristUuid;
        CartItem cartItem = (CartItem) redisTemplate.opsForHash().get(key, productId.toString());
        if(cartItem!=null){
            cartItem.setCount(count);
            cartItem.setUpdateTime(new Date());
        }else {
            cartItem=new CartItem(productId, count, new Date());
        }
//        redisTemplate.opsForHash().put(key, productId.toString(), cartItem);
        Map<Object,Object> touristCartMap = redisTemplate.opsForHash().entries(touristKey);
        Set<Map.Entry<Object, Object>> entrySet = touristCartMap.entrySet();
        for (Map.Entry<Object, Object> objectObjectEntry : entrySet) {
            CartItem cartItem1 =(CartItem) objectObjectEntry.getValue();
            if(cartItem1.getProductId().equals(productId)){
                int count1 = cartItem.getCount();
                int count2 = cartItem1.getCount();
                int totalCount=count1+count2;
                cartItem.setCount(totalCount);
            }
            redisTemplate.opsForHash().put(key, productId.toString(), cartItem);
        }
        redisTemplate.opsForHash().put(key, productId.toString(), cartItem);
        redisTemplate.opsForHash().delete(touristKey,productId.toString());
//        redisTemplate.opsForZSet().unionAndStore(key, touristKey, key);
        redisTemplate.expire(key, 7, TimeUnit.DAYS);
        return new ResultBean("200", "添加购物车成功");
    }

    @Override
    public void merge(String touristUuid,Long userId) {
        String touristKey=COOKIENAME+touristUuid;
        String UserKey=COOKIENAME+userId;
        Map<Object,Object> UserCartMap = redisTemplate.opsForHash().entries(UserKey);
        Map<Object,Object> touristCartMap = redisTemplate.opsForHash().entries(touristKey);
        Set<Map.Entry<Object, Object>> touristEntrySet = touristCartMap.entrySet();
        if(touristEntrySet.size()!=0){
            if(UserCartMap.size()!=0){
                Set<Map.Entry<Object, Object>> userEntrySet = UserCartMap.entrySet();
                for (Map.Entry<Object, Object> userEntry : userEntrySet) {
                    CartItem UserCartItem = (CartItem) userEntry.getValue();
                    Long productId = UserCartItem.getProductId();
                    redisTemplate.opsForHash().put(UserKey, productId.toString(), UserCartItem);
                    for (Map.Entry<Object, Object> touristEntry : touristEntrySet) {
                        CartItem touristCartItem =(CartItem) touristEntry.getValue();
                        if(touristCartItem.getProductId().equals(UserCartItem.getProductId())){
                            int count1 = UserCartItem.getCount();
                            int count2 = touristCartItem.getCount();
                            int totalCount=count1+count2;
                            touristCartItem.setCount(totalCount);
                        }
                        redisTemplate.opsForHash().put(UserKey, touristCartItem.getProductId().toString(), touristCartItem);
                    }
                    redisTemplate.opsForHash().delete(touristKey,productId.toString());
                }
            }
        }




    }

}
