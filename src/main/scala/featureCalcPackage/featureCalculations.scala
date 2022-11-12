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

object featureCalc extends App {
//  val fcVal = new featureCalculations
//  val listVal = fcVal.returnDataFromFileAsRows("C:\\MLOpsFromScratch\\data\\generatedInputData\\houseDataInput640956142740.csv")
//  println(listVal(0))
//  val l = List(1,2,3)
//  print(l.length)
}

object featureCalculations  extends App {
  val l = List(List("1,2,3,4"), List("4,5,7,11"), List("10, 11, 12, 13"))

  def splitStringRowIntoElements(listOfStringRows: List[List[String]]): List[List[String]] =
    listOfStringRows.map(x => x(0).split(",").toList)

  def returnColumnarFromRowData(listOfSeperatedStringRow: List[List[String]]): List[List[String]] =
    listOfSeperatedStringRow.transpose

  var schema = List(sqft, schoolRating, numberOfBedRooms, price)

  def imposeSchema(schema: List[dataVariable], ColumnStringData: List[List[String]]) =
    for (dataVar <- schema) {
      println(dataVar)
    }

  var numberVarList = List(1,2,3,4,5)

  //How can I get this to work for any "numeric" data type. Things you can add,divide, etc.
  def standardizeNumericData(columnOfData: List[Int]): List[Double] =
    val meanOfData = columnOfData.sum/columnOfData.length // V SIZE?
    val deviationFromMean = columnOfData.map(_ - meanOfData)
    val sumOfSquaredDeviations = deviationFromMean.map(scala.math.pow(_,2)).sum
    val variance = sumOfSquaredDeviations/(columnOfData.length-1)
    val standardDeviation = scala.math.sqrt(variance)
    val standardizedList = numberVarList.map(_ - meanOfData).map(_/standardDeviation)
    standardizedList

  var dumbData: List[List[Any]] = List(List("1", "2", "3","4","5"),List("not", "a", "number"))
  var dumbSchema: List[List[Any]] = List(List("Int",0), List("String",1))
//  dumbData.foreach(println)

  for (schema <- dumbSchema) {
//    println(schema(1).asInstanceOf[Int])
    if (schema(0).asInstanceOf[String] == "Int") {
      println("This is a Int column")
      val intStringDumbCol = dumbData(schema(1).asInstanceOf[Int])
      val intDumbCol = intStringDumbCol.map(_.asInstanceOf[String].toInt)
      val standardizedIntDumbCol = standardizeNumericData(intDumbCol)
      println(standardizedIntDumbCol)
    }
//    println(dumbData(schema(1).asInstanceOf[Int]))
  }


//  standardizeNumericData(numberVarList).foreach(println)
  //  var scala.math.sqrt(sumOfSquaredDeviations)
//  columnarList.foreach(println)
//  def returnColumnarData(listOfRows: List[List[Int]]):
//    listOfRows.transpose
//  val number_of_rows = l.length
//  //Weakness here is that the first row needs to have correct number of delimiters
//  val number_of_columns = l(0)(0).split(",").length
//  println(number_of_rows)
//  println(number_of_columns)
//  var columnarData = List.fill(number_of_rows)(List.fill(number_of_columns))
//  val seperatedElement = l.map(x => x(0).split(",").toList)
//  seperatedElement.foreach(println)
//  var newList = List.empty[List[Any]]
//  val listSep = l(0)(0).split(",")
//  listSep.foreach(println)
//  for (rowInList <- l) {
//    for (rowAsString <- rowInList) {
//      val splitElements = rowAsString.split(",").toList
//      println(rowInList)
////      print(splitElements)
////      newList = splitElements :: newList
//    }
//  }
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
