//import com.google.gson.{Gson, JsonObject}
//import dataUtilities.readingDataFromFlatFile.*
//import org.json.simple.JSONObject
//
//
//object dataProfiler extends App{
//  val data = returnDataFromFilesAsRowsAndSplitValues(raw"C:\MLOpsFromScratch\data\input\rawFeatures2403336600.csv")
//  data.tail.map((row:List[String]) => row.map((value:String) => value.toDouble)).transpose
//
//  val columnOfData: List[Double] = List(1,2,3,4,5)
//  val meanOfData = columnOfData.sum / columnOfData.length
//
//  def returnMeanFromColumn(columnOfData: List[Double]): Double =
//    columnOfData.sum / columnOfData.length
//  def returnVarianceFromColumn(columnOfData: List[Double]): Double =
//    val meanOfData = returnMeanFromColumn(columnOfData)
//    val deviationFromMean = columnOfData.map(_ - meanOfData)
//    val sumOfSquaredDeviations = deviationFromMean.map(scala.math.pow(_, 2)).sum
//    sumOfSquaredDeviations / (columnOfData.length - 1)
//
//  def returnStdDevFromColumn(columnOfData: List[Double]): Double =
//    scala.math.sqrt(returnVarianceFromColumn(columnOfData))
//
//
//  println(returnMeanFromColumn(columnOfData))
//
//  println(returnVarianceFromColumn(columnOfData))
//  case class DataProfile(
//                       column: String,
//                     mean: Double,
//                     stdDev: Double
//                   )
//  val gson = new Gson
//  val jsonString: String = """
//  	   {
//  	   "dumb_val":{"column":"homePrice","mean":250, "stdDev":10},
//  	   "Other_val":{"column":"sqft","mean":300, "stdDev":10}
//  	   }
//  	 """
//  val homeDataProfile:DataProfile = gson.fromJson(jsonString,classOf[DataProfile])
//
//  print(homeDataProfile)
////  val standardizedList = deviationFromMean.map(_ / standardDeviation)
//
//
//}
