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

Le backend démarre sur **http://localhost:8081/api/products**

### 2. Frontend (React + Vite)

```bash
cd frontend

# Installer les dépendances
npm install

# Lancer le serveur de développement
npm run dev
```

Le frontend démarre sur **http://localhost:3000**
