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

  def minBool(v1: Boolean, v2: Boolean): Boolean = if (v1 < v2) v1 else v2

  def determineRetraining(ksResults: Array[Double]): Boolean =
    ksResults.map(_ < .05).reduceLeft(minBool)

  val zipped = combineTrainingAndInputArray
  println(determineRetraining(performKSTestOnAllColumns(zipped)))

}

object dataDriftDetector extends App {
  val dataDriftDetectorInstance = new dataDriftDetector
//  println(dataDriftDetectorInstance.ksValue)
//  println(dataDriftDetectorInstance.ksValue)
}