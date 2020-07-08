package com.bankerarea.test;

import lombok.Data;

@Data
public class TestVO {
	private int num;
	private String str;
	
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getStr() {
		return str;
	}
	public void setStr(String str) {
		this.str = str;
	}
	
	@Override
	public String toString() {
		return "TestVO [num=" + num + ", str=" + str + "]";
	}
}
