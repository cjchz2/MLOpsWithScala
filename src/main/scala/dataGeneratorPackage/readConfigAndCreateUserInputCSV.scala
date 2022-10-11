package dataGeneratorPackage
import com.typesafe.config.{Config, ConfigFactory}
import java.io.{File, FileWriter}
import dataGeneratorPackage.dataGenerator
import dataGeneratorPackage.dataGeneratorCaseClasses.*


object readConfigAndCreateUserInputCSV extends App{
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
  val filePath = applicationConf.getString("filePath")
  
  val dataGeneratorVal = new dataGenerator(distributionParmList, errorTermParmsVal, numberOfRows, headers, filePath,baseFileName)

  dataGeneratorVal.generateAllRowsAndWriteToCSV
}