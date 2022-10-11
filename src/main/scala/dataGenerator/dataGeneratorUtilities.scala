package dataGenerator
import dataGenerator.dataGeneratorCaseClasses.*

import org.apache.commons.math3

import java.io.{File, PrintWriter}


class dataGeneratorUtilities(dataGenerationParmList: List[dataGenerationParameters], errorTermParmsVal: errorTermParms,
                             numberOfRows: Int, headers:String,  fileName:String) {

  def generateNormalDistInteger(mean: Int, stdDev: Int): Int =
    new math3.distribution.NormalDistribution(mean, stdDev).sample.toInt

  def generateUniformDistInteger(lowerBound: Int, upperBound: Int): Int =
    new math3.distribution.UniformIntegerDistribution(lowerBound, upperBound).sample

  def generateInputVarAndScaledInputVar(dataGenerationParms: dataGenerationParameters): inputVarAndScaledInputVar =
    dataGenerationParms match {
      case uniformDistributionVariableParameters(name, lowerBound, upperBound, coefficient) =>
        val inputVar = generateUniformDistInteger(lowerBound, upperBound)
        inputVarAndScaledInputVar(name, inputVar, inputVar * coefficient)
    }

  def generateErrorTerm: Int =
    generateNormalDistInteger(errorTermParmsVal.mean, errorTermParmsVal.stdDev)

  def generateRowOfInputs: List[inputVarAndScaledInputVar] =
    dataGenerationParmList.map(generateInputVarAndScaledInputVar)

  def generateOutputValue(inputVals: List[inputVarAndScaledInputVar], errorTerm: Int): Int =
    inputVals.map(_.scaledInputVar).sum + errorTerm

  def generateRow(inputVals: List[inputVarAndScaledInputVar], outputVal: Int): List[Int] =
    val inputList = inputVals.map(_.inputVar)
    inputList :+ outputVal

  def generateManyInputRows: List[List[Int]] =
    val listOfInputVars: List[List[inputVarAndScaledInputVar]] = List.fill(numberOfRows)(generateRowOfInputs)
    val listOfOutputVars: List[Int] = listOfInputVars.map(generateOutputValue(_, generateErrorTerm))
    val listOfRows: List[List[Int]] = (listOfInputVars zip listOfOutputVars).map(generateRow(_, _))
    listOfRows

  def writeRowsToCSV(listOfRows:List[List[Int]]):Unit =
    val writer = new PrintWriter(new File(fileName))

    writer.write(headers)
    writer.write("\n")

    for (list <- listOfRows) {
      writer.write(list.mkString(","))
      writer.write("\n")
    }
    writer.close
  def generateManyRowsAndWriteToCSV:Unit =
    val listOfRows = generateManyInputRows
    writeRowsToCSV(listOfRows)

  
}
