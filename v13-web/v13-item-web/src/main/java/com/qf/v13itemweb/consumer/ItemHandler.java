package com.qf.v13itemweb.consumer;

import com.qf.v13.common.constant.RabbitMQConstant;
import com.qf.v13.common.utils.HttpClientUtils;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author maizifeng
 * @Date 2019/6/21
 */
@Component
public class ItemHandler {

    @RabbitHandler
    @RabbitListener(queues = RabbitMQConstant.CREATEHTML_BYID_QUEUE)
    public void createHtml(Long id){
        HttpClientUtils.doGet("http://localhost:9093/item/createHtml/"+id);
    }
}
