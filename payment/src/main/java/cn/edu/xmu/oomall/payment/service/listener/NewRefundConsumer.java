package cn.edu.xmu.oomall.payment.service.listener;

import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.JacksonUtil;
import cn.edu.xmu.oomall.payment.controller.vo.RefundVo;
import cn.edu.xmu.oomall.payment.service.RefundService;
import cn.edu.xmu.oomall.payment.service.dto.OrderDto;
import cn.edu.xmu.oomall.payment.service.dto.RefundTransDto;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.messaging.Message;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * 消费订单模块发出的发起支付操作
 */
@Service
@RocketMQMessageListener(topic = "New-Refund", consumerGroup = "payment-new-refund", consumeThreadMax = 1)
public class NewRefundConsumer implements RocketMQListener<Message> {

    private static final Logger logger = LoggerFactory.getLogger(NewRefundConsumer.class);

    private RefundService refundService;
    private RocketMQTemplate rocketMQTemplate;

    @Autowired
    public NewRefundConsumer(RefundService refundService,RocketMQTemplate rocketMQTemplate) {
        this.refundService = refundService;
        this.rocketMQTemplate = rocketMQTemplate;
    }

    @Override
    public void onMessage(Message message) {
        String body = new String((byte[]) message.getPayload(), StandardCharsets.UTF_8);
        OrderDto order = (OrderDto) Objects.requireNonNull(message.getHeaders().get("order"));
        UserDto user = (UserDto) Objects.requireNonNull(message.getHeaders().get("user"));
        Long paymentId = (Long) Objects.requireNonNull(message.getHeaders().get("payment"));
        RefundVo vo = JacksonUtil.toObj(body, RefundVo.class);
        if (null == order || null == vo || null == user) {
            logger.error("RefundResultConsumer: wrong format.... body = {}", body);
        } else {
            RefundTransDto dto = refundService.createRefund(order.getShopId(),paymentId,vo.getAmount(),vo.getDivAmount(),user);
            if (dto != null) {
                String str = JacksonUtil.toJson(dto);
                Message<String> msg = MessageBuilder.withPayload(str).setHeader("customer", user).setHeader("order", order).build();
                rocketMQTemplate.sendOneWay("Pay-result:1", msg);
            }
        }
    }
}