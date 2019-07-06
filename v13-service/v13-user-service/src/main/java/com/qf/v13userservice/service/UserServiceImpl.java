package com.qf.v13userservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.v13.api.IUserService;
import com.qf.v13.common.base.BaseServiceImpl;
import com.qf.v13.common.base.IBaseDao;
import com.qf.v13.common.pojo.ResultBean;
import com.qf.v13.common.utils.TokenUtils;
import com.qf.v13.entity.TUser;
import com.qf.v13.mapper.TUserMapper;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashMap;

/**
 * @author maizifeng
 * @Date 2019/6/25
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<TUser> implements IUserService {

    @Autowired
    private TUserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public IBaseDao<TUser> getBaseDao() {
        return userMapper;
    }

    @Override
    public int insertSelective(TUser record) {
         super.insertSelective(record);
        return record.getId().intValue();
    }

    /**
     * 登录
     * @param user
     * @return
     */
    @Override
    public ResultBean login(TUser user) {
        TUser currentUser = userMapper.selectByUsername(user.getUsername());
        if(currentUser!=null){
            if(passwordEncoder.matches(user.getPassword(), currentUser.getPassword())){
                String uuid = TokenUtils.createJwtToken(currentUser.getId().toString(),null,currentUser.getUsername(),30*60*1000 );
                return new ResultBean("200", uuid);
            }
        }
        return new ResultBean("404", null);
    }

    /**
     * 判断是否登录
     * @param uuid
     * @return
     */
    @Override
    public ResultBean checkLogin(String uuid) {
        try {
            Claims claims = TokenUtils.parseJWT(uuid);
            TUser user=new TUser();
            user.setId(Long.parseLong(claims.getId()));
            user.setUsername(claims.getSubject());

            long expirationTime = claims.getExpiration().getTime();
            long currentTime = System.currentTimeMillis();
            if(expirationTime>currentTime){
                uuid = TokenUtils.createJwtToken(user.getId().toString(), null, user.getUsername(), 30*60*1000);
            }
            HashMap<String,Object> map=new HashMap<>();
            map.put("user", user);
            map.put("uuid", uuid);
            return new ResultBean("200", map);
        } catch (Exception e) {
            System.out.println(e);
            return new ResultBean("404", "登录超时请重新登录");

        }
    }

    /**
     * 注销
     * @param uuid
     * @return
     */
    @Override
    public ResultBean loginOut(String uuid) {
        String key="user:token:"+uuid;
        Boolean delete = redisTemplate.delete(key);
        if(delete){
            return new ResultBean("200", "删除成功");
        }
        return new ResultBean("404", "删除失败");
    }

    /**
     * 配备手机验证码的注册
     * @param user
     * @param phoneCode
     * @return
     */
    @Override
    public ResultBean eqCode(TUser user, String phoneCode) {
        String codeOld = (String) redisTemplate.opsForValue().get("code");
        if (phoneCode.equals(codeOld)) {
//            user.setStatus(true);
            userMapper.insertSelective(user);
            return new ResultBean("200", user.getId().intValue());
        }
        return new ResultBean("404", "注册失败");
    }

    /**
     * 邮件激活的注册
     * @param uuid
     * @return
     */
    @Override
    public ResultBean activation(String uuid) {
        long id = (int)  redisTemplate.opsForValue().get(uuid);
        TUser user = userMapper.selectByPrimaryKey(id);
        if(user!=null){
            user.setStatus(true);
            int count = userMapper.updateByPrimaryKeySelective(user);
            if(count>0){
                return new ResultBean("200", "激活成功");
            }else {
                return new ResultBean("404", "激活失败");
            }
        }

        return null;
    }

    @Override
    public void keepReferer(String referer) {
        redisTemplate.opsForValue().set("referer", referer);
    }

    @Override
    public String getReferer() {
        String referer = (String) redisTemplate.opsForValue().get("referer");
        return referer;
    }
}
