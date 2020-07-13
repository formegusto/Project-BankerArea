package com.bankerarea.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.bankerarea.vo.GoodsVO;
import com.bankerarea.vo.IdeaVO;

@Mapper
public interface IdeaMapper {
	List<IdeaVO> getIdeaList();
	IdeaVO getIdea(int idea_seq);
	List<GoodsVO> getGoodsList(int idea_seq);
	int getLikey_count(int idea_seq);
	void insertIdea(IdeaVO vo);
	void insertGoods(GoodsVO vo);
	int getCurrentIdea_seq();
	
	// Update
	void updateIdea(IdeaVO vo);
	void updateGoods(GoodsVO vo);
}
