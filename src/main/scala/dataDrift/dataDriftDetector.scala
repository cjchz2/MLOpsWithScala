package dataDrift
import scala.math
import org.apache.commons.math3.stat.inference
import transformData.transformData
import dataUtilities.readingDataFromFlatFile
import integration.createInputTransformAndEstimateData.{rawFilesNamesAsString, targetDataProfileFileName}

import java.io.File
import dataUtilities.readingDataFromFlatFile
class dataDriftDetector {


  def combineFeatureDataInDirAndReturnColumnarList(dirName: String): List[List[Double]] =
    val dir = new File(dirName).listFiles().map(_.toString).filter(_ contains "Features")
    dir.map(readingDataFromFlatFile.returnDataFromFilesAsRowsAndSplitValues(_).tail).reduce(_ ++ _).transpose.map(_.map(_.toDouble))


  def combineTrainingAndInputArray =
    val trainingData = combineFeatureDataInDirAndReturnColumnarList("data/training")
    val inputData = combineFeatureDataInDirAndReturnColumnarList("archiveData/featureStore")
    val trainingArray: Array[Array[Double]] = trainingData.map(_.toArray).toArray
    val inputArray: Array[Array[Double]] = inputData.map(_.toArray).toArray
    trainingArray zip inputArray


  def performKSTestOnAllColumns(trainingAndInput: Array[Tuple2[Array[Double], Array[Double]]]) =
    val ksTest = inference.KolmogorovSmirnovTest()
    trainingAndInput.map(ksTest.kolmogorovSmirnovTest(_, _, false))

  val zipped = combineTrainingAndInputArray
  performKSTestOnAllColumns(zipped).foreach(println)

  //
  //  def combineDataInDir(dirName: String) =
  //  for (file <- trainingeDir; if file contains "Feature") {
  //    readingDataFromFlatFile.returnDataFromFilesAsRowsAndSplitValues(file).tail
  //
  //  }
  //  val listOne: Array[Double] = Array(1,2,3,3,4,5,3,1,3,4,5,2,4,1,3,4,5,6)
  //  val listTwo: Array[Double] = Array(1000,4,1,1000,2,22345,245,4,5,4,3,55,30,333,23,12,2,5)
  //  val trainingDataDir = new File("data/trainingData/")
  //  val trainingFilesNamesAsString: Array[String] = trainingDataDir.listFiles.map(_.toString)
  //  // Transform targets and features.
  ////  for (file <- trainingFilesNamesAsString; if file contains "Features") {
  ////    val transformer = new transformData(file, targetDataProfileFileName)
  ////    returnDataFromFilesAsRowsAndSplitValues()
  ////  }
  //
  //  def returnKSPValueOnSingleColumn(trainingData: List[List[Double]], incrementalData):Double =
  //
  //
  //  val trainingData = trainingFilesNamesAsString.map((file:String) => readingDataFromFlatFile.returnDataFromFileAsRows(file))
  ////  val trainingDataNoHeaders = trainingData.map((dataSet: List[List[String]]) => dataSet.tail)
}


object dataDriftDetector extends App {
  val dataDriftDetectorInstance = new dataDriftDetector
//  println(dataDriftDetectorInstance.ksValue)
//  println(dataDriftDetectorInstance.ksValue)
}