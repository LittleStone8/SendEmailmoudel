package org.opentaps.warehouse.inventoryChange;

public class InventoryMenuContent {
	private String text;
	private String url;
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Override
	public String toString() {
		return "InventoryMenuContent [text=" + text + ", url=" + url + "]";
	}
	
}
