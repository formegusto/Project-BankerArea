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
import com.bankerarea.mapper.PurchaseMapper;
import com.bankerarea.vo.GoodsVO;
import com.bankerarea.vo.IdeaVO;
import com.bankerarea.vo.PurchaseVO;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/idea")
public class IdeaController {
	@Autowired
	IdeaMapper ideaMapper;
	@Autowired
	PurchaseMapper purchaseMapper;
	
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
	
	@GetMapping("/detail")
	public IdeaVO getIdea(int idea_seq, String id) {
		System.out.println("/idea/details ==> 아이디어 상세 조회 처리");
		// 아이디어 조회
		IdeaVO idea = ideaMapper.getIdea(idea_seq);
		
		// 굿즈 리스트 조회 및 배열로 변경
		List<GoodsVO> goodsList_ = ideaMapper.getGoodsList(idea_seq);
		GoodsVO[] goodsList = goodsList_.toArray(new GoodsVO[goodsList_.size()]);
		
		if(id.equals("anonymous")) {
			idea.setGoodsList(goodsList);
		} else if(id.equals(idea.getBanker_id())) {
			for(int i = 0; i< goodsList.length; ++i)
				goodsList[i].setOpen_status(1);
			idea.setGoodsList(goodsList);
		} else{
			PurchaseVO purchaseVO = new PurchaseVO();
			purchaseVO.setBuyer_id(id);
			for(int i = 0; i< goodsList.length; i++) {
				purchaseVO.setGoods_seq(goodsList[i].getGoods_seq());
				if(purchaseMapper.getPurchase(purchaseVO) != null) 
					goodsList[i].setOpen_status(1);
			}	
		}
		idea.setGoodsList(goodsList);
		
		return idea;
	}
	
	@PostMapping("/post")
	public void postIdea(@RequestBody IdeaVO vo) {
		ideaMapper.insertIdea(vo);
		int current_seq = ideaMapper.getCurrentIdea_seq();
		vo.setIdea_seq(current_seq);
		for(GoodsVO goods : vo.getGoodsList()) {
			goods.setIdea_seq(vo.getIdea_seq());
			ideaMapper.insertGoods(goods);
		}
		System.out.println(vo.toString());
	}
}
