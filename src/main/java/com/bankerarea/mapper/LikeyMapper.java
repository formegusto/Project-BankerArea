package com.bankerarea.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.bankerarea.vo.LikeyVO;

@Mapper
public interface LikeyMapper {
	void insertLikey(LikeyVO vo);
	void deleteLikey(LikeyVO vo);
	Integer doYouLike(LikeyVO vo);
}
