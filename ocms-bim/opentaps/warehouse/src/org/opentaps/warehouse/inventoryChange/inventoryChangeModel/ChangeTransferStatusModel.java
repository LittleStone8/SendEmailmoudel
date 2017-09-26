package org.opentaps.warehouse.inventoryChange.inventoryChangeModel;

public class ChangeTransferStatusModel {
	
	private String transferId;
	private String statusId;
	private String comments;
	public String getTransferId() {
		return transferId;
	}
	public void setTransferId(String transferId) {
		this.transferId = transferId;
	}
	public String getStatusId() {
		return statusId;
	}
	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	@Override
	public String toString() {
		return "ChangeTransferStatusModel [transferId=" + transferId + ", statusId=" + statusId + ", comments="
				+ comments + "]";
	}
	
	

}
