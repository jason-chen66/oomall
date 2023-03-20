package cn.edu.xmu.oomall.order.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.order.dao.bo.Order;
import cn.edu.xmu.oomall.order.dao.bo.OrderItem;
import cn.edu.xmu.oomall.order.dao.openfeign.CustomerDao;
import cn.edu.xmu.oomall.order.dao.openfeign.GoodsDao;
import cn.edu.xmu.oomall.order.mapper.generator.OrderItemPoMapper;
import cn.edu.xmu.oomall.order.mapper.generator.po.OrderItemPo;
import cn.edu.xmu.oomall.order.mapper.generator.po.OrderItemPoExample;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static cn.edu.xmu.javaee.core.util.Common.*;

@Repository
public class OrderItemDao {
    private GoodsDao goodsDao;

    private CustomerDao customerDao;
    private final OrderItemPoMapper orderItemPoMapper;

    @Autowired
    @Lazy
    public OrderItemDao(OrderItemPoMapper orderItemPoMapper,
                        @Qualifier("customerDaoProxy") CustomerDao customerDao,
                        @Qualifier("goodsDaoProxy") GoodsDao goodsDao) {
        this.orderItemPoMapper = orderItemPoMapper;
        this.customerDao = customerDao;
        this.goodsDao = goodsDao;
    }

    /**
     * 根据orderId拿到对应的订单明细
     *
     * @param id 订单id
     * @return 订单明细列表
     */
    public List<OrderItem> retrieveOrderItemsByOrderId(Long id) throws RuntimeException {
        if (id == null) {
            throw new BusinessException(ReturnNo.PARAMETER_MISSED);
        }
        OrderItemPoExample example = new OrderItemPoExample();
        OrderItemPoExample.Criteria criteria = example.createCriteria();
        criteria.andOrderIdEqualTo(id);
        List<OrderItemPo> list = orderItemPoMapper.selectByExample(example);
        return list.stream().map(po -> getBo(po)).collect(Collectors.toList());

    }

    /**
     * 通过 po 获取 bo
     *
     * @param po po
     * @return bo
     */
    private OrderItem getBo(OrderItemPo po) {
        OrderItem bo = OrderItem.builder()
                .id(po.getId())
                .name(po.getName())
                .orderId(po.getOrderId())
                .actId(po.getActivityId())
                .couponId(po.getCouponId())
                .creatorId(po.getCreatorId())
                .creatorName(po.getCreatorName())
                .gmtCreate(po.getGmtCreate())
                .discountPrice(po.getDiscountPrice())
                .price(po.getPrice())
                .modifierId(po.getModifierId())
                .modifierName(po.getModifierName())
                .gmtModified(po.getGmtModified())
                .onsaleId(po.getOnsaleId())
                .point(po.getPoint()).build();
        setBo(bo);
        return bo;
    }

    /**
     * 设置 bo 中的 dao
     *
     * @param bo bo对象
     */
    private void setBo(OrderItem bo) {
        bo.setGoodsDao(this.goodsDao);
        bo.setCustomerDao(this.customerDao);
    }

    /**
     * 插入订单明细
     *
     * @param orderItemPo 订单明细po
     */
    public void insert(OrderItemPo orderItemPo) throws RuntimeException {
        int ret = orderItemPoMapper.insert(orderItemPo);
        if (0 == ret) {
            throw new BusinessException(ReturnNo.INTERNAL_SERVER_ERR, String.format(ReturnNo.INTERNAL_SERVER_ERR.getMessage()));
        }
    }

    /**
     * 更新对象
     *
     * @param orderItem
     * @param user
     * @throws RuntimeException
     */
    public void saveById(OrderItem orderItem, UserDto user) throws RuntimeException {
        OrderItemPo po = cloneObj(orderItem, OrderItemPo.class);
        if (null != user) {
            putUserFields(po, "modifier", user);
            putGmtFields(po, "modified");
        }
        int ret = orderItemPoMapper.updateByPrimaryKeySelective(po);
        if (0 == ret) {
            throw new BusinessException(ReturnNo.INTERNAL_SERVER_ERR, String.format(ReturnNo.INTERNAL_SERVER_ERR.getMessage()));
        }
    }

    public List<OrderItem> findByAct(Long actId) {
        if (actId == null) {
            throw new BusinessException(ReturnNo.PARAMETER_MISSED);
        }
        OrderItemPoExample example = new OrderItemPoExample();
        OrderItemPoExample.Criteria criteria = example.createCriteria();
        criteria.andOrderIdEqualTo(actId);
        List<OrderItemPo> list = orderItemPoMapper.selectByExample(example);
        return list.stream().map(po -> getBo(po)).collect(Collectors.toList());
    }
}
