 -- MySQL dump 10.13  Distrib 8.0.46, for Linux (x86_64)
--
-- Host: localhost    Database: critical_smoke_db
-- ------------------------------------------------------
-- Server version	8.0.46

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `cart_items`
--

DROP TABLE IF EXISTS `cart_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart_items` (
  `cart_item_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `game_id` bigint NOT NULL,
  `game_title` varchar(255) DEFAULT NULL,
  `game_price` double DEFAULT NULL,
  `cover_image` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`cart_item_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart_items`
--

LOCK TABLES `cart_items` WRITE;
/*!40000 ALTER TABLE `cart_items` DISABLE KEYS */;
INSERT INTO `cart_items` VALUES (1,1,1,'Punishing Gray Ravens',10,'/uploads/covers/e0908cf3-4f6f-4709-8722-8c26edad8fe6.jpg'),(7,18,2,'Risk of Rain 2 ',183,'/uploads/covers/3e1897e5-51b5-4a30-b880-b198a8f3df6d.jpg');
/*!40000 ALTER TABLE `cart_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `games`
--

DROP TABLE IF EXISTS `games`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `games` (
  `game_id` bigint NOT NULL AUTO_INCREMENT,
  `owner_id` bigint DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `description1` text,
  `description2` text,
  `genre` varchar(255) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `cover_image` varchar(255) DEFAULT NULL,
  `game_logo` varchar(255) DEFAULT NULL,
  `preview_image1` varchar(255) DEFAULT NULL,
  `preview_image2` varchar(255) DEFAULT NULL,
  `preview_image3` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `game_files` varchar(255) DEFAULT NULL,
  `approved` tinyint(1) DEFAULT '0',
  `deleted` tinyint(1) DEFAULT '0',
  `under_review` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`game_id`),
  KEY `fk_game_owner` (`owner_id`),
  CONSTRAINT `fk_game_owner` FOREIGN KEY (`owner_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `games`
--

LOCK TABLES `games` WRITE;
/*!40000 ALTER TABLE `games` DISABLE KEYS */;
INSERT INTO `games` VALUES (1,12,'Punishing Gray Ravens','Punishing: Gray Raven is a fast-paced, post-apocalyptic sci-fi action RPG developed by Kuro Games. Players take on the role of \'Commandant\', leading an elite squad of \'Constructs\' (human minds housed in mechanical bodies) to reclaim a ruined Earth from a biomechanical virus called The Punishing.','If you are asking about the official app store description for the sci-fi action RPG Punishing: Gray Raven, it outlines a post-apocalyptic Earth overrun by the biomechanical virus, The Punishing. Humanity has fled to the space station Babylonia, and you—as Commandant—must lead the Gray Raven squad to reclaim the planet using elite biomechanical soldiers','Action,Horror,Adventure,Casual,Strategy,Shooters,RPG',10.00,'/uploads/covers/e0908cf3-4f6f-4709-8722-8c26edad8fe6.jpg','/uploads/logos/fb75eba1-5542-45eb-ae43-740fc369ae78.jpg','/uploads/previews/2f0f3ae3-7744-45fc-87c2-04b4ea836965.jpg','/uploads/previews/f24895c4-271d-4f75-97f0-b8639e151f31.jpg','/uploads/previews/21b559e0-994c-49ec-95db-16656b94717f.jpg','2026-06-30 00:29:29','movies.csv.zip',1,0,0),(2,12,'Risk of Rain 2 ','Risk of Rain 2 is a fast-paced, third-person roguelike shooter where you crash-land on a chaotic alien planet. Your goal is to survive endless waves of monsters, locate and activate the stage\'s teleporter to fight off a boss, and jump to the next level.','Risk of Rain 2 is a fast-paced, 3D roguelike shooter where you fight through hordes of monsters on an alien planet named Petrichor V. You collect loot to power up, survive against increasingly difficult enemies, and find teleporters to advance or loop the game','Action,Adventure,Strategy,Shooters',183.00,'/uploads/covers/3e1897e5-51b5-4a30-b880-b198a8f3df6d.jpg','/uploads/logos/aaf3f469-ce01-4429-99f1-d10adaeab1ff.jpg','/uploads/previews/03375b39-00a9-4d63-a85a-ce77a1e79937.jpg','/uploads/previews/a2c63eeb-78f7-4ba4-b033-3d581efed4f5.jpg',NULL,'2026-06-30 07:26:24','luV0mPfn.zip',1,0,0),(3,17,'devil may cry 5','Devil May Cry 5 is an action-adventure game released by Capcom. If you are looking for physical discs, they are widely available for PlayStation 4, PlayStation 5, and Xbox One/Xbox Series X','Unlike older games, Devil May Cry 5 does not use a second game disc. The entire base game fits on a single disc for Amazon.com: Devil May Cry - PlayStation 4 and Devil May Cry 5 Special Edition. The Vergil DLC and other content are simply digital downloads or included on the main disc.','Action,Horror,Adventure,Strategy,Shooters',1000.00,'/uploads/covers/c737853e-ec28-44a1-913c-ffd8561b52a9.jpg','/uploads/logos/7db4a496-566d-4978-9a45-7d7dadc73635.jpg','/uploads/previews/bb0ee1f8-f503-4210-ba4c-d69eff5c9d7e.jpg',NULL,NULL,'2026-06-30 07:49:14','archive.zip',1,1,0);
/*!40000 ALTER TABLE `games` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `order_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `game_id` bigint NOT NULL,
  `game_title` varchar(255) DEFAULT NULL,
  `game_price` double DEFAULT NULL,
  `cover_image` varchar(500) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,17,1,'Punishing Gray Ravens',10,'/uploads/covers/e0908cf3-4f6f-4709-8722-8c26edad8fe6.jpg','2026-06-30 01:30:41'),(2,12,1,'Punishing Gray Ravens',10,'/uploads/covers/e0908cf3-4f6f-4709-8722-8c26edad8fe6.jpg','2026-06-30 01:33:54'),(3,12,2,'Risk of Rain 2 edited',183,'/uploads/covers/3e1897e5-51b5-4a30-b880-b198a8f3df6d.jpg','2026-06-30 07:41:20'),(4,17,2,'Risk of Rain 2 ',183,'/uploads/covers/3e1897e5-51b5-4a30-b880-b198a8f3df6d.jpg','2026-06-30 07:47:41');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pending_edits`
--

DROP TABLE IF EXISTS `pending_edits`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pending_edits` (
  `edit_id` bigint NOT NULL AUTO_INCREMENT,
  `game_id` bigint NOT NULL,
  `owner_id` bigint NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `description1` text,
  `description2` text,
  `genre` varchar(255) DEFAULT NULL,
  `price` double DEFAULT NULL,
  `cover_image` varchar(500) DEFAULT NULL,
  `game_logo` varchar(500) DEFAULT NULL,
  `preview_image1` varchar(500) DEFAULT NULL,
  `preview_image2` varchar(500) DEFAULT NULL,
  `preview_image3` varchar(500) DEFAULT NULL,
  `submitted_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`edit_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pending_edits`
--

LOCK TABLES `pending_edits` WRITE;
/*!40000 ALTER TABLE `pending_edits` DISABLE KEYS */;
/*!40000 ALTER TABLE `pending_edits` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  `user_password` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `is_admin` tinyint(1) DEFAULT '0',
  `is_banned` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'password','admin','admin123@gmail.com',1,0),(3,NULL,'test','life0129837@gmail.com',0,0),(4,NULL,'bruhh','adadad@gmail.com',0,0),(5,NULL,'brehtest','awdadwawd@gmail.com',0,0),(6,NULL,'dontputpassword','adad@gmail.com',0,0),(7,NULL,'aaron','testing@gmail.com',0,0),(8,NULL,'hydro','bruhh@gmail.com',0,0),(9,'work','plswork','@gmail.com',0,0),(10,'password','whatintarnationisthishitheremanweewooweewooo','bruh@gmail.com',0,0),(11,'password','aaron','bro@gmail.com',0,0),(12,'user','user','user@gmail.com',0,0),(17,'password','hydroflakes','hydro@gmail.com',0,0),(18,'password','testing','test@gmail.com',0,0);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vouchers`
--

DROP TABLE IF EXISTS `vouchers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vouchers` (
  `voucher_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `code` varchar(50) NOT NULL,
  `discount_percent` int NOT NULL,
  `used` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`voucher_id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vouchers`
--

LOCK TABLES `vouchers` WRITE;
/*!40000 ALTER TABLE `vouchers` DISABLE KEYS */;
INSERT INTO `vouchers` VALUES (1,1,'WELCOME5-2AF0A8',5,0),(2,1,'WELCOME10-D9F3A7',10,0),(3,1,'WELCOME20-852379',20,0),(4,9,'WELCOME5-E77484',5,1),(5,9,'WELCOME10-9236BC',10,0),(6,9,'WELCOME20-8D1784',20,0),(7,12,'WELCOME5-8C409C',5,1),(8,12,'WELCOME10-6BEDCB',10,0),(9,12,'WELCOME20-F7DC47',20,1),(10,1,'WELCOME5-ABCDEF',5,0),(11,1,'WELCOME10-GHIJKL',10,0),(12,1,'WELCOME20-MNOPQR',20,0),(13,17,'WELCOME5-CBC575',5,1),(14,17,'WELCOME10-517BBC',10,1),(15,17,'WELCOME20-EF1ADE',20,1),(16,18,'WELCOME5-BECFC6',5,0),(17,18,'WELCOME10-CE36ED',10,0),(18,18,'WELCOME20-DD10B9',20,0);
/*!40000 ALTER TABLE `vouchers` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-06-30 16:16:07
