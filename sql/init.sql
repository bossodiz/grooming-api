SET NAMES utf8mb4;
SET time_zone = '+07:00';

CREATE TABLE IF NOT EXISTS `role` (
                                      `id` bigint NOT NULL AUTO_INCREMENT,
                                      `name` varchar(100) NOT NULL,
    `description` varchar(500) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `role_UN` (`name`)
    );

CREATE TABLE IF NOT EXISTS `user_profile` (
                                              `id` bigint NOT NULL AUTO_INCREMENT,
                                              `username` varchar(100) NOT NULL,
    `password` varchar(100) NOT NULL,
    `role` bigint DEFAULT NULL,
    `firstname` varchar(500) DEFAULT NULL,
    `lastname` varchar(500) DEFAULT NULL,
    `email` varchar(500) DEFAULT NULL,
    `phone1` varchar(100) DEFAULT NULL,
    `phone2` varchar(100) DEFAULT NULL,
    `created_date` timestamp NOT NULL,
    `nickname` varchar(100) DEFAULT NULL,
    `visit` int DEFAULT NULL,
    `lasted_date` timestamp NULL DEFAULT NULL,
    `remark` varchar(1000) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `user_profile_UN` (`username`),
    KEY `fk_user_profile_role` (`role`),
    CONSTRAINT `fk_user_profile_role` FOREIGN KEY (`role`) REFERENCES `role` (`id`) ON DELETE SET NULL
    );

CREATE TABLE IF NOT EXISTS `grooming_reserve` (
                                                  `id` bigint NOT NULL AUTO_INCREMENT,
                                                  `pet_id` bigint DEFAULT NULL,
                                                  `pet_name` varchar(100)  DEFAULT NULL,
    `phone` varchar(100) DEFAULT NULL,
    `pet_type_id` bigint DEFAULT NULL,
    `pet_breed_id` bigint DEFAULT NULL,
    `reserve_date_start` timestamp NULL DEFAULT NULL,
    `reserve_date_end` timestamp NULL DEFAULT NULL,
    `created_date` timestamp NULL DEFAULT NULL,
    `color` varchar(100) DEFAULT NULL,
    `note` varchar(500) DEFAULT NULL,
    PRIMARY KEY (`id`)
    );


CREATE TABLE IF NOT EXISTS `invoice` (
                                         `id` bigint NOT NULL AUTO_INCREMENT,
                                         `invoice_no` varchar(18)  NOT NULL COMMENT 'YYYYMMDDHHmmss(userid4digit)',
    `customer_id` bigint DEFAULT NULL COMMENT 'user_id',
    `price` decimal(10,0) DEFAULT NULL,
    `payment_type` varchar(100) DEFAULT NULL,
    `payment_status` tinyint(1) DEFAULT NULL,
    `payment_date` datetime DEFAULT NULL,
    `remark` varchar(1000) DEFAULT NULL,
    `created_date` timestamp NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `invoice_UN` (`invoice_no`),
    KEY `invoice_FK` (`customer_id`),
    CONSTRAINT `invoice_FK` FOREIGN KEY (`customer_id`) REFERENCES `user_profile` (`id`) ON DELETE SET NULL
    );


CREATE TABLE IF NOT EXISTS `invoice_session` (
                                                 `id` bigint NOT NULL AUTO_INCREMENT,
                                                 `invoice_no` varchar(18)  NOT NULL,
    `cart_hash` varchar(128) DEFAULT NULL,
    `payload_json` json DEFAULT NULL,
    `total_before` decimal(18,2) DEFAULT NULL,
    `total_discount` decimal(18,2) DEFAULT NULL,
    `total_after` decimal(18,2) DEFAULT NULL,
    `status` varchar(16) DEFAULT NULL,
    `created_at` datetime DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `calculation_id` (`invoice_no`)
    );


