> J : Matthieu, tu vas m'aider à développer le jeu du loup-garou de Thercelieux. 

> M : OK, fais moi une synthèse des règles du jeu, puis on ira dans le détail.

> J : Alors, voilà le principe du jeu. On a un village, composé de villageois et de loups-garous. 
> Ces derniers se font passer pour des villageois. Quant aux vrais villageois, certains d'entre eux ont un pouvoir. 
> 
> Chaque nuit, les loups-garous se réveillent et dévorent un villageois. 
> 
> Au matin, les villageois, y compris les loups-garous se réveillent et constatent la victime. 
> Alors, ils désignent un coupable et le condamnent. 
> 
> La nuit recommence et le cycle se poursuit jusqu'à ce que l'une des deux équipes gagne : 
> les loups-garous ou les villageois.

> M : Je vois, alors, essayons de définir ce qu'est un village. 
> Si je comprend bien, un village est composé de villageois, mais aussi de loups-garous. 
> Est-ce que les loups-garous sont des villageois ? 

> J : Non, ils se font passer pour des villageois.

> M : Selon le point de vue, ils sont soit villageois, soit loups-garous. 
> Pour éclaircir ça, je te propose de parler d'humains pour les vrais villageois, 
> et de loup-garou pour les faux villageois. 

```scala 3
enum Villageois:
  case Humain()
  case LoupGarou()

case class Village(humains: Set[Humain], loupsGarous: Set[LoupGarou])
```

> J : Mais comment on sait qui est Humain et qui est loup-garou ?

> M : Effectivement, on joue à un jeu, mais il nous manque les participants. Et si on les ajoutait ? 

```scala 3
case class Participant(nom: String)

enum Villageois:
  case Humain(participant: Participant)
  case LoupGarou(participant: Participant)

case class Village(humains: Set[Humain], loupsGarous: Set[LoupGarou])
```

> J : Ca devient plus clair, mais j'ai l'impression qu'on n'a pas encore les mécanismes du jeu.

> M : On va introduire la notion de partie et de victoire. 

```scala 3
case class Participant(nom: String)

enum Villageois:
  case Humain(participant: Participant)
  case LoupGarou(participant: Participant)

case class Village(humains: Set[Humain], loupsGarous: Set[LoupGarou])

enum FinDePartie:
  case VictoireDesLoups
  case VictoireDesHumains

def partie() : FinDePartie = ???
```

> J : Que signifie le ???

> M : Tout simplement que le code n'est pas implémenté. Mais nous allons y remédier. 
> Décris moi comment se déroule une partie.

> J : Tout d'abord, on distribue les rôles aux participants, puis on commence avec la nuit.

```scala 3
case class Participant(nom: String)

enum Villageois:
  case Humain(participant: Participant)
  case LoupGarou(participant: Participant)

case class Village(humains: Set[Humain], loupsGarous: Set[LoupGarou])

enum FinDePartie:
  case VictoireDesLoups
  case VictoireDesHumains

def partie() : FinDePartie =
  distributionDesRôles(
    Participant("bob"),
    Participant("alice"),
    Participant("sacha"),
    Participant("sarah"),
    Participant("karim")
  ) |> nuit

def nuit(village: Village) : FinDePartie = ???
def distributionDesRôles(participants: Participant*): Village = ???
```

> J : Je comprends, mais que signifie ce symbole `|>` ? 

> M : C'est un pipe. Cela signifie qu'on prend la valeur qui est avant le pipe et qu'on la passe en paramètre
> de la fonction nuit avec. C'est un peu comme quand tu fais un pipe en bash `ls | grep .png`

> J : D'accord, mais alors ton jeu va s'arrêter à la première nuit ? Pourquoi nuit retourne une FinDePartie ?!

> M : Nos fonctions doivent retourner des valeurs, du coup, on a deux solutions: soit on retourne `Option[FinDePartie]`
> et on va répéter l'appel à nuit tant que l'option est vide, soit on retourne toujours une `FinDePartie` et on
> s'occupe de ne pas sortir de la fonction tant que la partie n'est pas finie.

