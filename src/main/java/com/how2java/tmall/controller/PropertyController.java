package com.how2java.tmall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.how2java.tmall.pojo.Category;
import com.how2java.tmall.pojo.Property;
import com.how2java.tmall.service.CategoryService;
import com.how2java.tmall.service.PropertyService;
import com.how2java.tmall.util.Page;

@Controller
@RequestMapping("")
public class PropertyController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    PropertyService propertyService;

    @RequestMapping("admin_property_list")
    public String list(int cid, Model model,  Page page) {
        Category c = categoryService.get(cid);
        PageHelper.offsetPage(page.getStart(),page.getCount());
        List<Property> ps = propertyService.list(cid);
        int total = (int) new PageInfo<>(ps).getTotal();
        page.setTotal(total);
        //设置属性列表对应的category
        page.setParam("&cid="+c.getId());

        model.addAttribute("ps", ps);
        //用于新增属性 c是所属分类
        model.addAttribute("c", c);
        model.addAttribute("page", page);

        return "admin/listProperty";
    }

    @RequestMapping("admin_property_add")
    public String add(Model model, Property p) {
        propertyService.add(p);
        //返回所属分类的属性列表页面
        return "redirect:admin_property_list?cid="+p.getCid();
    }

    @RequestMapping("admin_property_delete")
    public String delete(int id) {
        Property p = propertyService.get(id);
        //根据id删除属性，id是唯一键，不用所属分类
        propertyService.delete(id);
        //返回所属分类的属性列表页面
        return "redirect:admin_property_list?cid="+p.getCid();
    }

    @RequestMapping("admin_property_edit")
    public String edit(Model model, int id) {
        Property p = propertyService.get(id);
        Category c = categoryService.get(p.getCid());
        model.addAttribute("c", c);
        model.addAttribute("p", p);
        return "admin/editProperty";
    }

    @RequestMapping("admin_property_update")
    public String update(Property p) {
        propertyService.update(p);
        //返回所属分类的属性列表页面
        return "redirect:admin_property_list?cid="+p.getCid();
    }
}