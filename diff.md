```diff initial
+ enum Villageois:
+   case Humain()
+   case LoupGarou()
+ 
+ case class Village(humains: Set[Humain], loupsGarous: Set[LoupGarou])
```
 
```diff 1->2
+ case class Participant(nom: String)
 enum Villageois:
-   case Humain()
-   case LoupGarou()
+   case Humain(participant: Participant)
+   case LoupGarou(participant: Participant)
 
- case class Village(humains: Set[Humain], loupsGarous: Set[LoupGarou])
+ case class Village(humains: Set[Humain], loupsGarous: Set[LoupGarou])
```
 
```diff 2->3
 case class Participant(nom: String)
 
 enum Villageois:
   case Humain(participant: Participant)
   case LoupGarou(participant: Participant)
 
 case class Village(humains: Set[Humain], loupsGarous: Set[LoupGarou])
 
+ enum FinDePartie:
+   case VictoireDesLoups
+   case VictoireDesHumains
+ 
+ def partie() : FinDePartie = ???
```
 
```diff 3->4
 case class Participant(nom: String)
 
 enum Villageois:
   case Humain(participant: Participant)
   case LoupGarou(participant: Participant)
 
 case class Village(humains: Set[Humain], loupsGarous: Set[LoupGarou])
 
 enum FinDePartie:
   case VictoireDesLoups
   case VictoireDesHumains
 
- def partie() : FinDePartie = ???
+ def partie() : FinDePartie =
+   distributionDesRôles(
+     Participant("bob"),
+     Participant("alice"),
+     Participant("sacha"),
+     Participant("sarah"),
+     Participant("karim")
+   ) |> nuit
+ 
+ def nuit(village: Village) : FinDePartie = ???
+ def distributionDesRôles(participants: Participant*): Village = ???
```
 
```diff 4->5
 case class Participant(nom: String)
 
 enum Villageois:
   case Humain(participant: Participant)
   case LoupGarou(participant: Participant)
 
 case class Village(humains: Set[Humain], loupsGarous: Set[LoupGarou])
 
 enum FinDePartie:
   case VictoireDesLoups
   case VictoireDesHumains
 
 def partie(): FinDePartie =
-   distributionDesRôles(
+   var village = distributionDesRôles(
     Participant("bob"),
     Participant("alice"),
     Participant("sacha"),
     Participant("sarah"),
     Participant("karim")
+   )
+   var finDePartie: Option[FinDePartie] = None
+   while (finDePartie == None) do
+     val aprèsLaNuit = nuit(village)
+     aprèsLaNuit match
+       case f: FinDePartie => finDePartie = Some(f)
+       case v: Village => village = v
+   finDePartie.get
-   ) |> nuit
 
- def nuit(village: Village): FinDePartie = ???
+ def nuit(village: Village): FinDePartie | Village = ???
 def distributionDesRôles(participants: Participant*): Village = ???
 
```
 
```diff 4->6
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
 
- def nuit(village: Village) : FinDePartie = ???
+ def nuit(village: Village) : FinDePartie =
+   village
+     |> loupsGarousAttaquent
+     |> jour
 
 def distributionDesRôles(participants: Participant*): Village = ???
+ def loupsGarousAttaquent(village: Village): Village = ???
+ def jour(village: Village): FinDePartie = ???
 
```
 
```diff 6->7
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
+     |> leJourSeLève
+ 
+ def leJourSeLève(village: Village) : FinDePartie =
+   laPartieEstFinie(village) ou jour
+ 
+ def laPartieEstFinie(village: Village): Village | FinDePartie =
+   village match
+     case v: Village if v.humains.isEmpty  => FinDePartie.VictoireDesLoupsGarous
+     case _ => village
+ 
+ extension (either: Village | FinDePartie)
+   def ou(f: Village => FinDePartie): FinDePartie =
+     either match
+       case fdp: FinDePartie => fdp
+       case v: Village     => f(v)
-     |> jour
 
 def distributionDesRôles(participants: Participant*): Village = ???
 def loupsGarousAttaquent(village: Village): Village = ???
 def jour(village: Village): FinDePartie = ???
```
 
