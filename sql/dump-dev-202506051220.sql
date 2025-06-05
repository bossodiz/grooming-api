-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: localhost    Database: dev
-- ------------------------------------------------------
-- Server version	8.0.41

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `grooming_reserve`
--

DROP TABLE IF EXISTS `grooming_reserve`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `grooming_reserve` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `pet_id` bigint DEFAULT NULL,
  `pet_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `phone` varchar(100) DEFAULT NULL,
  `pet_type_id` bigint DEFAULT NULL,
  `pet_breed_id` bigint DEFAULT NULL,
  `reserve_date_start` timestamp NULL DEFAULT NULL,
  `reserve_date_end` timestamp NULL DEFAULT NULL,
  `grooming_service_id` bigint DEFAULT NULL,
  `created_date` timestamp NULL DEFAULT NULL,
  `color` varchar(100) DEFAULT NULL,
  `note` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `grooming_reserve`
--

LOCK TABLES `grooming_reserve` WRITE;
/*!40000 ALTER TABLE `grooming_reserve` DISABLE KEYS */;
INSERT INTO `grooming_reserve` VALUES (7,3,'ข้าวสวย','1231232222',2,8,'2025-04-25 12:30:00','2025-04-25 14:15:00',4,'2025-04-25 09:14:08','event-pastel-4',NULL),(8,1,NULL,'1231232222',2,11,'2025-04-25 09:00:00','2025-04-25 10:00:00',2,'2025-04-25 09:58:32','event-pastel-10',NULL),(9,5,NULL,'3156468521',2,8,'2025-04-25 10:00:00','2025-04-25 11:30:00',2,'2025-04-25 09:58:41','event-pastel-1',NULL),(10,4,NULL,'3156468521',2,10,'2025-04-25 14:30:00','2025-04-25 16:30:00',2,'2025-04-25 10:07:38','event-pastel-10',NULL),(11,5,NULL,'3156468521',2,8,'2025-04-25 12:00:00','2025-04-25 13:30:00',2,'2025-04-25 10:09:44','event-pastel-2',NULL),(12,1,NULL,'1231232222',2,11,'2025-04-25 17:00:00','2025-04-25 18:30:00',6,'2025-04-25 10:10:04','event-pastel-9',NULL),(13,1,NULL,'1231232222',2,11,'2025-04-25 19:00:00','2025-04-25 20:00:00',6,'2025-04-25 10:10:11','event-pastel-4',NULL);
/*!40000 ALTER TABLE `grooming_reserve` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `grooming_service`
--

DROP TABLE IF EXISTS `grooming_service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `grooming_service` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name_th` varchar(100) DEFAULT NULL,
  `name_en` varchar(100) DEFAULT NULL,
  `pet_type_id` bigint DEFAULT NULL,
  `price` decimal(10,0) DEFAULT NULL,
  `remark` varchar(2000) DEFAULT NULL,
  `sequence` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `grooming_service_FK` (`pet_type_id`),
  CONSTRAINT `grooming_service_FK` FOREIGN KEY (`pet_type_id`) REFERENCES `pet_type` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `grooming_service`
--

LOCK TABLES `grooming_service` WRITE;
/*!40000 ALTER TABLE `grooming_service` DISABLE KEYS */;
INSERT INTO `grooming_service` VALUES (1,'อาบน้ำ ตัดขน (1-5KG)','Bath & Groom',1,500,'',2),(2,'อาบน้ำ ตัดขน (1-5KG)','Bath & Groom',2,600,'test',7),(3,'อาบน้ำ (1-5KG)','Bath',1,250,'',1),(4,'อาบน้ำ (1-5KG)','Bath',2,350,'test',1),(5,'ทำความสะอาดหู','Ear Cleaning',1,80,'',3),(6,'ทำความสะอาดหู','Ear Cleaning',2,90,'1123',6),(7,'ไถท้อง ไถอุ้งเท้า','Belly & Paw Shaving',1,150,'',4),(8,'ไถท้อง ไถอุ้งเท้า','Belly & Paw Shaving',2,180,'412313',5),(9,'ตัดขน',NULL,1,350,'test',5),(10,'อาบน้ำ (1-5KG)','Bath',1,250,'test',6),(11,'อาบน้ำ ตัดขน (1-5KG)','Bath & Groom',1,500,'',7),(12,'ตัดขน',NULL,1,350,'test',8),(13,'ไถท้อง ไถอุ้งเท้า','Belly & Paw Shaving',1,150,'123',9),(14,'ไถท้อง ไถอุ้งเท้า','Belly & Paw Shaving',1,150,'1234',10);
/*!40000 ALTER TABLE `grooming_service` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `image`
--

DROP TABLE IF EXISTS `image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `image` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `pet_id` bigint DEFAULT NULL,
  `product_id` bigint DEFAULT NULL,
  `promotion_id` bigint DEFAULT NULL,
  `invoice_id` bigint DEFAULT NULL,
  `image_path` varchar(500) DEFAULT NULL,
  `created_date` timestamp NOT NULL,
  PRIMARY KEY (`id`),
  KEY `image_FK` (`pet_id`),
  KEY `image_FK_1` (`invoice_id`),
  KEY `image_FK_2` (`product_id`),
  KEY `image_FK_3` (`promotion_id`),
  CONSTRAINT `image_FK` FOREIGN KEY (`pet_id`) REFERENCES `pet` (`id`) ON DELETE SET NULL,
  CONSTRAINT `image_FK_1` FOREIGN KEY (`invoice_id`) REFERENCES `invoice` (`id`) ON DELETE SET NULL,
  CONSTRAINT `image_FK_2` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE SET NULL,
  CONSTRAINT `image_FK_3` FOREIGN KEY (`promotion_id`) REFERENCES `promotion` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `image`
