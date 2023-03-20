package cn.edu.xmu.oomall.order.service.listener;

import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.JacksonUtil;
import cn.edu.xmu.oomall.order.dao.bo.Order;
import cn.edu.xmu.oomall.order.dao.bo.OrderPayment;
import cn.edu.xmu.oomall.order.dao.openfeign.dto.PaymentResultDto;
import cn.edu.xmu.oomall.order.service.OrderListenerService;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.springframework.messaging.Message;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * 消费订单模块发出的发起支付消息
 */
@Service

@RocketMQMessageListener(topic = "Pay-result", selectorExpression = "1", consumeMode = ConsumeMode.CONCURRENTLY, consumeThreadMax = 10, consumerGroup = "order-group")
public class PayResultConsumer implements RocketMQListener<Message> {
    private static final Logger logger = LoggerFactory.getLogger(PayResultConsumer.class);

    private final OrderListenerService orderListenerService;

    @Autowired
    public PayResultConsumer(OrderListenerService orderListenerService) {
        this.orderListenerService = orderListenerService;
    }

    @Override
    public void onMessage(Message message) {
        String body = new String((byte[]) message.getPayload(), StandardCharsets.UTF_8);
        Order order = (Order) Objects.requireNonNull(message.getHeaders().get("order"));
        UserDto user = (UserDto) Objects.requireNonNull(message.getHeaders().get("customer"));
        PaymentResultDto dto = JacksonUtil.toObj(body, PaymentResultDto.class);
        if (null != dto && order != null && user != null) {
            OrderPayment orderPayment = OrderPayment.builder().orderId(order.getId()).paymentId(dto.getId()).creatorId(dto.getCreatorId()).creatorName(dto.getCreatorName()).build();
            orderListenerService.saveOrderPayment(order, orderPayment, user);
        } else {
            logger.error("PayResultConsumer: wrong format.... body = {}", body);
        }
    }
}
