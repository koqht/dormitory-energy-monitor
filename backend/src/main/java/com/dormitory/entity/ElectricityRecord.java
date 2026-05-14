package com.dormitory.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("electricity_record")
public class ElectricityRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long meterId;
    private Long dormitoryId;
    private BigDecimal usageKwh;
    private BigDecimal voltage;
    private BigDecimal current;
    private BigDecimal power;
    private LocalDateTime collectTime;
}
