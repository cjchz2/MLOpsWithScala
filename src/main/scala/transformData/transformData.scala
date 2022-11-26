package transformData

import java.io.{File, FileWriter, PrintWriter}
import scala.io.Source
import com.typesafe.config.{Config, ConfigFactory}

import scala.jdk.CollectionConverters.CollectionHasAsScala

import dataUtilities.readingDataFromFlatFile._

/*
To do.
1. Read CSVs in batches? Difficult to do since we have to calculate a global mean/std dev across
whole dataset.
2. Figure out more elegant solution to schema approach?
3. Make standardizeNumericData generic to deal with other numeric data types.
4. How to get returnDataFromFileAsRows to append data vs prepend it?
5. Integrate schema information into config file more elegantly?
6. Unit test

*/

object transformData  extends App {
  

  //How can I get this to work for any "numeric" data type. Things you can add,divide, etc.
  def standardizeNumericData(columnOfData: List[Int]): List[Any] =
    val meanOfData = columnOfData.sum / columnOfData.length // V SIZE?
    val deviationFromMean = columnOfData.map(_ - meanOfData)
    val sumOfSquaredDeviations = deviationFromMean.map(scala.math.pow(_, 2)).sum
    val variance = sumOfSquaredDeviations / (columnOfData.length - 1)
    val standardDeviation = scala.math.sqrt(variance)
    val standardizedList = deviationFromMean.map(_ / standardDeviation)
    standardizedList

  def applyDataProcessingOnColumn(columnAndSchema: Tuple2[List[String], String]): List[Any] =
    if (columnAndSchema(1) == "Int")
      columnAndSchema(0).head :: standardizeNumericData(columnAndSchema(0).tail.map(_.toInt))
    else
      columnAndSchema(0)

  def applyTransformationsToAllData(data: List[List[String]], schema: List[String]): List[List[Any]] =
    var transformedData = List.empty[List[Any]]
    val zippedList = data zip schema
    for (data <- zippedList) {
      val column = applyDataProcessingOnColumn(data)
      transformedData = column :: transformedData
    }
    transformedData.reverse

  def returnSchema(featureOrTarget:String, configFileName:String) =
    val applicationConf: Config = ConfigFactory.load(configFileName)
    if (featureOrTarget == "feature")
      applicationConf.getStringList("featureSchema").asScala.toList
    else
      applicationConf.getStringList("targetSchema").asScala.toList


  def readDataCSVAndApplyCalculations(fileName: String,featureOrTarget:String, configFileName: String) =
    val csvSchema =  returnSchema(featureOrTarget, configFileName)
    val csvList = returnDataFromFileAsRows(fileName)
    val splitCSVList = splitStringRowIntoElements(csvList)
    val columnarList = returnColumnarFromRowData(splitCSVList)
    applyTransformationsToAllData(columnarList, csvSchema)

  def createFeatureCalculationFileName(inputFileWithPath: String, configFileName: String) = {
    val applicationConf: Config = ConfigFactory.load(configFileName)
    val outputPath = applicationConf.getString("transformedDataFilePath")
    val transformedFileName = inputFileWithPath.split("\\\\").last.replace("raw", "transformed")
    outputPath + transformedFileName
  }

  def determineFeatureOrTarget(fileName:String) =
    if (fileName contains "Features")
      "feature"
    else
      "target"

  def writeFeatureCalculationsToCSV(inputFileName:String, configFileName:String) =
    val outputFileName = createFeatureCalculationFileName(inputFileName, configFileName)
    val transformedData = readDataCSVAndApplyCalculations(inputFileName, determineFeatureOrTarget(inputFileName), configFileName)
    val transformedRowData = transformedData.transpose
    val headers = transformedRowData(0).map(_.toString).map(_.trim)
    val writer = PrintWriter(outputFileName)
    writer.write(headers.mkString(","))
    writer.write("\n")
    for (list <- transformedRowData.tail) {
      writer.write(list.mkString(","))
      writer.write("\n")
    }
    writer.close

  val fileName = raw"C:\MLOpsFromScratch\data\input\rawTarget165188679125.csv"
  val configFile = raw"application.conf"

  writeFeatureCalculationsToCSV(fileName, configFile)



}