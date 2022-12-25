package dataDrift
import scala.math
import org.apache.commons.math3.stat.inference
import transformData.transformData
import dataUtilities.readingDataFromFlatFile

import java.io.File
class dataDriftDetector {


  val listOne: Array[Double] = Array(1,2,3,3,4,5,3,1,3,4,5,2,4,1,3,4,5,6)
  val listTwo: Array[Double] = Array(1000,4,1,1000,2,22345,245,4,5,4,3,55,30,333,23,12,2,5)
  val trainingDataDir = new File("data/trainingData/")
  val trainingFilesNamesAsString: Array[String] = trainingDataDir.listFiles.map(_.toString)
  // Transform targets and features.
//  for (file <- trainingFilesNamesAsString; if file contains "Features") {
//    val transformer = new transformData(file, targetDataProfileFileName)
//    returnDataFromFilesAsRowsAndSplitValues()
//  }
  val trainingData = trainingFilesNamesAsString.map((file:String) => readingDataFromFlatFile.returnDataFromFileAsRows(file))
  val trainingDataNoHeaders = trainingData.map((dataSet: List[List[String]]) => dataSet.tail)
  val ksTest = inference.KolmogorovSmirnovTest()
  val ksValue = ksTest.kolmogorovSmirnovTest(listOne, listTwo, false)
}

object dataDriftDetector extends App {
  val dataDriftDetectorInstance = new dataDriftDetector

  println(dataDriftDetectorInstance.ksValue)
}