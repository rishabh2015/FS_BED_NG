SET FOREIGN_KEY_CHECKS = 0;

INSERT INTO kb_catalog_inventory.category (id,category_icon,category_name,parent_id,is_navigation,category_stage) VALUES
	 (1,NULL,'Women',NULL,1,1),
	 (2,NULL,'Men',NULL,1,1),
	 (3,NULL,'Women''s Clothing',1,1,2),
	 (4,NULL,'Men''s Clothing',2,1,2),
	 (5,NULL,'Topwear',4,1,3),
	 (6,NULL,'Bottomwear',4,1,3),
	 (7,NULL,'Footwear',2,1,2),
	 (8,NULL,'Indian and Festive Wear',4,1,3),
	 (9,NULL,'Fashion Accessories',2,1,2),
	 (10,NULL,'Innnerwear and SleepWear',2,1,2);
INSERT INTO kb_catalog_inventory.category (id,category_icon,category_name,parent_id,is_navigation,category_stage) VALUES
	 (11,NULL,'Jewellery & Accessories',2,1,2),
	 (12,NULL,'T-Shirts',5,1,4),
	 (13,NULL,'Casual Shirt',5,1,4),
	 (14,NULL,'Formal Shirt',5,1,4),
	 (15,NULL,'Jeans',6,1,4),
	 (16,NULL,'Casual Trousers',6,1,4),
	 (17,NULL,'Formal Trousers',6,1,4),
	 (18,NULL,'Shorts',6,1,4),
	 (19,NULL,'Kurtas & Kurta Sets',8,1,4),
	 (20,NULL,'Sherwanis',8,1,4);
INSERT INTO kb_catalog_inventory.category (id,category_icon,category_name,parent_id,is_navigation,category_stage) VALUES
	 (21,NULL,'Briefs',10,1,3),
	 (22,NULL,'Boxers',10,1,3),
	 (23,NULL,'Vests',10,1,3),
	 (24,NULL,'Sleepwear ',10,1,3),
	 (25,NULL,'Casual Shoes',7,1,3),
	 (26,NULL,'Sports Shoes',7,1,3),
	 (27,NULL,'Formal Shoes',7,1,3),
	 (28,NULL,'Sneakers',7,1,3),
	 (29,NULL,'Wallets',9,1,3),
	 (30,NULL,'Belts',9,1,3);
INSERT INTO kb_catalog_inventory.category (id,category_icon,category_name,parent_id,is_navigation,category_stage) VALUES
	 (31,NULL,'Perfumes & Body Mists',9,1,3),
	 (32,NULL,'Trimmers',9,1,3),
	 (33,NULL,'Deodorants',9,1,3),
	 (34,NULL,'Rings for Men',11,1,3),
	 (35,NULL,'Chains for Men',11,1,3),
	 (36,NULL,'Indian & Fusion Wear',3,1,3),
	 (37,NULL,'Western Wear',3,1,3),
	 (38,NULL,'Footwear',1,1,2),
	 (39,NULL,'Innnerwear and SleepWear',3,1,3),
	 (40,NULL,'Jewellery & Accessories',1,1,2);
INSERT INTO kb_catalog_inventory.category (id,category_icon,category_name,parent_id,is_navigation,category_stage) VALUES
	 (41,NULL,'Beauty & Personal Care',1,1,2),
	 (42,NULL,'Kurtas & Suits',36,1,4),
	 (43,NULL,'Kurtis, Tunics & Tops',36,1,4),
	 (44,NULL,'Ethnic Wear',36,1,4),
	 (45,NULL,'Leggings, Salwars & Churidars',36,1,4),
	 (46,NULL,'Skirts & Palazzos',36,1,4),
	 (47,NULL,'Sarees',36,1,4),
	 (48,NULL,'Jumpsuits',37,1,4),
	 (49,NULL,'Tops',37,1,4),
	 (50,NULL,'Jeans',37,1,4);
INSERT INTO kb_catalog_inventory.category (id,category_icon,category_name,parent_id,is_navigation,category_stage) VALUES
	 (51,NULL,'Trousers & Capris',37,1,4),
	 (52,NULL,'Shorts & Skirts',37,1,4),
	 (53,NULL,'Flats',38,1,3),
	 (54,NULL,'Casual Shoes',38,1,3),
	 (55,NULL,'Heels',38,1,3),
	 (56,NULL,'Boots',38,1,3),
	 (57,NULL,'Sleepwear',39,1,4),
	 (58,NULL,'Nightwear',39,1,4),
	 (59,NULL,'Camisoles and Thermals',39,1,4),
	 (60,NULL,'Bras & Lingerie Sets',39,1,4);
INSERT INTO kb_catalog_inventory.category (id,category_icon,category_name,parent_id,is_navigation,category_stage) VALUES
	 (61,NULL,'Babydolls',58,1,1),
	 (62,NULL,'Nightdress',58,1,1),
	 (63,NULL,'Pyjamas',58,1,1),
	 (64,NULL,'Nightsuits',58,1,1),
	 (65,NULL,'Pyjamas',58,1,1),
	 (66,NULL,'Camisoles',59,1,1),
	 (67,NULL,'Thermals Top',59,1,1),
	 (68,NULL,'Thermals Bottom',59,1,1),
	 (69,NULL,'Bras',60,0,1),
	 (70,NULL,'Lingerie Sets',60,0,1);
INSERT INTO kb_catalog_inventory.category (id,category_icon,category_name,parent_id,is_navigation,category_stage) VALUES
	 (71,NULL,'Briefs',60,0,1),
	 (72,NULL,'Shapewear',60,0,1),
	 (73,NULL,'Jewellery',40,0,3),
	 (74,NULL,'Anklets & Toe Rings',73,0,4),
	 (75,NULL,'Necklaces and Chains',73,0,4),
	 (76,NULL,'Pendants and Lockets',73,0,4),
	 (77,NULL,'RIngs',73,0,4),
	 (78,NULL,'Bangles',73,0,4),
	 (79,NULL,'Bracelets',73,0,4),
	 (80,NULL,'Jewellery Set',73,0,4);
INSERT INTO kb_catalog_inventory.category (id,category_icon,category_name,parent_id,is_navigation,category_stage) VALUES
	 (81,NULL,'Ear-rings and Studs',73,0,4),
	 (82,NULL,'Mangtikas',73,0,4),
	 (83,NULL,'Mangalsutras',73,0,4),
	 (84,NULL,'Nose-rings',73,0,4),
	 (85,NULL,'Kamarbandhs',73,0,4),
	 (86,NULL,'Bajuband',73,0,4);


INSERT INTO kb_catalog_inventory.brands (id,name,thumbnails) VALUES
	 (1,'Roadster',NULL),
	 (2,'WROGN',NULL),
	 (3,'Puma',NULL),
	 (4,'Free Authority',NULL),
	 (5,'Levi''s',NULL),
	 (6,'Lee',NULL),
	 (7,'Louis Philippe Sport',NULL),
	 (8,'Parx',NULL),
	 (9,'ColorPlus',NULL),
	 (10,'Allen Solly',NULL);
INSERT INTO kb_catalog_inventory.brands (id,name,thumbnails) VALUES
	 (11,'Blackberrys',NULL),
	 (12,'SPYKAR',NULL),
	 (13,'Flying Machine',NULL),
	 (14,'Van Heusen',NULL),
	 (15,'Louis Philippe',NULL),
	 (16,'Park Avenue',NULL),
	 (17,'Raymond',NULL),
	 (18,'Peter England',NULL),
	 (19,'Arrow',NULL),
	 (20,'DEYANN',NULL);
INSERT INTO kb_catalog_inventory.brands (id,name,thumbnails) VALUES
	 (21,'SOJANYA',NULL),
	 (22,'KISAH',NULL),
	 (23,'Manyavar',NULL),
	 (24,'Pepe Jeans',NULL),
	 (25,'HERE&NOW',NULL),
	 (26,'Jockey',NULL),
	 (27,'Calvin Klein',NULL),
	 (28,'Jack & Jones',NULL),
	 (29,'Tommy Hilfiger',NULL),
	 (30,'U.S. Polo Assn.',NULL);
INSERT INTO kb_catalog_inventory.brands (id,name,thumbnails) VALUES
	 (31,'BATA',NULL),
	 (32,'Woodland',NULL),
	 (33,'Ajmal',NULL),
	 (34,'Menjewell',NULL),
	 (35,'Wild stone',NULL),
	 (36,'Bvlgari',NULL),
	 (37,'DAVID BECKHAM',NULL),
	 (38,'Nivea',NULL),
	 (39,'Axe',NULL),
	 (40,'Fogg',NULL);
INSERT INTO kb_catalog_inventory.brands (id,name,thumbnails) VALUES
	 (41,'NOVA',NULL),
	 (42,'Philips',NULL),
	 (43,'Vega',NULL),
	 (44,'Pothys',NULL),
	 (45,'Mitera',NULL),
	 (46,'KALINI',NULL),
	 (47,'Anouk',NULL),
	 (48,'W',NULL),
	 (49,'MIMOSA',NULL),
	 (50,'VASTRANAND',NULL);
INSERT INTO kb_catalog_inventory.brands (id,name,thumbnails) VALUES
	 (51,'Saree mall',NULL),
	 (52,'Cottinfab',NULL),
	 (53,'FabAlley',NULL),
	 (54,'SASSAFRAS',NULL),
	 (55,'Tanishq',NULL),
	 (56,'P.C. jewellers',NULL),
	 (57,'Alankar',NULL);


INSERT INTO kb_catalog_inventory.brand_models (id,name,brand_id) VALUES
	 (1,'Blue Dyed Crinkled Polo Collar T-shirt',1),
	 (2,'Striped Henley Neck T-shirt',1),
	 (3,'Cotton Solid Polo Collar T-shirt',1),
	 (4,'Solid Pure Cotton Polo Collar T-shirt',2),
	 (5,'Solid Pure Cotton Polo Collar T-shirt',2),
	 (6,'Pure Cotton Logo Embroidered Round Neck T-shirt',2),
	 (7,'Pure Cotton Printed Round Neck T-shirt',2),
	 (8,'Checked Casual Shirt',3),
	 (9,'Standard Fit Colourblocked Casual Shirt',3),
	 (10,'Slim Fit Colourblocked Casual Shirt',3);
INSERT INTO kb_catalog_inventory.brand_models (id,name,brand_id) VALUES
	 (11,'Classic Regular Fit Solid Formal Shirt',16),
	 (12,'Pure Cotton Solid Formal Shirt',16),
	 (13,'Printed Kurta with Churidar',20),
	 (14,'Printed Straight Kurta',20),
	 (15,'Solid Straight Kurta',20),
	 (16,'Printed Kurta with Churidar',21),
	 (17,'Printed Straight Kurta',21),
	 (18,'Solid Straight Kurta',21),
	 (19,'Printed Kurta with Churidar',22),
	 (20,'Printed Straight Kurta',22);
INSERT INTO kb_catalog_inventory.brand_models (id,name,brand_id) VALUES
	 (21,'Solid Straight Kurta',22),
	 (22,'Printed Kurta with Churidar',23),
	 (23,'Printed Straight Kurta',23),
	 (24,'Solid Straight Kurta',23),
	 (25,'Blue Dyed Crinkled Polo Collar T-shirt',6),
	 (26,'Striped Henley Neck T-shirt',6),
	 (27,'Cotton Solid Polo Collar T-shirt',6),
	 (28,'Solid Pure Cotton Polo Collar T-shirt',6),
	 (29,'Solid Pure Cotton Polo Collar T-shirt',6),
	 (30,'Pure Cotton Logo Embroidered Round Neck T-shirt',6);
INSERT INTO kb_catalog_inventory.brand_models (id,name,brand_id) VALUES
	 (31,'Pure Cotton Printed Round Neck T-shirt',6),
	 (32,'Blue Dyed Crinkled Polo Collar T-shirt',7),
	 (33,'Striped Henley Neck T-shirt',7),
	 (34,'Cotton Solid Polo Collar T-shirt',7),
	 (35,'Solid Pure Cotton Polo Collar T-shirt',7),
	 (36,'Solid Pure Cotton Polo Collar T-shirt',7),
	 (37,'Pure Cotton Logo Embroidered Round Neck T-shirt',7),
	 (38,'Pure Cotton Printed Round Neck T-shirt',7),
	 (39,'Blue Dyed Crinkled Polo Collar T-shirt',10),
	 (40,'Striped Henley Neck T-shirt',10);
INSERT INTO kb_catalog_inventory.brand_models (id,name,brand_id) VALUES
	 (41,'Cotton Solid Polo Collar T-shirt',10),
	 (42,'Solid Pure Cotton Polo Collar T-shirt',10),
	 (43,'Solid Pure Cotton Polo Collar T-shirt',10),
	 (44,'Pure Cotton Logo Embroidered Round Neck T-shirt',10),
	 (45,'Pure Cotton Printed Round Neck T-shirt',10),
	 (46,'Classic Regular Fit Solid Formal Shirt',10),
	 (47,'Pure Cotton Solid Formal Shirt',11),
	 (48,'Classic Regular Fit Solid Formal Shirt',11),
	 (49,'Pure Cotton Solid Formal Shirt',12),
	 (50,'Classic Regular Fit Solid Formal Shirt',12);
INSERT INTO kb_catalog_inventory.brand_models (id,name,brand_id) VALUES
	 (51,'Pure Cotton Solid Formal Shirt',13),
	 (52,'Classic Regular Fit Solid Formal Shirt',13),
	 (53,'Pure Cotton Solid Formal Shirt',14),
	 (54,'Classic Regular Fit Solid Formal Shirt',14),
	 (55,'Pure Cotton Solid Formal Shirt',15),
	 (56,'Classic Regular Fit Solid Formal Shirt',15),
	 (57,'Pure Cotton Solid Formal Shirt',16),
	 (58,'Denim Jeans',5),
	 (59,'Denim Jeans',6),
	 (60,'Denim Jeans',13);
INSERT INTO kb_catalog_inventory.brand_models (id,name,brand_id) VALUES
	 (61,'Denim Jeans',24),
	 (62,'Casual Trousers',7),
	 (63,'Casual Trousers',8),
	 (64,'Casual Trousers',9),
	 (65,'Casual Trousers',10),
	 (66,'Casual Trousers',11),
	 (67,'Casual Trousers',12),
	 (68,'Casual Trousers',13),
	 (69,'Casual Trousers',14),
	 (70,'Casual Trousers',15);
INSERT INTO kb_catalog_inventory.brand_models (id,name,brand_id) VALUES
	 (71,'Casual Trousers',16),
	 (72,'Casual Trousers',17),
	 (73,'Formal Trousers',7),
	 (74,'Formal Trousers',8),
	 (75,'Formal Trousers',9),
	 (76,'Formal Trousers',10),
	 (77,'Formal Trousers',11),
	 (78,'Formal Trousers',12),
	 (79,'Formal Trousers',13),
	 (80,'Formal Trousers',14);
INSERT INTO kb_catalog_inventory.brand_models (id,name,brand_id) VALUES
	 (81,'Formal Trousers',15),
	 (82,'Formal Trousers',16),
	 (83,'Formal Trousers',17),
	 (84,'Sherwanis',23),
	 (85,'Briefs',26),
	 (86,'Briefs',27),
	 (87,'Briefs',28),
	 (88,'Briefs',29),
	 (89,'Briefs',30),
	 (90,'Boxers',26);
INSERT INTO kb_catalog_inventory.brand_models (id,name,brand_id) VALUES
	 (91,'Boxers',27),
	 (92,'Boxers',28),
	 (93,'Boxers',29),
	 (94,'Boxers',30),
	 (95,'vests',26),
	 (96,'vests',27),
	 (97,'vests',28),
	 (98,'vests',29),
	 (99,'vests',30),
	 (100,'Sleepwear ',26);
INSERT INTO kb_catalog_inventory.brand_models (id,name,brand_id) VALUES
	 (101,'Sleepwear ',27),
	 (102,'Sleepwear ',28),
	 (103,'Sleepwear ',29),
	 (104,'Sleepwear ',30),
	 (105,'Sports Shoes',3),
	 (106,'Formal Shoes',31),
	 (107,'Sneakers',32),
	 (108,'Sports Shoes',31),
	 (109,'Formal Shoes',3),
	 (110,'Sneakers',32);
INSERT INTO kb_catalog_inventory.brand_models (id,name,brand_id) VALUES
	 (111,'Sports Shoes',32),
	 (112,'Formal Shoes',31),
	 (113,'Sneakers',3),
	 (114,'Wallets',29),
	 (115,'Belts',30),
	 (116,'Wallets',30),
	 (117,'Belts',29),
	 (118,'Trimmers',41),
	 (119,'Trimmers',42),
	 (120,'Trimmers',43);
INSERT INTO kb_catalog_inventory.brand_models (id,name,brand_id) VALUES
	 (121,'Perfumes & Body Mists',35),
	 (122,'Perfumes & Body Mists',36),
	 (123,'Perfumes & Body Mists',37),
	 (124,'Perfumes & Body Mists',38),
	 (125,'Perfumes & Body Mists',39),
	 (126,'Perfumes & Body Mists',40),
	 (127,'Deodorants',35),
	 (128,'Deodorants',36),
	 (129,'Deodorants',37),
	 (130,'Deodorants',38);
INSERT INTO kb_catalog_inventory.brand_models (id,name,brand_id) VALUES
	 (131,'Deodorants',39),
	 (132,'Deodorants',40),
	 (133,'Kurtas & Suits',44),
	 (134,'Kurtis, Tunics & Tops',44),
	 (135,'Ethnic Wear',44),
	 (136,'Leggings, Salwars & Churidars',44),
	 (137,'Skirts & Palazzos',44),
	 (138,'Sarees',44),
	 (139,'Kurtas & Suits',45),
	 (140,'Kurtis, Tunics & Tops',45);
INSERT INTO kb_catalog_inventory.brand_models (id,name,brand_id) VALUES
	 (141,'Ethnic Wear',45),
	 (142,'Leggings, Salwars & Churidars',45),
	 (143,'Skirts & Palazzos',45),
	 (144,'Sarees',45),
	 (145,'Kurtas & Suits',46),
	 (146,'Kurtis, Tunics & Tops',46),
	 (147,'Ethnic Wear',46),
	 (148,'Leggings, Salwars & Churidars',46),
	 (149,'Skirts & Palazzos',46),
	 (150,'Sarees',46);
INSERT INTO kb_catalog_inventory.brand_models (id,name,brand_id) VALUES
	 (151,'Kurtas & Suits',47),
	 (152,'Kurtis, Tunics & Tops',47),
	 (153,'Ethnic Wear',47),
	 (154,'Leggings, Salwars & Churidars',47),
	 (155,'Skirts & Palazzos',47),
	 (156,'Sarees',47),
	 (157,'Kurtas & Suits',48),
	 (158,'Kurtis, Tunics & Tops',48),
	 (159,'Ethnic Wear',48),
	 (160,'Leggings, Salwars & Churidars',48);
INSERT INTO kb_catalog_inventory.brand_models (id,name,brand_id) VALUES
	 (161,'Skirts & Palazzos',48),
	 (162,'Sarees',48),
	 (163,'Kurtas & Suits',49),
	 (164,'Kurtis, Tunics & Tops',49),
	 (165,'Ethnic Wear',49),
	 (166,'Leggings, Salwars & Churidars',49),
	 (167,'Skirts & Palazzos',49),
	 (168,'Sarees',49),
	 (169,'Kurtas & Suits',50),
	 (170,'Kurtis, Tunics & Tops',50);
INSERT INTO kb_catalog_inventory.brand_models (id,name,brand_id) VALUES
	 (171,'Ethnic Wear',50),
	 (172,'Leggings, Salwars & Churidars',50),
	 (173,'Skirts & Palazzos',50),
	 (174,'Sarees',50),
	 (175,'Kurtas & Suits',51),
	 (176,'Kurtis, Tunics & Tops',51),
	 (177,'Ethnic Wear',51),
	 (178,'Leggings, Salwars & Churidars',51),
	 (179,'Skirts & Palazzos',51),
	 (180,'Sarees',51);
INSERT INTO kb_catalog_inventory.brand_models (id,name,brand_id) VALUES
	 (181,'Jumpsuits',52),
	 (182,'Tops',52),
	 (183,'Trousers & Capris',52),
	 (184,'Shorts & Skirts',52),
	 (185,'Jumpsuits',53),
	 (186,'Tops',53),
	 (187,'Trousers & Capris',53),
	 (188,'Shorts & Skirts',53),
	 (189,'Jumpsuits',54),
	 (190,'Tops',54);
INSERT INTO kb_catalog_inventory.brand_models (id,name,brand_id) VALUES
	 (191,'Trousers & Capris',54),
	 (192,'Shorts & Skirts',54),
	 (193,'Flats',31),
	 (194,'Casual Shoes',31),
	 (195,'Heels',31),
	 (196,'Boots',31),
	 (197,'Flats',32),
	 (198,'Casual Shoes',32),
	 (199,'Heels',32),
	 (200,'Boots',32);
INSERT INTO kb_catalog_inventory.brand_models (id,name,brand_id) VALUES
	 (201,'Babydolls',26),
	 (202,'Nightdress',26),
	 (203,'Pyjamas',26),
	 (204,'Nightsuits',26),
	 (205,'Pyjamas',26),
	 (206,'Camisoles',26),
	 (207,'Thermals Top',26),
	 (208,'Thermals Bottom',26),
	 (209,'Bras',26),
	 (210,'Lingerie Sets',26);
INSERT INTO kb_catalog_inventory.brand_models (id,name,brand_id) VALUES
	 (211,'Briefs',26),
	 (212,'Shapewear',26),
	 (213,'Babydolls',27),
	 (214,'Nightdress',27),
	 (215,'Pyjamas',27),
	 (216,'Nightsuits',27),
	 (217,'Pyjamas',27),
	 (218,'Camisoles',27),
	 (219,'Thermals Top',27),
	 (220,'Thermals Bottom',27);
INSERT INTO kb_catalog_inventory.brand_models (id,name,brand_id) VALUES
	 (221,'Bras',27),
	 (222,'Lingerie Sets',27),
	 (223,'Briefs',27),
	 (224,'Shapewear',27),
	 (225,'Babydolls',28),
	 (226,'Nightdress',28),
	 (227,'Pyjamas',28),
	 (228,'Nightsuits',28),
	 (229,'Pyjamas',28),
	 (230,'Camisoles',28);
INSERT INTO kb_catalog_inventory.brand_models (id,name,brand_id) VALUES
	 (231,'Thermals Top',28),
	 (232,'Thermals Bottom',28),
	 (233,'Bras',28),
	 (234,'Lingerie Sets',28),
	 (235,'Briefs',28),
	 (236,'Shapewear',28),
	 (237,'Babydolls',29),
	 (238,'Nightdress',29),
	 (239,'Pyjamas',29),
	 (240,'Nightsuits',29);
INSERT INTO kb_catalog_inventory.brand_models (id,name,brand_id) VALUES
	 (241,'Pyjamas',29),
	 (242,'Camisoles',29),
	 (243,'Thermals Top',29),
	 (244,'Thermals Bottom',29),
	 (245,'Bras',29),
	 (246,'Lingerie Sets',29),
	 (247,'Briefs',29),
	 (248,'Shapewear',29),
	 (249,'Rings for Men',55),
	 (250,'Chains for Men',55);
INSERT INTO kb_catalog_inventory.brand_models (id,name,brand_id) VALUES
	 (251,'Anklets & Toe Rings',55),
	 (252,'Necklaces and Chains',55),
	 (253,'Pendants and Lockets',55),
	 (254,'RIngs',55),
	 (255,'Bangles',55),
	 (256,'Bracelets',55),
	 (257,'Jewellery Set',55),
	 (258,'Ear-rings and Studs',55),
	 (259,'Mangtikas',55),
	 (260,'Mangalsutras',55);
INSERT INTO kb_catalog_inventory.brand_models (id,name,brand_id) VALUES
	 (261,'Nose-rings',55),
	 (262,'Kamarbandhs',55),
	 (263,'Bajuband',55),
	 (264,'Rings for Men',56),
	 (265,'Chains for Men',56),
	 (266,'Anklets & Toe Rings',56),
	 (267,'Necklaces and Chains',56),
	 (268,'Pendants and Lockets',56),
	 (269,'RIngs',56),
	 (270,'Bangles',56);
INSERT INTO kb_catalog_inventory.brand_models (id,name,brand_id) VALUES
	 (271,'Bracelets',56),
	 (272,'Jewellery Set',56),
	 (273,'Ear-rings and Studs',56),
	 (274,'Mangtikas',56),
	 (275,'Mangalsutras',56),
	 (276,'Nose-rings',56),
	 (277,'Kamarbandhs',56),
	 (278,'Bajuband',56),
	 (279,'Rings for Men',57),
	 (280,'Chains for Men',57);
INSERT INTO kb_catalog_inventory.brand_models (id,name,brand_id) VALUES
	 (281,'Anklets & Toe Rings',57),
	 (282,'Necklaces and Chains',57),
	 (283,'Pendants and Lockets',57),
	 (284,'RIngs',57),
	 (285,'Bangles',57),
	 (286,'Bracelets',57),
	 (287,'Jewellery Set',57),
	 (288,'Ear-rings and Studs',57),
	 (289,'Mangtikas',57),
	 (290,'Mangalsutras',57);
INSERT INTO kb_catalog_inventory.brand_models (id,name,brand_id) VALUES
	 (291,'Nose-rings',57),
	 (292,'Kamarbandhs',57),
	 (293,'Bajuband',57);
	 
	 
INSERT INTO kb_catalog_inventory.brand_model_category (id,category_name,name,brand_model_id,category_id) VALUES
	 (1,'T-Shirts','Blue Dyed Crinkled Polo Collar T-shirt',1,12),
	 (2,'T-Shirts','Striped Henley Neck T-shirt',2,12),
	 (3,'T-Shirts','Cotton Solid Polo Collar T-shirt',3,12),
	 (4,'T-Shirts','Solid Pure Cotton Polo Collar T-shirt',4,12),
	 (5,'T-Shirts','Solid Pure Cotton Polo Collar T-shirt',5,12),
	 (6,'T-Shirts','Pure Cotton Logo Embroidered Round Neck T-shirt',6,12),
	 (7,'T-Shirts','Pure Cotton Printed Round Neck T-shirt',7,12),
	 (8,'Casual Shirt','Checked Casual Shirt',8,13),
	 (9,'Casual Shirt','Standard Fit Colourblocked Casual Shirt',9,13),
	 (10,'Casual Shirt','Slim Fit Colourblocked Casual Shirt',10,13);
INSERT INTO kb_catalog_inventory.brand_model_category (id,category_name,name,brand_model_id,category_id) VALUES
	 (11,'Formal Shirt','Classic Regular Fit Solid Formal Shirt',11,14),
	 (12,'Formal Shirt','Pure Cotton Solid Formal Shirt',12,14),
	 (13,'Kurtas & Kurta Sets','Printed Kurta with Churidar',13,19),
	 (14,'Kurtas & Kurta Sets','Printed Straight Kurta',14,19),
	 (15,'Kurtas & Kurta Sets','Solid Straight Kurta',15,19),
	 (16,'Kurtas & Kurta Sets','Printed Kurta with Churidar',16,19),
	 (17,'Kurtas & Kurta Sets','Printed Straight Kurta',17,19),
	 (18,'Kurtas & Kurta Sets','Solid Straight Kurta',18,19),
	 (19,'Kurtas & Kurta Sets','Printed Kurta with Churidar',19,19),
	 (20,'Kurtas & Kurta Sets','Printed Straight Kurta',20,19);
