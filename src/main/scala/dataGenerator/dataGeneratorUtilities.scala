package dataGenerator

import dataGenerator.dataGeneratorCaseClasses.*
import org.apache.commons.math3

import java.io.{File, PrintWriter}

object dataGeneratorUtilities extends App {

  def generateNormalDistInteger(mean: Int, stdDev: Int): Int =
    math3.distribution.NormalDistribution(mean, stdDev).sample.toInt

  def generateUniformDistInteger(lowerBound: Int, upperBound: Int): Int =
    math3.distribution.UniformIntegerDistribution(lowerBound, upperBound).sample

  def generateInputVarAndScaledInputVar(dataGenerationValues: dataGenerationParameters): inputVarAndScaledInputVar =
    dataGenerationValues match
      case uniformDistributionVariableParameters(name, lowerBound, upperBound, coefficient) =>
        val inputVar = generateUniformDistInteger(lowerBound, upperBound)
        inputVarAndScaledInputVar(name, inputVar, inputVar * coefficient)

  def generateErrorTerm(errorParmsVal: errorTermParms): Int =
    generateNormalDistInteger(errorParmsVal.mean, errorParmsVal.stdDev)

  def generateRowOfInputs(parmList: List[dataGenerationParameters]): List[inputVarAndScaledInputVar] =
    parmList.map(generateInputVarAndScaledInputVar)

  def generateOutputValue(inputVals: List[inputVarAndScaledInputVar], errorTerm: Int): Int =
    inputVals.map(_.scaledInputVar).sum + errorTerm

  def generateRow(inputVals: List[inputVarAndScaledInputVar], outputVal: Int): List[Int] =
    val inputList = inputVals.map(_.inputVar)
    inputList :+ outputVal

  def generateManyInputRows(distributionParmList: List[dataGenerationParameters], errorTermParmsVal: errorTermParms, numberOfRows: Int): List[List[Int]] =
    {val listOfInputVars: List[List[inputVarAndScaledInputVar]] = List.fill(numberOfRows)(generateRowOfInputs(distributionParmList))
    val listOfOutputVars: List[Int] = listOfInputVars.map(generateOutputValue(_, generateErrorTerm(errorTermParmsVal)))
    val listOfRows: List[List[Int]] = (listOfInputVars zip listOfOutputVars).map(generateRow(_, _))
    listOfRows}

  def writeRowsToCSV(listOfRows:List[List[Int]], headers:String,  fileName:String)={
    val writer = new PrintWriter(new File(fileName))

    writer.write(headers)
    writer.write("\n")

    for (list <- listOfRows) {
      writer.write(list.mkString(","))
      writer.write("\n")
    }
    writer.close}
  def generateManyRowsAndWriteToCSV(distributionParmList: List[dataGenerationParameters],
                                    errorTermParmsVal: errorTermParms, numberOfRows: Int,
                                    headers:String,  fileName:String
                                   ):Unit = {
    val listOfRows:List[List[Int]]=  generateManyInputRows(distributionParmList, errorTermParmsVal, numberOfRows)
    writeRowsToCSV(listOfRows, headers, fileName)
  }
  
}
