-- 高校学生公寓智能电表数据可视化系统 - 数据库初始化脚本
CREATE DATABASE IF NOT EXISTS dormitory_monitor DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE dormitory_monitor;

-- 用户表
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `student_no` VARCHAR(20) NOT NULL COMMENT '学号',
  `name` VARCHAR(50) NOT NULL COMMENT '姓名',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
  `password` VARCHAR(100) NOT NULL COMMENT '密码',
  `gender` VARCHAR(4) DEFAULT NULL COMMENT '性别',
  `college` VARCHAR(100) DEFAULT NULL COMMENT '学院',
  `dormitory_id` BIGINT DEFAULT NULL COMMENT '宿舍ID',
  `role` VARCHAR(20) NOT NULL DEFAULT 'student' COMMENT '角色: student/admin/super_admin',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 1启用 0禁用',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_student_no` (`student_no`),
  KEY `idx_dormitory_id` (`dormitory_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 宿舍表
DROP TABLE IF EXISTS `dormitory`;
CREATE TABLE `dormitory` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `building_no` VARCHAR(10) NOT NULL COMMENT '楼栋号',
  `room_no` VARCHAR(10) NOT NULL COMMENT '房间号',
  `floor` INT DEFAULT NULL COMMENT '楼层',
  `capacity` INT NOT NULL DEFAULT 4 COMMENT '容量(人数)',
  `current_occupants` INT NOT NULL DEFAULT 0 COMMENT '当前人数',
  `meter_id` BIGINT DEFAULT NULL COMMENT '电表ID',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 1正常 0停用',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_building_room` (`building_no`, `room_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='宿舍表';

-- 电表表
DROP TABLE IF EXISTS `meter`;
CREATE TABLE `meter` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `meter_no` VARCHAR(30) NOT NULL COMMENT '电表编号',
  `dormitory_id` BIGINT DEFAULT NULL COMMENT '宿舍ID',
  `current_usage` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '当前用电读数(kWh)',
  `voltage` DECIMAL(8,2) DEFAULT NULL COMMENT '电压(V)',
  `current` DECIMAL(8,2) DEFAULT NULL COMMENT '电流(A)',
  `power` DECIMAL(8,2) DEFAULT NULL COMMENT '功率(W)',
  `collect_frequency` INT NOT NULL DEFAULT 60 COMMENT '采集频率(分钟)',
  `alert_threshold` DECIMAL(10,2) NOT NULL DEFAULT 50.00 COMMENT '预警阈值(kWh/日)',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 1运行 0停用',
  `last_collect_time` DATETIME DEFAULT NULL COMMENT '最后采集时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_meter_no` (`meter_no`),
  KEY `idx_dormitory_id` (`dormitory_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='电表表';

-- 用电记录表
DROP TABLE IF EXISTS `electricity_record`;
CREATE TABLE `electricity_record` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `meter_id` BIGINT NOT NULL COMMENT '电表ID',
  `dormitory_id` BIGINT NOT NULL COMMENT '宿舍ID',
  `usage_kwh` DECIMAL(10,4) NOT NULL DEFAULT 0.0000 COMMENT '用电量(kWh)',
  `voltage` DECIMAL(8,2) DEFAULT NULL,
  `current` DECIMAL(8,2) DEFAULT NULL,
  `power` DECIMAL(8,2) DEFAULT NULL,
  `collect_time` DATETIME NOT NULL COMMENT '采集时间',
  PRIMARY KEY (`id`),
  KEY `idx_meter_id` (`meter_id`),
  KEY `idx_dormitory_id` (`dormitory_id`),
  KEY `idx_collect_time` (`collect_time`),
  KEY `idx_dorm_time` (`dormitory_id`, `collect_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用电记录表';

-- 预警记录表
DROP TABLE IF EXISTS `alert`;
CREATE TABLE `alert` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `dormitory_id` BIGINT NOT NULL,
  `alert_type` VARCHAR(20) NOT NULL COMMENT '预警类型: high_usage/abnormal/device_error',
  `alert_data` VARCHAR(500) DEFAULT NULL COMMENT '预警数据详情',
  `alert_time` DATETIME NOT NULL COMMENT '预警时间',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态: 0未处理 1已处理',
  `handle_note` VARCHAR(500) DEFAULT NULL COMMENT '处理备注',
  `handle_time` DATETIME DEFAULT NULL COMMENT '处理时间',
  PRIMARY KEY (`id`),
  KEY `idx_dormitory_id` (`dormitory_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预警记录表';

-- 账单表
DROP TABLE IF EXISTS `bill`;
CREATE TABLE `bill` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `dormitory_id` BIGINT NOT NULL,
  `bill_month` VARCHAR(7) NOT NULL COMMENT '账单月份(YYYY-MM)',
  `usage_kwh` DECIMAL(10,4) NOT NULL DEFAULT 0.0000 COMMENT '用电量',
  `unit_price` DECIMAL(10,4) NOT NULL DEFAULT 0.5000 COMMENT '单价(元/kWh)',
  `amount` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '金额',
  `pay_status` TINYINT NOT NULL DEFAULT 0 COMMENT '缴费状态: 0未缴 1已缴',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_dormitory_id` (`dormitory_id`),
  UNIQUE KEY `uk_dorm_month` (`dormitory_id`, `bill_month`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账单表';

-- ===================== 初始测试数据 =====================

-- 系统管理员 (密码: admin123)
INSERT INTO `user` (`student_no`, `name`, `phone`, `password`, `role`, `status`) VALUES
('admin001', '系统管理员', '13800000000', 'admin123', 'super_admin', 1);

-- 宿舍管理员 (密码: admin123)
INSERT INTO `user` (`student_no`, `name`, `phone`, `password`, `role`, `status`) VALUES
('staff001', '张管理', '13800000001', 'admin123', 'admin', 1),
('staff002', '李管理', '13800000002', 'admin123', 'admin', 1);

-- 宿舍数据
INSERT INTO `dormitory` (`building_no`, `room_no`, `floor`, `capacity`, `current_occupants`) VALUES
('1', '101', 1, 4, 3), ('1', '102', 1, 4, 4), ('1', '103', 1, 4, 2),
('1', '201', 2, 4, 4), ('1', '202', 2, 4, 3), ('1', '203', 2, 4, 4),
('2', '101', 1, 4, 4), ('2', '102', 1, 4, 2), ('2', '103', 1, 4, 3),
('2', '201', 2, 4, 4), ('2', '202', 2, 4, 4), ('2', '203', 2, 4, 1),
('3', '101', 1, 6, 5), ('3', '102', 1, 6, 6), ('3', '103', 1, 6, 4);

-- 电表数据
INSERT INTO `meter` (`meter_no`, `dormitory_id`, `current_usage`, `voltage`, `current`, `power`, `alert_threshold`) VALUES
('M001', 1, 125.50, 220, 1.2, 264, 50),
('M002', 2, 98.30, 220, 0.8, 176, 50),
('M003', 3, 210.20, 220, 2.1, 462, 50),
('M004', 4, 156.80, 220, 1.5, 330, 50),
('M005', 5, 88.90, 220, 0.9, 198, 50),
('M006', 6, 302.10, 220, 2.8, 616, 50),
('M007', 7, 145.60, 220, 1.3, 286, 50),
('M008', 8, 76.40, 220, 0.6, 132, 50),
('M009', 9, 189.30, 220, 1.8, 396, 50),
('M010', 10, 267.50, 220, 2.5, 550, 50),
('M011', 11, 134.20, 220, 1.1, 242, 50),
('M012', 12, 52.10, 220, 0.4, 88, 50),
('M013', 13, 198.70, 220, 1.9, 418, 50),
('M014', 14, 321.40, 220, 3.0, 660, 50),
('M015', 15, 167.90, 220, 1.6, 352, 50);

-- 更新宿舍电表关联
UPDATE dormitory SET meter_id = id;

-- 学生用户 (密码: 123456)
INSERT INTO `user` (`student_no`, `name`, `phone`, `password`, `dormitory_id`, `gender`, `college`, `role`) VALUES
('20210001', '王同学', '13900000001', '123456', 1, '男', '计算机学院', 'student'),
('20210002', '李同学', '13900000002', '123456', 1, '男', '计算机学院', 'student'),
('20210003', '张同学', '13900000003', '123456', 1, '男', '计算机学院', 'student'),
('20210004', '赵同学', '13900000004', '123456', 2, '女', '管理学院', 'student'),
('20210005', '刘同学', '13900000005', '123456', 2, '女', '管理学院', 'student'),
('20210006', '陈同学', '13900000006', '123456', 2, '女', '管理学院', 'student'),
('20210007', '周同学', '13900000007', '123456', 2, '女', '管理学院', 'student'),
('20210008', '吴同学', '13900000008', '123456', 4, '男', '电子学院', 'student'),
('20210009', '郑同学', '13900000009', '123456', 4, '男', '电子学院', 'student');

-- 近30天用电记录（模拟数据）
-- 为每个宿舍生成30天数据
INSERT INTO `electricity_record` (`meter_id`, `dormitory_id`, `usage_kwh`, `collect_time`)
SELECT
  m.id AS meter_id,
  m.dormitory_id,
  ROUND(2 + RAND() * 6, 4) AS usage_kwh,
  DATE_ADD(CURDATE(), INTERVAL -days.n DAY) AS collect_time
FROM meter m
CROSS JOIN (
  SELECT 0 AS n UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9
  UNION SELECT 10 UNION SELECT 11 UNION SELECT 12 UNION SELECT 13 UNION SELECT 14 UNION SELECT 15 UNION SELECT 16 UNION SELECT 17 UNION SELECT 18 UNION SELECT 19
  UNION SELECT 20 UNION SELECT 21 UNION SELECT 22 UNION SELECT 23 UNION SELECT 24 UNION SELECT 25 UNION SELECT 26 UNION SELECT 27 UNION SELECT 28 UNION SELECT 29
) days
WHERE m.dormitory_id IS NOT NULL;

-- 预警记录
INSERT INTO `alert` (`dormitory_id`, `alert_type`, `alert_data`, `alert_time`, `status`, `handle_note`, `handle_time`) VALUES
(6, 'high_usage', '单日用电量52.3kWh,超过阈值50kWh', DATE_SUB(CURDATE(), INTERVAL 3 DAY), 0, NULL, NULL),
(14, 'high_usage', '单日用电量65.1kWh,超过阈值50kWh', DATE_SUB(CURDATE(), INTERVAL 5 DAY), 0, NULL, NULL),
(10, 'abnormal', '功率异常: 550W (正常范围100-400W)', DATE_SUB(CURDATE(), INTERVAL 2 DAY), 1, '已核实，学生使用大功率电器已没收', DATE_SUB(CURDATE(), INTERVAL 1 DAY)),
(4, 'high_usage', '连续3天用电量超40kWh', DATE_SUB(CURDATE(), INTERVAL 7 DAY), 1, '已通知学生注意节能', DATE_SUB(CURDATE(), INTERVAL 6 DAY));

-- 账单数据 (最近6个月)
INSERT INTO `bill` (`dormitory_id`, `bill_month`, `usage_kwh`, `unit_price`, `amount`, `pay_status`)
SELECT
  d.id AS dormitory_id,
  DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL m.n MONTH), '%Y-%m') AS bill_month,
  ROUND(80 + RAND() * 120, 4) AS usage_kwh,
  0.5 AS unit_price,
  ROUND((80 + RAND() * 120) * 0.5, 2) AS amount,
  IF(m.n = 0, 0, 1) AS pay_status
FROM dormitory d
CROSS JOIN (SELECT 0 AS n UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5) m;
