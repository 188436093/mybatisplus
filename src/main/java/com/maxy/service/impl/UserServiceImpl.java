package com.maxy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maxy.mapper.UserMapper;
import com.maxy.pojo.User;
import com.maxy.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author maxy
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
