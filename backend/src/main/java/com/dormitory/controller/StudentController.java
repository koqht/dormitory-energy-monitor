package com.dormitory.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dormitory.common.Result;
import com.dormitory.entity.*;
import com.dormitory.service.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    @Resource
    private UserService userService;
    @Resource
    private ElectricityRecordService electricityRecordService;
    @Resource
    private MeterService meterService;
    @Resource
    private AlertService alertService;
    @Resource
    private BillService billService;
    @Resource
    private DormitoryService dormitoryService;

    /** 个人仪表盘 */
    @GetMapping("/dashboard")
    public Result<?> dashboard(@RequestAttribute Long userId) {
        User user = userService.getById(userId);
        if (user.getDormitoryId() == null) {
            return Result.fail("未分配宿舍");
        }
        Long dormitoryId = user.getDormitoryId();
        Dormitory dormitory = dormitoryService.getById(dormitoryId);

        Map<String, Object> data = new HashMap<>();
        data.put("user", user);
        data.put("dormitory", dormitory);

        // 今日用电
        LocalDateTime todayStart = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        Double todayUsage = electricityRecordService.totalUsage(dormitoryId, 1);
        data.put("todayUsage", todayUsage != null ? todayUsage : 0);

        // 本月用电
        LocalDateTime monthStart = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        List<Map<String, Object>> dailyUsage = electricityRecordService.dailyUsage(dormitoryId, 30);
        double monthTotal = dailyUsage.stream().mapToDouble(m ->
                ((Number) m.get("total")).doubleValue()).sum();
        data.put("monthUsage", Math.round(monthTotal * 100.0) / 100.0);

        // 电表信息
        Meter meter = meterService.getOne(new LambdaQueryWrapper<Meter>()
                .eq(Meter::getDormitoryId, dormitoryId));
        data.put("meter", meter);

        // 未处理预警
        List<Alert> alerts = alertService.list(new LambdaQueryWrapper<Alert>()
                .eq(Alert::getDormitoryId, dormitoryId)
                .eq(Alert::getStatus, 0)
                .orderByDesc(Alert::getAlertTime));
        data.put("alerts", alerts);

        // 最新账单
        List<Bill> bills = billService.list(new LambdaQueryWrapper<Bill>()
                .eq(Bill::getDormitoryId, dormitoryId)
                .orderByDesc(Bill::getBillMonth)
                .last("LIMIT 3"));
        data.put("bills", bills);

        // 节能建议
        List<String> suggestions = generateSuggestions(todayUsage, monthTotal);
        data.put("suggestions", suggestions);

        return Result.ok(data);
    }

    @GetMapping("/usage/chart")
    public Result<?> usageChart(@RequestAttribute Long userId, @RequestParam(defaultValue = "7") int days) {
        User user = userService.getById(userId);
        if (user.getDormitoryId() == null) {
            return Result.fail("未分配宿舍");
        }
        return Result.ok(electricityRecordService.dailyUsage(user.getDormitoryId(), days));
    }

    @GetMapping("/usage/ranking")
    public Result<?> ranking(@RequestParam(defaultValue = "7") int days) {
        return Result.ok(electricityRecordService.ranking(days));
    }

    @GetMapping("/alerts")
    public Result<?> alerts(@RequestAttribute Long userId) {
        User user = userService.getById(userId);
        if (user.getDormitoryId() == null) return Result.ok(Collections.emptyList());
        return Result.ok(alertService.list(new LambdaQueryWrapper<Alert>()
                .eq(Alert::getDormitoryId, user.getDormitoryId())
                .orderByDesc(Alert::getAlertTime)));
    }

    @GetMapping("/bills")
    public Result<?> bills(@RequestAttribute Long userId) {
        User user = userService.getById(userId);
        if (user.getDormitoryId() == null) return Result.ok(Collections.emptyList());
        return Result.ok(billService.list(new LambdaQueryWrapper<Bill>()
                .eq(Bill::getDormitoryId, user.getDormitoryId())
                .orderByDesc(Bill::getBillMonth)));
    }

    private List<String> generateSuggestions(Double today, double month) {
        List<String> tips = new ArrayList<>();
        if (today != null && today > 10) tips.add("今日用电偏高，请检查是否有不必要电器运行");
        if (month > 200) tips.add("本月用电量较大，建议合理使用空调，温度设置在26°C以上");
        tips.add("离开宿舍时请关闭不必要的电源");
        tips.add("使用节能灯泡替代传统白炽灯可节省约30%用电");
        if (today != null && today > 5 && today < 10) tips.add("今日用电正常，继续保持节能好习惯");
        return tips;
    }
}
