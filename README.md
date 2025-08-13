# Conference Management API

## Description
Cette API permet de gérer les conférences, avec un système d'authentification JWT, une gestion des rôles, et des règles métier pour les talks, salles et plannings.

## Fonctionnalités principales
- **Authentification & inscription** (`/auth/register`, `/auth/login`)
- **Gestion des rôles** :  
  - `SPEAKER` : soumettre des talks  
  - `ADMIN` : gérer plannings et salles
- **CRUD complet** pour toutes les entités
- **Règles métier** :
  - Empêcher deux talks dans la même salle au même moment
  - Respect de la capacité maximale d'une salle
  - Durée des talks : 15 à 90 minutes
  - Pas de titres en double dans une conférence
- **Feedback** : un seul feedback par participant et par talk

---
## crée un USER
- ** POST /auth/register
- ** Content-Type: application/json

{
    "email": "admin@example.com",
    "password": "P@ssw0rd",
    "fullName": "Jane Admin",
    "role": "ADMIN"
}

##Récuperer le Token 
-**POST /auth/login
-**Content-Type: application/json
{
    "email": "admin@example.com",
    "password": "P@ssw0rd"
}
<img width="1322" height="625" alt="Login" src="https://github.com/user-attachments/assets/8045e35b-8494-40db-8853-a322335bc1e2" />
<img width="1367" height="458" alt="Register" src="https://github.com/user-attachments/assets/4f2d1f98-48ec-49ec-9b1e-c9430707c796" />



