package com.bankerarea.vo;

import java.util.List;

import lombok.Data;

@Data
public class IdeaVO {
	private int idea_seq;
	private String project_name;
	private String short_description;
	private String category;
	private String read_count;
	private String banker_id;
	private List<GoodsVO> goodsList;
	private int likey_count;
	public int getIdea_seq() {
		return idea_seq;
	}
	public void setIdea_seq(int idea_seq) {
		this.idea_seq = idea_seq;
	}
	public String getProject_name() {
		return project_name;
	}
	public void setProject_name(String project_name) {
		this.project_name = project_name;
	}
	public String getShort_description() {
		return short_description;
	}
	public void setShort_description(String short_description) {
		this.short_description = short_description;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getRead_count() {
		return read_count;
	}
	public void setRead_count(String read_count) {
		this.read_count = read_count;
	}
	public String getBanker_id() {
		return banker_id;
	}
	public void setBanker_id(String banker_id) {
		this.banker_id = banker_id;
	}
	public List<GoodsVO> getGoodsList() {
		return goodsList;
	}
	public void setGoodsList(List<GoodsVO> goodsList) {
		this.goodsList = goodsList;
	}
	public int getLikey_count() {
		return likey_count;
	}
	public void setLikey_count(int likey_count) {
		this.likey_count = likey_count;
	}
	
	@Override
	public String toString() {
		return "IdeaVO [idea_seq=" + idea_seq + ", project_name=" + project_name + ", short_description="
				+ short_description + ", category=" + category + ", read_count=" + read_count + ", banker_id="
				+ banker_id + ", goodsList=" + goodsList + ", likey_count=" + likey_count + "]";
	}
}
