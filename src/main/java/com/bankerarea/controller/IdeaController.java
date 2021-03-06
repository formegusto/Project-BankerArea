package com.bankerarea.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bankerarea.common.LoginManagementService;
import com.bankerarea.mapper.IdeaMapper;
import com.bankerarea.mapper.LikeyMapper;
import com.bankerarea.mapper.PurchaseMapper;
import com.bankerarea.vo.GoodsVO;
import com.bankerarea.vo.IdeaVO;
import com.bankerarea.vo.LikeyVO;
import com.bankerarea.vo.PurchaseVO;
import com.bankerarea.vo.SearchVO;

@CrossOrigin(origins = "", maxAge = 3600, allowCredentials="true")
@RestController
@RequestMapping("/idea")
public class IdeaController {
	@Autowired
	IdeaMapper ideaMapper;
	@Autowired
	PurchaseMapper purchaseMapper;
	@Autowired
	LikeyMapper likeyMapper;
	@Autowired
	LoginManagementService loginManagementService;
	
	/* 리스트 조회 관련 API */
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
	
	@GetMapping("/list/likey")
	public List<IdeaVO> likeyList(HttpServletResponse res,
			@CookieValue(name="accessKey", defaultValue="unAuth") String accessKey) 
			throws Exception {
		String id = "unAuth";
		if(!accessKey.equals("unAuth"))
			id = loginManagementService.getIdByToken(accessKey);
		
		if(id.equals("unAuth")) {
			res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}
		
		List<IdeaVO> ideaList = ideaMapper.getYourLikeyList(id);
		for(int i=0;i<ideaList.size();i++) {
			IdeaVO idea = ideaList.get(i);
			idea.setLikey_count(ideaMapper.getLikey_count(idea.getIdea_seq()));
		}
		
		return ideaList;
	}
	
	@GetMapping("/list/my")
	public List<IdeaVO> myList(HttpServletResponse res,
			@CookieValue(name="accessKey", defaultValue="unAuth") String accessKey) 
			throws Exception {
		String id = "unAuth";
		if(!accessKey.equals("unAuth"))
			id = loginManagementService.getIdByToken(accessKey);
		
		if(id.equals("unAuth")) {
			res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}
		
		List<IdeaVO> ideaList = ideaMapper.getMyIdeaList(id);
		for(int i=0;i<ideaList.size();i++) {
			IdeaVO idea = ideaList.get(i);
			idea.setLikey_count(ideaMapper.getLikey_count(idea.getIdea_seq()));
		}
		
		return ideaList;
	}
	
	@GetMapping("/list/search")
	public List<IdeaVO> searchIdeaList(SearchVO vo) {
		List<IdeaVO> searchList = ideaMapper.searchTypeIdeaList(vo);
		return searchList;
	}
	
	@GetMapping("/detail")
	public IdeaVO getIdea(int idea_seq,
			@CookieValue(name="accessKey", defaultValue="unAuth") String accessKey) throws Exception {
		System.out.println("/idea/detail ==> 아이디어 상세 조회 처리");
		
		// 사용자 id 설정
		String id = "unAuth";
		if(!accessKey.equals("unAuth"))
			id = loginManagementService.getIdByToken(accessKey);
		
		// 아이디어 조회
		IdeaVO idea = ideaMapper.getIdea(idea_seq);
		ideaMapper.increaseReadCnt(idea_seq);
		
		// 굿즈 리스트 조회
		List<GoodsVO> goodsList = ideaMapper.getGoodsList(idea_seq);
		
		// 사용자별 공개여부 설정
		if(id.equals("unAuth")) {
			for(int i = 0; i< goodsList.size(); ++i)
				goodsList.get(i).setOpen_status(0);
		} else if(id.equals(idea.getBanker_id())) {
			for(int i = 0; i< goodsList.size(); ++i)
				goodsList.get(i).setOpen_status(1);
		} else{
			PurchaseVO purchaseVO = new PurchaseVO();
			purchaseVO.setBuyer_id(id);
			for(int i = 0; i< goodsList.size(); i++) {
				purchaseVO.setGoods_seq(goodsList.get(i).getGoods_seq());
				if(purchaseMapper.getPurchase(purchaseVO) != null) 
					goodsList.get(i).setOpen_status(1);
			}	
		}
		idea.setGoodsList(goodsList);
		
		return idea;
	}
	
