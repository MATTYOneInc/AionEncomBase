/*
 Navicat Premium Data Transfer

 Source Server         : Localhost
 Source Server Type    : MySQL
 Source Server Version : 100419 (10.4.19-MariaDB)
 Source Host           : localhost:3306
 Source Schema         : al_server_gs

 Target Server Type    : MySQL
 Target Server Version : 100419 (10.4.19-MariaDB)
 File Encoding         : 65001

 Date: 09/04/2024 18:17:14
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for abyss_landing
-- ----------------------------
DROP TABLE IF EXISTS `abyss_landing`;
CREATE TABLE `abyss_landing`  (
  `id` int NOT NULL,
  `level` int NOT NULL DEFAULT 1,
  `points` int NOT NULL DEFAULT 0,
  `siege` int NOT NULL DEFAULT 0,
  `commander` int NOT NULL DEFAULT 0,
  `artefact` int NOT NULL DEFAULT 0,
  `base` int NOT NULL DEFAULT 0,
  `monuments` int NOT NULL DEFAULT 0,
  `quest` int NOT NULL DEFAULT 0,
  `facility` int NOT NULL DEFAULT 0,
  `race` enum('ELYOS','ASMODIANS') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `level_up_date` timestamp NOT NULL DEFAULT '2015-01-01 01:00:00',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for abyss_rank
-- ----------------------------
DROP TABLE IF EXISTS `abyss_rank`;
CREATE TABLE `abyss_rank`  (
  `player_id` int NOT NULL,
  `daily_ap` int NOT NULL,
  `daily_gp` int NOT NULL,
  `weekly_ap` int NOT NULL,
  `weekly_gp` int NOT NULL,
  `ap` int NOT NULL,
  `gp` int NOT NULL,
  `rank` int NOT NULL DEFAULT 1,
  `top_ranking` int NOT NULL,
  `daily_kill` int NOT NULL,
  `weekly_kill` int NOT NULL,
  `all_kill` int NOT NULL DEFAULT 0,
  `max_rank` int NOT NULL DEFAULT 1,
  `last_kill` int NOT NULL,
  `last_ap` int NOT NULL,
  `last_gp` int NOT NULL,
  `last_update` decimal(20, 0) NOT NULL,
  `rank_pos` int NOT NULL DEFAULT 0,
  `old_rank_pos` int NOT NULL DEFAULT 0,
  `rank_ap` int NOT NULL DEFAULT 0,
  PRIMARY KEY (`player_id`) USING BTREE,
  CONSTRAINT `abyss_rank_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for account_balance
-- ----------------------------
DROP TABLE IF EXISTS `account_balance`;
CREATE TABLE `account_balance`  (
  `account_id` int NOT NULL,
  `price_id` int NOT NULL DEFAULT 0,
  `value` int NULL DEFAULT 0,
  PRIMARY KEY (`account_id`, `price_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for announcements
-- ----------------------------
DROP TABLE IF EXISTS `announcements`;
CREATE TABLE `announcements`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `announce` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `faction` enum('ALL','ASMODIANS','ELYOS') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'ALL',
  `type` enum('SHOUT','ORANGE','YELLOW','WHITE','SYSTEM') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'SYSTEM',
  `delay` int NOT NULL DEFAULT 1800,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for banned_hdd
-- ----------------------------
DROP TABLE IF EXISTS `banned_hdd`;
CREATE TABLE `banned_hdd`  (
  `uniId` int NOT NULL AUTO_INCREMENT,
  `hdd_serial` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  `details` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  PRIMARY KEY (`uniId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for base_location
-- ----------------------------
DROP TABLE IF EXISTS `base_location`;
CREATE TABLE `base_location`  (
  `id` int NOT NULL,
  `race` enum('ELYOS','ASMODIANS','NPC') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for blocks
-- ----------------------------
DROP TABLE IF EXISTS `blocks`;
CREATE TABLE `blocks`  (
  `player` int NOT NULL,
  `blocked_player` int NOT NULL,
  `reason` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  PRIMARY KEY (`player`, `blocked_player`) USING BTREE,
  INDEX `blocked_player`(`blocked_player` ASC) USING BTREE,
  CONSTRAINT `blocks_ibfk_1` FOREIGN KEY (`player`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `blocks_ibfk_2` FOREIGN KEY (`blocked_player`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bookmark
-- ----------------------------
DROP TABLE IF EXISTS `bookmark`;
CREATE TABLE `bookmark`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `char_id` int NOT NULL,
  `x` float NOT NULL,
  `y` float NOT NULL,
  `z` float NOT NULL,
  `world_id` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for broker
-- ----------------------------
DROP TABLE IF EXISTS `broker`;
CREATE TABLE `broker`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `item_pointer` int NOT NULL DEFAULT 0,
  `item_id` int NOT NULL,
  `item_count` bigint NOT NULL,
  `item_creator` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `seller` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `price` bigint NOT NULL DEFAULT 0,
  `broker_race` enum('ELYOS','ASMODIAN') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `expire_time` timestamp NOT NULL DEFAULT '2010-01-01 14:00:00',
  `settle_time` timestamp NOT NULL DEFAULT '2010-01-01 14:00:00',
  `seller_id` int NOT NULL,
  `is_sold` tinyint(1) NOT NULL,
  `is_settled` tinyint(1) NOT NULL,
  `is_splitsell` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `seller_id`(`seller_id` ASC) USING BTREE,
  CONSTRAINT `broker_ibfk_1` FOREIGN KEY (`seller_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1153 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for challenge_tasks
-- ----------------------------
DROP TABLE IF EXISTS `challenge_tasks`;
CREATE TABLE `challenge_tasks`  (
  `task_id` int NOT NULL,
  `quest_id` int NOT NULL,
  `owner_id` int NOT NULL,
  `owner_type` enum('LEGION','TOWN') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `complete_count` int UNSIGNED NOT NULL DEFAULT 0,
  `complete_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`task_id`, `quest_id`, `owner_id`, `owner_type`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for competition_ranking
-- ----------------------------
DROP TABLE IF EXISTS `competition_ranking`;
CREATE TABLE `competition_ranking`  (
  `player_id` int NOT NULL,
  `table_id` int NOT NULL,
  `rank` int NOT NULL DEFAULT 0,
  `last_rank` int NOT NULL DEFAULT 0,
  `points` int NOT NULL DEFAULT 0,
  `last_points` int NOT NULL DEFAULT 0,
  `high_points` int NOT NULL DEFAULT 0,
  `low_points` int NOT NULL DEFAULT 0,
  `position_match` int NOT NULL DEFAULT 5
) ENGINE = MyISAM CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Fixed;

-- ----------------------------
-- Table structure for craft_cooldowns
-- ----------------------------
DROP TABLE IF EXISTS `craft_cooldowns`;
CREATE TABLE `craft_cooldowns`  (
  `player_id` int NOT NULL,
  `delay_id` int UNSIGNED NOT NULL,
  `reuse_time` bigint UNSIGNED NOT NULL,
  PRIMARY KEY (`player_id`, `delay_id`) USING BTREE,
  CONSTRAINT `craft_cooldowns_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for event_items
-- ----------------------------
DROP TABLE IF EXISTS `event_items`;
CREATE TABLE `event_items`  (
  `player_id` int NOT NULL,
  `item_id` int NOT NULL,
  `counts` int UNSIGNED NOT NULL,
  PRIMARY KEY (`player_id`, `item_id`) USING BTREE,
  CONSTRAINT `event_items_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for f2paccount
-- ----------------------------
DROP TABLE IF EXISTS `f2paccount`;
CREATE TABLE `f2paccount`  (
  `player_id` int NOT NULL,
  `time` int NOT NULL,
  PRIMARY KEY (`player_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for friends
-- ----------------------------
DROP TABLE IF EXISTS `friends`;
CREATE TABLE `friends`  (
  `player` int NOT NULL,
  `friend` int NOT NULL,
  PRIMARY KEY (`player`, `friend`) USING BTREE,
  INDEX `friend`(`friend` ASC) USING BTREE,
  CONSTRAINT `friends_ibfk_1` FOREIGN KEY (`player`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `friends_ibfk_2` FOREIGN KEY (`friend`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for guides
-- ----------------------------
DROP TABLE IF EXISTS `guides`;
CREATE TABLE `guides`  (
  `guide_id` int NOT NULL AUTO_INCREMENT,
  `player_id` int NOT NULL,
  `title` varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`guide_id`) USING BTREE,
  INDEX `player_id`(`player_id` ASC) USING BTREE,
  CONSTRAINT `guides_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 3391856 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for house_bids
-- ----------------------------
DROP TABLE IF EXISTS `house_bids`;
CREATE TABLE `house_bids`  (
  `player_id` int NOT NULL,
  `house_id` int NOT NULL,
  `bid` bigint NOT NULL,
  `bid_time` timestamp NULL DEFAULT current_timestamp,
  PRIMARY KEY (`player_id`, `house_id`, `bid`) USING BTREE,
  INDEX `house_id_ibfk_1`(`house_id` ASC) USING BTREE,
  CONSTRAINT `house_bids_ibfk_1` FOREIGN KEY (`house_id`) REFERENCES `houses` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for house_object_cooldowns
-- ----------------------------
DROP TABLE IF EXISTS `house_object_cooldowns`;
CREATE TABLE `house_object_cooldowns`  (
  `player_id` int NOT NULL,
  `object_id` int NOT NULL,
  `reuse_time` bigint NOT NULL,
  PRIMARY KEY (`player_id`, `object_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for house_scripts
-- ----------------------------
DROP TABLE IF EXISTS `house_scripts`;
CREATE TABLE `house_scripts`  (
  `house_id` int NOT NULL,
  `index` tinyint NOT NULL,
  `script` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  PRIMARY KEY (`house_id`, `index`) USING BTREE,
  CONSTRAINT `house_scripts_ibfk_1` FOREIGN KEY (`house_id`) REFERENCES `houses` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci KEY_BLOCK_SIZE = 16 ROW_FORMAT = COMPRESSED;

-- ----------------------------
-- Table structure for houses
-- ----------------------------
DROP TABLE IF EXISTS `houses`;
CREATE TABLE `houses`  (
  `id` int NOT NULL,
  `player_id` int NOT NULL DEFAULT 0,
  `building_id` int NOT NULL,
  `address` int NOT NULL,
  `acquire_time` timestamp NOT NULL DEFAULT current_timestamp ON UPDATE CURRENT_TIMESTAMP,
  `settings` int NOT NULL DEFAULT 0,
  `status` enum('ACTIVE','SELL_WAIT','INACTIVE','NOSALE') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'ACTIVE',
  `fee_paid` tinyint(1) NOT NULL DEFAULT 1,
  `next_pay` timestamp NULL DEFAULT NULL,
  `sell_started` timestamp NULL DEFAULT NULL,
  `sign_notice` binary(130) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `address`(`address` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ingameshop
-- ----------------------------
DROP TABLE IF EXISTS `ingameshop`;
CREATE TABLE `ingameshop`  (
  `object_id` int NOT NULL AUTO_INCREMENT,
  `item_id` int NOT NULL,
  `item_count` bigint NOT NULL DEFAULT 0,
  `item_price` bigint NOT NULL DEFAULT 0,
  `category` tinyint(1) NOT NULL DEFAULT 0,
  `sub_category` tinyint(1) NOT NULL DEFAULT 0,
  `list` int NOT NULL DEFAULT 0,
  `sales_ranking` int NOT NULL DEFAULT 0,
  `item_type` tinyint(1) NOT NULL DEFAULT 0,
  `gift` tinyint(1) NOT NULL DEFAULT 0,
  `title_description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`object_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for inventory
-- ----------------------------
DROP TABLE IF EXISTS `inventory`;
CREATE TABLE `inventory`  (
  `item_unique_id` int NOT NULL,
  `item_id` int NOT NULL,
  `item_count` bigint NOT NULL DEFAULT 0,
  `item_color` int NOT NULL DEFAULT 0,
  `color_expires` int NULL DEFAULT NULL,
  `item_owner` int NOT NULL,
  `item_creator` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `itemCreationTime` timestamp NOT NULL DEFAULT '2013-01-01 12:00:01',
  `expire_time` int NOT NULL DEFAULT 0,
  `is_equiped` tinyint(1) NOT NULL DEFAULT 0,
  `is_soul_bound` tinyint(1) NOT NULL DEFAULT 0,
  `slot` bigint NOT NULL DEFAULT 0,
  `item_location` tinyint(1) NULL DEFAULT 0,
  `enchant` tinyint(1) NULL DEFAULT 0,
  `enchant_bonus` int NOT NULL DEFAULT 0,
  `item_skin` int NOT NULL DEFAULT 0,
  `fusioned_item` int NOT NULL DEFAULT 0,
  `optional_socket` int NOT NULL DEFAULT 0,
  `optional_fusion_socket` int NOT NULL DEFAULT 0,
  `activation_count` int NOT NULL DEFAULT 0,
  `charge` mediumint NOT NULL DEFAULT 0,
  `rnd_bonus` smallint NULL DEFAULT NULL,
  `rnd_count` smallint NOT NULL DEFAULT 0,
  `wrappable_count` smallint NOT NULL DEFAULT 0,
  `is_packed` tinyint(1) NOT NULL DEFAULT 0,
  `tempering_level` smallint NOT NULL DEFAULT 0,
  `is_topped` tinyint(1) NOT NULL DEFAULT 0,
  `strengthen_skill` int NOT NULL DEFAULT 0,
  `skin_skill` int NULL DEFAULT 0,
  `luna_reskin` tinyint(1) NOT NULL DEFAULT 0,
  `reduction_level` int NOT NULL DEFAULT 0,
  `is_seal` int NOT NULL DEFAULT 0,
  `isEnhance` tinyint(1) NULL DEFAULT 0,
  `enhanceSkillId` int NOT NULL DEFAULT 0,
  `enhanceSkillEnchant` int NOT NULL DEFAULT 0,
  PRIMARY KEY (`item_unique_id`) USING BTREE,
  INDEX `item_owner`(`item_owner` ASC) USING BTREE,
  INDEX `item_location`(`item_location` ASC) USING BTREE,
  INDEX `is_equiped`(`is_equiped` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for item_cooldowns
-- ----------------------------
DROP TABLE IF EXISTS `item_cooldowns`;
CREATE TABLE `item_cooldowns`  (
  `player_id` int NOT NULL,
  `delay_id` int NOT NULL,
  `use_delay` int UNSIGNED NOT NULL,
  `reuse_time` bigint NOT NULL,
  PRIMARY KEY (`player_id`, `delay_id`) USING BTREE,
  CONSTRAINT `item_cooldowns_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for item_drop
-- ----------------------------
DROP TABLE IF EXISTS `item_drop`;
CREATE TABLE `item_drop`  (
  `npc_id` int NOT NULL,
  `item_id` int NOT NULL DEFAULT 0,
  `drop_group` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `race` enum('PC_ALL','ELYOS','ASMODIANS') CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'PC_ALL',
  `min_amount` int NOT NULL DEFAULT 0,
  `max_amount` int NOT NULL DEFAULT 0,
  `chance` decimal(11, 0) NULL DEFAULT NULL,
  `no_reduce` tinyint(1) NOT NULL DEFAULT 0,
  `eachmember` tinyint(1) NOT NULL DEFAULT 0
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for item_stones
-- ----------------------------
DROP TABLE IF EXISTS `item_stones`;
CREATE TABLE `item_stones`  (
  `item_unique_id` int NOT NULL,
  `item_id` int NOT NULL,
  `slot` int NOT NULL,
  `category` int NOT NULL DEFAULT 0,
  `polishNumber` int NULL DEFAULT NULL,
  `polishCharge` int NULL DEFAULT NULL,
  PRIMARY KEY (`item_unique_id`, `slot`, `category`) USING BTREE,
  CONSTRAINT `item_stones_ibfk_1` FOREIGN KEY (`item_unique_id`) REFERENCES `inventory` (`item_unique_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ladder_player
-- ----------------------------
DROP TABLE IF EXISTS `ladder_player`;
CREATE TABLE `ladder_player`  (
  `player_id` int NOT NULL,
  `rating` int NULL DEFAULT 1000,
  `wins` int NULL DEFAULT NULL,
  `losses` int NULL DEFAULT NULL,
  `leaves` int NULL DEFAULT NULL,
  `rank` int NOT NULL DEFAULT -1,
  `last_rank` int NOT NULL DEFAULT -1,
  `last_update` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`player_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for legion_announcement_list
-- ----------------------------
DROP TABLE IF EXISTS `legion_announcement_list`;
CREATE TABLE `legion_announcement_list`  (
  `legion_id` int NOT NULL,
  `announcement` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `date` timestamp NOT NULL DEFAULT current_timestamp,
  INDEX `legion_id`(`legion_id` ASC) USING BTREE,
  CONSTRAINT `legion_announcement_list_ibfk_1` FOREIGN KEY (`legion_id`) REFERENCES `legions` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for legion_emblems
-- ----------------------------
DROP TABLE IF EXISTS `legion_emblems`;
CREATE TABLE `legion_emblems`  (
  `legion_id` int NOT NULL,
  `emblem_id` int NOT NULL DEFAULT 0,
  `color_r` int NOT NULL DEFAULT 0,
  `color_g` int NOT NULL DEFAULT 0,
  `color_b` int NOT NULL DEFAULT 0,
  `emblem_type` enum('DEFAULT','CUSTOM') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'DEFAULT',
  `emblem_data` longblob NULL,
  PRIMARY KEY (`legion_id`) USING BTREE,
  CONSTRAINT `legion_emblems_ibfk_1` FOREIGN KEY (`legion_id`) REFERENCES `legions` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for legion_history
-- ----------------------------
DROP TABLE IF EXISTS `legion_history`;
CREATE TABLE `legion_history`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `legion_id` int NOT NULL,
  `date` timestamp NOT NULL DEFAULT current_timestamp,
  `history_type` enum('CREATE','JOIN','KICK','APPOINTED','EMBLEM_REGISTER','EMBLEM_MODIFIED','ITEM_DEPOSIT','ITEM_WITHDRAW','KINAH_DEPOSIT','KINAH_WITHDRAW','LEVEL_UP') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `tab_id` smallint NOT NULL DEFAULT 0,
  `description` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `legion_id`(`legion_id` ASC) USING BTREE,
  CONSTRAINT `legion_history_ibfk_1` FOREIGN KEY (`legion_id`) REFERENCES `legions` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 17796 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for legion_join_requests
-- ----------------------------
DROP TABLE IF EXISTS `legion_join_requests`;
CREATE TABLE `legion_join_requests`  (
  `legionId` int NOT NULL DEFAULT 0,
  `playerId` int NOT NULL DEFAULT 0,
  `playerName` varchar(64) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL DEFAULT '',
  `playerClassId` int NOT NULL DEFAULT 0,
  `playerRaceId` int NOT NULL DEFAULT 0,
  `playerLevel` int NOT NULL DEFAULT 0,
  `playerGenderId` int NOT NULL DEFAULT 0,
  `joinRequestMsg` varchar(40) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL DEFAULT '',
  `date` timestamp NOT NULL DEFAULT current_timestamp,
  PRIMARY KEY (`legionId`, `playerId`) USING BTREE,
  INDEX `legionId`(`legionId` ASC) USING BTREE,
  INDEX `playerId`(`playerId` ASC) USING BTREE,
  CONSTRAINT `legion_join_requests_ibfk_1` FOREIGN KEY (`legionId`) REFERENCES `legions` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `legion_join_requests_ibfk_2` FOREIGN KEY (`playerId`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for legion_members
-- ----------------------------
DROP TABLE IF EXISTS `legion_members`;
CREATE TABLE `legion_members`  (
  `legion_id` int NOT NULL,
  `player_id` int NOT NULL,
  `nickname` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `rank` enum('BRIGADE_GENERAL','CENTURION','LEGIONARY','DEPUTY','VOLUNTEER') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'VOLUNTEER',
  `selfintro` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `challenge_score` int NOT NULL DEFAULT 0,
  PRIMARY KEY (`player_id`) USING BTREE,
  INDEX `player_id`(`player_id` ASC) USING BTREE,
  INDEX `legion_id`(`legion_id` ASC) USING BTREE,
  CONSTRAINT `legion_members_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `legion_members_ibfk_2` FOREIGN KEY (`legion_id`) REFERENCES `legions` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for legions
-- ----------------------------
DROP TABLE IF EXISTS `legions`;
CREATE TABLE `legions`  (
  `id` int NOT NULL,
  `name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `level` int NOT NULL DEFAULT 1,
  `contribution_points` bigint NOT NULL DEFAULT 0,
  `deputy_permission` int NOT NULL DEFAULT 7692,
  `centurion_permission` int NOT NULL DEFAULT 7176,
  `legionary_permission` int NOT NULL DEFAULT 6144,
  `volunteer_permission` int NOT NULL DEFAULT 2048,
  `disband_time` int NOT NULL DEFAULT 0,
  `rank_cp` int NOT NULL DEFAULT 0,
  `rank_pos` int NOT NULL DEFAULT 0,
  `old_rank_pos` int NOT NULL DEFAULT 0,
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `joinType` int NOT NULL DEFAULT 0,
  `minJoinLevel` int NOT NULL DEFAULT 0,
  `territory` int NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name_unique`(`name` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for log_command_add
-- ----------------------------
DROP TABLE IF EXISTS `log_command_add`;
CREATE TABLE `log_command_add`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `admin_id` int NOT NULL DEFAULT 0,
  `admin_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `player_id` int NULL DEFAULT 0,
  `player_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `item_unique` int NULL DEFAULT 0,
  `item_id` int NULL DEFAULT 0,
  `item_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `item_count` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `date` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 211 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for log_exchange_admin
-- ----------------------------
DROP TABLE IF EXISTS `log_exchange_admin`;
CREATE TABLE `log_exchange_admin`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `admin_id` int NOT NULL DEFAULT 0,
  `admin_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `player_id` int NULL DEFAULT 0,
  `player_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `item_id` int NULL DEFAULT 0,
  `item_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `item_count` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `date` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 429 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for log_exchange_player
-- ----------------------------
DROP TABLE IF EXISTS `log_exchange_player`;
CREATE TABLE `log_exchange_player`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `player_id` int NOT NULL DEFAULT 0,
  `player_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `partner_id` int NULL DEFAULT 0,
  `partner_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `item_id` int NULL DEFAULT 0,
  `item_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `item_count` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `date` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8995 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for log_mail_admin
-- ----------------------------
DROP TABLE IF EXISTS `log_mail_admin`;
CREATE TABLE `log_mail_admin`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `admin_id` int NOT NULL DEFAULT 0,
  `admin_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `item_id` int NULL DEFAULT 0,
  `item_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `item_count` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0',
  `player_recive_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `date` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 138 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for log_mail_player
-- ----------------------------
DROP TABLE IF EXISTS `log_mail_player`;
CREATE TABLE `log_mail_player`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `sender_id` int NOT NULL DEFAULT 0,
  `sender_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `item_id` int NULL DEFAULT 0,
  `item_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `item_count` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0',
  `player_recive_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `date` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1029 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mail
-- ----------------------------
DROP TABLE IF EXISTS `mail`;
CREATE TABLE `mail`  (
  `mail_unique_id` int NOT NULL,
  `mail_recipient_id` int NOT NULL,
  `sender_name` varchar(26) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `mail_title` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `mail_message` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `unread` tinyint NOT NULL DEFAULT 1,
  `attached_item_id` int NOT NULL,
  `attached_kinah_count` bigint NOT NULL,
  `attached_ap_count` bigint NOT NULL,
  `express` tinyint NOT NULL DEFAULT 0,
  `recieved_time` timestamp NOT NULL DEFAULT current_timestamp ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`mail_unique_id`) USING BTREE,
  INDEX `mail_recipient_id`(`mail_recipient_id` ASC) USING BTREE,
  CONSTRAINT `FK_mail` FOREIGN KEY (`mail_recipient_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for network_ban
-- ----------------------------
DROP TABLE IF EXISTS `network_ban`;
CREATE TABLE `network_ban`  (
  `uniId` int NOT NULL AUTO_INCREMENT,
  `ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  `details` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  PRIMARY KEY (`uniId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for old_names
-- ----------------------------
DROP TABLE IF EXISTS `old_names`;
CREATE TABLE `old_names`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT,
  `player_id` int NOT NULL,
  `old_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `new_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `player_id`(`player_id` ASC) USING BTREE,
  CONSTRAINT `old_names_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 75 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for outpost_location
-- ----------------------------
DROP TABLE IF EXISTS `outpost_location`;
CREATE TABLE `outpost_location`  (
  `id` int NOT NULL,
  `race` enum('ELYOS','ASMODIANS','NPC') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for petitions
-- ----------------------------
DROP TABLE IF EXISTS `petitions`;
CREATE TABLE `petitions`  (
  `id` bigint NOT NULL,
  `player_id` int NOT NULL,
  `type` int NOT NULL,
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `message` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `add_data` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `time` bigint NOT NULL DEFAULT 0,
  `status` enum('PENDING','IN_PROGRESS','REPLIED') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for player_appearance
-- ----------------------------
DROP TABLE IF EXISTS `player_appearance`;
CREATE TABLE `player_appearance`  (
  `player_id` int NOT NULL,
  `voice` int NOT NULL,
  `skin_rgb` int NOT NULL,
  `hair_rgb` int NOT NULL,
  `eye_rgb` int NOT NULL,
  `lip_rgb` int NOT NULL,
  `face` int NOT NULL,
  `hair` int NOT NULL,
  `deco` int NOT NULL,
  `tattoo` int NOT NULL,
  `face_contour` int NOT NULL,
  `expression` int NOT NULL,
  `pupil_shape` int NOT NULL,
  `remove_mane` int NOT NULL,
  `right_eye_rgb` int NOT NULL,
  `eye_lash_shape` int NOT NULL,
  `jaw_line` int NOT NULL,
  `forehead` int NOT NULL,
  `eye_height` int NOT NULL,
  `eye_space` int NOT NULL,
  `eye_width` int NOT NULL,
  `eye_size` int NOT NULL,
  `eye_shape` int NOT NULL,
  `eye_angle` int NOT NULL,
  `brow_height` int NOT NULL,
  `brow_angle` int NOT NULL,
  `brow_shape` int NOT NULL,
  `nose` int NOT NULL,
  `nose_bridge` int NOT NULL,
  `nose_width` int NOT NULL,
  `nose_tip` int NOT NULL,
  `cheek` int NOT NULL,
  `lip_height` int NOT NULL,
  `mouth_size` int NOT NULL,
  `lip_size` int NOT NULL,
  `smile` int NOT NULL,
  `lip_shape` int NOT NULL,
  `jaw_height` int NOT NULL,
  `chin_jut` int NOT NULL,
  `ear_shape` int NOT NULL,
  `head_size` int NOT NULL,
  `neck` int NOT NULL,
  `neck_length` int NOT NULL,
  `shoulder_size` int NOT NULL,
  `torso` int NOT NULL,
  `chest` int NOT NULL,
  `waist` int NOT NULL,
  `hips` int NOT NULL,
  `arm_thickness` int NOT NULL,
  `hand_size` int NOT NULL,
  `leg_thickness` int NOT NULL,
  `facial_rate` int NOT NULL,
  `foot_size` int NOT NULL,
  `arm_length` int NOT NULL,
  `leg_length` int NOT NULL,
  `shoulders` int NOT NULL,
  `face_shape` int NOT NULL,
  `pupil_size` int NOT NULL,
  `upper_torso` int NOT NULL,
  `fore_arm_thickness` int NOT NULL,
  `hand_span` int NOT NULL,
  `calf_thickness` int NOT NULL,
  `height` float NOT NULL,
  PRIMARY KEY (`player_id`) USING BTREE,
  CONSTRAINT `player_id_fk` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for player_atreian_bestiary
-- ----------------------------
DROP TABLE IF EXISTS `player_atreian_bestiary`;
CREATE TABLE `player_atreian_bestiary`  (
  `player_id` int NOT NULL,
  `id` int NOT NULL,
  `kill_count` int NOT NULL,
  `level` int NOT NULL,
  `claim_reward` int NOT NULL DEFAULT 0,
  PRIMARY KEY (`player_id`, `id`) USING BTREE,
  CONSTRAINT `fk_player_atreian_bestiary` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for player_bind_point
-- ----------------------------
DROP TABLE IF EXISTS `player_bind_point`;
CREATE TABLE `player_bind_point`  (
  `player_id` int NOT NULL,
  `map_id` int NOT NULL,
  `x` float NOT NULL,
  `y` float NOT NULL,
  `z` float NOT NULL,
  `heading` int NOT NULL,
  PRIMARY KEY (`player_id`) USING BTREE,
  CONSTRAINT `player_bind_point_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for player_combat_points
-- ----------------------------
DROP TABLE IF EXISTS `player_combat_points`;
CREATE TABLE `player_combat_points`  (
  `player_id` int NOT NULL,
  `slot_id` int NOT NULL,
  `cp_point` int NOT NULL DEFAULT 1,
  `category` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`player_id`, `slot_id`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for player_cooldowns
-- ----------------------------
DROP TABLE IF EXISTS `player_cooldowns`;
CREATE TABLE `player_cooldowns`  (
  `player_id` int NOT NULL,
  `cooldown_id` int NOT NULL,
  `reuse_delay` bigint NOT NULL,
  PRIMARY KEY (`player_id`, `cooldown_id`) USING BTREE,
  CONSTRAINT `player_cooldowns_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for player_cp
-- ----------------------------
DROP TABLE IF EXISTS `player_cp`;
CREATE TABLE `player_cp`  (
  `player_id` int NOT NULL,
  `slot` int NOT NULL,
  `point` int NOT NULL,
  PRIMARY KEY (`player_id`, `slot`) USING BTREE,
  CONSTRAINT `player_cp_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for player_effects
-- ----------------------------
DROP TABLE IF EXISTS `player_effects`;
CREATE TABLE `player_effects`  (
  `player_id` int NOT NULL,
  `skill_id` int NOT NULL,
  `skill_lvl` tinyint NOT NULL,
  `current_time` int NOT NULL,
  `end_time` bigint NOT NULL,
  PRIMARY KEY (`player_id`, `skill_id`) USING BTREE,
  CONSTRAINT `player_effects_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for player_emotions
-- ----------------------------
DROP TABLE IF EXISTS `player_emotions`;
CREATE TABLE `player_emotions`  (
  `player_id` int NOT NULL,
  `emotion` int NOT NULL,
  `remaining` int NOT NULL DEFAULT 0,
  PRIMARY KEY (`player_id`, `emotion`) USING BTREE,
  CONSTRAINT `player_emotions_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for player_equipment_setting
-- ----------------------------
DROP TABLE IF EXISTS `player_equipment_setting`;
CREATE TABLE `player_equipment_setting`  (
  `player_id` int NOT NULL,
  `slot` int NOT NULL,
  `name` varchar(225) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `display` int NOT NULL DEFAULT 0,
  `m_hand` int NOT NULL DEFAULT 0,
  `s_hand` int NOT NULL DEFAULT 0,
  `helmet` int NOT NULL DEFAULT 0,
  `torso` int NOT NULL DEFAULT 0,
  `glove` int NOT NULL DEFAULT 0,
  `boots` int NOT NULL DEFAULT 0,
  `earrings_left` int NOT NULL DEFAULT 0,
  `earrings_right` int NOT NULL DEFAULT 0,
  `ring_left` int NOT NULL DEFAULT 0,
  `ring_right` int NOT NULL DEFAULT 0,
  `necklace` int NOT NULL DEFAULT 0,
  `shoulder` int NOT NULL DEFAULT 0,
  `pants` int NOT NULL DEFAULT 0,
  `powershard_left` int NOT NULL DEFAULT 0,
  `powershard_right` int NOT NULL DEFAULT 0,
  `wings` int NOT NULL DEFAULT 0,
  `waist` int NOT NULL DEFAULT 0,
  `m_off_hand` int NOT NULL DEFAULT 0,
  `s_off_hand` int NOT NULL DEFAULT 0,
  `plume` int NOT NULL DEFAULT 0,
  `bracelet` int NOT NULL DEFAULT 0,
  PRIMARY KEY (`player_id`, `slot`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for player_event_cooldowns
-- ----------------------------
DROP TABLE IF EXISTS `player_event_cooldowns`;
CREATE TABLE `player_event_cooldowns`  (
  `player_id` int NOT NULL,
  `event_id` int NOT NULL,
  `count` int NOT NULL,
  `mac_address` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'xx-xx-xx-xx-xx-xx',
  `ip_address` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `next_use` decimal(20, 0) NOT NULL,
  PRIMARY KEY (`player_id`) USING BTREE,
  CONSTRAINT `player_event_cooldowns_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for player_events_window
-- ----------------------------
DROP TABLE IF EXISTS `player_events_window`;
CREATE TABLE `player_events_window`  (
  `account_id` int NOT NULL,
  `event_id` int NOT NULL,
  `last_stamp` timestamp NULL DEFAULT current_timestamp ON UPDATE CURRENT_TIMESTAMP,
  `elapsed` int NOT NULL DEFAULT 0,
  `reward_recived_count` int NOT NULL DEFAULT 0,
  PRIMARY KEY (`account_id`, `event_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Table structure for player_game_stats
-- ----------------------------
DROP TABLE IF EXISTS `player_game_stats`;
CREATE TABLE `player_game_stats`  (
  `player_id` int NOT NULL,
  `defense_physic` int NOT NULL DEFAULT 1,
  `block` int NOT NULL DEFAULT 1,
  `parry` int NOT NULL DEFAULT 1,
  `magical_critical` int NOT NULL DEFAULT 1,
  `evasion` int NOT NULL DEFAULT 1,
  `precision` int NOT NULL DEFAULT 1,
  `attack` int NOT NULL DEFAULT 1,
  `magical_precision` int NOT NULL DEFAULT 1,
  `attack_speed` int NOT NULL DEFAULT 1,
  `magical_resist` int NOT NULL DEFAULT 1,
  `magical_attack` int NOT NULL DEFAULT 1,
  `main_hand_magical_attack` int NOT NULL DEFAULT 1,
  `off_hand_magical_attack` int NOT NULL DEFAULT 1,
  `physical_critical` int NOT NULL DEFAULT 1,
  `attack_range` int NOT NULL DEFAULT 1,
  `magical_defense` int NOT NULL DEFAULT 1,
  `agility` int NOT NULL DEFAULT 1,
  `knowledge` int NOT NULL DEFAULT 1,
  `will` int NOT NULL DEFAULT 1,
  `magical_boost` int NOT NULL DEFAULT 1,
  `magical_boost_resist` int NOT NULL DEFAULT 1,
  `physical_critical_resist` int NOT NULL DEFAULT 1,
  `magical_critical_resist` int NOT NULL DEFAULT 1,
  PRIMARY KEY (`player_id`) USING BTREE,
  CONSTRAINT `player_game_stats` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for player_ladder
-- ----------------------------
DROP TABLE IF EXISTS `player_ladder`;
CREATE TABLE `player_ladder`  (
  `player_id` int NOT NULL,
  `event_id` int NOT NULL DEFAULT 0,
  `race` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `class` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `kill` int NULL DEFAULT 0,
  `dead` int NULL DEFAULT 0,
  PRIMARY KEY (`player_id`, `event_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for player_life_stats
-- ----------------------------
DROP TABLE IF EXISTS `player_life_stats`;
CREATE TABLE `player_life_stats`  (
  `player_id` int NOT NULL,
  `hp` int NOT NULL DEFAULT 1,
  `mp` int NOT NULL DEFAULT 1,
  `fp` int NOT NULL DEFAULT 1,
  PRIMARY KEY (`player_id`) USING BTREE,
  CONSTRAINT `FK_player_life_stats` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for player_luna_shop
-- ----------------------------
DROP TABLE IF EXISTS `player_luna_shop`;
CREATE TABLE `player_luna_shop`  (
  `player_id` int NOT NULL,
  `free_under` tinyint(1) NOT NULL,
  `free_munition` tinyint(1) NOT NULL,
  `free_chest` tinyint(1) NOT NULL
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for player_macrosses
-- ----------------------------
DROP TABLE IF EXISTS `player_macrosses`;
CREATE TABLE `player_macrosses`  (
  `player_id` int NOT NULL,
  `order` int NOT NULL,
  `macro` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  UNIQUE INDEX `main`(`player_id` ASC, `order` ASC) USING BTREE,
  CONSTRAINT `player_macrosses_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for player_minions
-- ----------------------------
DROP TABLE IF EXISTS `player_minions`;
CREATE TABLE `player_minions`  (
  `player_id` int NOT NULL,
  `minion_id` int NOT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `birthday` timestamp NOT NULL DEFAULT current_timestamp,
  `growth_point` int NULL DEFAULT 0,
  `buff_item` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `is_locked` tinyint NULL DEFAULT 0,
  `expire_time` int NOT NULL DEFAULT 0,
  `despawn_time` timestamp NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Table structure for player_monsterbook
-- ----------------------------
DROP TABLE IF EXISTS `player_monsterbook`;
CREATE TABLE `player_monsterbook`  (
  `player_id` int NOT NULL,
  `id` int NOT NULL,
  `kill_count` int NOT NULL,
  `level` int NOT NULL,
  `claim_reward` int NOT NULL DEFAULT 0,
  PRIMARY KEY (`player_id`, `id`) USING BTREE,
  CONSTRAINT `fk_player_monsterbook` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for player_motions
-- ----------------------------
DROP TABLE IF EXISTS `player_motions`;
CREATE TABLE `player_motions`  (
  `player_id` int NOT NULL,
  `motion_id` int NOT NULL,
  `time` int NOT NULL DEFAULT 0,
  `active` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`player_id`, `motion_id`) USING BTREE,
  CONSTRAINT `motions_player_id_fk` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for player_npc_factions
-- ----------------------------
DROP TABLE IF EXISTS `player_npc_factions`;
CREATE TABLE `player_npc_factions`  (
  `player_id` int NOT NULL,
  `faction_id` int NOT NULL,
  `active` tinyint(1) NOT NULL,
  `time` int NOT NULL,
  `state` enum('NOTING','START','COMPLETE') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'NOTING',
  `quest_id` int NOT NULL DEFAULT 0,
  PRIMARY KEY (`player_id`, `faction_id`) USING BTREE,
  CONSTRAINT `player_npc_factions_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for player_passkey
-- ----------------------------
DROP TABLE IF EXISTS `player_passkey`;
CREATE TABLE `player_passkey`  (
  `account_id` int NOT NULL,
  `passkey` varchar(65) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`account_id`, `passkey`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for player_passports
-- ----------------------------
DROP TABLE IF EXISTS `player_passports`;
CREATE TABLE `player_passports`  (
  `account_id` int NOT NULL,
  `passport_id` int NOT NULL,
  `stamps` int NOT NULL DEFAULT 0,
  `last_stamp` timestamp NOT NULL DEFAULT current_timestamp ON UPDATE CURRENT_TIMESTAMP,
  `rewarded` tinyint(1) NOT NULL DEFAULT 0,
  UNIQUE INDEX `account_passport`(`account_id` ASC, `passport_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for player_pets
-- ----------------------------
DROP TABLE IF EXISTS `player_pets`;
CREATE TABLE `player_pets`  (
  `player_id` int NOT NULL,
  `pet_id` int NOT NULL,
  `decoration` int NOT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `hungry_level` tinyint NOT NULL DEFAULT 0,
  `feed_progress` int NOT NULL DEFAULT 0,
  `reuse_time` bigint NOT NULL DEFAULT 0,
  `birthday` timestamp NOT NULL DEFAULT current_timestamp,
  `mood_started` bigint NOT NULL DEFAULT 0,
  `counter` int NOT NULL DEFAULT 0,
  `mood_cd_started` bigint NOT NULL DEFAULT 0,
  `gift_cd_started` bigint NOT NULL DEFAULT 0,
  `dopings` varchar(80) CHARACTER SET ascii COLLATE ascii_general_ci NULL DEFAULT NULL,
  `despawn_time` timestamp NULL DEFAULT NULL,
  `expire_time` int NOT NULL,
  PRIMARY KEY (`player_id`, `pet_id`) USING BTREE,
  CONSTRAINT `FK_player_pets` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for player_punishments
-- ----------------------------
DROP TABLE IF EXISTS `player_punishments`;
CREATE TABLE `player_punishments`  (
  `player_id` int NOT NULL,
  `punishment_type` enum('PRISON','GATHER','CHARBAN') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `start_time` int UNSIGNED NULL DEFAULT 0,
  `duration` int UNSIGNED NULL DEFAULT 0,
  `reason` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  PRIMARY KEY (`player_id`, `punishment_type`) USING BTREE,
  CONSTRAINT `player_punishments_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for player_quests
-- ----------------------------
DROP TABLE IF EXISTS `player_quests`;
CREATE TABLE `player_quests`  (
  `player_id` int NOT NULL,
  `quest_id` int UNSIGNED NOT NULL DEFAULT 0,
  `status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'NONE',
  `quest_vars` int UNSIGNED NOT NULL DEFAULT 0,
  `complete_count` int UNSIGNED NOT NULL DEFAULT 0,
  `complete_time` timestamp NULL DEFAULT NULL,
  `next_repeat_time` timestamp NULL DEFAULT NULL,
  `reward` smallint NULL DEFAULT NULL,
  PRIMARY KEY (`player_id`, `quest_id`) USING BTREE,
  CONSTRAINT `player_quests_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for player_recipes
-- ----------------------------
DROP TABLE IF EXISTS `player_recipes`;
CREATE TABLE `player_recipes`  (
  `player_id` int NOT NULL,
  `recipe_id` int NOT NULL,
  PRIMARY KEY (`player_id`, `recipe_id`) USING BTREE,
  CONSTRAINT `player_recipes_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for player_registered_items
-- ----------------------------
DROP TABLE IF EXISTS `player_registered_items`;
CREATE TABLE `player_registered_items`  (
  `player_id` int NOT NULL,
  `item_unique_id` int NOT NULL,
  `item_id` int NOT NULL,
  `expire_time` int NULL DEFAULT NULL,
  `color` int NULL DEFAULT NULL,
  `color_expires` int NOT NULL DEFAULT 0,
  `owner_use_count` int NOT NULL DEFAULT 0,
  `visitor_use_count` int NOT NULL DEFAULT 0,
  `x` float NOT NULL DEFAULT 0,
  `y` float NOT NULL DEFAULT 0,
  `z` float NOT NULL DEFAULT 0,
  `h` smallint NULL DEFAULT NULL,
  `area` enum('NONE','INTERIOR','EXTERIOR','ALL','DECOR') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'NONE',
  `floor` tinyint NOT NULL DEFAULT 0,
  PRIMARY KEY (`player_id`, `item_unique_id`, `item_id`) USING BTREE,
  CONSTRAINT `player_registered_items_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for player_settings
-- ----------------------------
DROP TABLE IF EXISTS `player_settings`;
CREATE TABLE `player_settings`  (
  `player_id` int NOT NULL,
  `settings_type` tinyint(1) NOT NULL,
  `settings` blob NOT NULL,
  PRIMARY KEY (`player_id`, `settings_type`) USING BTREE,
  CONSTRAINT `ps_pl_fk` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for player_shugo_sweep
-- ----------------------------
DROP TABLE IF EXISTS `player_shugo_sweep`;
CREATE TABLE `player_shugo_sweep`  (
  `player_id` int NOT NULL,
  `free_dice` int NOT NULL DEFAULT 0,
  `sweep_step` int NOT NULL DEFAULT 0,
  `board_id` int NOT NULL DEFAULT 0,
  PRIMARY KEY (`player_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for player_skill_skins
-- ----------------------------
DROP TABLE IF EXISTS `player_skill_skins`;
CREATE TABLE `player_skill_skins`  (
  `player_id` int NULL DEFAULT 0,
  `skin_id` int NULL DEFAULT 0,
  `remaining` bigint NULL DEFAULT 0,
  `active` int NULL DEFAULT 0
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for player_skills
-- ----------------------------
DROP TABLE IF EXISTS `player_skills`;
CREATE TABLE `player_skills`  (
  `player_id` int NOT NULL,
  `skill_id` int NOT NULL,
  `skill_level` int NOT NULL DEFAULT 1,
  `skin_id` int NULL DEFAULT 0,
  `skin_active_date` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `skin_expire_time` int NULL DEFAULT NULL,
  `skin_activated` bigint NOT NULL DEFAULT 0,
  PRIMARY KEY (`player_id`, `skill_id`) USING BTREE,
  CONSTRAINT `player_skills_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for player_stigmas_equipped
-- ----------------------------
DROP TABLE IF EXISTS `player_stigmas_equipped`;
CREATE TABLE `player_stigmas_equipped`  (
  `player_id` int NOT NULL,
  `item_id` int NOT NULL,
  `item_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`player_id`, `item_id`) USING BTREE,
  CONSTRAINT `player_stigmas_equipped_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for player_thieves
-- ----------------------------
DROP TABLE IF EXISTS `player_thieves`;
CREATE TABLE `player_thieves`  (
  `player_id` int NOT NULL,
  `rank` int NOT NULL DEFAULT 0,
  `thieves_count` int NOT NULL DEFAULT 0,
  `prison_count` int NOT NULL DEFAULT 0,
  `last_kinah` bigint NOT NULL DEFAULT 0,
  `revenge_name` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'Нет',
  `revenge_count` int NOT NULL DEFAULT 0,
  `revenge_date` timestamp NOT NULL DEFAULT '2016-07-30 00:00:00',
  PRIMARY KEY (`player_id`) USING BTREE,
  UNIQUE INDEX `unique_name`(`player_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for player_titles
-- ----------------------------
DROP TABLE IF EXISTS `player_titles`;
CREATE TABLE `player_titles`  (
  `player_id` int NOT NULL,
  `title_id` int NOT NULL,
  `remaining` int NOT NULL DEFAULT 0,
  PRIMARY KEY (`player_id`, `title_id`) USING BTREE,
  CONSTRAINT `player_titles_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for player_transform
-- ----------------------------
DROP TABLE IF EXISTS `player_transform`;
CREATE TABLE `player_transform`  (
  `player_id` int NOT NULL,
  `panel_id` int NOT NULL DEFAULT 0,
  `item_id` int NOT NULL DEFAULT 0
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for player_upgrade_arcade
-- ----------------------------
DROP TABLE IF EXISTS `player_upgrade_arcade`;
CREATE TABLE `player_upgrade_arcade`  (
  `player_id` int NOT NULL,
  `frenzy_meter` int NOT NULL,
  `upgrade_lvl` int NOT NULL,
  PRIMARY KEY (`player_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for player_vars
-- ----------------------------
DROP TABLE IF EXISTS `player_vars`;
CREATE TABLE `player_vars`  (
  `player_id` int NOT NULL,
  `param` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `value` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `time` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`player_id`, `param`) USING BTREE,
  CONSTRAINT `player_vars_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for player_wardrobe
-- ----------------------------
DROP TABLE IF EXISTS `player_wardrobe`;
CREATE TABLE `player_wardrobe`  (
  `player_id` int NOT NULL,
  `item_id` int NOT NULL,
  `slot` int NOT NULL,
  `reskin_count` int NOT NULL DEFAULT 0,
  PRIMARY KEY (`player_id`, `item_id`) USING BTREE,
  CONSTRAINT `player_wardrobe_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for player_world_bans
-- ----------------------------
DROP TABLE IF EXISTS `player_world_bans`;
CREATE TABLE `player_world_bans`  (
  `player` int NOT NULL,
  `by` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `duration` bigint NOT NULL,
  `date` bigint NOT NULL,
  `reason` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`player`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for players
-- ----------------------------
DROP TABLE IF EXISTS `players`;
CREATE TABLE `players`  (
  `id` int NOT NULL,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `account_id` int NOT NULL,
  `account_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `exp` bigint NOT NULL DEFAULT 0,
  `recoverexp` bigint NOT NULL DEFAULT 0,
  `x` float NOT NULL,
  `y` float NOT NULL,
  `z` float NOT NULL,
  `heading` int NOT NULL,
  `world_id` int NOT NULL,
  `gender` enum('MALE','FEMALE') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `race` enum('ASMODIANS','ELYOS') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `player_class` enum('WARRIOR','GLADIATOR','TEMPLAR','SCOUT','ASSASSIN','RANGER','MAGE','SORCERER','SPIRIT_MASTER','PRIEST','CLERIC','CHANTER','TECHNIST','GUNSLINGER','AETHERTECH','MUSE','SONGWEAVER') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `creation_date` timestamp NULL DEFAULT NULL,
  `deletion_date` timestamp NULL DEFAULT NULL,
  `last_online` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `quest_expands` tinyint(1) NOT NULL DEFAULT 0,
  `advenced_stigma_slot_size` tinyint(1) NOT NULL DEFAULT 0,
  `warehouse_size` tinyint(1) NOT NULL DEFAULT 0,
  `mailbox_letters` tinyint NOT NULL DEFAULT 0,
  `bind_point` int NOT NULL DEFAULT 0,
  `title_id` int NOT NULL DEFAULT -1,
  `bonus_title_id` int NOT NULL,
  `online` tinyint(1) NOT NULL DEFAULT 0,
  `note` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `npc_expands` tinyint(1) NOT NULL DEFAULT 0,
  `world_owner` int NOT NULL DEFAULT 0,
  `dp` int NOT NULL DEFAULT 0,
  `soul_sickness` tinyint(1) UNSIGNED NOT NULL DEFAULT 0,
  `reposte_energy` bigint NOT NULL DEFAULT 0,
  `mentor_flag_time` int NOT NULL DEFAULT 0,
  `last_transfer_time` decimal(20, 0) NOT NULL DEFAULT 0,
  `stamps` int NOT NULL DEFAULT 0,
  `last_stamp` timestamp NOT NULL DEFAULT '2015-01-01 12:00:00',
  `rewarded_pass` int NOT NULL DEFAULT 0,
  `passport_time` bigint NOT NULL DEFAULT 0,
  `is_archdaeva` tinyint(1) NOT NULL,
  `creativity_point` int NOT NULL DEFAULT 0,
  `aura_of_growth` bigint NOT NULL DEFAULT 0,
  `join_legion_id` int NOT NULL DEFAULT 0,
  `join_state` enum('NONE','DENIED','ACCEPTED') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'NONE',
  `berdin_star` bigint NOT NULL DEFAULT 0,
  `luna_consume` int NOT NULL DEFAULT 0,
  `muni_keys` int NOT NULL DEFAULT 0,
  `luna_consume_count` int NOT NULL DEFAULT 0,
  `wardrobe_slot` int NOT NULL DEFAULT 2,
  `abyss_favor` bigint NULL DEFAULT 0,
  `luna_points` int NULL DEFAULT 0,
  `frenzy_points` int NULL DEFAULT 0,
  `frenzy_count` int NULL DEFAULT 0,
  `toc_floor` int NULL DEFAULT 0,
  `stone_cp` int NOT NULL DEFAULT 0,
  `golden_dice` int NOT NULL DEFAULT 0,
  `sweep_reset` int NOT NULL DEFAULT 0,
  `minion_point` int NULL DEFAULT 0,
  `minion_function_time` timestamp NULL DEFAULT NULL,
  `minion_auto_renew_function` tinyint(1) NULL DEFAULT 0,
  `minion_auto_charge` tinyint(1) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name_unique`(`name` ASC) USING BTREE,
  INDEX `account_id`(`account_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for portal_cooldowns
-- ----------------------------
DROP TABLE IF EXISTS `portal_cooldowns`;
CREATE TABLE `portal_cooldowns`  (
  `player_id` int NOT NULL,
  `world_id` int NOT NULL,
  `reuse_time` bigint NOT NULL,
  `entry_count` int NOT NULL,
  PRIMARY KEY (`player_id`, `world_id`) USING BTREE,
  CONSTRAINT `portal_cooldowns_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for realms
-- ----------------------------
DROP TABLE IF EXISTS `realms`;
CREATE TABLE `realms`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(32) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `sqlhost` varchar(32) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `sqluser` varchar(32) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `sqlpass` varchar(32) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `chardb` varchar(32) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 3 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for server_variables
-- ----------------------------
DROP TABLE IF EXISTS `server_variables`;
CREATE TABLE `server_variables`  (
  `key` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `value` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`key`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for siege_locations
-- ----------------------------
DROP TABLE IF EXISTS `siege_locations`;
CREATE TABLE `siege_locations`  (
  `id` int NOT NULL,
  `race` enum('ELYOS','ASMODIANS','BALAUR') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `legion_id` int NOT NULL,
  `occupy_count` int NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for skill_motions
-- ----------------------------
DROP TABLE IF EXISTS `skill_motions`;
CREATE TABLE `skill_motions`  (
  `motion_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `skill_id` int NOT NULL,
  `attack_speed` int NOT NULL,
  `weapon_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `off_weapon_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `race` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `gender` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `time` int NOT NULL DEFAULT 0,
  PRIMARY KEY (`motion_name`, `skill_id`, `attack_speed`, `weapon_type`, `off_weapon_type`, `gender`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for special_landing
-- ----------------------------
DROP TABLE IF EXISTS `special_landing`;
CREATE TABLE `special_landing`  (
  `id` int NOT NULL,
  `type` enum('ACTIVE','NO_ACTIVE') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for surveys
-- ----------------------------
DROP TABLE IF EXISTS `surveys`;
CREATE TABLE `surveys`  (
  `unique_id` int NOT NULL AUTO_INCREMENT,
  `owner_id` int NOT NULL,
  `item_id` int NOT NULL,
  `item_count` decimal(20, 0) NOT NULL DEFAULT 1,
  `html_text` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `html_radio` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'accept',
  `used` tinyint(1) NOT NULL DEFAULT 0,
  `used_time` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  PRIMARY KEY (`unique_id`) USING BTREE,
  INDEX `owner_id`(`owner_id` ASC) USING BTREE,
  CONSTRAINT `surveys_ibfk_1` FOREIGN KEY (`owner_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tasks
-- ----------------------------
DROP TABLE IF EXISTS `tasks`;
CREATE TABLE `tasks`  (
  `id` int NOT NULL,
  `task_type` enum('SHUTDOWN','RESTART') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `trigger_type` enum('FIXED_IN_TIME') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `trigger_param` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `exec_param` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for threes_upgrade
-- ----------------------------
DROP TABLE IF EXISTS `threes_upgrade`;
CREATE TABLE `threes_upgrade`  (
  `id` int NOT NULL,
  `exp` int NOT NULL DEFAULT 0,
  `level` int NOT NULL DEFAULT 1,
  `threeId` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for towns
-- ----------------------------
DROP TABLE IF EXISTS `towns`;
CREATE TABLE `towns`  (
  `id` int NOT NULL,
  `level` int NOT NULL DEFAULT 1,
  `points` int NOT NULL DEFAULT 0,
  `race` enum('ELYOS','ASMODIANS') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `level_up_date` timestamp NOT NULL DEFAULT '2013-01-01 14:00:00',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for transaction_buy_history
-- ----------------------------
DROP TABLE IF EXISTS `transaction_buy_history`;
CREATE TABLE `transaction_buy_history`  (
  `id` int NOT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `price_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `value` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `date` datetime NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Table structure for transaction_convert_history
-- ----------------------------
DROP TABLE IF EXISTS `transaction_convert_history`;
CREATE TABLE `transaction_convert_history`  (
  `account_id` int NOT NULL,
  `player_id` int NULL DEFAULT NULL,
  `player_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `price_id` bigint NULL DEFAULT NULL,
  `from_val` bigint NULL DEFAULT NULL,
  `value` bigint NULL DEFAULT NULL,
  `date` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'NONE'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `account` varchar(30) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `ip` varchar(30) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `date` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 10 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for veteran_rewards
-- ----------------------------
DROP TABLE IF EXISTS `veteran_rewards`;
CREATE TABLE `veteran_rewards`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `player` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `type` int NOT NULL,
  `item` int NOT NULL,
  `count` int NOT NULL,
  `kinah` int NOT NULL,
  `sender` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `message` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for weddings
-- ----------------------------
DROP TABLE IF EXISTS `weddings`;
CREATE TABLE `weddings`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT,
  `player1` int NOT NULL,
  `player2` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `player1`(`player1` ASC) USING BTREE,
  INDEX `player2`(`player2` ASC) USING BTREE,
  CONSTRAINT `weddings_ibfk_1` FOREIGN KEY (`player1`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `weddings_ibfk_2` FOREIGN KEY (`player2`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for weddings_log
-- ----------------------------
DROP TABLE IF EXISTS `weddings_log`;
CREATE TABLE `weddings_log`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT,
  `player_id` int NOT NULL,
  `partner_name` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `wedding_start` timestamp NULL DEFAULT NULL,
  `wedding_end` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
