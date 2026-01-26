# SmartCaf E-commerce

Application e-commerce complète avec backend Java Spring Boot et frontend React.

## Structure du Projet

```
SmartCaf-/
├── backend/          # Application Spring Boot
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/smartcaf/
│   │   │   │   ├── SmartCafApplication.java
│   │   │   │   ├── config/
│   │   │   │   ├── model/
│   │   │   │   ├── repository/
│   │   │   │   └── controller/
│   │   │   └── resources/
│   │   │       └── application.yml
│   │   └── test/
│   └── pom.xml
│
└── frontend/         # Application React
    ├── src/
    │   ├── components/
    │   ├── pages/
    │   ├── services/
    │   ├── store/
    │   ├── App.jsx
    │   └── main.jsx
    └── package.json
```

## Prérequis

### Backend
- Java 17 ou supérieur
- Maven 3.6+

### Frontend
- Node.js 18+ 
- npm ou yarn

## Installation et Démarrage

### 1. Backend (Spring Boot)

```bash
cd backend

# Installer les dépendances et lancer l'application
./mvnw spring-boot:run

# Ou avec Maven installé globalement
mvn spring-boot:run
```

Le backend démarre sur **http://localhost:8080**

**Console H2** (base de données en mémoire) : http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:smartcafdb`
- Username: `sa`
- Password: (vide)

### 2. Frontend (React + Vite)

```bash
cd frontend

# Installer les dépendances
npm install

# Lancer le serveur de développement
npm run dev
```

Le frontend démarre sur **http://localhost:3000**

## Fonctionnalités Actuelles

### Backend
- ✅ API REST complète pour les produits (CRUD)
- ✅ Configuration Spring Security avec CORS
- ✅ Base de données H2 en mémoire (développement)
- ✅ Modèles: Product, User
- ✅ Validation des données avec Bean Validation
- ✅ Support PostgreSQL pour production

### Frontend
- ✅ Navigation avec React Router
- ✅ Page d'accueil
- ✅ Liste des produits
- ✅ Détail d'un produit
- ✅ Panier d'achat avec persistance (LocalStorage)
- ✅ Gestion d'état avec Zustand
- ✅ Design responsive

## Endpoints API Principaux

### Produits
- `GET /api/products` - Liste tous les produits
- `GET /api/products/{id}` - Détail d'un produit
- `GET /api/products/category/{category}` - Produits par catégorie
- `GET /api/products/search?name={name}` - Recherche de produits
- `POST /api/products` - Créer un produit
- `PUT /api/products/{id}` - Modifier un produit
- `DELETE /api/products/{id}` - Supprimer un produit

## Prochaines Étapes

### À développer
- [ ] Système d'authentification JWT complet
- [ ] Gestion des commandes
- [ ] Gestion des catégories
- [ ] Système de paiement (Stripe/PayPal)
- [ ] Espace administrateur
- [ ] Upload d'images
- [ ] Gestion des avis clients
- [ ] Historique des commandes
- [ ] Email de confirmation

### Configuration Production
- [ ] Configurer PostgreSQL
- [ ] Variables d'environnement
- [ ] Docker compose
- [ ] CI/CD

## Technologies Utilisées

### Backend
- Spring Boot 3.2.1
- Spring Data JPA
- Spring Security
- H2 Database (dev) / PostgreSQL (prod)
- Maven
- Lombok
- JWT (jjwt)

### Frontend
- React 18
- Vite
- React Router DOM
- Axios
- Zustand (state management)

## Scripts Disponibles

### Backend
```bash
mvn clean install    # Compiler le projet
mvn test            # Lancer les tests
mvn spring-boot:run # Démarrer l'application
```

### Frontend
```bash
npm run dev         # Mode développement
npm run build       # Build production
npm run preview     # Prévisualiser le build
npm run lint        # Linter le code
```

## Contribution

1. Créer une branche pour votre fonctionnalité
2. Commiter vos changements
3. Pousser vers la branche
4. Ouvrir une Pull Request

## Licence

MIT
