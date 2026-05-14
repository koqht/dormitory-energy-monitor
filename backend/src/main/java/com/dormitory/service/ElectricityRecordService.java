package com.dormitory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dormitory.entity.ElectricityRecord;

import java.util.List;
import java.util.Map;

public interface ElectricityRecordService extends IService<ElectricityRecord> {
    List<Map<String, Object>> dailyUsage(Long dormitoryId, int days);
    List<Map<String, Object>> ranking(int days);
    Map<String, Object> dashboardData();
    Double totalUsage(Long dormitoryId, int days);
}
