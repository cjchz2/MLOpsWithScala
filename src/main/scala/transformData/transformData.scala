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
7. How to deal with dates of target labels???

*/

class transformData(dataFileName: String, dataProfileFileName:String) extends App {
  val applicationConf: Config = ConfigFactory.load("application.conf")


  def returnDataProfileAgg(aggType: String): List[Double] =
    val headers = if (dataFileName contains "Target") {
     applicationConf.getString("targetHeadersForTransformation").split(",").toList
    }
    else {
      applicationConf.getString("featureHeaders").split(",").toList
    }
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
  // break up this crappy function
  def readDataCSVAndStandardize =
    val data = returnDataFromFileAsRows(dataFileName)
    val splitData = splitStringRowIntoElements(data)
    val splitDataNoHeaders = splitData.tail
    val columnarData: List[List[String]] = if (dataFileName contains "Target")
      List(returnColumnarFromRowData(splitDataNoHeaders).head)
    else
      returnColumnarFromRowData(splitDataNoHeaders)
    val stdDev: List[Double] = returnDataProfileAgg("stdDev")
    val mean: List[Double] = returnDataProfileAgg("mean")
    val zippedMeanAndStdDev= stdDev zip mean
    val standardizedColumns = standardizeAllColumns(
      columnarData.map((column:List[String]) => column.map((value:String) => value.toDouble)),
      zippedMeanAndStdDev)
    val finalColumns = if (dataFileName contains "Target")
      val dates = returnColumnarFromRowData(splitDataNoHeaders).tail.head
      val zippedDatesAndLabels = standardizedColumns.head zip dates
      zippedDatesAndLabels.map{
        case  (data,date) => List(data,date)
        }
        else
          standardizedColumns.transpose

    splitData.head :: finalColumns

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

  val dataFileName = raw"C:\MLOpsFromScratch\data\input\rawFeatures579052687363.csv"
  val dataProfileFileName = raw"C:\MLOpsFromScratch\data\dataProfile\dataProfileFeatures2403249156.csv"
//  val dataProfileFileName = raw"C:\MLOpsFromScratch\data\dataProfile\dataProfileTarget2403249156.csv"
//  val dataFileName = raw"C:\MLOpsFromScratch\data\input\rawTarget725898811707.csv"
  val transformer = new transformData(dataFileName, dataProfileFileName)
  transformer.writeTransformedDataToCSV
//  val test  = List(List(1,2,3), List('a', 'b', 'c'))
//
//  val numbers = test.head
//  val letters = test.tail.head
//  println(numbers)
//  println(letters)
//  val zippie = numbers zip letters
//
//  println(zippie)
//  val zipList = zippie.map{
//    case  (a,b) => List(a,b)
//  }
//  println(zipList)
//  println(zipList.map((f: List[String]) => f.mkString))

}