package loupgarou

import Villageois.LoupGarou
import Villageois.Humain

case class Participant(nom: String)

enum Villageois:
  case Humain(participant: Participant)
  case LoupGarou(participant: Participant)

case class Village(humains: Set[Humain], loupsGarous: Set[LoupGarou], maire: Option[Villageois] = None)

enum FinDePartie:
  case VictoireDesLoupsGarous
  case VictoireDesHumains

def partie()(using interaction: Interaction) : FinDePartie =
  distributionDesRôles(
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

def distributionDesRôles(participants: Participant*): Village = ???

trait Interaction:
  def lesLoupsGarousChoisissentUneVictime(loupsGarous: Set[LoupGarou]): Humain = ???
  def annoncerLaMortDeLaVictime(victime: Victime): Unit = ???
  def electionMaire(village: Village): Villageois = ???

case class Victime(humain: Humain)

def loupsGarousAttaquent(village: Village)(using interaction: Interaction): (Village, Victime) =
  import village.*
  val victime: Humain = interaction.lesLoupsGarousChoisissentUneVictime(loupsGarous)
  val villageÀJour = village.copy(humains = humains - victime)
  (villageÀJour, Victime(victime))

def déroulementDuJour(village: Village)(using interaction: Interaction): Village =
  val maire = interaction.electionMaire(village)
  village.copy(maire = Some(maire))

def jour(victime: Victime)(village: Village)(using interaction: Interaction): FinDePartie =
  interaction.annoncerLaMortDeLaVictime(victime)
  village
    |> déroulementDuJour
    |> laPartieEstFinie ou nuit