
  PROJET DE SIMULATION DE RESTAURANT
==================================

Ce document explique comment installer et exécuter le programme de simulation de restaurant .


PRÉREQUIS
---------
- Java JDK 8 ou supérieur
- Eclipse IDE pour Java Developers

INSTALLATION
-----------
1. Téléchargez et décompressez l'archive du projet restaurant

2. Importez le projet dans Eclipse

EXÉCUTION DU PROGRAMME
---------------------
1. Dans l'explorateur de projets Eclipse (à gauche), localisez le package "main"
2. Ouvrez le package "main"
3. Faites un clic droit sur la classe "JeuRestaurant.java"
4. Sélectionnez "Run As" > "Java Application"

Le programme devrait démarrer et afficher l'interface graphique du restaurant.


ORGANISATION DU PROJET 
--------------------------
L'espace de travail contient deux dossiers :

- src : le dossier pour gérer les sources

Dans 'src/main' se trouve le contenu du projet :

-'data':Ce package contient les classes représentant les données et les modèles du restaurant.
-'gestion':Ce package contient les contrôleurs qui gèrent la logique et la gestion.
-'affichage':Ce package contient les classes liées à l'interface utilisateur.
-'images':Ce package contient toutes les images lié à l'affichage correct du restaurant.

BUT
------
C'est un système de gestion de restaurant basé sur Java, développé dans le cadre d'un projet scolaire.
Qui traites toutes ces mécaniques :  

- Traitement des commandes des clients
- Gestion des employés
- Contrôle des stocks
- Recueil de données 

UTILISATION DE BASE
------------------
- Cliquez sur le bouton "Embaucher" pour recruter du personnel
- Utilisez "Gérer le stock" pour acheter des ingrédients
- Accédez à "Gérer le menu" pour ajouter des plats
- Cliquez sur "Améliorer" pour acheter des tables et des décorations
- Le bouton "Pause" permet de mettre en pause la simulation


