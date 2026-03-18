package com.example.templete.domain.user.mapper;

import com.example.templete.domain.user.model.User;
import com.example.templete.domain.user.model.UserLoginResponse;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    UserLoginResponse selectUserById(String loginId);

    void insertUser(User user);
}