> J : Ta deuxième solution me semble plus compliquée, non ?

> M : J'ai omis un détail. Si tu souhaites appeler en boucle `nuit`, tu vas devoir aussi conserver l'état de ton 
> `Village`: qui est vivant, qui est mort, etc. Or, on n'a que des fonctions et de paramètres, donc pour conserver
> un état, il faut que la fonction `nuit` retourne ce nouvel état. On se retrouverait avec

```scala 3

case class Participant(nom: String)

enum Villageois:
  case Humain(participant: Participant)
  case LoupGarou(participant: Participant)

case class Village(humains: Set[Humain], loupsGarous: Set[LoupGarou])

enum FinDePartie:
  case VictoireDesLoups
  case VictoireDesHumains


def partie(): FinDePartie =
  var village = distributionDesRôles(
    Participant("bob"),
    Participant("alice"),
    Participant("sacha"),
    Participant("sarah"),
    Participant("karim")
  )
  var finDePartie: Option[FinDePartie] = None
  while (finDePartie == None) do
    val aprèsLaNuit = nuit(village)
    aprèsLaNuit match
      case f: FinDePartie => finDePartie = Some(f)
      case v: Village => village = v
  finDePartie.get

def nuit(village: Village): FinDePartie | Village = ???
def distributionDesRôles(participants: Participant*): Village = ???
```

> M : Mais ce code nécessite deux variables mutables et pas mal de complexité. C'est sûrement familier pour beaucoup
> de développeur, mais après tout, on est là pour explorer d'autres choses, je te propose qu'on continue avec la
> version fonctionnelle.

> J : Bon, aller, de toute façon on se doutait bien que t'allait nous emmener dans des solutions de snob du code.
> Et on est venu quand même. On enchaine donc sur la fonction `nuit` ?

> M : Effectivement, je te propose d'ignorer la distribution des rôles pour le moment, et de nous concentrer sur la nuit.
> Que se passe-t-il durant cette phase de jeu ?

> J : Et bien, les loups-garous se réveillent et décident de tuer quelqu'un. Puis il y a certains pouvoirs qui
> s'activent la nuit, comme la sorcière. Et après, le jour se lève.

> M : OK. On va essayer d'ignorer les pouvoirs dans un premier temps et d'avancer sur les phases de jeu. Il se passe
> quoi lorsque le jour se lève ?

> J : Lorsque le jour se lève, on annonce aux joueurs qui est mort et on entame la phase de jeu correspondant au jour.

> M : OK, on verra le jour après. Tu as parlé de victime, cela se produit lors de l'attaque des loups-garous ?

> J : Oui, c'est ça. Les loups-garous choisissent un participant. Il est tué. Lorsque le jour se lève, on annonce 
> à ce participant qu'il est éliminé.

> M : que penses-tu de ça ? 

```scala 3
case class Participant(nom: String)

enum Villageois:
  case Humain(participant: Participant)
  case LoupGarou(participant: Participant)

case class Village(humains: Set[Humain], loupsGarous: Set[LoupGarou])

enum FinDePartie:
  case VictoireDesLoups
  case VictoireDesHumains

def partie() : FinDePartie =
  distributionDesRôles(
    Participant("bob"),
    Participant("alice"),
    Participant("sacha"),
    Participant("sarah"),
    Participant("karim")
  ) |> nuit

def nuit(village: Village) : FinDePartie =
  village
    |> loupsGarousAttaquent
    |> jour

def distributionDesRôles(participants: Participant*): Village = ???
def loupsGarousAttaquent(village: Village): Village = ???
def jour(village: Village): FinDePartie = ???
```

> J : Pourquoi tu passes un `Village` et renvoies un `Village` pour `loupsGarousAttaquent` ?

> M : Si j'ai bien compris, on doit tuer un humain, n'est-ce pas ?

> J : Oui, en effet.

> M : Du coup, tuer un humain ça revient à avoir le même village avec un humain de moins dans la liste, non ?

> J : Oui. Mais tu peux bien le retirer du Village qu'on te passe, non ?

