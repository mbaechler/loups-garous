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