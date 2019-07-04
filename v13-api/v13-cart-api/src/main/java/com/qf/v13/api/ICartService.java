package com.qf.v13.api;

import com.qf.v13.common.pojo.ResultBean;

/**
 * @author maizifeng
 * @Date 2019/6/29
 */
public interface ICartService {
    public ResultBean add(String uuid,Long productId,int count);

    public ResultBean del(String uuid,Long productId);

    public ResultBean update(String uuid,Long productId,int count);

    public ResultBean select(String uuid);

    ResultBean add(String uuid, Long productId, int count, String touristUuid);

    void merge(String touristUuid,Long userId);
}
