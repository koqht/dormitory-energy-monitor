package com.dormitory.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dormitory.common.Result;
import com.dormitory.entity.*;
import com.dormitory.service.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Resource
    private ElectricityRecordService electricityRecordService;
    @Resource
    private AlertService alertService;
    @Resource
    private BillService billService;
    @Resource
    private DormitoryService dormitoryService;
    @Resource
    private MeterService meterService;
    @Resource
    private UserService userService;

    /** 宿管仪表盘 */
    @GetMapping("/dashboard")
    public Result<?> dashboard() {
        return Result.ok(electricityRecordService.dashboardData());
    }

    /** 用电数据查询 */
    @GetMapping("/usage/query")
    public Result<?> queryUsage(@RequestParam(required = false) Long dormitoryId,
                                @RequestParam(defaultValue = "7") int days) {
        if (dormitoryId != null) {
            return Result.ok(electricityRecordService.dailyUsage(dormitoryId, days));
        }
        return Result.ok(electricityRecordService.dailyUsage(null, days));
    }

    /** 用电排行 */
    @GetMapping("/usage/ranking")
    public Result<?> ranking(@RequestParam(defaultValue = "30") int days) {
        return Result.ok(electricityRecordService.ranking(days));
    }

    /** 预警列表 */
    @GetMapping("/alerts")
    public Result<?> alerts(@RequestParam(defaultValue = "1") int page,
                            @RequestParam(defaultValue = "10") int size) {
        Page<Alert> alertPage = alertService.page(new Page<>(page, size),
                new LambdaQueryWrapper<Alert>().orderByDesc(Alert::getAlertTime));
        return Result.ok(alertPage);
    }

    /** 处理预警 */
    @PutMapping("/alerts/{id}/handle")
    public Result<?> handleAlert(@PathVariable Long id, @RequestBody Map<String, String> body) {
        Alert alert = alertService.getById(id);
        if (alert == null) return Result.fail("预警不存在");
        alert.setStatus(1);
        alert.setHandleNote(body.get("handleNote"));
        alert.setHandleTime(LocalDateTime.now());
        alertService.updateById(alert);
        return Result.ok();
    }

    /** 账单管理 */
    @GetMapping("/bills")
    public Result<?> bills(@RequestParam(defaultValue = "1") int page,
                           @RequestParam(defaultValue = "10") int size,
                           @RequestParam(required = false) String month) {
        LambdaQueryWrapper<Bill> wrapper = new LambdaQueryWrapper<Bill>()
                .eq(month != null, Bill::getBillMonth, month)
                .orderByDesc(Bill::getBillMonth);
        return Result.ok(billService.page(new Page<>(page, size), wrapper));
    }

    /** 生成账单 */
    @PostMapping("/bills/generate")
    public Result<?> generateBill(@RequestBody Map<String, String> body) {
        String month = body.get("month");
        List<Dormitory> dormitories = dormitoryService.list();
        int count = 0;
        for (Dormitory d : dormitories) {
            Bill exist = billService.getOne(new LambdaQueryWrapper<Bill>()
                    .eq(Bill::getDormitoryId, d.getId())
                    .eq(Bill::getBillMonth, month));
            if (exist != null) continue;
            Double usage = electricityRecordService.totalUsage(d.getId(), 30);
            if (usage == null) usage = 0.0;
            Bill bill = new Bill();
            bill.setDormitoryId(d.getId());
            bill.setBillMonth(month);
            bill.setUsageKwh(new java.math.BigDecimal(usage).setScale(4, java.math.RoundingMode.HALF_UP));
            bill.setUnitPrice(new java.math.BigDecimal("0.5"));
            bill.setAmount(new java.math.BigDecimal(usage * 0.5).setScale(2, java.math.RoundingMode.HALF_UP));
            bill.setPayStatus(0);
            billService.save(bill);
            count++;
        }
        return Result.ok("生成账单完成，共" + count + "条");
    }

    /** 宿舍列表 */
    @GetMapping("/dormitories")
    public Result<?> dormitories() {
        return Result.ok(dormitoryService.list());
    }

    /** 报表导出(简化: 返回JSON数据) */
    @GetMapping("/report")
    public Result<?> report(@RequestParam(defaultValue = "30") int days) {
        Map<String, Object> report = new HashMap<>();
        report.put("ranking", electricityRecordService.ranking(days));
        report.put("dormitories", dormitoryService.list());
        report.put("summary", electricityRecordService.dashboardData());
        return Result.ok(report);
    }
}
