package cn.edu.xmu.oomall.order.service.orderProcessor;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.JacksonUtil;
import cn.edu.xmu.oomall.order.dao.bo.Order;
import cn.edu.xmu.oomall.order.dao.bo.OrderPayment;
import cn.edu.xmu.oomall.order.dao.openfeign.dto.PaymentResultDto;
import cn.edu.xmu.oomall.order.dao.openfeign.dto.RefundResultDto;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service("AdvSaleProcessor")
public class AdvSaleProcessor implements OrderProcessor {
    private RocketMQTemplate rocketMQTemplate;
    @Autowired
    public AdvSaleProcessor(RocketMQTemplate rocketMQTemplate){
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
            order.setStatus(Order.BALANCE_UNPAID);
        }
        else if(order.getStatus().equals(Order.BALANCE_UNPAID)){
            order.setStatus(Order.PAID);
        }
        else throw new BusinessException(ReturnNo.STATENOTALLOW,ReturnNo.STATENOTALLOW.getMessage());
    }

    /**
     * 退款处理导致的状态迁移和消息发送
     *
     * @param order
     * @param cancelType
     */
    @Override
    public void orderRefundStatus(Order order, Byte cancelType, UserDto user) throws BusinessException{
        //此时允许待退款或直接取消
        if(order.allowStatus(Order.NOT_REFUNDED)||order.allowStatus(Order.REFUNDED)){
            //因活动取消，全额退款
            if(cancelType.equals(OrderProcessor.ACT_CANCEL)){
                order.setStatus(Order.NOT_REFUNDED);
                List<OrderPayment> paymentList = order.getOrderPayments();
                createRefundMsg(order, user, paymentList);
            }
            //未支付尾款退款
            else if(order.getStatus().equals(Order.BALANCE_UNPAID)){
                //商店取消退定金,只有一笔
                if(cancelType.equals(OrderProcessor.SHOP_CANCEL)){
                    order.setStatus(Order.NOT_REFUNDED);
                    List<OrderPayment> paymentList = order.getOrderPayments();
                    createRefundMsg(order, user, paymentList);
                }
                //未支付尾款且顾客取消不退钱
                else if(cancelType.equals(OrderProcessor.CUSTOMER_CANCEL)) {
                    order.setStatus(Order.REFUNDED);
                }
            }
            //新订单取消
            else if(order.getStatus().equals(Order.NEW)){
                order.setStatus(Order.REFUNDED);
            }
            //已支付尾款退款或者未发货
            else {
                order.setStatus(Order.NOT_REFUNDED);
                //商家取消退全款
                if(cancelType.equals(OrderProcessor.SHOP_CANCEL)) {
                    List<OrderPayment> paymentList = order.getOrderPayments();
                    createRefundMsg(order, user, paymentList);
                }
                //顾客取消只退尾款
                else if(cancelType.equals(OrderProcessor.CUSTOMER_CANCEL)){
                    List<OrderPayment> paymentList = order.getOrderPayments();
                    //找到时间较晚的一笔进行退款
                    OrderPayment payment = paymentList.stream().reduce((x,y)->{
                        if(x.getGmtCreate().isAfter(y.getGmtCreate())) return x;
                        else return y;
                    }).get();
                    List<OrderPayment> confirmedList = new ArrayList<>();
                    confirmedList.add(payment);
                    createRefundMsg(order, user, confirmedList);
                }
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