INSERT INTO kb_catalog_inventory.brand_model_category (id,category_name,name,brand_model_id,category_id) VALUES
	 (21,'Kurtas & Kurta Sets','Solid Straight Kurta',21,19),
	 (22,'Kurtas & Kurta Sets','Printed Kurta with Churidar',22,19),
	 (23,'Kurtas & Kurta Sets','Printed Straight Kurta',23,19),
	 (24,'Kurtas & Kurta Sets','Solid Straight Kurta',24,19),
	 (25,'T-Shirts','Blue Dyed Crinkled Polo Collar T-shirt',25,12),
	 (26,'T-Shirts','Striped Henley Neck T-shirt',26,12),
	 (27,'T-Shirts','Cotton Solid Polo Collar T-shirt',27,12),
	 (28,'T-Shirts','Solid Pure Cotton Polo Collar T-shirt',28,12),
	 (29,'T-Shirts','Solid Pure Cotton Polo Collar T-shirt',29,12),
	 (30,'T-Shirts','Pure Cotton Logo Embroidered Round Neck T-shirt',30,12);
INSERT INTO kb_catalog_inventory.brand_model_category (id,category_name,name,brand_model_id,category_id) VALUES
	 (31,'T-Shirts','Pure Cotton Printed Round Neck T-shirt',31,12),
	 (32,'T-Shirts','Blue Dyed Crinkled Polo Collar T-shirt',32,12),
	 (33,'T-Shirts','Striped Henley Neck T-shirt',33,12),
	 (34,'T-Shirts','Cotton Solid Polo Collar T-shirt',34,12),
	 (35,'T-Shirts','Solid Pure Cotton Polo Collar T-shirt',35,12),
	 (36,'T-Shirts','Solid Pure Cotton Polo Collar T-shirt',36,12),
	 (37,'T-Shirts','Pure Cotton Logo Embroidered Round Neck T-shirt',37,12),
	 (38,'T-Shirts','Pure Cotton Printed Round Neck T-shirt',38,12),
	 (39,'T-Shirts','Blue Dyed Crinkled Polo Collar T-shirt',39,12),
	 (40,'T-Shirts','Striped Henley Neck T-shirt',40,12);
INSERT INTO kb_catalog_inventory.brand_model_category (id,category_name,name,brand_model_id,category_id) VALUES
	 (41,'T-Shirts','Cotton Solid Polo Collar T-shirt',41,12),
	 (42,'T-Shirts','Solid Pure Cotton Polo Collar T-shirt',42,12),
	 (43,'T-Shirts','Solid Pure Cotton Polo Collar T-shirt',43,12),
	 (44,'T-Shirts','Pure Cotton Logo Embroidered Round Neck T-shirt',44,12),
	 (45,'T-Shirts','Pure Cotton Printed Round Neck T-shirt',45,12),
	 (46,'Formal Shirt','Classic Regular Fit Solid Formal Shirt',46,14),
	 (47,'Formal Shirt','Pure Cotton Solid Formal Shirt',47,14),
	 (48,'Formal Shirt','Classic Regular Fit Solid Formal Shirt',48,14),
	 (49,'Formal Shirt','Pure Cotton Solid Formal Shirt',49,14),
	 (50,'Formal Shirt','Classic Regular Fit Solid Formal Shirt',50,14);
INSERT INTO kb_catalog_inventory.brand_model_category (id,category_name,name,brand_model_id,category_id) VALUES
	 (51,'Formal Shirt','Pure Cotton Solid Formal Shirt',51,14),
	 (52,'Formal Shirt','Classic Regular Fit Solid Formal Shirt',52,14),
	 (53,'Formal Shirt','Pure Cotton Solid Formal Shirt',53,14),
	 (54,'Formal Shirt','Classic Regular Fit Solid Formal Shirt',54,14),
	 (55,'Formal Shirt','Pure Cotton Solid Formal Shirt',55,14),
	 (56,'Formal Shirt','Classic Regular Fit Solid Formal Shirt',56,14),
	 (57,'Formal Shirt','Pure Cotton Solid Formal Shirt',57,14),
	 (58,'Jeans','Denim Jeans',58,15),
	 (59,'Jeans','Denim Jeans',59,15),
	 (60,'Jeans','Denim Jeans',60,15);
INSERT INTO kb_catalog_inventory.brand_model_category (id,category_name,name,brand_model_id,category_id) VALUES
	 (61,'Jeans','Denim Jeans',61,15),
	 (62,'Casual Trousers','Casual Trousers',62,16),
	 (63,'Casual Trousers','Casual Trousers',63,16),
	 (64,'Casual Trousers','Casual Trousers',64,16),
	 (65,'Casual Trousers','Casual Trousers',65,16),
	 (66,'Casual Trousers','Casual Trousers',66,16),
	 (67,'Casual Trousers','Casual Trousers',67,16),
	 (68,'Casual Trousers','Casual Trousers',68,16),
	 (69,'Casual Trousers','Casual Trousers',69,16),
	 (70,'Casual Trousers','Casual Trousers',70,16);
INSERT INTO kb_catalog_inventory.brand_model_category (id,category_name,name,brand_model_id,category_id) VALUES
	 (71,'Casual Trousers','Casual Trousers',71,16),
	 (72,'Casual Trousers','Casual Trousers',72,16),
	 (73,'Formal Trousers','Formal Trousers',73,17),
	 (74,'Formal Trousers','Formal Trousers',74,17),
	 (75,'Formal Trousers','Formal Trousers',75,17),
	 (76,'Formal Trousers','Formal Trousers',76,17),
	 (77,'Formal Trousers','Formal Trousers',77,17),
	 (78,'Formal Trousers','Formal Trousers',78,17),
	 (79,'Formal Trousers','Formal Trousers',79,17),
	 (80,'Formal Trousers','Formal Trousers',80,17);
INSERT INTO kb_catalog_inventory.brand_model_category (id,category_name,name,brand_model_id,category_id) VALUES
	 (81,'Formal Trousers','Formal Trousers',81,17),
	 (82,'Formal Trousers','Formal Trousers',82,17),
	 (83,'Formal Trousers','Formal Trousers',83,17),
	 (84,'Sherwanis','Sherwanis',84,20),
	 (85,'Briefs','Briefs',85,21),
	 (86,'Briefs','Briefs',86,21),
	 (87,'Briefs','Briefs',87,21),
	 (88,'Briefs','Briefs',88,21),
	 (89,'Briefs','Briefs',89,21),
	 (90,'Boxers','Boxers',90,22);
INSERT INTO kb_catalog_inventory.brand_model_category (id,category_name,name,brand_model_id,category_id) VALUES
	 (91,'Boxers','Boxers',91,22),
	 (92,'Boxers','Boxers',92,22),
	 (93,'Boxers','Boxers',93,22),
	 (94,'Boxers','Boxers',94,22),
	 (95,'Vests','vests',95,23),
	 (96,'Vests','vests',96,23),
	 (97,'Vests','vests',97,23),
	 (98,'Vests','vests',98,23),
	 (99,'Vests','vests',99,23),
	 (100,'Sleepwear ','Sleepwear ',100,24);
INSERT INTO kb_catalog_inventory.brand_model_category (id,category_name,name,brand_model_id,category_id) VALUES
	 (101,'Sleepwear ','Sleepwear ',101,24),
	 (102,'Sleepwear ','Sleepwear ',102,24),
	 (103,'Sleepwear ','Sleepwear ',103,24),
	 (104,'Sleepwear ','Sleepwear ',104,24),
	 (105,'Sports Shoes','Sports Shoes',105,26),
	 (106,'Formal Shoes','Formal Shoes',106,27),
	 (107,'Sneakers','Sneakers',107,28),
	 (108,'Sports Shoes','Sports Shoes',108,26),
	 (109,'Formal Shoes','Formal Shoes',109,27),
	 (110,'Sneakers','Sneakers',110,28);
INSERT INTO kb_catalog_inventory.brand_model_category (id,category_name,name,brand_model_id,category_id) VALUES
	 (111,'Sports Shoes','Sports Shoes',111,26),
	 (112,'Formal Shoes','Formal Shoes',112,27),
	 (113,'Sneakers','Sneakers',113,28),
	 (114,'Wallets','Wallets',114,29),
	 (115,'Belts','Belts',115,30),
	 (116,'Wallets','Wallets',116,29),
	 (117,'Belts','Belts',117,30),
	 (118,'Trimmers','Trimmers',118,32),
	 (119,'Trimmers','Trimmers',119,32),
	 (120,'Trimmers','Trimmers',120,32);
INSERT INTO kb_catalog_inventory.brand_model_category (id,category_name,name,brand_model_id,category_id) VALUES
	 (121,'Perfumes & Body Mists','Perfumes & Body Mists',121,31),
	 (122,'Perfumes & Body Mists','Perfumes & Body Mists',122,31),
	 (123,'Perfumes & Body Mists','Perfumes & Body Mists',123,31),
	 (124,'Perfumes & Body Mists','Perfumes & Body Mists',124,31),
	 (125,'Perfumes & Body Mists','Perfumes & Body Mists',125,31),
	 (126,'Perfumes & Body Mists','Perfumes & Body Mists',126,31),
	 (127,'Deodorants','Deodorants',127,33),
	 (128,'Deodorants','Deodorants',128,33),
	 (129,'Deodorants','Deodorants',129,33),
	 (130,'Deodorants','Deodorants',130,33);
INSERT INTO kb_catalog_inventory.brand_model_category (id,category_name,name,brand_model_id,category_id) VALUES
	 (131,'Deodorants','Deodorants',131,33),
	 (132,'Deodorants','Deodorants',132,33),
	 (133,'Kurtas & Suits','Kurtas & Suits',133,42),
	 (134,'Kurtis, Tunics & Tops','Kurtis, Tunics & Tops',134,43),
	 (135,'Ethnic Wear','Ethnic Wear',135,44),
	 (136,'Leggings, Salwars & Churidars','Leggings, Salwars & Churidars',136,45),
	 (137,'Skirts & Palazzos','Skirts & Palazzos',137,46),
	 (138,'Sarees','Sarees',138,47),
	 (139,'Kurtas & Suits','Kurtas & Suits',139,42),
	 (140,'Kurtis, Tunics & Tops','Kurtis, Tunics & Tops',140,43);
INSERT INTO kb_catalog_inventory.brand_model_category (id,category_name,name,brand_model_id,category_id) VALUES
	 (141,'Ethnic Wear','Ethnic Wear',141,44),
	 (142,'Leggings, Salwars & Churidars','Leggings, Salwars & Churidars',142,45),
	 (143,'Skirts & Palazzos','Skirts & Palazzos',143,46),
	 (144,'Sarees','Sarees',144,47),
	 (145,'Kurtas & Suits','Kurtas & Suits',145,42),
	 (146,'Kurtis, Tunics & Tops','Kurtis, Tunics & Tops',146,43),
	 (147,'Ethnic Wear','Ethnic Wear',147,44),
	 (148,'Leggings, Salwars & Churidars','Leggings, Salwars & Churidars',148,45),
	 (149,'Skirts & Palazzos','Skirts & Palazzos',149,46),
	 (150,'Sarees','Sarees',150,47);
INSERT INTO kb_catalog_inventory.brand_model_category (id,category_name,name,brand_model_id,category_id) VALUES
	 (151,'Kurtas & Suits','Kurtas & Suits',151,42),
	 (152,'Kurtis, Tunics & Tops','Kurtis, Tunics & Tops',152,43),
	 (153,'Ethnic Wear','Ethnic Wear',153,44),
	 (154,'Leggings, Salwars & Churidars','Leggings, Salwars & Churidars',154,45),
	 (155,'Skirts & Palazzos','Skirts & Palazzos',155,46),
	 (156,'Sarees','Sarees',156,47),
	 (157,'Kurtas & Suits','Kurtas & Suits',157,42),
	 (158,'Kurtis, Tunics & Tops','Kurtis, Tunics & Tops',158,43),
	 (159,'Ethnic Wear','Ethnic Wear',159,44),
	 (160,'Leggings, Salwars & Churidars','Leggings, Salwars & Churidars',160,45);
INSERT INTO kb_catalog_inventory.brand_model_category (id,category_name,name,brand_model_id,category_id) VALUES
	 (161,'Skirts & Palazzos','Skirts & Palazzos',161,46),
	 (162,'Sarees','Sarees',162,47),
	 (163,'Kurtas & Suits','Kurtas & Suits',163,42),
	 (164,'Kurtis, Tunics & Tops','Kurtis, Tunics & Tops',164,43),
	 (165,'Ethnic Wear','Ethnic Wear',165,44),
	 (166,'Leggings, Salwars & Churidars','Leggings, Salwars & Churidars',166,45),
	 (167,'Skirts & Palazzos','Skirts & Palazzos',167,46),
	 (168,'Sarees','Sarees',168,47),
	 (169,'Kurtas & Suits','Kurtas & Suits',169,42),
	 (170,'Kurtis, Tunics & Tops','Kurtis, Tunics & Tops',170,43);
INSERT INTO kb_catalog_inventory.brand_model_category (id,category_name,name,brand_model_id,category_id) VALUES
	 (171,'Ethnic Wear','Ethnic Wear',171,44),
	 (172,'Leggings, Salwars & Churidars','Leggings, Salwars & Churidars',172,45),
	 (173,'Skirts & Palazzos','Skirts & Palazzos',173,46),
	 (174,'Sarees','Sarees',174,47),
	 (175,'Kurtas & Suits','Kurtas & Suits',175,42),
	 (176,'Kurtis, Tunics & Tops','Kurtis, Tunics & Tops',176,43),
	 (177,'Ethnic Wear','Ethnic Wear',177,44),
	 (178,'Leggings, Salwars & Churidars','Leggings, Salwars & Churidars',178,45),
	 (179,'Skirts & Palazzos','Skirts & Palazzos',179,46),
	 (180,'Sarees','Sarees',180,47);
INSERT INTO kb_catalog_inventory.brand_model_category (id,category_name,name,brand_model_id,category_id) VALUES
	 (181,'Jumpsuits','Jumpsuits',181,48),
	 (182,'Tops','Tops',182,49),
	 (183,'Trousers & Capris','Trousers & Capris',183,51),
	 (184,'Shorts & Skirts','Shorts & Skirts',184,52),
	 (185,'Jumpsuits','Jumpsuits',185,48),
	 (186,'Tops','Tops',186,49),
	 (187,'Trousers & Capris','Trousers & Capris',187,51),
	 (188,'Shorts & Skirts','Shorts & Skirts',188,52),
	 (189,'Jumpsuits','Jumpsuits',189,48),
	 (190,'Tops','Tops',190,49);
INSERT INTO kb_catalog_inventory.brand_model_category (id,category_name,name,brand_model_id,category_id) VALUES
	 (191,'Trousers & Capris','Trousers & Capris',191,51),
	 (192,'Shorts & Skirts','Shorts & Skirts',192,52),
	 (193,'Flats','Flats',193,53),
	 (194,'Casual Shoes','Casual Shoes',194,54),
	 (195,'Heels','Heels',195,55),
	 (196,'Boots','Boots',196,56),
	 (197,'Flats','Flats',197,53),
	 (198,'Casual Shoes','Casual Shoes',198,54),
	 (199,'Heels','Heels',199,55),
	 (200,'Boots','Boots',200,56);
INSERT INTO kb_catalog_inventory.brand_model_category (id,category_name,name,brand_model_id,category_id) VALUES
	 (201,'Babydolls','Babydolls',201,61),
	 (202,'Nightdress','Nightdress',202,62),
	 (203,'Pyjamas','Pyjamas',203,63),
	 (204,'Nightsuits','Nightsuits',204,64),
	 (205,'Pyjamas','Pyjamas',205,65),
	 (206,'Camisoles','Camisoles',206,66),
	 (207,'Thermals Top','Thermals Top',207,67),
	 (208,'Thermals Bottom','Thermals Bottom',208,68),
	 (209,'Bras','Bras',209,69),
	 (210,'Lingerie Sets','Lingerie Sets',210,70);
INSERT INTO kb_catalog_inventory.brand_model_category (id,category_name,name,brand_model_id,category_id) VALUES
	 (211,'Briefs','Briefs',211,71),
	 (212,'Shapewear','Shapewear',212,72),
	 (213,'Babydolls','Babydolls',213,61),
	 (214,'Nightdress','Nightdress',214,62),
	 (215,'Pyjamas','Pyjamas',215,63),
	 (216,'Nightsuits','Nightsuits',216,64),
	 (217,'Pyjamas','Pyjamas',217,65),
	 (218,'Camisoles','Camisoles',218,66),
	 (219,'Thermals Top','Thermals Top',219,67),
	 (220,'Thermals Bottom','Thermals Bottom',220,68);
INSERT INTO kb_catalog_inventory.brand_model_category (id,category_name,name,brand_model_id,category_id) VALUES
	 (221,'Bras','Bras',221,69),
	 (222,'Lingerie Sets','Lingerie Sets',222,70),
	 (223,'Briefs','Briefs',223,71),
	 (224,'Shapewear','Shapewear',224,72),
	 (225,'Babydolls','Babydolls',225,61),
	 (226,'Nightdress','Nightdress',226,62),
	 (227,'Pyjamas','Pyjamas',227,63),
	 (228,'Nightsuits','Nightsuits',228,64),
	 (229,'Pyjamas','Pyjamas',229,65),
	 (230,'Camisoles','Camisoles',230,66);
INSERT INTO kb_catalog_inventory.brand_model_category (id,category_name,name,brand_model_id,category_id) VALUES
	 (231,'Thermals Top','Thermals Top',231,67),
	 (232,'Thermals Bottom','Thermals Bottom',232,68),
	 (233,'Bras','Bras',233,69),
	 (234,'Lingerie Sets','Lingerie Sets',234,70),
	 (235,'Briefs','Briefs',235,71),
	 (236,'Shapewear','Shapewear',236,72),
	 (237,'Babydolls','Babydolls',237,61),
	 (238,'Nightdress','Nightdress',238,62),
	 (239,'Pyjamas','Pyjamas',239,63),
	 (240,'Nightsuits','Nightsuits',240,64);
INSERT INTO kb_catalog_inventory.brand_model_category (id,category_name,name,brand_model_id,category_id) VALUES
	 (241,'Pyjamas','Pyjamas',241,65),
	 (242,'Camisoles','Camisoles',242,66),
	 (243,'Thermals Top','Thermals Top',243,67),
	 (244,'Thermals Bottom','Thermals Bottom',244,68),
	 (245,'Bras','Bras',245,69),
	 (246,'Lingerie Sets','Lingerie Sets',246,70),
	 (247,'Briefs','Briefs',247,71),
	 (248,'Shapewear','Shapewear',248,72),
	 (249,'Rings for Men','Rings for Men',249,34),
	 (250,'Chains for Men','Chains for Men',250,35);
INSERT INTO kb_catalog_inventory.brand_model_category (id,category_name,name,brand_model_id,category_id) VALUES
	 (251,'Anklets & Toe Rings','Anklets & Toe Rings',251,74),
	 (252,'Necklaces and Chains','Necklaces and Chains',252,75),
	 (253,'Pendants and Lockets','Pendants and Lockets',253,76),
	 (254,'RIngs','RIngs',254,77),
	 (255,'Bangles','Bangles',255,78),
	 (256,'Bracelets','Bracelets',256,79),
	 (257,'Jewellery Set','Jewellery Set',257,80),
	 (258,'Ear-rings and Studs','Ear-rings and Studs',258,81),
	 (259,'Mangtikas','Mangtikas',259,82),
	 (260,'Mangalsutras','Mangalsutras',260,83);
INSERT INTO kb_catalog_inventory.brand_model_category (id,category_name,name,brand_model_id,category_id) VALUES
	 (261,'Nose-rings','Nose-rings',261,84),
	 (262,'Kamarbandhs','Kamarbandhs',262,85),
	 (263,'Bajuband','Bajuband',263,86),
	 (264,'Rings for Men','Rings for Men',264,34),
	 (265,'Chains for Men','Chains for Men',265,35),
	 (266,'Anklets & Toe Rings','Anklets & Toe Rings',266,74),
	 (267,'Necklaces and Chains','Necklaces and Chains',267,75),
	 (268,'Pendants and Lockets','Pendants and Lockets',268,76),
	 (269,'RIngs','RIngs',269,77),
	 (270,'Bangles','Bangles',270,78);
INSERT INTO kb_catalog_inventory.brand_model_category (id,category_name,name,brand_model_id,category_id) VALUES
	 (271,'Bracelets','Bracelets',271,79),
	 (272,'Jewellery Set','Jewellery Set',272,80),
	 (273,'Ear-rings and Studs','Ear-rings and Studs',273,81),
	 (274,'Mangtikas','Mangtikas',274,82),
	 (275,'Mangalsutras','Mangalsutras',275,83),
	 (276,'Nose-rings','Nose-rings',276,84),
	 (277,'Kamarbandhs','Kamarbandhs',277,85),
	 (278,'Bajuband','Bajuband',278,86),
	 (279,'Rings for Men','Rings for Men',279,34),
	 (280,'Chains for Men','Chains for Men',280,35);
INSERT INTO kb_catalog_inventory.brand_model_category (id,category_name,name,brand_model_id,category_id) VALUES
	 (281,'Anklets & Toe Rings','Anklets & Toe Rings',281,74),
	 (282,'Necklaces and Chains','Necklaces and Chains',282,75),
	 (283,'Pendants and Lockets','Pendants and Lockets',283,76),
	 (284,'RIngs','RIngs',284,77),
	 (285,'Bangles','Bangles',285,78),
	 (286,'Bracelets','Bracelets',286,79),
	 (287,'Jewellery Set','Jewellery Set',287,80),
	 (288,'Ear-rings and Studs','Ear-rings and Studs',288,81),
	 (289,'Mangtikas','Mangtikas',289,82),
	 (290,'Mangalsutras','Mangalsutras',290,83);
INSERT INTO kb_catalog_inventory.brand_model_category (id,category_name,name,brand_model_id,category_id) VALUES
	 (291,'Nose-rings','Nose-rings',291,84),
	 (292,'Kamarbandhs','Kamarbandhs',292,85),
	 (293,'Bajuband','Bajuband',293,86);




INSERT INTO kb_catalog_inventory.supplier (id,gst_or_udyog_number,supplier_address,supplier_name) VALUES
	 (1,'123456','xyz','abc');




INSERT INTO kb_catalog_inventory.product (id,country_id,preview_image,product_name,product_string,updated_on,brand_model_category_id,supplier_id,UUID) VALUES
	 (1,1,NULL,'Blue Dyed Crinkled Polo Collar T-shirt',NULL,NULL,1,1,'1c6735f6-895a-4730-8126-2dd42887d83a'),
	 (2,1,NULL,'Striped Henley Neck T-shirt',NULL,NULL,2,1,'ba5d1e56-2f34-48d8-aa33-e3ef3caa2042'),
	 (3,1,NULL,'Cotton Solid Polo Collar T-shirt',NULL,NULL,3,1,'0d260c47-cad8-45fa-ba14-17b7702308cc'),
	 (4,1,NULL,'Solid Pure Cotton Polo Collar T-shirt',NULL,NULL,4,1,'ee0f7184-bec4-4534-a9a7-6c03adad787c'),
	 (5,1,NULL,'Solid Pure Cotton Polo Collar T-shirt',NULL,NULL,5,1,'6d286ae1-d404-4514-9f2f-c7fadb54f38b'),
	 (6,1,NULL,'Pure Cotton Logo Embroidered Round Neck T-shirt',NULL,NULL,6,1,'9fa5fe78-41e1-41bc-b435-7e49d6d75a88'),
	 (7,1,NULL,'Pure Cotton Printed Round Neck T-shirt',NULL,NULL,7,1,'4763bc99-09fd-4848-a82d-293c5ad39cfd'),
	 (8,1,NULL,'Checked Casual Shirt',NULL,NULL,8,1,'eb30d45a-d170-4534-91e5-b6bea39ab0e8'),
	 (9,1,NULL,'Standard Fit Colourblocked Casual Shirt',NULL,NULL,9,1,'0acfb990-0634-4895-9d97-f9ce221be819'),
	 (10,1,NULL,'Slim Fit Colourblocked Casual Shirt',NULL,NULL,10,1,'e73a13b9-72a9-411c-b134-d9c661f538e4');
INSERT INTO kb_catalog_inventory.product (id,country_id,preview_image,product_name,product_string,updated_on,brand_model_category_id,supplier_id,UUID) VALUES
	 (11,1,NULL,'Classic Regular Fit Solid Formal Shirt',NULL,NULL,11,1,'427faf5a-ca75-460b-9f69-8dcb6ded5c6d'),
	 (12,1,NULL,'Pure Cotton Solid Formal Shirt',NULL,NULL,12,1,'c8f0f72d-58d8-4cfb-8ac3-ce883c59ec1b'),
	 (13,1,NULL,'Printed Kurta with Churidar',NULL,NULL,13,1,'b8238156-6d49-4400-9f44-070a6de256f2'),
	 (14,1,NULL,'Printed Straight Kurta',NULL,NULL,14,1,'63525df6-5154-41a5-92fc-7b61e87f1ebd'),
	 (15,1,NULL,'Solid Straight Kurta',NULL,NULL,15,1,'cbd73365-43b0-4237-a802-cab455e3a71b'),
	 (16,1,NULL,'Printed Kurta with Churidar',NULL,NULL,16,1,'fff1aef2-63aa-4509-9f70-fcd4458505d1'),
	 (17,1,NULL,'Printed Straight Kurta',NULL,NULL,17,1,'ed086308-e8f8-4482-96ad-22e7a4177cf0'),
	 (18,1,NULL,'Solid Straight Kurta',NULL,NULL,18,1,'cedd77c2-cf27-4b8c-a4fb-0a79a8b0b5d0'),
	 (19,1,NULL,'Printed Kurta with Churidar',NULL,NULL,19,1,'79c42b1e-a18c-425d-acea-3b39f8a3c7f7'),
	 (20,1,NULL,'Printed Straight Kurta',NULL,NULL,20,1,'ba927662-fa47-4f64-93d9-06499e20a548');
INSERT INTO kb_catalog_inventory.product (id,country_id,preview_image,product_name,product_string,updated_on,brand_model_category_id,supplier_id,UUID) VALUES
	 (21,1,NULL,'Solid Straight Kurta',NULL,NULL,21,1,'60472ea0-5e00-4af5-b427-e7023f786412'),
	 (22,1,NULL,'Printed Kurta with Churidar',NULL,NULL,22,1,'3b8c58d3-15eb-4e74-a265-be1786e25a86'),
	 (23,1,NULL,'Printed Straight Kurta',NULL,NULL,23,1,'19ab2652-d16a-48a3-987e-a0a0cf3c93cb'),
	 (24,1,NULL,'Solid Straight Kurta',NULL,NULL,24,1,'45ff6ab5-037c-4013-9595-62e627b3b694'),
	 (25,1,NULL,'Blue Dyed Crinkled Polo Collar T-shirt',NULL,NULL,25,1,'5751b9da-5c8f-4eac-b375-02859a588ec5'),
	 (26,1,NULL,'Striped Henley Neck T-shirt',NULL,NULL,26,1,'aceea29b-7f9b-4f23-a09c-955273973cbc'),
	 (27,1,NULL,'Cotton Solid Polo Collar T-shirt',NULL,NULL,27,1,'88dfec4a-0865-41f0-95f8-bac3f12c3cb9'),
	 (28,1,NULL,'Solid Pure Cotton Polo Collar T-shirt',NULL,NULL,28,1,'94810caf-1d31-4c8d-8f34-e81d0a94ec0b'),
	 (29,1,NULL,'Solid Pure Cotton Polo Collar T-shirt',NULL,NULL,29,1,'32591521-6b0a-496f-a552-c0f76e10b1fa'),
	 (30,1,NULL,'Pure Cotton Logo Embroidered Round Neck T-shirt',NULL,NULL,30,1,'f043bd5c-5de2-485c-9873-e000d6468416');
