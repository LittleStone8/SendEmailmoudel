package org.ofbiz.commondefine.bean;

import java.io.Serializable;
import java.util.List;

public class NavInfoBeanV2 implements Serializable {

    private String shortName;
    private String applicationName;
    private String description;
    private boolean selected;
    private String name;
    private String linkUrl;

    private List<NavInfoBeanV2> childNode;

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public boolean isSelected() {
	return selected;
    }

    public void setSelected(boolean selected) {
	this.selected = selected;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getLinkUrl() {
	return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
	this.linkUrl = linkUrl;
    }

    public List<NavInfoBeanV2> getChildNode() {
	return childNode;
    }

    public void setChildNode(List<NavInfoBeanV2> childNode) {
	this.childNode = childNode;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }
    

}
