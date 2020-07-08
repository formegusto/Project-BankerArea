package com.bankerarea.vo;

import lombok.Data;

@Data
public class LikeyVO {
	private int likey_seq;
	private String id;
	private int idea_seq;
	
	public int getLikey_seq() {
		return likey_seq;
	}
	public void setLikey_seq(int likey_seq) {
		this.likey_seq = likey_seq;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getIdea_seq() {
		return idea_seq;
	}
	public void setIdea_seq(int idea_seq) {
		this.idea_seq = idea_seq;
	}
	
	@Override
	public String toString() {
		return "LikeyVO [likey_seq=" + likey_seq + ", id=" + id + ", idea_seq=" + idea_seq + "]";
	}
}