INSERT INTO kb_catalog_inventory.product (id,country_id,preview_image,product_name,product_string,updated_on,brand_model_category_id,supplier_id,UUID) VALUES
	 (31,1,NULL,'Pure Cotton Printed Round Neck T-shirt',NULL,NULL,31,1,'2ccc5714-ea1a-4c78-b56c-5768336a489d'),
	 (32,1,NULL,'Blue Dyed Crinkled Polo Collar T-shirt',NULL,NULL,32,1,'5ff7f6c1-64b6-4f99-b7d1-fac47fc8ff9f'),
	 (33,1,NULL,'Striped Henley Neck T-shirt',NULL,NULL,33,1,'060943c7-ea89-4310-a137-202659f40743'),
	 (34,1,NULL,'Cotton Solid Polo Collar T-shirt',NULL,NULL,34,1,'8f94d955-4264-4351-b519-8981c76e7037'),
	 (35,1,NULL,'Solid Pure Cotton Polo Collar T-shirt',NULL,NULL,35,1,'856d3484-4a99-40ba-a4e3-e55b0a5040fb'),
	 (36,1,NULL,'Solid Pure Cotton Polo Collar T-shirt',NULL,NULL,36,1,'c7cb6970-6d72-415f-b384-6bc7c207ec82'),
	 (37,1,NULL,'Pure Cotton Logo Embroidered Round Neck T-shirt',NULL,NULL,37,1,'25800450-067b-4c50-8fec-c19b53b0c5f8'),
	 (38,1,NULL,'Pure Cotton Printed Round Neck T-shirt',NULL,NULL,38,1,'03f54198-8094-497e-9b00-fd96da31dd3a'),
	 (39,1,NULL,'Blue Dyed Crinkled Polo Collar T-shirt',NULL,NULL,39,1,'532c92ba-0b52-4984-a501-cab8251b1c5d'),
	 (40,1,NULL,'Striped Henley Neck T-shirt',NULL,NULL,40,1,'dc12d9c9-53a3-48cf-8fa4-817a3d106b42');
INSERT INTO kb_catalog_inventory.product (id,country_id,preview_image,product_name,product_string,updated_on,brand_model_category_id,supplier_id,UUID) VALUES
	 (41,1,NULL,'Cotton Solid Polo Collar T-shirt',NULL,NULL,41,1,'87bbc330-314b-4229-968a-2205b89d4b76'),
	 (42,1,NULL,'Solid Pure Cotton Polo Collar T-shirt',NULL,NULL,42,1,'7a55e419-16c5-434c-a4b0-40d076fffc0c'),
	 (43,1,NULL,'Solid Pure Cotton Polo Collar T-shirt',NULL,NULL,43,1,'81cfd9a2-dbe6-4f63-8b29-9eb562ff0095'),
	 (44,1,NULL,'Pure Cotton Logo Embroidered Round Neck T-shirt',NULL,NULL,44,1,'3fc64c7b-e427-41cd-9e7d-d0107c993173'),
	 (45,1,NULL,'Pure Cotton Printed Round Neck T-shirt',NULL,NULL,45,1,'16352823-0886-4bde-8651-b41430045359'),
	 (46,1,NULL,'Classic Regular Fit Solid Formal Shirt',NULL,NULL,46,1,'65107adf-3cd1-4418-833c-df65da5d4649'),
	 (47,1,NULL,'Pure Cotton Solid Formal Shirt',NULL,NULL,47,1,'fe941e8f-e297-46eb-a00b-8b49248d73b8'),
	 (48,1,NULL,'Classic Regular Fit Solid Formal Shirt',NULL,NULL,48,1,'cdb838d9-be94-4cef-b1ff-b320e5cbe125'),
	 (49,1,NULL,'Pure Cotton Solid Formal Shirt',NULL,NULL,49,1,'9929f089-280d-4e88-b21a-79d3cdc3017c'),
	 (50,1,NULL,'Classic Regular Fit Solid Formal Shirt',NULL,NULL,50,1,'16fbc39d-9615-4e50-95fe-2c0449f6ea08');
INSERT INTO kb_catalog_inventory.product (id,country_id,preview_image,product_name,product_string,updated_on,brand_model_category_id,supplier_id,UUID) VALUES
	 (51,1,NULL,'Pure Cotton Solid Formal Shirt',NULL,NULL,51,1,'88ca2ab0-ef62-4f60-bf99-ae803bebac3b'),
	 (52,1,NULL,'Classic Regular Fit Solid Formal Shirt',NULL,NULL,52,1,'5f5fe87e-eb2f-4f1e-b41f-9c7cf56bc5d6'),
	 (53,1,NULL,'Pure Cotton Solid Formal Shirt',NULL,NULL,53,1,'38411204-257b-4c22-a20a-f85e6a935f89'),
	 (54,1,NULL,'Classic Regular Fit Solid Formal Shirt',NULL,NULL,54,1,'6a282f57-15c2-4f2c-a16a-b26fd4a52d68'),
	 (55,1,NULL,'Pure Cotton Solid Formal Shirt',NULL,NULL,55,1,'76def48e-fe5d-4f35-bb17-9ede1947b674'),
	 (56,1,NULL,'Classic Regular Fit Solid Formal Shirt',NULL,NULL,56,1,'904fbedc-e94d-47ec-a3aa-c1a1a663f4f1'),
	 (57,1,NULL,'Pure Cotton Solid Formal Shirt',NULL,NULL,57,1,'d1328ce4-0e41-4e5f-8147-016edde12a28'),
	 (58,1,NULL,'Denim Jeans',NULL,NULL,58,1,'fb258f44-7f58-459c-82c8-73fbc4e6a822'),
	 (59,1,NULL,'Denim Jeans',NULL,NULL,59,1,'9fe7dc71-734d-490a-995b-5acea199b645'),
	 (60,1,NULL,'Denim Jeans',NULL,NULL,60,1,'bf88f895-8d42-4f46-adcd-bf64ff579a08');
INSERT INTO kb_catalog_inventory.product (id,country_id,preview_image,product_name,product_string,updated_on,brand_model_category_id,supplier_id,UUID) VALUES
	 (61,1,NULL,'Denim Jeans',NULL,NULL,61,1,'1efc29d4-9201-41a3-bfc4-ccf02da159b3'),
	 (62,1,NULL,'Casual Trousers',NULL,NULL,62,1,'88bab993-ef0b-4498-8b01-c3a4da997573'),
	 (63,1,NULL,'Casual Trousers',NULL,NULL,63,1,'15b7c59a-8877-4e45-9a75-d0eb709468bb'),
	 (64,1,NULL,'Casual Trousers',NULL,NULL,64,1,'db36b30a-9a5c-416c-bda9-e8cf8085c30e'),
	 (65,1,NULL,'Casual Trousers',NULL,NULL,65,1,'3f56a64c-8edf-45c3-bf7b-08f3eb8f8475'),
	 (66,1,NULL,'Casual Trousers',NULL,NULL,66,1,'2d0592e0-4124-4476-9576-bd8d6c6123b6'),
	 (67,1,NULL,'Casual Trousers',NULL,NULL,67,1,'e0feb2ac-6c28-4784-867a-ddc54b761bfb'),
	 (68,1,NULL,'Casual Trousers',NULL,NULL,68,1,'a04c943b-a261-46f7-abdf-eaf7007ef72d'),
	 (69,1,NULL,'Casual Trousers',NULL,NULL,69,1,'9d877f9d-2f59-430c-be25-47f9120409ab'),
	 (70,1,NULL,'Casual Trousers',NULL,NULL,70,1,'e66266c8-3a40-4879-bfcc-12021b70aade');
INSERT INTO kb_catalog_inventory.product (id,country_id,preview_image,product_name,product_string,updated_on,brand_model_category_id,supplier_id,UUID) VALUES
	 (71,1,NULL,'Casual Trousers',NULL,NULL,71,1,'a46861b4-956e-4719-835a-68f876e1081b'),
	 (72,1,NULL,'Casual Trousers',NULL,NULL,72,1,'d88c6ec0-7aae-4528-baee-4a705d7ee45c'),
	 (73,1,NULL,'Formal Trousers',NULL,NULL,73,1,'fc602250-97fd-4ea0-bbf6-6f6aeb0987cb'),
	 (74,1,NULL,'Formal Trousers',NULL,NULL,74,1,'c819c46a-d6c1-4723-81e6-a53e3c610cfa'),
	 (75,1,NULL,'Formal Trousers',NULL,NULL,75,1,'91238a2d-1211-417d-8229-7cc804e38d02'),
	 (76,1,NULL,'Formal Trousers',NULL,NULL,76,1,'c83760ef-c40c-4494-b839-7525e91e6e62'),
	 (77,1,NULL,'Formal Trousers',NULL,NULL,77,1,'b87dd8c7-8b1d-4094-bf27-881dad9df468'),
	 (78,1,NULL,'Formal Trousers',NULL,NULL,78,1,'e8d1e6e0-7ea4-414b-9df1-5cf0edafecc1'),
	 (79,1,NULL,'Formal Trousers',NULL,NULL,79,1,'a97ba179-9057-42e5-9fde-fd0c02731024'),
	 (80,1,NULL,'Formal Trousers',NULL,NULL,80,1,'4794ed78-56c8-47cb-af4f-ec59eb939fb9');
INSERT INTO kb_catalog_inventory.product (id,country_id,preview_image,product_name,product_string,updated_on,brand_model_category_id,supplier_id,UUID) VALUES
	 (81,1,NULL,'Formal Trousers',NULL,NULL,81,1,'742d8f91-d6b4-4d3f-b0c1-ae5941650d65'),
	 (82,1,NULL,'Formal Trousers',NULL,NULL,82,1,'013eeb70-88bc-4933-b993-b5ccb5f9afdf'),
	 (83,1,NULL,'Formal Trousers',NULL,NULL,83,1,'f9e60f8a-e5b8-4f1b-9922-b1949a8dedcd'),
	 (84,1,NULL,'Sherwanis',NULL,NULL,84,1,'d4ff3c0b-f734-480b-96a1-fd71b3faa8f1'),
	 (85,1,NULL,'Briefs',NULL,NULL,85,1,'8ca1c983-152e-4011-bed7-2b82cd5c27cf'),
	 (86,1,NULL,'Briefs',NULL,NULL,86,1,'e1ea1fca-3863-464c-95a1-5c8f9cf144c3'),
	 (87,1,NULL,'Briefs',NULL,NULL,87,1,'0caf2ab0-7d5a-4806-ba0c-e1dc234a7496'),
	 (88,1,NULL,'Briefs',NULL,NULL,88,1,'5fdd5ab5-675d-4530-8e0d-7f2e9f283929'),
	 (89,1,NULL,'Briefs',NULL,NULL,89,1,'0fc31f9a-8b1f-4b6b-8de1-1ffd8aa891e4'),
	 (90,1,NULL,'Boxers',NULL,NULL,90,1,'f24999fe-c267-4e8a-8f83-5028fcb8abb2');
INSERT INTO kb_catalog_inventory.product (id,country_id,preview_image,product_name,product_string,updated_on,brand_model_category_id,supplier_id,UUID) VALUES
	 (91,1,NULL,'Boxers',NULL,NULL,91,1,'2a81575f-397d-41d3-be0f-721e389a1bf8'),
	 (92,1,NULL,'Boxers',NULL,NULL,92,1,'6b20090a-0e23-4b5a-b822-06cf8c05f049'),
	 (93,1,NULL,'Boxers',NULL,NULL,93,1,'d79b978e-68ab-4169-ad44-f9ca7c427a43'),
	 (94,1,NULL,'Boxers',NULL,NULL,94,1,'8f9793fb-dd16-4068-a22a-53fe4b26939b'),
	 (95,1,NULL,'vests',NULL,NULL,95,1,'7da2fe51-645b-4380-a9a5-200f74327fe7'),
	 (96,1,NULL,'vests',NULL,NULL,96,1,'e147e0f4-2520-4e44-82f5-328fcb7881d7'),
	 (97,1,NULL,'vests',NULL,NULL,97,1,'cc0b20f1-899f-413f-8dc8-b94e89c450c9'),
	 (98,1,NULL,'vests',NULL,NULL,98,1,'d19c6599-9a47-463b-9ebf-04b1dcedbdd2'),
	 (99,1,NULL,'vests',NULL,NULL,99,1,'bfe73dbd-07d8-49cc-863a-f5cdf158c30c'),
	 (100,1,NULL,'Sleepwear ',NULL,NULL,100,1,'ac703172-9548-4d92-9b57-f73a553e5b74');
INSERT INTO kb_catalog_inventory.product (id,country_id,preview_image,product_name,product_string,updated_on,brand_model_category_id,supplier_id,UUID) VALUES
	 (101,1,NULL,'Sleepwear ',NULL,NULL,101,1,'263fe909-6faf-4a83-be0a-722a4728408e'),
	 (102,1,NULL,'Sleepwear ',NULL,NULL,102,1,'a726fc35-3382-454f-b664-106dfcccdd45'),
	 (103,1,NULL,'Sleepwear ',NULL,NULL,103,1,'b3751ade-c30a-49e2-a043-6be38cd3afc5'),
	 (104,1,NULL,'Sleepwear ',NULL,NULL,104,1,'9e741012-6205-4876-ac34-4f8505a3fc8b'),
	 (105,1,NULL,'Sports Shoes',NULL,NULL,105,1,'4a44a640-4c21-4d93-8baa-98838cd39cc3'),
	 (106,1,NULL,'Formal Shoes',NULL,NULL,106,1,'f1119b90-00db-4dcb-81cd-0c27ec3fab6e'),
	 (107,1,NULL,'Sneakers',NULL,NULL,107,1,'3531960a-6e35-471b-bb58-e5416f642c41'),
	 (108,1,NULL,'Sports Shoes',NULL,NULL,108,1,'4b84a904-b0de-4084-9848-049fc679b924'),
	 (109,1,NULL,'Formal Shoes',NULL,NULL,109,1,'d2e98c2b-fc65-4c97-ad62-30a6c7c8b1f3'),
	 (110,1,NULL,'Sneakers',NULL,NULL,110,1,'59e67f83-7edd-44a5-bb65-3d88c22e255b');
INSERT INTO kb_catalog_inventory.product (id,country_id,preview_image,product_name,product_string,updated_on,brand_model_category_id,supplier_id,UUID) VALUES
	 (111,1,NULL,'Sports Shoes',NULL,NULL,111,1,'36d5e411-d82c-45ca-a4af-6d1371931cd5'),
	 (112,1,NULL,'Formal Shoes',NULL,NULL,112,1,'ac4d7c13-99dd-492f-b4d4-1a709bdc953d'),
	 (113,1,NULL,'Sneakers',NULL,NULL,113,1,'8138300f-d023-4110-bc8b-69d2400c258f'),
	 (114,1,NULL,'Wallets',NULL,NULL,114,1,'8ad9ff32-f789-4f26-a565-cfd82a8d3eeb'),
	 (115,1,NULL,'Belts',NULL,NULL,115,1,'292d725f-49bf-4b8a-b065-d780abff118c'),
	 (116,1,NULL,'Wallets',NULL,NULL,116,1,'585822ce-6ea2-4fdd-b2dd-75c6b0bbb0b9'),
	 (117,1,NULL,'Belts',NULL,NULL,117,1,'345e4a6a-e6e5-405d-825a-90cd4d06d231'),
	 (118,1,NULL,'Trimmers',NULL,NULL,118,1,'9bb9179d-8977-43d6-ba5c-62b8a9ef4e34'),
	 (119,1,NULL,'Trimmers',NULL,NULL,119,1,'4b546fee-496d-4668-a280-1f5d2983d2bf'),
	 (120,1,NULL,'Trimmers',NULL,NULL,120,1,'354ddba5-0de5-4c24-ad34-1f8a36fe7e33');
INSERT INTO kb_catalog_inventory.product (id,country_id,preview_image,product_name,product_string,updated_on,brand_model_category_id,supplier_id,UUID) VALUES
	 (121,1,NULL,'Perfumes & Body Mists',NULL,NULL,121,1,'26402a08-28d5-4514-b7fb-ce387a9de0e8'),
	 (122,1,NULL,'Perfumes & Body Mists',NULL,NULL,122,1,'e4be4833-0580-445b-9586-233d2c743b91'),
	 (123,1,NULL,'Perfumes & Body Mists',NULL,NULL,123,1,'f5b648cb-4e45-4128-988f-a773cfc57c6a'),
	 (124,1,NULL,'Perfumes & Body Mists',NULL,NULL,124,1,'401a3503-be8b-43c7-9308-50a6b6908c14'),
	 (125,1,NULL,'Perfumes & Body Mists',NULL,NULL,125,1,'eb8bf27f-d719-42a6-aa65-17789440e7da'),
	 (126,1,NULL,'Perfumes & Body Mists',NULL,NULL,126,1,'9b1c801e-a1fe-4f0f-a06b-1d264ca4c690'),
	 (127,1,NULL,'Deodorants',NULL,NULL,127,1,'84b94d22-c6b1-4c8a-b504-f583ae79eea2'),
	 (128,1,NULL,'Deodorants',NULL,NULL,128,1,'62119cc0-3451-4b00-815c-e3e6ae71bea8'),
	 (129,1,NULL,'Deodorants',NULL,NULL,129,1,'3c216e49-423a-43df-881f-7126e2e37ebc'),
	 (130,1,NULL,'Deodorants',NULL,NULL,130,1,'d2a5e908-46b9-4fc8-bd76-ba696a8b8fa8');
INSERT INTO kb_catalog_inventory.product (id,country_id,preview_image,product_name,product_string,updated_on,brand_model_category_id,supplier_id,UUID) VALUES
	 (131,1,NULL,'Deodorants',NULL,NULL,131,1,'cbbbf5b5-67bc-4f3d-92b2-501eed689ddc'),
	 (132,1,NULL,'Deodorants',NULL,NULL,132,1,'88b75daa-7e7f-4bf8-aabe-8e54c72d64ff'),
	 (133,1,NULL,'Kurtas & Suits',NULL,NULL,133,1,'b8b48726-825f-45b1-84d0-ac23dcf71dc2'),
	 (134,1,NULL,'Kurtis, Tunics & Tops',NULL,NULL,134,1,'1ad1c5f4-83cc-4ad7-bdb9-af53d2802190'),
	 (135,1,NULL,'Ethnic Wear',NULL,NULL,135,1,'4fae937e-c9f8-4739-84dd-12c0578fb8cc'),
	 (136,1,NULL,'Leggings, Salwars & Churidars',NULL,NULL,136,1,'3cc76ffc-c0aa-41b2-bdb8-d0d0b3dc61ba'),
	 (137,1,NULL,'Skirts & Palazzos',NULL,NULL,137,1,'de82bdfb-3d9f-4620-a3bb-10f97b8ee477'),
	 (138,1,NULL,'Sarees',NULL,NULL,138,1,'3a84827f-9cbf-416e-8f7b-32fe434320b1'),
	 (139,1,NULL,'Kurtas & Suits',NULL,NULL,139,1,'a449aac3-8964-4c37-94e4-7ae79e951caa'),
	 (140,1,NULL,'Kurtis, Tunics & Tops',NULL,NULL,140,1,'edcd3061-3da6-46c5-93f4-cce4aae72d1b');
INSERT INTO kb_catalog_inventory.product (id,country_id,preview_image,product_name,product_string,updated_on,brand_model_category_id,supplier_id,UUID) VALUES
	 (141,1,NULL,'Ethnic Wear',NULL,NULL,141,1,'942c153d-548d-4cf3-ae52-80439d8e3a6a'),
	 (142,1,NULL,'Leggings, Salwars & Churidars',NULL,NULL,142,1,'f03c05f1-770a-49e7-a48e-9457cca09d00'),
	 (143,1,NULL,'Skirts & Palazzos',NULL,NULL,143,1,'59d1ad14-bea0-42e8-99d0-555b4b3424e9'),
	 (144,1,NULL,'Sarees',NULL,NULL,144,1,'c1d9aca7-f3d1-4d0f-a977-5eb88782053c'),
	 (145,1,NULL,'Kurtas & Suits',NULL,NULL,145,1,'410336e3-9306-48da-b1bf-52bf0b8ba4fd'),
	 (146,1,NULL,'Kurtis, Tunics & Tops',NULL,NULL,146,1,'ac4a7f1e-efc9-47c8-822d-423b49dfe171'),
	 (147,1,NULL,'Ethnic Wear',NULL,NULL,147,1,'bd457480-35eb-49da-b0ce-ed2ec5d19c85'),
	 (148,1,NULL,'Leggings, Salwars & Churidars',NULL,NULL,148,1,'d35180d9-fb8a-48ac-9067-b820720215dc'),
	 (149,1,NULL,'Skirts & Palazzos',NULL,NULL,149,1,'310bb472-c576-4c72-a8e7-d7c3a58ac1a3'),
	 (150,1,NULL,'Sarees',NULL,NULL,150,1,'cb1f58f6-3d50-4b74-8876-f4c816b8ed0d');
INSERT INTO kb_catalog_inventory.product (id,country_id,preview_image,product_name,product_string,updated_on,brand_model_category_id,supplier_id,UUID) VALUES
	 (151,1,NULL,'Kurtas & Suits',NULL,NULL,151,1,'f76b013d-314c-428a-b50d-c046181a2269'),
	 (152,1,NULL,'Kurtis, Tunics & Tops',NULL,NULL,152,1,'982639cf-23df-49e2-b529-101417666a27'),
	 (153,1,NULL,'Ethnic Wear',NULL,NULL,153,1,'5f66f957-84c7-4be6-b9ab-f5ed0348a879'),
	 (154,1,NULL,'Leggings, Salwars & Churidars',NULL,NULL,154,1,'3985b0d4-3780-4df9-8f8a-c4a58fbe8672'),
	 (155,1,NULL,'Skirts & Palazzos',NULL,NULL,155,1,'e31cfb43-bd33-4d27-8075-981fe9a11ab3'),
	 (156,1,NULL,'Sarees',NULL,NULL,156,1,'93bf49f3-821b-4059-b959-97fd44e978b0'),
	 (157,1,NULL,'Kurtas & Suits',NULL,NULL,157,1,'0f7d355c-98da-4a40-b619-3cdedac2211a'),
	 (158,1,NULL,'Kurtis, Tunics & Tops',NULL,NULL,158,1,'1363d164-1aea-4f60-82b6-cb87ebd18167'),
	 (159,1,NULL,'Ethnic Wear',NULL,NULL,159,1,'f235288f-9aa6-46bb-ab9b-6232396dd6d3'),
	 (160,1,NULL,'Leggings, Salwars & Churidars',NULL,NULL,160,1,'213b55c9-5b69-4b8b-b712-9abbeeabaaef');
INSERT INTO kb_catalog_inventory.product (id,country_id,preview_image,product_name,product_string,updated_on,brand_model_category_id,supplier_id,UUID) VALUES
	 (161,1,NULL,'Skirts & Palazzos',NULL,NULL,161,1,'0c8e3610-b116-4c11-afca-6e0bb9d24b4d'),
	 (162,1,NULL,'Sarees',NULL,NULL,162,1,'38c50025-fcdb-4874-bf83-09ee19ccfeca'),
	 (163,1,NULL,'Kurtas & Suits',NULL,NULL,163,1,'5f7d5581-6ddc-49f8-ab3e-b9208a9a7f68'),
	 (164,1,NULL,'Kurtis, Tunics & Tops',NULL,NULL,164,1,'ba1de45f-5ac7-401d-9527-07f9a78295c3'),
	 (165,1,NULL,'Ethnic Wear',NULL,NULL,165,1,'039887d8-f1f9-480c-9388-89e93ef71fe4'),
	 (166,1,NULL,'Leggings, Salwars & Churidars',NULL,NULL,166,1,'1dd2d396-ce70-422f-8fc4-a8b0b2916fd6'),
	 (167,1,NULL,'Skirts & Palazzos',NULL,NULL,167,1,'9766b8bd-46fa-4362-a4da-05e64059e03c'),
	 (168,1,NULL,'Sarees',NULL,NULL,168,1,'db78ff07-322b-430d-867a-f1bb2fea403f'),
	 (169,1,NULL,'Kurtas & Suits',NULL,NULL,169,1,'f0dc6744-221a-4b5d-a5e2-7e8c6882207e'),
	 (170,1,NULL,'Kurtis, Tunics & Tops',NULL,NULL,170,1,'78237c01-b454-4075-8099-1a26c3e23109');
INSERT INTO kb_catalog_inventory.product (id,country_id,preview_image,product_name,product_string,updated_on,brand_model_category_id,supplier_id,UUID) VALUES
	 (171,1,NULL,'Ethnic Wear',NULL,NULL,171,1,'2e63abb6-67bc-4b4a-9a8a-dac6d1cbfead'),
	 (172,1,NULL,'Leggings, Salwars & Churidars',NULL,NULL,172,1,'e16cf2d2-5d7d-4bd4-b4d7-718750f10f2f'),
	 (173,1,NULL,'Skirts & Palazzos',NULL,NULL,173,1,'f8d86b7d-052b-41e1-8696-d74a055fff20'),
	 (174,1,NULL,'Sarees',NULL,NULL,174,1,'0e070afd-67a1-4df7-ad15-c7415a854a03'),
	 (175,1,NULL,'Kurtas & Suits',NULL,NULL,175,1,'c8729221-7f2f-49fe-bd50-fddb9219500c'),
	 (176,1,NULL,'Kurtis, Tunics & Tops',NULL,NULL,176,1,'aeca7303-1278-4a7b-ae01-e0430c037c07'),
	 (177,1,NULL,'Ethnic Wear',NULL,NULL,177,1,'f2115919-6629-4bfc-b74b-84e9bdb440aa'),
	 (178,1,NULL,'Leggings, Salwars & Churidars',NULL,NULL,178,1,'284a652e-c3af-47b7-9e3e-a7f7ba435e7c'),
	 (179,1,NULL,'Skirts & Palazzos',NULL,NULL,179,1,'d2dc2f2d-c138-4b45-91ff-8f9e78f2f6ee'),
	 (180,1,NULL,'Sarees',NULL,NULL,180,1,'6cbb3f40-fe27-4ebd-bbbb-aa435bd8a526');
INSERT INTO kb_catalog_inventory.product (id,country_id,preview_image,product_name,product_string,updated_on,brand_model_category_id,supplier_id,UUID) VALUES
	 (181,1,NULL,'Jumpsuits',NULL,NULL,181,1,'cf8f57a5-67af-40f8-8595-f4f56c5af9e7'),
	 (182,1,NULL,'Tops',NULL,NULL,182,1,'105ee0dd-738e-40cf-b8ca-be2caa60a188'),
	 (183,1,NULL,'Trousers & Capris',NULL,NULL,183,1,'16037aad-ec34-404a-85cf-e353d827ad85'),
	 (184,1,NULL,'Shorts & Skirts',NULL,NULL,184,1,'b932e77a-61d1-4417-b05c-b5c6393d56a6'),
	 (185,1,NULL,'Jumpsuits',NULL,NULL,185,1,'57193f32-bd83-423b-8f2c-27f2867e99d1'),
	 (186,1,NULL,'Tops',NULL,NULL,186,1,'a5f096ab-4316-42f6-bf8b-32cef1ef2129'),
	 (187,1,NULL,'Trousers & Capris',NULL,NULL,187,1,'ba6ed96d-4e12-4b8a-8fe3-6de6b51b241f'),
	 (188,1,NULL,'Shorts & Skirts',NULL,NULL,188,1,'5642c512-1890-4ee5-a4b3-3823ea9fda42'),
	 (189,1,NULL,'Jumpsuits',NULL,NULL,189,1,'f35389e9-d680-4f9f-a176-b0499007f5f7'),
	 (190,1,NULL,'Tops',NULL,NULL,190,1,'dc0f6bde-693e-4b7b-9eca-162ec9e125d7');
