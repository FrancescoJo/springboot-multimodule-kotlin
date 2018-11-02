/* MariaDB SQL */
CREATE TABLE IF NOT EXISTS `users` (
  `id`               BIGINT         NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `id_token`         VARCHAR(31)    NOT NULL UNIQUE,
  `status`           VARCHAR(4)     NOT NULL,
  `roles`            VARCHAR(31)    NOT NULL
  COMMENT 'Value for obscuring sequential property of primary key.',
  `name`             VARCHAR(31)    NOT NULL UNIQUE
  COMMENT 'Internal user name representation for management',
  `login_type`       VARCHAR(4)     NOT NULL,
  `platform_type`    VARCHAR(4)     NOT NULL DEFAULT '',
  `platform_version` VARCHAR(127)   NOT NULL DEFAULT '',
  `app_version`      VARCHAR(31)    NOT NULL DEFAULT '',
  `email`            VARCHAR(127)   NOT NULL DEFAULT '',
  `created_date`     DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_ip`       VARBINARY(16)  NOT NULL
  COMMENT 'IPv6 address used for signing up. IPv4 addresses should be converted for IPv6 format that following the rule of RFC 4291.',
  `push_token`       TEXT           NOT NULL
  COMMENT 'Firebase Cloud Messaging push token',
  `invited_by`       BIGINT         NOT NULL DEFAULT 0
  COMMENT 'User id of invitation host.',
  `credential`       VARBINARY(254) NOT NULL
  COMMENT 'Security credential, usually a password, 3rdparty access token, etc.',

  UNIQUE KEY UK_Users_Identity(`id_token`, `name`),
  UNIQUE KEY UK_Users_Status(`id`, `status`),
  UNIQUE KEY UK_Users_Credential(`name`, `login_type`, `email`, `credential`)
)
  COMMENT 'Stores user information which scarcely changes.'
