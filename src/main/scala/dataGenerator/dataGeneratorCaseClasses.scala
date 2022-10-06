package dataGenerator

object dataGeneratorCaseClasses {
  sealed trait dataVariable

  case class sqft() extends dataVariable

  case class schoolRating() extends dataVariable

  case class numberOfBedRooms() extends dataVariable

  case class price() extends dataVariable

  sealed trait dataGenerationParameters

  case class uniformDistributionVariableParameters(name: dataVariable, lowerBound: Int,
                                                   upperBound: Int, coefficient: Int) extends dataGenerationParameters

  case class errorTermParms(mean: Int, stdDev: Int) extends dataGenerationParameters

  case class inputVarAndScaledInputVar(name: dataVariable, inputVar: Int, scaledInputVar: Int)
}
