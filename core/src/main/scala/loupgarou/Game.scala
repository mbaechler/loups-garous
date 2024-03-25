package loupgarou

import Villageois.LoupGarou
import Villageois.Humain
import loupgarou.syntax.{log, |>}

import scala.util.Random

case class Participant(nom: String)

enum Villageois:
  case Humain(participant: Participant)
  case LoupGarou(participant: Participant)

case class Village(humains: Set[Humain], loupsGarous: Set[LoupGarou], maire: Option[Villageois] = None):
  def retirerVillageois(villageois: Villageois): Village =
    (villageois match
      case humain: Humain => copy(humains = humains - humain)
      case loupGarou: LoupGarou => copy(loupsGarous = loupsGarous - loupGarou)
      ).copy(maire = maire.filter(_ != villageois))

  def setMaire(maire: Villageois): Village = copy(maire = Some(maire))

  override def toString: String = s"humains: ${humains.map(_.participant.nom).mkString(", ")} / loupsGarous ${loupsGarous.map(_.participant.nom).mkString(", ")} / maire $maire"

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
    |> log("la nuit tombe")
    |> loupsGarousAttaquent
    |> log("le jour se lève")
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


object ConsoleInteraction extends Interaction:

  override def lesVillageoisChoisissentUneVictime(village: Village): Villageois =
    Random.shuffle(village.humains ++ village.loupsGarous).head |> log("les villageois choisissent")

  override def annoncerLaMortDeLaVictime(victime: Victime): Unit =
    println(s"aujourd'hui ${victime.humain} est mort")

  override def electionMaire(village: Village): Villageois =
    Random.shuffle(village.humains ++ village.loupsGarous).head

  override def lesLoupsGarousChoisissentUneVictime(village: Village): Villageois.Humain =
    Random.shuffle(village.humains).head |> log("les loups garous choisissent")

  override def distributionDesRôles(participants: Participant*): Village =
    val (loupGarou, humains) = Random.shuffle(participants).splitAt(1)
    Village(
      loupsGarous = loupGarou.toSet.map(LoupGarou.apply),
      humains = humains.toSet.map(Humain.apply))

@main def play() = partie()(using ConsoleInteraction)
