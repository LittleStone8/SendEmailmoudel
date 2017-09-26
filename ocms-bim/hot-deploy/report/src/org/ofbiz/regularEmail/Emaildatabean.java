package org.ofbiz.regularEmail;

public class Emaildatabean {

		String productstroeid;
		String productname;
		String summary;
		String qty;
		public String getProductstroeid() {
			return productstroeid;
		}
		public void setProductstroeid(String productstroeid) {
			this.productstroeid = productstroeid;
		}
		public String getProductname() {
			return productname;
		}
		public void setProductname(String productname) {
			this.productname = productname;
		}
		public String getSummary() {
			return summary;
		}
		public void setSummary(String summary) {
			this.summary = summary;
		}
		public String getQty() {
			return qty;
		}
		public void setQty(String qty) {
			this.qty = qty;
		}
		public Emaildatabean(String productstroeid, String productname, String summary, String qty) {
			super();
			this.productstroeid = productstroeid;
			this.productname = productname;
			this.summary = summary;
			this.qty = qty;
		}
		public Emaildatabean() {
			super();
			// TODO Auto-generated constructor stub
		}
		
		
		
}
