import zio.{Scope, ZIO, ZIOAppArgs, ZIOAppDefault}

import scala.util.Random

object types:

  enum Potion:
    case PotionDeMort
    case PotionDeVie

  enum Pouvoir:
    case SansPouvoir
    case Sorcière(potions: Set[Potion])

  extension (sorcière: Pouvoir.Sorcière)
    def retirePotion(potion: Potion): Pouvoir.Sorcière =
      sorcière.copy(potions = sorcière.potions - potion)

  case class Participant(nom: String)

  enum Villageois:
    case Humain(participant: Participant, pouvoir: Pouvoir)
    case LoupGarou(participant: Participant)

  import Villageois._

  case class LoupsGarous(lesLoupsGarous: Set[LoupGarou])

  enum Village:
    case Menacé(humains: Set[Humain], loupsGarous: Set[LoupGarou])
    case Décimé(loupGarou: Set[LoupGarou])
    case Tranquille(humains: Set[Humain])

  extension (village: Village.Menacé)
    def sorcière: Option[(Humain, Pouvoir.Sorcière)] =
      village.humains.collectFirst {
        case sorcière @ Humain(_, pouvoir @ Pouvoir.Sorcière(_)) =>
          (sorcière, pouvoir)
      }

    def retireLaPotionÀLaSorcière(potion: Potion): Village.Menacé =
      (for
        (humain, sorcière) <- village.sorcière
        humainSansPotion = humain.copy(pouvoir = sorcière.retirePotion(potion))
      yield village.copy(humains =
        village.humains - humain + humainSansPotion
      ): Village.Menacé).getOrElse[Village.Menacé](village)

  case class Victime(humain: Humain)

  enum PotionUtilisée:
    case PotionDeMort(villageois: Villageois)
    case PotionDeVie

  case class Maire(villageois: Villageois)

  case class Coupable(villageois: Villageois)

  enum FinDePartie:
    case VictoireDesLoups
    case VictoireDesHumains

object syntax:
  extension [A](a: A) def |>[B](f: A => B): B = f(a)

  def tireAuHasard[T](choses: Set[T]): T =
    Random.shuffle(choses).head