INSERT INTO kb_catalog_inventory.product (id,country_id,preview_image,product_name,product_string,updated_on,brand_model_category_id,supplier_id,UUID) VALUES
	 (191,1,NULL,'Trousers & Capris',NULL,NULL,191,1,'e19fa096-674e-4112-bfd5-0e9c0da30dc2'),
	 (192,1,NULL,'Shorts & Skirts',NULL,NULL,192,1,'6276b4b1-bcc1-45ea-95c0-7f7d8f4857f4'),
	 (193,1,NULL,'Flats',NULL,NULL,193,1,'5898e6f5-bad7-416e-aab5-e850c416cbe5'),
	 (194,1,NULL,'Casual Shoes',NULL,NULL,194,1,'9e752b98-a171-4837-883e-76ce2b8e0557'),
	 (195,1,NULL,'Heels',NULL,NULL,195,1,'27062c4a-26c5-46da-a716-8a4585049b04'),
	 (196,1,NULL,'Boots',NULL,NULL,196,1,'c44f19f6-701c-4c17-8bff-743835ed0e60'),
	 (197,1,NULL,'Flats',NULL,NULL,197,1,'822097fc-e04b-47af-93ce-32c3301f02a1'),
	 (198,1,NULL,'Casual Shoes',NULL,NULL,198,1,'166edd68-11df-477a-a31b-20eb44771433'),
	 (199,1,NULL,'Heels',NULL,NULL,199,1,'b8e6c970-42ae-4d15-8586-de66f76dfcec'),
	 (200,1,NULL,'Boots',NULL,NULL,200,1,'40838fea-70dd-416c-95d8-6345fe03d483');
INSERT INTO kb_catalog_inventory.product (id,country_id,preview_image,product_name,product_string,updated_on,brand_model_category_id,supplier_id,UUID) VALUES
	 (201,1,NULL,'Babydolls',NULL,NULL,201,1,'de0845f5-b686-486c-84d9-911d50ea3ab8'),
	 (202,1,NULL,'Nightdress',NULL,NULL,202,1,'f59036a8-597a-4b93-88e3-0203a8212b91'),
	 (203,1,NULL,'Pyjamas',NULL,NULL,203,1,'a4eb0ba8-d23d-4311-b59a-cce48961d164'),
	 (204,1,NULL,'Nightsuits',NULL,NULL,204,1,'502752a9-5737-45cf-aff9-279dd65a640a'),
	 (205,1,NULL,'Pyjamas',NULL,NULL,205,1,'3c66a0ab-2ea6-4fc1-ba6e-188568fc218c'),
	 (206,1,NULL,'Camisoles',NULL,NULL,206,1,'6a70d165-ddb6-437d-8548-4a121467a3fd'),
	 (207,1,NULL,'Thermals Top',NULL,NULL,207,1,'335feead-c23d-49ea-9e25-3b335ea86c1b'),
	 (208,1,NULL,'Thermals Bottom',NULL,NULL,208,1,'a9022f7f-3b52-4f7b-a3a2-50951138f628'),
	 (209,1,NULL,'Bras',NULL,NULL,209,1,'aee1f9e6-a4c5-4c5d-89e0-9c56be48b6e8'),
	 (210,1,NULL,'Lingerie Sets',NULL,NULL,210,1,'b8ffc429-c344-4ede-be2f-6c4f55864450');
INSERT INTO kb_catalog_inventory.product (id,country_id,preview_image,product_name,product_string,updated_on,brand_model_category_id,supplier_id,UUID) VALUES
	 (211,1,NULL,'Briefs',NULL,NULL,211,1,'7177f300-5ec7-47c4-ac9f-cfe474ae4fe7'),
	 (212,1,NULL,'Shapewear',NULL,NULL,212,1,'93932aa6-6e3e-4e19-be99-3fb637631fcb'),
	 (213,1,NULL,'Babydolls',NULL,NULL,213,1,'9c5a9421-41f5-40bb-bc8f-f73939308d30'),
	 (214,1,NULL,'Nightdress',NULL,NULL,214,1,'0216435e-4c54-4524-9b8e-4c076bbe3258'),
	 (215,1,NULL,'Pyjamas',NULL,NULL,215,1,'950e7374-ca45-4489-98e6-a3b4f6b647f5'),
	 (216,1,NULL,'Nightsuits',NULL,NULL,216,1,'95e15082-9282-4d55-b40e-d6b5ead0494d'),
	 (217,1,NULL,'Pyjamas',NULL,NULL,217,1,'fac8022a-83a1-4c7f-9fd4-cd1da6a582ef'),
	 (218,1,NULL,'Camisoles',NULL,NULL,218,1,'dc73e57d-ad87-4804-97c9-85c24269f6c8'),
	 (219,1,NULL,'Thermals Top',NULL,NULL,219,1,'2be95024-daa0-4ce0-bccf-d51bc3f61407'),
	 (220,1,NULL,'Thermals Bottom',NULL,NULL,220,1,'02cd6043-2fed-494a-986c-07a4e1807bc0');
INSERT INTO kb_catalog_inventory.product (id,country_id,preview_image,product_name,product_string,updated_on,brand_model_category_id,supplier_id,UUID) VALUES
	 (221,1,NULL,'Bras',NULL,NULL,221,1,'a0b9dac6-0673-43ea-8461-69534cf68cea'),
	 (222,1,NULL,'Lingerie Sets',NULL,NULL,222,1,'4f95cd2f-7d5c-409c-8024-02dc5e94e748'),
	 (223,1,NULL,'Briefs',NULL,NULL,223,1,'d3e2d104-bede-4d94-aef0-03db104cc6cc'),
	 (224,1,NULL,'Shapewear',NULL,NULL,224,1,'7fc785b9-c069-4c22-9770-e9742c52ac4b'),
	 (225,1,NULL,'Babydolls',NULL,NULL,225,1,'2385936c-1584-4618-b02f-f669a216c506'),
	 (226,1,NULL,'Nightdress',NULL,NULL,226,1,'c0a15f9d-d31f-468e-b097-6b73903e6ad1'),
	 (227,1,NULL,'Pyjamas',NULL,NULL,227,1,'2b300d95-45f0-4fdc-8197-65a44caf591d'),
	 (228,1,NULL,'Nightsuits',NULL,NULL,228,1,'03aabe06-dd2d-4ede-ac3d-548a9d6eb42f'),
	 (229,1,NULL,'Pyjamas',NULL,NULL,229,1,'24c1153f-34e3-4f19-84f7-a0c1b1356329'),
	 (230,1,NULL,'Camisoles',NULL,NULL,230,1,'b0510193-eabc-41b4-9bde-55bcaf6c71f5');
INSERT INTO kb_catalog_inventory.product (id,country_id,preview_image,product_name,product_string,updated_on,brand_model_category_id,supplier_id,UUID) VALUES
	 (231,1,NULL,'Thermals Top',NULL,NULL,231,1,'171194fa-c870-4b9c-90f0-ab6f49f1ad25'),
	 (232,1,NULL,'Thermals Bottom',NULL,NULL,232,1,'1ffa624b-2578-42ff-9bef-6ca528efc90c'),
	 (233,1,NULL,'Bras',NULL,NULL,233,1,'00904a66-d7c7-4f05-8ec3-6a3ccc26a871'),
	 (234,1,NULL,'Lingerie Sets',NULL,NULL,234,1,'1b42ec33-18f1-449a-8586-53f1178a0102'),
	 (235,1,NULL,'Briefs',NULL,NULL,235,1,'23a2ffd5-1d1b-43af-950d-5d68852faf42'),
	 (236,1,NULL,'Shapewear',NULL,NULL,236,1,'899cab0a-9bd1-45a4-8e26-39c6010f8d7a'),
	 (237,1,NULL,'Babydolls',NULL,NULL,237,1,'b1472f4a-ed18-47d8-ad20-98bb48d172ce'),
	 (238,1,NULL,'Nightdress',NULL,NULL,238,1,'5878f176-c397-4ef3-9d8a-7790555f9d81'),
	 (239,1,NULL,'Pyjamas',NULL,NULL,239,1,'a99481aa-65e3-4750-a3e3-e217f3b29757'),
	 (240,1,NULL,'Nightsuits',NULL,NULL,240,1,'1088e855-ed1b-41c7-b03d-abd873c1669d');
INSERT INTO kb_catalog_inventory.product (id,country_id,preview_image,product_name,product_string,updated_on,brand_model_category_id,supplier_id,UUID) VALUES
	 (241,1,NULL,'Pyjamas',NULL,NULL,241,1,'501bfdc8-852d-4543-8dfb-d7873f8445a1'),
	 (242,1,NULL,'Camisoles',NULL,NULL,242,1,'91736f4d-658a-4395-aab3-ea56fa962c6a'),
	 (243,1,NULL,'Thermals Top',NULL,NULL,243,1,'79b984fd-b0ef-4d8e-b343-b402f664ea6c'),
	 (244,1,NULL,'Thermals Bottom',NULL,NULL,244,1,'2455d04b-cf91-4348-9391-a4bf9d469b64'),
	 (245,1,NULL,'Bras',NULL,NULL,245,1,'c8e1fd6e-d604-4996-810f-23090e1dedea'),
	 (246,1,NULL,'Lingerie Sets',NULL,NULL,246,1,'664b0e6d-2d69-49f3-a3fd-04134a8fe37b'),
	 (247,1,NULL,'Briefs',NULL,NULL,247,1,'940ef1c9-b6de-4b5e-a743-df8ddf455bff'),
	 (248,1,NULL,'Shapewear',NULL,NULL,248,1,'1ef8724b-54f2-4d4e-aef9-237c8462707c'),
	 (249,1,NULL,'Rings for Men',NULL,NULL,249,1,'36ddf5d0-d0b6-49bc-818d-c2107dd72f65'),
	 (250,1,NULL,'Chains for Men',NULL,NULL,250,1,'1daa94fc-14a0-4d6d-83ad-60206a9969ae');
INSERT INTO kb_catalog_inventory.product (id,country_id,preview_image,product_name,product_string,updated_on,brand_model_category_id,supplier_id,UUID) VALUES
	 (251,1,NULL,'Anklets & Toe Rings',NULL,NULL,251,1,'bd8fca0e-c98a-41d3-a72d-60474e2b7410'),
	 (252,1,NULL,'Necklaces and Chains',NULL,NULL,252,1,'9fda0be5-edbd-473e-91e7-5d76855f7010'),
	 (253,1,NULL,'Pendants and Lockets',NULL,NULL,253,1,'f4779c16-9a53-4db7-98e6-12322ac0c9a0'),
	 (254,1,NULL,'RIngs',NULL,NULL,254,1,'8bbe2b5b-81af-411a-82f4-df67f9874dc8'),
	 (255,1,NULL,'Bangles',NULL,NULL,255,1,'62d59d1d-46a1-497f-8142-927554930179'),
	 (256,1,NULL,'Bracelets',NULL,NULL,256,1,'a2bfd0ce-a4ed-40e5-958a-7a37f6b4606c'),
	 (257,1,NULL,'Jewellery Set',NULL,NULL,257,1,'d02efbd4-0d47-420a-a2c5-3958a458465b'),
	 (258,1,NULL,'Ear-rings and Studs',NULL,NULL,258,1,'a086f199-a895-4bd5-9fdb-c05fd1662de9'),
	 (259,1,NULL,'Mangtikas',NULL,NULL,259,1,'924eb4e3-1527-4571-b13f-5a96057aeb9e'),
	 (260,1,NULL,'Mangalsutras',NULL,NULL,260,1,'5c012c0c-2b0f-4a50-9a64-7c9b8f1b9880');
INSERT INTO kb_catalog_inventory.product (id,country_id,preview_image,product_name,product_string,updated_on,brand_model_category_id,supplier_id,UUID) VALUES
	 (261,1,NULL,'Nose-rings',NULL,NULL,261,1,'72353fa7-026c-4bdd-9784-9f323192faed'),
	 (262,1,NULL,'Kamarbandhs',NULL,NULL,262,1,'4f3b52f9-0869-4067-bb24-e79727e5d1d5'),
	 (263,1,NULL,'Bajuband',NULL,NULL,263,1,'42e6b499-768e-45f1-bf35-5fca1d5c96b6'),
	 (264,1,NULL,'Rings for Men',NULL,NULL,264,1,'f5a0b70f-3bc3-4846-8e09-e96c5a079358'),
	 (265,1,NULL,'Chains for Men',NULL,NULL,265,1,'d5cafd68-4ef5-47c6-bf94-e4badc5b66f5'),
	 (266,1,NULL,'Anklets & Toe Rings',NULL,NULL,266,1,'e0a1139e-5478-41ac-b518-fa6ccd1cc7a1'),
	 (267,1,NULL,'Necklaces and Chains',NULL,NULL,267,1,'948d2f34-ba08-4b4e-b6ad-437af25fa0f5'),
	 (268,1,NULL,'Pendants and Lockets',NULL,NULL,268,1,'7b7d91d5-508a-4e8e-96d6-65bc4ed274f7'),
	 (269,1,NULL,'RIngs',NULL,NULL,269,1,'b20a3310-dbf8-4203-9c56-2f767c1cd1b0'),
	 (270,1,NULL,'Bangles',NULL,NULL,270,1,'1f9a4788-6009-40ed-88cd-cfe7ae417bf0');
INSERT INTO kb_catalog_inventory.product (id,country_id,preview_image,product_name,product_string,updated_on,brand_model_category_id,supplier_id,UUID) VALUES
	 (271,1,NULL,'Bracelets',NULL,NULL,271,1,'59bf59af-1a3b-4f4e-b352-e544490c9305'),
	 (272,1,NULL,'Jewellery Set',NULL,NULL,272,1,'fa2579b4-2fbc-4be2-a7b4-79d0a19f5c5b'),
	 (273,1,NULL,'Ear-rings and Studs',NULL,NULL,273,1,'03e8ca57-62f2-4d28-a973-767546f39471'),
	 (274,1,NULL,'Mangtikas',NULL,NULL,274,1,'dc9f57c8-ba72-4a85-a03a-b722bad9e444'),
	 (275,1,NULL,'Mangalsutras',NULL,NULL,275,1,'9146133e-0ee6-4ce9-ad93-c3f678b8b610'),
	 (276,1,NULL,'Nose-rings',NULL,NULL,276,1,'d80f3e54-7d4f-4461-b669-620ccf227b9e'),
	 (277,1,NULL,'Kamarbandhs',NULL,NULL,277,1,'cc953d99-d92b-4129-acb6-df22cbc69170'),
	 (278,1,NULL,'Bajuband',NULL,NULL,278,1,'4440dd17-7932-4e1f-aa8b-59c055fdb35c'),
	 (279,1,NULL,'Rings for Men',NULL,NULL,279,1,'c3e1f14f-0ce0-4c95-8f5b-7e23e531d02e'),
	 (280,1,NULL,'Chains for Men',NULL,NULL,280,1,'e79c4d42-208a-424d-a38a-aa795b86feee');
INSERT INTO kb_catalog_inventory.product (id,country_id,preview_image,product_name,product_string,updated_on,brand_model_category_id,supplier_id,UUID) VALUES
	 (281,1,NULL,'Anklets & Toe Rings',NULL,NULL,281,1,'3722e5eb-410b-467f-b41a-ff42f09094ca'),
	 (282,1,NULL,'Necklaces and Chains',NULL,NULL,282,1,'38f15548-2915-42d4-b9e8-e910ca073c8c'),
	 (283,1,NULL,'Pendants and Lockets',NULL,NULL,283,1,'bc4e87cb-aa4e-4c5b-99cf-d0ece2b1869b'),
	 (284,1,NULL,'RIngs',NULL,NULL,284,1,'ac3c7494-e470-4afc-a95e-65931031ab0f'),
	 (285,1,NULL,'Bangles',NULL,NULL,285,1,'fbadb1d7-24e4-40a2-a6b6-4b5f8049d9e2'),
	 (286,1,NULL,'Bracelets',NULL,NULL,286,1,'304d42b4-8446-4552-9e4c-d89cc8b2eba6'),
	 (287,1,NULL,'Jewellery Set',NULL,NULL,287,1,'5bb3c191-9506-44f9-ae0e-0463dbe0ab97'),
	 (288,1,NULL,'Ear-rings and Studs',NULL,NULL,288,1,'f65112ad-af19-47c4-99ae-d8a8ac846636'),
	 (289,1,NULL,'Mangtikas',NULL,NULL,289,1,'221cf48f-fd41-4f35-acf7-d07513b33960'),
	 (290,1,NULL,'Mangalsutras',NULL,NULL,290,1,'edf761e5-852c-426c-83f2-f5114c1d91c5');
INSERT INTO kb_catalog_inventory.product (id,country_id,preview_image,product_name,product_string,updated_on,brand_model_category_id,supplier_id,UUID) VALUES
	 (291,1,NULL,'Nose-rings',NULL,NULL,291,1,'088da03d-51fb-459f-9423-f08e7e53f8c1'),
	 (292,1,NULL,'Kamarbandhs',NULL,NULL,292,1,'049b57e1-5a0a-4f82-a636-46653e1e7a17'),
	 (293,1,NULL,'Bajuband',NULL,NULL,293,1,'fa1593b4-8227-41fb-881f-7a4dc696a314');
	 

 

INSERT INTO kb_catalog_inventory.variation (id,variation_name,variation_option_name,variation_id) VALUES
	 (1,'size',NULL,NULL),
	 (2,'colour',NULL,NULL),
	 (3,'fabric',NULL,NULL);




INSERT INTO kb_catalog_inventory.variation_options (id,variation_option_name,variation_id) VALUES
	 (1,'10XL',1),
	 (2,'4XL',1),
	 (3,'5XL',1),
	 (4,'blue',1),
	 (5,'pink',2),
	 (6,'Acrylic',3),
	 (7,'Chiffon',3);
	 

INSERT INTO kb_catalog_inventory.image_gallery (id,large_image,medium_image,small_image) VALUES
	 (1,NULL,NULL,NULL);	 
	 

INSERT INTO kb_catalog_inventory.product_variation_option_value (id,product_option_name,product_id,variation_option_id) VALUES
	 (1,'product with size S',1,1),
	 (2,'product  with size M',2,2),
	 (3,'product  with size L',3,3),
	 (4,'product  with size 4XL',4,4),
	 (5,'product  with blue colour',4,5),
	 (6,'product  with Pink colour',6,6),
	 (7,'product with size S',7,1),
	 (8,'product  with size M',8,2),
	 (9,'product  with size L',9,3),
	 (10,'product  with size 10XL',4,4);
INSERT INTO kb_catalog_inventory.product_variation_option_value (id,product_option_name,product_id,variation_option_id) VALUES
	 (11,'product  with blue colour',11,5),
	 (12,'product  with Pink colour',12,6),
	 (13,'product with size S',13,1),
	 (14,'product  with size M',14,2),
	 (15,'product  with size L',15,3),
	 (16,'product  with size XL',16,4),
	 (17,'product  with blue colour',17,5),
	 (18,'product  with Pink colour',18,6),
	 (19,'product with size S',19,1),
	 (20,'product  with size M',20,2);
INSERT INTO kb_catalog_inventory.product_variation_option_value (id,product_option_name,product_id,variation_option_id) VALUES
	 (21,'product  with size L',21,3),
	 (22,'product  with size XL',22,4),
	 (23,'product  with blue colour',23,5),
	 (24,'product  with Pink colour',24,6),
	 (25,'product with size S',25,1),
	 (26,'product  with size M',26,2),
	 (27,'product  with size L',27,3),
	 (28,'product  with size XL',28,4),
	 (29,'product  with blue colour',29,5),
	 (30,'product  with Pink colour',30,6);
INSERT INTO kb_catalog_inventory.product_variation_option_value (id,product_option_name,product_id,variation_option_id) VALUES
	 (31,'product with size S',31,1),
	 (32,'product  with size M',32,2),
	 (33,'product  with size L',33,3),
	 (34,'product  with size XL',34,4),
	 (35,'product  with blue colour',35,5),
	 (36,'product  with Pink colour',36,6),
	 (37,'product with size S',37,1),
	 (38,'product  with size M',38,2),
	 (39,'product  with size L',39,3),
	 (40,'product  with size XL',40,4);
INSERT INTO kb_catalog_inventory.product_variation_option_value (id,product_option_name,product_id,variation_option_id) VALUES
	 (41,'product  with blue colour',41,5),
	 (42,'product  with Pink colour',42,6),
	 (43,'product with size S',43,1),
	 (44,'product  with size M',44,2),
	 (45,'product  with size L',45,3),
	 (46,'product  with size XL',46,4),
	 (47,'product  with blue colour',47,5),
	 (48,'product  with Pink colour',48,6),
	 (49,'product with size S',49,1),
	 (50,'product  with size M',50,2);
INSERT INTO kb_catalog_inventory.product_variation_option_value (id,product_option_name,product_id,variation_option_id) VALUES
	 (51,'product  with size L',51,3),
	 (52,'product  with size XL',52,4),
	 (53,'product  with blue colour',53,5),
	 (54,'product  with Pink colour',54,6),
	 (55,'product with size S',55,1),
	 (56,'product  with size M',56,2),
	 (57,'product  with size L',57,3),
	 (58,'product  with size XL',58,4),
	 (59,'product  with blue colour',59,5),
	 (60,'product  with Pink colour',60,6);
INSERT INTO kb_catalog_inventory.product_variation_option_value (id,product_option_name,product_id,variation_option_id) VALUES
	 (61,'product with size S',61,1),
	 (62,'product  with size M',62,2),
	 (63,'product  with size L',63,3),
	 (64,'product  with size XL',64,4),
	 (65,'product  with blue colour',65,5),
	 (66,'product  with Pink colour',66,6),
	 (67,'product with size S',67,1),
	 (68,'product  with size M',68,2),
	 (69,'product  with size L',69,3),
	 (70,'product  with size XL',70,4);
INSERT INTO kb_catalog_inventory.product_variation_option_value (id,product_option_name,product_id,variation_option_id) VALUES
	 (71,'product  with blue colour',71,5),
	 (72,'product  with Pink colour',72,6),
	 (73,'product with size S',73,1),
	 (74,'product  with size M',74,2),
	 (75,'product  with size L',75,3),
	 (76,'product  with size XL',76,4),
	 (77,'product  with blue colour',77,5),
	 (78,'product  with Pink colour',78,6),
	 (79,'product with size S',79,1),
	 (80,'product  with size M',80,2);
INSERT INTO kb_catalog_inventory.product_variation_option_value (id,product_option_name,product_id,variation_option_id) VALUES
	 (81,'product  with size L',81,3),
	 (82,'product  with size XL',82,4),
	 (83,'product  with blue colour',83,5),
	 (84,'product  with Pink colour',84,6),
	 (85,'product with size S',85,1),
	 (86,'product  with size M',86,2),
	 (87,'product  with size L',87,3),
	 (88,'product  with size XL',88,4),
	 (89,'product  with blue colour',89,5),
	 (90,'product  with Pink colour',90,6);
INSERT INTO kb_catalog_inventory.product_variation_option_value (id,product_option_name,product_id,variation_option_id) VALUES
	 (91,'product with size S',91,1),
	 (92,'product  with size M',92,2),
	 (93,'product  with size L',93,3),
	 (94,'product  with size XL',94,4),
	 (95,'product  with blue colour',95,5),
	 (96,'product  with Pink colour',96,6),
	 (97,'product with size S',97,1),
	 (98,'product  with size M',98,2),
	 (99,'product  with size L',99,3),
	 (100,'product  with size XL',100,4);
INSERT INTO kb_catalog_inventory.product_variation_option_value (id,product_option_name,product_id,variation_option_id) VALUES
	 (101,'product  with blue colour',101,5),
	 (102,'product  with Pink colour',102,6),
	 (103,'product with size S',103,1),
	 (104,'product  with size M',104,2),
	 (105,'product  with size L',105,3),
	 (106,'product  with size XL',106,4),
	 (107,'product  with blue colour',107,5),
	 (108,'product  with Pink colour',108,6),
	 (109,'product with size S',109,1),
	 (110,'product  with size M',110,2);
INSERT INTO kb_catalog_inventory.product_variation_option_value (id,product_option_name,product_id,variation_option_id) VALUES
	 (111,'product  with size L',111,3),
	 (112,'product  with size XL',112,4),
	 (113,'product  with blue colour',113,5),
	 (114,'product  with Pink colour',133,6),
	 (115,'product with size S',134,1),
	 (116,'product  with size M',135,2),
	 (117,'product  with size L',136,3),
	 (118,'product  with size XL',137,4),
	 (119,'product  with blue colour',138,5),
	 (120,'product  with Pink colour',139,6);
INSERT INTO kb_catalog_inventory.product_variation_option_value (id,product_option_name,product_id,variation_option_id) VALUES
	 (121,'product with size S',140,1),
	 (122,'product  with size M',141,2),
	 (123,'product  with size L',142,3),
	 (124,'product  with size XL',143,4),
	 (125,'product  with blue colour',144,5),
	 (126,'product  with Pink colour',145,6),
	 (127,'product with size S',146,1),
	 (128,'product  with size M',147,2),
	 (129,'product  with size L',148,3),
	 (130,'product  with size XL',149,4);
INSERT INTO kb_catalog_inventory.product_variation_option_value (id,product_option_name,product_id,variation_option_id) VALUES
	 (131,'product  with blue colour',150,5),
	 (132,'product  with Pink colour',151,6),
	 (133,'product with size S',152,1),
	 (134,'product  with size M',153,2),
	 (135,'product  with size L',154,3),
	 (136,'product  with size XL',155,4),
	 (137,'product  with blue colour',156,5),
	 (138,'product  with Pink colour',157,6),
	 (139,'product with size S',158,1),
	 (140,'product  with size M',159,2);
INSERT INTO kb_catalog_inventory.product_variation_option_value (id,product_option_name,product_id,variation_option_id) VALUES
	 (141,'product  with size L',160,3),
	 (142,'product  with size XL',161,4),
	 (143,'product  with blue colour',162,5),
	 (144,'product  with Pink colour',163,6),
	 (145,'product with size S',164,1),
	 (146,'product  with size M',165,2),
	 (147,'product  with size L',166,3),
	 (148,'product  with size XL',167,4),
	 (149,'product  with blue colour',168,5),
	 (150,'product  with Pink colour',169,6);
INSERT INTO kb_catalog_inventory.product_variation_option_value (id,product_option_name,product_id,variation_option_id) VALUES
	 (151,'product with size S',170,1),
	 (152,'product  with size M',171,2),
	 (153,'product  with size L',172,3),
	 (154,'product  with size XL',173,4),
	 (155,'product  with blue colour',174,5),
	 (156,'product  with Pink colour',175,6),
	 (157,'product with size S',176,1),
	 (158,'product  with size M',177,2),
	 (159,'product  with size L',178,3),
	 (160,'product  with size XL',179,4);
INSERT INTO kb_catalog_inventory.product_variation_option_value (id,product_option_name,product_id,variation_option_id) VALUES
	 (161,'product  with blue colour',180,5),
	 (162,'product  with Pink colour',181,6),
	 (163,'product with size S',182,1),
	 (164,'product  with size M',183,2),
	 (165,'product  with size L',184,3),
	 (166,'product  with size XL',185,4),
	 (167,'product  with blue colour',186,5),
	 (168,'product  with Pink colour',187,6),
	 (169,'product with size S',188,1),
	 (170,'product  with size M',189,2);