> M : Ah, je vois. J'ai oublié de te dire que toutes mes valeurs sont immuables. Tu ne peux pas modifier une valeur.
> Pour cela, tu dois la copier en modifiant la valeur que tu souhaites.

> J : Mais c'est horrible !

> M : Au contraire. Tu sais que le paramètre que tu passes à une fonction ne peut jamais changer dans la fonction.
> Tu n'as pas besoin d'aller voir le code pour savoir ce qui s'y passe. Et la signature de la fonction te
> décrit parfaitement les entrées et les sorties. Le code devient beaucoup plus simple à lire.

> J : Je ne suis pas encore convaincu. Mais continuons.

> M : Si j'ai bien compris, les loups-garous gagnent la partie lorsque tous les humains sont éliminés ?

> J : Oui, on annonce cela lorsque le jour se lève. 

> M : OK, alors, on a une phase de jeu différente au matin si les loups-garous gagnent ?

> J : Tout à fait

> M : Je vais ajouter le lever du jour

```scala 3
case class Participant(nom: String)

enum Villageois:
  case Humain(participant: Participant)
  case LoupGarou(participant: Participant)

case class Village(humains: Set[Humain], loupsGarous: Set[LoupGarou])

enum FinDePartie:
  case VictoireDesLoupsGarous
  case VictoireDesHumains

def partie() : FinDePartie =
  distributionDesRôles(
    Participant("bob"),
    Participant("alice"),
    Participant("sacha"),
    Participant("sarah"),
    Participant("karim")
  ) |> nuit

def nuit(village: Village) : FinDePartie =
  village
    |> loupsGarousAttaquent
    |> leJourSeLève

def leJourSeLève(village: Village) : FinDePartie =
  laPartieEstFinie(village) ou jour

def laPartieEstFinie(village: Village): Village | FinDePartie =
  village match
    case v: Village if v.humains.isEmpty  => FinDePartie.VictoireDesLoupsGarous
    case _ => village

extension (either: Village | FinDePartie)
  def ou(f: Village => FinDePartie): FinDePartie =
    either match
      case fdp: FinDePartie => fdp
      case v: Village     => f(v)

def distributionDesRôles(participants: Participant*): Village = ???
def loupsGarousAttaquent(village: Village): Village = ???
def jour(village: Village): FinDePartie = ???
```

> J : C'est quoi ce `ou` que tu nous as mis ?

> M : J'ai créé une "extension method" pour rendre le code plus facile à lire. Dans ce cas, étant donné une valeur
> qui serait soit un Village soit une FinDePartie, si c'est une fin de partie, je renvoie la valeur, mais si c'est
> un Village, j'appelle la fonction passée en paramètre en lui passant le village.

> J : C'est ça le `|` ? C'est pour dire soit un type soit un autre ?

> M : Oui, c'est une disjonction ou aussi appelé type union. Ça permet de symboliser des branches de code.
> Ici, c'est `laPartieEstFinie` qui décide comme le code branche en fonction de s'il reste des humains dans le village
> ou non.

> J : OK. On a le lever du jour, mais je te propose de continuer sur l'attaque des loups-garous. Lorsqu'ils attaquent, les loups-garous doivent désigner une victime et ils la mettent à mort.


> M : Si je te suis, après l'attaque, on a donc un Humain de moins dans le Village ? Essayons ça :

