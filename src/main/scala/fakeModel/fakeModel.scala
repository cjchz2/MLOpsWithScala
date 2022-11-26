package fakeModel
import dataUtilities.readingDataFromFlatFile._

object fakeModel extends App {
  val fileName = raw"C:\MLOpsFromScratch\data\transformed\transformedFeatures165188679125.csv"
  val data = returnDataFromFilesAsRowsAndSplitValues(fileName)

  def removeHeadersFromData(data:List[List[String]]):List[List[String]] =
    data.tail

  def convertDataToDouble(dataNoHeaders: List[List[String]]): List[List[Double]] =
    dataNoHeaders.map((row:List[String]) => row.map((value:String) => value.toDouble))

  def returnDataAndParmsZipped(dataNoHeaders: List[List[Double]], parameters: List[Double]): List[List[Tuple2[Double, Double]]] =
    dataNoHeaders.map((rowInData:List[Double]) => rowInData.zip(parameters))

  def multiplyDataAndParms(dataAndParms: List[List[Tuple2[Double, Double]]]): List[List[Double]] =
    dataAndParms
      .map((listOfTuples: List[Tuple2[Double, Double]]) => listOfTuples.map((dataVal:Double, parm:Double) => dataVal * parm))

  def readFeaturesFromFileAndReturnEstimate(fileName: String, parameters: List[Double]): List[List[Double]] =
    val data = returnDataFromFilesAsRowsAndSplitValues(fileName)
    val dataNoHeaders = removeHeadersFromData(data)
    val doubleData = convertDataToDouble(dataNoHeaders)
    val dataAndParms = returnDataAndParmsZipped(doubleData, parameters)
    val estimates = multiplyDataAndParms(dataAndParms)
    estimates
}


