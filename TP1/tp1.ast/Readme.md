# Analyseur de Code Source Orienté Objet

Cet outil Java utilisant Maven vous permet d'analyser le code source d'une application orientée objet passée en tant que paramètre et de calculer les informations suivantes :

1. **Nombre de classes de l'application.**
2. **Nombre de lignes de code de l'application.**
3. **Nombre total de méthodes de l'application.**
4. **Nombre total de packages de l'application.**
5. **Nombre moyen de méthodes par classe.**
6. **Nombre moyen de lignes de code par méthode.**
7. **Nombre moyen d'attributs par classe.**
8. **Les 10 % des classes qui possèdent le plus grand nombre de méthodes.**
9. **Les 10 % des classes qui possèdent le plus grand nombre d'attributs.**
10. **Les classes qui font partie en même temps des deux catégories précédentes.**
11. **Les classes qui possèdent plus de X méthodes (la valeur de X est donnée).**
12. **Les 10 % des méthodes qui possèdent le plus grand nombre de lignes de code (par classe).**
13. **Le nombre maximal de paramètres par rapport à toutes les méthodes de l'application.**

## Interface Graphique

Nous avons également ajouté une interface graphique conviviale à cet outil, qui permet à l'utilisateur de :

- Rechercher et sélectionner facilement un projet Java à analyser.
- Effectuer une analyse complète du code source de l'application.
- Créer et visualiser le graphe d'appel de l'application orientée objet.

## Utilisation

Pour utiliser l'interface graphique, suivez ces étapes :

1. Clonez le référentiel vers votre machine locale.

2. Compilez le projet en utilisant Maven :
  mvn clean install
3. Exécutez l'application en lançant l'interface graphique :
4. Utilisez l'interface pour charger votre projet Java, effectuer une analyse et créer le graphe d'appel.

## Exigences

- Java JDK 8 ou supérieur
- Apache Maven

## Contributeurs

- Sandratra MBELO NDRIAMANAMPY
- Yoann REYNE

## Lien vers le Référentiel GitHub

Vous pouvez accéder au code source de ce projet sur GitHub : [Analyseur de Code Source Orienté Objet](https://github.com/Daipivo/evolution_logiciels/tree/main/TP1/tp1.ast).
