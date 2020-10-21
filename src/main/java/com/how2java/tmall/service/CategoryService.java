package com.how2java.tmall.service;
import com.how2java.tmall.pojo.Category;
import java.util.List;

public interface CategoryService{
    void add(Category category);

    void delete(int id);

    void update(Category category);

    Category get(int id);

    List<Category> list();
}