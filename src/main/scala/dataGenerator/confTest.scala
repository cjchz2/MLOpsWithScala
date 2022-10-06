package dataGenerator
import com.typesafe.config.{Config, ConfigFactory}
import dataGenerator.dataGeneratorUtilities.*
import dataGenerator.dataGeneratorCaseClasses.*
import com.github.tototoshi.csv
import com.opencsv.CSVWriterBuilder

import java.io.{File, FileWriter}


object confTest extends App{
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

  val manyRows = generateManyInputRows(distributionParmList, errorTermParmsVal, 10)
  val writer = new CSVWriterBuilder(new FileWriter("yourfile.csv")).withSeparator(',').build()
  val entries: Array[String] = "first#second#third".split("#")
//  println(manyRows)
//  writer.writeNext(manyRows)
//  writer.close()
}
