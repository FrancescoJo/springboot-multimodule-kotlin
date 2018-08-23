/* MySQL initial table creation script */
CREATE TABLE IF NOT EXISTS `members` (
  `id`   INTEGER UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(32)
)
  DEFAULT CHARACTER SET = utf8mb4;


INSERT INTO `members` (`id`, `name`)
VALUES ('1', 'FirstUser')
ON DUPLICATE KEY UPDATE `id`   = VALUES(`id`),
                        `name` = VALUES(`name`);