```scala 3
case class Participant(nom: String)

enum Villageois:
  case Humain(participant: Participant)
  case LoupGarou(participant: Participant)

case class Village(humains: Set[Humain], loupsGarous: Set[LoupGarou]):
  def retirerVillageois(villageois: Villageois): Village = ???

enum FinDePartie:
  case VictoireDesLoupsGarous
  case VictoireDesHumains

def partie() : FinDePartie =
  distributionDesRôles(
    Participant("bob"),
    Participant("alice"),
    Participant("sacha"),
    Participant("sarah"),
    Participant("karim")
  ) |> nuit

def nuit(village: Village) : FinDePartie =
  village
    |> loupsGarousAttaquent
    |> leJourSeLève

def leJourSeLève(village: Village) : FinDePartie =
  laPartieEstFinie(village) ou jour

def laPartieEstFinie(village: Village): Village | FinDePartie =
  village match
    case v: Village if v.humains.isEmpty  => FinDePartie.VictoireDesLoupsGarous
    case _ => village

extension (either: Village | FinDePartie)
  def ou(f: Village => FinDePartie): FinDePartie =
    either match
      case fdp: FinDePartie => fdp
      case v: Village     => f(v)

def distributionDesRôles(participants: Participant*): Village = ???

def loupsGarousAttaquent(village: Village): Village =
  val victime: Humain = ???
  village.retirerVillageois(victime)

def jour(village: Village): FinDePartie = ???
```

> M : à ce stade, je pense qu'il faudrait matérialiser les interactions avec les joueurs, ici le choix de la victime requiert une interaction.

```scala 3
case class Participant(nom: String)

enum Villageois:
  case Humain(participant: Participant)
  case LoupGarou(participant: Participant)

case class Village(humains: Set[Humain], loupsGarous: Set[LoupGarou]):
  def retirerVillageois(villageois: Villageois): Village = ???

enum FinDePartie:
  case VictoireDesLoupsGarous
  case VictoireDesHumains

def partie()(using interaction: Interaction) : FinDePartie =
  interaction.distributionDesRôles(
    Participant("bob"),
    Participant("alice"),
    Participant("sacha"),
    Participant("sarah"),
    Participant("karim")
  ) |> nuit

def nuit(village: Village)(using interaction: Interaction) : FinDePartie =
  village
    |> loupsGarousAttaquent
    |> leJourSeLève

def leJourSeLève(village: Village) : FinDePartie =
  laPartieEstFinie(village) ou jour

def laPartieEstFinie(village: Village): Village | FinDePartie =
  village match
    case v: Village if v.humains.isEmpty  => FinDePartie.VictoireDesLoupsGarous
    case _ => village

extension (either: Village | FinDePartie)
  def ou(f: Village => FinDePartie): FinDePartie =
    either match
      case fdp: FinDePartie => fdp
      case v: Village     => f(v)


trait Interaction:
  def choixVictime(village: Village): Humain
  def distributionDesRôles(participants: Participant*): Village

def loupsGarousAttaquent(village: Village)(using interaction: Interaction): Village =
  import village.*
  val victime: Humain = interaction.choixVictime(village)
  village.retirerVillageois(victime)

def jour(village: Village): FinDePartie = ???
```

> M : Laissons l'implémentation de ce trait pour plus tard, la signature des méthodes suffit. 

> J : Pourquoi on implémenterait les autres méthodes mais pas celles-ci ?

> M : Elles ne changent rien à la modélisation, elles sont plutôt liées à l'IHM, ce n'est pas notre préoccupation ici

> J : OK. Alors, les loups-garous peuvent agir la nuit. On vérifie s'il reste des humains, puis c'est le jour.
> Durant cette phase, ce sont les villageois qui agissent, donc les humains et les loups-garous qui se font passer pour des villageois.
> Une journée se décompose en plusieurs phases.
> On commence par annoncer aux joueurs qui est mort durant la nuit. 
> Ensuite, on élit un maire s'il n'y en a pas. Puis les joueurs votent pour éliminer
> une personne. Et enfin, on vérifie si la partie est terminée.
> Si ce n'est pas le cas, la nuit tombe.

> M : Oulà, tu me demandes beaucoup de choses. Je te propose qu'on découpe et qu'on y aille étape par étape.
> Dans un premier temps, on va simplement passer au jour, puis de nouveau à la nuit. 
> On intégrera le reste au fur et à mesure.
 
