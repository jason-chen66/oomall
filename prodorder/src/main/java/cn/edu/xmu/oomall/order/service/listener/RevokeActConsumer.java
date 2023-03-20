package cn.edu.xmu.oomall.order.service.listener;

import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.JacksonUtil;
import cn.edu.xmu.oomall.order.service.OrderListenerService;
import cn.edu.xmu.oomall.order.dao.openfeign.dto.OrderItemActivityDto;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.springframework.messaging.Message;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

/**
 * 消费商品模块发出的团购活动结束消息
 */
@Service
@RocketMQMessageListener(topic = "Revoke-Activity", selectorExpression = "1", consumeMode = ConsumeMode.CONCURRENTLY, consumeThreadMax = 10, consumerGroup = "order-group")
public class RevokeActConsumer implements RocketMQListener<Message> {
    private static final Logger logger = LoggerFactory.getLogger(RevokeActConsumer.class);
    private final Byte SUCCESS =1;
    private OrderListenerService orderListenerService;

    @Autowired
    public RevokeActConsumer(OrderListenerService orderListenerService) {
        this.orderListenerService = orderListenerService;
    }

    @Override
    public void onMessage(Message message) {
        String content = new String((byte[]) message.getPayload(), StandardCharsets.UTF_8);
        OrderItemActivityDto activityDto = JacksonUtil.toObj(content, OrderItemActivityDto.class);
        UserDto user = (UserDto) message.getHeaders().get("user");
        Byte type = (Byte) message.getHeaders().get("type");
        if (null == activityDto || null == user){
            logger.error("RevokeActConsumer: wrong format.... content = {}",content);
        }else{
            assert type != null;
            if(type.equals(SUCCESS))
                orderListenerService.actSuccessOrder(activityDto.getId(),user);
            else
                orderListenerService.actFailOrder(activityDto.getId(),user);
        }
    }
}
