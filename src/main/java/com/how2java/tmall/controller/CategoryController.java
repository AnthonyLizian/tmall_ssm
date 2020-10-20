package com.how2java.tmall.controller;

import com.how2java.tmall.pojo.Category;
import com.how2java.tmall.service.CategoryService;
import com.how2java.tmall.util.ImageUtil;
import com.how2java.tmall.util.Page;
import com.how2java.tmall.util.UploadedImageFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @RequestMapping("admin_category_list")
    public String list(Model model, Page page){
        List<Category> cs= categoryService.list(page);
        int total = categoryService.total();
        page.setTotal(total);
        model.addAttribute("cs", cs);
        model.addAttribute("page", page);
        return "admin/listCategory";
    }

    @RequestMapping("admin_category_add")
    //参数 session 用于在后续获取当前应用的路径
    //UploadedImageFile 用于接受上传的图片
    public String add(Category c, HttpSession session, UploadedImageFile uploadedImageFile) throws IOException {
            categoryService.add(c);
            //通过session获取ServletContext,再通过getRealPath定位存放分类图片的路径。
            File imageFolder= new File(session.getServletContext().getRealPath("img/category"));
            //根据分类id创建文件名
            File file = new File(imageFolder,c.getId()+".jpg");
            //如果/img/category目录不存在，则创建该目录，否则后续保存浏览器传过来图片，会提示无法保存
            if(!file.getParentFile().exists())
                file.getParentFile().mkdirs();
            //通过UploadedImageFile 把浏览器传递过来的图片保存在上述指定的位置
            uploadedImageFile.getImage().transferTo(file);
            //通过ImageUtil.change2jpg(file); 确保图片格式一定是jpg，而不仅仅是后缀名是jpg.
            BufferedImage img = ImageUtil.change2jpg(file);
            //写入图片
            ImageIO.write(img, "jpg", file);
            return "redirect:/admin_category_list";
        }

    @RequestMapping("admin_category_delete")
    //提供session参数，用于后续定位文件位置
    public String delete(int id,HttpSession session) throws IOException {
        categoryService.delete(id);
        //通过session获取ControllerContext然后获取分类图片位置，接着删除分类图片
        File  imageFolder= new File(session.getServletContext().getRealPath("img/category"));
        File file = new File(imageFolder,id+".jpg");
        file.delete();
        return "redirect:/admin_category_list";
    }

    @RequestMapping("admin_category_edit")
    public String edit(int id,Model model) throws IOException {
        Category c= categoryService.get(id);
        model.addAttribute("c", c);
        return "admin/editCategory";
    }

    @RequestMapping("admin_category_update")
    //参数 session 用于在后续获取当前应用的路径
    //UploadedImageFile 用于接受上传的图片
    public String update(Category c, HttpSession session, UploadedImageFile uploadedImageFile) throws IOException {
        categoryService.update(c);
        MultipartFile image = uploadedImageFile.getImage();
        //判断是否有上传图片
        if(null!=image &&!image.isEmpty()){
            //如果有上传，那么通过session获取ControllerContext,再通过getRealPath定位存放分类图片的路径
            File  imageFolder= new File(session.getServletContext().getRealPath("img/category"));
            //根据分类id创建文件名
            File file = new File(imageFolder,c.getId()+".jpg");
            //通过UploadedImageFile 把浏览器传递过来的图片保存在上述指定的位置
            image.transferTo(file);
            //通过ImageUtil.change2jpg(file); 确保图片格式一定是jpg，而不仅仅是后缀名是jpg.
            BufferedImage img = ImageUtil.change2jpg(file);
            ImageIO.write(img, "jpg", file);
        }
        return "redirect:/admin_category_list";
    }
}