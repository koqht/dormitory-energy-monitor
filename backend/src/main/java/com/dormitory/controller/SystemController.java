package com.dormitory.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dormitory.common.Result;
import com.dormitory.entity.*;
import com.dormitory.service.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/api/system")
public class SystemController {

    @Resource
    private UserService userService;
    @Resource
    private DormitoryService dormitoryService;
    @Resource
    private MeterService meterService;

    // ==================== 用户管理 ====================

    @GetMapping("/users")
    public Result<?> users(@RequestParam(defaultValue = "1") int page,
                           @RequestParam(defaultValue = "10") int size,
                           @RequestParam(required = false) String keyword) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .and(keyword != null && !keyword.isEmpty(), w -> w
                        .like(User::getStudentNo, keyword)
                        .or().like(User::getName, keyword))
                .orderByDesc(User::getCreateTime);
        Page<User> result = userService.page(new Page<>(page, size), wrapper);
        result.getRecords().forEach(u -> u.setPassword(null));
        return Result.ok(result);
    }

    @PostMapping("/users")
    public Result<?> addUser(@RequestBody User user) {
        User exist = userService.getByStudentNo(user.getStudentNo());
        if (exist != null) return Result.fail("学号已存在");
        userService.save(user);
        return Result.ok();
    }

    @PutMapping("/users/{id}")
    public Result<?> updateUser(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        userService.updateById(user);
        return Result.ok();
    }

    @DeleteMapping("/users/{id}")
    public Result<?> deleteUser(@PathVariable Long id) {
        User user = userService.getById(id);
        if ("super_admin".equals(user.getRole())) return Result.fail("不能删除超级管理员");
        userService.removeById(id);
        return Result.ok();
    }

    @PutMapping("/users/{id}/status")
    public Result<?> toggleStatus(@PathVariable Long id) {
        User user = userService.getById(id);
        user.setStatus(user.getStatus() == 1 ? 0 : 1);
        userService.updateById(user);
        return Result.ok();
    }

    // ==================== 宿舍管理 ====================

    @GetMapping("/dormitories")
    public Result<?> dormitories(@RequestParam(defaultValue = "1") int page,
                                 @RequestParam(defaultValue = "10") int size) {
        return Result.ok(dormitoryService.page(new Page<>(page, size)));
    }

    @PostMapping("/dormitories")
    public Result<?> addDormitory(@RequestBody Dormitory dormitory) {
        dormitoryService.save(dormitory);
        return Result.ok();
    }

    @PutMapping("/dormitories/{id}")
    public Result<?> updateDormitory(@PathVariable Long id, @RequestBody Dormitory dormitory) {
        dormitory.setId(id);
        dormitoryService.updateById(dormitory);
        return Result.ok();
    }

    @DeleteMapping("/dormitories/{id}")
    public Result<?> deleteDormitory(@PathVariable Long id) {
        dormitoryService.removeById(id);
        return Result.ok();
    }

    // ==================== 电表管理 ====================

    @GetMapping("/meters")
    public Result<?> meters(@RequestParam(defaultValue = "1") int page,
                            @RequestParam(defaultValue = "10") int size) {
        return Result.ok(meterService.page(new Page<>(page, size)));
    }

    @PostMapping("/meters")
    public Result<?> addMeter(@RequestBody Meter meter) {
        meterService.save(meter);
        return Result.ok();
    }

    @PutMapping("/meters/{id}")
    public Result<?> updateMeter(@PathVariable Long id, @RequestBody Meter meter) {
        meter.setId(id);
        meterService.updateById(meter);
        return Result.ok();
    }

    @PutMapping("/meters/{id}/bind")
    public Result<?> bindMeter(@PathVariable Long id, @RequestBody Map<String, Long> body) {
        Meter meter = meterService.getById(id);
        meter.setDormitoryId(body.get("dormitoryId"));
        meterService.updateById(meter);
        // 同时更新宿舍的电表关联
        Dormitory dormitory = dormitoryService.getById(body.get("dormitoryId"));
        if (dormitory != null) {
            dormitory.setMeterId(id);
            dormitoryService.updateById(dormitory);
        }
        return Result.ok();
    }

    @PutMapping("/meters/{id}/threshold")
    public Result<?> setThreshold(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        Meter meter = meterService.getById(id);
        meter.setAlertThreshold(new java.math.BigDecimal(body.get("threshold").toString()));
        meterService.updateById(meter);
        return Result.ok();
    }
}
