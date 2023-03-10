-- kb_catalog_inventory.category definition



-- kb_catalog_inventory.hibernate_sequence definition

CREATE TABLE `hibernate_sequence` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;

CREATE TABLE `category` (
  `id` bigint NOT NULL,
  `category_icon` varchar(255) DEFAULT NULL,
  `category_name` varchar(255) DEFAULT NULL,
  `parent_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2y94svpmqttx80mshyny85wqr` (`parent_id`),
  CONSTRAINT `FK2y94svpmqttx80mshyny85wqr` FOREIGN KEY (`parent_id`) REFERENCES `category` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;
ALTER TABLE kb_catalog_inventory.category ADD category_stage INTEGER NULL;

-- kb_catalog_inventory.brands definition

CREATE TABLE `brands` (
  `id` bigint NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `thumbnails` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;


-- kb_catalog_inventory.brand_models definition

CREATE TABLE `brand_models` (
  `id` bigint NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `brand_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKmctjy92jf1kjlyc1dhnx99i48` (`brand_id`),
  CONSTRAINT `FKmctjy92jf1kjlyc1dhnx99i48` FOREIGN KEY (`brand_id`) REFERENCES `brands` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;



-- kb_catalog_inventory.brand_model_category definition

CREATE TABLE `brand_model_category` (
  `id` bigint NOT NULL,
  `category_name` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `brand_model_id` bigint DEFAULT NULL,
  `category_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1g01ksycpw7h9p5tdlbr2yjtp` (`brand_model_id`),
  KEY `FKikhy2upw2md69ta46a5ui8uk7` (`category_id`),
  CONSTRAINT `FK1g01ksycpw7h9p5tdlbr2yjtp` FOREIGN KEY (`brand_model_id`) REFERENCES `brand_models` (`id`),
  CONSTRAINT `FKikhy2upw2md69ta46a5ui8uk7` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;



-- kb_catalog_inventory.supplier definition

CREATE TABLE `supplier` (
  `id` bigint NOT NULL,
  `gst_or_udyog_number` varchar(255) DEFAULT NULL,
  `supplier_address` varchar(255) DEFAULT NULL,
  `supplier_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;


-- kb_catalog_inventory.product definition

CREATE TABLE `product` (
  `id` bigint NOT NULL,
  `country_id` bigint DEFAULT NULL,
  `preview_image` varchar(255) DEFAULT NULL,
  `product_name` varchar(255) DEFAULT NULL,
  `product_string` varchar(255) DEFAULT NULL,
  `updated_on` datetime DEFAULT NULL,
  `brand_model_category_id` bigint DEFAULT NULL,
  `supplier_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3d06s544relukkqkbsosx8vor` (`brand_model_category_id`),
  KEY `FK2kxvbr72tmtscjvyp9yqb12by` (`supplier_id`),
  CONSTRAINT `FK2kxvbr72tmtscjvyp9yqb12by` FOREIGN KEY (`supplier_id`) REFERENCES `supplier` (`id`),
  CONSTRAINT `FK3d06s544relukkqkbsosx8vor` FOREIGN KEY (`brand_model_category_id`) REFERENCES `brand_model_category` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;


-- kb_catalog_inventory.variation definition

CREATE TABLE `variation` (
  `id` bigint NOT NULL,
  `variation_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;

-- kb_catalog_inventory.variation_options definition

CREATE TABLE `variation_options` (
  `id` bigint NOT NULL,
  `variation_option_name` varchar(255) DEFAULT NULL,
  `variation_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1sd9jrwkc19ru5vurw7ptss16` (`variation_id`),
  CONSTRAINT `FK1sd9jrwkc19ru5vurw7ptss16` FOREIGN KEY (`variation_id`) REFERENCES `variation` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;

-- kb_catalog_inventory.image_gallery definition

CREATE TABLE `image_gallery` (
  `id` bigint NOT NULL,
  `large_image` varchar(255) DEFAULT NULL,
  `medium_image` varchar(255) DEFAULT NULL,
  `small_image` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;


-- kb_catalog_inventory.product_variation_option_value definition

CREATE TABLE `product_variation_option_value` (
  `id` bigint NOT NULL,
  `product_option_name` varchar(255) DEFAULT NULL,
  `product_id` bigint DEFAULT NULL,
  `variation_option_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5i27xs0d9ebpvatd3nwbdmcl3` (`product_id`),
  KEY `FK81p4k9gq8t78vcpjv6u2aulwl` (`variation_option_id`),
  CONSTRAINT `FK5i27xs0d9ebpvatd3nwbdmcl3` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `FK81p4k9gq8t78vcpjv6u2aulwl` FOREIGN KEY (`variation_option_id`) REFERENCES `variation_options` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;


-- kb_catalog_inventory.product_variation_option_image definition

CREATE TABLE `product_variation_option_image` (
  `id` bigint NOT NULL,
  `is_featured` bit(1) DEFAULT NULL,
  `image_gallery_id` bigint DEFAULT NULL,
  `product_id` bigint DEFAULT NULL,
  `product_variation_option_value_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKcfgp9dp84lsbs2wrwswb3imkd` (`image_gallery_id`),
  KEY `FKruuvm5a8kgbyhcm7p7apt2ols` (`product_id`),
  KEY `FK1gydw9l4xvo9sd03xiti6vb5y` (`product_variation_option_value_id`),
  CONSTRAINT `FK1gydw9l4xvo9sd03xiti6vb5y` FOREIGN KEY (`product_variation_option_value_id`) REFERENCES `product_variation_option_value` (`id`),
  CONSTRAINT `FKcfgp9dp84lsbs2wrwswb3imkd` FOREIGN KEY (`image_gallery_id`) REFERENCES `image_gallery` (`id`),
  CONSTRAINT `FKruuvm5a8kgbyhcm7p7apt2ols` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;



-- kb_catalog_inventory.product_combinations definition

CREATE TABLE `product_combinations` (
  `id` bigint NOT NULL,
  `available_stock` int DEFAULT NULL,
  `combination_string` varchar(255) DEFAULT NULL,
  `inserted_on` datetime DEFAULT NULL,
  `price` float DEFAULT NULL,
  `sku` varchar(255) DEFAULT NULL,
  `supplier_price` float DEFAULT NULL,
  `unique_identifier` varchar(255) DEFAULT NULL,
  `updated_on` datetime DEFAULT NULL,
  `product_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKcii2n5knrfrd2cv5vayuqckp4` (`product_id`),
  CONSTRAINT `FKcii2n5knrfrd2cv5vayuqckp4` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;
ALTER TABLE kb_catalog_inventory.product_combinations ADD HSN varchar(255) NULL;
ALTER TABLE kb_catalog_inventory.product_combinations ADD product_weight varchar(255) NULL;


-- kb_catalog_inventory.product_stock definition

CREATE TABLE `product_stock` (
  `id` bigint NOT NULL,
  `total_price` float DEFAULT NULL,
  `total_stock` int DEFAULT NULL,
  `unit_price` float DEFAULT NULL,
  `product_combination_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK51kfrblyujfddxs0yjs4anjm5` (`product_combination_id`),
  CONSTRAINT `FK51kfrblyujfddxs0yjs4anjm5` FOREIGN KEY (`product_combination_id`) REFERENCES `product_combinations` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;



-- kb_catalog_inventory.product_cache definition

CREATE TABLE `product_cache` (
  `id` bigint NOT NULL,
  `data` varchar(255) DEFAULT NULL,
  `slug` varchar(255) DEFAULT NULL,
  `product_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKlwrinb28cq3vb64gj5flr6a6u` (`product_id`),
  CONSTRAINT `FKlwrinb28cq3vb64gj5flr6a6u` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;