--

LOCK TABLES `image` WRITE;
/*!40000 ALTER TABLE `image` DISABLE KEYS */;
/*!40000 ALTER TABLE `image` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoice`
--

DROP TABLE IF EXISTS `invoice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invoice` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `invoice_no` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'YYYYMMDDHHmmss(userid4digit)',
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
  CONSTRAINT `invoice_FK` FOREIGN KEY (`customer_id`) REFERENCES `user` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice`
--

LOCK TABLES `invoice` WRITE;
/*!40000 ALTER TABLE `invoice` DISABLE KEYS */;
/*!40000 ALTER TABLE `invoice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `item_info`
--

DROP TABLE IF EXISTS `item_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `item_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(500) DEFAULT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `category` varchar(100) DEFAULT NULL,
  `price` varchar(100) DEFAULT NULL,
  `stock` varchar(100) DEFAULT NULL,
  `barcode` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item_info`
--

LOCK TABLES `item_info` WRITE;
/*!40000 ALTER TABLE `item_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `item_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `item_tag`
--

DROP TABLE IF EXISTS `item_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `item_tag` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `item_info_id` bigint NOT NULL,
  `tag_name` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `item_tag_UN` (`item_info_id`,`tag_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item_tag`
--

LOCK TABLES `item_tag` WRITE;
/*!40000 ALTER TABLE `item_tag` DISABLE KEYS */;
/*!40000 ALTER TABLE `item_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order`
--

DROP TABLE IF EXISTS `order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `invoice_id` bigint DEFAULT NULL,
  `customer_id` bigint DEFAULT NULL,
  `order_type` varchar(100) DEFAULT NULL,
  `grooming_service_id` bigint DEFAULT NULL,
  `pet_id` bigint DEFAULT NULL,
  `product_id` bigint DEFAULT NULL,
  `promotion_id` bigint DEFAULT NULL,
  `created_date` timestamp NOT NULL,
  PRIMARY KEY (`id`),
  KEY `order_FK` (`customer_id`),
  KEY `order_FK_1` (`grooming_service_id`),
  KEY `order_FK_2` (`invoice_id`),
  KEY `order_FK_3` (`product_id`),
  KEY `order_FK_4` (`promotion_id`),
  KEY `order_FK_5` (`pet_id`),
  CONSTRAINT `order_FK` FOREIGN KEY (`customer_id`) REFERENCES `user` (`id`) ON DELETE SET NULL,
  CONSTRAINT `order_FK_1` FOREIGN KEY (`grooming_service_id`) REFERENCES `grooming_service` (`id`) ON DELETE SET NULL,
  CONSTRAINT `order_FK_2` FOREIGN KEY (`invoice_id`) REFERENCES `invoice` (`id`) ON DELETE SET NULL,
  CONSTRAINT `order_FK_3` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE SET NULL,
  CONSTRAINT `order_FK_4` FOREIGN KEY (`promotion_id`) REFERENCES `promotion` (`id`) ON DELETE SET NULL,
  CONSTRAINT `order_FK_5` FOREIGN KEY (`pet_id`) REFERENCES `pet` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order`
--

LOCK TABLES `order` WRITE;
/*!40000 ALTER TABLE `order` DISABLE KEYS */;
/*!40000 ALTER TABLE `order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pet`
--

DROP TABLE IF EXISTS `pet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pet` (
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
  CONSTRAINT `pet_FK` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE SET NULL,
  CONSTRAINT `pet_FK_1` FOREIGN KEY (`pet_breed_id`) REFERENCES `pet_breed` (`id`) ON DELETE SET NULL,
  CONSTRAINT `pet_fk_type` FOREIGN KEY (`pet_type_id`) REFERENCES `pet_type` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pet`
--

LOCK TABLES `pet` WRITE;
/*!40000 ALTER TABLE `pet` DISABLE KEYS */;
INSERT INTO `pet` VALUES (1,'ไข่ดาว',12,'MALE',11,3.81,4,'2025-04-10 14:57:49',0,10,'2025-04-23 16:05:32',2),(2,'ทองคำ',2,'MALE',10,3.00,5,'2025-04-10 14:57:51',0,0,'2025-04-23 16:08:11',2),(3,'ข้าวสวย',4,'FEMALE',8,5.00,4,'2025-04-10 17:30:28',0,0,'2025-04-23 16:08:31',2),(4,'ทองดี',2,'MALE',10,NULL,7,'2025-04-12 15:19:34',0,0,'2025-04-12 15:19:34',2),(5,'ทองคำ',1,'MALE',8,NULL,7,'2025-04-12 15:22:21',0,0,'2025-04-12 15:22:21',2);
/*!40000 ALTER TABLE `pet` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pet_breed`
--

DROP TABLE IF EXISTS `pet_breed`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pet_breed` (
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
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pet_breed`
--

LOCK TABLES `pet_breed` WRITE;
/*!40000 ALTER TABLE `pet_breed` DISABLE KEYS */;
INSERT INTO `pet_breed` VALUES (1,'ชิวาวา (CHIHUAHUA)','ชิวาวา','CHIHUAHUA',1,'assets/images/breeds/dog/chihuahua.jpg'),(2,'ชิสุห์ (Shih Tzu)','ชิสุห์','Shih Tzu',1,'assets/images/breeds/dog/shih_tzu.jpg'),(3,'ปอมเมอเรเนียน (Pomeranian)','ปอมเมอเรเนียน','Pomeranian',1,'assets/images/breeds/dog/pom.jpg'),(4,'บีเกิ้ล (Beagle)','บีเกิ้ล','Beagle',1,'assets/images/breeds/dog/beagle.jpg'),(5,'ชิบะ (Shiba)','ชิบะ','Shiba',1,'assets/images/breeds/dog/shiba.jpg'),(6,'พุดเดิ้ลทอย (Toy Poodle)','พุดเดิ้ลทอย','Toy Poodle',1,'assets/images/breeds/dog/poodle_toy.jpg'),(7,'เฟรนช์ บูลด็อก (French bulldog)','เฟรนช์ บูลด็อก','French bulldog',1,'assets/images/breeds/dog/french_bulldog.jpg'),(8,'อเมริกันช็อตแฮร์ (American Shorthair)','อเมริกันช็อตแฮร์','American Shorthair',2,'assets/images/breeds/cat/american-shorthair.jpg'),(9,'เปอร์เซีย (Persian)','เปอร์เซีย','Persian',2,'assets/images/breeds/cat/Persian.jpg'),(10,'บริติช ช็อตแฮร์ (British Shorthair)','บริติช ช็อตแฮร์','British Shorthair',2,'assets/images/breeds/cat/British_shorthair.jpg'),(11,'แมวไทย ลายสลิด','แมวไทย ลายสลิด','Mackerel Thai Cat',2,NULL);
/*!40000 ALTER TABLE `pet_breed` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pet_type`
--

DROP TABLE IF EXISTS `pet_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pet_type` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `name_th` varchar(100) DEFAULT NULL,
  `name_en` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `pet_type_UN` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pet_type`
--

LOCK TABLES `pet_type` WRITE;
/*!40000 ALTER TABLE `pet_type` DISABLE KEYS */;
INSERT INTO `pet_type` VALUES (1,'dog','สุนัข','Dog'),(2,'cat','แมว','Cat');
/*!40000 ALTER TABLE `pet_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name_th` varchar(500) DEFAULT NULL,
  `name_en` varchar(500) DEFAULT NULL,
  `description_th` varchar(1000) DEFAULT NULL,
  `description_en` varchar(1000) DEFAULT NULL,
  `price` decimal(10,0) DEFAULT NULL,
  `stock` int DEFAULT NULL,
  `barcode` varchar(1000) DEFAULT NULL,
  `remark` varchar(1000) DEFAULT NULL,
  `created_date` timestamp NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `promotion`
--

DROP TABLE IF EXISTS `promotion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `promotion` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name_th` varchar(100) DEFAULT NULL,
  `name_en` varchar(100) DEFAULT NULL,
  `promotion_type` varchar(100) DEFAULT NULL,
  `grooming_service_id` bigint DEFAULT NULL,
  `product_id` bigint DEFAULT NULL,
  `start_date` datetime DEFAULT NULL,
  `expire_date` datetime DEFAULT NULL,
  `amount` decimal(10,0) DEFAULT NULL,
  `amount_type` varchar(100) DEFAULT NULL,
  `created_date` timestamp NOT NULL,
  PRIMARY KEY (`id`),
  KEY `promotion_FK` (`grooming_service_id`),
  KEY `promotion_FK_1` (`product_id`),
  CONSTRAINT `promotion_FK` FOREIGN KEY (`grooming_service_id`) REFERENCES `grooming_service` (`id`) ON DELETE SET NULL,
  CONSTRAINT `promotion_FK_1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `promotion`
--

LOCK TABLES `promotion` WRITE;
/*!40000 ALTER TABLE `promotion` DISABLE KEYS */;
/*!40000 ALTER TABLE `promotion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `description` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `role_UN` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'ADMIN',NULL),(2,'CUSTOMER',NULL);
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
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
  UNIQUE KEY `user_UN` (`username`),
  KEY `fk_user_role` (`role`),
  CONSTRAINT `fk_user_role` FOREIGN KEY (`role`) REFERENCES `role` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (3,'admin','$2a$10$yB9PTbxTB4x4waX3STGWxujSDGKopfLjWSWj1D2aKzeF2iZfcoK1q',1,'Krittawat','Sornwiriya','bossodiz@gmail.com','0818092589',NULL,'2025-03-26 17:01:48','Boss',NULL,NULL,NULL),(4,'1231232222','',2,'Krittawat','Sornwiriya','asdasdawdasd@ff','1231232222','1231231231','2025-03-30 13:40:35','Boss',NULL,NULL,'asdasdasd'),(5,'1231241241','',2,NULL,NULL,NULL,'1231241241',NULL,'2025-03-30 13:40:53','sdbxzvz',NULL,NULL,NULL),(6,'1231241242','',2,NULL,NULL,NULL,'1231241242',NULL,'2025-03-30 13:54:17','zzzz',NULL,NULL,NULL),(7,'3156468521','',2,NULL,NULL,NULL,'3156468521',NULL,'2025-03-30 13:55:41','xzxvzz',NULL,NULL,NULL),(8,'2352652342','',2,NULL,NULL,NULL,'2352652342',NULL,'2025-03-30 13:56:38','asdasda',NULL,NULL,NULL),(9,'4765189864','',2,NULL,NULL,NULL,'4765189864',NULL,'2025-03-30 13:59:58','fdgdfdffdg',NULL,NULL,NULL),(10,'0818092589','',2,'Krittawat','Sornwiriya',NULL,'0818092589','-','2025-03-31 13:52:05','Boss',0,NULL,NULL),(11,'0882414554','',2,NULL,NULL,NULL,'0882414554',NULL,'2025-03-31 13:57:48','Air',0,NULL,NULL),(12,'0885469985','',2,NULL,NULL,NULL,'0885469985',NULL,'2025-04-06 18:31:29','Air',0,NULL,NULL),(13,'0818092522','',2,NULL,NULL,NULL,'0818092522',NULL,'2025-04-06 18:32:51','zxczxczxc',0,NULL,NULL),(14,'0879965452','',2,'Test','test',NULL,'0879965452',NULL,'2025-04-10 13:18:53','ABC',0,NULL,NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'dev'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-05 12:20:54
