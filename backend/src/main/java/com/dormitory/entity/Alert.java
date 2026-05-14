package com.dormitory.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("alert")
public class Alert {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long dormitoryId;
    private String alertType;
    private String alertData;
    private LocalDateTime alertTime;
    private Integer status;
    private String handleNote;
    private LocalDateTime handleTime;
}