INSERT INTO kb_catalog_inventory.product_variation_option_value (id,product_option_name,product_id,variation_option_id) VALUES
	 (171,'product  with size L',190,3),
	 (172,'product  with size XL',191,4),
	 (173,'product  with blue colour',192,5),
	 (174,'product  with Pink colour',201,6),
	 (175,'product with size S',202,1),
	 (176,'product  with size M',203,2),
	 (177,'product  with size L',204,3),
	 (178,'product  with size XL',205,4),
	 (179,'product  with blue colour',206,5),
	 (180,'product  with Pink colour',207,6);
INSERT INTO kb_catalog_inventory.product_variation_option_value (id,product_option_name,product_id,variation_option_id) VALUES
	 (181,'product with size S',208,1),
	 (182,'product  with size M',209,2),
	 (183,'product  with size L',210,3),
	 (184,'product  with size XL',211,4),
	 (185,'product  with blue colour',212,5),
	 (186,'product  with Pink colour',213,6),
	 (187,'product with size S',214,1),
	 (188,'product  with size M',215,2),
	 (189,'product  with size L',216,3),
	 (190,'product  with size XL',217,4);
INSERT INTO kb_catalog_inventory.product_variation_option_value (id,product_option_name,product_id,variation_option_id) VALUES
	 (191,'product  with blue colour',218,5),
	 (192,'product  with Pink colour',219,6),
	 (193,'product with size S',220,1),
	 (194,'product  with size M',221,2),
	 (195,'product  with size L',222,3),
	 (196,'product  with size XL',223,4),
	 (197,'product  with blue colour',224,5),
	 (198,'product  with Pink colour',225,6),
	 (199,'product with size S',226,1),
	 (200,'product  with size M',227,2);
INSERT INTO kb_catalog_inventory.product_variation_option_value (id,product_option_name,product_id,variation_option_id) VALUES
	 (201,'product  with size L',228,3),
	 (202,'product  with size XL',229,4),
	 (203,'product  with blue colour',230,5),
	 (204,'product  with Pink colour',231,6),
	 (205,'product with size S',232,1),
	 (206,'product  with size M',233,2),
	 (207,'product  with size L',234,3),
	 (208,'product  with size XL',235,4),
	 (209,'product  with blue colour',236,5),
	 (210,'product  with Pink colour',237,6);
INSERT INTO kb_catalog_inventory.product_variation_option_value (id,product_option_name,product_id,variation_option_id) VALUES
	 (211,'product with size S',238,1),
	 (212,'product  with size M',239,2),
	 (213,'product  with size L',240,3),
	 (214,'product  with size XL',241,4),
	 (215,'product  with blue colour',242,5),
	 (216,'product  with Pink colour',243,6),
	 (217,'product with size S',244,1),
	 (218,'product  with size M',245,2),
	 (219,'product  with size L',246,3),
	 (220,'product  with size XL',247,4);
INSERT INTO kb_catalog_inventory.product_variation_option_value (id,product_option_name,product_id,variation_option_id) VALUES
	 (221,'product  with blue colour',248,5);

INSERT INTO kb_catalog_inventory.product_variation_option_image (id,is_featured,image_gallery_id,product_id,product_variation_option_value_id) VALUES
	 (1,NULL,1,1,1),
	 (2,NULL,1,2,2),
	 (3,NULL,1,3,3),
	 (4,NULL,1,4,4),
	 (5,NULL,1,5,5),
	 (6,NULL,1,6,6),
	 (7,NULL,1,7,7),
	 (8,NULL,1,8,8),
	 (9,NULL,1,9,9),
	 (10,NULL,1,10,10);
INSERT INTO kb_catalog_inventory.product_variation_option_image (id,is_featured,image_gallery_id,product_id,product_variation_option_value_id) VALUES
	 (11,NULL,1,11,11),
	 (12,NULL,1,12,12),
	 (13,NULL,1,13,13),
	 (14,NULL,1,14,14),
	 (15,NULL,1,15,15),
	 (16,NULL,1,16,16),
	 (17,NULL,1,17,17),
	 (18,NULL,1,18,18),
	 (19,NULL,1,19,19),
	 (20,NULL,1,20,20);
INSERT INTO kb_catalog_inventory.product_variation_option_image (id,is_featured,image_gallery_id,product_id,product_variation_option_value_id) VALUES
	 (21,NULL,1,21,21),
	 (22,NULL,1,22,22),
	 (23,NULL,1,23,23),
	 (24,NULL,1,24,24),
	 (25,NULL,1,25,25),
	 (26,NULL,1,26,26),
	 (27,NULL,1,27,27),
	 (28,NULL,1,28,28),
	 (29,NULL,1,29,29),
	 (30,NULL,1,30,30);
INSERT INTO kb_catalog_inventory.product_variation_option_image (id,is_featured,image_gallery_id,product_id,product_variation_option_value_id) VALUES
	 (31,NULL,1,31,31),
	 (32,NULL,1,32,32),
	 (33,NULL,1,33,33),
	 (34,NULL,1,34,34),
	 (35,NULL,1,35,35),
	 (36,NULL,1,36,36),
	 (37,NULL,1,37,37),
	 (38,NULL,1,38,38),
	 (39,NULL,1,39,39),
	 (40,NULL,1,40,40);
INSERT INTO kb_catalog_inventory.product_variation_option_image (id,is_featured,image_gallery_id,product_id,product_variation_option_value_id) VALUES
	 (41,NULL,1,41,41),
	 (42,NULL,1,42,42),
	 (43,NULL,1,43,43),
	 (44,NULL,1,44,44),
	 (45,NULL,1,45,45),
	 (46,NULL,1,46,46),
	 (47,NULL,1,47,47),
	 (48,NULL,1,48,48),
	 (49,NULL,1,49,49),
	 (50,NULL,1,50,50);
INSERT INTO kb_catalog_inventory.product_variation_option_image (id,is_featured,image_gallery_id,product_id,product_variation_option_value_id) VALUES
	 (51,NULL,1,51,51),
	 (52,NULL,1,52,52),
	 (53,NULL,1,53,53),
	 (54,NULL,1,54,54),
	 (55,NULL,1,55,55),
	 (56,NULL,1,56,56),
	 (57,NULL,1,57,57),
	 (58,NULL,1,58,58),
	 (59,NULL,1,59,59),
	 (60,NULL,1,60,60);
INSERT INTO kb_catalog_inventory.product_variation_option_image (id,is_featured,image_gallery_id,product_id,product_variation_option_value_id) VALUES
	 (61,NULL,1,61,61),
	 (62,NULL,1,62,62),
	 (63,NULL,1,63,63),
	 (64,NULL,1,64,64),
	 (65,NULL,1,65,65),
	 (66,NULL,1,66,66),
	 (67,NULL,1,67,67),
	 (68,NULL,1,68,68),
	 (69,NULL,1,69,69),
	 (70,NULL,1,70,70);
INSERT INTO kb_catalog_inventory.product_variation_option_image (id,is_featured,image_gallery_id,product_id,product_variation_option_value_id) VALUES
	 (71,NULL,1,71,71),
	 (72,NULL,1,72,72),
	 (73,NULL,1,73,73),
	 (74,NULL,1,74,74),
	 (75,NULL,1,75,75),
	 (76,NULL,1,76,76),
	 (77,NULL,1,77,77),
	 (78,NULL,1,78,78),
	 (79,NULL,1,79,79),
	 (80,NULL,1,80,80);
INSERT INTO kb_catalog_inventory.product_variation_option_image (id,is_featured,image_gallery_id,product_id,product_variation_option_value_id) VALUES
	 (81,NULL,1,81,81),
	 (82,NULL,1,82,82),
	 (83,NULL,1,83,83),
	 (84,NULL,1,84,84),
	 (85,NULL,1,85,85),
	 (86,NULL,1,86,86),
	 (87,NULL,1,87,87),
	 (88,NULL,1,88,88),
	 (89,NULL,1,89,89),
	 (90,NULL,1,90,90);
INSERT INTO kb_catalog_inventory.product_variation_option_image (id,is_featured,image_gallery_id,product_id,product_variation_option_value_id) VALUES
	 (91,NULL,1,91,91),
	 (92,NULL,1,92,92),
	 (93,NULL,1,93,93),
	 (94,NULL,1,94,94),
	 (95,NULL,1,95,95),
	 (96,NULL,1,96,96),
	 (97,NULL,1,97,97),
	 (98,NULL,1,98,98),
	 (99,NULL,1,99,99),
	 (100,NULL,1,100,100);
INSERT INTO kb_catalog_inventory.product_variation_option_image (id,is_featured,image_gallery_id,product_id,product_variation_option_value_id) VALUES
	 (101,NULL,1,101,101),
	 (102,NULL,1,102,102),
	 (103,NULL,1,103,103),
	 (104,NULL,1,104,104),
	 (105,NULL,1,105,105),
	 (106,NULL,1,106,106),
	 (107,NULL,1,107,107),
	 (108,NULL,1,108,108),
	 (109,NULL,1,109,109),
	 (110,NULL,1,110,110);
INSERT INTO kb_catalog_inventory.product_variation_option_image (id,is_featured,image_gallery_id,product_id,product_variation_option_value_id) VALUES
	 (111,NULL,1,111,111),
	 (112,NULL,1,112,112),
	 (113,NULL,1,113,113),
	 (114,NULL,1,133,114),
	 (115,NULL,1,134,115),
	 (116,NULL,1,135,116),
	 (117,NULL,1,136,117),
	 (118,NULL,1,137,118),
	 (119,NULL,1,138,119),
	 (120,NULL,1,139,120);
INSERT INTO kb_catalog_inventory.product_variation_option_image (id,is_featured,image_gallery_id,product_id,product_variation_option_value_id) VALUES
	 (121,NULL,1,140,121),
	 (122,NULL,1,141,122),
	 (123,NULL,1,142,123),
	 (124,NULL,1,143,124),
	 (125,NULL,1,144,125),
	 (126,NULL,1,145,126),
	 (127,NULL,1,146,127),
	 (128,NULL,1,147,128),
	 (129,NULL,1,148,129),
	 (130,NULL,1,149,130);
INSERT INTO kb_catalog_inventory.product_variation_option_image (id,is_featured,image_gallery_id,product_id,product_variation_option_value_id) VALUES
	 (131,NULL,1,150,131),
	 (132,NULL,1,151,132),
	 (133,NULL,1,152,133),
	 (134,NULL,1,153,134),
	 (135,NULL,1,154,135),
	 (136,NULL,1,155,136),
	 (137,NULL,1,156,137),
	 (138,NULL,1,157,138),
	 (139,NULL,1,158,139),
	 (140,NULL,1,159,140);
INSERT INTO kb_catalog_inventory.product_variation_option_image (id,is_featured,image_gallery_id,product_id,product_variation_option_value_id) VALUES
	 (141,NULL,1,160,141),
	 (142,NULL,1,161,142),
	 (143,NULL,1,162,143),
	 (144,NULL,1,163,144),
	 (145,NULL,1,164,145),
	 (146,NULL,1,165,146),
	 (147,NULL,1,166,147),
	 (148,NULL,1,167,148),
	 (149,NULL,1,168,149),
	 (150,NULL,1,169,150);
INSERT INTO kb_catalog_inventory.product_variation_option_image (id,is_featured,image_gallery_id,product_id,product_variation_option_value_id) VALUES
	 (151,NULL,1,170,151),
	 (152,NULL,1,171,152),
	 (153,NULL,1,172,153),
	 (154,NULL,1,173,154),
	 (155,NULL,1,174,155),
	 (156,NULL,1,175,156),
	 (157,NULL,1,176,157),
	 (158,NULL,1,177,158),
	 (159,NULL,1,178,159),
	 (160,NULL,1,179,160);
INSERT INTO kb_catalog_inventory.product_variation_option_image (id,is_featured,image_gallery_id,product_id,product_variation_option_value_id) VALUES
	 (161,NULL,1,180,161),
	 (162,NULL,1,181,162),
	 (163,NULL,1,182,163),
	 (164,NULL,1,183,164),
	 (165,NULL,1,184,165),
	 (166,NULL,1,185,166),
	 (167,NULL,1,186,167),
	 (168,NULL,1,187,168),
	 (169,NULL,1,188,169),
	 (170,NULL,1,189,170);
INSERT INTO kb_catalog_inventory.product_variation_option_image (id,is_featured,image_gallery_id,product_id,product_variation_option_value_id) VALUES
	 (171,NULL,1,190,171),
	 (172,NULL,1,191,172),
	 (173,NULL,1,192,173),
	 (174,NULL,1,201,174),
	 (175,NULL,1,202,175),
	 (176,NULL,1,203,176),
	 (177,NULL,1,204,177),
	 (178,NULL,1,205,178),
	 (179,NULL,1,206,179),
	 (180,NULL,1,207,180);
INSERT INTO kb_catalog_inventory.product_variation_option_image (id,is_featured,image_gallery_id,product_id,product_variation_option_value_id) VALUES
	 (181,NULL,1,208,181),
	 (182,NULL,1,209,182),
	 (183,NULL,1,210,183),
	 (184,NULL,1,211,184),
	 (185,NULL,1,212,185),
	 (186,NULL,1,213,186),
	 (187,NULL,1,214,187),
	 (188,NULL,1,215,188),
	 (189,NULL,1,216,189),
	 (190,NULL,1,217,190);
INSERT INTO kb_catalog_inventory.product_variation_option_image (id,is_featured,image_gallery_id,product_id,product_variation_option_value_id) VALUES
	 (191,NULL,1,218,191),
	 (192,NULL,1,219,192),
	 (193,NULL,1,220,193),
	 (194,NULL,1,221,194),
	 (195,NULL,1,222,195),
	 (196,NULL,1,223,196),
	 (197,NULL,1,224,197),
	 (198,NULL,1,225,198),
	 (199,NULL,1,226,199),
	 (200,NULL,1,227,200);
INSERT INTO kb_catalog_inventory.product_variation_option_image (id,is_featured,image_gallery_id,product_id,product_variation_option_value_id) VALUES
	 (201,NULL,1,228,201),
	 (202,NULL,1,229,202),
	 (203,NULL,1,230,203),
	 (204,NULL,1,231,204),
	 (205,NULL,1,232,205),
	 (206,NULL,1,233,206),
	 (207,NULL,1,234,207),
	 (208,NULL,1,235,208),
	 (209,NULL,1,236,209),
	 (210,NULL,1,237,210);
INSERT INTO kb_catalog_inventory.product_variation_option_image (id,is_featured,image_gallery_id,product_id,product_variation_option_value_id) VALUES
	 (211,NULL,1,238,211),
	 (212,NULL,1,239,212),
	 (213,NULL,1,240,213),
	 (214,NULL,1,241,214),
	 (215,NULL,1,242,215),
	 (216,NULL,1,243,216),
	 (217,NULL,1,244,217),
	 (218,NULL,1,245,218),
	 (219,NULL,1,246,219),
	 (220,NULL,1,247,220);
INSERT INTO kb_catalog_inventory.product_variation_option_image (id,is_featured,image_gallery_id,product_id,product_variation_option_value_id) VALUES
	 (221,NULL,1,248,221);


INSERT INTO kb_catalog_inventory.product_combinations (id,available_stock,combination_string,inserted_on,price,sku,supplier_price,unique_identifier,updated_on,product_id) VALUES
	 (1,100,'Blue-10XL','2021-07-13 00:00:00',1000.0,'6108',1000.0,'b1b5c46e-c41e-4813-add6-1201d6c856f6',NULL,1),
	 (2,101,'Blue-4XL','2021-07-13 00:00:00',1001.0,'610831',1001.0,'12635d4c-6735-40bc-8f8a-73b166e70c1e',NULL,1),
	 (3,102,'pink-10XL','2021-07-13 00:00:00',1002.0,'610832',1002.0,'6184c7c9-43c9-40bf-b773-78aee0c23198',NULL,1),
	 (4,103,'Pink-4XL','2021-07-13 00:00:00',1003.0,'61083210',1003.0,'ef33aa5d-191a-462c-892c-a8f8a1f0f9d7',NULL,1),
	 (5,104,'Blue-10XL','2021-07-13 00:00:00',1004.0,'610839',1004.0,'7423fcbc-19bb-4057-be5c-359a0ecca9fd',NULL,2),
	 (6,105,'Blue-4XL','2021-07-13 00:00:00',1005.0,'6208',1005.0,'e54a5c74-d2cd-444a-bf4d-9ece20209921',NULL,2),
	 (7,106,'pink-10XL','2021-07-13 00:00:00',1006.0,'620821',1006.0,'24543711-f8d8-4822-9ee5-2f9f8c080836',NULL,2),
	 (8,107,'Pink-4XL','2021-07-13 00:00:00',1007.0,'62082100',1007.0,'6e46b2b0-a184-4034-a48e-5b467fa8a95a',NULL,2),
	 (9,108,'Blue-10XL','2021-07-13 00:00:00',1008.0,'620822',1008.0,'3d5049cd-3170-48e0-a029-badfca3a1fc9',NULL,3),
	 (10,109,'Blue-4XL','2021-07-13 00:00:00',1009.0,'62082200',1009.0,'52bfed2d-08fa-468d-9b09-208a28db79c8',NULL,3);
INSERT INTO kb_catalog_inventory.product_combinations (id,available_stock,combination_string,inserted_on,price,sku,supplier_price,unique_identifier,updated_on,product_id) VALUES
	 (11,110,'pink-10XL','2021-07-13 00:00:00',1010.0,'620829',1010.0,'c305e5b7-5462-4fcc-9cfa-30359413adf3',NULL,3),
	 (12,111,'Pink-4XL','2021-07-13 00:00:00',1011.0,'6108',1011.0,'5c5797a2-79e9-466a-b32f-5c3aea35457a',NULL,3),
	 (13,112,'Blue-10XL','2021-07-13 00:00:00',1012.0,'610831',1012.0,'d31f3e81-2cd8-45df-b4c5-fe142d7249fc',NULL,4),
	 (14,113,'Blue-4XL','2021-07-13 00:00:00',1013.0,'610832',1013.0,'6f255ec4-6aa8-47d0-b007-16f41c1d7c23',NULL,4),
	 (15,114,'pink-10XL','2021-07-13 00:00:00',1014.0,'61083210',1014.0,'664ae908-011b-40b1-b07c-e05c3ad559ef',NULL,4),
	 (16,115,'Pink-4XL','2021-07-13 00:00:00',1015.0,'610839',1015.0,'68fd2838-ef6e-44fc-8aea-90b215165f63',NULL,4),
	 (17,116,'Blue-10XL','2021-07-13 00:00:00',1016.0,'6208',1016.0,'76ff6221-fa88-4480-99de-2b29d351be49',NULL,5),
	 (18,117,'Blue-4XL','2021-07-13 00:00:00',1017.0,'620821',1017.0,'769ae7bd-80a0-423f-9c73-ac0405e8b2c2',NULL,5),
	 (19,118,'pink-10XL','2021-07-13 00:00:00',1018.0,'62082100',1018.0,'02055bd3-6ee2-40dc-816b-eb261e96a964',NULL,5),
	 (20,119,'Pink-4XL','2021-07-13 00:00:00',1019.0,'620822',1019.0,'ef5921f5-639a-4f41-9953-d43994a95736',NULL,5);
INSERT INTO kb_catalog_inventory.product_combinations (id,available_stock,combination_string,inserted_on,price,sku,supplier_price,unique_identifier,updated_on,product_id) VALUES
	 (21,120,'Blue-10XL','2021-07-13 00:00:00',1020.0,'62082200',1020.0,'74efd859-2c8c-4a93-ab52-347453f9387b',NULL,6),
	 (22,121,'Blue-4XL','2021-07-13 00:00:00',1021.0,'620829',1021.0,'3c294390-5ed9-4d1d-bd6f-de640450976a',NULL,6),
	 (23,122,'pink-10XL','2021-07-13 00:00:00',1022.0,'6108',1022.0,'b6c8e2de-14db-4284-94e0-2cd3bdc84e5c',NULL,6),
	 (24,123,'Pink-4XL','2021-07-13 00:00:00',1023.0,'610831',1023.0,'e8de66c2-f47f-4c12-97e5-5f94d4c68d98',NULL,6),
	 (25,124,'Blue-10XL','2021-07-13 00:00:00',1024.0,'610832',1024.0,'8ad12008-dc33-48b2-b997-c6d54d486e27',NULL,7),
	 (26,125,'Blue-4XL','2021-07-13 00:00:00',1025.0,'61083210',1025.0,'5dad084d-8fde-4dfb-b8a1-b84ded29c109',NULL,7),
	 (27,126,'pink-10XL','2021-07-13 00:00:00',1026.0,'610839',1026.0,'ae726bb9-7d40-4999-a94d-c25719d5f4bd',NULL,7),
	 (28,127,'Pink-4XL','2021-07-13 00:00:00',1027.0,'6208',1027.0,'a49f8ca6-5462-487c-9730-65c230a905d3',NULL,7),
	 (29,128,'Blue-10XL','2021-07-13 00:00:00',1028.0,'620821',1028.0,'17aec762-a85f-4263-9c61-4e0f63896497',NULL,8),
	 (30,129,'Blue-4XL','2021-07-13 00:00:00',1029.0,'62082100',1029.0,'499d7a14-665f-4e26-b5e6-6080b1feb843',NULL,8);
INSERT INTO kb_catalog_inventory.product_combinations (id,available_stock,combination_string,inserted_on,price,sku,supplier_price,unique_identifier,updated_on,product_id) VALUES
	 (31,130,'pink-10XL','2021-07-13 00:00:00',1030.0,'620822',1030.0,'00d5b7d5-ab06-441e-b115-fe4f3ea62a13',NULL,8),
	 (32,131,'Pink-4XL','2021-07-13 00:00:00',1031.0,'62082200',1031.0,'33561c79-d12d-49b6-a364-1fa53c268a6a',NULL,8),
	 (33,132,'Blue-10XL','2021-07-13 00:00:00',1032.0,'620829',1032.0,'23582c48-43d2-460a-bee3-d3dd2b5353c7',NULL,9),
	 (34,133,'Blue-4XL','2021-07-13 00:00:00',1033.0,'6108',1033.0,'13636a98-a0bf-41bc-a1a9-2111e8942c11',NULL,9),
	 (35,134,'pink-10XL','2021-07-13 00:00:00',1034.0,'610831',1034.0,'53822c21-07e1-4fe2-a385-a520d559b9f2',NULL,9),
	 (36,135,'Pink-4XL','2021-07-13 00:00:00',1035.0,'610832',1035.0,'2d8c32f3-dd16-4ead-a77f-725f33b7454d',NULL,9),
	 (37,136,'Blue-10XL','2021-07-13 00:00:00',1036.0,'61083210',1036.0,'5fbb2c62-ece8-4c3a-95db-8387d0111d37',NULL,10),
	 (38,137,'Blue-4XL','2021-07-13 00:00:00',1037.0,'610839',1037.0,'ccbef089-4ae3-4771-8ee6-9c5a97c0a603',NULL,10),
	 (39,138,'pink-10XL','2021-07-13 00:00:00',1038.0,'6208',1038.0,'c4762151-dd39-4270-95cf-a0adfeed1674',NULL,10),
	 (40,139,'Pink-4XL','2021-07-13 00:00:00',1039.0,'620821',1039.0,'38729519-c5a8-4022-844c-d5db28a8e0df',NULL,10);
INSERT INTO kb_catalog_inventory.product_combinations (id,available_stock,combination_string,inserted_on,price,sku,supplier_price,unique_identifier,updated_on,product_id) VALUES
	 (41,140,'Blue-10XL','2021-07-13 00:00:00',1040.0,'62082100',1040.0,'5638ef00-f0c3-41aa-a165-abc154522ec6',NULL,11),
	 (42,141,'Blue-4XL','2021-07-13 00:00:00',1041.0,'620822',1041.0,'ed6c6ecc-affe-4a28-998c-7796c4f899ab',NULL,11),
	 (43,142,'pink-10XL','2021-07-13 00:00:00',1042.0,'62082200',1042.0,'2409b143-a7b9-4869-a621-f5a45f70765b',NULL,11),
	 (44,143,'Pink-4XL','2021-07-13 00:00:00',1043.0,'620829',1043.0,'6224cc5d-9abb-41e7-9d1c-6e97ae698048',NULL,11),
	 (45,144,'Blue-10XL','2021-07-13 00:00:00',1044.0,'6108',1044.0,'9837d4a4-c344-479b-9251-1e0554334da8',NULL,39),
	 (46,145,'Blue-4XL','2021-07-13 00:00:00',1045.0,'610831',1045.0,'ae7ffe80-f18e-4657-9fe5-303353597d11',NULL,39),
	 (47,146,'pink-10XL','2021-07-13 00:00:00',1046.0,'610832',1046.0,'0281f96b-2b3e-42fa-8364-2e130ba65553',NULL,39),
	 (48,147,'Pink-4XL','2021-07-13 00:00:00',1047.0,'61083210',1047.0,'504022c1-eb72-4863-935b-f9cf6e101421',NULL,39),
	 (49,148,'Blue-10XL','2021-07-13 00:00:00',1048.0,'610839',1048.0,'3033b527-fe02-4863-a475-fea514e3e831',NULL,43),
	 (50,149,'Blue-4XL','2021-07-13 00:00:00',1049.0,'6208',1049.0,'7a1e1d6c-4167-421e-9085-9adac6c64335',NULL,43);
INSERT INTO kb_catalog_inventory.product_combinations (id,available_stock,combination_string,inserted_on,price,sku,supplier_price,unique_identifier,updated_on,product_id) VALUES
	 (51,150,'pink-10XL','2021-07-13 00:00:00',1050.0,'620821',1050.0,'84567201-9bca-4d91-8a1a-6821ed567dcf',NULL,43),
	 (52,151,'Pink-4XL','2021-07-13 00:00:00',1051.0,'62082100',1051.0,'0964fa77-d925-40b6-bc0b-fddf30c45e44',NULL,43),
	 (53,152,'Blue-10XL','2021-07-13 00:00:00',1052.0,'620822',1052.0,'9ad50dac-02f9-4687-b245-d7939f8dbac6',NULL,47),
	 (54,153,'Blue-4XL','2021-07-13 00:00:00',1053.0,'62082200',1053.0,'59376a3b-124a-40a0-81db-ac5f89fa55f0',NULL,47),
	 (55,154,'pink-10XL','2021-07-13 00:00:00',1054.0,'620829',1054.0,'8c721b46-910e-42e4-977f-e59bf5b2c1da',NULL,47),
	 (56,155,'Pink-4XL','2021-07-13 00:00:00',1055.0,'6108',1055.0,'426b8993-b6e7-499d-b23e-771f5feab584',NULL,47),
	 (57,156,'Blue-10XL','2021-07-13 00:00:00',1056.0,'610831',1056.0,'fc9c8de1-55ff-40dc-9817-59d9597e92a7',NULL,51),
	 (58,157,'Blue-4XL','2021-07-13 00:00:00',1057.0,'610832',1057.0,'6d3112d5-7345-465f-898d-130831540216',NULL,51),
	 (59,158,'pink-10XL','2021-07-13 00:00:00',1058.0,'61083210',1058.0,'6f997555-f0ee-4b9b-8b59-b97586b09313',NULL,51),
	 (60,159,'Pink-4XL','2021-07-13 00:00:00',1059.0,'610839',1059.0,'dfc84522-b564-47ac-81d3-fb3c5a9732aa',NULL,51);