```scala 3
case class Participant(nom: String)

enum Villageois:
  case Humain(participant: Participant)
  case LoupGarou(participant: Participant)

case class Village(humains: Set[Humain], loupsGarous: Set[LoupGarou]):
  def retirerVillageois(villageois: Villageois): Village = ???

enum FinDePartie:
  case VictoireDesLoupsGarous
  case VictoireDesHumains

def partie()(using interaction: Interaction) : FinDePartie =
  interaction.distributionDesRôles(
    Participant("bob"),
    Participant("alice"),
    Participant("sacha"),
    Participant("sarah"),
    Participant("karim")
  ) |> nuit

def nuit(village: Village)(using interaction: Interaction) : FinDePartie =
  village
    |> loupsGarousAttaquent
    |> leJourSeLève

def leJourSeLève(village: Village)(using interaction: Interaction) : FinDePartie =
  laPartieEstFinie(village) ou jour

def laPartieEstFinie(village: Village): Village | FinDePartie =
  village match
    case v: Village if v.humains.isEmpty  => FinDePartie.VictoireDesLoupsGarous
    case _ => village

extension (either: Village | FinDePartie)
  def ou(f: Village => FinDePartie): FinDePartie =
    either match
      case fdp: FinDePartie => fdp
      case v: Village     => f(v)


trait Interaction:
  def choixVictime(village: Village): Humain
  def distributionDesRôles(participants: Participant*): Village

def loupsGarousAttaquent(village: Village)(using interaction: Interaction): Village =
  import village.*
  val victime: Humain = interaction.choixVictime(village)
  village.retirerVillageois(victime)

def déroulementDuJour(village: Village): Village = ???

def jour(village: Village)(using interaction: Interaction): FinDePartie =
  village
    |> déroulementDuJour
    |> laPartieEstFinie ou nuit
```

> M : Est-ce que ça te semble être un bon cadre avant de rentrer dans les détails ?

> J : Oui, ça me semble un bon point de départ. Je te propose d'annoncer aux joueurs qui est mort durant la nuit. 

> M : Pour faire ça, il faudrait qu'on passe l'information de la phase nuit à la phase jour.

```scala 3
case class Participant(nom: String)

enum Villageois:
  case Humain(participant: Participant)
  case LoupGarou(participant: Participant)

case class Village(humains: Set[Humain], loupsGarous: Set[LoupGarou]):
  def retirerVillageois(villageois: Villageois): Village = ???

enum FinDePartie:
  case VictoireDesLoupsGarous
  case VictoireDesHumains

def partie()(using interaction: Interaction) : FinDePartie =
  interaction.distributionDesRôles(
    Participant("bob"),
    Participant("alice"),
    Participant("sacha"),
    Participant("sarah"),
    Participant("karim")
  ) |> nuit

def nuit(village: Village)(using interaction: Interaction): FinDePartie =
  village
    |> loupsGarousAttaquent
    |> leJourSeLève

def leJourSeLève(village: Village, victime: Victime)(using interaction: Interaction): FinDePartie =
  laPartieEstFinie(village) ou jour(victime)

def laPartieEstFinie(village: Village): Village | FinDePartie =
  village match
    case v: Village if v.humains.isEmpty  => FinDePartie.VictoireDesLoupsGarous
    case _ => village

extension (either: Village | FinDePartie)
  def ou(f: Village => FinDePartie): FinDePartie =
    either match
      case fdp: FinDePartie => fdp
      case v: Village     => f(v)


trait Interaction:
  def choixVictime(village: Village): Humain
  def distributionDesRôles(participants: Participant*): Village


case class Victime(humain: Humain)

def loupsGarousAttaquent(village: Village)(using interaction: Interaction): (Village, Victime) =
  import village.*
  val victime: Humain = interaction.choixVictime(village)
  val villageÀJour = village.retirerVillageois(victime)
  (villageÀJour, Victime(victime))

def déroulementDuJour(village: Village): Village = ???

def annoncerLaMortDeLaVictime(victime: Victime): Unit = ???

def jour(victime: Victime)(village: Village)(using interaction: Interaction): FinDePartie =
  annoncerLaMortDeLaVictime(victime)
  village
    |> déroulementDuJour
    |> laPartieEstFinie ou nuit
```

> J: La syntaxe de la fonction jour est étrange. On dirait qu'il y a deux fois des paramètres. Tu peux m'expliquer ?

