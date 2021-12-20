package com.javaproject.topjava.to;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


public class RestaurantTo extends NamedTo {

    @Size(max = 50)
    @NotBlank
    String address;

    public RestaurantTo(Integer id, String name, String address) {
        super(id, name);
        this.address = address;
    }

    public RestaurantTo() {

    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "RestaurantTo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
