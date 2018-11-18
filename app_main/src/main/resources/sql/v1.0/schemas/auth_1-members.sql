/* MariaDB SQL */
CREATE TABLE IF NOT EXISTS `members` (
  `id`                    BIGINT        NOT NULL PRIMARY KEY,
  `nickname`              VARCHAR(63)   NOT NULL,
  `gender`                VARCHAR(4)    NOT NULL DEFAULT '',
  `last_active_timestamp` DATETIME               DEFAULT CURRENT_TIMESTAMP,
  `last_active_ip`        VARBINARY(16) NOT NULL,
  `suspended_on`          DATETIME,
  `suspended_until`       DATETIME,

  FOREIGN KEY FK_Members_Users_Id(`id`) REFERENCES `users` (`id`)
    ON DELETE CASCADE
)
  COMMENT 'Stores user information which is likely to be changed.'
  DEFAULT CHARACTER SET = utf8mb4;
