package cn.edu.xmu.oomall.freight.dao;


import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.freight.dao.bo.Express;
import cn.edu.xmu.oomall.freight.dao.openfeign.RegionDao;
import cn.edu.xmu.oomall.freight.mapper.generator.ExpressPoMapper;
import cn.edu.xmu.oomall.freight.mapper.generator.po.ExpressPo;
import cn.edu.xmu.oomall.freight.mapper.generator.po.ExpressPoExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import java.util.List;

import static cn.edu.xmu.javaee.core.util.Common.putGmtFields;
import static cn.edu.xmu.javaee.core.util.Common.putUserFields;


@Repository
public class ExpressDao {
    private static final Logger logger = LoggerFactory.getLogger(ExpressDao.class);
    private ExpressPoMapper expressPoMapper;
    private RegionDao regionDao;
    private ShopLogisticsDao shopLogisticsDao;

    @Autowired
    @Lazy
    public ExpressDao(ExpressPoMapper expressPoMapper, RegionDao regionDao, ShopLogisticsDao shopLogisticsDao) {
        this.expressPoMapper = expressPoMapper;
        this.regionDao = regionDao;
        this.shopLogisticsDao = shopLogisticsDao;
    }

    private void setBo(Express bo)
    {
        bo.setRegionDao(this.regionDao);
        bo.setShopLogisticsDao(this.shopLogisticsDao);
    }

    /**
     * 获得Bo对象
     */
    private Express getBo(ExpressPo po)
    {
        Express bo = Express.builder().id(po.getId()).creatorId(po.getCreatorId()).creatorName(po.getCreatorName()).gmtCreate(po.getGmtCreate()).gmtModified(po.getGmtModified()).modifierId(po.getModifierId()).modifierName(po.getModifierName())
                .status(po.getStatus()).shopLogisticsId(po.getShopLogisticsId()).shopId(po.getShopId()).billCode(po.getBillCode()).senderRegionId(po.getSenderRegionId()).deliveryRegionId(po.getDeliverRegionId())
                .senderAddress(po.getSenderAddress()).deliveryAddress(po.getDeliverAddress()).senderName(po.getSenderName()).senderMobile(po.getSenderMobile()).deliveryName(po.getDeliverName()).deliveryMobile(po.getDeliverMobile()).build();
        this.setBo(bo);
        return bo;
    }

    /**
     * 获取bo对应的po
     * @param bo
     * @return
     */
    private ExpressPo getPo(Express bo)
    {
        ExpressPo po = ExpressPo.builder().status(bo.getStatus()).shopLogisticsId(bo.getShopLogisticsId()).shopId(bo.getShopId()).billCode(bo.getBillCode()).senderRegionId(bo.getSenderRegionId()).deliverRegionId(bo.getDeliveryRegionId())
                .senderAddress(bo.getSenderAddress()).deliverAddress(bo.getDeliveryAddress()).senderName(bo.getSenderName()).senderMobile(bo.getSenderMobile()).deliverName(bo.getDeliveryName()).deliverMobile(bo.getDeliveryMobile()).build();
        return po;
    }

    public Express findById(Long id) throws RuntimeException
    {
        Express ret;
        if(null != id)
        {
            ExpressPo po = expressPoMapper.selectByPrimaryKey(id);
            if(null == po)
                throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST,String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(),"packageId",id));
            else {
                ret = this.getBo(po);
                return ret;
            }
        }
        else
            throw new BusinessException(ReturnNo.PARAMETER_MISSED,String.format(ReturnNo.PARAMETER_MISSED.getMessage()));
    }

    /**
     * 通过物流运单号查找运单
     * @param billCode
     * @return
     */
    public  Express findByBillCode(String billCode)
    {
        Express ret;
        if(null != billCode)
        {
            ExpressPoExample example = new ExpressPoExample();
            ExpressPoExample.Criteria criteria = example.createCriteria();
            criteria.andBillCodeEqualTo(billCode);
            List<ExpressPo> pos = expressPoMapper.selectByExample(example);
            if(pos.size() == 0)
                throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST,String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(),"BillCode", billCode));
            ret = getBo(pos.get(0));
            return ret;
        }
        else
            throw new BusinessException(ReturnNo.PARAMETER_MISSED,String.format(ReturnNo.PARAMETER_MISSED.getMessage()));
    }

    /**
     * 生成运单
     * @param express
     * @param user
     */
    public Express insert(Express express, UserDto user) throws RuntimeException
    {
        ExpressPo expressPo = getPo(express);
        putUserFields(expressPo,"creator",user);
        putGmtFields(expressPo,"create");
        int ret = expressPoMapper.insert(expressPo);
        logger.info("insertObj:po = {}",expressPo);
        if(0 == ret)
        {
            throw new BusinessException(ReturnNo.INTERNAL_SERVER_ERR,String.format(ReturnNo.INTERNAL_SERVER_ERR.getMessage()));
        }
        return getBo(expressPo);
    }

    /**
     * 更新运单信息
     * @param express
     * @param user
     */
    public int saveById(Express express, UserDto user) throws RuntimeException
    {
        ExpressPo po = getPo(express);
        if(null != user)
        {
            putUserFields(po,"modifier",user);
            putGmtFields(po,"Modified");
        }
        ExpressPoExample example = new ExpressPoExample();
        ExpressPoExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(express.getId());
        int ret = expressPoMapper.updateByExampleSelective(po,example);
        if(0 == ret)
            throw new BusinessException(ReturnNo.INTERNAL_SERVER_ERR,String.format(ReturnNo.INTERNAL_SERVER_ERR.getMessage()));
        return ret;
    }

}
