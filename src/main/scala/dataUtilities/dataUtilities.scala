package dataUtilities

import com.typesafe.config.{Config, ConfigFactory}

import java.io.File

object helperFunctions extends App {
  // Use generic functions!
  def returnMinBoolValue (v1: Boolean, v2: Boolean): Boolean = if (v1 < v2) v1 else v2
  def returnMaxBoolValue (v1: Boolean, v2: Boolean): Boolean = if (v1 > v2) v1 else v2
  def returnMinIntValue (v1: Int, v2: Int): Int = if (v1 < v2) v1 else v2
  def returnMaxIntValue (v1: Int, v2: Int): Int = if (v1 > v2) v1 else v2
}

object readingDataFromFlatFile extends App{
  def splitStringRowIntoElements(listOfStringRows: List[List[String]]): List[List[String]] =
      listOfStringRows.map(x => x.head.split(",").toList)

  def returnColumnarFromRowData(listOfSeperatedStringRow: List[List[String]]): List[List[String]] =
    listOfSeperatedStringRow.transpose
  
  def returnDataFromFileAsRows(fileName: String): List[List[String]] =
    val dataFile = scala.io.Source.fromFile(fileName)
    val csvData = dataFile.getLines.toList.map((x:String) => x.split("\n").toList)
    dataFile.close()
    csvData

  def returnHeadersAsListFromFile(fileName: String): List[String] =
    val dataFile = scala.io.Source.fromFile(fileName)
    val header = dataFile.getLines.toList.head
    dataFile.close()
    header.split(",").map(_.trim).toList

  def returnDataFromFilesAsRowsAndSplitValues(fileName:String) =
    val data = returnDataFromFileAsRows(fileName)
    splitStringRowIntoElements(data)
    
  def returnDataWithNoHeadersAndAsDouble(data:List[List[String]]):List[List[Double]] =
    data.tail.map((stringList: List[String])=>stringList.map((stringVal:String)=>stringVal.toDouble))
  
  def returnColumnarData(data:List[List[Double]]): List[List[Double]] =
    data.transpose
    
  def returnFileHeaders(data:List[List[String]]): List[String] =
    data.head
}

object fileOperations extends App {
  def removeAllFilesInDirectory(folderName: String): Unit =
    val dataDir = new File(folderName)
    val fileList = dataDir.listFiles
    for (file <- fileList) {
      file.delete()
    }

  def findFileByMaxTimestamp(folderName: String, prefix: String):Unit =
    val dataDir = new File(folderName)
    val fileListAsString = dataDir.listFiles.map(_.toString)
    for (file <- fileListAsString) {
      // If there is a file in dir that doesn't contain prefix ignore.
      if (file.split(prefix)(0) != file) {
        println(file)
        println(file.split(prefix)(1).split(".csv")(0))
      }
//      println(file)
//      println(file.split(prefix)(0) == file)
//      println(file.split(prefix).isEmpty)


    }
  findFileByMaxTimestamp(raw"C:\MLOpsFromScratch\archiveData\input", "rawFeatures")

}