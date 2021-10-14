package dev.lucas.consumers;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Order {

    @JsonProperty("uuid")
    private String uuid;

    @JsonProperty("status")
    private String status;

    public Order(String uuid, String status) {
        this.uuid = uuid;
        this.status = status;
    }

    public Order() {
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Purchase{" +
                "uuid='" + uuid + '\'' +
                "status='" + status + '\'' +
                '}';
    }
}
