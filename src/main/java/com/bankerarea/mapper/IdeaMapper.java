package com.bankerarea.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.bankerarea.vo.GoodsVO;
import com.bankerarea.vo.IdeaVO;
import com.bankerarea.vo.SearchVO;

@Mapper
public interface IdeaMapper {
	// READ
	List<IdeaVO> getIdeaList();
	IdeaVO getIdea(int idea_seq);
	List<GoodsVO> getGoodsList(int idea_seq);
	int getLikey_count(int idea_seq);
	int getCurrentIdea_seq();
	List<IdeaVO> getYourLikeyList(String id);
	List<IdeaVO> getMyIdeaList(String id);
	List<IdeaVO> searchTypeIdeaList(SearchVO vo);
	
	// WRITE
	void insertIdea(IdeaVO vo);
	void insertGoods(GoodsVO vo);
	void updateIdea(IdeaVO vo);
	void updateGoods(GoodsVO vo);
	void increaseReadCnt(int idea_seq);
}
