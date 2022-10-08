package dataGenerator
import com.typesafe.config.{Config, ConfigFactory}
import dataGenerator.dataGeneratorUtilities.*
import dataGenerator.dataGeneratorCaseClasses.*
import java.io.{File, FileWriter}


object main extends App{
  val applicationConf: Config = ConfigFactory.load("application.conf")

  val sqftParms = uniformDistributionVariableParameters(
    sqft(), applicationConf.getInt("sqft.lowerBound"),
    applicationConf.getInt("sqft.upperBound"), applicationConf.getInt("sqft.coefficient"))

  val schoolRatingParms = uniformDistributionVariableParameters(
    schoolRating(), applicationConf.getInt("schoolRating.lowerBound"),
    applicationConf.getInt("schoolRating.upperBound"), applicationConf.getInt("schoolRating.coefficient"))

  val numberOfBedRoomsParms = uniformDistributionVariableParameters(
    numberOfBedRooms(), applicationConf.getInt("numberOfBedRooms.lowerBound"),
    applicationConf.getInt("numberOfBedRooms.upperBound"), applicationConf.getInt("numberOfBedRooms.coefficient"))

  val errorTermParmsVal = errorTermParms(applicationConf.getInt("errorTerm.mean"),applicationConf.getInt("errorTerm.stdDev"))

  val distributionParmList: List[dataGenerationParameters] = List(sqftParms, schoolRatingParms, numberOfBedRoomsParms)


  val headers: String = applicationConf.getString("headers")
  val numberOfRows =  applicationConf.getInt("numberOfRowsGeneratedByUser")
  val baseFileName = applicationConf.getString("baseFileName")
  val uniqueFileName = baseFileName + System.currentTimeMillis()/1000 + ".csv"

  generateManyRowsAndWriteToCSV(distributionParmList, errorTermParmsVal, numberOfRows,headers, uniqueFileName)
}
