//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.order.service.listener;

import cn.edu.xmu.javaee.core.util.JacksonUtil;
import cn.edu.xmu.oomall.order.dao.bo.Order;
import cn.edu.xmu.oomall.order.service.OrderService;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@RocketMQTransactionListener
public class OrderListener implements RocketMQLocalTransactionListener {

    private static final Logger logger = LoggerFactory.getLogger(OrderListener.class);

    private final OrderService orderService;

    @Autowired
    public OrderListener(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * 事务消息发送成功回调
     *
     * @param msg 消息
     * @return RocketMQLocalTransactionState
     */
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        String body = new String((byte[]) msg.getPayload(), StandardCharsets.UTF_8);
        try {
            List<Order> orderList = JacksonUtil.toObj(body, List.class);
            if (null == orderList) {
                logger.info("orderList is null");
                return RocketMQLocalTransactionState.ROLLBACK;
            }
            this.orderService.saveOrders(orderList);
        } catch (Exception e) {
            return RocketMQLocalTransactionState.ROLLBACK;
        }
        return RocketMQLocalTransactionState.COMMIT;
    }

    /**
     * 检查事务是否执行成功
     *
     * @param msg 消息
     * @return RocketMQLocalTransactionState
     */
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        String body = new String((byte[]) msg.getPayload(), StandardCharsets.UTF_8);
        try {
            List<Order> orderList = JacksonUtil.toObj(body, List.class);
            if (null == orderList) {
                logger.info("orderList is null");
                return RocketMQLocalTransactionState.ROLLBACK;
            }
            // 判断是否存在某个订单编号无对应的订单
            // 由于保存订单列表是一个事务操作，因此只要有一个订单编号无对应的订单，就表该批订单都未存入数据库
            if (orderList.stream().map(Order::getOrderSn).anyMatch((orderSn) -> !orderService.checkIfExistByOrderSn(orderSn))) {
                return RocketMQLocalTransactionState.COMMIT;
            }
        } catch (Exception e) {
            return RocketMQLocalTransactionState.ROLLBACK;
        }
        // 如果本地事务执行成功，在第二个 if 分支中就应该返回 COMMIT，因此此处应返回 ROLLBACK
        return RocketMQLocalTransactionState.ROLLBACK;
    }
}
