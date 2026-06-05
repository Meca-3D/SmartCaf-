-- phpMyAdmin SQL Dump
-- version 5.2.1deb3
-- https://www.phpmyadmin.net/
--
-- Hôte : localhost:3306
-- Généré le : mer. 18 fév. 2026 à 13:43
-- Version du serveur : 8.0.45-0ubuntu0.24.04.1
-- Version de PHP : 8.2.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `smartcafé`
--

-- --------------------------------------------------------

--
-- Structure de la table `categories`
--

CREATE TABLE `categories` (
  `id` bigint NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `favorites`
--

CREATE TABLE `favorites` (
  `id` bigint NOT NULL,
  `user_id` bigint DEFAULT NULL,
  `product_id` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `notifications`
--

CREATE TABLE `notifications` (
  `id` bigint NOT NULL,
  `user_id` bigint DEFAULT NULL,
  `order_id` bigint DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  `is_read` tinyint(1) DEFAULT '0',
  `created_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `orders`
--

CREATE TABLE `orders` (
  `id` bigint NOT NULL,
  `user_id` bigint DEFAULT NULL,
  `table_id` bigint DEFAULT NULL,
  `customer_name` varchar(255) DEFAULT NULL,
  `order_type` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `total_price` decimal(10,0) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `order_items`
--

CREATE TABLE `order_items` (
  `id` bigint NOT NULL,
  `order_id` bigint DEFAULT NULL,
  `product_id` bigint DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `price` decimal(10,0) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `order_item_options`
--

CREATE TABLE `order_item_options` (
  `id` bigint NOT NULL,
  `order_item_id` bigint DEFAULT NULL,
  `product_option_id` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `payments`
--

CREATE TABLE `payments` (
  `id` bigint NOT NULL,
  `order_id` bigint DEFAULT NULL,
  `payment_method` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `amount` decimal(10,0) DEFAULT NULL,
  `paid_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `products`
--

CREATE TABLE `products` (
  `id` bigint NOT NULL,
  `category_id` bigint DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `description` text,
  `price` decimal(38,2) NOT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `category` varchar(255) NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `stock` int NOT NULL,
  `updated_at` datetime(6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `product_options`
--

CREATE TABLE `product_options` (
  `id` bigint NOT NULL,
  `product_id` bigint DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `extra_price` decimal(10,0) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `tables`
--

CREATE TABLE `tables` (
  `id` bigint NOT NULL,
  `table_number` int DEFAULT NULL,
  `qr_code` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `users`
--

CREATE TABLE `users` (
  `id` bigint NOT NULL,
  `firstname` varchar(255) DEFAULT NULL,
  `lastname` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `language` varchar(255) DEFAULT NULL,
  `loyalty_points` int DEFAULT '0',
  `created_at` datetime DEFAULT NULL,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `role` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `categories`
--
ALTER TABLE `categories`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `favorites`
--
ALTER TABLE `favorites`
  ADD PRIMARY KEY (`id`),
  ADD KEY `favorites_ibfk_1` (`user_id`),
  ADD KEY `favorites_ibfk_2` (`product_id`);

--
-- Index pour la table `notifications`
--
ALTER TABLE `notifications`
  ADD PRIMARY KEY (`id`),
  ADD KEY `notifications_ibfk_1` (`user_id`),
  ADD KEY `notifications_ibfk_2` (`order_id`);

--
-- Index pour la table `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`id`),
  ADD KEY `orders_ibfk_1` (`user_id`),
  ADD KEY `orders_ibfk_2` (`table_id`);

--
-- Index pour la table `order_items`
--
ALTER TABLE `order_items`
  ADD PRIMARY KEY (`id`),
  ADD KEY `order_items_ibfk_1` (`order_id`),
  ADD KEY `order_items_ibfk_2` (`product_id`);

--
-- Index pour la table `order_item_options`
--
ALTER TABLE `order_item_options`
  ADD PRIMARY KEY (`id`),
  ADD KEY `order_item_options_ibfk_1` (`order_item_id`),
  ADD KEY `order_item_options_ibfk_2` (`product_option_id`);

--
-- Index pour la table `payments`
--
ALTER TABLE `payments`
  ADD PRIMARY KEY (`id`),
  ADD KEY `payments_ibfk_1` (`order_id`);

--
-- Index pour la table `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`id`),
  ADD KEY `products_ibfk_1` (`category_id`);

--
-- Index pour la table `product_options`
--
ALTER TABLE `product_options`
  ADD PRIMARY KEY (`id`),
  ADD KEY `product_options_ibfk_1` (`product_id`);

--
-- Index pour la table `tables`
--
ALTER TABLE `tables`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `categories`
--
ALTER TABLE `categories`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `favorites`
--
ALTER TABLE `favorites`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `notifications`
--
ALTER TABLE `notifications`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `orders`
--
ALTER TABLE `orders`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `order_items`
--
ALTER TABLE `order_items`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `order_item_options`
--
ALTER TABLE `order_item_options`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `payments`
--
ALTER TABLE `payments`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `products`
--
ALTER TABLE `products`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `product_options`
--
ALTER TABLE `product_options`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `tables`
--
ALTER TABLE `tables`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `users`
--
ALTER TABLE `users`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `favorites`
--
ALTER TABLE `favorites`
  ADD CONSTRAINT `favorites_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `favorites_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`);

--
-- Contraintes pour la table `notifications`
--
ALTER TABLE `notifications`
  ADD CONSTRAINT `notifications_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `notifications_ibfk_2` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`);

--
-- Contraintes pour la table `orders`
--
ALTER TABLE `orders`
  ADD CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `orders_ibfk_2` FOREIGN KEY (`table_id`) REFERENCES `tables` (`id`);

--
-- Contraintes pour la table `order_items`
--
ALTER TABLE `order_items`
  ADD CONSTRAINT `order_items_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  ADD CONSTRAINT `order_items_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`);

--
-- Contraintes pour la table `order_item_options`
--
ALTER TABLE `order_item_options`
  ADD CONSTRAINT `order_item_options_ibfk_1` FOREIGN KEY (`order_item_id`) REFERENCES `order_items` (`id`),
  ADD CONSTRAINT `order_item_options_ibfk_2` FOREIGN KEY (`product_option_id`) REFERENCES `product_options` (`id`);

--
-- Contraintes pour la table `payments`
--
ALTER TABLE `payments`
  ADD CONSTRAINT `payments_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`);

--
-- Contraintes pour la table `products`
--
ALTER TABLE `products`
  ADD CONSTRAINT `products_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`);

--
-- Contraintes pour la table `product_options`
--
ALTER TABLE `product_options`
  ADD CONSTRAINT `product_options_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`);

--
-- Données pour la table `users`
--

INSERT INTO `users` (`first_name`, `last_name`, `email`, `password`, `role`, `created_at`)
VALUES ('Admin', 'SmartCafe', 'admin@smartcafe.fr', '$2a$10$HPzV6/f1uHfWilyth7e6MeZnIte.1rp9vi1sG5eDpcfd1tFE57eDS', 'ADMIN', NOW());

-- ===== CATÉGORIES =====
INSERT INTO `categories` (`id`, `name`, `description`) VALUES
(1, 'Boissons chaudes',  'Cafés, thés, chocolats chauds'),
(2, 'Boissons froides',  'Jus, sodas, eaux, smoothies'),
(3, 'Sandwichs',         'Sandwichs frais préparés chaque matin'),
(4, 'Snacks',            'Viennoiseries, biscuits et en-cas'),
(5, 'Desserts',          'Pâtisseries et gourmandises sucrées');

-- ===== PRODUITS =====
INSERT INTO `products` (`id`, `name`, `description`, `price`, `stock`, `category`, `image_url`, `is_active`, `created_at`, `updated_at`) VALUES
(1,  'Café Expresso',         'Expresso serré 100% arabica',                   1.50,  80, 'Boissons chaudes', 'https://images.unsplash.com/photo-1510707577719-ae7c14805e3a?w=400', 1, NOW(), NOW()),
(2,  'Café Latte',            'Expresso allongé avec lait mousseux',           2.50,  60, 'Boissons chaudes', 'https://images.unsplash.com/photo-1561047029-3000c68339ca?w=400', 1, NOW(), NOW()),
(3,  'Cappuccino',            'Expresso, lait vapeur et mousse onctueuse',     2.80,  60, 'Boissons chaudes', 'https://images.unsplash.com/photo-1574914629385-46448b209e27?w=400', 1, NOW(), NOW()),
(4,  'Chocolat chaud',        'Chocolat noir fondu au lait entier',            3.00,  40, 'Boissons chaudes', 'https://images.unsplash.com/photo-1542990253-0d0f5be5f0ed?w=400', 1, NOW(), NOW()),
(5,  'Thé vert',              'Thé vert bio en feuilles',                      2.00,  50, 'Boissons chaudes', 'https://images.unsplash.com/photo-1556679343-c7306c1976bc?w=400', 1, NOW(), NOW()),
(6,  'Jus d''orange frais',  'Orange pressée minute',                         3.50,  30, 'Boissons froides', 'https://images.unsplash.com/photo-1600271886742-f049cd451bba?w=400', 1, NOW(), NOW()),
(7,  'Eau minérale',          'Eau plate 50cl',                                1.00, 120, 'Boissons froides', 'https://images.unsplash.com/photo-1553361371-9b22f78e8b1d?w=400', 1, NOW(), NOW()),
(8,  'Coca-Cola',             'Coca-Cola 33cl bien frais',                     2.50,  70, 'Boissons froides', 'https://images.unsplash.com/photo-1629203851122-3726ecdf080e?w=400', 1, NOW(), NOW()),
(9,  'Sandwich Jambon-Beurre','Baguette tradition, jambon, beurre demi-sel',   4.50,  25, 'Sandwichs',        'https://images.unsplash.com/photo-1509722747041-616f39b57569?w=400', 1, NOW(), NOW()),
(10, 'Sandwich Poulet-Pesto', 'Ciabatta, poulet grillé, pesto basilic',        5.50,  20, 'Sandwichs',        'https://images.unsplash.com/photo-1553909489-cd47e0907980?w=400', 1, NOW(), NOW()),
(11, 'Croissant au beurre',   'Croissant feuilleté pur beurre',                1.20,  45, 'Snacks',           'https://images.unsplash.com/photo-1555507036-ab1f4038808a?w=400', 1, NOW(), NOW()),
(12, 'Pain au chocolat',      'Deux barres de chocolat noir dans la pâte',     1.50,  45, 'Snacks',           'https://images.unsplash.com/photo-1623334044303-241021148842?w=400', 1, NOW(), NOW()),
(13, 'Brownie chocolat',      'Brownie moelleux aux noix de pécan',            2.80,  30, 'Desserts',         'https://images.unsplash.com/photo-1564355808539-22fda35bed7e?w=400', 1, NOW(), NOW()),
(14, 'Muffin myrtille',       'Muffin maison aux myrtilles fraîches',          2.50,  25, 'Desserts',         'https://images.unsplash.com/photo-1558303274-2c6a5c91c8e2?w=400', 1, NOW(), NOW());

-- ===== COMMANDES (20) =====
INSERT INTO `orders` (`id`, `user_id`, `customer_name`, `order_type`, `status`, `total_price`, `created_at`) VALUES
(1,  1, 'Alice Martin',     'ON_SITE',           'COMPLETED', 5.30,  DATE_SUB(NOW(), INTERVAL 30 DAY)),
(2,  1, 'Bob Dupont',       'CLICK_AND_COLLECT', 'COMPLETED', 9.00,  DATE_SUB(NOW(), INTERVAL 28 DAY)),
(3,  NULL,'Chloé Bernard',  'ON_SITE',           'COMPLETED', 4.30,  DATE_SUB(NOW(), INTERVAL 27 DAY)),
(4,  NULL,'David Leclerc',  'CLICK_AND_COLLECT', 'COMPLETED', 11.00, DATE_SUB(NOW(), INTERVAL 25 DAY)),
(5,  1, 'Emma Petit',       'ON_SITE',           'COMPLETED', 6.30,  DATE_SUB(NOW(), INTERVAL 22 DAY)),
(6,  NULL,'Félix Rousseau', 'ON_SITE',           'CANCELLED', 2.50,  DATE_SUB(NOW(), INTERVAL 20 DAY)),
(7,  1, 'Grace Moreau',     'CLICK_AND_COLLECT', 'COMPLETED', 13.50, DATE_SUB(NOW(), INTERVAL 18 DAY)),
(8,  NULL,'Hugo Simon',     'ON_SITE',           'COMPLETED', 7.80,  DATE_SUB(NOW(), INTERVAL 16 DAY)),
(9,  1, 'Iris Laurent',     'ON_SITE',           'COMPLETED', 5.00,  DATE_SUB(NOW(), INTERVAL 14 DAY)),
(10, NULL,'Jules Fontaine',  'CLICK_AND_COLLECT', 'COMPLETED', 10.50, DATE_SUB(NOW(), INTERVAL 12 DAY)),
(11, 1, 'Karine Michel',    'ON_SITE',           'COMPLETED', 8.30,  DATE_SUB(NOW(), INTERVAL 10 DAY)),
(12, NULL,'Luc Garnier',    'CLICK_AND_COLLECT', 'CANCELLED', 4.50,  DATE_SUB(NOW(), INTERVAL 9 DAY)),
(13, 1, 'Marie Blanc',      'ON_SITE',           'COMPLETED', 15.60, DATE_SUB(NOW(), INTERVAL 8 DAY)),
(14, NULL,'Nicolas Faure',  'ON_SITE',           'COMPLETED', 6.00,  DATE_SUB(NOW(), INTERVAL 7 DAY)),
(15, 1, 'Océane Denis',     'CLICK_AND_COLLECT', 'COMPLETED', 12.80, DATE_SUB(NOW(), INTERVAL 6 DAY)),
(16, NULL,'Paul Girard',    'ON_SITE',           'PENDING',   3.00,  DATE_SUB(NOW(), INTERVAL 3 DAY)),
(17, 1, 'Quentin Leroy',    'ON_SITE',           'COMPLETED', 9.30,  DATE_SUB(NOW(), INTERVAL 2 DAY)),
(18, NULL,'Rose Morin',     'CLICK_AND_COLLECT', 'PENDING',   7.50,  DATE_SUB(NOW(), INTERVAL 1 DAY)),
(19, 1, 'Samuel Perrin',    'ON_SITE',           'COMPLETED', 11.30, DATE_SUB(NOW(), INTERVAL 12 HOUR)),
(20, NULL,'Tina Marchand',  'CLICK_AND_COLLECT', 'PENDING',   5.50,  DATE_SUB(NOW(), INTERVAL 2 HOUR));

-- ===== DÉTAILS COMMANDES =====
INSERT INTO `order_items` (`id`, `order_id`, `product_id`, `quantity`, `price`) VALUES
-- Commande 1 : Cappuccino + Croissant
(1,  1,  3,  1, 2.80), (2,  1,  11, 2, 1.20),
-- Commande 2 : Sandwich poulet + Jus orange + Café latte
(3,  2,  10, 1, 5.50), (4,  2,  6,  1, 3.50),
-- Commande 3 : Café Expresso + Pain au chocolat + Eau
(5,  3,  1,  1, 1.50), (6,  3,  12, 1, 1.50), (7,  3,  7,  1, 1.00),
-- Commande 4 : 2x Sandwich jambon + 2x Coca
(8,  4,  9,  2, 4.50), (9,  4,  8,  1, 2.50),
-- Commande 5 : Chocolat chaud + Brownie + Eau
(10, 5,  4,  1, 3.00), (11, 5,  13, 1, 2.80), (12, 5,  7,  1, 1.00),
-- Commande 6 (annulée) : Coca
(13, 6,  8,  1, 2.50),
-- Commande 7 : Sandwich poulet + Jus + Cappuccino + Brownie
(14, 7,  10, 1, 5.50), (15, 7,  6,  1, 3.50), (16, 7,  3,  1, 2.80), (17, 7,  13, 1, 2.80),
-- Commande 8 : Café latte + Sandwich jambon + Eau
(18, 8,  2,  1, 2.50), (19, 8,  9,  1, 4.50), (20, 8,  7,  1, 1.00),
-- Commande 9 : 2x Thé vert + Croissant
(21, 9,  5,  2, 2.00), (22, 9,  11, 1, 1.20),
-- Commande 10 : Sandwich jambon + Sandwich poulet + Jus
(23, 10, 9,  1, 4.50), (24, 10, 10, 1, 5.50), (25, 10, 7,  1, 1.00),
-- Commande 11 : Cappuccino + Muffin + Sandwich jambon
(26, 11, 3,  1, 2.80), (27, 11, 14, 1, 2.50), (28, 11, 9,  1, 4.50),
-- Commande 12 (annulée) : Sandwich jambon
(29, 12, 9,  1, 4.50),
-- Commande 13 : 2x Sandwich poulet + 2x Café latte + 2x Brownie
(30, 13, 10, 2, 5.50), (31, 13, 2,  2, 2.50), (32, 13, 13, 1, 2.80),
-- Commande 14 : Chocolat chaud + Eau + Croissant
(33, 14, 4,  1, 3.00), (34, 14, 7,  1, 1.00), (35, 14, 11, 1, 1.20),
-- Commande 15 : Sandwich poulet + 2x Cappuccino + 2x Pain choco
(36, 15, 10, 1, 5.50), (37, 15, 3,  2, 2.80), (38, 15, 12, 1, 1.50),
-- Commande 16 (pending) : Chocolat chaud
(39, 16, 4,  1, 3.00),
-- Commande 17 : Café expresso + Sandwich jambon + Brownie + Coca
(40, 17, 1,  1, 1.50), (41, 17, 9,  1, 4.50), (42, 17, 13, 1, 2.80), (43, 17, 7,  1, 1.00),
-- Commande 18 (pending) : 2x Thé vert + Muffin
(44, 18, 5,  2, 2.00), (45, 18, 14, 1, 2.50),
-- Commande 19 : Café latte + Sandwich poulet + Pain choco + Coca
(46, 19, 2,  1, 2.50), (47, 19, 10, 1, 5.50), (48, 19, 12, 1, 1.50), (49, 19, 8,  1, 2.50),
-- Commande 20 (pending) : Cappuccino + Croissant
(50, 20, 3,  1, 2.80), (51, 20, 11, 1, 1.20), (52, 20, 7,  1, 1.00);

COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
