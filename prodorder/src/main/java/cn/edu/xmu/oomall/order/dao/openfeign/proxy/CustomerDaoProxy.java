package cn.edu.xmu.oomall.order.dao.openfeign.proxy;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.oomall.order.dao.openfeign.CustomerDao;
import cn.edu.xmu.oomall.order.dao.openfeign.dto.CouponDto;
import cn.edu.xmu.oomall.order.dao.openfeign.dto.CustomerDto;
import cn.edu.xmu.oomall.order.dao.openfeign.dto.SimpleCouponDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@Component("customerDaoProxy")
public class CustomerDaoProxy implements CustomerDao {

    @Autowired
    public CustomerDaoProxy(@Qualifier("customerDao") CustomerDao customerDao, RedisUtil redisUtil) {
        this.customerDao = customerDao;
        this.redisUtil = redisUtil;
    }

    @Override
    public InternalReturnObject<CustomerDto> findCustomer(Long shopId, Long id) {
        if (0 == customerTimeout) {
            return customerDao.findCustomer(shopId, id);
        }
        String key = String.format(CUSTOMER_DTO_KEY, id);
        if (redisUtil.hasKey(key)) {
            return new InternalReturnObject<>((CustomerDto) redisUtil.get(key));
        }
        InternalReturnObject<CustomerDto> ret = customerDao.findCustomer(shopId, id);
        if (ReturnNo.OK == ReturnNo.getByCode(ret.getErrno())) {
            redisUtil.set(key, (Serializable) ret.getData(), customerTimeout);
        }
        return ret;
    }

    @Override
    public InternalReturnObject<List<SimpleCouponDto>> retrieveCouponsByActivityId(Integer status, Long actId) {
        return customerDao.retrieveCouponsByActivityId(status, actId);
    }

    @Override
    public InternalReturnObject<CouponDto> findCouponById(Long shopId, Long id) {
        return customerDao.findCouponById(shopId, id);
    }

    @Value("${oomall.order.open-feign-timeout.customer-dao.customer-dto}")
    private static Long customerTimeout;

    private static String CUSTOMER_DTO_KEY = "CTD%d";

    private final CustomerDao customerDao;

    private final RedisUtil redisUtil;
}
