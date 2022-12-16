package fakeModel
import com.typesafe.config.{Config, ConfigFactory}
import dataUtilities.readingDataFromFlatFile.*

import java.io.PrintWriter

object fakeModel extends App {
  def removeHeadersFromData(data:List[List[String]]):List[List[String]] =
    data.tail

  def convertDataToDouble(dataNoHeaders: List[List[String]]): List[List[Double]] =
    dataNoHeaders.map((row:List[String]) => row.map((value:String) => value.toDouble))

  def returnDataAndParmsZipped(dataNoHeaders: List[List[Double]], parameters: List[Double]): List[List[Tuple2[Double, Double]]] =
    dataNoHeaders.map((rowInData:List[Double]) => rowInData.zip(parameters))

  def multiplyDataAndParms(dataAndParms: List[List[Tuple2[Double, Double]]]): List[List[Double]] =
    dataAndParms
      .map((listOfTuples: List[Tuple2[Double, Double]]) => listOfTuples.map((dataVal:Double, parm:Double) => dataVal * parm))

  def readFeaturesFromFileAndReturnEstimate(fileName: String, parameters: List[Double]): List[Double] =
    val data = returnDataFromFilesAsRowsAndSplitValues(fileName)
    val dataNoHeaders = removeHeadersFromData(data)
    val doubleData = convertDataToDouble(dataNoHeaders)
    val dataAndParms = returnDataAndParmsZipped(doubleData, parameters)
    val estimates = multiplyDataAndParms(dataAndParms)
    estimates.map((scaledInputs:List[Double]) => scaledInputs.sum)


  def createEstimatesCalculationFileName(inputFileWithPath: String, configFileName: String) = {
    val applicationConf: Config = ConfigFactory.load(configFileName)
    val outputPath = applicationConf.getString("estimatesDataFilePath")
    val estimatesFileName = inputFileWithPath.split("\\\\").last.replace("transformedFeatures", "estimates")
    outputPath + estimatesFileName
  }

  def writeEstimatesToCSV(data: List[Double], fileName:String): Unit =
    val writer = PrintWriter(fileName)
    writer.write("estimatedHousePrice")
    writer.write("\n")
    for (estimate <- data) {
      writer.write(estimate.toString)
      writer.write("\n")
    }
    writer.close()


  def createEstimatesAndWriteToCSV(fileName:String, parameters: List[Double], configFileName: String): Unit =
    val estimates = readFeaturesFromFileAndReturnEstimate(fileName, parameters)
    val estimatesFileName = createEstimatesCalculationFileName(fileName: String, configFileName: String)
    writeEstimatesToCSV(estimates, estimatesFileName)
}