INSERT INTO kb_catalog_inventory.product_combinations (id,available_stock,combination_string,inserted_on,price,sku,supplier_price,unique_identifier,updated_on,product_id) VALUES
	 (61,160,'Blue-10XL','2021-07-13 00:00:00',1060.0,'6208',1060.0,'13db386d-35c5-4adf-a276-2c5217875a0e',NULL,55),
	 (62,161,'Blue-4XL','2021-07-13 00:00:00',1061.0,'620821',1061.0,'17344a21-991b-47bb-9b5f-3b54501eb810',NULL,55),
	 (63,162,'pink-10XL','2021-07-13 00:00:00',1062.0,'62082100',1062.0,'cd44194d-85c4-4015-a614-780f41fc617e',NULL,55),
	 (64,163,'Pink-4XL','2021-07-13 00:00:00',1063.0,'620822',1063.0,'f677b0d8-4f09-4d21-8f8d-251fed2f50c8',NULL,55),
	 (65,164,'Blue-10XL','2021-07-13 00:00:00',1064.0,'62082200',1064.0,'9756be6c-4ffb-4cea-ad30-c49dbb90bc06',NULL,59),
	 (66,165,'Blue-4XL','2021-07-13 00:00:00',1065.0,'620829',1065.0,'a2efae81-a702-4429-a384-f8b4fd72d957',NULL,59),
	 (67,166,'pink-10XL','2021-07-13 00:00:00',1066.0,'6108',1066.0,'d804d4f3-f229-49f8-81d8-0992d0d2416e',NULL,59),
	 (68,167,'Pink-4XL','2021-07-13 00:00:00',1067.0,'610831',1067.0,'dc1c5dab-c44e-4b7a-b22e-c3be5fbbdbe9',NULL,59),
	 (69,168,'Blue-10XL','2021-07-13 00:00:00',1068.0,'610832',1068.0,'7bfdcd1e-25ec-40e0-9808-1c1c0962e90b',NULL,63),
	 (70,169,'Blue-4XL','2021-07-13 00:00:00',1069.0,'61083210',1069.0,'9e47269d-444f-492b-a171-4256bc104fd0',NULL,63);
INSERT INTO kb_catalog_inventory.product_combinations (id,available_stock,combination_string,inserted_on,price,sku,supplier_price,unique_identifier,updated_on,product_id) VALUES
	 (71,170,'pink-10XL','2021-07-13 00:00:00',1070.0,'610839',1070.0,'de9f7aff-14c7-46ae-8efd-bd84f3370b56',NULL,63),
	 (72,171,'Pink-4XL','2021-07-13 00:00:00',1071.0,'6208',1071.0,'3186a3bb-0621-47d2-9f64-7b393ee59d48',NULL,63),
	 (73,172,'Blue-10XL','2021-07-13 00:00:00',1072.0,'620821',1072.0,'c401e9d6-c8c6-4bd5-b4bf-2e91322d8ee1',NULL,67),
	 (74,173,'Blue-4XL','2021-07-13 00:00:00',1073.0,'62082100',1073.0,'54792e7a-3dc9-47d5-8156-c7994b3e4c4f',NULL,67),
	 (75,174,'pink-10XL','2021-07-13 00:00:00',1074.0,'620822',1074.0,'35189295-8840-4108-a9cb-562c915a399f',NULL,67),
	 (76,175,'Pink-4XL','2021-07-13 00:00:00',1075.0,'62082200',1075.0,'9f4f0589-4ce9-420e-b2aa-5ea7885c3959',NULL,67),
	 (77,176,'Blue-10XL','2021-07-13 00:00:00',1076.0,'620829',1076.0,'64d7fe71-adf8-4b8b-95f6-d2fef5d40138',NULL,71),
	 (78,177,'Blue-4XL','2021-07-13 00:00:00',1077.0,'6108',1077.0,'d6d35017-9d2a-48fa-ab02-85c242646701',NULL,71),
	 (79,178,'pink-10XL','2021-07-13 00:00:00',1078.0,'610831',1078.0,'5a39f334-944d-4058-8590-d423efc6b6cd',NULL,71),
	 (80,179,'Pink-4XL','2021-07-13 00:00:00',1079.0,'610832',1079.0,'e2dca66a-5c31-4f59-b46e-06f2e6b26c04',NULL,71);
INSERT INTO kb_catalog_inventory.product_combinations (id,available_stock,combination_string,inserted_on,price,sku,supplier_price,unique_identifier,updated_on,product_id) VALUES
	 (81,180,'Blue-10XL','2021-07-13 00:00:00',1080.0,'61083210',1080.0,'c91baf01-c827-4648-a0af-3e3e60b036af',NULL,75),
	 (82,181,'Blue-4XL','2021-07-13 00:00:00',1081.0,'610839',1081.0,'a94b526c-f398-435a-b537-1262c961caff',NULL,75),
	 (83,182,'pink-10XL','2021-07-13 00:00:00',1082.0,'6208',1082.0,'a597569c-0e45-43ba-a4ed-69c32c1a4d4e',NULL,75),
	 (84,183,'Pink-4XL','2021-07-13 00:00:00',1083.0,'620821',1083.0,'fe874883-4e72-4310-80b6-f31b6d5ff54d',NULL,75),
	 (85,184,'Blue-10XL','2021-07-13 00:00:00',1084.0,'62082100',1084.0,'0b6c216b-d148-43f6-a3f7-3538f76c319b',NULL,79),
	 (86,185,'Blue-4XL','2021-07-13 00:00:00',1085.0,'620822',1085.0,'ef7f2e76-cfc7-4a40-bb3c-066e0b39b7f3',NULL,79),
	 (87,186,'pink-10XL','2021-07-13 00:00:00',1086.0,'62082200',1086.0,'fc06b9ba-bdbd-4062-a013-ed14c441238c',NULL,79),
	 (88,187,'Pink-4XL','2021-07-13 00:00:00',1087.0,'620829',1087.0,'3a003a49-e08a-46d0-a302-f69aada840d6',NULL,79),
	 (89,188,'Blue-10XL','2021-07-13 00:00:00',1088.0,'6108',1088.0,'71fbb33f-ba85-4f6a-8677-6729dfd4b869',NULL,83),
	 (90,189,'Blue-4XL','2021-07-13 00:00:00',1089.0,'610831',1089.0,'abf7f045-9ef5-42fd-9d17-41f82316dee0',NULL,83);
INSERT INTO kb_catalog_inventory.product_combinations (id,available_stock,combination_string,inserted_on,price,sku,supplier_price,unique_identifier,updated_on,product_id) VALUES
	 (91,190,'pink-10XL','2021-07-13 00:00:00',1090.0,'610832',1090.0,'6fb461d3-7f27-4595-8b77-acbd86b05c93',NULL,83),
	 (92,191,'Pink-4XL','2021-07-13 00:00:00',1091.0,'61083210',1091.0,'52288ce0-d2ee-48db-b87b-74ca2df2c74a',NULL,83),
	 (93,192,'Blue-10XL','2021-07-13 00:00:00',1092.0,'610839',1092.0,'ad62d2ac-8a02-4fc8-a2ad-77375f95669d',NULL,87),
	 (94,193,'Blue-4XL','2021-07-13 00:00:00',1093.0,'6208',1093.0,'03c9d315-0530-4f2c-bc59-aadce71ff20c',NULL,87),
	 (95,194,'pink-10XL','2021-07-13 00:00:00',1094.0,'620821',1094.0,'d5b4e58a-695b-42cd-b806-e3d61e13eb1a',NULL,87),
	 (96,195,'Pink-4XL','2021-07-13 00:00:00',1095.0,'62082100',1095.0,'a7928442-fb1f-4b65-82e9-0c02c1aac0f4',NULL,87),
	 (97,196,'Blue-10XL','2021-07-13 00:00:00',1096.0,'620822',1096.0,'4a1755cd-7411-4af8-9558-8593dc05bbbb',NULL,91),
	 (98,197,'Blue-4XL','2021-07-13 00:00:00',1097.0,'62082200',1097.0,'6ab612ed-e588-4ecf-93fb-0d6085f99854',NULL,91),
	 (99,198,'pink-10XL','2021-07-13 00:00:00',1098.0,'620829',1098.0,'bde9b72a-b1d4-44fb-b330-c2192098b777',NULL,91),
	 (100,199,'Pink-4XL','2021-07-13 00:00:00',1099.0,'6108',1099.0,'6a622563-b209-424e-93e1-fbc2caed7f53',NULL,91);
INSERT INTO kb_catalog_inventory.product_combinations (id,available_stock,combination_string,inserted_on,price,sku,supplier_price,unique_identifier,updated_on,product_id) VALUES
	 (101,200,'Blue-10XL','2021-07-13 00:00:00',1100.0,'610831',1100.0,'2408e85e-f82d-403a-9668-71b918a16c06',NULL,95),
	 (102,201,'Blue-4XL','2021-07-13 00:00:00',1101.0,'610832',1101.0,'85544a15-ea0f-4b08-9946-bad8b333b717',NULL,95),
	 (103,202,'pink-10XL','2021-07-13 00:00:00',1102.0,'61083210',1102.0,'c9502594-f68c-4d8d-b405-03e1d8b9c516',NULL,95),
	 (104,203,'Pink-4XL','2021-07-13 00:00:00',1103.0,'610839',1103.0,'d5eb4dfb-73b3-4c88-a77f-2d04ba5f4843',NULL,95),
	 (105,204,'Blue-10XL','2021-07-13 00:00:00',1104.0,'6208',1104.0,'600036bc-24d7-4f47-9a55-0bd8e198f8e3',NULL,99),
	 (106,205,'Blue-4XL','2021-07-13 00:00:00',1105.0,'620821',1105.0,'e7b2935d-372d-4038-9cd1-e893fe31a96c',NULL,99),
	 (107,206,'pink-10XL','2021-07-13 00:00:00',1106.0,'62082100',1106.0,'51990514-8d72-47c0-a450-aed3de820c2f',NULL,99),
	 (108,207,'Pink-4XL','2021-07-13 00:00:00',1107.0,'620822',1107.0,'6353c1fe-e5d3-4483-87f3-4907f062b9ea',NULL,99),
	 (109,208,'Blue-10XL','2021-07-13 00:00:00',1108.0,'62082200',1108.0,'21ff988b-9d82-4368-9e7d-8f5c6a569678',NULL,103),
	 (110,209,'Blue-4XL','2021-07-13 00:00:00',1109.0,'620829',1109.0,'8e3643df-ab16-4c41-9f8f-ae2be560570a',NULL,103);
INSERT INTO kb_catalog_inventory.product_combinations (id,available_stock,combination_string,inserted_on,price,sku,supplier_price,unique_identifier,updated_on,product_id) VALUES
	 (111,210,'pink-10XL','2021-07-13 00:00:00',1110.0,'6108',1110.0,'c0a168ef-904b-4569-8185-efbb02eb13f8',NULL,103),
	 (112,211,'Pink-4XL','2021-07-13 00:00:00',1111.0,'610831',1111.0,'429caf13-f9ac-4d18-866f-758ea1aefc07',NULL,103),
	 (113,212,'Blue-10XL','2021-07-13 00:00:00',1112.0,'610832',1112.0,'c8cf3fb8-3ddf-4910-ac70-03a0fe3314c5',NULL,107),
	 (114,213,'Blue-4XL','2021-07-13 00:00:00',1113.0,'61083210',1113.0,'48b59b5a-3a2d-4fb6-b592-e8a60bcbece8',NULL,107),
	 (115,214,'pink-10XL','2021-07-13 00:00:00',1114.0,'610839',1114.0,'d556a80d-3aa6-45dc-a699-4dcf5c61585d',NULL,107),
	 (116,215,'Pink-4XL','2021-07-13 00:00:00',1115.0,'6208',1115.0,'71395a04-8fe5-4c71-8cd9-c5bcd87a36c3',NULL,107),
	 (117,216,'Blue-10XL','2021-07-13 00:00:00',1116.0,'620821',1116.0,'0be77b8f-1f3b-4079-b955-9530b5782ea0',NULL,111),
	 (118,217,'Blue-4XL','2021-07-13 00:00:00',1117.0,'62082100',1117.0,'f8556426-121d-4dfa-9956-691106f52d4c',NULL,111),
	 (119,218,'pink-10XL','2021-07-13 00:00:00',1118.0,'620822',1118.0,'e53c8279-4188-4c90-838e-beb80a736c88',NULL,111),
	 (120,219,'Pink-4XL','2021-07-13 00:00:00',1119.0,'62082200',1119.0,'2422a5c0-84de-4834-baeb-3d233546ecb1',NULL,111);
INSERT INTO kb_catalog_inventory.product_combinations (id,available_stock,combination_string,inserted_on,price,sku,supplier_price,unique_identifier,updated_on,product_id) VALUES
	 (121,220,'Blue-10XL','2021-07-13 00:00:00',1120.0,'620829',1120.0,'5b8beec3-4da2-4091-aeef-c1eb3e26be37',NULL,115),
	 (122,221,'Blue-4XL','2021-07-13 00:00:00',1121.0,'6108',1121.0,'0ae81ddb-f39f-4a97-9d69-3083760566a5',NULL,115),
	 (123,222,'pink-10XL','2021-07-13 00:00:00',1122.0,'610831',1122.0,'80ee110a-4327-42f1-bab6-8b36d5a03996',NULL,115),
	 (124,223,'Pink-4XL','2021-07-13 00:00:00',1123.0,'610832',1123.0,'0b4274f2-f102-4dd6-af41-3b52ff603587',NULL,115),
	 (125,224,'Blue-10XL','2021-07-13 00:00:00',1124.0,'61083210',1124.0,'2c265516-181c-4f1d-a1c2-48c8c7886fb3',NULL,119),
	 (126,225,'Blue-4XL','2021-07-13 00:00:00',1125.0,'610839',1125.0,'cd1fe5c9-4007-4655-8996-3af4f924d0c8',NULL,119),
	 (127,226,'pink-10XL','2021-07-13 00:00:00',1126.0,'6208',1126.0,'806b0827-70fc-4d84-9728-0ef64f9f4166',NULL,119),
	 (128,227,'Pink-4XL','2021-07-13 00:00:00',1127.0,'620821',1127.0,'d8353c07-5884-4fba-aa98-ca1c1fd5233f',NULL,119),
	 (129,228,'Blue-10XL','2021-07-13 00:00:00',1128.0,'62082100',1128.0,'5b82545b-1f4a-46be-bcfe-6f34bd467344',NULL,123),
	 (130,229,'Blue-4XL','2021-07-13 00:00:00',1129.0,'620822',1129.0,'21477b20-4d78-4c98-b0db-e6b517d5c7ee',NULL,123);
INSERT INTO kb_catalog_inventory.product_combinations (id,available_stock,combination_string,inserted_on,price,sku,supplier_price,unique_identifier,updated_on,product_id) VALUES
	 (131,230,'pink-10XL','2021-07-13 00:00:00',1130.0,'62082200',1130.0,'459ecb90-c150-4a42-8a43-2048a2e05fcf',NULL,123),
	 (132,231,'Pink-4XL','2021-07-13 00:00:00',1131.0,'620829',1131.0,'5bebc60b-b730-40b6-808d-4dfecaf7eb32',NULL,123),
	 (133,232,'Blue-10XL','2021-07-13 00:00:00',1132.0,'6108',1132.0,'15fbc4dc-22eb-4953-a697-6ef37c8acdcd',NULL,127),
	 (134,233,'Blue-4XL','2021-07-13 00:00:00',1133.0,'610831',1133.0,'17333f74-aab8-4be0-877f-f8c87fff91b9',NULL,127),
	 (135,234,'pink-10XL','2021-07-13 00:00:00',1134.0,'610832',1134.0,'8bf3a037-700f-4f7a-b6ff-4cea3c7f619a',NULL,127),
	 (136,235,'Pink-4XL','2021-07-13 00:00:00',1135.0,'61083210',1135.0,'de8ab9e2-ff5f-4980-af41-b502128ed937',NULL,127),
	 (137,236,'Blue-10XL','2021-07-13 00:00:00',1136.0,'610839',1136.0,'4e3295bc-e910-4da9-ad23-fff7737c1d42',NULL,131),
	 (138,237,'Blue-4XL','2021-07-13 00:00:00',1137.0,'6208',1137.0,'be89f514-6085-4825-b2b7-0e5ebdcc9cbb',NULL,131),
	 (139,238,'pink-10XL','2021-07-13 00:00:00',1138.0,'620821',1138.0,'264df57e-2ea3-48f7-a04e-c265a3de2d18',NULL,131),
	 (140,239,'Pink-4XL','2021-07-13 00:00:00',1139.0,'62082100',1139.0,'bcb43109-4634-433b-8dd5-3fcc81d73fda',NULL,131);
INSERT INTO kb_catalog_inventory.product_combinations (id,available_stock,combination_string,inserted_on,price,sku,supplier_price,unique_identifier,updated_on,product_id) VALUES
	 (141,240,'Blue-10XL','2021-07-13 00:00:00',1140.0,'620822',1140.0,'835ee1dc-cba0-4043-805e-8e8f035eab8c',NULL,135),
	 (142,241,'Blue-4XL','2021-07-13 00:00:00',1141.0,'62082200',1141.0,'3324a449-b3fb-4127-886d-a22b8365c6c2',NULL,135),
	 (143,242,'pink-10XL','2021-07-13 00:00:00',1142.0,'620829',1142.0,'347513a8-4965-4715-afb7-55b614044cc8',NULL,135),
	 (144,243,'Pink-4XL','2021-07-13 00:00:00',1143.0,'6108',1143.0,'34a8391f-38cb-47a0-a869-bd6da247111b',NULL,135),
	 (145,244,'Blue-10XL','2021-07-13 00:00:00',1144.0,'610831',1144.0,'aee01e07-f257-4a35-9744-79ed58114686',NULL,139),
	 (146,245,'Blue-4XL','2021-07-13 00:00:00',1145.0,'610832',1145.0,'0834d7a6-4c1b-4de9-9192-36a80b5ff556',NULL,139),
	 (147,246,'pink-10XL','2021-07-13 00:00:00',1146.0,'61083210',1146.0,'20bbb7cf-0e31-469b-a309-c3ded5e0bf24',NULL,139),
	 (148,247,'Pink-4XL','2021-07-13 00:00:00',1147.0,'610839',1147.0,'2691dbc0-a4f2-41da-9dfe-66a14af27982',NULL,139),
	 (149,248,'Blue-10XL','2021-07-13 00:00:00',1148.0,'6208',1148.0,'25912dda-5b57-481f-80ea-b1f3fe486156',NULL,143),
	 (150,249,'Blue-4XL','2021-07-13 00:00:00',1149.0,'620821',1149.0,'4ddf1ebb-a265-43a3-a43a-4df1cf2056e4',NULL,143);
INSERT INTO kb_catalog_inventory.product_combinations (id,available_stock,combination_string,inserted_on,price,sku,supplier_price,unique_identifier,updated_on,product_id) VALUES
	 (151,250,'pink-10XL','2021-07-13 00:00:00',1150.0,'62082100',1150.0,'3c94055f-a635-4241-a120-de0cf6a24df9',NULL,143),
	 (152,251,'Pink-4XL','2021-07-13 00:00:00',1151.0,'620822',1151.0,'bd93fe39-1ff1-426a-81e2-f2301adb9042',NULL,143),
	 (153,252,'Blue-10XL','2021-07-13 00:00:00',1152.0,'62082200',1152.0,'1a4cde49-9f86-4ff1-94f9-75c84eb554b7',NULL,147),
	 (154,253,'Blue-4XL','2021-07-13 00:00:00',1153.0,'620829',1153.0,'04683e02-6d2a-41fc-966c-5306b4d66220',NULL,147),
	 (155,254,'pink-10XL','2021-07-13 00:00:00',1154.0,'6108',1154.0,'e2def7d1-c404-417b-9c37-ae38376e4e4b',NULL,147),
	 (156,255,'Pink-4XL','2021-07-13 00:00:00',1155.0,'610831',1155.0,'1cb7dade-fcf6-4955-85a6-da24cd0f9b44',NULL,147),
	 (157,256,'Blue-10XL','2021-07-13 00:00:00',1156.0,'610832',1156.0,'ba097896-e0b9-45c1-beda-8ed82818f77e',NULL,151),
	 (158,257,'Blue-4XL','2021-07-13 00:00:00',1157.0,'61083210',1157.0,'9895b987-f779-4e68-8387-9cb70d67fa6e',NULL,151),
	 (159,258,'pink-10XL','2021-07-13 00:00:00',1158.0,'610839',1158.0,'f74a33f5-75d9-4c65-98a9-2a45154b084d',NULL,151),
	 (160,259,'Pink-4XL','2021-07-13 00:00:00',1159.0,'6208',1159.0,'25fd3926-830e-4a70-9bf4-585627351cc4',NULL,151);
INSERT INTO kb_catalog_inventory.product_combinations (id,available_stock,combination_string,inserted_on,price,sku,supplier_price,unique_identifier,updated_on,product_id) VALUES
	 (161,260,'Blue-10XL','2021-07-13 00:00:00',1160.0,'620821',1160.0,'affd9345-13fd-4cc0-93cb-aac5f593bf10',NULL,155),
	 (162,261,'Blue-4XL','2021-07-13 00:00:00',1161.0,'62082100',1161.0,'03001704-e42d-4186-ad77-bba8c1a89904',NULL,155),
	 (163,262,'pink-10XL','2021-07-13 00:00:00',1162.0,'620822',1162.0,'149efe86-57ed-4f21-9d7c-b2fcb1756859',NULL,155),
	 (164,263,'Pink-4XL','2021-07-13 00:00:00',1163.0,'62082200',1163.0,'931ebbe4-ed62-45d3-b15e-2293163a8e70',NULL,155),
	 (165,264,'Blue-10XL','2021-07-13 00:00:00',1164.0,'620829',1164.0,'3a6c25c2-cc35-4088-8e2a-7b3d111b9d84',NULL,159),
	 (166,265,'Blue-4XL','2021-07-13 00:00:00',1165.0,'6108',1165.0,'a281ffc4-659f-40f6-bb11-6885123915aa',NULL,159),
	 (167,266,'pink-10XL','2021-07-13 00:00:00',1166.0,'610831',1166.0,'d6beae6c-275b-4ffa-b87e-1dc5b0950c7e',NULL,159),
	 (168,267,'Pink-4XL','2021-07-13 00:00:00',1167.0,'610832',1167.0,'a5c93df8-89e9-4fed-9e8e-4b11b9bdc61c',NULL,159),
	 (169,268,'Blue-10XL','2021-07-13 00:00:00',1168.0,'61083210',1168.0,'3a42e8df-e6a4-4869-aed9-ef48974cc320',NULL,163),
	 (170,269,'Blue-4XL','2021-07-13 00:00:00',1169.0,'610839',1169.0,'fb07baf4-f5be-4992-a4a7-705740cb1fda',NULL,163);
INSERT INTO kb_catalog_inventory.product_combinations (id,available_stock,combination_string,inserted_on,price,sku,supplier_price,unique_identifier,updated_on,product_id) VALUES
	 (171,270,'pink-10XL','2021-07-13 00:00:00',1170.0,'6208',1170.0,'7b43850e-d2e1-4d7c-9bf4-ec09645eab1d',NULL,163),
	 (172,271,'Pink-4XL','2021-07-13 00:00:00',1171.0,'620821',1171.0,'fa77ea0c-6ae2-4cea-a1f1-c4f10274d461',NULL,163),
	 (173,272,'Blue-10XL','2021-07-13 00:00:00',1172.0,'62082100',1172.0,'e0ed50b7-b21b-4512-8bb3-0146250db69f',NULL,167),
	 (174,273,'Blue-4XL','2021-07-13 00:00:00',1173.0,'620822',1173.0,'5f4efe7b-a5e4-417a-a8b4-071c83e06bf0',NULL,167),
	 (175,274,'pink-10XL','2021-07-13 00:00:00',1174.0,'62082200',1174.0,'47866d1a-1cec-41aa-a3a9-46b5b40635f4',NULL,167),
	 (176,275,'Pink-4XL','2021-07-13 00:00:00',1175.0,'620829',1175.0,'07b4b39a-8dd3-4586-8588-296aebac86b2',NULL,167),
	 (177,276,'Blue-10XL','2021-07-13 00:00:00',1176.0,'6108',1176.0,'aadc158a-6d78-4ebe-bff8-35d4528db1ef',NULL,171),
	 (178,277,'Blue-4XL','2021-07-13 00:00:00',1177.0,'610831',1177.0,'069af663-0a0c-44f9-aa00-c43b4321f627',NULL,171),
	 (179,278,'pink-10XL','2021-07-13 00:00:00',1178.0,'610832',1178.0,'bcb9635e-fa6a-4d7a-8015-f76942974ea4',NULL,171),
	 (180,279,'Pink-4XL','2021-07-13 00:00:00',1179.0,'61083210',1179.0,'d637f659-a368-4c8a-8737-64586086257c',NULL,171);
INSERT INTO kb_catalog_inventory.product_combinations (id,available_stock,combination_string,inserted_on,price,sku,supplier_price,unique_identifier,updated_on,product_id) VALUES
	 (181,280,'Blue-10XL','2021-07-13 00:00:00',1180.0,'610839',1180.0,'2bb5f082-44da-45c9-bcd1-8dd287fdab42',NULL,175),
	 (182,281,'Blue-4XL','2021-07-13 00:00:00',1181.0,'6208',1181.0,'4add419a-0c72-424f-b885-0e4980265a54',NULL,175),
	 (183,282,'pink-10XL','2021-07-13 00:00:00',1182.0,'620821',1182.0,'ad3fb2e0-6f03-4c85-a7b6-348568b88916',NULL,175),
	 (184,283,'Pink-4XL','2021-07-13 00:00:00',1183.0,'62082100',1183.0,'e3756ad0-b488-4a5e-9d56-bce126b3b5de',NULL,175),
	 (185,284,'Blue-10XL','2021-07-13 00:00:00',1184.0,'620822',1184.0,'b84a81a3-80b0-4e8e-ae59-a0774d6736a0',NULL,179),
	 (186,285,'Blue-4XL','2021-07-13 00:00:00',1185.0,'62082200',1185.0,'5eef8622-6b01-47cf-8e13-c29b5369758e',NULL,179),
	 (187,286,'pink-10XL','2021-07-13 00:00:00',1186.0,'620829',1186.0,'6de108a9-9bdd-4c09-a762-e53d1e779264',NULL,179),
	 (188,287,'Pink-4XL','2021-07-13 00:00:00',1187.0,'6108',1187.0,'40fb06e6-2e9c-49a9-bd66-3f6cdb692226',NULL,179),
	 (189,288,'Blue-10XL','2021-07-13 00:00:00',1188.0,'610831',1188.0,'5131405d-732d-4e96-8344-2156cc2db801',NULL,183),
	 (190,289,'Blue-4XL','2021-07-13 00:00:00',1189.0,'610832',1189.0,'4783ee50-fca6-4253-a9ef-fd5e52873c83',NULL,183);
