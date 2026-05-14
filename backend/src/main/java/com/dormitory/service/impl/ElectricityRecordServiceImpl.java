package com.dormitory.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dormitory.entity.ElectricityRecord;
import com.dormitory.entity.Alert;
import com.dormitory.entity.Meter;
import com.dormitory.mapper.AlertMapper;
import com.dormitory.mapper.ElectricityRecordMapper;
import com.dormitory.mapper.MeterMapper;
import com.dormitory.service.ElectricityRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ElectricityRecordServiceImpl extends ServiceImpl<ElectricityRecordMapper, ElectricityRecord>
        implements ElectricityRecordService {

    @Resource
    private MeterMapper meterMapper;
    @Resource
    private AlertMapper alertMapper;

    @Override
    public List<Map<String, Object>> dailyUsage(Long dormitoryId, int days) {
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = end.minusDays(days);
        return baseMapper.dailyUsage(dormitoryId, start, end);
    }

    @Override
    public List<Map<String, Object>> ranking(int days) {
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = end.minusDays(days);
        return baseMapper.rankingByRange(start, end);
    }

    @Override
    public Map<String, Object> dashboardData() {
        Map<String, Object> data = new HashMap<>();
        LocalDateTime todayStart = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime monthStart = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);

        // 今日总用电
        Double todayUsage = baseMapper.totalUsage(null, todayStart, LocalDateTime.now());
        data.put("todayUsage", todayUsage != null ? todayUsage : 0);

        // 本月总用电
        Double monthUsage = baseMapper.totalUsage(null, monthStart, LocalDateTime.now());
        data.put("monthUsage", monthUsage != null ? monthUsage : 0);

        // 用电排行TOP10
        data.put("top10", baseMapper.top10Usage(monthStart, LocalDateTime.now()));

        // 未处理预警数
        Long alertCount = alertMapper.selectCount(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Alert>()
                        .eq(Alert::getStatus, 0));
        data.put("alertCount", alertCount);

        // 宿舍总数
        data.put("dormitoryCount", meterMapper.selectCount(null));

        return data;
    }

    @Override
    public Double totalUsage(Long dormitoryId, int days) {
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = end.minusDays(days);
        return baseMapper.totalUsage(dormitoryId, start, end);
    }
}
