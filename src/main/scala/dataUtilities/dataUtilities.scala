package dataUtilities

import com.typesafe.config.{Config, ConfigFactory}

object readingDataFromFlatFile extends App{
  def returnDataFromFileAsRows(fileName: String): List[List[String]] =
    var listVal = List.empty[List[String]]
    val dataFile = scala.io.Source.fromFile(fileName)
    for (line <- dataFile.getLines) {
      listVal = List(line) :: listVal
    }
    dataFile.close()
    listVal.reverse

  def splitStringRowIntoElements(listOfStringRows: List[List[String]]): List[List[String]] =
      listOfStringRows.map(x => x.head.split(",").toList)

  def returnColumnarFromRowData(listOfSeperatedStringRow: List[List[String]]): List[List[String]] =
    listOfSeperatedStringRow.transpose
}


