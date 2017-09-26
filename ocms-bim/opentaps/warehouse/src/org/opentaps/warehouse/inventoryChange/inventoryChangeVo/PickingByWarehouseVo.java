package org.opentaps.warehouse.inventoryChange.inventoryChangeVo;

import java.util.List;

public class PickingByWarehouseVo {
	private String warehouse;
	private String transNum;
	private List<PickingVo> pickingVo;
	public String getWarehouse() {
		return warehouse;
	}
	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}
	public String getTransNum() {
		return transNum;
	}
	public void setTransNum(String transNum) {
		this.transNum = transNum;
	}
	public List<PickingVo> getPickingVo() {
		return pickingVo;
	}
	public void setPickingVo(List<PickingVo> pickingVo) {
		this.pickingVo = pickingVo;
	}
	@Override
	public String toString() {
		return "PickingByWarehouseVo [warehouse=" + warehouse + ", transNum=" + transNum + ", pickingVo=" + pickingVo
				+ "]";
	}
	
}
