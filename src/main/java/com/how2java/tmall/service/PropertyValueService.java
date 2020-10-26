package com.how2java.tmall.service;

import com.how2java.tmall.pojo.Product;
import com.how2java.tmall.pojo.PropertyValue;

import java.util.List;

public interface PropertyValueService {
    //初始化
    void init(Product p);

    PropertyValue get(int ptid, int pid);

    void update(PropertyValue pv);

    List<PropertyValue> list(int pid);
}