> M: On avait défini la fonction `ou` comme prenant une fonction qui va de `Village` vers `FinDePartie`. 
> Sauf que maintenant, on doit passer deux paramètres à jour, on doit passer la victime et le village.
> Du coup, pour changer le minimum de choses, je peux dire que ma fonction prend deux ensembles de paramètres.
> Ainsi, une fois que j'ai passé victime, j'ai bien une fonction qui prend un `Village`.

> J: OK.
> 
> J: Maintenant, on va pouvoir élire le maire. 

```scala 3
case class Participant(nom: String)

enum Villageois:
  case Humain(participant: Participant)
  case LoupGarou(participant: Participant)

case class Village(humains: Set[Humain], loupsGarous: Set[LoupGarou], maire: Option[Villageois] = None):
  def retirerVillageois(villageois: Villageois): Village = ???
  def setMaire(maire: Villageois): Village = ???

enum FinDePartie:
  case VictoireDesLoupsGarous
  case VictoireDesHumains

def partie()(using interaction: Interaction) : FinDePartie =
  interaction.distributionDesRôles(
    Participant("bob"),
    Participant("alice"),
    Participant("sacha"),
    Participant("sarah"),
    Participant("karim")
  ) |> nuit

def nuit(village: Village)(using interaction: Interaction): FinDePartie =
  village
    |> loupsGarousAttaquent
    |> leJourSeLève

def leJourSeLève(village: Village, victime: Victime)(using interaction: Interaction): FinDePartie =
  laPartieEstFinie(village) ou jour(victime)

def laPartieEstFinie(village: Village): Village | FinDePartie =
  village match
    case v: Village if v.humains.isEmpty  => FinDePartie.VictoireDesLoupsGarous
    case _ => village

extension (either: Village | FinDePartie)
  def ou(f: Village => FinDePartie): FinDePartie =
    either match
      case fdp: FinDePartie => fdp
      case v: Village     => f(v)


trait Interaction:
  def lesLoupsGarousChoisissentUneVictime(village: Village): Humain
  def distributionDesRôles(participants: Participant*): Village
  def annoncerLaMortDeLaVictime(victime: Victime): Unit
  def electionMaire(village: Village): Villageois

case class Victime(humain: Humain)

def loupsGarousAttaquent(village: Village)(using interaction: Interaction): (Village, Victime) =
  import village.*
  val victime: Humain = interaction.lesLoupsGarousChoisissentUneVictime(village)
  val villageÀJour = village.retirerVillageois(victime)
  (villageÀJour, Victime(victime))

def déroulementDuJour(village: Village)(using interaction: Interaction): Village =
  val maire = interaction.electionMaire(village)
  village.setMaire(maire)

def jour(victime: Victime)(village: Village)(using interaction: Interaction): FinDePartie =
  interaction.annoncerLaMortDeLaVictime(victime)
  village
    |> déroulementDuJour
    |> laPartieEstFinie ou nuit
```

> M: Comme tu vois, comme on commençait à avoir pas mal de fonctions qui correspondent à des interactions avec les joueurs,
> j'ai créé une interface (`trait` en scala) pour regrouper ces fonctions.
> Si on allait au bout du développement, avec une interface homme-machine, il faudrait implémenter des interactions
> avec les utilisateurs du jeu pour chaque fonction de cette interface.
 
> J: Super, comme ça, je pourrai sortir mon jeu en version web et en version mobile.

> J: Par contre, je me rends compte qu'on élit le maire tous les jours. Lorsqu'il y a déjà un maire, il ne faut pas en élire un nouveau.

