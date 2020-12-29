-- MySQL dump 10.13  Distrib 8.0.22, for Win64 (x86_64)
--
-- Host: localhost    Database: parkdb
-- ------------------------------------------------------
-- Server version	8.0.22

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
-- Table structure for table `creditcards`
--

DROP TABLE IF EXISTS `creditcards`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `creditcards` (
  `creditCardNumber` varchar(19) NOT NULL,
  `ownerID` varchar(20) DEFAULT NULL,
  `nameOnCard` varchar(30) DEFAULT NULL,
  `cvv` varchar(4) DEFAULT NULL,
  `edMonth` varchar(2) DEFAULT NULL,
  `edYear` varchar(4) DEFAULT NULL,
  `cardType` enum('VISA','MASTERCARD','AMERICANEXPRESS') DEFAULT NULL,
  PRIMARY KEY (`creditCardNumber`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `creditcards`
--

LOCK TABLES `creditcards` WRITE;
/*!40000 ALTER TABLE `creditcards` DISABLE KEYS */;
INSERT INTO `creditcards` VALUES ('1111-1111-1111-1111','111111111','Geralt of Rivia','111','01','2022','VISA'),('1111-1111-1111-1114','111111114','Dante Son of Sparda','114','07','2026','MASTERCARD'),('1111-1111-1111-1115','111111115','Vergil Son of Sparda','115','09','2028','MASTERCARD'),('1111-1111-1111-1118','111111118','Link of Hyrule','118','08','2028','VISA'),('1111-1111-1111-1121','111111121','Dio Brando','121','10','2021','MASTERCARD'),('1111-1111-1111-1126','111111126','Ned Stark','126','04','2034','MASTERCARD'),('1111-111111-11113','111111113','Mario Nintendo','1113','05','2025','AMERICANEXPRESS'),('1111-111111-11119','111111119','Belle Delphine','1119','06','2029','AMERICANEXPRESS'),('1111-111111-11122','111111122','Joseph Joestar','1122','08','2024','AMERICANEXPRESS');
/*!40000 ALTER TABLE `creditcards` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `discounts`
--

DROP TABLE IF EXISTS `discounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `discounts` (
  `discountID` varchar(40) NOT NULL,
  `discountValue` float DEFAULT NULL,
  `startDate` timestamp(1) NULL DEFAULT NULL,
  `endDate` timestamp(1) NULL DEFAULT NULL,
  `isApproved` tinyint DEFAULT NULL,
  PRIMARY KEY (`discountID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `discounts`
--

LOCK TABLES `discounts` WRITE;
/*!40000 ALTER TABLE `discounts` DISABLE KEYS */;
INSERT INTO `discounts` VALUES ('Christmas2021',0.45,'2021-12-24 18:30:00.0','2021-12-31 18:30:00.0',0),('LalaDay2021',0.5,'2021-03-09 18:30:00.0','2021-03-10 18:30:00.0',0),('Passover2021',0.3,'2021-03-27 16:30:00.0','2021-04-02 16:30:00.0',1),('Purim2020',0.2,'2021-02-25 18:30:00.0','2021-02-28 18:30:00.0',1),('VivaLaVida',0.15,'2021-07-31 16:30:00.0','2021-08-31 16:30:00.0',0);
/*!40000 ALTER TABLE `discounts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `parkSite` varchar(20) DEFAULT NULL,
  `numberOfVisitors` int DEFAULT NULL,
  `orderID` int NOT NULL,
  `priceOfOrder` float DEFAULT NULL,
  `email` varchar(20) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `type` enum('PRIVATE','PRIVATEGROUP','GUIDE','FAMILY') DEFAULT NULL,
  `orderStatus` enum('CANCEL','IDLE','CONFIRMED','WAITINGLIST','WAITINGLISTMASSAGESENT') DEFAULT NULL,
  `visitTime` timestamp(1) NULL DEFAULT NULL,
  `timeOfOrder` timestamp(1) NULL DEFAULT NULL,
  `isUsed` tinyint(1) DEFAULT NULL,
  `ownerID` varchar(20) DEFAULT NULL,
  `numberOfSubscribers` int DEFAULT NULL,
  PRIMARY KEY (`orderID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `park`
--

DROP TABLE IF EXISTS `park`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `park` (
  `parkId` varchar(20) NOT NULL,
  `parkName` varchar(20) DEFAULT NULL,
  `maxCapacity` int DEFAULT NULL,
  `managerId` varchar(20) DEFAULT NULL,
  `maxPreOrder` int DEFAULT NULL,
  `avgVisitTime` double DEFAULT NULL,
  `currentNumOfVisitors` int DEFAULT NULL,
  `openTime` int DEFAULT NULL,
  `closeTime` int DEFAULT NULL,
  PRIMARY KEY (`parkId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `park`
--

LOCK TABLES `park` WRITE;
/*!40000 ALTER TABLE `park` DISABLE KEYS */;
INSERT INTO `park` VALUES ('Gold','Gold',100,'000000003',80,4,0,8,16),('Platinum','Platinum',200,'000000004',160,4,0,8,16),('Silver','Silver',50,'000000002',40,4,0,8,16);
/*!40000 ALTER TABLE `park` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `parkentry`
--

DROP TABLE IF EXISTS `parkentry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `parkentry` (
  `entryType` enum('Personal','Subscriber','Group','PrivateGroup') DEFAULT NULL,
  `personID` varchar(20) NOT NULL,
  `parkID` varchar(20) DEFAULT NULL,
  `arriveTime` timestamp(1) NOT NULL,
  `exitTime` timestamp(1) NULL DEFAULT NULL,
  `numberOfVisitors` int DEFAULT NULL,
  `numberOfSubscribers` int DEFAULT NULL,
  `isCasual` tinyint DEFAULT NULL,
  `priceOfOrder` float DEFAULT NULL,
  `priceOfEntry` float DEFAULT NULL,
  PRIMARY KEY (`personID`,`arriveTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `parkentry`
--

LOCK TABLES `parkentry` WRITE;
/*!40000 ALTER TABLE `parkentry` DISABLE KEYS */;
/*!40000 ALTER TABLE `parkentry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subscribers`
--

DROP TABLE IF EXISTS `subscribers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `subscribers` (
  `subscriberID` varchar(20) NOT NULL,
  `personalID` varchar(20) DEFAULT NULL,
  `firstName` varchar(20) DEFAULT NULL,
  `lastName` varchar(20) DEFAULT NULL,
  `phone` varchar(10) DEFAULT NULL,
  `email` varchar(30) DEFAULT NULL,
  `creditCardNumber` varchar(19) DEFAULT NULL,
  `familySize` int DEFAULT NULL,
  `type` enum('SUBSCRIBER','GUIDE') DEFAULT NULL,
  PRIMARY KEY (`subscriberID`),
  KEY `creditCardNumber` (`creditCardNumber`),
  CONSTRAINT `subscribers_ibfk_1` FOREIGN KEY (`creditCardNumber`) REFERENCES `creditcards` (`creditCardNumber`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subscribers`
--

LOCK TABLES `subscribers` WRITE;
/*!40000 ALTER TABLE `subscribers` DISABLE KEYS */;
INSERT INTO `subscribers` VALUES ('S111111111','111111111','Geralt','of Rivia','0500000001','Geralt@gmail.com','1111-1111-1111-1111',3,'SUBSCRIBER'),('S111111112','111111112','Jack','Sparrow','0500000002','Jack@gmail.com',NULL,1,'GUIDE'),('S111111113','111111113','Mario','Nintendo','0500000003','Mario@nintendo.com','1111-111111-11113',5,'SUBSCRIBER'),('S111111114','111111114','Dante','Son of Sparda','0500000004','Dante@gmail.com','1111-1111-1111-1114',3,'SUBSCRIBER'),('S111111115','111111115','Vergil','Son of Sparda','0500000005','Vergil@gmail.com','1111-1111-1111-1115',2,'SUBSCRIBER'),('S111111116','111111116','Luke','Skywalker','0500000006','Luke@gmail.com',NULL,1,'SUBSCRIBER'),('S111111117','111111117','Yoda','Master','0500000007','Yoda@gmail.com',NULL,1,'GUIDE'),('S111111118','111111118','Link','of Hyrule','0500000008','Link@walla.com','1111-1111-1111-1118',2,'SUBSCRIBER'),('S111111119','111111119','Belle','Delphine','0500000009','Belle@gmail.com','1111-111111-11119',1,'GUIDE'),('S111111120','111111120','Muhammad','Avdol','0500000010','Avdol@gmail.com',NULL,1,'GUIDE'),('S111111121','111111121','Dio','Brando','0500000021','Dio@gmail.com','1111-1111-1111-1121',2,'SUBSCRIBER'),('S111111122','111111122','Joseph','Joestar','0500000022','Joseph@gmail.com','1111-111111-11122',3,'SUBSCRIBER'),('S111111123','111111123','Ash','Ketchum','0500000023','Ash@gmail.com',NULL,3,'SUBSCRIBER'),('S111111124','111111124','Gary','Oak','0500000024','Gary@gmail.com',NULL,2,'SUBSCRIBER'),('S111111125','111111125','Kratos','God of War','0500000025','Kratos@gmail.com',NULL,1,'GUIDE'),('S111111126','111111126','Ned','Stark','0500000026','Ned@gmail.com','1111-1111-1111-1126',1,'GUIDE');
/*!40000 ALTER TABLE `subscribers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `valuechangerequest`
--

DROP TABLE IF EXISTS `valuechangerequest`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `valuechangerequest` (
  `parkId` varchar(20) NOT NULL,
  `attributeName` enum('MaxCapacity','MaxPreOrder','AvgVisitTime') NOT NULL,
  `requestedValue` double DEFAULT NULL,
  `currentValue` double DEFAULT NULL,
  PRIMARY KEY (`parkId`,`attributeName`),
  CONSTRAINT `valuechangerequest_ibfk_1` FOREIGN KEY (`parkId`) REFERENCES `park` (`parkId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `valuechangerequest`
--

LOCK TABLES `valuechangerequest` WRITE;
/*!40000 ALTER TABLE `valuechangerequest` DISABLE KEYS */;
/*!40000 ALTER TABLE `valuechangerequest` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `waitinglist`
--

DROP TABLE IF EXISTS `waitinglist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `waitinglist` (
  `parkSite` varchar(20) DEFAULT NULL,
  `numberOfVisitors` int DEFAULT NULL,
  `orderID` int NOT NULL,
  `priceOfOrder` float DEFAULT NULL,
  `email` varchar(20) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `type` enum('PRIVATE','PRIVATEGROUP','GUIDE','FAMILY') DEFAULT NULL,
  `orderStatus` enum('CANCEL','IDLE','CONFIRMED','WAITINGLIST','WAITINGLISTMASSAGESENT') DEFAULT NULL,
  `visitTime` timestamp(1) NULL DEFAULT NULL,
  `timeOfOrder` timestamp(1) NULL DEFAULT NULL,
  `isUsed` tinyint(1) DEFAULT NULL,
  `ownerID` varchar(20) DEFAULT NULL,
  `numberOfSubscribers` int DEFAULT NULL,
  PRIMARY KEY (`orderID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `waitinglist`
--

LOCK TABLES `waitinglist` WRITE;
/*!40000 ALTER TABLE `waitinglist` DISABLE KEYS */;
/*!40000 ALTER TABLE `waitinglist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `workers`
--

DROP TABLE IF EXISTS `workers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `workers` (
  `firstName` varchar(20) DEFAULT NULL,
  `lastName` varchar(20) DEFAULT NULL,
  `workerID` varchar(12) DEFAULT NULL,
  `email` varchar(30) DEFAULT NULL,
  `userName` varchar(20) NOT NULL,
  `workerType` varchar(20) DEFAULT NULL,
  `password` varchar(20) DEFAULT NULL,
  `isLogged` varchar(4) DEFAULT NULL,
  `permissions` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`userName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `workers`
--

LOCK TABLES `workers` WRITE;
/*!40000 ALTER TABLE `workers` DISABLE KEYS */;
INSERT INTO `workers` VALUES ('Nir','Pikan','000000001','nir@gmail.com','depManager','departmentManager','Aa123456','NO','department Registration VistitorsView ReportExport ApproveParameters ApproveDiscounts'),('Or','Man','000000003','or@gmail.com','goldManager','parkManager','Aa123456','NO','Gold Registration VistitorsView ReportExport EditParameters'),('Giorno','Giovanna','000000006','giorno@gmail.com','goldWorker','parkWorker','Aa123456','NO','Gold Registration VistitorsView'),('Michael','Gindin','000000004','michael@gmail.com','platinumManager','parkManager','Aa123456','NO','Platinum Registration VistitorsView ReportExport EditParameters'),('Jotaro','Kujo','000000007','jotaro@gmail.com','platinumWorker','parkWorker','Aa123456','NO','Platinum Registration VistitorsView'),('Roman','Kozak','000000002','roman@gmail.com','silverManager','parkManager','Aa123456','NO','Silver Registration VistitorsView ReportExport EditParameters'),('Aviv','Vanunu','000000005','aviv@gmail.com','silverWorker','parkWorker','Aa123456','NO','Silver Registration VistitorsView');
/*!40000 ALTER TABLE `workers` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-12-29 19:55:35
