package cn.edu.xmu.oomall.order.dao.openfeign;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.oomall.order.dao.openfeign.dto.*;
import cn.edu.xmu.oomall.order.dao.openfeign.vo.PaymentVo;
import cn.edu.xmu.oomall.order.dao.openfeign.vo.RefundVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment-service")
public interface PaymentDao {
    /**
     * 查询一笔退款
     *
     * @param shopId 店铺id
     * @param id paymentId
     */
    @GetMapping("/internal/shops/{shopId}/refunds/{id}")
    InternalReturnObject<RefundCheckDto> findRefund(@PathVariable Long shopId, @PathVariable Long id);

    /**
     * 查询一笔支付
     *
     * @param shopId 店铺id
     */
    @GetMapping("/internal/shops/{shopId}/payments/{id}")
    InternalReturnObject<PaymentCheckDto> findPayment(@PathVariable Long shopId, @PathVariable Long id);
}
