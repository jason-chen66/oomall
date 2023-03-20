package cn.edu.xmu.oomall.payment.service.listener;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.JacksonUtil;
import cn.edu.xmu.oomall.payment.controller.vo.OrderPayVo;
import cn.edu.xmu.oomall.payment.controller.vo.RefundVo;
import cn.edu.xmu.oomall.payment.service.PaymentService;
import cn.edu.xmu.oomall.payment.service.dto.OrderDto;
import cn.edu.xmu.oomall.payment.service.dto.PayTransDto;
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
@RocketMQMessageListener(topic = "New-Payment", consumerGroup = "payment-new-payment", consumeThreadMax = 1)
public class NewPaymentConsumer implements RocketMQListener<Message> {

    private static final Logger logger = LoggerFactory.getLogger(NewPaymentConsumer.class);

    private PaymentService paymentService;
    private RocketMQTemplate rocketMQTemplate;

    @Autowired
    public NewPaymentConsumer(PaymentService paymentService, RocketMQTemplate rocketMQTemplate) {
        this.paymentService = paymentService;
        this.rocketMQTemplate = rocketMQTemplate;
    }

    @Override
    public void onMessage(Message message) throws BusinessException {
        String body = new String((byte[]) message.getPayload(), StandardCharsets.UTF_8);
        OrderDto order = (OrderDto) Objects.requireNonNull(message.getHeaders().get("order"));
        UserDto user = (UserDto) Objects.requireNonNull(message.getHeaders().get("user"));
        OrderPayVo vo = JacksonUtil.toObj(body, OrderPayVo.class);
        if (null != order && null != vo && null != user) {
            PayTransDto dto = paymentService.createPayment(vo.getTimeBegin(), vo.getTimeEnd(), vo.getSpOpenid(), vo.getShopChannelId(), vo.getAmount(), vo.getDivAmount(), user);
            String str = JacksonUtil.toJson(dto);
            Message<String> msg = MessageBuilder.withPayload(str).setHeader("customer", user).setHeader("order", order).build();
            rocketMQTemplate.send("Pay-result:1", msg);

        } else {
            logger.error("RefundResultConsumer: wrong format.... body = {}", body);
        }
    }

}