```scala 3
case class Participant(nom: String)

enum Villageois:
  case Humain(participant: Participant)
  case LoupGarou(participant: Participant)

case class Village(humains: Set[Humain], loupsGarous: Set[LoupGarou], maire: Option[Villageois] = None):
  def retirerVillageois(villageois: Villageois): Village = ???
  def setMaire(maire: Villageois): Village = ???

enum FinDePartie:
  case VictoireDesLoupsGarous
  case VictoireDesHumains

def partie()(using interaction: Interaction) : FinDePartie =
  interaction.distributionDesRôles(
    Participant("bob"),
    Participant("alice"),
    Participant("sacha"),
    Participant("sarah"),
    Participant("karim")
  ) |> nuit

def nuit(village: Village)(using interaction: Interaction): FinDePartie =
  village
    |> loupsGarousAttaquent
    |> leJourSeLève

def leJourSeLève(village: Village, victime: Victime)(using interaction: Interaction): FinDePartie =
  laPartieEstFinie(village) ou jour(victime)

def laPartieEstFinie(village: Village): Village | FinDePartie =
  village match
    case v: Village if v.humains.isEmpty  => FinDePartie.VictoireDesLoupsGarous
    case _ => village

extension (either: Village | FinDePartie)
  def ou(f: Village => FinDePartie): FinDePartie =
    either match
      case fdp: FinDePartie => fdp
      case v: Village     => f(v)


trait Interaction:
  def lesLoupsGarousChoisissentUneVictime(village: Village): Humain
  def distributionDesRôles(participants: Participant*): Village
  def annoncerLaMortDeLaVictime(victime: Victime): Unit
  def electionMaire(village: Village): Villageois

case class Victime(humain: Humain)

def loupsGarousAttaquent(village: Village)(using interaction: Interaction): (Village, Victime) =
  import village.*
  val victime: Humain = interaction.lesLoupsGarousChoisissentUneVictime(village)
  val villageÀJour = village.retirerVillageois(victime)
  (villageÀJour, Victime(victime))

def déroulementDuJour(village: Village)(using interaction: Interaction): Village =
  val maire = village.maire.getOrElse(interaction.electionMaire(village))
  village.setMaire(maire)

def jour(victime: Victime)(village: Village)(using interaction: Interaction): FinDePartie =
  interaction.annoncerLaMortDeLaVictime(victime)
  village
    |> déroulementDuJour
    |> laPartieEstFinie ou nuit
```
> M: j'ai changé le code pour ne faire l'élection que lorsque la propriété `maire` du Village est vide.
> (et oui, on copie le village même quand ça ne sert à rien)

> J: Bien, maintenant, on va partir à la chasse aux loups-garous, les villageois doivent voter pour désigner leur prochaine victime. Tout le monde vote, y compris les loups-garous.

```scala 3
case class Participant(nom: String)

enum Villageois:
  case Humain(participant: Participant)
  case LoupGarou(participant: Participant)

case class Village(humains: Set[Humain], loupsGarous: Set[LoupGarou], maire: Option[Villageois] = None):
  def retirerVillageois(villageois: Villageois): Village = ???
  def setMaire(maire: Villageois): Village = ???

enum FinDePartie:
  case VictoireDesLoupsGarous
  case VictoireDesHumains

def partie()(using interaction: Interaction) : FinDePartie =
  interaction.distributionDesRôles(
    Participant("bob"),
    Participant("alice"),
    Participant("sacha"),
    Participant("sarah"),
    Participant("karim")
  ) |> nuit

def nuit(village: Village)(using interaction: Interaction): FinDePartie =
  village
    |> loupsGarousAttaquent
    |> leJourSeLève

def leJourSeLève(village: Village, victime: Victime)(using interaction: Interaction): FinDePartie =
  laPartieEstFinie(village) ou jour(victime)

def laPartieEstFinie(village: Village): Village | FinDePartie =
  village match
    case v: Village if v.humains.isEmpty  => FinDePartie.VictoireDesLoupsGarous
    case _ => village

extension (either: Village | FinDePartie)
  def ou(f: Village => FinDePartie): FinDePartie =
    either match
      case fdp: FinDePartie => fdp
      case v: Village     => f(v)


trait Interaction:
  def lesLoupsGarousChoisissentUneVictime(village: Village): Humain
  def lesVillageoisChoisissentUneVictime(village: Village): Villageois
  def distributionDesRôles(participants: Participant*): Village
  def annoncerLaMortDeLaVictime(victime: Victime): Unit
  def electionMaire(village: Village): Villageois

case class Victime(humain: Humain)

def loupsGarousAttaquent(village: Village)(using interaction: Interaction): (Village, Victime) =
  import village.*
  val victime: Humain = interaction.lesLoupsGarousChoisissentUneVictime(village)
  val villageÀJour = village.retirerVillageois(victime)
  (villageÀJour, Victime(victime))

def déroulementDuJour(village: Village)(using interaction: Interaction): Village =
  val maire = village.maire.getOrElse(interaction.electionMaire(village))
  val villageAvecUnMaire = village.setMaire(maire)
  val victime = interaction.lesVillageoisChoisissentUneVictime(villageAvecUnMaire)
  villageAvecUnMaire.retirerVillageois(victime)


def jour(victime: Victime)(village: Village)(using interaction: Interaction): FinDePartie =
  interaction.annoncerLaMortDeLaVictime(victime)
  village
    |> déroulementDuJour
    |> laPartieEstFinie ou nuit
```

