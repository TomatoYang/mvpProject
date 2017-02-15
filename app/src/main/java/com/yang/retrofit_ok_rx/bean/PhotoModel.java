package com.yang.retrofit_ok_rx.bean;

import java.util.List;

public class PhotoModel {

    public PageBean pagebean;
    public String ret_code;

    public PhotoModel() {
    }

    public class PageBean {
        public String allNum;
        public String allPages;
        public String currentPage;
        public String maxResult;
        public List<PictureBody> contentlist;

        public PageBean() {
        }

        public String getAllNum() {
            return allNum;
        }

        public void setAllNum(String allNum) {
            this.allNum = allNum;
        }

        public String getAllPages() {
            return allPages;
        }

        public void setAllPages(String allPages) {
            this.allPages = allPages;
        }

        public String getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(String currentPage) {
            this.currentPage = currentPage;
        }

        public String getMaxResult() {
            return maxResult;
        }

        public void setMaxResult(String maxResult) {
            this.maxResult = maxResult;
        }

        public List<PictureBody> getContentlist() {
            return contentlist;
        }

        public void setContentlist(List<PictureBody> contentlist) {
            this.contentlist = contentlist;
        }
    }

    public class PictureBody {
        public String ct;// 2016-03-10 04;//12;//06.606,
        public String itemId;// 39889571,
        public String title;// 清纯美女头像壁纸 葵花丛中的女孩,
        public String type;// 4001,
        public String typeName;// 清纯
        public List<PictureImage> list;//图片数组

        public PictureBody() {
        }

        public String getCt() {
            return ct;
        }

        public void setCt(String ct) {
            this.ct = ct;
        }

        public String getItemId() {
            return itemId;
        }

        public void setItemId(String itemId) {
            this.itemId = itemId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public List<PictureImage> getList() {
            return list;
        }

        public void setList(List<PictureImage> list) {
            this.list = list;
        }
    }

    public class PictureImage {
        public String big;// http;////image.tianjimedia.com/uploadImages/2014/308/28/968836971LMM.JPEG,
        public String middle;// http;////image.tianjimedia.com/uploadImages/2014/308/28/968836971LMM_680x500.JPEG,
        public String small;// http;////image.tianjimedia.com/uploadImages/2014/308/28/968836971LMM_113.JPEG


        public PictureImage() {
        }

        public String getBig() {
            return big;
        }

        public void setBig(String big) {
            this.big = big;
        }

        public String getMiddle() {
            return middle;
        }

        public void setMiddle(String middle) {
            this.middle = middle;
        }

        public String getSmall() {
            return small;
        }

        public void setSmall(String small) {
            this.small = small;
        }
    }

    public PageBean getPagebean() {
        return pagebean;
    }

    public void setPagebean(PageBean pagebean) {
        this.pagebean = pagebean;
    }

    public String getRet_code() {
        return ret_code;
    }

    public void setRet_code(String ret_code) {
        this.ret_code = ret_code;
    }
}
