package com.javaproject.topjava.to;

import com.javaproject.topjava.HasId;

public abstract class BaseTo implements HasId {

    protected Integer id;


    public BaseTo() {

    }
    public BaseTo(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean isNew() {
        return HasId.super.isNew();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ":" + id;
    }



}
