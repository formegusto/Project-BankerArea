package com.bankerarea.vo;

import lombok.Data;

@Data
public class GoodsVO {
	private int goods_seq;
	private String goods_type;
	private int open_status;
	private String content;
	private int price;
	private int idea_seq;
	
	public int getGoods_seq() {
		return goods_seq;
	}
	public void setGoods_seq(int goods_seq) {
		this.goods_seq = goods_seq;
	}
	public String getGoods_type() {
		return goods_type;
	}
	public void setGoods_type(String goods_type) {
		this.goods_type = goods_type;
	}
	public int getOpen_status() {
		return open_status;
	}
	public void setOpen_status(int open_status) {
		this.open_status = open_status;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getIdea_seq() {
		return idea_seq;
	}
	public void setIdea_seq(int idea_seq) {
		this.idea_seq = idea_seq;
	}
	
	@Override
	public String toString() {
		return "GoodsVO [goods_seq=" + goods_seq + ", goods_type=" + goods_type + ", open_status=" + open_status
				+ ", content=" + content + ", price=" + price + ", idea_seq=" + idea_seq + "]";
	}
}
