# Sauvegarde des données (SharedPreferences et fichiers) avec bonnes pratiques de sécurité

Ce projet est une application Android développée en Java illustrant les différentes méthodes de stockage de données locales tout en respectant les standards de sécurité modernes.

## Fonctionnalités
- **SharedPreferences classiques** : Stockage de configurations simples (alias, langue, mode visuel).
- **EncryptedSharedPreferences** : Utilisation de la bibliothèque AndroidX Security pour chiffrer les données sensibles (tokens API).
- **Stockage Interne** : Persistance de fichiers texte et JSON en mode privé.
- **Gestion du Cache** : Utilisation du répertoire temporaire pour les logs de session.
- **Stockage Externe (App-specific)** : Exportation de données vers le stockage externe propre à l'application.

## Sécurité
- Utilisation de `MasterKey` pour la gestion des clés de chiffrement via Android Keystore.
- Chiffrement AES-256 pour les clés et les valeurs.
- Absence de logs contenant des informations sensibles.



## Technologies utilisées
- Langage : Java
- SDK Minimum : 24
- Bibliothèques : `androidx.security:security-crypto`, `org.json`

## Auteur
[Votre Nom / Saif]
