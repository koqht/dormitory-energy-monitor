package com.dormitory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dormitory.entity.ElectricityRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface ElectricityRecordMapper extends BaseMapper<ElectricityRecord> {

    @Select("<script>SELECT DATE(collect_time) as date, SUM(usage_kwh) as total " +
            "FROM electricity_record WHERE collect_time >= #{start} AND collect_time &lt;= #{end}" +
            "<if test='dormitoryId != null'> AND dormitory_id = #{dormitoryId}</if> " +
            "GROUP BY DATE(collect_time) ORDER BY date</script>")
    List<Map<String, Object>> dailyUsage(@Param("dormitoryId") Long dormitoryId,
                                         @Param("start") LocalDateTime start,
                                         @Param("end") LocalDateTime end);

    @Select("SELECT dormitory_id, SUM(usage_kwh) as total " +
            "FROM electricity_record WHERE collect_time >= #{start} AND collect_time <= #{end} " +
            "GROUP BY dormitory_id ORDER BY total DESC")
    List<Map<String, Object>> rankingByRange(@Param("start") LocalDateTime start,
                                             @Param("end") LocalDateTime end);

    @Select("<script>SELECT SUM(usage_kwh) FROM electricity_record " +
            "WHERE collect_time >= #{start} AND collect_time &lt;= #{end}" +
            "<if test='dormitoryId != null'> AND dormitory_id = #{dormitoryId}</if></script>")
    Double totalUsage(@Param("dormitoryId") Long dormitoryId,
                      @Param("start") LocalDateTime start,
                      @Param("end") LocalDateTime end);

    @Select("SELECT d.building_no, d.room_no, SUM(er.usage_kwh) as total " +
            "FROM electricity_record er JOIN dormitory d ON er.dormitory_id = d.id " +
            "WHERE er.collect_time >= #{start} AND er.collect_time <= #{end} " +
            "GROUP BY er.dormitory_id ORDER BY total DESC LIMIT 10")
    List<Map<String, Object>> top10Usage(@Param("start") LocalDateTime start,
                                         @Param("end") LocalDateTime end);
}