object jeu:

  import types._
  import Villageois._
  import Pouvoir._
  import Potion._
  import FinDePartie._
  import syntax._

  def distributionDesRôles(participants: Participant*): Village.Menacé =
    Random.shuffle(List.from(participants)) match
      case Nil => ???
      case loupGarou :: sorcière :: lesAutres =>
        Village.Menacé(
          loupsGarous = Set(LoupGarou(loupGarou)),
          humains = lesAutres.map[Humain](Humain(_, SansPouvoir)).toSet +
            Humain(
              sorcière,
              Sorcière(Set(Potion.PotionDeMort, Potion.PotionDeVie))
            )
        )

  def loupsGarousAttaquent(village: Village.Menacé): Victime =
    Victime(tireAuHasard(village.humains))

  def sorcièreUtiliseUnePotion(
      village: Village.Menacé,
      victime: Victime
  ): Option[PotionUtilisée] =
    for
      (sorcière, pouvoir) <- village.sorcière
      potion <- Random.shuffle(pouvoir.potions).headOption
    yield potion match
      case Potion.PotionDeMort =>
        val victimeDeLaPotion = tireAuHasard(
          village.humains ++ village.loupsGarous - sorcière - victime.humain
        )
        PotionUtilisée.PotionDeMort(victimeDeLaPotion)
      case Potion.PotionDeVie => PotionUtilisée.PotionDeVie

  def leJourSeLève(
      village: Village.Menacé,
      victime: Victime,
      potion: Option[PotionUtilisée]
  ): Village =
    potion match
      case None => humainMort(victime.humain)(village)

      case Some(PotionUtilisée.PotionDeVie) =>
        village.retireLaPotionÀLaSorcière(PotionDeVie)

      case Some(PotionUtilisée.PotionDeMort(loupGarou: LoupGarou)) =>
        village.retireLaPotionÀLaSorcière(PotionDeMort)
          |> humainMort(victime.humain)
          |> loupGarouMort(loupGarou)

      case Some(PotionUtilisée.PotionDeMort(humain: Humain)) =>
        village.retireLaPotionÀLaSorcière(PotionDeMort)
          |> humainMort(victime.humain)
          |> humainMort(humain)

  def villageoisMort(village: Village.Menacé, villageois: Villageois): Village =
    villageois match
      case humain: Humain       => humainMort(humain)(village)
      case loupGarou: LoupGarou => loupGarouMort(loupGarou)(village)

  def humainMort(humain: Humain)(village: Village): Village =
    village match
      case menacé @ Village.Menacé(humains, loupsGarous) =>
        val humainsRestant = humains - humain
        if humainsRestant.isEmpty
        then Village.Décimé(menacé.loupsGarous)
        else
          Village.Menacé(
            humains = humainsRestant,
            loupsGarous = menacé.loupsGarous
          )

      case décimé: Village.Décimé         => décimé
      case tranquille: Village.Tranquille => tranquille

  def loupGarouMort(loupGarou: LoupGarou)(village: Village): Village =
    village match
      case _: Village.Décimé     => village
      case _: Village.Tranquille => village
      case menacé: Village.Menacé =>
        val loupsGarousRestant = menacé.loupsGarous - loupGarou
        if loupsGarousRestant.isEmpty
        then Village.Tranquille(menacé.humains)
        else
          Village.Menacé(
            humains = menacé.humains,
            loupsGarous = loupsGarousRestant
          )

  def électionDuMaire(village: Village.Menacé): Maire =
    Maire(tireAuHasard(village.humains ++ village.loupsGarous))

  def désigneUnCoupable(village: Village.Menacé): Coupable =
    Coupable(tireAuHasard(village.humains ++ village.loupsGarous))

  def bruleLeCoupable(village: Village.Menacé, coupable: Coupable): Village =
    villageoisMort(village, coupable.villageois)

  def laPartieEstFinie(village: Village): Either[Village.Menacé, FinDePartie] =
    village match
      case v: Village.Menacé     => Left(v)
      case _: Village.Décimé     => Right(VictoireDesLoups)
      case _: Village.Tranquille => Right(VictoireDesHumains)

  extension [T](t: T)
    def log(step: String): T =
      println(s"$step $t")
      t

  extension (either: Either[Village.Menacé, FinDePartie])
    def ou(f: Village.Menacé => FinDePartie): FinDePartie =
      either match
        case Left(village)      => f(village)
        case Right(finDePartie) => finDePartie

  def nuit(village: Village.Menacé): FinDePartie =
    val victime = loupsGarousAttaquent(village).log("loupsGarousAttaquent")
    val potion = sorcièreUtiliseUnePotion(village, victime).log("sorcièreUtiliseUnePotion")

    val villageAuMatin = leJourSeLève(village, victime, potion).log("leJourSeLève")

    laPartieEstFinie(villageAuMatin).log("laPartieEstFinie") ou jour

  def jour(villageAuMatin: Village.Menacé): FinDePartie =

    val maire = électionDuMaire(villageAuMatin).log("électionDuMaire")
    val coupable = désigneUnCoupable(villageAuMatin).log("désigneUnCoupable")
    val villageAvantLaNuit = bruleLeCoupable(villageAuMatin, coupable).log("bruleLeCoupable")

    laPartieEstFinie(villageAvantLaNuit) ou nuit

  def partie(): FinDePartie =
    distributionDesRôles(
      Participant("bob"),
      Participant("alice"),
      Participant("sacha"),
      Participant("sarah"),
      Participant("karim")
    ).log("distributionDesRôles")
      |> nuit

object Main extends ZIOAppDefault:

  override def run: ZIO[Environment & ZIOAppArgs & Scope, Any, Any] =
    ZIO.succeed(jeu.partie())

