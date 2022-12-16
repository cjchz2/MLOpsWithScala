package dataGenerator

import dataGeneratorCaseClasses.{dataGenerationParameters, errorTermParms, uniformDistributionVariableParameters, inputVarAndScaledInputVar}
import org.apache.commons.math3
import java.io.{File, PrintWriter}
import org.apache.commons.math3.distribution.{AbstractRealDistribution, NormalDistribution}
import org.apache.commons.math3.random.RandomDataGenerator


class invalidBatchSize(s: String) extends Exception(s)
class invalidRowSize(s: String) extends Exception(s)
class invalidGenerationOption(s: String) extends Exception(s)
class randomDataGenerator extends RandomDataGenerator
class randomDataGeneratorWriter(fileName:File) extends PrintWriter(fileName:File)

class createRawInput(dataGenerationParmList: List[dataGenerationParameters], errorTermParmsVal: errorTermParms,
                    var totalNumberOfRows: Int, headers:String, filePath:String, baseFileName:String,
                    val randomDataGeneratorVal: RandomDataGenerator = new randomDataGenerator
                   ) {

  require (totalNumberOfRows > 0)

  private var batchSize = 10000

  def generateSeperateTargetandFeatureFileName =
    val timestamp = System.nanoTime()/1000
    (filePath +  baseFileName + "Features" + timestamp + ".csv", filePath +  baseFileName + "Target" + timestamp + ".csv")
  
  def setBatchSize(newBatchSize:Int): Unit =
    if (newBatchSize > 0)
      batchSize = newBatchSize
    else
      throw new invalidBatchSize("batchSize must be strictly larger than 0")

  def getBatchSize = batchSize

  def setTotalNumberOfRows(newTotalNumberOfRows:Int): Unit =
    if (newTotalNumberOfRows > 0)
      totalNumberOfRows = newTotalNumberOfRows
    else
      throw new invalidRowSize("totalNumberOfRows must be larger than zero")

  def generateNormalDistInteger(mean: Int, stdDev: Int): Int =
    math.ceil(randomDataGeneratorVal.nextGaussian(mean, stdDev)).toInt

  def generateUniformDistInteger(lowerBound: Int, upperBound: Int): Int =
    math.ceil(randomDataGeneratorVal.nextUniform(lowerBound, upperBound)).toInt

  def generateInputVarAndScaledInputVar(dataGenerationParms: dataGenerationParameters): inputVarAndScaledInputVar =
    dataGenerationParms match {
      case uniformDistributionVariableParameters(name, lowerBound, upperBound, coefficient) =>
        val inputVar = generateUniformDistInteger(lowerBound, upperBound)
        inputVarAndScaledInputVar(name, inputVar, inputVar * coefficient)
    }

  def generateRowOfInputs(dataGenerationParmList: List[dataGenerationParameters]): List[inputVarAndScaledInputVar] =
    dataGenerationParmList.map(generateInputVarAndScaledInputVar)

  def generateOutputValue(inputVals: List[inputVarAndScaledInputVar], errorTerm: Int): Int =
    inputVals.map(_.scaledInputVar).sum + errorTerm

  def generateRow(inputVals: List[inputVarAndScaledInputVar], outputVal: Int): List[Any] =
    val inputList = inputVals.map(_.inputVar)
    inputList :+ outputVal

  def generateBatchOfRows(numberOfRowsToWrite: Int): List[List[Any]] =
    val listOfInputVars: List[List[inputVarAndScaledInputVar]] = List.fill(numberOfRowsToWrite)(generateRowOfInputs(dataGenerationParmList))
    val listOfOutputVars: List[Int] = listOfInputVars.map(generateOutputValue(_, generateNormalDistInteger(errorTermParmsVal.mean, errorTermParmsVal.stdDev)))
    val listOfRows: List[List[Any]] = (listOfInputVars zip listOfOutputVars).map(generateRow(_, _))
    listOfRows

  def returnValidHeaders(trainingFeatureOrTarget:String):String = 
    if (trainingFeatureOrTarget == "feature")
      headers.split(",").init.mkString(",")
    else if (trainingFeatureOrTarget == "target")
      headers.split(",").last.trim
    else
      headers

  def writeRowsToCSV(listOfRows:List[List[Any]], writer: randomDataGeneratorWriter, trainingFeatureOrTarget:String): Unit =
    val newHeaders = returnValidHeaders(trainingFeatureOrTarget)
    writer.write(newHeaders)
    writer.write("\n")
    for (list <- listOfRows) 
      writer.write(list.mkString(","))
      writer.write("\n")
    writer.close

  def returnNumberOfRowsToWrite(remainingRowsToWrite:Int): Int =
      if (batchSize > remainingRowsToWrite)
        remainingRowsToWrite
      else
        batchSize

  def generateBatchOfRowsWriteToCSV(remainingRowsToWrite:Int): Any =
    if (remainingRowsToWrite  > 0)
      val numberOfRowsToWrite = returnNumberOfRowsToWrite(remainingRowsToWrite)
      val batchOfRows = generateBatchOfRows(numberOfRowsToWrite)
      val fileNames = generateSeperateTargetandFeatureFileName
      lazy val featureWriter = new randomDataGeneratorWriter(new File(fileNames(0)))
      val featureData = batchOfRows.map(_.init)
      writeRowsToCSV(featureData, featureWriter, "feature") 
      lazy val targetWriter = new randomDataGeneratorWriter(new File(fileNames(1)))
      val targetData = batchOfRows.map(_.last).map(List(_))
      writeRowsToCSV(targetData, targetWriter, "target")
      val newRemainingRowsToWrite = remainingRowsToWrite - batchSize
      generateBatchOfRowsWriteToCSV(newRemainingRowsToWrite)

  def generateAllRowsAndWriteToCSV: Unit =
    generateBatchOfRowsWriteToCSV(totalNumberOfRows)

}
