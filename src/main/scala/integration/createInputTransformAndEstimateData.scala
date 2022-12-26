package integration

import dataGenerator.*
import transformData.transformData
//import transformData.transformData.writeTransformedDataToCSV
import fakeModel.fakeModel.createEstimatesAndWriteToCSV
import java.io.File
import org.apache.commons.io.FileUtils

import java.nio.file.{ Files, Path, StandardCopyOption}
import dataProfiler.dataProfiler

object createInputTransformAndEstimateData extends App {
  val parameterEstimates: List[Double] = List(.75,.1,.1)
//   Generate Data
  val featureDataProfileFileName = "C:\\MLOpsFromScratch\\data\\dataProfile\\dataProfileFeatures2403249156.csv"
  val targetDataProfileFileName = "C:\\MLOpsFromScratch\\data\\dataProfile\\dataProfileTarget2403249156.csv"
  readConfigAndGenerateData
  val rawTargetsStagingDir = new File("data/rawTargetsStaging/")
  val rawFeaturesStagingDir = new File("data/rawFeaturesStaging/")
  val rawTargetFileNamesAsString = rawTargetsStagingDir.listFiles.map(_.toString)
  val rawFeatureFileNamesAsString = rawFeaturesStagingDir.listFiles.map(_.toString)
  // Transform targets and features.
  for (file <- rawTargetFileNamesAsString) {
    val transformer = new transformData(file, targetDataProfileFileName, "target")
    transformer.writeTransformedDataToCSV
  }
  for (file <- rawFeatureFileNamesAsString) {
    val transformer = new transformData(file, featureDataProfileFileName, "feature")
    transformer.writeTransformedDataToCSV
  }
  // Estimate data points
  val transformedFeatureDir = new File("data/transformedFeatures")
  val transformedTargetDir = new File("data/transformedTargets")
  val transformedFeatureFilesNamesAsString = transformedFeatureDir.listFiles.map(_.toString)
  val transformedTargetFilesNamesAsString = transformedTargetDir.listFiles.map(_.toString)
//  //Write estimates to estimates folder.
  transformedFeatureFilesNamesAsString.foreach(createEstimatesAndWriteToCSV(_, parameterEstimates, "application.conf"))
  //Move rawTargets in data to archiveData
  val archivedTransformedDataFilePath = new File("archiveData/rawTargets/")
  //move data from data/input to archiveData input
  import org.apache.commons.io.FileUtils
    for (file <- rawTargetsStagingDir.listFiles()) {
      FileUtils.moveFileToDirectory(file, new File(archivedTransformedDataFilePath.toString.replace("data", "archiveData")), false )
    }
  //Move rawFeatures in data to archiveData
  val archivedRawFeaturesDataFilePath = new File("archiveData/rawFeatures/")
  for (file <- rawFeaturesStagingDir.listFiles()) {
    FileUtils.moveFileToDirectory(file, new File(archivedRawFeaturesDataFilePath.toString.replace("data", "archiveData")), false )
  }
  //move transformed features from data/transformedFeatures to archiveData/featureStore

  val featureStoreDataDir = new File("archiveData/featureStore/")
  for (file <- transformedFeatureDir.listFiles()) {
    FileUtils.moveFileToDirectory(file, new File(featureStoreDataDir.toString.replace("transformed", "featureStore")), false )
  }
//  val targetStoreDataDir = new File("archiveData/transformedTargets/")
//  for (file <- transformedTargetDir.listFiles()) {
//    FileUtils.moveFileToDirectory(file, new File("archiveData/transformedTargets/", false ))
//  }

}
