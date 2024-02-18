CREATE TABLE `product` (
  `cost` int(11) NOT NULL,
  `expiration_date` date NOT NULL,
  `price` int(11) NOT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `seller_id` bigint(20) NOT NULL,
  `barcode` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `category` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `size` enum('LARGE','SMALL') COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` enum('PAUSE','SALE') COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` tinytext COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `product_uk_barcode` (`barcode`),
  KEY `product_index_seller` (`seller_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `product_name_initial_character` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_id` bigint(20) NOT NULL,
  `seller_id` bigint(20) NOT NULL,
  `initial_character` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `product_name_initial_character_index_seller_id_initial_character` (`seller_id`,`initial_character`),
  KEY `product_name_initial_character_index_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `seller` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `phone_number` varchar(11) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `seller_uk_phone_number` (`phone_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;