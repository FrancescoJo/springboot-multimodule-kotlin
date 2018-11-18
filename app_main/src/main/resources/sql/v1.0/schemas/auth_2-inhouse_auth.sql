/* MariaDB SQL */
CREATE TABLE IF NOT EXISTS `in_house_auth` (
  `id`                BIGINT         NOT NULL PRIMARY KEY,
  `access_token`      VARBINARY(127) NOT NULL UNIQUE,

  FOREIGN KEY FK_InHouseAuth_Users_Id(`id`) REFERENCES `users` (`id`)
  ON DELETE CASCADE
)
  COMMENT 'Stores values used for in-house authentication.';
