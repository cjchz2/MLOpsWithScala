package integration

import dataGenerator._
//import transformData.transformData.writeTransformedDataToCSV
import fakeModel.fakeModel.createEstimatesAndWriteToCSV
import java.io.File
import dataProfiler.dataProfiler

object createInputTransformAndEstimateData extends App {
//  val parameterEstimates: List[Double] = List(1,10,100)
//  // Generate Data
//  readConfigAndGenerateData
//  val rawDataDir = new File("data/input/")
//  val rawFilesNamesAsString = rawDataDir.listFiles.map(_.toString)
//  // Write transformed data
//  rawFilesNamesAsString.foreach(writeTransformedDataToCSV(_,"application.conf"))
//  val transformedDataDir = new File("data/transformed")
//  val transformedFilesNamesAsString = transformedDataDir.listFiles.map(_.toString).filter(_ contains "Features")
//  //Write estimates to estimates folder. 
//  transformedFilesNamesAsString.foreach(createEstimatesAndWriteToCSV(_, parameterEstimates, "application.conf"))

//  transformData


}
