# HAI716I - Mistress Run

[English translation of this file can be found here.](./README_EN.md)

Un projet de programmation multi-agents.

# Sommaire

* [Compilation](#compilation)
* [Exécution](#exécution)
* [Ressources](#ressources)
* [Sources](#sources)

# Compilation

Ce projet utilise Maven. Ainsi, le fichier `pom.xml` automatise la compilation et l'installation des dépendances. Une directive de compilation permet de créer une archive `JAR` contenant le code compilé ainsi que les dépendances (fichier `...-jar-with-dependencies.jar` dans le répertoire `target/` généré). Cette archive peut être exécutée ainsi en standalone.

La commande de compilation est la suivante : `mvn package`.

# Exécution

Pour exécuter le projet, sous Windows, il suffit de double-cliquer sur l'archive `...-jar-with-dependencies.jar` dans le répertoire `target/` généré par la compilation.

Sous Linux, la commande `java -jar [NOM_ARCHIVE]-jar-with-dependencies.jar` permet son exécution.

Un fichier `launch.sh` contient la commande d'exécution du projet, afin de rendre la commande d'exécution moins verbeuse si besoin est.

# Ressources

* [Tileset de la classe, par NettySvit, license CC0](https://opengameart.org/content/cool-school-tileset)
* [Sprites des personnages, par GrafxKid, pas de license](https://opengameart.org/content/rpg-character-sprites)
* [Sprites des bonbons, par Master484, dans le domaine public](https://opengameart.org/content/super-candy-set-m484-games)

# Sources

* [Sprites modifiés avec Piskel](https://www.piskelapp.com/)
