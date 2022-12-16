package dataProfiler

import com.typesafe.config.{Config, ConfigFactory}

import java.io.{File, FileReader, PrintWriter}
import dataUtilities.readingDataFromFlatFile.*

case class dataProfile(name: String, aggregationType: String, value: Double)

class dataProfiler {
  def returnMeanOfColumn(column: List[Double]): Double =
    column.sum / column.length

  def returnStdDevOfColumn(column: List[Double]): Double =
    val meanOfData = column.sum / column.length // V SIZE?
    val deviationFromMean = column.map(_ - meanOfData)
    val sumOfSquaredDeviations = deviationFromMean.map(scala.math.pow(_, 2)).sum
    val variance = sumOfSquaredDeviations / (column.length - 1)
    val standardDeviation = scala.math.sqrt(variance)
    standardDeviation

  def profileMeanData(columnarData: List[List[Double]], header: List[String]) =
    val meansOfColumns: List[Double] = columnarData.map(returnMeanOfColumn)
    val meanList = List.fill(header.length)("mean")
    val fullList = header.zip(meanList).zip(meansOfColumns).map {
      case ((x, y), z) => (x, y, z)
    }
    fullList

  def profileStdDevData(columnarData: List[List[Double]], header: List[String]) =
    val stdDevOfColumns: List[Double] = columnarData.map(returnStdDevOfColumn)
    val meanList = List.fill(header.length)("stdDev")
    val fullList = header.zip(meanList).zip(stdDevOfColumns).map {
      case ((x, y), z) => (x, y, z)
    }
    fullList

  def createDataProfileFileName(inputFileWithPath: String, configFileName: String) =
    val applicationConf: Config = ConfigFactory.load(configFileName)
    val outputPath = applicationConf.getString("dataProfileFilePath")
    val transformedFileName = inputFileWithPath.split("\\\\").last.replace("raw", "/dataProfile")
    outputPath + transformedFileName

  def writeDataProfile(inputFileName: String) =
    val outputFileName = createDataProfileFileName(inputFileName, "application.conf")
    println(outputFileName)
    val data = returnDataFromFilesAsRowsAndSplitValues(inputFileName)
    val headers = data.head
    val doubleColumnarData = returnDataWithNoHeadersAndAsDouble(data).transpose
    val profiledMeanData = profileMeanData(doubleColumnarData, headers)
    val profiledStdDevData = profileStdDevData(doubleColumnarData, headers)
    val writer = PrintWriter(outputFileName)
    for (list <- profiledMeanData) {
      writer.write(list.toString.replace("(", "").replace(")", ""))
      writer.write("\n")
    }
    for (list <- profiledStdDevData) {
      writer.write(list.toString.replace("(", "").replace(")", ""))
      writer.write("\n")
    }
    writer.close()
}
class readDataProfile {
    def readDataProfile(fileName:String) =
      val dataProfileList = returnDataFromFilesAsRowsAndSplitValues(fileName)
      val dataProfileSplit = dataProfileList.map((f:List[String]) => dataProfile(f(0),f(1),f(2).toDouble))
      dataProfileSplit

    def returnAppropriateAggregation(dataProfileList: List[dataProfile], columnName:String, aggregationType:String) =
      val aggregationValue = for (
        item <- dataProfileList; if item.name == columnName; if item.aggregationType == aggregationType)
      yield item.value
      aggregationValue.head
  }

object dataProfileTest extends App{
  val dataProfilerInstance = new dataProfiler
  dataProfilerInstance.writeDataProfile(raw"C:\MLOpsFromScratch\data\input\rawFeatures2403249156.csv")
  val readDataProfileInstance = new readDataProfile
  val dp = readDataProfileInstance.readDataProfile(raw"C:\\MLOpsFromScratch\\data\\dataProfile\\dataProfileFeatures2403249156.csv")
  println(readDataProfileInstance.returnAppropriateAggregation(dp, "sqft", "stdDev"))
}