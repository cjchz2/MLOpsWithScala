package dataUtilities

import com.typesafe.config.{Config, ConfigFactory}

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
