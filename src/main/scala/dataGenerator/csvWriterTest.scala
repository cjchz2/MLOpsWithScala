package dataGenerator
import com.opencsv._
import java.io.FileWriter
import kantan.csv._
import kantan.csv.ops._
import kantan.csv.generic._
import shapeless.~?>.idKeyWitness
import shapeless.~?>.idValueWitness
import shapeless.~?>.witness
import java.io._

object csvWriterTest extends App {
  val writer = new CSVWriterBuilder(new FileWriter("yourfile.csv")).withSeparator(',').build()
  val entries: Array[String] = "first#second#third".split("#")
  println(entries)
  writer.writeNext(entries)
  writer.close()
}

object otherCSVWriterTest extends App {
  val randoRow = dataGeneratorCaseClasses.row(Some(1),Some(2),Some(3),Some(4))
  val otherRandoRow = dataGeneratorCaseClasses.row(Some(2),Some(3),Some(4),Some(5))

  val randoList = List(randoRow,otherRandoRow)

  println(Tuple.fromProductTyped(randoRow))
}

object finalCSVWriterTest extends App {

  def writeRowsToCSV(listOfRows:List[List[Int]], headers:String,  fileName:String)=

    val writer = new PrintWriter(new File(fileName))

    writer.write(headers)
    writer.write("\n")

    for (list <- listOfRows) {
      writer.write(list.mkString(","))
      writer.write("\n")
  }
    writer.close

  val listOfLists = List(List(1,2,3), List(4,5,6))
  writeRowsToCSV(listOfLists, "a,b,c", "dumb_file.csv")
}