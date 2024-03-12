package loupgarou

import Villageois.LoupGarou
import Villageois.Humain

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