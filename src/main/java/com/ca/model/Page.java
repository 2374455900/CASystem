package com.ca.model;


/**
 * 分页状态类
 */
public class Page {

    private int everyPage;          //每页显示记录数
    private int totalCount;         //总记录数
    private int totalPage;          //总页数
    private int currentPage;        //当前页
    private int beginIndex;         //查询起始点
    private boolean hasPrePage;     //是否有上一页
    private boolean hasNextPage;    //是否有下一页

    public Page(int everyPage, int totalCount, int totalPage, int currentPage, int beginIndex, boolean hasPrePage, boolean hasNextPage) {  //自定义构造方法
        this.everyPage = everyPage;
        this.totalCount = totalCount;
        this.totalPage = totalPage;
        this.currentPage = currentPage;
        this.beginIndex = beginIndex;
        this.hasPrePage = hasPrePage;
        this.hasNextPage = hasNextPage;
    }

    public int getEveryPage() {
        return everyPage;
    }

    public void setEveryPage(int everyPage) {
        this.everyPage = everyPage;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getBeginIndex() {
        return beginIndex;
    }

    public void setBeginIndex(int beginIndex) {
        this.beginIndex = beginIndex;
    }

    public boolean isHasPrePage() {
        return hasPrePage;
    }

    public void setHasPrePage(boolean hasPrePage) {
        this.hasPrePage = hasPrePage;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    @Override
    public String toString() {
        return "Page{" + "everyPage=" + everyPage + ", totalCount=" + totalCount + ", totalPage="
                + totalPage + ", currentPage=" + currentPage + ", beginIndex=" + beginIndex
                + ", hasPrePage=" + hasPrePage + ", hasNextPage=" + hasNextPage + '}';
    }
}