```diff 7->8
 case class Participant(nom: String)
 
 enum Villageois:
   case Humain(participant: Participant)
   case LoupGarou(participant: Participant)
 
- case class Village(humains: Set[Humain], loupsGarous: Set[LoupGarou])
+ case class Village(humains: Set[Humain], loupsGarous: Set[LoupGarou]):
+   def retirerVillageois(villageois: Villageois): Village = ???
 
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
- def loupsGarousAttaquent(village: Village): Village = ???
+ def loupsGarousAttaquent(village: Village): Village =
+   val victime: Humain = ???
+   village.retirerVillageois(victime)
 
 def jour(village: Village): FinDePartie = ???
```
 
```diff 8->9
 case class Participant(nom: String)
 
 enum Villageois:
   case Humain(participant: Participant)
   case LoupGarou(participant: Participant)
 
 case class Village(humains: Set[Humain], loupsGarous: Set[LoupGarou]):
   def retirerVillageois(villageois: Villageois): Village = ???
 
 enum FinDePartie:
   case VictoireDesLoupsGarous
   case VictoireDesHumains
 
- def partie() : FinDePartie =
-   distributionDesRôles(
+ def partie()(using interaction: Interaction) : FinDePartie =
+   interaction.distributionDesRôles(
     Participant("bob"),
     Participant("alice"),
     Participant("sacha"),
     Participant("sarah"),
     Participant("karim")
   ) |> nuit
 
- def nuit(village: Village) : FinDePartie =
+ def nuit(village: Village)(using interaction: Interaction) : FinDePartie =
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
 
- def distributionDesRôles(participants: Participant*): Village = ???
 
+ trait Interaction:
+   def choixVictime(village: Village): Humain
+   def distributionDesRôles(participants: Participant*): Village
 
- def loupsGarousAttaquent(village: Village): Village =
-   val victime: Humain = ???
+ def loupsGarousAttaquent(village: Village)(using interaction: Interaction): Village =
+   import village.*
+   val victime: Humain = interaction.lesLoupsGarousChoisissentUneVictime(village)
   village.retirerVillageois(victime)
 
 def jour(village: Village): FinDePartie = ???
```
 
 
```diff 9->10
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
 
- def leJourSeLève(village: Village) : FinDePartie =
+ def leJourSeLève(village: Village)(using interaction: Interaction) : FinDePartie =
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
   val victime: Humain = interaction.lesLoupsGarousChoisissentUneVictime(village)
   village.retirerVillageois(victime)
 
+ def déroulementDuJour(village: Village): Village = ???
 
- def jour(village: Village): FinDePartie = ???
+ def jour(village: Village)(using interaction: Interaction): FinDePartie =
+   village
+     |> déroulementDuJour
+     |> laPartieEstFinie ou nuit
```
 
 
 
 
 
```diff 10->11
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
 
- def leJourSeLève(village: Village)(using interaction: Interaction): FinDePartie =
-   laPartieEstFinie(village) ou jour
+ def leJourSeLève(village: Village, victime: Victime)(using interaction: Interaction): FinDePartie =
+   laPartieEstFinie(village) ou jour(victime)
 
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
 
 
+ case class Victime(humain: Humain)
 
- def loupsGarousAttaquent(village: Village)(using interaction: Interaction): Village =
+ def loupsGarousAttaquent(village: Village)(using interaction: Interaction): (Village, Victime) =
   import village.*
   val victime: Humain = interaction.lesLoupsGarousChoisissentUneVictime(village)
+   val villageÀJour = village.retirerVillageois(victime)
+   (villageÀJour, Victime(victime))
-   village.retirerVillageois(victime)
 
 def déroulementDuJour(village: Village): Village = ???
 
+ def annoncerLaMortDeLaVictime(victime: Victime): Unit = ???
 
- def jour(village: Village)(using interaction: Interaction): FinDePartie =
+ def jour(victime: Victime)(village: Village)(using interaction: Interaction): FinDePartie =
+   annoncerLaMortDeLaVictime(victime)
   village
     |> déroulementDuJour
     |> laPartieEstFinie ou nuit
    ```
 
