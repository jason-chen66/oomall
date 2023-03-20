package cn.edu.xmu.oomall.freight.dao.bo.handler;

public abstract class DeliveryHandler {

    protected DeliveryHandler next;

    public  void next(DeliveryHandler next) {
        this.next = next;
    }

    public abstract DeliveryInfo doHandler(DeliveryInfo deliveryInfo);

}
