package com.how2java.tmall.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
    public String list(Model model,Page page){
        //通过PageHelper插件指定start（第几页），count（每页行数）
        PageHelper.offsetPage(page.getStart(),page.getCount());
        //获取cs（分类列表）
        List<Category> cs= categoryService.list();
        //通过PageInfo获取total（总行数）
        int total = (int) new PageInfo<>(cs).getTotal();
        //把total（总行数）赋给page
        page.setTotal(total);
        //把cs（分类列表）和page传到listCategory.jpg
        model.addAttribute("cs", cs);
        model.addAttribute("page", page);
        return "admin/listCategory";
    }

    @RequestMapping("admin_category_add")
    //session：获取存放图片的路径
    //UploadedImageFile：上传的照片
    public String add(Category c, HttpSession session, UploadedImageFile uploadedImageFile) throws IOException {
        //添加分类
        categoryService.add(c);
        //imageFolder：存放路径（img/category）
        File imageFolder= new File(session.getServletContext().getRealPath("img/category"));
        //file：文件名（id.jpg）
        File file = new File(imageFolder,c.getId()+".jpg");
        //如果/img/category目录不存在，则创建该目录
        if(!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        //把UploadedImageFile（上传的照片）保存到 （img/category/id.jpg）
        uploadedImageFile.getImage().transferTo(file);
        //通过ImageUtil.change2jpg(file); 确保图片格式一定是jpg，而不仅仅是后缀名是jpg.
        BufferedImage img = ImageUtil.change2jpg(file);
        //写入图片
        ImageIO.write(img, "jpg", file);
        //跳转到list页面
        return "redirect:/admin_category_list";
    }

    @RequestMapping("admin_category_delete")
    //session：获取存放图片的路径
    public String delete(int id,HttpSession session) throws IOException {
        //删除对应的分类
        categoryService.delete(id);
        //imageFolder：存放路径（img/category）
        File  imageFolder= new File(session.getServletContext().getRealPath("img/category"));
        //file：文件名（id.jpg）
        File file = new File(imageFolder,id+".jpg");
        //删除图片 （img/category/id.jpg）
        file.delete();
        //跳转到list页面
        return "redirect:/admin_category_list";
    }

    @RequestMapping("admin_category_edit")
    public String edit(int id,Model model) throws IOException {
        //获取对应的分类
        Category c= categoryService.get(id);
        //把分类传到listCategory.jpg
        model.addAttribute("c", c);
        return "admin/editCategory";
    }

    @RequestMapping("admin_category_update")
    //session：获取存放图片的路径
    //UploadedImageFile：上传的照片
    public String update(Category c, HttpSession session, UploadedImageFile uploadedImageFile) throws IOException {
        //修改分类
        categoryService.update(c);
        //获取上传的图片
        MultipartFile image = uploadedImageFile.getImage();
        //判断是否有上传图片
        if(null!=image &&!image.isEmpty()){
            //如果有上传，imageFolder：存放路径（img/category）
            File  imageFolder= new File(session.getServletContext().getRealPath("img/category"));
            //file：文件名（id.jpg）
            File file = new File(imageFolder,c.getId()+".jpg");
            //把UploadedImageFile（上传的照片）保存到 （img/category/id.jpg）
            image.transferTo(file);
            //通过ImageUtil.change2jpg(file); 确保图片格式一定是jpg，而不仅仅是后缀名是jpg.
            BufferedImage img = ImageUtil.change2jpg(file);
            //写入图片
            ImageIO.write(img, "jpg", file);
        }
        //跳转到list页面
        return "redirect:/admin_category_list";
    }
}