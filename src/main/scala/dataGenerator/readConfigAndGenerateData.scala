package dataGenerator
import com.typesafe.config.{Config, ConfigFactory}
import dataGenerator.createRawInput
import dataGenerator.dataGeneratorCaseClasses._


object readConfigAndGenerateData extends App {

  val applicationConf: Config = ConfigFactory.load("application.conf")
  val dataFilePath = applicationConf.getString("inputDataFilePath")

  val sqftParms = dataGeneratorCaseClasses.uniformDistributionVariableParameters(
    dataGeneratorCaseClasses.sqft(), applicationConf.getInt("sqft.lowerBound"),
    applicationConf.getInt("sqft.upperBound"), applicationConf.getInt("sqft.coefficient"))

  val schoolRatingParms = dataGeneratorCaseClasses.uniformDistributionVariableParameters(
    dataGeneratorCaseClasses.schoolRating(), applicationConf.getInt("schoolRating.lowerBound"),
    applicationConf.getInt("schoolRating.upperBound"), applicationConf.getInt("schoolRating.coefficient"))

  val numberOfBedRoomsParms = dataGeneratorCaseClasses.uniformDistributionVariableParameters(
    dataGeneratorCaseClasses.numberOfBedRooms(), applicationConf.getInt("numberOfBedRooms.lowerBound"),
    applicationConf.getInt("numberOfBedRooms.upperBound"), applicationConf.getInt("numberOfBedRooms.coefficient"))

  val errorTermParmsVal = dataGeneratorCaseClasses.errorTermParms(applicationConf.getInt("errorTerm.mean"),applicationConf.getInt("errorTerm.stdDev"))

  val distributionParmList: List[dataGeneratorCaseClasses.dataGenerationParameters] = List(sqftParms, schoolRatingParms, numberOfBedRoomsParms)


  val featureHeaders: String = applicationConf.getString("featureHeaders")
  val targetHeaders: String = applicationConf.getString("targetHeaders")
  val numberOfRows =  applicationConf.getInt("numberOfRowsGeneratedByUser")
  val baseFileName = applicationConf.getString("baseFileName")

  
  val dataGeneratorVal = new createRawInput(
    distributionParmList, errorTermParmsVal, numberOfRows,
    featureHeaders, targetHeaders, dataFilePath, baseFileName, "training")

  dataGeneratorVal.generateAllRowsAndWriteToCSV
}

