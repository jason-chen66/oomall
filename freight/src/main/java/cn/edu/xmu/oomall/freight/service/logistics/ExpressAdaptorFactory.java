package cn.edu.xmu.oomall.freight.service.logistics;

import cn.edu.xmu.oomall.freight.dao.bo.Logistics;
import cn.edu.xmu.oomall.freight.dao.bo.ShopLogistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class ExpressAdaptorFactory {

    private static final Logger logger = LoggerFactory.getLogger(ExpressAdaptorFactory.class);

    private ApplicationContext context;

    @Autowired
    public ExpressAdaptorFactory(ApplicationContext context) {
        this.context = context;
    }

    /**
     * 返回仓库的支付渠道服务
     * 简单工厂模式
     *
     * @param shopLogistics 仓库快递渠道
     * @return
     * @author 庄婉如
     * <p>
     * date: 2022-12-1
     */
    public ExpressAdaptor createExpressAdaptorByShop(ShopLogistics shopLogistics) {
        String logisticsClass= shopLogistics.getLogistics().getLogisticsClass();
        return (ExpressAdaptor) context.getBean(logisticsClass);
    }

}