package cn.edu.xmu.oomall.order.dao;


import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.order.dao.bo.OrderPayment;
import cn.edu.xmu.oomall.order.dao.openfeign.PaymentDao;
import cn.edu.xmu.oomall.order.mapper.generator.OrderPaymentPoMapper;
import cn.edu.xmu.oomall.order.mapper.generator.po.OrderPaymentPo;
import cn.edu.xmu.oomall.order.mapper.generator.po.OrderPaymentPoExample;
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
public class OrderPaymentDao {
    private static final Logger logger = LoggerFactory.getLogger(OrderPaymentDao.class);

    private final OrderPaymentPoMapper orderPaymentPoMapper;

    @Autowired
    @Lazy
    public OrderPaymentDao(OrderPaymentPoMapper orderPaymentPoMapper) {
        this.orderPaymentPoMapper = orderPaymentPoMapper;
    }

    /**
     * 根据orderId拿到对应的付款订单
     *
     * @param id 订单id
     * @return 付款订单列表
     */
    public List<OrderPayment> retrieveOrderPaymentsByOrderId(Long id) throws RuntimeException {
        if (id == null) {
            throw new BusinessException(ReturnNo.PARAMETER_MISSED);
        }

        OrderPaymentPoExample example = new OrderPaymentPoExample();
        OrderPaymentPoExample.Criteria criteria = example.createCriteria();
        criteria.andOrderIdEqualTo(id);
        List<OrderPaymentPo> list = orderPaymentPoMapper.selectByExample(example);
        return list.stream().map(po -> OrderPayment.builder().id(po.getId()).orderId(po.getOrderId()).paymentId(po.getPaymentId()).creatorId(po.getCreatorId()).creatorName(po.getCreatorName()).gmtCreate(po.getGmtCreate()).gmtModified(po.getGmtModified()).modifierName(po.getModifierName()).modifierId(po.getModifierId()).build()).collect(Collectors.toList());
    }

    /**
     * 插入对象
     *
     * @param bo
     * @param user
     * @throws RuntimeException
     */
    public void save(OrderPayment bo, UserDto user) throws RuntimeException {
        logger.debug("insertObj: obj = {}", bo);

        OrderPaymentPo po = OrderPaymentPo.builder().orderId(bo.getOrderId()).paymentId(bo.getPaymentId()).id(bo.getId()).creatorId(bo.getCreatorId()).creatorName(bo.getCreatorName()).gmtCreate(bo.getGmtCreate()).build();

        putUserFields(po, "creator", user);
        putGmtFields(po, "create");

        int ret = orderPaymentPoMapper.insertSelective(po);
        bo.setId(po.getId());
        if (0 == ret) {
            throw new BusinessException(ReturnNo.INTERNAL_SERVER_ERR, String.format(ReturnNo.INTERNAL_SERVER_ERR.getMessage()));
        }
    }
}