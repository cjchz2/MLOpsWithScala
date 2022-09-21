package data_generator
import org.apache.commons.math3

object DataGenerator extends App {
  val ERROR_MEAN = 0
  val ERROR_STD_DEV =  20000
  val X_LOWER_BOUND = 700
  val X_UPPER_BOUND  = 5000
  val BETA = 185

  def generateErrorValue(errorMean: Double, errorStdDev: Double): Int =
    math3.distribution.NormalDistribution(errorMean, errorStdDev).sample.toInt

  def generateXValue(xLowerBound:Int, xUpperBound: Int): Int =
    math3.distribution.UniformIntegerDistribution(xLowerBound, xUpperBound).sample

  def generateYValue(beta: Int, xValue:Int, errorValue: Int): Int =
    beta*xValue-errorValue

  def generateRow(): Tuple =
    val errorValue = generateErrorValue(ERROR_MEAN, ERROR_STD_DEV)
    val xValue = generateXValue(X_LOWER_BOUND, X_UPPER_BOUND)
    val yValue = generateYValue(BETA, xValue, errorValue)
    (yValue, xValue)

  def generateOneHundredRows(): List[Tuple] =
    List.fill(100)(generateRow())
}
