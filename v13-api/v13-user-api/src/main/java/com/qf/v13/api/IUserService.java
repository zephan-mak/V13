package com.qf.v13.api;

import com.qf.v13.common.base.IBaseService;
import com.qf.v13.common.pojo.ResultBean;
import com.qf.v13.entity.TUser;

/**
 * @author maizifeng
 * @Date 2019/6/25
 */
public interface IUserService extends IBaseService<TUser> {
    ResultBean login(TUser user);

    ResultBean checkLogin(String uuid);

    ResultBean loginOut(String uuid);

    ResultBean eqCode(TUser user, String phoneCode);

    ResultBean activation(String uuid);

    void keepReferer(String referer);

    String getReferer();
}
