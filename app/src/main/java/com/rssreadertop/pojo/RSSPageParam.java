package com.rssreadertop.pojo;

/**
 * Created by anilkukreti on 05/02/16.
 */
public class RSSPageParam {
    private String pageTitle;
    private String pageUrl;
    private int xmlHandler;

    public int getXmlHandler() {
        return xmlHandler;
    }

    public void setXmlHandler(int xmlHandler) {
        this.xmlHandler = xmlHandler;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }
}
