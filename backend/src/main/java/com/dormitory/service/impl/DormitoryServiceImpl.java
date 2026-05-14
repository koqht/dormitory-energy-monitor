package com.dormitory.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dormitory.entity.Dormitory;
import com.dormitory.mapper.DormitoryMapper;
import com.dormitory.service.DormitoryService;
import org.springframework.stereotype.Service;

@Service
public class DormitoryServiceImpl extends ServiceImpl<DormitoryMapper, Dormitory>
        implements DormitoryService {
}
