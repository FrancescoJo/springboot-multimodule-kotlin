/* MariaDB SQL */
CREATE TABLE IF NOT EXISTS `access_log` (
  `seq`       BIGINT        NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `timestamp` DATETIME                DEFAULT CURRENT_TIMESTAMP,
  `user_id`   BIGINT        NOT NULL,
  `activity`  VARCHAR(4)    NOT NULL  DEFAULT '',
  `ip_addr`   VARBINARY(16) NOT NULL,
  `input`     TEXT,
  `output`    TEXT,

  UNIQUE KEY UK_Access_Log_History(`seq`, `timestamp`, `user_id`, `activity`)
)
  COMMENT 'Stores user activity for management purpose.'
  DEFAULT CHARACTER SET = utf8mb4;
