package featureCalcPackage

import scala.io.Source
import dataGeneratorPackage.dataGeneratorCaseClasses._



/*
To do.
1. Read CSVs in batches? Difficult to do since we have to calculate a global mean/std dev across
whole dataset.
2. Figure out more elegant solution to schema approach?
3. Make standardizeNumericData generic to deal with other numeric data types.
4. How to get returnDataFromFileAsRows to append data vs prepend it?
5. Unit test

*/

object featureCalculations  extends App {

  def splitStringRowIntoElements(listOfStringRows: List[List[String]]): List[List[String]] =
    listOfStringRows.map(x => x.head.split(",").toList)

  def returnColumnarFromRowData(listOfSeperatedStringRow: List[List[String]]): List[List[String]] =
    listOfSeperatedStringRow.transpose

  //How can I get this to work for any "numeric" data type. Things you can add,divide, etc.
  def standardizeNumericData(columnOfData: List[Int]): List[Any] =
    val meanOfData = columnOfData.sum / columnOfData.length // V SIZE?
    val deviationFromMean = columnOfData.map(_ - meanOfData)
    val sumOfSquaredDeviations = deviationFromMean.map(scala.math.pow(_, 2)).sum
    val variance = sumOfSquaredDeviations / (columnOfData.length - 1)
    val standardDeviation = scala.math.sqrt(variance)
    val standardizedList = deviationFromMean.map(_ / standardDeviation)
    standardizedList

  def applyDataProcessingOnColumn(columnAndSchema: Tuple2[List[String], String]): List[Any] =
    if (columnAndSchema(1) == "Int")
      columnAndSchema(0).head :: standardizeNumericData(columnAndSchema(0).tail.map(_.toInt))
    else
      columnAndSchema(0)

  //How can I append as opposed to prepending? The "::" operator is not allowing these to be swapped even though both are lists.
  //Getting the following error when swapped Required List[List[String]], required List[Object] (for listVal).
  //But I am specifying listVal as an empty List[String]!
  def returnDataFromFileAsRows(fileName: String): List[List[String]] =
    var listVal = List.empty[List[String]]
    val dataFile = scala.io.Source.fromFile(fileName)
    for (line <- dataFile.getLines){
      listVal =  List(line) :: listVal}
    dataFile.close()
    listVal.reverse

  def applyTransformationsToAllData(data: List[List[String]], schema: List[String]):List[List[Any]] =
    var transformedData = List.empty[List[Any]]
    val zippedList = data zip schema
    for (data <- zippedList) {
      val thing = applyDataProcessingOnColumn(data)
      transformedData = thing :: transformedData
    }
    transformedData


  def readDataCSVAndApplyFeatureCalculations(fileName:String,csvSchema:List[String]) =
    val csvList = returnDataFromFileAsRows(fileName)
    val splitCSVList = splitStringRowIntoElements(csvList)
    val columnarList = returnColumnarFromRowData(splitCSVList)

    applyTransformationsToAllData(columnarList,csvSchema)
}