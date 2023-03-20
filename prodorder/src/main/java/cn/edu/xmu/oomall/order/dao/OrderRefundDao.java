package cn.edu.xmu.oomall.order.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.order.dao.bo.OrderPayment;
import cn.edu.xmu.oomall.order.dao.bo.OrderRefund;
import cn.edu.xmu.oomall.order.mapper.generator.OrderRefundPoMapper;
import cn.edu.xmu.oomall.order.mapper.generator.po.OrderPaymentPo;
import cn.edu.xmu.oomall.order.mapper.generator.po.OrderRefundPo;
import cn.edu.xmu.oomall.order.dao.openfeign.PaymentDao;
import cn.edu.xmu.oomall.order.mapper.generator.po.OrderRefundPoExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static cn.edu.xmu.javaee.core.util.Common.putGmtFields;
import static cn.edu.xmu.javaee.core.util.Common.putUserFields;

@Repository
public class OrderRefundDao {
    private static final Logger logger = LoggerFactory.getLogger(OrderRefundDao.class);
    private final OrderRefundPoMapper orderRefundPoMapper;

    @Autowired
    @Lazy
    public OrderRefundDao(OrderRefundPoMapper orderRefundPoMapper) {
        this.orderRefundPoMapper = orderRefundPoMapper;
    }

    /**
     * 根据orderId拿到对应的订单退款
     *
     * @param id 订单id
     * @return 订单退款
     */
    public List<OrderRefund> retrieveOrderRefundsByOrderId(Long id) throws RuntimeException {
        if (id == null) {
            throw new BusinessException(ReturnNo.PARAMETER_MISSED);
        }
        OrderRefundPoExample example = new OrderRefundPoExample();
        OrderRefundPoExample.Criteria criteria = example.createCriteria();
        criteria.andOrderIdEqualTo(id);
        List<OrderRefundPo> list = orderRefundPoMapper.selectByExample(example);
        List<OrderRefund> boList = list.stream().map(po -> OrderRefund.builder().id(po.getId()).orderId(po.getOrderId()).refundId(po.getRefundId()).creatorId(po.getCreatorId()).creatorName(po.getCreatorName()).gmtCreate(po.getGmtCreate()).gmtModified(po.getGmtModified()).modifierName(po.getModifierName()).modifierId(po.getModifierId()).point(po.getPoint().longValue()).build()).collect(Collectors.toList());
        return boList;
    }

    /**
     * 插入对象
     *
     * @param bo
     * @param user
     * @throws RuntimeException
     */
    public void save(OrderRefund bo, UserDto user) throws RuntimeException {
        logger.debug("insertObj: obj = {}", bo);
        OrderRefundPo po =  OrderRefundPo.builder().orderId(bo.getOrderId()).refundId(bo.getRefundId()).id(bo.getId()).creatorId(bo.getCreatorId()).creatorName(bo.getCreatorName()).gmtCreate(bo.getGmtCreate()).build();

        putUserFields(po, "creator", user);
        putGmtFields(po, "create");

        int ret = orderRefundPoMapper.insertSelective(po);
        bo.setId(po.getId());
        if (0 == ret) {
            throw new BusinessException(ReturnNo.INTERNAL_SERVER_ERR, String.format(ReturnNo.INTERNAL_SERVER_ERR.getMessage()));
        }
    }
}
