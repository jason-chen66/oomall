//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.freight.controller;

import cn.edu.xmu.javaee.core.aop.Audit;
import cn.edu.xmu.javaee.core.aop.LoginUser;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.freight.controller.vo.ConfirmExpressVo;
import cn.edu.xmu.oomall.freight.controller.vo.ExpressVo;
import cn.edu.xmu.oomall.freight.service.ExpressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * 内部的接口
 */
@RestController
@RequestMapping(value = "/internal", produces = "application/json;charset=UTF-8")
public class InternalFreightController {

    private final Logger logger = LoggerFactory.getLogger(InternalFreightController.class);

    private ExpressService expressService;

    public InternalFreightController(ExpressService expressService)
    {
        this.expressService = expressService;
    }

    /**
     * 商户或管理员生成新的运单
     * @param shopId
     * @param expressVo
     * @param user
     * @return
     */
    @PostMapping("/shops/{shopId}/packages")
    @Audit(departName = "shops")
    public ReturnObject postPackage(@PathVariable Long shopId,
                                    @Validated @RequestBody ExpressVo expressVo,
                                    @LoginUser UserDto user)
    {
        // 创建运单服务
        return new ReturnObject(ReturnNo.CREATED,expressService.creatExpress(shopId,expressVo,user));
    }

    /**
     * 商户或管理员获取运单详情
     * @param shopId
     * @param billCode
     * @return
     */
    @GetMapping("shops/{shopId}/packages")
    @Audit(departName = "shops")
    public ReturnObject getPackage(@PathVariable long shopId,
                                   @RequestParam(required = true,defaultValue = "0") String billCode,
                                   @LoginUser UserDto user)
    {
        return new ReturnObject(ReturnNo.OK,expressService.getExpress(shopId,null,billCode,(byte) 0,user));
    }

    /**
     * 其他模块获取运单信息
     * @param id
     * @return
     */
    @GetMapping("packages/{id}")
    public ReturnObject getPackage(@PathVariable long id,
                                   @LoginUser UserDto user)
    {
        return new ReturnObject(ReturnNo.OK,expressService.getExpress(null, id, null, (byte)1,user));
    }

    /**
     * 商户签收包裹
     * @param shopId
     * @param id
     * @param confirmExpressVo
     * @return
     */
    @PutMapping("shop/{shopId}/packages/{id}/confirm")
    @Audit(departName = "shops")
    public ReturnObject confirmPackage(@PathVariable long shopId,
                                       @PathVariable long id,
                                       @Validated @RequestBody ConfirmExpressVo confirmExpressVo,
                                        @LoginUser UserDto user)
    {
        return new ReturnObject(ReturnNo.OK,expressService.confirmExpress(shopId,id,confirmExpressVo,user));
    }

    /**
     * 商户取消包裹运单
      * @param shopId
     * @param id
     * @return
     */
    @PutMapping("shops/{shopId}/packages/{id}/cancel")
    @Audit(departName = "shops")
    public ReturnObject cancelPackage(@PathVariable long shopId,
                                      @PathVariable long id,
                                      @LoginUser UserDto user) {

        return new ReturnObject(ReturnNo.OK,expressService.cancelExpress(shopId,id,user));
    }

}