> J: Maintenant qu'on a désigné la victime, il faut vérifier si la partie est terminée. 
> On a peut-être tué le dernier humain ou le dernier loup-garou.
 
 
```scala 3
case class Participant(nom: String)

enum Villageois:
  case Humain(participant: Participant)
  case LoupGarou(participant: Participant)

case class Village(humains: Set[Humain], loupsGarous: Set[LoupGarou], maire: Option[Villageois] = None):
  def retirerVillageois(villageois: Villageois): Village = ???
  def setMaire(maire: Villageois): Village = ???

enum FinDePartie:
  case VictoireDesLoupsGarous
  case VictoireDesHumains

def partie()(using interaction: Interaction) : FinDePartie =
  interaction.distributionDesRôles(
    Participant("bob"),
    Participant("alice"),
    Participant("sacha"),
    Participant("sarah"),
    Participant("karim")
  ) |> nuit

def nuit(village: Village)(using interaction: Interaction): FinDePartie =
  village
    |> loupsGarousAttaquent
    |> leJourSeLève

def leJourSeLève(village: Village, victime: Victime)(using interaction: Interaction): FinDePartie =
  laPartieEstFinie(village) ou jour(victime)

def laPartieEstFinie(village: Village): Village | FinDePartie =
  village match
    case v: Village if v.humains.isEmpty => FinDePartie.VictoireDesLoupsGarous
    case v: Village if v.loupsGarous.isEmpty => FinDePartie.VictoireDesHumains
    case _ => village

extension (either: Village | FinDePartie)
  def ou(f: Village => FinDePartie): FinDePartie =
    either match
      case fdp: FinDePartie => fdp
      case v: Village     => f(v)


trait Interaction:
  def lesLoupsGarousChoisissentUneVictime(village: Village): Humain
  def lesVillageoisChoisissentUneVictime(village: Village): Villageois
  def distributionDesRôles(participants: Participant*): Village
  def annoncerLaMortDeLaVictime(victime: Victime): Unit
  def electionMaire(village: Village): Villageois

case class Victime(humain: Humain)

def loupsGarousAttaquent(village: Village)(using interaction: Interaction): (Village, Victime) =
  import village.*
  val victime: Humain = interaction.lesLoupsGarousChoisissentUneVictime(village)
  val villageÀJour = village.retirerVillageois(victime)
  (villageÀJour, Victime(victime))

def déroulementDuJour(village: Village)(using interaction: Interaction): Village =
  val maire = village.maire.getOrElse(interaction.electionMaire(village))
  val villageAvecUnMaire = village.setMaire(maire)
  val victime = interaction.lesVillageoisChoisissentUneVictime(villageAvecUnMaire)
  villageAvecUnMaire.retirerVillageois(victime)


def jour(victime: Victime)(village: Village)(using interaction: Interaction): FinDePartie =
  interaction.annoncerLaMortDeLaVictime(victime)
  village
    |> déroulementDuJour
    |> laPartieEstFinie ou nuit
```