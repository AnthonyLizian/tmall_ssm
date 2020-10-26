package com.how2java.tmall.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.how2java.tmall.pojo.Category;
import com.how2java.tmall.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.how2java.tmall.pojo.Product;
import com.how2java.tmall.pojo.ProductImage;
import com.how2java.tmall.service.ProductImageService;
import com.how2java.tmall.service.ProductService;
import com.how2java.tmall.util.ImageUtil;
import com.how2java.tmall.util.Page;
import com.how2java.tmall.util.UploadedImageFile;

@Controller
@RequestMapping("")
public class ProductImageController {
    @Autowired
    ProductService productService;

    @Autowired
    ProductImageService productImageService;

    @Autowired
    CategoryService categoryService;

    @RequestMapping("admin_productImage_list")
    public String list(int pid, Model model) {
        Product p = productService.get(pid);
        Category c = categoryService.get(p.getCid());

        List<ProductImage> pisSingle = productImageService.list(pid, ProductImageService.type_single);
        List<ProductImage> pisDetail = productImageService.list(pid, ProductImageService.type_detail);

        model.addAttribute("p", p);
        model.addAttribute("c", c);
        model.addAttribute("pisSingle", pisSingle);
        model.addAttribute("pisDetail", pisDetail);

        return "admin/listProductImage";
    }

    @RequestMapping("admin_productImage_add")
    public String add(ProductImage  pi, HttpSession session, UploadedImageFile uploadedImageFile) {
        productImageService.add(pi);
        //文件名
        String fileName = pi.getId()+ ".jpg";
        //存放路径
        String imageFolder;
        String imageFolder_small=null;
        String imageFolder_middle=null;

        //判断图片类型是单个图片还是详情图片
        if(ProductImageService.type_single.equals(pi.getType())){
            //imageFolder：存放路径 单个图片
            imageFolder= session.getServletContext().getRealPath("img/productSingle");
            //小图的存放路径
            imageFolder_small= session.getServletContext().getRealPath("img/productSingle_small");
            //中图的存放路径
            imageFolder_middle= session.getServletContext().getRealPath("img/productSingle_middle");
        }
        else{
            //多图的存放路径
            imageFolder= session.getServletContext().getRealPath("img/productDetail");
        }

        //更具文件路径和文件名创建文件
        File f = new File(imageFolder, fileName);
        f.getParentFile().mkdirs();
        try {
            //把UploadedImageFile（上传的照片）保存到 文件里
            uploadedImageFile.getImage().transferTo(f);
            //通过ImageUtil.change2jpg(file); 确保图片格式一定是jpg，而不仅仅是后缀名是jpg.
            BufferedImage img = ImageUtil.change2jpg(f);
            //写入图片
            ImageIO.write(img, "jpg", f);

            if(ProductImageService.type_single.equals(pi.getType())) {
                File f_small = new File(imageFolder_small, fileName);
                File f_middle = new File(imageFolder_middle, fileName);

                //把正常大小的图片，改变大小之后，分别复制到productSingle_middle和productSingle_small目录下。
                ImageUtil.resizeImage(f, 56, 56, f_small);
                ImageUtil.resizeImage(f, 217, 190, f_middle);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:admin_productImage_list?pid="+pi.getPid();
    }

    @RequestMapping("admin_productImage_delete")
    public String delete(int id,HttpSession session) {
        ProductImage pi = productImageService.get(id);

        String fileName = pi.getId()+ ".jpg";
        String imageFolder;
        String imageFolder_small=null;
        String imageFolder_middle=null;

        //判断图片类型是单个图片还是详情图片
        if(ProductImageService.type_single.equals(pi.getType())){
            //imageFolder：存放路径
            imageFolder= session.getServletContext().getRealPath("img/productSingle");
            imageFolder_small= session.getServletContext().getRealPath("img/productSingle_small");
            imageFolder_middle= session.getServletContext().getRealPath("img/productSingle_middle");
            //找到对应的文件
            File imageFile = new File(imageFolder,fileName);
            File f_small = new File(imageFolder_small,fileName);
            File f_middle = new File(imageFolder_middle,fileName);
            //删除文件
            imageFile.delete();
            f_small.delete();
            f_middle.delete();
        }
        else{
            //imageFolder：存放路径
            imageFolder= session.getServletContext().getRealPath("img/productDetail");
            //找到对应的文件
            File imageFile = new File(imageFolder,fileName);
            //删除文件
            imageFile.delete();
        }

        productImageService.delete(id);

        return "redirect:admin_productImage_list?pid="+pi.getPid();
    }
}