CREATE TABLE IF NOT EXISTS `item` (
                                      `id` bigint NOT NULL AUTO_INCREMENT,
                                      `name` varchar(1000) NOT NULL,
    `description` varchar(1000) DEFAULT NULL,
    `price` decimal(10,0) DEFAULT NULL,
    `stock` int DEFAULT NULL,
    `barcode` varchar(100) DEFAULT NULL,
    `remark` varchar(1000)  DEFAULT NULL,
    `created_date` varchar(100) DEFAULT NULL,
    `item_type` varchar(100) DEFAULT NULL,
    `item_category` varchar(100) DEFAULT NULL,
    PRIMARY KEY (`id`)
    );


CREATE TABLE IF NOT EXISTS `tag` (
                                     `id` bigint NOT NULL AUTO_INCREMENT,
                                     `name` varchar(100)  NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `tag_unique` (`name`)
    );



CREATE TABLE IF NOT EXISTS `item_tag` (
                                          `id` bigint NOT NULL AUTO_INCREMENT,
                                          `item_id` bigint NOT NULL,
                                          `tag_id` bigint NOT NULL,
                                          PRIMARY KEY (`id`),
    UNIQUE KEY `item_tag_unique` (`tag_id`,`item_id`),
    KEY `item_tag_item_FK` (`item_id`),
    CONSTRAINT `item_tag_item_FK` FOREIGN KEY (`item_id`) REFERENCES `item` (`id`),
    CONSTRAINT `item_tag_tag_FK` FOREIGN KEY (`tag_id`) REFERENCES `tag` (`id`)
    );


CREATE TABLE IF NOT EXISTS `promotion` (
                                           `id` bigint NOT NULL AUTO_INCREMENT,
                                           `name` varchar(500) DEFAULT NULL,
    `discount_category` varchar(100) DEFAULT NULL,
    `amount_type` varchar(100)  DEFAULT NULL,
    `amount` decimal(10,2) DEFAULT NULL,
    `period_type` varchar(100) DEFAULT NULL,
    `start_date` datetime DEFAULT NULL,
    `end_date` datetime DEFAULT NULL,
    `specific_days` varchar(100) DEFAULT NULL,
    `customer_only` tinyint(1) DEFAULT NULL,
    `status` tinyint(1) DEFAULT NULL,
    `quota` int DEFAULT NULL,
    `created_at` datetime DEFAULT NULL,
    `updated_at` datetime DEFAULT NULL,
    `discount_type` varchar(100) DEFAULT NULL,
    `condition_value` varchar(100)  DEFAULT NULL,
    PRIMARY KEY (`id`)
    );


CREATE TABLE IF NOT EXISTS `pet_type` (
                                          `id` bigint NOT NULL AUTO_INCREMENT,
                                          `name` varchar(100) NOT NULL,
    `name_th` varchar(100) DEFAULT NULL,
    `name_en` varchar(100) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `pet_type_UN` (`name`)
    );


CREATE TABLE IF NOT EXISTS `pet_breed` (
                                           `id` bigint NOT NULL AUTO_INCREMENT,
                                           `name` varchar(100) NOT NULL,
    `name_th` varchar(100) DEFAULT NULL,
    `name_en` varchar(100) DEFAULT NULL,
    `pet_type_id` bigint DEFAULT NULL,
    `image` varchar(500) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `pet_breed_UN` (`name`),
    KEY `pet_breed_FK` (`pet_type_id`),
    CONSTRAINT `pet_breed_FK` FOREIGN KEY (`pet_type_id`) REFERENCES `pet_type` (`id`) ON DELETE SET NULL
    );


CREATE TABLE IF NOT EXISTS `pet` (
                                     `id` bigint NOT NULL AUTO_INCREMENT,
                                     `name` varchar(100) NOT NULL,
    `age_year` int DEFAULT NULL,
    `gender` varchar(100) DEFAULT NULL,
    `pet_breed_id` bigint DEFAULT NULL,
    `weight` decimal(10,2) DEFAULT NULL,
    `user_id` bigint DEFAULT NULL,
    `created_date` timestamp NULL DEFAULT NULL,
    `service_count` int DEFAULT NULL,
    `age_month` int DEFAULT NULL,
    `last_update_year` timestamp NULL DEFAULT NULL,
    `pet_type_id` bigint DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `pet_FK` (`user_id`),
    KEY `pet_FK_1` (`pet_breed_id`),
    KEY `pet_fk_type` (`pet_type_id`),
    CONSTRAINT `pet_FK` FOREIGN KEY (`user_id`) REFERENCES `user_profile` (`id`) ON DELETE SET NULL,
    CONSTRAINT `pet_FK_1` FOREIGN KEY (`pet_breed_id`) REFERENCES `pet_breed` (`id`) ON DELETE SET NULL,
    CONSTRAINT `pet_fk_type` FOREIGN KEY (`pet_type_id`) REFERENCES `pet_type` (`id`)
    );



