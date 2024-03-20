package loupgarou

import Villageois.LoupGarou
import Villageois.Humain
import loupgarou.syntax.|>

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