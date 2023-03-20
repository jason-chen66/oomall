package cn.edu.xmu.oomall.order.service.orderProcessor;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.JacksonUtil;
import cn.edu.xmu.oomall.order.dao.bo.Order;
import cn.edu.xmu.oomall.order.dao.bo.OrderPayment;
import cn.edu.xmu.oomall.order.dao.openfeign.dto.RefundResultDto;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("NormalProcessor")
public class NormalProcessor implements OrderProcessor {
    private RocketMQTemplate rocketMQTemplate;
    @Autowired
    public NormalProcessor(RocketMQTemplate rocketMQTemplate){
        this.rocketMQTemplate = rocketMQTemplate;
    }
    /**
     * 支付导致的状态迁移
     *
     * @param order
     */
    @Override
    public void orderPayStatus(Order order) throws BusinessException {
        if(order.getStatus().equals(Order.NEW)){
            order.setStatus(Order.PAID);
        }
        else throw new BusinessException(ReturnNo.STATENOTALLOW,ReturnNo.STATENOTALLOW.getMessage());
    }

    /**
     * 退款处理以及导致的状态迁移
     *
     * @param order
     * @param cancelType
     * @param user
     */
    @Override
    public void orderRefundStatus(Order order, Byte cancelType, UserDto user) {
        //此时允许待退款或直接取消
        if(order.allowStatus(Order.NOT_REFUNDED)||order.allowStatus(Order.REFUNDED)){
            //新订单直接取消
            if(order.getStatus().equals(Order.NEW)){
                order.setStatus(Order.REFUNDED);
            }
            //其他情况退全款
            else {
                List<OrderPayment> paymentList = order.getOrderPayments();
                order.setStatus(Order.NOT_REFUNDED);
                createRefundMsg(order, user, paymentList);
            }
        }
        else throw new BusinessException(ReturnNo.STATENOTALLOW,ReturnNo.STATENOTALLOW.getMessage());
    }

    private void createRefundMsg(Order order, UserDto user, List<OrderPayment> paymentList) {
        for (OrderPayment payment : paymentList) {
            //发消息到payment,发起退款
            String str = JacksonUtil.toJson(order.createRefund(payment.getPaymentId()));
            Message<String> msg = MessageBuilder.withPayload(str).setHeader("order",order).setHeader("customer", user).setHeader("payment", payment.getPaymentId()).build();
            rocketMQTemplate.send("New-refund:1", msg);
        }
    }
}
