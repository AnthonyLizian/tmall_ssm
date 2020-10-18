package com.how2java.tmall.util;

import org.springframework.web.multipart.MultipartFile;

/* 上传图片类型 */
public class UploadedImageFile {
    //属性名称image必须和type="file"的name值保持一致。
    //<input id="categoryPic" accept="image/*" type="file" name="image" />
    MultipartFile image;

    public MultipartFile getImage() {return image;}
    public void setImage(MultipartFile image) {this.image = image;}

}