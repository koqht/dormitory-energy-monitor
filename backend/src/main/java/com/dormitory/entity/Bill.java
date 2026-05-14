package com.dormitory.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("bill")
public class Bill {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long dormitoryId;
    private String billMonth;
    private BigDecimal usageKwh;
    private BigDecimal unitPrice;
    private BigDecimal amount;
    private Integer payStatus;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
