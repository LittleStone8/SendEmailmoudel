package org.ofbiz.commondefine.bean;

import java.io.Serializable;

public class NavInfoBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3968419357116362635L;
	
	private String description;
	private boolean selected;
	private String name;
	private String linkUrl;
	
	
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
	
	
	

}
