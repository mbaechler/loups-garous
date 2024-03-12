package loupgarou

import Villageois.LoupGarou
import Villageois.Humain

case class Participant(nom: String)

enum Villageois:
  case Humain(participant: Participant)
  case LoupGarou(participant: Participant)

case class Village(humains: Set[Humain], loupsGarous: Set[LoupGarou])

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

def distributionDesRôles(participants: Participant*): Village = ???

trait Interaction:
  def choixVictime(loupsGarous: Set[LoupGarou]): Humain = ???

def loupsGarousAttaquent(village: Village)(using interaction: Interaction): Village =
  import village.*
  val victime: Humain = interaction.choixVictime(loupsGarous)
  village.copy(humains = humains - victime)

def déroulementDuJour(village: Village): Village = ???

def jour(village: Village)(using interaction: Interaction): FinDePartie =
    village
      |> déroulementDuJour
      |> laPartieEstFinie ou nuit