CREATE TABLE IF NOT EXISTS `order` (
                                       `id` bigint NOT NULL AUTO_INCREMENT,
                                       `invoice_id` bigint DEFAULT NULL,
                                       `customer_id` bigint DEFAULT NULL,
                                       `pet_id` bigint DEFAULT NULL,
                                       `item_id` bigint DEFAULT NULL,
                                       `promotion_id` bigint DEFAULT NULL,
                                       `created_date` timestamp NOT NULL,
                                       PRIMARY KEY (`id`),
    KEY `order_FK` (`customer_id`),
    KEY `order_FK_2` (`invoice_id`),
    KEY `order_FK_3` (`item_id`),
    KEY `order_FK_4` (`promotion_id`),
    KEY `order_FK_5` (`pet_id`),
    CONSTRAINT `order_FK` FOREIGN KEY (`customer_id`) REFERENCES `user_profile` (`id`) ON DELETE SET NULL,
    CONSTRAINT `order_FK_2` FOREIGN KEY (`invoice_id`) REFERENCES `invoice` (`id`) ON DELETE SET NULL,
    CONSTRAINT `order_FK_4` FOREIGN KEY (`promotion_id`) REFERENCES `promotion` (`id`) ON DELETE SET NULL,
    CONSTRAINT `order_FK_5` FOREIGN KEY (`pet_id`) REFERENCES `pet` (`id`) ON DELETE SET NULL
    );


CREATE TABLE IF NOT EXISTS `promotion_excluded_item` (
                                                         `id` bigint NOT NULL AUTO_INCREMENT,
                                                         `promotion_id` bigint NOT NULL,
                                                         `item_id` bigint NOT NULL,
                                                         `created_at` datetime NOT NULL,
                                                         PRIMARY KEY (`id`),
    KEY `promotion_excluded_item_promotion_FK` (`promotion_id`),
    KEY `promotion_excluded_item_item_FK` (`item_id`),
    CONSTRAINT `promotion_excluded_item_item_FK` FOREIGN KEY (`item_id`) REFERENCES `item` (`id`),
    CONSTRAINT `promotion_excluded_item_promotion_FK` FOREIGN KEY (`promotion_id`) REFERENCES `promotion` (`id`)
    );


CREATE TABLE IF NOT EXISTS `promotion_free_gift_item` (
                                                          `id` bigint NOT NULL AUTO_INCREMENT,
                                                          `promotion_id` bigint NOT NULL,
                                                          `buy_item_id` bigint NOT NULL,
                                                          `free_item_id` bigint NOT NULL,
                                                          `created_at` datetime DEFAULT NULL,
                                                          PRIMARY KEY (`id`),
    KEY `promotion_id` (`promotion_id`),
    CONSTRAINT `promotion_free_gift_item_ibfk_1` FOREIGN KEY (`promotion_id`) REFERENCES `promotion` (`id`)
    );


CREATE TABLE IF NOT EXISTS `promotion_included_item` (
                                                         `id` bigint NOT NULL AUTO_INCREMENT,
                                                         `promotion_id` bigint NOT NULL,
                                                         `item_id` bigint NOT NULL,
                                                         `created_at` datetime NOT NULL,
                                                         PRIMARY KEY (`id`),
    KEY `promotion_included_item_promotion_FK` (`promotion_id`),
    KEY `promotion_included_item_item_FK` (`item_id`),
    CONSTRAINT `promotion_included_item_item_FK` FOREIGN KEY (`item_id`) REFERENCES `item` (`id`),
    CONSTRAINT `promotion_included_item_promotion_FK` FOREIGN KEY (`promotion_id`) REFERENCES `promotion` (`id`)
    );
