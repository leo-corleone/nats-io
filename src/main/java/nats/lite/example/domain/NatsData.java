package nats.lite.example.domain;

import java.io.Serializable;

/**
 * @author: suen
 * @time: 2023/7/30
 * @description:
 **/


public class NatsData implements Serializable {

    private Integer id;

    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "NatsData{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
