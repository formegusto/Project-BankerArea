package com.bankerarea.vo;

import lombok.Data;

@Data
public class PurchaseVO {
	private String buyer_id;
	private int goods_seq;
	
	public String getBuyer_id() {
		return buyer_id;
	}
	public void setBuyer_id(String buyer_id) {
		this.buyer_id = buyer_id;
	}
	public int getGoods_seq() {
		return goods_seq;
	}
	public void setGoods_seq(int goods_seq) {
		this.goods_seq = goods_seq;
	}
	
	@Override
	public String toString() {
		return "PurchaseVO [buyer_id=" + buyer_id + ", goods_seq=" + goods_seq + "]";
	}
}
