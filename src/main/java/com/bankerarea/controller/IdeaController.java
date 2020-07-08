package com.bankerarea.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bankerarea.mapper.IdeaMapper;
import com.bankerarea.vo.GoodsVO;
import com.bankerarea.vo.IdeaVO;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/idea")
public class IdeaController {
	@Autowired
	IdeaMapper ideaMapper;
	
	@GetMapping("/list")
	public List<IdeaVO> getIdeaList() {
		System.out.println("/idea/list ==> 아이디어 리스트 조회 처리");
		
		List<IdeaVO> ideaList = ideaMapper.getIdeaList();
		for(int i=0;i<ideaList.size();i++) {
			IdeaVO idea = ideaList.get(i);
			idea.setLikey_count(ideaMapper.getLikey_count(idea.getIdea_seq()));
		}
		
		return ideaList;
	}
	
	@GetMapping("/details")
	public IdeaVO getIdea(int idea_seq, String id) {
		System.out.println("/idea/details ==> 아이디어 상세 조회 처리");
		// 아이디어 조회
		IdeaVO idea = ideaMapper.getIdea(idea_seq);
		
		// 굿즈 리스트 조회 및 배열로 변경
		List<GoodsVO> goodsList_ = ideaMapper.getGoodsList(idea_seq);
		GoodsVO[] goodsList = goodsList_.toArray(new GoodsVO[goodsList_.size()]);
		idea.setGoodsList(goodsList);
		
		return idea;
	}
	
	@PostMapping("/posts")
	public void getIdeaList(@RequestBody IdeaVO idea) {
		System.out.println("/idea/posts ==> 아이디어 등록 처리");
		System.out.println(idea);
	}
}
