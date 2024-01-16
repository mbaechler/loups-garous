> Jonathan : Matthieu, tu vas m'aider à développer le jeu du loup garou de Thercelieux. 

> Matthieu : OK, fais moi une synthèse des règles du jeu, puis on ira dans le détail.

> Jonathan : Alors, voilà le principe du jeu. On a un village, composé de villageois et de loups garou. Ces derniers se font passer pour des villageois. Quant aux vrais villageois, certains d'entre eux ont un pouvoir. 
> 
> Jonathan : Chaque nuit, les loups garous se réveillent et dévorent un villageois. Au matin, les villageois, y compris les loups garous se réveillent et constatent la victime. Alors, ils désignent un coupable et le condamne. La nuit recommence et le cycle se poursuit jusqu'à ce que l'une des deux équipes gagne : les loups garous ou les villageois.
>
>  M : Je vois, alors, voyons voir ce qu'est un village. Si je comprend bien, un village est composé de villageois, mais aussi de loup garou. Est-ce que les loups garous sont des villageois ? 

> J : Non, ils se font passer pour des villageois.

> M : Selon le point de vue, ils sont soit villageois, soit loup-garous. Pour éclaircir ça, je te propose de parler d'humains pour les vrais villageois, et de loup-garou pour les faux villageois. 

```scala
object types: 
	enum Villageois
		case Humain()
		case LoupGarou()
		
	enum Village
		case Menacé(humains: Set[Humain], loupsGarous: Set[LoupGarou])  
		case Décimé(loupGarou: Set[LoupGarou])  
		case Tranquille(humains: Set[Humain])
```

> M : On constate que le village peut avoir 3 états différents, selon la présence d'humains ou de loups-garous.

> J : Mais comment on sait qui est Humain et qui est Loup Garou ?

> M : Effectivement, on joue à un jeu, mais il nous manque les participants. Et si on les ajoutait ? 

```scala
object types: 
	case class Participant(nom: String)

	enum Villageois
		case Humain(participant: Participant)
		case LoupGarou(participant: Participant)
		
	enum Village
		case Menacé(humains: Set[Humain], loupsGarous: Set[LoupGarou])  
		case Décimé(loupGarou: Set[LoupGarou])  
		case Tranquille(humains: Set[Humain])
```

> J : Ca devient plus clair, mais j'ai l'impression qu'on n'a pas encore les mécanismes du jeu.

> M : On va introduire la notion de partie et de victoire. 

```scala
object types: 
	case class Participant(nom: String)

	enum Villageois
		case Humain(participant: Participant)
		case LoupGarou(participant: Participant)
		
	enum Village
		case Menacé(humains: Set[Humain], loupsGarous: Set[LoupGarou])  
		case Décimé(loupGarou: Set[LoupGarou])  
		case Tranquille(humains: Set[Humain])

	enum FinDePartie:  
	  case VictoireDesLoups  
	  case VictoireDesHumains

object jeu:
	def partie() : FinDePartie = ???
```

> J : Que signifie le ???

> M : Tout simplement que le code n'est pas implémenté. Mais nous allons y remédier. Décris moi comment se déroule une partie.

> J : Tout d'abord, on distribue les rôles aux participants, puis on commence avec la nuit.

```scala
object types: 
	case class Participant(nom: String)

	enum Villageois
		case Humain(participant: Participant)
		case LoupGarou(participant: Participant)
		
	enum Village
		case Menacé(humains: Set[Humain], loupsGarous: Set[LoupGarou])  
		case Décimé(loupGarou: Set[LoupGarou])  
		case Tranquille(humains: Set[Humain])

	enum FinDePartie:  
	  case VictoireDesLoups  
	  case VictoireDesHumains

object syntax:  
  extension [A](a: A) def |>[B](f: A => B): B = f(a)  

object jeu:
	def partie() : FinDePartie = 
		distributionDesRôles(  
		  Participant("bob"),  
		  Participant("alice"),  
		  Participant("sacha"),  
		  Participant("sarah"),  
		  Participant("karim")  
		) |> nuit

	def nuit(village: Village.Menacé) : FinDePartie = ???
	def distributionDesRôles(participants: Participant*): Village.Menacé = ???
```

