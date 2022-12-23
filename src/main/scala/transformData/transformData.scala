package transformData

import java.io.{File, FileWriter, PrintWriter}
import scala.io.Source
import com.typesafe.config.{Config, ConfigFactory}

import scala.jdk.CollectionConverters.CollectionHasAsScala
import dataUtilities.readingDataFromFlatFile.*
import dataProfiler.readDataProfile

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

class transformData(dataFileName: String, dataProfileFileName:String) extends App {
  def returnDataProfileAgg(aggType: String): List[Double] =
    val headers: List[String] = returnHeadersAsListFromFile(dataFileName)
    val readDataProfileInstance = new readDataProfile
    val dataProfile = readDataProfileInstance.readDataProfile(dataProfileFileName)
    headers
      .map((strVal:String) => readDataProfileInstance.returnAppropriateAggregation(dataProfile, strVal, aggType))

  def standardizeColumn(columnOfData: List[Double], mean: Double, stdDev: Double): List[Double] =
      val deviationFromMean = columnOfData.map(_ - mean)
      deviationFromMean.map(_ / stdDev)

  def standardizeAllColumns(data:List[List[Double]], zippedMeanAndStdDev: List[(Double, Double)]): List[List[Double]] =
    val dataZipAgg = data zip zippedMeanAndStdDev
    dataZipAgg.map {
      case (x: List[Double], (y: Double, z: Double)) => standardizeColumn(x,y,z)
    }

  def createTransformedFileName(inputFileName: String) =
    val applicationConf: Config = ConfigFactory.load("application.conf")
    val outputPath = applicationConf.getString("transformedDataFilePath")
    val transformedFileName = inputFileName.split("\\\\").last.replace("raw", "transformed")
    outputPath + transformedFileName

  def readDataCSVAndStandardize =
    val data = returnDataFromFileAsRows(dataFileName)
    val splitData = splitStringRowIntoElements(data)
    val splitDataNoHeaders = splitData.tail
    val columnarData = returnColumnarFromRowData(splitDataNoHeaders)
    val stdDev: List[Double] = returnDataProfileAgg("stdDev")
    val mean: List[Double] = returnDataProfileAgg("mean")
    val zippedMeanAndStdDev= stdDev zip mean
    val standardizedColumns = standardizeAllColumns(
      columnarData.map((column:List[String]) => column.map((value:String) => value.toDouble)),
      zippedMeanAndStdDev)
    splitData.head :: standardizedColumns.transpose

  def writeTransformedDataToCSV =
    val transformedRowData = readDataCSVAndStandardize
    val transformFileName = createTransformedFileName(dataFileName)
    val writer = PrintWriter(transformFileName)
    for (list <- transformedRowData) {
      writer.write(list.mkString(","))
      writer.write("\n")
    }
    writer.close

}
object runTransform extends App {

//  val dataFileName = raw"C:\MLOpsFromScratch\data\input\rawFeatures203004313136.csv"
//  val dataProfileFileName = raw"C:\MLOpsFromScratch\data\dataProfile\dataProfileFeatures2403249156.csv"
//  val dataProfileFileName = raw"C:\MLOpsFromScratch\data\dataProfile\dataProfileTarget2403249156.csv"
//  val dataFileName = raw"C:\MLOpsFromScratch\data\input\rawTarget203004274309.csv"
//  val transformer = new transformData(dataFileName, dataProfileFileName)
//  transformer.writeTransformedDataToCSV


}