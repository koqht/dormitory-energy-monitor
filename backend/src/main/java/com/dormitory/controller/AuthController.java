package com.dormitory.controller;

import com.dormitory.common.Result;
import com.dormitory.entity.User;
import com.dormitory.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Resource
    private UserService userService;

    @PostMapping("/login")
    public Result<?> login(@RequestBody User loginUser) {
        return Result.ok(userService.login(loginUser.getStudentNo(), loginUser.getPassword()));
    }

    @GetMapping("/info")
    public Result<?> info(@RequestAttribute Long userId) {
        User user = userService.getById(userId);
        user.setPassword(null);
        return Result.ok(user);
    }
}
