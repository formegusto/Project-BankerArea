package com.bankerarea.vo;

public class SearchVO {
	private String type;
	private String keyword;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
	@Override
	public String toString() {
		return "SearchVO [type=" + type + ", keyword=" + keyword + "]";
	}
}
