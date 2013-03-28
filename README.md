# Le jeu de la vie

## Principe
Dans une grille est individus sont placés aléatoirement dans les cases, si une case à au moins 3 individu autour alors elle devient un individu, si elle à 2 individu autour elle reste en vie sinon elle meure ou reste morte.

## Représentation
Pour représenter ce problème nous utiliserons une matrice de taille n+2, avec n la taille d'un coté de la grille. La matrice sera carré de type entier et contiendra une "bordure" de '0' (d'ou le n+2), de ce fait on pourra parcourir le "centre" de la matrice qui représentera les données sans se soucié si on est au bord ou non.

###Exemple grille 4x4
    0 0 0 0 0 0
    0 Données 0
    0   De    0
    0   La    0
    0 Matrice 0
    0 0 0 0 0 0

## Méthode

### Methode 100% centralisée
Dans cette méthode on utilisera un seul thread, le thread principal qui va faire de manière iterative le calcul de chaque nouvelle case.

### Méthode 100% partagé
Dans cette méthode on utilise un thread par case. On doit donc utlisé un système de synchronisation (voir par rendez-vous).

### Méthode combiné

