package com.dormitory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dormitory.common.LoginException;
import com.dormitory.entity.User;
import com.dormitory.mapper.UserMapper;
import com.dormitory.service.UserService;
import com.dormitory.util.JwtUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public Map<String, Object> login(String studentNo, String password) {
        User user = baseMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getStudentNo, studentNo));
        if (user == null) {
            throw new LoginException("账号不存在");
        }
        if (user.getStatus() == 0) {
            throw new LoginException("账号已被禁用");
        }
        if (!password.equals(user.getPassword())) {
            throw new LoginException("密码错误");
        }
        String token = JwtUtil.generateToken(user.getId(), user.getRole());
        user.setPassword(null);
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", user);
        return result;
    }

    @Override
    public User getByStudentNo(String studentNo) {
        return baseMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getStudentNo, studentNo));
    }
}