> J : Je comprend, mais que signifie ce symbole ? 

> M : C'est un pipe. Cela signifie qu'on enchaîne la fonction nuit avec la sortie de la fonction distributionDesRôles

> J : Et pourquoi on utilise le type Village.Menacé ? 

> M : Et bien, le jeu ne peut se dérouler qu'en présence d'humains et de loups-garous. S'il n'y a pas les deux, on est soit dans un village tranquille, soit dans un village décimé, et à ce stade, la partie est terminée.

> J : OK, c'est vrai que je ne me pose pas ces questions quand je joue, mais ça tombe sous le sens. Alors, je vois qu'on a encore des choses non implémentées.

> M : Effectivement, je te propose d'ignorer la distribution des rôles pour le moment, et de nous concentrer sur la nuit. Que se passe-t-il durant cette phase de jeu ?

> J : Et bien, les loups-garous se réveillent et décident de tuer quelqu'un. Puis il y a certains pouvoirs qui s'activent la nuit, comme la sorcière. Et après, le jour se lève.

> M : OK. On va essayer d'ignorer les pouvoirs dans un premier temps et d'avancer sur les phases de jeu, ça donnerait ça.

```scala
object types: 
	case class Participant(nom: String)

	enum Villageois
		case Humain(participant: Participant)
		case LoupGarou(participant: Participant)
		
	enum Village
		case Menacé(humains: Set[Humain], loupsGarous: Set[LoupGarou])  
		case Décimé(loupGarou: Set[LoupGarou])  
		case Tranquille(humains: Set[Humain])

	enum FinDePartie:  
	  case VictoireDesLoups  
	  case VictoireDesHumains

object jeu:
	def partie() : FinDePartie = 
		distributionDesRôles(  
		  Participant("bob"),  
		  Participant("alice"),  
		  Participant("sacha"),  
		  Participant("sarah"),  
		  Participant("karim")  
		) |> nuit

	def nuit(village: Village.Menacé) : FinDePartie = 
		loupsGarousAttaquent()
		jour()
		
	def distributionDesRôles(participants: Participant*): Village.Menacé = ???
	def loupsGarousAttaquent() = ???
	def jour() = FinDePartie = ???
```

> M : Et ensuite

> J : Lorsque le jour se lève, on annonce aux joueurs qui a été la victime et on entame la phase de jeu correspondant au jour.

> M : OK, on verra le jour après. Tu as parlé de victime, cela se produit lors de l'attaque des loups-garous ?

> J : Oui, c'est ça. Les loups garous choisissent un participant. Il est tué. Lorsque le jour se lève, on annonce à ce participant qu'il est éliminé.

> M : Alors, ça pourrait donner ça.

```scala
object types: 
	case class Participant(nom: String)

	enum Villageois
		case Humain(participant: Participant)
		case LoupGarou(participant: Participant)
		
	enum Village
		case Menacé(humains: Set[Humain], loupsGarous: Set[LoupGarou])  
		case Décimé(loupGarou: Set[LoupGarou])  
		case Tranquille(humains: Set[Humain])

	enum FinDePartie:  
	  case VictoireDesLoups  
	  case VictoireDesHumains

	case class Victime(humain: Humain)

object syntax:  
  extension [A](a: A) def |>[B](f: A => B): B = f(a)  
  
  def tireAuHasard[T](choses: Set[T]): T =  
    Random.shuffle(choses).head

object jeu:
	def partie() : FinDePartie = 
		distributionDesRôles(  
		  Participant("bob"),  
		  Participant("alice"),  
		  Participant("sacha"),  
		  Participant("sarah"),  
		  Participant("karim")  
		) |> nuit

	def nuit(village: Village.Menacé) : FinDePartie = 
		val victime = loupsGarousAttaquent(village)
		jour()
		
	def distributionDesRôles(participants: Participant*): Village.Menacé = ???
	def loupsGarousAttaquent(village: Village.Menacé): Victime =  
		Victime(tireAuHasard(village.humains))
	def jour() = FinDePartie = ???
```