	/*
	@GetMapping("/detailUp")
	public IdeaVO getIdeaUp(int idea_seq, HttpServletResponse res,
			@CookieValue(name="accessKey", defaultValue="unAuth") String accessKey) throws Exception {
		System.out.println("/idea/detailUp ==> 아이디어 상세 조회 처리");
		// 사용자 id 설정
				String id = "unAuth";
				if(!accessKey.equals("unAuth"))
					id = loginManagementService.getIdByToken(accessKey);
		
		// 아이디어 조회
		IdeaVO idea = ideaMapper.getIdea(idea_seq);
		
		// 권한이 없는
		if(idea.getBanker_id().equals(id) && !(id.equals("unAuth"))){
			// 굿즈 리스트 조회
			List<GoodsVO> goodsList_ = ideaMapper.getGoodsList(idea_seq);
			GoodsVO[] goodsList = goodsList_.toArray(new GoodsVO[goodsList_.size()]);
			
			idea.setGoodsList(goodsList);
		} else {
			res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			idea = null;
		}
		
		return idea;
	}
	*/
	/* CUD 관련 API */
	@PostMapping("/post")
	public void post(@RequestBody IdeaVO vo, HttpServletResponse res,
			@CookieValue(name="accessKey", defaultValue="unAuth") String accessKey) throws Exception {
		System.out.println("/idea/post ==> 아이디어 등록 처리");
		
		if(accessKey.equals("unAuth")) {
			res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		} else {
			String id = loginManagementService.getIdByToken(accessKey);
			vo.setBanker_id(id);
			ideaMapper.insertIdea(vo);
			
			int current_seq = ideaMapper.getCurrentIdea_seq();
			vo.setIdea_seq(current_seq);
			
			for(GoodsVO goods : vo.getGoodsList()) {
				goods.setIdea_seq(vo.getIdea_seq());
				ideaMapper.insertGoods(goods);
			}
		}
	}
	
	@PatchMapping("/update")
	public void update(@RequestBody IdeaVO vo) {
		System.out.println("/idea/detail ==> 아이디어 수정 처리");
		ideaMapper.updateIdea(vo);
		for(GoodsVO goods : vo.getGoodsList()) {
			ideaMapper.updateGoods(goods);
		}
		System.out.println(vo.toString());
	}
	
	@DeleteMapping("/delete")
	public void delete(@RequestBody IdeaVO vo, HttpServletResponse res,
			@CookieValue(name="accessKey", defaultValue="unAuth") String accessKey) throws Exception {
		System.out.println("/idea/delete ==> 아이디어 삭제 처리");
		IdeaVO idea = ideaMapper.getIdea(vo.getIdea_seq());
		
		/* 사용자 권한 체크 */
		String id = "unAuth";
		if(!accessKey.equals("unAuth"))
			id = loginManagementService.getIdByToken(accessKey);
		
		if(id.equals("unAuth") || !idea.getBanker_id().equals(id)) {
			res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		} else {
			/* 영한이가 만들어 준 SQL 실행 */
			// IdeaVO delIdea = ideaMapper.영한이 함수()
			Integer idea_seq = purchaseMapper.isThisSoldIdea(vo.getIdea_seq());
			if(idea_seq != null) {
				// 값이 있으면 ( 지우면 안됨! )
				res.setStatus(HttpServletResponse.SC_FORBIDDEN);
			} else {
				// 값이 없으면 ( 지우기! )
				ideaMapper.deleteIdea(vo.getIdea_seq());
			}
		}
	}
	
	
	/*구매 관련 API */
	@PostMapping("/purchase")
	public void purchase(@RequestBody List<Integer> goodsSeqList, 
			@CookieValue(name="accessKey", defaultValue = "unAuth") String accessKey) throws Exception {
		String id = loginManagementService.getIdByToken(accessKey);
		System.out.println(id + "<== 이 분이 구매하실 리스트 ==> " + goodsSeqList.toString());
		for(int goods_seq : goodsSeqList) {
			PurchaseVO pvo = new PurchaseVO();
			pvo.setGoods_seq(goods_seq);
			pvo.setBuyer_id(id);
			
			purchaseMapper.insertPurchase(pvo);
		}
	}
	
	/* 좋아요 관련 API */
	@PostMapping("/likey")
	public void likey(@RequestBody LikeyVO vo, HttpServletResponse res,
			@CookieValue(name="accessKey", defaultValue = "unAuth") String accessKey) throws Exception {
		String id = "unAuth";
		if(!accessKey.equals("unAuth"))
			id = loginManagementService.getIdByToken(accessKey);
		if(id.equals("unAuth")) {
			res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		} else{
			vo.setId(id);
			likeyMapper.insertLikey(vo);
		}
	}
	
	@PostMapping("/unlikey")
	public void unlikey(@RequestBody LikeyVO vo, HttpServletResponse res,
			@CookieValue(name="accessKey", defaultValue = "unAuth") String accessKey) throws Exception {
		String id = "unAuth";
		if(!accessKey.equals("unAuth"))
			id = loginManagementService.getIdByToken(accessKey);
		if(id.equals("unAuth")) {
			res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		} else{
			vo.setId(id);
			likeyMapper.deleteLikey(vo);
		}
	}
	
	@PostMapping("/doYouLike") 
	public int doYouLike(@RequestBody LikeyVO vo, HttpServletResponse res,
			@CookieValue(name="accessKey", defaultValue = "unAuth") String accessKey) throws Exception {
		int likey_status = 0;
		String id = "unAuth";
		if(!accessKey.equals("unAuth"))
			id = loginManagementService.getIdByToken(accessKey);
		
		
		if(id.equals("unAuth")) {
			res.setStatus(HttpServletResponse.SC_BAD_REQUEST); //400
		} else {
			vo.setId(id);
			likey_status = (likeyMapper.doYouLike(vo) == null) ? 0 : 1;
		}
		return likey_status;
	}
}
