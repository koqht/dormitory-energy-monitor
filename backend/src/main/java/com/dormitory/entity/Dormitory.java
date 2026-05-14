package com.dormitory.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("dormitory")
public class Dormitory {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String buildingNo;
    private String roomNo;
    private Integer floor;
    private Integer capacity;
    private Integer currentOccupants;
    private Long meterId;
    private Integer status;
}
