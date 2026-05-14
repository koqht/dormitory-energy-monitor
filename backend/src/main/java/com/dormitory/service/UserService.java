package com.dormitory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dormitory.entity.User;

import java.util.Map;

public interface UserService extends IService<User> {
    Map<String, Object> login(String studentNo, String password);
    User getByStudentNo(String studentNo);
}
