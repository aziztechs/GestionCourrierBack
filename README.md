# Gestion des Courriers - Backend

## Description du Projet
Ce projet est une application de gestion de courriers développée pour le COUD (Centre des Œuvres Universitaires de Dakar). Il permet de gérer les courriers internes et externes, leur suivi, ainsi que les utilisateurs du système.

## Technologies Utilisées
- **Spring Boot 3.2.3** : Framework Java pour le développement d'applications
- **Spring Data JPA** : Pour la persistance des données
- **MySQL** : Base de données relationnelle
- **Swagger/OpenAPI** : Pour la documentation de l'API
- **Maven** : Outil de gestion de dépendances et de build

## Prérequis
- Java 17 ou supérieur
- MySQL 8.0 ou supérieur
- Maven 3.6 ou supérieur

## Configuration de la Base de Données
La configuration de la base de données se trouve dans le fichier `src/main/resources/application.properties`. Par défaut, l'application utilise les paramètres suivants :

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/gestion_courrier_coud?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

La base de données sera créée automatiquement si elle n'existe pas, grâce à l'option `createDatabaseIfNotExist=true`.

## Installation et Démarrage

### Cloner le Projet
```bash
git clone <url-du-repo>
cd GestionCourrierBack
```

### Compiler et Exécuter
```bash
# Avec Maven
mvn clean install
mvn spring-boot:run

# Ou avec le wrapper Maven inclus
./mvnw clean install
./mvnw spring-boot:run
```

L'application sera accessible à l'adresse : http://localhost:8081

## Documentation de l'API
La documentation de l'API est générée automatiquement avec Swagger/OpenAPI et est accessible aux URLs suivantes :

- **Interface Swagger UI** : http://localhost:8081/swagger-ui.html
- **Documentation OpenAPI** : http://localhost:8081/api-docs

## Structure du Projet

### Entités
- **User** : Représente un utilisateur du système (nom, prénom, username, matricule, etc.)
- **Courrier** : Représente un courrier (numéro, objet, type, nature, etc.)
- **Suivi** : Représente le suivi d'un courrier (instruction, description, date)

### Endpoints Principaux

#### Utilisateurs
- `GET /api/users` : Liste tous les utilisateurs
- `GET /api/users/{id}` : Récupère un utilisateur par son ID
- `POST /api/users` : Crée un nouvel utilisateur
- `PUT /api/users/{id}` : Met à jour un utilisateur
- `DELETE /api/users/{id}` : Supprime un utilisateur

#### Courriers
- `GET /api/courriers` : Liste tous les courriers
- `GET /api/courriers/{id}` : Récupère un courrier par son ID
- `POST /api/courriers` : Crée un nouveau courrier
- `PUT /api/courriers/{id}` : Met à jour un courrier
- `DELETE /api/courriers/{id}` : Supprime un courrier

#### Suivis
- `GET /api/suivis` : Liste tous les suivis
- `GET /api/suivis/{id}` : Récupère un suivi par son ID
- `POST /api/suivis` : Crée un nouveau suivi
- `PUT /api/suivis/{id}` : Met à jour un suivi
- `DELETE /api/suivis/{id}` : Supprime un suivi

## Initialisation des Données
L'application inclut un composant `DataInitializer` qui peuple automatiquement la base de données avec des données d'exemple lors du premier démarrage. Cela inclut :
- Des utilisateurs avec différents rôles
- Des courriers de différents types et natures
- Des suivis pour ces courriers

## Fonctionnalités
- Gestion complète des utilisateurs (CRUD)
- Gestion des courriers internes et externes
- Suivi des courriers
- Upload de fichiers PDF pour les courriers
- Recherche de courriers par différents critères (date, type, nature, etc.)
- Documentation API complète avec Swagger

## Frontend
Cette application backend est conçue pour fonctionner avec un frontend Angular 15. Le frontend communique avec le backend via les API REST exposées.

## Licence
Ce projet est développé par Abdou Aziz NDIAYE ingénieur génie logiciel et consultant IT au niveau du COUD (Centre des Œuvres Universitaires de Dakar).
