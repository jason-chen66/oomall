package cn.edu.xmu.oomall.order.service.listener;


import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.JacksonUtil;
import cn.edu.xmu.oomall.order.dao.bo.Order;
import cn.edu.xmu.oomall.order.dao.bo.OrderRefund;
import cn.edu.xmu.oomall.order.dao.openfeign.dto.RefundResultDto;
import cn.edu.xmu.oomall.order.service.OrderListenerService;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.messaging.Message;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
/**
 * 消费支付模块发出的退款结果
 */
@Service
@RocketMQMessageListener(topic = "refund-Result", selectorExpression = "1", consumeMode = ConsumeMode.CONCURRENTLY, consumeThreadMax = 10, consumerGroup = "order-group")
public class RefundResultConsumer implements RocketMQListener<Message> {
    private static final Logger logger = LoggerFactory.getLogger(RefundResultConsumer.class);

    private final OrderListenerService orderListenerService;

    @Autowired
    public RefundResultConsumer(OrderListenerService orderListenerService) {
        this.orderListenerService = orderListenerService;
    }

    @Override
    public void onMessage(Message message) {
        String body = new String((byte[]) message.getPayload(), StandardCharsets.UTF_8);
        Order order = (Order) Objects.requireNonNull(message.getHeaders().get("order"));
        Long point = (Long) Objects.requireNonNull(message.getHeaders().get("point"));
        UserDto user = (UserDto) Objects.requireNonNull(message.getHeaders().get("user"));
        RefundResultDto dto = JacksonUtil.toObj(body, RefundResultDto.class);
        if (null == order) {
            logger.error("RefundResultConsumer: wrong format.... body = {}", body);
        } else {
            OrderRefund bo = OrderRefund.builder().orderId(order.getId()).refundId(dto.getId()).point(point).build();
            orderListenerService.saveOrderRefund(order, bo, user);
        }
    }
}
