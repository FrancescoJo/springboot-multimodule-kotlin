/* MariaDB SQL */
CREATE TABLE IF NOT EXISTS `rsa_key_pairs` (
  `id`          VARCHAR(36) NOT NULL PRIMARY KEY,
  `is_enabled`  INT         NOT NULL DEFAULT '1',
  `private_key` TEXT        NOT NULL,
  `public_key`  TEXT        NOT NULL,
  `issued_at`   DATETIME             DEFAULT CURRENT_TIMESTAMP
)
  COMMENT 'Stores RSA Key issue history.'
  DEFAULT CHARACTER SET = utf8mb4;
