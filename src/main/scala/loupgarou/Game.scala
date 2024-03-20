package loupgarou

import Villageois.LoupGarou
import Villageois.Humain

enum Villageois:
  case Humain()
  case LoupGarou()

case class Village(humains: Set[Humain], loupsGarous: Set[LoupGarou])
