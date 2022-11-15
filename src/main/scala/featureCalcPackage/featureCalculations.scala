package featureCalcPackage

import scala.io.Source
import dataGeneratorPackage.dataGeneratorCaseClasses._
class featureCalculations {

  def returnDataFromFileAsRows(fileName: String):List[List[Any]] =
    var listVal = List.empty[List[Any]]
    for (line <- Source.fromFile(fileName).getLines)
//      val cols = line.split(",").map(_.toInt)
      listVal = List(line) :: listVal
    listVal
}
  def returnDataAsColumns(dataSet: List[List[Any]]): List[List[Any]] =
    ???



object featureCalculationsObj  extends App {
  val l = List(List("1,2,3,4"), List("4,5,7,11"), List("10, 11, 12, 13"))

  def splitStringRowIntoElements(listOfStringRows: List[List[String]]): List[List[String]] =
    listOfStringRows.map(x => x(0).split(",").toList)

  def returnColumnarFromRowData(listOfSeperatedStringRow: List[List[String]]): List[List[String]] =
    listOfSeperatedStringRow.transpose

  def imposeSchema(schema: List[dataVariable], ColumnStringData: List[List[String]]) =
    for (dataVar <- schema) {
      println(dataVar)
    }

  var numberVarList = List(1,2,3,4,5)

  //How can I get this to work for any "numeric" data type. Things you can add,divide, etc.
  def standardizeNumericData(columnOfData: List[Int]): List[Any] =
    val meanOfData = columnOfData.sum/columnOfData.length // V SIZE?
    val deviationFromMean = columnOfData.map(_ - meanOfData)
    val sumOfSquaredDeviations = deviationFromMean.map(scala.math.pow(_,2)).sum
    val variance = sumOfSquaredDeviations/(columnOfData.length-1)
    val standardDeviation = scala.math.sqrt(variance)
    val standardizedList = deviationFromMean.map(_/standardDeviation)
    standardizedList

  var dumbData: List[List[Any]] = List(List("1", "2", "3","4","5"),List("not", "a", "number"))
  var dumbSchema: List[List[Any]] = List(List("Int",0), List("String",1))

  def applyIntegerDataProcessing(columnOfData:List[Any], schema: List[Any]): List[Any] =
      if (schema(0).asInstanceOf[String] == "Int") {
        val stringCol = columnOfData.asInstanceOf[List[String]]
        val intCol = stringCol.map(_.toInt)
        val standardizedIntCol = standardizeNumericData(intCol)
        standardizedIntCol
      }
      else {
        columnOfData
      }
  def applyIntegerDataProcessingAlt(dataAndSchema:Tuple2[List[Any],Any])= //: List[Any] =
    if (dataAndSchema(1) == "Int")
      standardizeNumericData(dataAndSchema(0).map(_.toString).map(_.toInt))
    else
      dataAndSchema(0)
//      val intCol =
//      standardizeNumericData(dataAndSchema(0))
  val data = List(List("1", "2","3", "4"), List("a", "thing", "would", "cool"))
  val schema = List("Int","String")


  val dataAndSchema = data.zip(schema)
  dataAndSchema.map(applyIntegerDataProcessingAlt(_)).foreach(println)

//  val zippedColSchema: List[(List[String], List[Matchable])] = data.zip(schema)
//  zippedColSchema.map(applyIntegerDataProcessing((_,_)))
//  zippedColSchema.foreach(println)
//  val columnOfData = List(1,2,3,4)
//  println(columnOfData.sum/columnOfData.length)

}

  // for (line <- newList) {
  ////   println(line)
  //   for (element <- line) {
  //     try {
  //       println(element.asInstanceOf[String].toInt)
  //     }
  //     catch {
  //       case e: Exception => println(element)
  //     }
  //   }
  // }

  //  def toNonAny(x: Any): Any = {
  //    x match {
  //      case i: Int => i
  //      case s: String => s
  //      case _ => println("Unknown")
  //    }
  //  }
  //  var newestList = List.empty[Int]
  //  for (line <- newList) {
  //    //   println(line)
  //    for (element <- line) {
  //      println(toNonAny(element))
  //      newestList= toNonAny(element) :: newestList
  //    }
  //  }
  //  newestList.map(_+1)




//}