```diff 11->12
case class Participant(nom: String)

enum Villageois:
  case Humain(participant: Participant)
  case LoupGarou(participant: Participant)

-case class Village(humains: Set[Humain], loupsGarous: Set[LoupGarou]):
+case class Village(humains: Set[Humain], loupsGarous: Set[LoupGarou], maire: Option[Villageois] = None):
  def retirerVillageois(villageois: Villageois): Village = ???
+  def setMaire(maire: Villageois): Village = ???

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
+  def annoncerLaMortDeLaVictime(victime: Victime): Unit
+  def electionMaire(village: Village): Villageois

case class Victime(humain: Humain)

def loupsGarousAttaquent(village: Village)(using interaction: Interaction): (Village, Victime) =
  import village.*
  val victime: Humain = interaction.lesLoupsGarousChoisissentUneVictime(village)
  val villageÀJour = village.retirerVillageois(victime)
  (villageÀJour, Victime(victime))

-def déroulementDuJour(village: Village): Village = ???
+def déroulementDuJour(village: Village)(using interaction: Interaction): Village =
+  val maire = interaction.electionMaire(village)
+  village.setMaire(maire)

-def annoncerLaMortDeLaVictime(victime: Victime): Unit = ???

def jour(victime: Victime)(village: Village)(using interaction: Interaction): FinDePartie =
-  annoncerLaMortDeLaVictime(victime)
+  interaction.annoncerLaMortDeLaVictime(victime)
  village
    |> déroulementDuJour
    |> laPartieEstFinie ou nuit
```
 
 
 
```diff 12->13
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
-   val maire = interaction.electionMaire(village)
+   val maire = village.maire.getOrElse(interaction.electionMaire(village))
   village.setMaire(maire)
 
 def jour(victime: Victime)(village: Village)(using interaction: Interaction): FinDePartie =
   interaction.annoncerLaMortDeLaVictime(victime)
   village
     |> déroulementDuJour
     |> laPartieEstFinie ou nuit
```
 
```diff 13->14
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
+   def lesVillageoisChoisissentUneVictime(village: Village): Villageois
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
+   val villageAvecUnMaire = village.setMaire(maire)
-   village.setMaire(maire)
+   val victime = interaction.lesVillageoisChoisissentUneVictime(villageAvecUnMaire)
+   villageAvecUnMaire.retirerVillageois(victime)
 
 def jour(victime: Victime)(village: Village)(using interaction: Interaction): FinDePartie =
   interaction.annoncerLaMortDeLaVictime(victime)
   village
     |> déroulementDuJour
     |> laPartieEstFinie ou nuit
```
 
```diff 14->15
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
-     case v: Village if v.humains.isEmpty  => FinDePartie.VictoireDesLoupsGarous
+     case v: Village if v.humains.isEmpty => FinDePartie.VictoireDesLoupsGarous
+     case v: Village if v.loupsGarous.isEmpty => FinDePartie.VictoireDesHumains
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




```bloc 1
enum Villageois:
  case Humain()
  case LoupGarou()

case class Village(humains: Set[Humain], loupsGarous: Set[LoupGarou])
```



```bloc 2
case class Participant(nom: String)

enum Villageois:
  case Humain(participant: Participant)
  case LoupGarou(participant: Participant)

case class Village(humains: Set[Humain], loupsGarous: Set[LoupGarou])
```


```bloc 3
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

```bloc 4
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

```bloc 5

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

```bloc 6
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

```bloc 7
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

```bloc 8
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

```bloc 9
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
  val victime: Humain = interaction.lesLoupsGarousChoisissentUneVictime(village)
  village.retirerVillageois(victime)

def jour(village: Village): FinDePartie = ???
```

```bloc 11
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
  val victime: Humain = interaction.lesLoupsGarousChoisissentUneVictime(village)
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

```bloc 12
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

```bloc 13
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

```bloc 14
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

```bloc 15
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
