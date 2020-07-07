package com.bankerarea.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.bankerarea.vo.UserVO;

@Mapper
public interface UserMapper {
	UserVO signinUser(UserVO vo);
	void signupUser(UserVO vo);
}
