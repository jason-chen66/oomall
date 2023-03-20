package cn.edu.xmu.oomall.order.service.orderProcessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class OrderProcessorFactory {
    private static final Logger logger = LoggerFactory.getLogger(OrderProcessorFactory.class);

    private ApplicationContext context;
    @Autowired
    public OrderProcessorFactory(ApplicationContext context)
    {
        this.context = context;
    }

    /**
     * 创建指定类型的订单
     * 简单工厂模式
     *
     * @param beanName
     */
    public OrderProcessor createOrderProcessor(String beanName) {
        return (OrderProcessor) context.getBean(beanName);
    }
}
