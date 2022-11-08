package featureCalcPackage

import scala.io.Source

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

object dhflfu  extends App {
  val l = List(List("1,2,3"),List("4,5,7"))
  var newList = List.empty[List[Any]]
  val listSep = l(0)(0).split(",")
  for (listInList <- l) {
    for (elements <- listInList) {
      val splitElements = elements.split(",").toList
      //      print(splitElements)
      newList = splitElements :: newList
    }
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




}
