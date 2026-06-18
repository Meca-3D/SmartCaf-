# Documentation Fonctionnelle et Technique — SmartCafé

**Projet UF DEV B3 — Ynov Informatique**
**Formation :** Bachelor 3 Informatique
**Groupe :** 2–3 étudiants
**Dépôt GitHub :** https://github.com/Meca-3D/SmartCaf-.git

---

## Sommaire

1. [Présentation du projet](#1-présentation-du-projet)
2. [Architecture globale](#2-architecture-globale)
3. [Documentation fonctionnelle](#3-documentation-fonctionnelle)
   - 3.1 Parcours utilisateur — Client (Web)
   - 3.2 Parcours utilisateur — Administrateur (Web)
   - 3.3 Parcours utilisateur — Client (Mobile)
4. [Documentation technique — Backend](#4-documentation-technique--backend)
5. [Documentation technique — Frontend Web](#5-documentation-technique--frontend-web)
6. [Documentation technique — Application Mobile](#6-documentation-technique--application-mobile)
7. [Base de données](#7-base-de-données)
8. [Référence API](#8-référence-api)
9. [Sécurité](#9-sécurité)
10. [Installation et démarrage](#10-installation-et-démarrage)
11. [Organisation du projet](#11-organisation-du-projet)

---

## 1. Présentation du projet

### 1.1 Contexte

L'évolution du secteur de la restauration s'accompagne d'une transformation numérique forte. De nombreux établissements français restent en retard face à des pays comme les États-Unis, le Japon ou la Corée du Sud qui ont rapidement adopté les technologies numériques dans leurs pratiques quotidiennes.

Dans une stratégie de diversification inspirée des grandes maisons de luxe françaises (Dior, Louis Vuitton), une grande marque a décidé d'ouvrir un café-restaurant haut de gamme. L'objectif est d'offrir à ses clients une expérience lifestyle immersive, où la gastronomie, l'élégance de la marque et la technologie se rencontrent dans un lieu unique.

### 1.2 Objectifs du projet

Le projet **SmartCafé** vise à développer un prototype fonctionnel permettant de moderniser les processus de commande et de gestion dans un restaurant haut de gamme. La solution repose sur trois piliers :

- **Fluidité** : réduction de l'attente grâce à la commande autonome via QR code ou application mobile.
- **Personnalisation** : capacité pour le client de configurer précisément ses articles.
- **Fidélisation** : utilisation des données (historique et points) pour engager la clientèle sur le long terme.

### 1.3 Livrables

| Livrable | Description |
|---|---|
| Application web (admin) | Interface de gestion du café (back-office) |
| Application mobile (iOS & Android) | Prise de commandes pour les clients |
| Backend API REST | Service Java Spring Boot exposant tous les endpoints |
| Base de données relationnelle | MySQL avec modèle de données complet |
| Documentation fonctionnelle et technique | Le présent document |

### 1.4 Stack technologique

| Couche | Technologie |
|---|---|
| Backend | Java 25, Spring Boot, Maven 4.0 |
| Frontend Web | React (Vite), JavaScript ES6+, Zustand |
| Application Mobile | Kotlin Multiplatform, Jetpack Compose, Ktor |
| Base de données | MySQL 8.0 |
| Sécurité | Spring Security, BCrypt |
| Communication | API REST, JSON, Axios (web) / Ktor (mobile) |

---

## 2. Architecture globale

### 2.1 Vue d'ensemble

L'application SmartCafé repose sur une architecture **client-serveur** avec une séparation stricte des responsabilités :

```
┌─────────────────────────────────────────────────────────────┐
│                     CLIENTS                                 │
│                                                             │
│  ┌─────────────────┐        ┌─────────────────────────┐    │
│  │  Frontend Web    │        │   Application Mobile     │    │
│  │  React (Vite)    │        │  Kotlin Multiplatform   │    │
│  │  localhost:5173  │        │  Android & iOS          │    │
│  └────────┬────────┘        └────────────┬────────────┘    │
│           │  HTTP/REST                   │  HTTP/REST       │
└───────────┼──────────────────────────────┼─────────────────┘
            │                              │
            ▼                              ▼
┌─────────────────────────────────────────────────────────────┐
│                  BACKEND (Spring Boot)                      │
│                   localhost:8081/api                        │
│                                                             │
│  ┌──────────────┐  ┌────────────┐  ┌──────────────────┐    │
│  │ AuthController│  │ProductCtrl │  │ AdminController  │    │
│  │ /api/auth    │  │/api/product│  │ /api/admin       │    │
│  └──────────────┘  └────────────┘  └──────────────────┘    │
│  ┌──────────────────────────────────────────────────────┐   │
│  │              Spring Security (BCrypt)                │   │
│  └──────────────────────────────────────────────────────┘   │
│  ┌──────────────────────────────────────────────────────┐   │
│  │              Spring Data JPA / Hibernate             │   │
│  └──────────────────────────┬───────────────────────────┘   │
└──────────────────────────────┼──────────────────────────────┘
                               │  JDBC
                               ▼
┌─────────────────────────────────────────────────────────────┐
│                   BASE DE DONNÉES                           │
│                  MySQL 8.0 — smartcafé                      │
│  users | products | orders | order_items | categories | ... │
└─────────────────────────────────────────────────────────────┘
```

### 2.2 Structure du dépôt

```
SmartCaf-/
├── admin/
│   ├── backend/          ← API Spring Boot
│   │   ├── src/main/java/com/smartcaf/
│   │   │   ├── controller/   ← Endpoints REST
│   │   │   ├── model/        ← Entités JPA
│   │   │   ├── repository/   ← Accès BDD (Spring Data)
│   │   │   ├── dto/          ← Objets de transfert
│   │   │   └── config/       ← Sécurité, CORS
│   │   └── src/main/resources/application.yml
│   └── frontend/         ← Interface React (admin + client)
│       └── src/
│           ├── components/   ← Composants réutilisables
│           ├── pages/        ← Pages client
│           ├── pages/admin/  ← Pages administration
│           ├── services/     ← Appels API
│           └── store/        ← State global (Zustand)
└── mobile/SmartCafeMobile/  ← App Kotlin Multiplatform
    └── composeApp/src/
        ├── commonMain/   ← Code partagé iOS/Android
        ├── androidMain/  ← Spécifique Android
        └── iosMain/      ← Spécifique iOS
```

---

## 3. Documentation fonctionnelle

### 3.1 Parcours utilisateur — Client (Web)

#### 3.1.1 Inscription

Un nouveau client accède à la page d'inscription depuis la page de connexion. Il renseigne son prénom, son nom, son adresse e-mail et un mot de passe. Après validation du formulaire, son compte est créé avec le rôle `CLIENT` et il est redirigé vers la page de connexion.

**[Capture d'écran : formulaire d'inscription avec les champs Prénom, Nom, Email, Mot de passe et le bouton "S'inscrire"]**

#### 3.1.2 Connexion

Le client saisit son adresse e-mail et son mot de passe. En cas d'identifiants incorrects, un message d'erreur explicite s'affiche. En cas de succès, le client est redirigé vers la page d'accueil.

**[Capture d'écran : page de connexion avec le formulaire Email/Mot de passe et le lien vers l'inscription]**

#### 3.1.3 Navigation sur le catalogue

Depuis la page d'accueil, le client visualise l'ensemble des produits disponibles. Il peut filtrer les produits par catégorie (boissons, pâtisseries, plats, etc.) ou effectuer une recherche par nom. Chaque produit est affiché sous forme de carte avec son image, son nom, sa description et son prix.

**[Capture d'écran : page d'accueil avec la grille de produits et le filtre par catégorie]**

#### 3.1.4 Détail d'un produit

En cliquant sur un produit, le client accède à sa fiche détaillée. Il peut sélectionner la quantité souhaitée et ajouter l'article à son panier.

**[Capture d'écran : page de détail d'un produit avec l'image, la description, le prix, le sélecteur de quantité et le bouton "Ajouter au panier"]**

#### 3.1.5 Panier

Le panier récapitule tous les articles sélectionnés, leurs quantités, leur prix unitaire et le montant total de la commande. Le client peut modifier les quantités ou supprimer des articles. Un bouton "Commander" lance la validation de la commande.

**[Capture d'écran : page du panier avec la liste des articles, les totaux et le bouton de validation]**

#### 3.1.6 Validation de commande

Lors de la validation, le client indique son nom, choisit le type de commande (sur place ou à emporter) et, le cas échéant, son numéro de table. La commande est envoyée au backend avec le statut `PENDING`.

**[Capture d'écran : formulaire de validation de commande avec les champs "Nom", "Type de commande" et "Numéro de table"]**

---

### 3.2 Parcours utilisateur — Administrateur (Web)

L'interface d'administration est accessible uniquement aux utilisateurs ayant le rôle `ADMIN`. Elle est protégée par la route `/admin` via le composant `ProtectedAdminRoute`.

#### 3.2.1 Tableau de bord (Dashboard)

Le tableau de bord synthétise les indicateurs clés du café en temps réel :
- Nombre total de produits et nombre de produits actifs
- Nombre total de commandes, commandes en attente et commandes terminées
- Chiffre d'affaires total
- Top 10 des produits les plus vendus (nom, catégorie, quantité vendue, chiffre d'affaires)

**[Capture d'écran : dashboard admin avec les 6 tuiles de statistiques et le tableau des top produits]**

#### 3.2.2 Gestion des produits

L'administrateur consulte la liste complète des produits. Il peut :
- Créer un nouveau produit (nom, description, prix, stock, catégorie, image)
- Modifier un produit existant
- Activer ou désactiver un produit (visible ou masqué pour les clients)
- Supprimer des produits individuellement ou en lot

**[Capture d'écran : page AdminProducts avec le tableau des produits, les boutons "Activer/Désactiver", "Modifier" et "Supprimer"]**

**[Capture d'écran : formulaire de création/modification d'un produit]**

#### 3.2.3 Gestion des commandes

L'administrateur visualise toutes les commandes classées par date décroissante. Il peut consulter le détail d'une commande (articles, quantités, prix, type) et mettre à jour son statut parmi les valeurs : `PENDING`, `IN_PREPARATION`, `READY`, `COMPLETED`, `CANCELLED`.

**[Capture d'écran : page AdminOrders avec le tableau des commandes et les badges de statut colorés]**

**[Capture d'écran : vue détail d'une commande avec la liste des articles et le sélecteur de statut]**

#### 3.2.4 Gestion des utilisateurs

L'administrateur consulte la liste de tous les utilisateurs inscrits (prénom, nom, email, rôle, date d'inscription). Il peut promouvoir un utilisateur au rôle `ADMIN` ou le rétrograder, et supprimer un compte.

**[Capture d'écran : page AdminUsers avec le tableau des utilisateurs et les actions de gestion de rôle]**

#### 3.2.5 Statistiques de ventes

Une page dédiée aux statistiques de vente permet à l'administrateur de visualiser les performances par produit : nom, catégorie, quantité totale vendue et chiffre d'affaires généré.

**[Capture d'écran : page AdminSales avec le tableau des ventes par produit]**

---

### 3.3 Parcours utilisateur — Client (Mobile)

L'application mobile SmartCafé est développée en **Kotlin Multiplatform** avec **Jetpack Compose Multiplatform** et cible Android et iOS depuis une base de code commune.

#### 3.3.1 Connexion et inscription

Au lancement de l'application, le client arrive sur l'écran de connexion. Il peut se connecter avec ses identifiants ou créer un nouveau compte via le lien d'inscription. Le formulaire d'inscription comprend le prénom, le nom, l'e-mail et le mot de passe.

**[Capture d'écran : écran de connexion mobile avec les champs Email/Mot de passe et le lien "Créer un compte"]**

**[Capture d'écran : écran d'inscription mobile avec les 4 champs et le bouton "S'inscrire"]**

#### 3.3.2 Accueil et catalogue produits

Après connexion, le client accède à la liste des produits disponibles. Chaque produit est affiché sous forme de carte avec son image, son nom, son prix et un bouton pour l'ajouter au panier.

**[Capture d'écran : écran d'accueil mobile avec la grille de produits et la barre de navigation]**

#### 3.3.3 Panier

Le client consulte son panier, modifie les quantités et supprime des articles. Le total est recalculé en temps réel.

**[Capture d'écran : écran du panier mobile avec la liste des articles et le total]**

#### 3.3.4 Choix du type de commande

Avant de valider, le client choisit entre "Sur place" et "À emporter". S'il choisit "Sur place", il peut scanner le QR code de sa table ou saisir manuellement son numéro.

**[Capture d'écran : écran de choix du type de commande avec les deux boutons "Sur place" et "À emporter"]**

#### 3.3.5 Scan QR Code

L'application intègre un scanner de QR code natif (ZXing sur Android, AVFoundation sur iOS). Lorsque le client scanne le QR code de sa table, le numéro de table est automatiquement associé à sa commande.

**[Capture d'écran : écran de scan QR code avec le viewfinder de l'appareil photo]**

#### 3.3.6 Paiement et confirmation

L'écran de paiement récapitule la commande et le montant total avant validation finale. Une fois la commande envoyée, un écran de confirmation affiche le numéro de commande et le statut initial.

**[Capture d'écran : écran de confirmation de commande avec le numéro de commande et le message de succès]**

---

## 4. Documentation technique — Backend

### 4.1 Vue d'ensemble

Le backend est une application **Java Spring Boot** exposant une API REST. Elle est structurée en couches distinctes selon le pattern MVC enrichi :

| Couche | Package | Rôle |
|---|---|---|
| Controller | `com.smartcaf.controller` | Exposition des endpoints HTTP |
| Model | `com.smartcaf.model` | Entités JPA / modèles de données |
| Repository | `com.smartcaf.repository` | Accès base de données (Spring Data JPA) |
| DTO | `com.smartcaf.dto` | Objets de transfert de données |
| Config | `com.smartcaf.config` | Configuration sécurité, CORS, BDD |

### 4.2 Dépendances principales (pom.xml)

- `spring-boot-starter-web` — Serveur HTTP embarqué (Tomcat)
- `spring-boot-starter-data-jpa` — ORM Hibernate
- `spring-boot-starter-security` — Spring Security
- `spring-boot-starter-validation` — Validation des beans (`@Valid`)
- `mysql-connector-j` — Driver MySQL
- `lombok` — Génération de code (getters, setters, constructeurs)

### 4.3 Configuration (`application.yml`)

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/smartcafé
    username: leo
    password: leo
  jpa:
    hibernate:
      ddl-auto: update     # Mise à jour automatique du schéma
    show-sql: true

server:
  port: 8081

cors:
  allowed-origins: http://localhost:3000,http://localhost:5173

jwt:
  secret: <clé_secrète>
  expiration: 86400000    # 24h
```

### 4.4 Entités JPA

#### User

```
users (id, firstName, lastName, email, password, role, createdAt)
```

- `role` : `CLIENT` ou `ADMIN`
- `password` : haché en BCrypt avant persistance

#### Product

```
products (id, name, description, price, stock, imageUrl, isActive, category, createdAt, updatedAt)
```

- `isActive` : contrôle la visibilité pour les clients
- `category` : catégorie textuelle du produit

#### Order

```
orders (id, userId, tableId, customerName, orderType, status, totalPrice, createdAt)
```

- `orderType` : `SUR_PLACE` ou `A_EMPORTER`
- `status` : `PENDING` → `IN_PREPARATION` → `READY` → `COMPLETED` / `CANCELLED`
- Relation `@OneToMany` avec `OrderItem`

#### OrderItem

```
order_items (id, order_id, product_id, quantity, price)
```

- Prix calculé à la création : `price = product.price × quantity`
- Relation `@ManyToOne` vers `Order` et vers `Product`

#### Category

```
categories (id, name, description)
```

### 4.5 Sécurité

La sécurité est gérée par **Spring Security** :

- Chiffrement des mots de passe : **BCryptPasswordEncoder**
- Politique de session : **STATELESS** (sans état serveur)
- CSRF désactivé (API REST)
- CORS configuré pour autoriser les origines des clients web et mobile

Les endpoints publics (sans authentification) sont :
- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /api/products/**`
- `POST /api/orders`
- `GET /api/admin/**` *(sécurisation côté frontend via ProtectedAdminRoute)*

> **Note** : La génération de JWT est prévue dans la configuration mais n'est pas encore finalisée côté backend (commentaire `TODO` dans `AuthController`). L'authentification est actuellement gérée par session côté frontend via le store Zustand.

---

## 5. Documentation technique — Frontend Web

### 5.1 Vue d'ensemble

Le frontend est développé en **React** avec **Vite** comme bundler. Il comprend deux espaces distincts :
- **Espace client** : navigation catalogue, panier, commande
- **Espace admin** : gestion complète du café (accessible sous `/admin`)

### 5.2 Structure des pages

| Route | Composant | Description |
|---|---|---|
| `/` | `Home.jsx` | Page d'accueil avec catalogue |
| `/products` | `Products.jsx` | Liste complète des produits |
| `/products/:id` | `ProductDetail.jsx` | Détail d'un produit |
| `/cart` | `Cart.jsx` | Panier d'achat |
| `/login` | `Login.jsx` | Connexion |
| `/register` | `Register.jsx` | Inscription |
| `/admin` | `AdminDashboard.jsx` | Tableau de bord admin |
| `/admin/products` | `AdminProducts.jsx` | Gestion produits |
| `/admin/orders` | `AdminOrders.jsx` | Gestion commandes |
| `/admin/users` | `AdminUsers.jsx` | Gestion utilisateurs |
| `/admin/sales` | `AdminSales.jsx` | Statistiques de ventes |

### 5.3 Gestion de l'état (State Management)

L'application utilise **Zustand** pour la gestion de l'état global :

#### `cartStore.js`
Gère le panier d'achat :
- `items` : liste des articles avec quantité
- `addToCart(product)` : ajoute ou incrémente un article
- `removeFromCart(id)` : retire un article
- `clearCart()` : vide le panier
- Persistance locale via `localStorage`

#### `userStore.js`
Gère l'état de l'utilisateur connecté :
- `user` : objet utilisateur (id, email, rôle, prénom, nom)
- `setUser(user)` : stocke l'utilisateur après connexion
- `logout()` : supprime l'utilisateur et redirige
- Persistance locale via `localStorage`

### 5.4 Service API (`services/api.js`)

Toutes les communications avec le backend passent par un client **Axios** centralisé pointant sur `http://localhost:8081/api`.

| Fonction | Méthode | Endpoint |
|---|---|---|
| `getProducts()` | GET | `/products` |
| `getProduct(id)` | GET | `/products/:id` |
| `createProduct(data)` | POST | `/products` |
| `updateProduct(id, data)` | PUT | `/products/:id` |
| `deleteProduct(id)` | DELETE | `/products/:id` |
| `searchProducts(term)` | GET | `/products/search?name=` |
| `getProductsByCategory(cat)` | GET | `/products/category/:cat` |
| `getDashboardStats()` | GET | `/admin/dashboard` |
| `getAdminProducts()` | GET | `/admin/products` |
| `toggleProductStatus(id)` | PUT | `/admin/products/:id/toggle-status` |
| `getAdminOrders()` | GET | `/admin/orders` |
| `updateOrderStatus(id, status)` | PUT | `/admin/orders/:id/status` |
| `getAdminUsers()` | GET | `/admin/users` |
| `updateUserRole(id, role)` | PUT | `/admin/users/:id/role` |
| `deleteAdminUser(id)` | DELETE | `/admin/users/:id` |
| `getSalesByProduct()` | GET | `/admin/sales/by-product` |

### 5.5 Protection des routes admin

Le composant `ProtectedAdminRoute.jsx` vérifie que l'utilisateur connecté possède le rôle `ADMIN` avant d'autoriser l'accès aux pages d'administration. Si l'utilisateur n'est pas connecté ou n'est pas admin, il est redirigé vers la page de connexion.

---

## 6. Documentation technique — Application Mobile

### 6.1 Vue d'ensemble

L'application mobile est développée avec **Kotlin Multiplatform (KMP)** et **Compose Multiplatform**. Cette approche permet de partager la logique métier, les composants UI et les appels réseau entre Android et iOS depuis une base de code unique.

### 6.2 Targets supportées

| Plateforme | Implémentation |
|---|---|
| Android | `androidMain` — activité `MainActivity` |
| iOS | `iosMain` — `MainViewController` via Swift |
| JVM (desktop, tests) | `jvmMain` |

### 6.3 Architecture MVVM

L'application suit le pattern **Model-View-ViewModel** :

```
UI (Screens Compose)
       ↕
ViewModel (état + logique)
       ↕
ApiService (appels réseau Ktor)
       ↕
Backend Spring Boot
```

#### ViewModels

| ViewModel | Responsabilité |
|---|---|
| `AuthViewModel` | Connexion, inscription, état de l'utilisateur |
| `ProductViewModel` | Chargement et filtrage du catalogue |
| `CartViewModel` | Gestion du panier (ajout, suppression, total) |
| `OrderViewModel` | Création de commande, récupération du statut |

### 6.4 Navigation

La navigation est gérée par un système de routes typées via la sealed class `AppScreen` :

| Écran | Description |
|---|---|
| `Login` | Connexion |
| `Register` | Inscription |
| `Home` | Catalogue produits |
| `Cart` | Panier |
| `OrderType` | Choix sur place / à emporter |
| `QRScan` | Scanner QR code de table |
| `Payment(orderType, tableId)` | Récapitulatif avant paiement |
| `Confirmation(orderId, orderType, tableId)` | Confirmation commande |

### 6.5 Réseau (Ktor)

Les appels réseau sont effectués avec **Ktor** (client HTTP multiplateforme) :

- Android : `ktor-client-android`
- iOS : `ktor-client-darwin`
- Sérialisation : `kotlinx.serialization` (JSON)

L'URL de base pointe sur `http://10.0.2.2:8081` pour l'émulateur Android (qui mappe `10.0.2.2` vers `localhost` de la machine hôte) et sur `http://localhost:8081` pour iOS simulateur.

### 6.6 Scanner QR Code

Le scan QR code est implémenté de façon native selon la plateforme :

- **Android** : utilisation de la bibliothèque **ZXing** (`zxing-android-embedded`)
- **iOS** : utilisation de **AVFoundation** (framework natif Apple)

L'interface commune est définie via `expect/actual` dans `QRScanner.kt`.

### 6.7 Dépendances principales

```toml
[libraries]
compose-material3
ktor-client-core
ktor-client-content-negotiation
ktor-serialization-json
kotlinx-serialization-json
coil-compose             # Chargement d'images asynchrone
coil-network-ktor
androidx-lifecycle-viewmodelCompose
zxing-android-embedded   # Scan QR (Android)
```

---

## 7. Base de données

### 7.1 Système de gestion

- **SGBD** : MySQL 8.0
- **Nom de la base** : `smartcafé`
- **Encodage** : UTF-8 (utf8mb4)

### 7.2 Schéma de la base de données

**[Diagramme entité-relation (ERD) de la base de données SmartCafé avec les 11 tables et leurs relations]**

### 7.3 Description des tables

#### `users`
| Colonne | Type | Contrainte | Description |
|---|---|---|---|
| `id` | BIGINT | PK, AUTO_INCREMENT | Identifiant unique |
| `firstname` | VARCHAR(255) | NOT NULL | Prénom |
| `lastname` | VARCHAR(255) | NOT NULL | Nom |
| `email` | VARCHAR(255) | NOT NULL, UNIQUE | Adresse e-mail |
| `password` | VARCHAR(255) | NOT NULL | Mot de passe haché (BCrypt) |
| `role` | VARCHAR(50) | NOT NULL, DEFAULT 'CLIENT' | Rôle : CLIENT ou ADMIN |
| `created_at` | DATETIME | NOT NULL | Date de création du compte |

#### `products`
| Colonne | Type | Contrainte | Description |
|---|---|---|---|
| `id` | BIGINT | PK, AUTO_INCREMENT | Identifiant unique |
| `category_id` | BIGINT | FK → categories.id | Catégorie du produit |
| `name` | VARCHAR(255) | NOT NULL | Nom du produit |
| `description` | VARCHAR(1000) | | Description |
| `price` | DECIMAL(10,2) | NOT NULL | Prix unitaire |
| `stock` | INT | NOT NULL | Quantité disponible |
| `image_url` | VARCHAR(255) | | URL de l'image |
| `is_active` | TINYINT(1) | DEFAULT 1 | Produit actif/inactif |
| `created_at` | DATETIME | | Date de création |
| `updated_at` | DATETIME | | Date de mise à jour |

#### `categories`
| Colonne | Type | Description |
|---|---|---|
| `id` | BIGINT | PK, AUTO_INCREMENT |
| `name` | VARCHAR(255) | Nom de la catégorie |
| `description` | VARCHAR(255) | Description |

#### `orders`
| Colonne | Type | Description |
|---|---|---|
| `id` | BIGINT | PK, AUTO_INCREMENT |
| `user_id` | BIGINT | FK → users.id (nullable) |
| `table_id` | BIGINT | FK → tables.id (nullable) |
| `customer_name` | VARCHAR(255) | Nom du client |
| `order_type` | VARCHAR(50) | SUR_PLACE ou A_EMPORTER |
| `status` | VARCHAR(50) | PENDING / IN_PREPARATION / READY / COMPLETED / CANCELLED |
| `total_price` | DECIMAL(10,2) | Montant total |
| `created_at` | DATETIME | Date de création |

#### `order_items`
| Colonne | Type | Description |
|---|---|---|
| `id` | BIGINT | PK, AUTO_INCREMENT |
| `order_id` | BIGINT | FK → orders.id |
| `product_id` | BIGINT | FK → products.id |
| `quantity` | INT | Quantité commandée |
| `price` | DECIMAL(10,2) | Prix total de la ligne |

#### `order_item_options`
| Colonne | Type | Description |
|---|---|---|
| `id` | BIGINT | PK |
| `order_item_id` | BIGINT | FK → order_items.id |
| (options supplémentaires) | | Options/suppléments de l'article |

#### `product_options`
| Colonne | Type | Description |
|---|---|---|
| `id` | BIGINT | PK |
| `product_id` | BIGINT | FK → products.id |
| `name` | VARCHAR(255) | Nom de l'option |
| `extra_price` | DECIMAL(10,2) | Supplément de prix |

#### `tables`
| Colonne | Type | Description |
|---|---|---|
| `id` | BIGINT | PK |
| `table_number` | INT | Numéro de table |
| `qr_code` | VARCHAR(255) | Contenu du QR code associé |

#### `payments`
| Colonne | Type | Description |
|---|---|---|
| `id` | BIGINT | PK |
| `order_id` | BIGINT | FK → orders.id |
| `payment_method` | VARCHAR(50) | Méthode de paiement |
| `status` | VARCHAR(50) | Statut du paiement |
| `amount` | DECIMAL(10,2) | Montant payé |

#### `notifications`
| Colonne | Type | Description |
|---|---|---|
| `id` | BIGINT | PK |
| `user_id` | BIGINT | FK → users.id |
| `order_id` | BIGINT | FK → orders.id |
| `message` | VARCHAR(255) | Contenu de la notification |
| `is_read` | TINYINT(1) | Lu ou non lu |
| `created_at` | DATETIME | Date de création |

#### `favorites`
| Colonne | Type | Description |
|---|---|---|
| `id` | BIGINT | PK |
| `user_id` | BIGINT | FK → users.id |
| `product_id` | BIGINT | FK → products.id |

---

## 8. Référence API

### 8.1 Authentification

#### POST `/api/auth/register`

Crée un nouveau compte utilisateur.

**Corps de la requête :**
```json
{
  "firstName": "Jean",
  "lastName": "Dupont",
  "email": "jean.dupont@email.com",
  "password": "motdepasse123"
}
```

**Réponses :**
| Code | Description |
|---|---|
| `200 OK` | `{"message": "Inscription réussie"}` |
| `400 Bad Request` | `{"message": "Email déjà utilisé"}` |

---

#### POST `/api/auth/login`

Authentifie un utilisateur existant.

**Corps de la requête :**
```json
{
  "email": "jean.dupont@email.com",
  "password": "motdepasse123"
}
```

**Réponses :**
| Code | Description |
|---|---|
| `200 OK` | `{"message": "Connexion réussie", "user": {...}}` |
| `401 Unauthorized` | `{"message": "Email ou mot de passe incorrect"}` |

---

### 8.2 Produits

#### GET `/api/products`
Retourne la liste de tous les produits actifs.

#### GET `/api/products/{id}`
Retourne un produit par son identifiant.

#### GET `/api/products/category/{category}`
Filtre les produits par catégorie.

#### GET `/api/products/search?name={terme}`
Recherche des produits par nom (insensible à la casse).

#### POST `/api/products`
Crée un nouveau produit.

**Corps :**
```json
{
  "name": "Café Allongé",
  "description": "Café long, doux et équilibré",
  "price": 3.50,
  "stock": 100,
  "category": "boissons",
  "imageUrl": "https://...",
  "isActive": true
}
```

#### PUT `/api/products/{id}`
Met à jour un produit existant.

#### DELETE `/api/products/{id}`
Supprime un produit.

---

### 8.3 Commandes

#### POST `/api/orders`
Crée une nouvelle commande.

**Corps :**
```json
{
  "customerName": "Jean Dupont",
  "orderType": "SUR_PLACE",
  "tableId": 3,
  "items": [
    { "productId": 1, "quantity": 2 },
    { "productId": 5, "quantity": 1 }
  ]
}
```

**Réponse `200 OK` :** l'objet `Order` créé avec son `id` et le total calculé.

#### GET `/api/orders/{id}`
Retourne le détail d'une commande par son identifiant.

---

### 8.4 Administration

Tous les endpoints `/api/admin/**` sont réservés aux utilisateurs `ADMIN`.

#### GET `/api/admin/dashboard`
Retourne les statistiques globales du café.

**Réponse :**
```json
{
  "totalProducts": 24,
  "activeProducts": 20,
  "totalOrders": 156,
  "pendingOrders": 8,
  "completedOrders": 143,
  "totalRevenue": 2845.50,
  "topProducts": [...]
}
```

#### GET `/api/admin/products`
Liste tous les produits (actifs et inactifs).

#### PUT `/api/admin/products/{id}/toggle-status`
Bascule l'état actif/inactif d'un produit.

#### DELETE `/api/admin/products/batch`
Supprime plusieurs produits en une requête.

**Corps :** `[1, 2, 3]` (liste d'identifiants)

#### GET `/api/admin/orders`
Liste toutes les commandes par date décroissante.

#### GET `/api/admin/orders/{id}`
Retourne le détail d'une commande.

#### PUT `/api/admin/orders/{id}/status`
Met à jour le statut d'une commande.

**Corps :** `{"status": "IN_PREPARATION"}`

#### GET `/api/admin/sales/by-product`
Retourne les statistiques de ventes agrégées par produit.

#### GET `/api/admin/users`
Liste tous les utilisateurs.

#### PUT `/api/admin/users/{id}/role`
Modifie le rôle d'un utilisateur.

**Corps :** `{"role": "ADMIN"}`

#### DELETE `/api/admin/users/{id}`
Supprime un utilisateur.

#### GET `/api/admin/categories`
Liste toutes les catégories.

#### POST `/api/admin/categories`
Crée une nouvelle catégorie.

#### PUT `/api/admin/categories/{id}`
Met à jour une catégorie.

#### DELETE `/api/admin/categories/{id}`
Supprime une catégorie.

---

## 9. Sécurité

### 9.1 Hachage des mots de passe

Les mots de passe ne sont jamais stockés en clair. Lors de l'inscription, le mot de passe est haché avec **BCrypt** (facteur de coût par défaut = 10) avant d'être persisté en base de données.

```java
user.setPassword(passwordEncoder.encode(user.getPassword()));
```

Lors de la connexion, la vérification est effectuée par `passwordEncoder.matches(...)` sans jamais déchiffrer le hash.

### 9.2 CORS

La configuration CORS est centralisée dans `CorsConfig.java`. Seules les origines déclarées dans `application.yml` sont autorisées (`localhost:3000`, `localhost:5173`). Les requêtes preflight `OPTIONS` sont systématiquement autorisées.

### 9.3 Spring Security

La chaîne de filtres Spring Security est configurée en mode **STATELESS** (sans session serveur). Le CSRF est désactivé car l'API est consommée par des clients front-end qui gèrent eux-mêmes les tokens.

### 9.4 Points d'amélioration identifiés

- **JWT** : l'implémentation JWT est prévue mais non finalisée. Actuellement, l'état de connexion est maintenu côté client uniquement (Zustand + localStorage). En production, il faudrait implémenter une génération de token JWT signé à la connexion et une validation côté backend sur chaque requête protégée.
- **Autorisation admin** : les endpoints `/api/admin/**` sont actuellement ouverts au niveau du filtre Spring Security. La protection est assurée uniquement côté frontend (`ProtectedAdminRoute`). En production, il faudrait vérifier le rôle côté backend via `@PreAuthorize("hasRole('ADMIN')")`.
- **HTTPS** : à configurer obligatoirement en environnement de production.

---

## 10. Installation et démarrage

### 10.1 Prérequis

| Outil | Version minimale |
|---|---|
| Java (JDK) | 17+ |
| Maven | 3.6+ |
| Node.js | 18+ |
| MySQL | 8.0 |
| Android Studio | 2024.1+ (pour le mobile) |

### 10.2 Base de données

1. Créer la base de données MySQL :
```sql
CREATE DATABASE `smartcafé` CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
```

2. Importer le schéma et les données :
```bash
mysql -u root -p smartcafé < admin/backend/smartcafé.sql
```

3. Configurer les identifiants dans `admin/backend/src/main/resources/application.yml` :
```yaml
spring:
  datasource:
    username: <votre_utilisateur>
    password: <votre_mot_de_passe>
```

### 10.3 Backend Spring Boot

```bash
cd admin/backend

# Démarrer l'application
./mvnw spring-boot:run
```

Le backend est accessible sur **http://localhost:8081**

**[Capture d'écran : terminal affichant le démarrage Spring Boot avec le message "Started SmartCafApplication"]**

### 10.4 Frontend Web (React)

```bash
cd admin/frontend

# Installer les dépendances
npm install

# Lancer en développement
npm run dev
```

Le frontend est accessible sur **http://localhost:5173**

**[Capture d'écran : terminal affichant le démarrage Vite avec l'URL locale]**

### 10.5 Application Mobile (Android)

1. Ouvrir le dossier `mobile/SmartCafeMobile` dans Android Studio.
2. Attendre la synchronisation Gradle.
3. Sélectionner le target `composeApp` et lancer sur un émulateur ou appareil physique.
4. S'assurer que le backend tourne et que l'URL dans `ApiClient.kt` est correctement configurée.

**[Capture d'écran : Android Studio avec le projet ouvert et le bouton Run]**

### 10.6 Application Mobile (iOS)

1. Ouvrir `mobile/SmartCafeMobile/iosApp/iosApp.xcodeproj` dans Xcode.
2. Compiler et lancer sur un simulateur ou appareil iOS.

---

## 11. Organisation du projet

### 11.1 Méthodologie

Le projet a été développé en suivant une approche agile inspirée de **Scrum**, avec des itérations organisées autour des phases de la roadmap :

| Phase | Objectif | État |
|---|---|---|
| Phase 0 | Setup API + Figma | Terminé |
| Phase 1 | MVP fonctionnel (catalogue, panier, commande, auth) | Terminé |
| Phase 2 | Back-office admin, statistiques | Terminé |
| Phase 3 | QR Code, types de commande, mobile | En cours |
| Phase 4 | Fidélisation, notifications push, déploiement | Prévu |

### 11.2 Gestion de versions (Git)

Le code source est versionné sur **GitHub** à l'adresse :
`https://github.com/Meca-3D/SmartCaf-.git`

**[Capture d'écran : page GitHub du dépôt SmartCaf- avec la liste des commits et les branches]**

La branche principale est `main`. Les développements sont réalisés selon le workflow suivant :
- `main` : branche stable, code testé
- Branches de fonctionnalités : `feature/<nom>` pour chaque développement

### 11.3 Répartition des tâches

**[Capture d'écran : tableau Trello/Notion/GitHub Projects avec les tâches réparties par membre de l'équipe]**

### 11.4 Diagramme de Gantt

**[Capture d'écran ou diagramme : planning du projet avec les jalons des 4 phases sur la durée du projet]**

---

*Document rédigé dans le cadre du projet UF DEV B3 — Ynov Informatique*
*SmartCafé — Dépôt : https://github.com/Meca-3D/SmartCaf-.git*
