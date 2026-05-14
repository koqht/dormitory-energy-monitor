package com.dormitory.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("meter")
public class Meter {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String meterNo;
    private Long dormitoryId;
    private BigDecimal currentUsage;
    private BigDecimal voltage;
    private BigDecimal current;
    private BigDecimal power;
    private Integer collectFrequency;
    private BigDecimal alertThreshold;
    private Integer status;
    private LocalDateTime lastCollectTime;
}