> M : Et on continue avec le jour qui se lève : 

```scala
object types: 
	case class Participant(nom: String)

	enum Villageois
		case Humain(participant: Participant)
		case LoupGarou(participant: Participant)
		
	enum Village
		case Menacé(humains: Set[Humain], loupsGarous: Set[LoupGarou])  
		case Décimé(loupGarou: Set[LoupGarou])  
		case Tranquille(humains: Set[Humain])

	enum FinDePartie:  
	  case VictoireDesLoups  
	  case VictoireDesHumains

	case class Victime(humain: Humain)

object syntax:  
  extension [A](a: A) def |>[B](f: A => B): B = f(a)  
  
  def tireAuHasard[T](choses: Set[T]): T =  
    Random.shuffle(choses).head

object jeu:
	def partie() : FinDePartie = 
		distributionDesRôles(  
		  Participant("bob"),  
		  Participant("alice"),  
		  Participant("sacha"),  
		  Participant("sarah"),  
		  Participant("karim")  
		) |> nuit

	def nuit(village: Village.Menacé) : FinDePartie = 
		val victime = loupsGarousAttaquent(village)
		val villageAuMatin = leJourSeLève(village, victime)
		jour()
		
	def distributionDesRôles(participants: Participant*): Village.Menacé = ???
	def loupsGarousAttaquent(village: Village.Menacé): Victime =  
		Victime(tireAuHasard(village.humains))
	def jour() = FinDePartie = ???
	def leJourSeLève(village: Village.Menacé, victime: Victime) : Village = ???
```

> M : Si j'ai bien compris, les loups-garous gagnent la partie lorsque tous les humains sont éliminés ?

> J : Oui, on annonce cela lorsque le jour se lève. 

> M : OK, alors, on a une phase de jeu différente au matin si les loups-garous gagnent ?

> J : Tout à fait

```scala
object types: 
	case class Participant(nom: String)

	enum Villageois
		case Humain(participant: Participant)
		case LoupGarou(participant: Participant)
		
	enum Village
		case Menacé(humains: Set[Humain], loupsGarous: Set[LoupGarou])  
		case Décimé(loupGarou: Set[LoupGarou])  
		case Tranquille(humains: Set[Humain])

	enum FinDePartie:  
	  case VictoireDesLoups  
	  case VictoireDesHumains

	case class Victime(humain: Humain)

object syntax:  
  extension [A](a: A) def |>[B](f: A => B): B = f(a)  
  
  def tireAuHasard[T](choses: Set[T]): T =  
    Random.shuffle(choses).head

object jeu:
	def partie() : FinDePartie = 
		distributionDesRôles(  
		  Participant("bob"),  
		  Participant("alice"),  
		  Participant("sacha"),  
		  Participant("sarah"),  
		  Participant("karim")  
		) |> nuit

	def nuit(village: Village.Menacé) : FinDePartie = 
		val victime = loupsGarousAttaquent(village)
		val villageAuMatin = leJourSeLève(village, victime)
		laPartieEstFinie(villageAuMatin) ou jour
		
	def distributionDesRôles(participants: Participant*): Village.Menacé = ???
	def loupsGarousAttaquent(village: Village.Menacé): Victime =  
		Victime(tireAuHasard(village.humains))
	def jour() = FinDePartie = ???
	def leJourSeLève(village: Village.Menacé, victime: Victime) : Village = ???

	def laPartieEstFinie(village: Village): Either[Village.Menacé, FinDePartie] =  
	  village match  
	    case v: Village.Menacé     => Left(v)  
	    case _: Village.Décimé     => Right(VictoireDesLoups)  
	    case _: Village.Tranquille => Right(VictoireDesHumains)

	extension (either: Either[Village.Menacé, FinDePartie])  
	  def ou(f: Village.Menacé => FinDePartie): FinDePartie =  
	    either match  
	      case Left(village)      => f(village)  
	      case Right(finDePartie) => finDePartie	
```