INSERT INTO kb_catalog_inventory.product_combinations (id,available_stock,combination_string,inserted_on,price,sku,supplier_price,unique_identifier,updated_on,product_id) VALUES
	 (191,290,'pink-10XL','2021-07-13 00:00:00',1190.0,'61083210',1190.0,'ac4cd6da-a876-48c5-93c6-d8d26cc6615c',NULL,183),
	 (192,291,'Pink-4XL','2021-07-13 00:00:00',1191.0,'610839',1191.0,'1ed644eb-087c-43d1-960d-09055f5c5f5f',NULL,183),
	 (193,292,'Blue-10XL','2021-07-13 00:00:00',1192.0,'6208',1192.0,'dd167bda-fe8a-4207-bcba-da3977005f94',NULL,187),
	 (194,293,'Blue-4XL','2021-07-13 00:00:00',1193.0,'620821',1193.0,'cdc547e5-c3d9-451f-bc8d-4eb8643dccfc',NULL,187),
	 (195,294,'pink-10XL','2021-07-13 00:00:00',1194.0,'62082100',1194.0,'45b1751a-ae54-48ac-a4fb-69927cde556a',NULL,187),
	 (196,295,'Pink-4XL','2021-07-13 00:00:00',1195.0,'620822',1195.0,'84b08f32-23ef-4e94-9d18-998b6fa36880',NULL,187),
	 (197,296,'Blue-10XL','2021-07-13 00:00:00',1196.0,'62082200',1196.0,'2a67a7fc-f10f-43d4-b01a-cf08ea6c9e12',NULL,191),
	 (198,297,'Blue-4XL','2021-07-13 00:00:00',1197.0,'620829',1197.0,'208cded6-236c-4271-9478-c423022ac840',NULL,191),
	 (199,298,'pink-10XL','2021-07-13 00:00:00',1198.0,'62082200',1198.0,'0e53d069-265f-4938-840f-951d9b282aee',NULL,191),
	 (200,299,'Pink-4XL','2021-07-13 00:00:00',1199.0,'620829',1199.0,'fc0ef567-e1b0-4d34-aefa-727dc5d8bc2e',NULL,191);
INSERT INTO kb_catalog_inventory.product_combinations (id,available_stock,combination_string,inserted_on,price,sku,supplier_price,unique_identifier,updated_on,product_id) VALUES
	 (201,300,NULL,'2021-07-13 00:00:00',1200.0,'620829',1200.0,'d162d5e0-f058-4bf0-9d05-76f0806888bd',NULL,201),
	 (202,301,NULL,'2021-07-13 00:00:00',1201.0,'620829',1201.0,'e8a39f9c-07ea-45aa-b704-e2f1a540b9b0',NULL,202),
	 (203,302,NULL,'2021-07-13 00:00:00',1202.0,'620829',1202.0,'8034aa77-f650-4906-aa9a-fcea6a8ff093',NULL,203),
	 (204,303,NULL,'2021-07-13 00:00:00',1203.0,'620829',1203.0,'88cd7119-0b2b-4f16-affa-19875b1788f8',NULL,204),
	 (205,304,NULL,'2021-07-13 00:00:00',1204.0,'620829',1204.0,'e4987485-fbed-434c-9403-69cc84ec8053',NULL,205),
	 (206,305,NULL,'2021-07-13 00:00:00',1205.0,'620829',1205.0,'9b9644ba-ae57-4425-993e-e82bfe2b3cd6',NULL,206),
	 (207,306,NULL,'2021-07-13 00:00:00',1206.0,'620829',1206.0,'b77dd965-ad70-4e98-b8d2-7c6902cfea84',NULL,207),
	 (208,307,NULL,'2021-07-13 00:00:00',1207.0,'620829',1207.0,'2b0a4309-d76a-4d8f-acf4-cc36ed6d243a',NULL,208),
	 (209,308,NULL,'2021-07-13 00:00:00',1208.0,'620829',1208.0,'398d8c0a-bc3a-49fb-aa76-a9fc09e42a66',NULL,209),
	 (210,309,NULL,'2021-07-13 00:00:00',1209.0,'620829',1209.0,'29ccbc88-8daa-44c8-b0b9-b041812a6f97',NULL,210);
INSERT INTO kb_catalog_inventory.product_combinations (id,available_stock,combination_string,inserted_on,price,sku,supplier_price,unique_identifier,updated_on,product_id) VALUES
	 (211,310,NULL,'2021-07-13 00:00:00',1210.0,'620829',1210.0,'1aaa2a4b-7831-4a20-994f-7405e1072a29',NULL,211),
	 (212,311,NULL,'2021-07-13 00:00:00',1211.0,'620829',1211.0,'991be1b4-eac9-4462-8e3a-8ade1d233bfe',NULL,212),
	 (213,312,NULL,'2021-07-13 00:00:00',1212.0,'620829',1212.0,'5b4d3bf2-2183-488f-8ab5-e7712f9f27c0',NULL,213),
	 (214,313,NULL,'2021-07-13 00:00:00',1213.0,'620829',1213.0,'d5e6fa9e-bc65-4758-8f3a-33b930ea66c8',NULL,214),
	 (215,314,NULL,'2021-07-13 00:00:00',1214.0,'620829',1214.0,'d38b39a3-10c5-450c-a122-dc1a2c989910',NULL,215),
	 (216,315,NULL,'2021-07-13 00:00:00',1215.0,'620829',1215.0,'579ba1a0-8439-4bb8-86b7-4aadd093ea31',NULL,216),
	 (217,316,NULL,'2021-07-13 00:00:00',1216.0,'620829',1216.0,'c4d752ee-fcbe-4e70-accd-68ceeb06e188',NULL,217),
	 (218,317,NULL,'2021-07-13 00:00:00',1217.0,'620829',1217.0,'60bb0acd-4de3-43ce-9e3b-414ded6a5e1a',NULL,218),
	 (219,318,NULL,'2021-07-13 00:00:00',1218.0,'620829',1218.0,'d5b7c9c7-7aac-48a9-981f-1a7006e79797',NULL,219),
	 (220,319,NULL,'2021-07-13 00:00:00',1219.0,'620829',1219.0,'29088f3a-38e8-4764-9505-37e666a8c41f',NULL,220);
INSERT INTO kb_catalog_inventory.product_combinations (id,available_stock,combination_string,inserted_on,price,sku,supplier_price,unique_identifier,updated_on,product_id) VALUES
	 (221,320,NULL,'2021-07-13 00:00:00',1220.0,'620829',1220.0,'27edac06-c322-4702-ac94-08d91f2273c0',NULL,221),
	 (222,321,NULL,'2021-07-13 00:00:00',1221.0,'620829',1221.0,'847fa802-d254-48dc-ac81-fc00f5874e17',NULL,222),
	 (223,322,NULL,'2021-07-13 00:00:00',1222.0,'620829',1222.0,'f3e0ee55-f3ec-43f7-9358-1f72e3ec7177',NULL,223),
	 (224,323,NULL,'2021-07-13 00:00:00',1223.0,'620829',1223.0,'8b35968f-4a3b-4197-bee4-45f9451b2b87',NULL,224),
	 (225,324,NULL,'2021-07-13 00:00:00',1224.0,'620829',1224.0,'57b37811-ab44-41e3-9373-2eee3afd2aae',NULL,225),
	 (226,325,NULL,'2021-07-13 00:00:00',1225.0,'620829',1225.0,'d800253c-eb5d-4cf5-a8a4-0c92ad635732',NULL,226),
	 (227,326,NULL,'2021-07-13 00:00:00',1226.0,'620829',1226.0,'e7673b08-18a4-45a3-9ece-9795b3714f4a',NULL,227),
	 (228,327,NULL,'2021-07-13 00:00:00',1227.0,'620829',1227.0,'7c2b45ee-1688-4f86-9274-5d5c6202ee42',NULL,228),
	 (229,328,NULL,'2021-07-13 00:00:00',1228.0,'620829',1228.0,'3bf0fa59-35b8-460f-b932-bad0fe31844a',NULL,229),
	 (230,329,NULL,'2021-07-13 00:00:00',1229.0,'620829',1229.0,'5aa59cf6-f7f7-461d-b23a-f2e7c0e2e2e7',NULL,230);
INSERT INTO kb_catalog_inventory.product_combinations (id,available_stock,combination_string,inserted_on,price,sku,supplier_price,unique_identifier,updated_on,product_id) VALUES
	 (231,330,NULL,'2021-07-13 00:00:00',1230.0,'620829',1230.0,'74cb69c2-3099-473a-93bf-c5c6f3ffe7de',NULL,231),
	 (232,331,NULL,'2021-07-13 00:00:00',1231.0,'620829',1231.0,'e9f5e9c6-0dd7-4fe0-a1dc-2d92d16d6cbb',NULL,232),
	 (233,332,NULL,'2021-07-13 00:00:00',1232.0,'620829',1232.0,'76116750-6b32-4aa0-acac-0b122897dc11',NULL,233),
	 (234,333,NULL,'2021-07-13 00:00:00',1233.0,'620829',1233.0,'12734484-22c8-4c77-a23f-66b81f20a23a',NULL,234),
	 (235,334,NULL,'2021-07-13 00:00:00',1234.0,'620829',1234.0,'15c0afd0-84be-4d54-be12-89a80f4547e9',NULL,235),
	 (236,335,NULL,'2021-07-13 00:00:00',1235.0,'620829',1235.0,'dedfbda5-dc96-4a9a-ad9e-ee0504e512e8',NULL,236),
	 (237,336,NULL,'2021-07-13 00:00:00',1236.0,'620829',1236.0,'54018250-10d9-4a9f-b857-5b75d6e62ab1',NULL,237),
	 (238,337,NULL,'2021-07-13 00:00:00',1237.0,'620829',1237.0,'0c47e99f-aab8-4ee3-b2e5-750f6dd99bbf',NULL,238),
	 (239,338,NULL,'2021-07-13 00:00:00',1238.0,'620829',1238.0,'dda743de-9a9d-4d5f-9516-5e6e95edd19b',NULL,239),
	 (240,339,NULL,'2021-07-13 00:00:00',1239.0,'620829',1239.0,'51681b01-d217-4fd7-ab54-2e96c1bb72b9',NULL,240);
INSERT INTO kb_catalog_inventory.product_combinations (id,available_stock,combination_string,inserted_on,price,sku,supplier_price,unique_identifier,updated_on,product_id) VALUES
	 (241,340,NULL,'2021-07-13 00:00:00',1240.0,'620829',1240.0,'1ab0bc2a-9024-4b8c-b032-92a7c0b3d124',NULL,241),
	 (242,341,NULL,'2021-07-13 00:00:00',1241.0,'620829',1241.0,'05f6dc5d-3060-4325-8535-f58e6e1ff9be',NULL,242),
	 (243,342,NULL,'2021-07-13 00:00:00',1242.0,'620829',1242.0,'1e409927-7a6e-4181-ab6e-2e33679dbe04',NULL,243),
	 (244,343,NULL,'2021-07-13 00:00:00',1243.0,'620829',1243.0,'3285aca5-9f5a-4739-a221-d778f6695434',NULL,244),
	 (245,344,NULL,'2021-07-13 00:00:00',1244.0,'620829',1244.0,'68f6992d-8d11-4db6-b824-dd4182fac498',NULL,245),
	 (246,345,NULL,'2021-07-13 00:00:00',1245.0,'620829',1245.0,'5c980f94-d2e0-4ed5-a56f-09a0d86ed286',NULL,246),
	 (247,346,NULL,'2021-07-13 00:00:00',1246.0,'620829',1246.0,'26e74f78-e55f-459b-84ce-1b2dffdc8978',NULL,247),
	 (248,347,NULL,'2021-07-13 00:00:00',1247.0,'620829',1247.0,'2891d26f-ba1b-4d5a-a139-93e0639cfba3',NULL,248),
	 (249,348,NULL,'2021-07-13 00:00:00',1248.0,'620829',1248.0,'695c7fc1-9130-4e5c-a2cc-60bff2340c44',NULL,249),
	 (250,349,NULL,'2021-07-13 00:00:00',1249.0,'620829',1249.0,'a07319b4-4fb4-4d51-8e53-0d40c4b7c5c6',NULL,250);
INSERT INTO kb_catalog_inventory.product_combinations (id,available_stock,combination_string,inserted_on,price,sku,supplier_price,unique_identifier,updated_on,product_id) VALUES
	 (251,350,NULL,'2021-07-13 00:00:00',1250.0,'620829',1250.0,'54181c15-a354-4a32-b6c0-0aea1ed3ded5',NULL,251),
	 (252,351,NULL,'2021-07-13 00:00:00',1251.0,'620829',1251.0,'511d7fcb-3fe9-4a23-9a78-6269ba4b65bf',NULL,252),
	 (253,352,NULL,'2021-07-13 00:00:00',1252.0,'620829',1252.0,'20e38936-2090-4e5d-8996-8335caee87b8',NULL,253),
	 (254,353,NULL,'2021-07-13 00:00:00',1253.0,'620829',1253.0,'1424ba31-d6aa-4569-b9f9-d19dce51b996',NULL,254),
	 (255,354,NULL,'2021-07-13 00:00:00',1254.0,'620829',1254.0,'3456e3d5-a975-4695-9ac9-f0463c4652f7',NULL,255),
	 (256,355,NULL,'2021-07-13 00:00:00',1255.0,'620829',1255.0,'ca1b7137-ed72-4d9d-bd03-03e8c9cbe08e',NULL,256),
	 (257,356,NULL,'2021-07-13 00:00:00',1256.0,'620829',1256.0,'328a7a20-4c58-453d-8fc4-866d7185dffe',NULL,257),
	 (258,357,NULL,'2021-07-13 00:00:00',1257.0,'620829',1257.0,'2938dacc-1e29-4e04-8bc9-ce7a8c146c09',NULL,258),
	 (259,358,NULL,'2021-07-13 00:00:00',1258.0,'620829',1258.0,'ca585677-64c5-4f30-8f31-4de1496ba5a6',NULL,259),
	 (260,359,NULL,'2021-07-13 00:00:00',1259.0,'620829',1259.0,'c66bfbc8-64e7-47ec-b2f6-9e50101c6034',NULL,260);
INSERT INTO kb_catalog_inventory.product_combinations (id,available_stock,combination_string,inserted_on,price,sku,supplier_price,unique_identifier,updated_on,product_id) VALUES
	 (261,360,NULL,'2021-07-13 00:00:00',1260.0,'620829',1260.0,'2d6302cd-8d9e-4773-a215-7fb438589ac3',NULL,261),
	 (262,361,NULL,'2021-07-13 00:00:00',1261.0,'620829',1261.0,'0ffc1609-da4a-4c73-b25d-b0c9829fe7f3',NULL,262),
	 (263,362,NULL,'2021-07-13 00:00:00',1262.0,'620829',1262.0,'89300395-9376-433d-b8b2-9ba104ac2add',NULL,263),
	 (264,363,NULL,'2021-07-13 00:00:00',1263.0,'620829',1263.0,'b48be153-01e6-4c94-88ec-5877b275a3d9',NULL,264),
	 (265,364,NULL,'2021-07-13 00:00:00',1264.0,'620829',1264.0,'35c31cbd-e062-4c2a-9ba9-87b64af936f7',NULL,265),
	 (266,365,NULL,'2021-07-13 00:00:00',1265.0,'620829',1265.0,'4b16a838-e9d7-4d1d-872f-d869c60975dc',NULL,266),
	 (267,366,NULL,'2021-07-13 00:00:00',1266.0,'620829',1266.0,'ee9f3313-ada8-4c80-ad80-dc78feaece5b',NULL,267),
	 (268,367,NULL,'2021-07-13 00:00:00',1267.0,'620829',1267.0,'5c777d08-df74-4c8b-9968-2206ce6c6a13',NULL,268),
	 (269,368,NULL,'2021-07-13 00:00:00',1268.0,'620829',1268.0,'0fcc248b-b6a3-4a4e-9b62-c8359286a386',NULL,269),
	 (270,369,NULL,'2021-07-13 00:00:00',1269.0,'620829',1269.0,'27f6e826-f062-4f07-9e07-2579a3459334',NULL,270);
INSERT INTO kb_catalog_inventory.product_combinations (id,available_stock,combination_string,inserted_on,price,sku,supplier_price,unique_identifier,updated_on,product_id) VALUES
	 (271,370,NULL,'2021-07-13 00:00:00',1270.0,'620829',1270.0,'f0bbb286-5c1c-4b91-918c-2cbb7822e032',NULL,271),
	 (272,371,NULL,'2021-07-13 00:00:00',1271.0,'620829',1271.0,'98f2e512-935c-460f-8b8b-beabcc4f7724',NULL,272),
	 (273,372,NULL,'2021-07-13 00:00:00',1272.0,'620829',1272.0,'119819f6-f23a-43a6-bb67-568106c9348b',NULL,273),
	 (274,373,NULL,'2021-07-13 00:00:00',1273.0,'620829',1273.0,'82560777-cfaa-4c44-b27d-ee5f0a4a955e',NULL,274),
	 (275,374,NULL,'2021-07-13 00:00:00',1274.0,'620829',1274.0,'2971b334-b0ea-4d6b-b8a6-d2ae09b1c20b',NULL,275),
	 (276,375,NULL,'2021-07-13 00:00:00',1275.0,'620829',1275.0,'85f6fba6-2eca-45f5-a076-7ef7e8564f71',NULL,276),
	 (277,376,NULL,'2021-07-13 00:00:00',1276.0,'620829',1276.0,'7a4d9592-5b85-4d8f-99ec-f0cf07142000',NULL,277),
	 (278,377,NULL,'2021-07-13 00:00:00',1277.0,'620829',1277.0,'5463e6e8-1999-4af5-9599-958c172bbdd4',NULL,278),
	 (279,378,NULL,'2021-07-13 00:00:00',1278.0,'620829',1278.0,'55dab18e-b4cb-44cb-a0b5-0ba6be591bb7',NULL,279),
	 (280,379,NULL,'2021-07-13 00:00:00',1279.0,'620829',1279.0,'8c773fde-12e2-4f76-8d13-1b5e1182168c',NULL,280);
INSERT INTO kb_catalog_inventory.product_combinations (id,available_stock,combination_string,inserted_on,price,sku,supplier_price,unique_identifier,updated_on,product_id) VALUES
	 (281,380,NULL,'2021-07-13 00:00:00',1280.0,'620829',1280.0,'0d137d1c-75e0-400b-a09b-6e063230fb40',NULL,281),
	 (282,381,NULL,'2021-07-13 00:00:00',1281.0,'620829',1281.0,'e36d3d06-5ee8-435c-a0c4-de4f3f6fa931',NULL,282),
	 (283,382,NULL,'2021-07-13 00:00:00',1282.0,'620829',1282.0,'ea0df24e-66d2-412e-80de-f3716dbed1ea',NULL,283),
	 (284,383,NULL,'2021-07-13 00:00:00',1283.0,'620829',1283.0,'ebbca3eb-b9a6-4c6c-8622-e81d50da644e',NULL,284),
	 (285,384,NULL,'2021-07-13 00:00:00',1284.0,'620829',1284.0,'f82c345b-5362-45cf-8605-6180bc6ca8a0',NULL,285),
	 (286,385,NULL,'2021-07-13 00:00:00',1285.0,'620829',1285.0,'0879567e-867d-4b1d-b3f3-835d1d8b3104',NULL,286),
	 (287,386,NULL,'2021-07-13 00:00:00',1286.0,'620829',1286.0,'74fc0720-cf21-40a2-82c4-18ccb883dc6f',NULL,287),
	 (288,387,NULL,'2021-07-13 00:00:00',1287.0,'620829',1287.0,'caeb822b-bb81-4656-9375-72ba25329f91',NULL,288),
	 (289,388,NULL,'2021-07-13 00:00:00',1288.0,'620829',1288.0,'5932aa6f-1bca-4e94-82a1-a5b8c2dfb2da',NULL,289),
	 (290,389,NULL,'2021-07-13 00:00:00',1289.0,'620829',1289.0,'8021702e-97ea-4b21-8d6d-5da904a22801',NULL,290);
INSERT INTO kb_catalog_inventory.product_combinations (id,available_stock,combination_string,inserted_on,price,sku,supplier_price,unique_identifier,updated_on,product_id) VALUES
	 (291,390,NULL,'2021-07-13 00:00:00',1290.0,'620829',1290.0,'21c873c4-071c-4feb-a87c-c1f464b0ab9f',NULL,291),
	 (292,391,NULL,'2021-07-13 00:00:00',1291.0,'620829',1291.0,'285aa0b4-f0f2-47ea-82af-5c4bcacc335c',NULL,292),
	 (293,392,NULL,'2021-07-13 00:00:00',1292.0,'620829',1292.0,'857a58ec-341c-4b5d-ad02-9b09f7bb6c2a',NULL,293);





INSERT INTO kb_catalog_inventory.product_stock (id,total_price,total_stock,unit_price,product_combination_id) VALUES
	 (1,50000.0,500,100.0,1),
	 (2,100000.0,500,200.0,2),
	 (3,150000.0,500,300.0,3),
	 (4,200000.0,500,400.0,4),
	 (5,250000.0,500,500.0,5),
	 (6,300000.0,500,600.0,6),
	 (7,350000.0,500,700.0,7),
	 (8,400000.0,500,800.0,8),
	 (9,450000.0,500,900.0,9),
	 (10,500000.0,500,1000.0,10);
INSERT INTO kb_catalog_inventory.product_stock (id,total_price,total_stock,unit_price,product_combination_id) VALUES
	 (11,550000.0,500,1100.0,11),
	 (12,600000.0,500,1200.0,12),
	 (13,650000.0,500,1300.0,13),
	 (14,700000.0,500,1400.0,14),
	 (15,750000.0,500,1500.0,15),
	 (16,800000.0,500,1600.0,16),
	 (17,850000.0,500,1700.0,17),
	 (18,900000.0,500,1800.0,18),
	 (19,950000.0,500,1900.0,19),
	 (20,1000000.0,500,2000.0,20);
INSERT INTO kb_catalog_inventory.product_stock (id,total_price,total_stock,unit_price,product_combination_id) VALUES
	 (21,1050000.0,500,2100.0,21),
	 (22,1100000.0,500,2200.0,22),
	 (23,1150000.0,500,2300.0,23),
	 (24,50000.0,500,100.0,24),
	 (25,100000.0,500,200.0,25),
	 (26,150000.0,500,300.0,26),
	 (27,200000.0,500,400.0,27),
	 (28,250000.0,500,500.0,28),
	 (29,300000.0,500,600.0,29),
	 (30,350000.0,500,700.0,30);
INSERT INTO kb_catalog_inventory.product_stock (id,total_price,total_stock,unit_price,product_combination_id) VALUES
	 (31,400000.0,500,800.0,31),
	 (32,450000.0,500,900.0,32),
	 (33,500000.0,500,1000.0,33),
	 (34,550000.0,500,1100.0,34),
	 (35,600000.0,500,1200.0,35),
	 (36,650000.0,500,1300.0,36),
	 (37,700000.0,500,1400.0,37),
	 (38,750000.0,500,1500.0,38),
	 (39,800000.0,500,1600.0,39),
	 (40,850000.0,500,1700.0,40);
INSERT INTO kb_catalog_inventory.product_stock (id,total_price,total_stock,unit_price,product_combination_id) VALUES
	 (41,900000.0,500,1800.0,41),
	 (42,950000.0,500,1900.0,42),
	 (43,1000000.0,500,2000.0,43),
	 (44,1050000.0,500,2100.0,44),
	 (45,1100000.0,500,2200.0,45),
	 (46,1150000.0,500,2300.0,46),
	 (47,50000.0,500,100.0,47),
	 (48,100000.0,500,200.0,48),
	 (49,150000.0,500,300.0,49),
	 (50,200000.0,500,400.0,50);
INSERT INTO kb_catalog_inventory.product_stock (id,total_price,total_stock,unit_price,product_combination_id) VALUES
	 (51,250000.0,500,500.0,51),
	 (52,300000.0,500,600.0,52),
	 (53,350000.0,500,700.0,53),
	 (54,400000.0,500,800.0,54),
	 (55,450000.0,500,900.0,55),
	 (56,500000.0,500,1000.0,56),
	 (57,550000.0,500,1100.0,57),
	 (58,600000.0,500,1200.0,58),
	 (59,650000.0,500,1300.0,59),
	 (60,700000.0,500,1400.0,60);
INSERT INTO kb_catalog_inventory.product_stock (id,total_price,total_stock,unit_price,product_combination_id) VALUES
	 (61,750000.0,500,1500.0,61),
	 (62,800000.0,500,1600.0,62),
	 (63,850000.0,500,1700.0,63),
	 (64,900000.0,500,1800.0,64),
	 (65,950000.0,500,1900.0,65),
	 (66,1000000.0,500,2000.0,66),
	 (67,1050000.0,500,2100.0,67),
	 (68,1100000.0,500,2200.0,68),
	 (69,1150000.0,500,2300.0,69),
	 (70,50000.0,500,100.0,70);
INSERT INTO kb_catalog_inventory.product_stock (id,total_price,total_stock,unit_price,product_combination_id) VALUES
	 (71,100000.0,500,200.0,71),
	 (72,150000.0,500,300.0,72),
	 (73,200000.0,500,400.0,73),
	 (74,250000.0,500,500.0,74),
	 (75,300000.0,500,600.0,75),
	 (76,350000.0,500,700.0,76),
	 (77,400000.0,500,800.0,77),
	 (78,450000.0,500,900.0,78),
	 (79,500000.0,500,1000.0,79),
	 (80,550000.0,500,1100.0,80);
INSERT INTO kb_catalog_inventory.product_stock (id,total_price,total_stock,unit_price,product_combination_id) VALUES
	 (81,600000.0,500,1200.0,81),
	 (82,650000.0,500,1300.0,82),
	 (83,700000.0,500,1400.0,83),
	 (84,750000.0,500,1500.0,84),
	 (85,800000.0,500,1600.0,85),
	 (86,850000.0,500,1700.0,86),
	 (87,900000.0,500,1800.0,87),
	 (88,950000.0,500,1900.0,88),
	 (89,1000000.0,500,2000.0,89),
	 (90,1050000.0,500,2100.0,90);
INSERT INTO kb_catalog_inventory.product_stock (id,total_price,total_stock,unit_price,product_combination_id) VALUES
	 (91,1100000.0,500,2200.0,91),
	 (92,1150000.0,500,2300.0,92),
	 (93,50000.0,500,100.0,93),
	 (94,100000.0,500,200.0,94),
	 (95,150000.0,500,300.0,95),
	 (96,200000.0,500,400.0,96),
	 (97,250000.0,500,500.0,97),
	 (98,300000.0,500,600.0,98),
	 (99,350000.0,500,700.0,99),
	 (100,400000.0,500,800.0,100);
INSERT INTO kb_catalog_inventory.product_stock (id,total_price,total_stock,unit_price,product_combination_id) VALUES
	 (101,450000.0,500,900.0,101),
	 (102,500000.0,500,1000.0,102),
	 (103,550000.0,500,1100.0,103),
	 (104,600000.0,500,1200.0,104),
	 (105,650000.0,500,1300.0,105),
	 (106,700000.0,500,1400.0,106),
	 (107,750000.0,500,1500.0,107),
	 (108,800000.0,500,1600.0,108),
	 (109,850000.0,500,1700.0,109),
	 (110,900000.0,500,1800.0,110);
INSERT INTO kb_catalog_inventory.product_stock (id,total_price,total_stock,unit_price,product_combination_id) VALUES
	 (111,950000.0,500,1900.0,111),
	 (112,1000000.0,500,2000.0,112),
	 (113,1050000.0,500,2100.0,113),
	 (114,1100000.0,500,2200.0,114),
	 (115,1150000.0,500,2300.0,115),
	 (116,50000.0,500,100.0,116),
	 (117,100000.0,500,200.0,117),
	 (118,150000.0,500,300.0,118),
	 (119,200000.0,500,400.0,119),
	 (120,250000.0,500,500.0,120);
INSERT INTO kb_catalog_inventory.product_stock (id,total_price,total_stock,unit_price,product_combination_id) VALUES
	 (121,300000.0,500,600.0,121),
	 (122,350000.0,500,700.0,122),
	 (123,400000.0,500,800.0,123),
	 (124,450000.0,500,900.0,124),
	 (125,500000.0,500,1000.0,125),
	 (126,550000.0,500,1100.0,126),
	 (127,600000.0,500,1200.0,127),
	 (128,650000.0,500,1300.0,128),
	 (129,700000.0,500,1400.0,129),
	 (130,750000.0,500,1500.0,130);
