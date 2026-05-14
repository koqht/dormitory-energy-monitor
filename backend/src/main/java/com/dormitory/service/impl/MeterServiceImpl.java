package com.dormitory.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dormitory.entity.Meter;
import com.dormitory.mapper.MeterMapper;
import com.dormitory.service.MeterService;
import org.springframework.stereotype.Service;

@Service
public class MeterServiceImpl extends ServiceImpl<MeterMapper, Meter> implements MeterService {
}
