//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.freight.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.oomall.freight.dao.bo.Logistics;
import cn.edu.xmu.oomall.freight.mapper.generator.LogisticsPoMapper;
import cn.edu.xmu.oomall.freight.mapper.generator.po.LogisticsPo;
import cn.edu.xmu.oomall.freight.mapper.generator.po.LogisticsPoExample;
import cn.edu.xmu.oomall.freight.service.dto.SimpleLogisticsDto;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mysql.cj.log.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static cn.edu.xmu.javaee.core.util.Common.*;

@Repository
public class LogisticsDao {

    private static Logger logger = LoggerFactory.getLogger(LogisticsDao.class);

    public static final String KEY = "L%d";

    private RedisUtil redisUtil;

    private LogisticsPoMapper logisticsPoMapper;

    @Autowired
    public LogisticsDao(RedisUtil redisUtil, LogisticsPoMapper logisticsPoMapper) {
        this.redisUtil = redisUtil;
        this.logisticsPoMapper = logisticsPoMapper;
    }

    private Logistics getBo(LogisticsPo po){
        Logistics bo = Logistics.builder().logisticClass(po.getLogisticsClass())
                .name(po.getName()).secret(po.getSecret()).appId(po.getAppId())
                .snPattern(po.getSnPattern()).id(po.getId()).build();
        return bo;
    }

    private boolean pattern(String key,String code){
        Pattern pattern=Pattern.compile(key);
        Matcher matcher=pattern.matcher(code);
        return matcher.find();
    }
    private SimpleLogisticsDto getSimpleLogisticsDto(LogisticsPo po) {
        SimpleLogisticsDto simpleLogisticsDto=new SimpleLogisticsDto();
        simpleLogisticsDto.setId(po.getId());
        simpleLogisticsDto.setName(po.getName());
        return simpleLogisticsDto;
    }

    public List<SimpleLogisticsDto> findLogisticsByBillCode(String code){
        List<LogisticsPo> logisticsPoList=logisticsPoMapper.selectIdAndNameAndPattern();

        //没有给条件 返回所有物流
        if(null==code)
        {
            List<SimpleLogisticsDto> simpleLogisticsDtoList=logisticsPoList.stream()
                    .map(po->getSimpleLogisticsDto(po))
                    .collect(Collectors.toList());
            return simpleLogisticsDtoList;
        }

        List<SimpleLogisticsDto> ret=logisticsPoList.stream().filter(po -> pattern(po.getSnPattern(),code))
                .map(po -> getSimpleLogisticsDto(po))
                .collect(Collectors.toList());
        logger.debug("listSize = {}",ret.size());
        if(ret.size()==0)
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST,String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "物流渠道", 1L));
        return ret;
    }



    /**
     * 按照主键获得对象
     * @author zwr
     * <p>
     * date: 2022-11-20 11:44
     * @param id
     * @return
     * @throws RuntimeException
     */
    public Logistics findById(Long id) throws RuntimeException {
        Logistics channel = null;
        if (null != id) {
            logger.debug("findObjById: id = {}",id);
            String key = String.format(KEY, id);
            if (redisUtil.hasKey(key)) {
                channel = (Logistics) redisUtil.get(key);
            } else {
                LogisticsPo po = this.logisticsPoMapper.selectByPrimaryKey(id);
                if (null == po) {
                    return null;
                }
                channel = getBo(po);
                //永不过期
                redisUtil.set(key, channel, -1);
            }
        }
        return channel;
    }

}
