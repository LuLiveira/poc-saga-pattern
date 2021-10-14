package dev.lucas.repositories;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import java.util.UUID;

@Entity(name = "tb_order")
public class Order {

    @Id
    @JsonProperty("uuid")
    private String uuid;

    @JsonProperty("status")
    private String status;

    public Order(String uuid, String status) {
        this.uuid = uuid;
        this.status = status;
    }

    public String getUuid() {
        return uuid;
    }

    public Order() {
    }

    @PrePersist
    public void prePersist(){
        this.uuid = UUID.randomUUID().toString();
        this.status = "PENDING";
    }

    @Override
    public String toString() {
        return "Purchase{" +
                "uuid='" + uuid + '\'' +
                "status='" + status + '\'' +
                '}';
    }
}
