package dataGeneratorPackage
import dataGeneratorPackage.dataGeneratorCaseClasses.*

import org.apache.commons.math3

import java.io.{File, PrintWriter}

class dataGenerator(dataGenerationParmList: List[dataGenerationParameters], errorTermParmsVal: errorTermParms,
                    var totalNumberOfRows: Int, headers:String, filePath:String, baseFileName:String) {

  require (totalNumberOfRows > 0)

  private var batchSize = 10000

  def setBatchSize(newBatchSize:Int): Unit =
    if (newBatchSize > 0)
      batchSize = newBatchSize
    else
      println("Batch size must be larger than zero")

  def setTotalNumberOfRows(newTotalNumberOfRows:Int): Unit =
    if (newTotalNumberOfRows > 0)
      totalNumberOfRows = newTotalNumberOfRows
    else
      println("Row size must be larger than zero")

  def generateNormalDistInteger(mean: Int, stdDev: Int): Int =
    math3.distribution.NormalDistribution(mean, stdDev).sample.toInt

  def generateUniformDistInteger(lowerBound: Int, upperBound: Int): Int =
    math3.distribution.UniformIntegerDistribution(lowerBound, upperBound).sample

  def generateErrorTerm: Int =
    generateNormalDistInteger(errorTermParmsVal.mean, errorTermParmsVal.stdDev)

  def generateInputVarAndScaledInputVar(dataGenerationParms: dataGenerationParameters): inputVarAndScaledInputVar =
    dataGenerationParms match {
      case uniformDistributionVariableParameters(name, lowerBound, upperBound, coefficient) =>
        val inputVar = generateUniformDistInteger(lowerBound, upperBound)
        inputVarAndScaledInputVar(name, inputVar, inputVar * coefficient)
    }

  def generateRowOfInputs: List[inputVarAndScaledInputVar] =
    dataGenerationParmList.map(generateInputVarAndScaledInputVar)

  def generateOutputValue(inputVals: List[inputVarAndScaledInputVar], errorTerm: Int): Int =
    inputVals.map(_.scaledInputVar).sum + errorTerm

  def generateRow(inputVals: List[inputVarAndScaledInputVar], outputVal: Int): List[Any] =
    val inputList = inputVals.map(_.inputVar)
    inputList :+ outputVal

  def generateBatchOfRows(numberOfRowsToWrite: Int): List[List[Any]] =
    val listOfInputVars: List[List[inputVarAndScaledInputVar]] = List.fill(numberOfRowsToWrite)(generateRowOfInputs)
    val listOfOutputVars: List[Int] = listOfInputVars.map(generateOutputValue(_, generateErrorTerm))
    val listOfRows: List[List[Any]] = (listOfInputVars zip listOfOutputVars).map(generateRow(_, _))
    listOfRows

  def writeRowsToCSV(listOfRows:List[List[Any]], fileName: String):Unit =
    val writer = new PrintWriter(new File(fileName))

    writer.write(headers)
    writer.write("\n")

    for (list <- listOfRows) {
      writer.write(list.mkString(","))
      writer.write("\n")
    }
    writer.close

  def returnNumberOfRowsToWrite(remainingRowsToWrite:Int): Int =
      if (batchSize > remainingRowsToWrite)
        remainingRowsToWrite
      else
        batchSize

  def generateUniqueFileName =
    filePath +  baseFileName + System.nanoTime()/1000 + ".csv"

  def generateBatchOfRowsAndApplyFunction(remainingRowsToWrite:Int, functionToApply: (List[List[Any]], String) => Any): Any =
    if (remainingRowsToWrite  > 0)
      val numberOfRowsToWrite = returnNumberOfRowsToWrite(remainingRowsToWrite)
      val batchOfRows = generateBatchOfRows(numberOfRowsToWrite)
      functionToApply(batchOfRows, generateUniqueFileName)
      val newRemainingRowsToWrite = remainingRowsToWrite - batchSize
      generateBatchOfRowsAndApplyFunction(newRemainingRowsToWrite, functionToApply)

  def generateAllRowsAndWriteToCSV: Unit =
    generateBatchOfRowsAndApplyFunction(totalNumberOfRows, writeRowsToCSV)

}
