package com.bankerarea.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.bankerarea.vo.PurchaseVO;

@Mapper
public interface PurchaseMapper {
	PurchaseVO getPurchase(PurchaseVO vo);
	void insertPurchase(PurchaseVO vo);
}
