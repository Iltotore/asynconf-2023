# Green Bank - Asynconf 2023

Soumission de Raphaël FROMENTIN (Il_totore) au hackaton de l'Asynconf 2023.

## Lancer le projet

Pour lancer le projet, il faut le compiler puis lancer un serveur web à sa racine (par exemple avec `php -S localhost:8080`)

### Compiler le projet

Sur Windows:

```bash
./millw.bat fastLinkJS
```

Sur UNIX (Linux/Mac OS)

```sh
./millw fastLinkJS
```

## Architecture du projet

Le projet est écrit en [Scala](https://scala-lang.org/) en utilisant les technologies [Scala JS](https://scala-js.org), un compilateur Scala vers JavaScript, et [Tyrian](https://tyrian.indigoengine.io/), une bibliothèque de front-end web basée sur Scala JS. Tyrian utilise une architecture similaire à celle du langage Elm (TEA), très fonctionnelle. Le but est de mettre à jour un modèle immuable en renvoyant à chaque évènement un nouvel état.