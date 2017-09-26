package org.opentaps.warehouse.inventoryChange.inventoryChangeModel;

import java.util.List;

public class StartPickingDto {
	private String transhipmentShippingBillId;
	private List<PickingDto> pickingDtos;
	
	public String getTranshipmentShippingBillId() {
		return transhipmentShippingBillId;
	}
	public void setTranshipmentShippingBillId(String transhipmentShippingBillId) {
		this.transhipmentShippingBillId = transhipmentShippingBillId;
	}
	public List<PickingDto> getPickingDtos() {
		return pickingDtos;
	}
	public void setPickingDtos(List<PickingDto> pickingDtos) {
		this.pickingDtos = pickingDtos;
	}
	@Override
	public String toString() {
		return "StartPickingDto [transhipmentShippingBillId=" + transhipmentShippingBillId + ", pickingDtos="
				+ pickingDtos + "]";
	}
	
	
}
