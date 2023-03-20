package cn.edu.xmu.oomall.freight.dao.bo.handler;

public class DeliveryHandlerChainBuilder {
    private DeliveryHandler head;
    private DeliveryHandler tail;

    public DeliveryHandlerChainBuilder addHandler(DeliveryHandler handler) {
        if (this.head == null) {
            this.head = this.tail = handler;
            return this;
        }
        this.tail.next(handler);
        this.tail = handler;
        return this;
    }

    public DeliveryHandler build() {
        return this.head;
    }
}