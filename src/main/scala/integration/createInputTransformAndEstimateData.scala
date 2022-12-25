package integration

import dataGenerator.*
import transformData.transformData
//import transformData.transformData.writeTransformedDataToCSV
import fakeModel.fakeModel.createEstimatesAndWriteToCSV
import java.io.File
import dataProfiler.dataProfiler

object createInputTransformAndEstimateData extends App {
  val parameterEstimates: List[Double] = List(.75,.1,.1)
//   Generate Data
  val featureDataProfileFileName = "C:\\MLOpsFromScratch\\data\\dataProfile\\dataProfileFeatures2403249156.csv"
  val targetDataProfileFileName = "C:\\MLOpsFromScratch\\data\\dataProfile\\dataProfileTarget2403249156.csv"
  readConfigAndGenerateData
  val rawDataDir = new File("data/input/")
  val rawFilesNamesAsString = rawDataDir.listFiles.map(_.toString)
  // Transform targets and features.
  for (file <- rawFilesNamesAsString; if file contains "Target") {
    val transformer = new transformData(file, targetDataProfileFileName)
    transformer.writeTransformedDataToCSV
  }
  for (file <- rawFilesNamesAsString; if file contains "Features") {
    val transformer = new transformData(file, featureDataProfileFileName)
    transformer.writeTransformedDataToCSV
  }
  // Estimate data points
  val transformedDataDir = new File("data/transformed")
  val transformedFilesNamesAsString = transformedDataDir.listFiles.map(_.toString).filter(_ contains "Features")
//  //Write estimates to estimates folder.
  transformedFilesNamesAsString.foreach(createEstimatesAndWriteToCSV(_, parameterEstimates, "application.conf"))
  //Move


}