INSERT INTO kb_catalog_inventory.product_stock (id,total_price,total_stock,unit_price,product_combination_id) VALUES
	 (131,800000.0,500,1600.0,131),
	 (132,850000.0,500,1700.0,132),
	 (133,900000.0,500,1800.0,133),
	 (134,950000.0,500,1900.0,134),
	 (135,1000000.0,500,2000.0,135),
	 (136,1050000.0,500,2100.0,136),
	 (137,1100000.0,500,2200.0,137),
	 (138,1150000.0,500,2300.0,138),
	 (139,50000.0,500,100.0,139),
	 (140,100000.0,500,200.0,140);
INSERT INTO kb_catalog_inventory.product_stock (id,total_price,total_stock,unit_price,product_combination_id) VALUES
	 (141,150000.0,500,300.0,141),
	 (142,200000.0,500,400.0,142),
	 (143,250000.0,500,500.0,143),
	 (144,300000.0,500,600.0,144),
	 (145,350000.0,500,700.0,145),
	 (146,400000.0,500,800.0,146),
	 (147,450000.0,500,900.0,147),
	 (148,500000.0,500,1000.0,148),
	 (149,550000.0,500,1100.0,149),
	 (150,600000.0,500,1200.0,150);
INSERT INTO kb_catalog_inventory.product_stock (id,total_price,total_stock,unit_price,product_combination_id) VALUES
	 (151,650000.0,500,1300.0,151),
	 (152,700000.0,500,1400.0,152),
	 (153,750000.0,500,1500.0,153),
	 (154,800000.0,500,1600.0,154),
	 (155,850000.0,500,1700.0,155),
	 (156,900000.0,500,1800.0,156),
	 (157,950000.0,500,1900.0,157),
	 (158,1000000.0,500,2000.0,158),
	 (159,1050000.0,500,2100.0,159),
	 (160,1100000.0,500,2200.0,160);
INSERT INTO kb_catalog_inventory.product_stock (id,total_price,total_stock,unit_price,product_combination_id) VALUES
	 (161,1150000.0,500,2300.0,161),
	 (162,50000.0,500,100.0,162),
	 (163,100000.0,500,200.0,163),
	 (164,150000.0,500,300.0,164),
	 (165,200000.0,500,400.0,165),
	 (166,250000.0,500,500.0,166),
	 (167,300000.0,500,600.0,167),
	 (168,350000.0,500,700.0,168),
	 (169,400000.0,500,800.0,169),
	 (170,450000.0,500,900.0,170);
INSERT INTO kb_catalog_inventory.product_stock (id,total_price,total_stock,unit_price,product_combination_id) VALUES
	 (171,500000.0,500,1000.0,171),
	 (172,550000.0,500,1100.0,172),
	 (173,600000.0,500,1200.0,173),
	 (174,650000.0,500,1300.0,174),
	 (175,700000.0,500,1400.0,175),
	 (176,750000.0,500,1500.0,176),
	 (177,800000.0,500,1600.0,177),
	 (178,850000.0,500,1700.0,178),
	 (179,900000.0,500,1800.0,179),
	 (180,950000.0,500,1900.0,180);
INSERT INTO kb_catalog_inventory.product_stock (id,total_price,total_stock,unit_price,product_combination_id) VALUES
	 (181,1000000.0,500,2000.0,181),
	 (182,1050000.0,500,2100.0,182),
	 (183,1100000.0,500,2200.0,183),
	 (184,1150000.0,500,2300.0,184),
	 (185,50000.0,500,100.0,185),
	 (186,100000.0,500,200.0,186),
	 (187,150000.0,500,300.0,187),
	 (188,200000.0,500,400.0,188),
	 (189,250000.0,500,500.0,189),
	 (190,300000.0,500,600.0,190);
INSERT INTO kb_catalog_inventory.product_stock (id,total_price,total_stock,unit_price,product_combination_id) VALUES
	 (191,350000.0,500,700.0,191),
	 (192,400000.0,500,800.0,192),
	 (193,450000.0,500,900.0,193),
	 (194,500000.0,500,1000.0,194),
	 (195,550000.0,500,1100.0,195),
	 (196,600000.0,500,1200.0,196),
	 (197,650000.0,500,1300.0,197),
	 (198,700000.0,500,1400.0,198),
	 (199,750000.0,500,1500.0,199),
	 (200,800000.0,500,1600.0,200);
	 
	 INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(1, 1, 1, 'discount1', 2, 'b1b5c46e-c41e-4813-add6-1201d6c856f6', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(2, 1, 1, 'discount2', 2, '12635d4c-6735-40bc-8f8a-73b166e70c1e', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(3, 1, 1, 'discount3', 2, '6184c7c9-43c9-40bf-b773-78aee0c23198', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(4, 1, 1, 'discount4', 2, 'ef33aa5d-191a-462c-892c-a8f8a1f0f9d7', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(5, 1, 1, 'discount5', 2, '7423fcbc-19bb-4057-be5c-359a0ecca9fd', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(6, 1, 1, 'discount6', 2, 'e54a5c74-d2cd-444a-bf4d-9ece20209921', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(7, 1, 1, 'discount7', 2, '24543711-f8d8-4822-9ee5-2f9f8c080836', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(8, 1, 1, 'discount8', 2, '6e46b2b0-a184-4034-a48e-5b467fa8a95a', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(9, 1, 1, 'discount9', 2, '3d5049cd-3170-48e0-a029-badfca3a1fc9', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(10, 1, 1, 'discount10', 2, '52bfed2d-08fa-468d-9b09-208a28db79c8', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(11, 1, 1, 'discount11', 2, 'c305e5b7-5462-4fcc-9cfa-30359413adf3', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(12, 1, 1, 'discount12', 2, '5c5797a2-79e9-466a-b32f-5c3aea35457a', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(13, 1, 1, 'discount13', 2, 'd31f3e81-2cd8-45df-b4c5-fe142d7249fc', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(14, 1, 1, 'discount14', 2, '6f255ec4-6aa8-47d0-b007-16f41c1d7c23', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(15, 1, 1, 'discount15', 2, '664ae908-011b-40b1-b07c-e05c3ad559ef', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(16, 1, 1, 'discount16', 2, '68fd2838-ef6e-44fc-8aea-90b215165f63', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(17, 1, 1, 'discount17', 2, '76ff6221-fa88-4480-99de-2b29d351be49', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(18, 1, 1, 'discount18', 2, '769ae7bd-80a0-423f-9c73-ac0405e8b2c2', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(19, 1, 1, 'discount19', 2, '02055bd3-6ee2-40dc-816b-eb261e96a964', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(20, 1, 1, 'discount20', 2, 'ef5921f5-639a-4f41-9953-d43994a95736', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(21, 1, 1, 'discount21', 2, '74efd859-2c8c-4a93-ab52-347453f9387b', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(22, 1, 1, 'discount22', 2, '3c294390-5ed9-4d1d-bd6f-de640450976a', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(23, 1, 1, 'discount23', 2, 'b6c8e2de-14db-4284-94e0-2cd3bdc84e5c', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(24, 1, 1, 'discount24', 2, 'e8de66c2-f47f-4c12-97e5-5f94d4c68d98', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(25, 1, 1, 'discount25', 2, '8ad12008-dc33-48b2-b997-c6d54d486e27', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(26, 1, 1, 'discount26', 2, '5dad084d-8fde-4dfb-b8a1-b84ded29c109', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(27, 1, 1, 'discount27', 2, 'ae726bb9-7d40-4999-a94d-c25719d5f4bd', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(28, 1, 1, 'discount28', 2, 'a49f8ca6-5462-487c-9730-65c230a905d3', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(29, 1, 1, 'discount29', 2, '17aec762-a85f-4263-9c61-4e0f63896497', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(30, 1, 1, 'discount30', 2, '499d7a14-665f-4e26-b5e6-6080b1feb843', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(31, 1, 1, 'discount31', 2, '00d5b7d5-ab06-441e-b115-fe4f3ea62a13', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(32, 1, 1, 'discount32', 2, '33561c79-d12d-49b6-a364-1fa53c268a6a', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(33, 1, 1, 'discount33', 2, '23582c48-43d2-460a-bee3-d3dd2b5353c7', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(34, 1, 1, 'discount34', 2, '13636a98-a0bf-41bc-a1a9-2111e8942c11', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(35, 1, 1, 'discount35', 2, '53822c21-07e1-4fe2-a385-a520d559b9f2', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(36, 1, 1, 'discount36', 2, '2d8c32f3-dd16-4ead-a77f-725f33b7454d', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(37, 1, 1, 'discount37', 2, '5fbb2c62-ece8-4c3a-95db-8387d0111d37', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(38, 1, 1, 'discount38', 2, 'ccbef089-4ae3-4771-8ee6-9c5a97c0a603', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(39, 1, 1, 'discount39', 2, 'c4762151-dd39-4270-95cf-a0adfeed1674', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(40, 1, 1, 'discount40', 2, '38729519-c5a8-4022-844c-d5db28a8e0df', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(41, 1, 1, 'discount41', 2, '5638ef00-f0c3-41aa-a165-abc154522ec6', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(42, 1, 1, 'discount42', 2, 'ed6c6ecc-affe-4a28-998c-7796c4f899ab', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(43, 1, 1, 'discount43', 2, '2409b143-a7b9-4869-a621-f5a45f70765b', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(44, 1, 1, 'discount44', 2, '6224cc5d-9abb-41e7-9d1c-6e97ae698048', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(45, 1, 1, 'discount45', 2, '9837d4a4-c344-479b-9251-1e0554334da8', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(46, 1, 1, 'discount46', 2, 'ae7ffe80-f18e-4657-9fe5-303353597d11', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(47, 1, 1, 'discount47', 2, '0281f96b-2b3e-42fa-8364-2e130ba65553', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(48, 1, 1, 'discount48', 2, '504022c1-eb72-4863-935b-f9cf6e101421', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(49, 1, 1, 'discount49', 2, '3033b527-fe02-4863-a475-fea514e3e831', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(50, 1, 1, 'discount50', 2, '7a1e1d6c-4167-421e-9085-9adac6c64335', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(51, 1, 1, 'discount51', 2, '84567201-9bca-4d91-8a1a-6821ed567dcf', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(52, 1, 1, 'discount52', 2, '0964fa77-d925-40b6-bc0b-fddf30c45e44', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(53, 1, 1, 'discount53', 2, '9ad50dac-02f9-4687-b245-d7939f8dbac6', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(54, 1, 1, 'discount54', 2, '59376a3b-124a-40a0-81db-ac5f89fa55f0', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(55, 1, 1, 'discount55', 2, '8c721b46-910e-42e4-977f-e59bf5b2c1da', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(56, 1, 1, 'discount56', 2, '426b8993-b6e7-499d-b23e-771f5feab584', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(57, 1, 1, 'discount57', 2, 'fc9c8de1-55ff-40dc-9817-59d9597e92a7', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(58, 1, 1, 'discount58', 2, '6d3112d5-7345-465f-898d-130831540216', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(59, 1, 1, 'discount59', 2, '6f997555-f0ee-4b9b-8b59-b97586b09313', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(60, 1, 1, 'discount60', 2, 'dfc84522-b564-47ac-81d3-fb3c5a9732aa', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(61, 1, 1, 'discount61', 2, '13db386d-35c5-4adf-a276-2c5217875a0e', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(62, 1, 1, 'discount62', 2, '17344a21-991b-47bb-9b5f-3b54501eb810', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(63, 1, 1, 'discount63', 2, 'cd44194d-85c4-4015-a614-780f41fc617e', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(64, 1, 1, 'discount64', 2, 'f677b0d8-4f09-4d21-8f8d-251fed2f50c8', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(65, 1, 1, 'discount65', 2, '9756be6c-4ffb-4cea-ad30-c49dbb90bc06', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(66, 1, 1, 'discount66', 2, 'a2efae81-a702-4429-a384-f8b4fd72d957', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(67, 1, 1, 'discount67', 2, 'd804d4f3-f229-49f8-81d8-0992d0d2416e', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(68, 1, 1, 'discount68', 2, 'dc1c5dab-c44e-4b7a-b22e-c3be5fbbdbe9', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(69, 1, 1, 'discount69', 2, '7bfdcd1e-25ec-40e0-9808-1c1c0962e90b', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(70, 1, 1, 'discount70', 2, '9e47269d-444f-492b-a171-4256bc104fd0', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(71, 1, 1, 'discount71', 2, 'de9f7aff-14c7-46ae-8efd-bd84f3370b56', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(72, 1, 1, 'discount72', 2, '3186a3bb-0621-47d2-9f64-7b393ee59d48', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(73, 1, 1, 'discount73', 2, 'c401e9d6-c8c6-4bd5-b4bf-2e91322d8ee1', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(74, 1, 1, 'discount74', 2, '54792e7a-3dc9-47d5-8156-c7994b3e4c4f', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(75, 1, 1, 'discount75', 2, '35189295-8840-4108-a9cb-562c915a399f', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(76, 1, 1, 'discount76', 2, '9f4f0589-4ce9-420e-b2aa-5ea7885c3959', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(77, 1, 1, 'discount77', 2, '64d7fe71-adf8-4b8b-95f6-d2fef5d40138', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(78, 1, 1, 'discount78', 2, 'd6d35017-9d2a-48fa-ab02-85c242646701', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(79, 1, 1, 'discount79', 2, '5a39f334-944d-4058-8590-d423efc6b6cd', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(80, 1, 1, 'discount80', 2, 'e2dca66a-5c31-4f59-b46e-06f2e6b26c04', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(81, 1, 1, 'discount81', 2, 'c91baf01-c827-4648-a0af-3e3e60b036af', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(82, 1, 1, 'discount82', 2, 'a94b526c-f398-435a-b537-1262c961caff', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(83, 1, 1, 'discount83', 2, 'a597569c-0e45-43ba-a4ed-69c32c1a4d4e', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(84, 1, 1, 'discount84', 2, 'fe874883-4e72-4310-80b6-f31b6d5ff54d', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(85, 1, 1, 'discount85', 2, '0b6c216b-d148-43f6-a3f7-3538f76c319b', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(86, 1, 1, 'discount86', 2, 'ef7f2e76-cfc7-4a40-bb3c-066e0b39b7f3', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(87, 1, 1, 'discount87', 2, 'fc06b9ba-bdbd-4062-a013-ed14c441238c', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(88, 1, 1, 'discount88', 2, '3a003a49-e08a-46d0-a302-f69aada840d6', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(89, 1, 1, 'discount89', 2, '71fbb33f-ba85-4f6a-8677-6729dfd4b869', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(90, 1, 1, 'discount90', 2, 'abf7f045-9ef5-42fd-9d17-41f82316dee0', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(91, 1, 1, 'discount91', 2, '6fb461d3-7f27-4595-8b77-acbd86b05c93', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(92, 1, 1, 'discount92', 2, '52288ce0-d2ee-48db-b87b-74ca2df2c74a', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(93, 1, 1, 'discount93', 2, 'ad62d2ac-8a02-4fc8-a2ad-77375f95669d', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(94, 1, 1, 'discount94', 2, '03c9d315-0530-4f2c-bc59-aadce71ff20c', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(95, 1, 1, 'discount95', 2, 'd5b4e58a-695b-42cd-b806-e3d61e13eb1a', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(96, 1, 1, 'discount96', 2, 'a7928442-fb1f-4b65-82e9-0c02c1aac0f4', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(97, 1, 1, 'discount97', 2, '4a1755cd-7411-4af8-9558-8593dc05bbbb', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(98, 1, 1, 'discount98', 2, '6ab612ed-e588-4ecf-93fb-0d6085f99854', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(99, 1, 1, 'discount99', 2, 'bde9b72a-b1d4-44fb-b330-c2192098b777', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(100, 1, 1, 'discount100', 2, '6a622563-b209-424e-93e1-fbc2caed7f53', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(101, 1, 1, 'discount101', 2, '2408e85e-f82d-403a-9668-71b918a16c06', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(102, 1, 1, 'discount102', 2, '85544a15-ea0f-4b08-9946-bad8b333b717', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(103, 1, 1, 'discount103', 2, 'c9502594-f68c-4d8d-b405-03e1d8b9c516', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(104, 1, 1, 'discount104', 2, 'd5eb4dfb-73b3-4c88-a77f-2d04ba5f4843', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(105, 1, 1, 'discount105', 2, '600036bc-24d7-4f47-9a55-0bd8e198f8e3', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(106, 1, 1, 'discount106', 2, 'e7b2935d-372d-4038-9cd1-e893fe31a96c', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(107, 1, 1, 'discount107', 2, '51990514-8d72-47c0-a450-aed3de820c2f', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(108, 1, 1, 'discount108', 2, '6353c1fe-e5d3-4483-87f3-4907f062b9ea', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(109, 1, 1, 'discount109', 2, '21ff988b-9d82-4368-9e7d-8f5c6a569678', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(110, 1, 1, 'discount110', 2, '8e3643df-ab16-4c41-9f8f-ae2be560570a', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(111, 1, 1, 'discount111', 2, 'c0a168ef-904b-4569-8185-efbb02eb13f8', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(112, 1, 1, 'discount112', 2, '429caf13-f9ac-4d18-866f-758ea1aefc07', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(113, 1, 1, 'discount113', 2, 'c8cf3fb8-3ddf-4910-ac70-03a0fe3314c5', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(114, 1, 1, 'discount114', 2, '48b59b5a-3a2d-4fb6-b592-e8a60bcbece8', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(115, 1, 1, 'discount115', 2, 'd556a80d-3aa6-45dc-a699-4dcf5c61585d', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(116, 1, 1, 'discount116', 2, '71395a04-8fe5-4c71-8cd9-c5bcd87a36c3', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(117, 1, 1, 'discount117', 2, '0be77b8f-1f3b-4079-b955-9530b5782ea0', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(118, 1, 1, 'discount118', 2, 'f8556426-121d-4dfa-9956-691106f52d4c', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(119, 1, 1, 'discount119', 2, 'e53c8279-4188-4c90-838e-beb80a736c88', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(120, 1, 1, 'discount120', 2, '2422a5c0-84de-4834-baeb-3d233546ecb1', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(121, 1, 1, 'discount121', 2, '5b8beec3-4da2-4091-aeef-c1eb3e26be37', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(122, 1, 1, 'discount122', 2, '0ae81ddb-f39f-4a97-9d69-3083760566a5', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(123, 1, 1, 'discount123', 2, '80ee110a-4327-42f1-bab6-8b36d5a03996', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(124, 1, 1, 'discount124', 2, '0b4274f2-f102-4dd6-af41-3b52ff603587', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(125, 1, 1, 'discount125', 2, '2c265516-181c-4f1d-a1c2-48c8c7886fb3', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(126, 1, 1, 'discount126', 2, 'cd1fe5c9-4007-4655-8996-3af4f924d0c8', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(127, 1, 1, 'discount127', 2, '806b0827-70fc-4d84-9728-0ef64f9f4166', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(128, 1, 1, 'discount128', 2, 'd8353c07-5884-4fba-aa98-ca1c1fd5233f', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(129, 1, 1, 'discount129', 2, '5b82545b-1f4a-46be-bcfe-6f34bd467344', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(130, 1, 1, 'discount130', 2, '21477b20-4d78-4c98-b0db-e6b517d5c7ee', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(131, 1, 1, 'discount131', 2, '459ecb90-c150-4a42-8a43-2048a2e05fcf', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(132, 1, 1, 'discount132', 2, '5bebc60b-b730-40b6-808d-4dfecaf7eb32', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(133, 1, 1, 'discount133', 2, '15fbc4dc-22eb-4953-a697-6ef37c8acdcd', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(134, 1, 1, 'discount134', 2, '17333f74-aab8-4be0-877f-f8c87fff91b9', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(135, 1, 1, 'discount135', 2, '8bf3a037-700f-4f7a-b6ff-4cea3c7f619a', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(136, 1, 1, 'discount136', 2, 'de8ab9e2-ff5f-4980-af41-b502128ed937', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(137, 1, 1, 'discount137', 2, '4e3295bc-e910-4da9-ad23-fff7737c1d42', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(138, 1, 1, 'discount138', 2, 'be89f514-6085-4825-b2b7-0e5ebdcc9cbb', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(139, 1, 1, 'discount139', 2, '264df57e-2ea3-48f7-a04e-c265a3de2d18', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(140, 1, 1, 'discount140', 2, 'bcb43109-4634-433b-8dd5-3fcc81d73fda', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(141, 1, 1, 'discount141', 2, '835ee1dc-cba0-4043-805e-8e8f035eab8c', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(142, 1, 1, 'discount142', 2, '3324a449-b3fb-4127-886d-a22b8365c6c2', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(143, 1, 1, 'discount143', 2, '347513a8-4965-4715-afb7-55b614044cc8', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(144, 1, 1, 'discount144', 2, '34a8391f-38cb-47a0-a869-bd6da247111b', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(145, 1, 1, 'discount145', 2, 'aee01e07-f257-4a35-9744-79ed58114686', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(146, 1, 1, 'discount146', 2, '0834d7a6-4c1b-4de9-9192-36a80b5ff556', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(147, 1, 1, 'discount147', 2, '20bbb7cf-0e31-469b-a309-c3ded5e0bf24', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(148, 1, 1, 'discount148', 2, '2691dbc0-a4f2-41da-9dfe-66a14af27982', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(149, 1, 1, 'discount149', 2, '25912dda-5b57-481f-80ea-b1f3fe486156', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(150, 1, 1, 'discount150', 2, '4ddf1ebb-a265-43a3-a43a-4df1cf2056e4', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(151, 1, 1, 'discount151', 2, '3c94055f-a635-4241-a120-de0cf6a24df9', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(152, 1, 1, 'discount152', 2, 'bd93fe39-1ff1-426a-81e2-f2301adb9042', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(153, 1, 1, 'discount153', 2, '1a4cde49-9f86-4ff1-94f9-75c84eb554b7', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(154, 1, 1, 'discount154', 2, '04683e02-6d2a-41fc-966c-5306b4d66220', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(155, 1, 1, 'discount155', 2, 'e2def7d1-c404-417b-9c37-ae38376e4e4b', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(156, 1, 1, 'discount156', 2, '1cb7dade-fcf6-4955-85a6-da24cd0f9b44', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(157, 1, 1, 'discount157', 2, 'ba097896-e0b9-45c1-beda-8ed82818f77e', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(158, 1, 1, 'discount158', 2, '9895b987-f779-4e68-8387-9cb70d67fa6e', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(159, 1, 1, 'discount159', 2, 'f74a33f5-75d9-4c65-98a9-2a45154b084d', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(160, 1, 1, 'discount160', 2, '25fd3926-830e-4a70-9bf4-585627351cc4', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(161, 1, 1, 'discount161', 2, 'affd9345-13fd-4cc0-93cb-aac5f593bf10', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(162, 1, 1, 'discount162', 2, '03001704-e42d-4186-ad77-bba8c1a89904', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(163, 1, 1, 'discount163', 2, '149efe86-57ed-4f21-9d7c-b2fcb1756859', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(164, 1, 1, 'discount164', 2, '931ebbe4-ed62-45d3-b15e-2293163a8e70', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(165, 1, 1, 'discount165', 2, '3a6c25c2-cc35-4088-8e2a-7b3d111b9d84', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(166, 1, 1, 'discount166', 2, 'a281ffc4-659f-40f6-bb11-6885123915aa', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(167, 1, 1, 'discount167', 2, 'd6beae6c-275b-4ffa-b87e-1dc5b0950c7e', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(168, 1, 1, 'discount168', 2, 'a5c93df8-89e9-4fed-9e8e-4b11b9bdc61c', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(169, 1, 1, 'discount169', 2, '3a42e8df-e6a4-4869-aed9-ef48974cc320', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(170, 1, 1, 'discount170', 2, 'fb07baf4-f5be-4992-a4a7-705740cb1fda', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(171, 1, 1, 'discount171', 2, '7b43850e-d2e1-4d7c-9bf4-ec09645eab1d', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(172, 1, 1, 'discount172', 2, 'fa77ea0c-6ae2-4cea-a1f1-c4f10274d461', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(173, 1, 1, 'discount173', 2, 'e0ed50b7-b21b-4512-8bb3-0146250db69f', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(174, 1, 1, 'discount174', 2, '5f4efe7b-a5e4-417a-a8b4-071c83e06bf0', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(175, 1, 1, 'discount175', 2, '47866d1a-1cec-41aa-a3a9-46b5b40635f4', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(176, 1, 1, 'discount176', 2, '07b4b39a-8dd3-4586-8588-296aebac86b2', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(177, 1, 1, 'discount177', 2, 'aadc158a-6d78-4ebe-bff8-35d4528db1ef', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(178, 1, 1, 'discount178', 2, '069af663-0a0c-44f9-aa00-c43b4321f627', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(179, 1, 1, 'discount179', 2, 'bcb9635e-fa6a-4d7a-8015-f76942974ea4', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(180, 1, 1, 'discount180', 2, 'd637f659-a368-4c8a-8737-64586086257c', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(181, 1, 1, 'discount181', 2, '2bb5f082-44da-45c9-bcd1-8dd287fdab42', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(182, 1, 1, 'discount182', 2, '4add419a-0c72-424f-b885-0e4980265a54', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(183, 1, 1, 'discount183', 2, 'ad3fb2e0-6f03-4c85-a7b6-348568b88916', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(184, 1, 1, 'discount184', 2, 'e3756ad0-b488-4a5e-9d56-bce126b3b5de', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(185, 1, 1, 'discount185', 2, 'b84a81a3-80b0-4e8e-ae59-a0774d6736a0', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(186, 1, 1, 'discount186', 2, '5eef8622-6b01-47cf-8e13-c29b5369758e', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(187, 1, 1, 'discount187', 2, '6de108a9-9bdd-4c09-a762-e53d1e779264', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(188, 1, 1, 'discount188', 2, '40fb06e6-2e9c-49a9-bd66-3f6cdb692226', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(189, 1, 1, 'discount189', 2, '5131405d-732d-4e96-8344-2156cc2db801', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(190, 1, 1, 'discount190', 2, '4783ee50-fca6-4253-a9ef-fd5e52873c83', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(191, 1, 1, 'discount191', 2, 'ac4cd6da-a876-48c5-93c6-d8d26cc6615c', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(192, 1, 1, 'discount192', 2, '1ed644eb-087c-43d1-960d-09055f5c5f5f', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(193, 1, 1, 'discount193', 2, 'dd167bda-fe8a-4207-bcba-da3977005f94', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(194, 1, 1, 'discount194', 2, 'cdc547e5-c3d9-451f-bc8d-4eb8643dccfc', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(195, 1, 1, 'discount195', 2, '45b1751a-ae54-48ac-a4fb-69927cde556a', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(196, 1, 1, 'discount196', 2, '84b08f32-23ef-4e94-9d18-998b6fa36880', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(197, 1, 1, 'discount197', 2, '2a67a7fc-f10f-43d4-b01a-cf08ea6c9e12', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(198, 1, 1, 'discount198', 2, '208cded6-236c-4271-9478-c423022ac840', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(199, 1, 1, 'discount199', 2, '0e53d069-265f-4938-840f-951d9b282aee', 20);
INSERT INTO kb_pricing.price_stepup_stepdown
(id, is_active, lower_limit, name, value_in_percent, product_combination_unique_id, upper_limit)
VALUES(200, 1, 1, 'discount200', 2, 'fc0ef567-e1b0-4d34-aefa-727dc5d8bc2e', 20);
	 
	 
	 
	 SET FOREIGN_KEY_CHECKS = 1;