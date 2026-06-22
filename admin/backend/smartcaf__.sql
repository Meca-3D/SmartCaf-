-- phpMyAdmin SQL Dump
-- version 5.2.1deb3
-- https://www.phpmyadmin.net/
--
-- Hôte : localhost:3306
-- Généré le : lun. 22 juin 2026 à 08:50
-- Version du serveur : 8.0.46-0ubuntu0.24.04.2
-- Version de PHP : 8.3.6

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

--
-- Déchargement des données de la table `categories`
--

INSERT INTO `categories` (`id`, `name`, `description`) VALUES
(1, 'Boissons chaudes', 'Cafés, thés, chocolats chauds'),
(2, 'Boissons froides', 'Jus, sodas, eaux, smoothies'),
(3, 'Sandwichs', 'Sandwichs frais préparés chaque matin'),
(4, 'Snacks', 'Viennoiseries, biscuits et en-cas'),
(5, 'Desserts', 'Pâtisseries et gourmandises sucrées'),
(6, 'Boissons tièdes', 'toute les boissons tièdes'),
(7, 'test', '');

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
  `total_price` decimal(38,2) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `table_number` int DEFAULT NULL
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
  `price` decimal(38,2) DEFAULT NULL
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
  `updated_at` datetime(6) NOT NULL,
  `article_code` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `products`
--

INSERT INTO `products` (`id`, `category_id`, `name`, `description`, `price`, `image_url`, `is_active`, `category`, `created_at`, `stock`, `updated_at`, `article_code`) VALUES
(1, NULL, 'Café Expresso', 'Expresso serré 100% arabica', 1.50, 'https://images.unsplash.com/photo-1510707577719-ae7c14805e3a?w=400', 1, 'Boissons chaudes', '2026-06-05 09:18:07.000000', 80, '2026-06-05 09:18:07.000000', NULL),
(2, NULL, 'Café Latte', 'Expresso allongé avec lait mousseux', 2.50, 'https://images.unsplash.com/photo-1561047029-3000c68339ca?w=400', 1, 'Boissons chaudes', '2026-06-05 09:18:07.000000', 60, '2026-06-05 09:18:07.000000', NULL),
(4, NULL, 'Chocolat chaud', 'Chocolat noir fondu au lait entier', 3.00, 'https://images.unsplash.com/photo-1542990253-0d0f5be5f0ed?w=400', 1, 'Boissons chaudes', '2026-06-05 09:18:07.000000', 40, '2026-06-05 09:18:07.000000', NULL),
(5, NULL, 'Thé vert', 'Thé vert bio en feuilles', 2.00, 'https://images.unsplash.com/photo-1556679343-c7306c1976bc?w=400', 1, 'Boissons chaudes', '2026-06-05 09:18:07.000000', 50, '2026-06-05 09:18:07.000000', NULL),
(6, NULL, 'Jus d\'orange frais', 'Orange pressée minute', 3.50, 'https://images.unsplash.com/photo-1600271886742-f049cd451bba?w=400', 1, 'Boissons froides', '2026-06-05 09:18:07.000000', 30, '2026-06-05 09:18:07.000000', NULL),
(7, NULL, 'Eau minérale', 'Eau plate 50cl', 1.00, 'https://images.unsplash.com/photo-1553361371-9b22f78e8b1d?w=400', 1, 'Boissons froides', '2026-06-05 09:18:07.000000', 120, '2026-06-05 09:18:07.000000', NULL),
(8, NULL, 'Coca-Cola', 'Coca-Cola 33cl bien frais', 2.50, 'https://images.unsplash.com/photo-1629203851122-3726ecdf080e?w=400', 1, 'Boissons froides', '2026-06-05 09:18:07.000000', 70, '2026-06-05 09:18:07.000000', NULL),
(9, NULL, 'Sandwich Jambon-Beurre', 'Baguette tradition, jambon, beurre demi-sel', 4.50, 'https://images.unsplash.com/photo-1509722747041-616f39b57569?w=400', 1, 'Sandwichs', '2026-06-05 09:18:07.000000', 25, '2026-06-05 09:18:07.000000', NULL),
(10, NULL, 'Sandwich Poulet-Pesto', 'Ciabatta, poulet grillé, pesto basilic', 5.50, 'https://images.unsplash.com/photo-1553909489-cd47e0907980?w=400', 1, 'Sandwichs', '2026-06-05 09:18:07.000000', 20, '2026-06-05 09:18:07.000000', NULL),
(11, NULL, 'Croissant au beurre', 'Croissant feuilleté pur beurre', 1.20, 'https://images.unsplash.com/photo-1555507036-ab1f4038808a?w=400', 1, 'Snacks', '2026-06-05 09:18:07.000000', 45, '2026-06-05 09:18:07.000000', NULL),
(12, NULL, 'Pain au chocolat', 'Deux barres de chocolat noir dans la pâte', 1.50, 'https://images.unsplash.com/photo-1623334044303-241021148842?w=400', 1, 'Snacks', '2026-06-05 09:18:07.000000', 45, '2026-06-05 09:18:07.000000', NULL),
(13, NULL, 'Brownie chocolat', 'Brownie moelleux aux noix de pécan', 2.80, 'https://images.unsplash.com/photo-1564355808539-22fda35bed7e?w=400', 1, 'Desserts', '2026-06-05 09:18:07.000000', 30, '2026-06-05 09:18:07.000000', NULL),
(16, NULL, 'jus de banane', 'jus de banane 2', 13.00, 'https://assets.afcdn.com/story/20260615/2327611_w600h380c1.webp', 1, 'test', '2026-06-22 08:07:16.822989', 14, '2026-06-22 08:07:16.823015', NULL);

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
-- Déchargement des données de la table `users`
--

INSERT INTO `users` (`id`, `firstname`, `lastname`, `email`, `password`, `language`, `loyalty_points`, `created_at`, `first_name`, `last_name`, `role`) VALUES
(1, NULL, NULL, 'admin@smartcafe.fr', '$2a$10$HPzV6/f1uHfWilyth7e6MeZnIte.1rp9vi1sG5eDpcfd1tFE57eDS', NULL, 0, '2026-06-05 09:18:07', 'Admin', 'SmartCafe', 'ADMIN'),
(5, NULL, NULL, 'employer@smartcafe.fr', '$2a$10$To4pUeEq9b0eSAujuR7gIOmby21ktthQyZQUUW3uKR1K359IW/OJm', NULL, 0, '2026-06-22 09:57:28', 'Employé', 'SmartCafé', 'EMPLOYER'),
(6, NULL, NULL, 'leo@gmail.com', '$2a$10$gTr1RVJxS5maJIFEyAmQk.1ooKvpPhDo5PBan.N0hNPniIOcEaS6O', NULL, 0, '2026-06-22 08:00:04', 'leo', 'ben', 'CLIENT');

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
  ADD UNIQUE KEY `UK_86x27x6pd8vflgtm9x8owxq7m` (`article_code`),
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
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

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
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=43;

--
-- AUTO_INCREMENT pour la table `order_items`
--
ALTER TABLE `order_items`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=74;

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
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT pour la table `product_options`
--
ALTER TABLE `product_options`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `tables`
--
ALTER TABLE `tables`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT pour la table `users`
--
ALTER TABLE `users`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

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
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
