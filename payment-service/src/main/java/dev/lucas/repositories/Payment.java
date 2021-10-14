package dev.lucas.repositories;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import java.util.UUID;

@Entity(name = "payment_tb")
public class Payment {

    @Id
    private String uuid;

    private String status;

    private String orderId;

    public Payment() {
    }

    public Payment(String orderId, String status) {
        this.status = status;
        this.orderId = orderId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @PrePersist
    public void prePersist(){
        this.uuid = UUID.randomUUID().toString();
    }
}
