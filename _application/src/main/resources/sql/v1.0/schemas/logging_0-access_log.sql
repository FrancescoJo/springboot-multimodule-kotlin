/* MariaDB SQL */
CREATE TABLE IF NOT EXISTS `access_log` (
  `seq`       BIGINT     NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `user_id`   BIGINT     NOT NULL,
  `activity`  VARCHAR(4) NOT NULL DEFAULT '',
  `input`     TEXT,
  `output`    TEXT,
  `timestamp` DATETIME            DEFAULT CURRENT_TIMESTAMP
)
  COMMENT 'Stores user activity for management purpose.'
  DEFAULT CHARACTER SET = utf8mb4;
