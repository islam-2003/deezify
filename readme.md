# INFO-F-307: Génie Logiciel et Gestion de Projets

## 2024-2025 – Deezify

### Description du Projet
Deezify est un lecteur de musique moderne et polyvalent, développé en Java, offrant une expérience utilisateur intuitive et riche en fonctionnalités. L'application permet de gérer, organiser et écouter de la musique de manière fluide, avec des options avancées pour personnaliser l'expérience d'écoute. Elle est compatible avec les systèmes d'exploitation **Linux, Windows et MacOS**.

---

### Fonctionnalités Implémentées

#### **Gestion de la Musique**
- **Lecture de morceaux** : Navigation parmi les morceaux (MP3, FLAC, etc.) avec affichage de la durée et du temps écoulé. *(Histoire 1)*  
  Priorité Client: 1 | Risque Développeur: 1
- **File d'attente** : Ajout/suppression de morceaux, lecture automatique du suivant. *(Histoire 2)*  
  Priorité Client: 1 | Risque Développeur: 1
- **Boutons de contrôle** : Play/pause, navigation, volume. *(Histoire 3)*  
  Priorité Client: 1 | Risque Développeur: 1
- **Métadonnées** : Édition des informations (artiste, titre, album). *(Histoire 4)*  
  Priorité Client: 1 | Risque Développeur: 2
- **Autocomplétion** : Suggestions pour les tags, artistes ou albums existants. *(Histoire 5)*  
  Priorité Client: 2 | Risque Développeur: 2

#### **Organisation et Recherche**
- **Tags** : Attribution de tags personnalisés ou prédéfinis. *(Histoire 6)*  
  Priorité Client: 2 | Risque Développeur: 2
- **Recherche** : Par titre, artiste, album ou tags. *(Histoire 7)*  
  Priorité Client: 1 | Risque Développeur: 2
- **Playlists** : Création, réorganisation et lecture (remplacement ou ajout à la file). *(Histoire 8)*  
  Priorité Client: 1 | Risque Développeur: 1
- **Suggestions** : Morceaux similaires basés sur les tags/artistes. *(Histoire 9)*  
  Priorité Client: 3 | Risque Développeur: 2

#### **Expérience d'Écoute**
- **Visualiseur graphique** : Animation synchronisée avec la musique. *(Histoire 12)*  
  Priorité Client: 2 | Risque Développeur: 3
- **Paroles** : Affichage pendant la lecture. *(Histoire 13)*  
  Priorité Client: 3 | Risque Développeur: 3
- **Karaoké** : Paroles synchronisées. *(Histoire 14)*  
  Priorité Client: 3 | Risque Développeur: 1
- **Transitions** : Fondu entre morceaux (durée réglable). *(Histoire 15)*  
  Priorité Client: 3 | Risque Développeur: 3
- **Contrôles avancés** : Vitesse de lecture, aléatoire, balance audio. *(Histoire 16)*  
  Priorité Client: 1 | Risque Développeur: 1

#### **Personnalisation**
- **Multilinguisme** : Français, néerlandais, anglais. *(Histoire 18)*  
  Priorité Client: 2 | Risque Développeur: 2
- **Pochettes d'album** : Ajout et affichage. *(Histoire 19)*  
  Priorité Client: 3 | Risque Développeur: 3
- **Favoris** : Playlist par défaut. *(Histoire 21)*  
  Priorité Client: 2 | Risque Développeur: 3
- **Comptes utilisateurs** : Gestion multi-utilisateurs avec dossiers personnels. *(Histoire 22)*  
  Priorité Client: 3 | Risque Développeur: 3

---

### Fonctionnalités Non Implémentées
Les histoires suivantes n'ont pas été demandées :
- Mode « DJ » *(Histoire 10)*  
  Priorité Client: 2 | Risque Développeur: 2
- Égaliseur *(Histoire 11)*  
  Priorité Client: 3 | Risque Développeur: 2
- Radio *(Histoire 17)*  
  Priorité Client: 2 | Risque Développeur: 2
- Vidéo comme pochette *(Histoire 20)*  
  Priorité Client: 2 | Risque Développeur: 3

---

### Compatibilité et Technologies

- **Systèmes supportés** : Linux, Windows, MacOS
- **Langage** : Java (≥ 18)
- **Outils de build** : Maven
- **Bibliothèques principales** :
   - *JavaFX* (controls, fxml, media) — Interface graphique et gestion multimédia
   - *jaudiotagger* — Lecture et écriture des métadonnées audio
   - *Gson* — Sérialisation/désérialisation JSON
   - *Lombok* — Génération automatique de code (getters, setters, etc.)
   - *ControlsFX* — Composants graphiques avancés pour JavaFX
- **Tests et qualité** :
   - *JUnit Jupiter* — Framework de tests unitaires
   - *Mockito* — Mocking pour les tests unitaires

---

### Compilation et Exécution

#### Méthode 1 : Lancer le projet avec Maven

Pour compiler et exécuter le projet directement avec Maven, vous pouvez suivre ces étapes :

1. **Compiler le projet** :
    ```
    mvn clean install
    ```
2. **Tester le projet** :
    ```
    mvn test
    ```
3. **Lancer l'application** :
    ```
   mvn exec:java
   ```

#### Méthode 2 : Créer un JAR et exécuter le projet compilé

Pour compiler le projet, puis le lancer avec le fichier JAR, procédez ainsi :

1. **Compiler le projet et créer le fichier JAR** :
   ```
   mvn clean package
   ```

2. **Lancer l'application avec le fichier JAR** :
   ```
   java -jar target/be.deezify.jar
   ```

### Activer le profil JavaFX 23

Le projet supporte **JavaFX 17** par défaut, mais vous pouvez facilement basculer vers **JavaFX 23** en activant le
profil approprié dans Maven.

Voici comment procéder :

Pour compiler le projet avec **JavaFX 23** :

   ```
   mvn clean install -Plocal
   ```

Pour lancer l'application avec JavaFX 23 :

   ```
   mvn exec:java -Plocal
   ```

---

### Objectif

Développement d'un lecteur de musique moderne et polyvalent, capable de
s’adapter aux attentes variées des utilisateurs. Ce projet vise à offrir une application intuitive et riche
en fonctionnalités, permettant aux utilisateurs de gérer, organiser et écouter leur musique de manière
fluide et agréable. Que ce soit pour créer des playlists, explorer des morceaux par tags, ou simplement
profiter d’une écoute immersive avec des effets audio personnalisés, l'application répond
à une multitude de besoins.
    