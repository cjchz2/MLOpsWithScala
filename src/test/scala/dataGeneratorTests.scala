import org.scalatest.flatspec.AnyFlatSpec
import org.mockito.ArgumentCaptor.forClass
import org.mockito.{ArgumentCaptor, InjectMocks, Mock}
import dataGeneratorPackage.*
import dataGeneratorPackage.dataGeneratorCaseClasses.*
import org.mockito.Mockito.*

import java.lang.IllegalArgumentException
import org.apache.commons.math3

// How to set a fake values for primatives?
class tests extends AnyFlatSpec {
  val dataGenerationParmMock = mock(classOf[dataGenerationParameters])
  val errorTermParmMock = mock(classOf[errorTermParms])
  var totalNumberOfRowsDumbVal = 10
  val headersDumbVal = "column1, column2, column3"
  val filePathDumbVal = "dumbFilePath"
  val basicFileDumbVal = "dumbFileName"

  val testUniformDistVarOne = uniformDistributionVariableParameters(sqft(), 0, 1, 1000)
  val testUniformDistVarTwo = uniformDistributionVariableParameters(schoolRating(), 5, 10, 3)
  val testUniformDistVarThree = uniformDistributionVariableParameters(numberOfBedRooms(), 20, 30, 2)
  val testdataGenerationParmList = List(testUniformDistVarOne,testUniformDistVarTwo,testUniformDistVarThree)

  val testInputVarOne = inputVarAndScaledInputVar(sqft(), 10000, 5)
  val testInputVarTwo = inputVarAndScaledInputVar(schoolRating(), 20000, 3)
  val testInputVarThree = inputVarAndScaledInputVar(numberOfBedRooms(), 30000, 2)
  val testInputVarList = List(testInputVarOne,testInputVarTwo,testInputVarThree)
  val testOutputVal = 250
  val testErrorTerm = 100

  val mockRandomDataGenerator = mock(classOf[randomDataGenerator])

  "setBatchSize" should "throw invalidBatchSize if batchSize smaller than 1" in {
    val dataGeneratorVal = dataGenerator(
      List(dataGenerationParmMock), errorTermParmMock, totalNumberOfRowsDumbVal,
      headersDumbVal, filePathDumbVal, basicFileDumbVal)
    assertThrows[invalidBatchSize] {
      dataGeneratorVal.setBatchSize(0)
    }
  }

  it should "change batchSize if batchSize larger than 1" in {
    val dataGeneratorVal = dataGenerator(
      List(dataGenerationParmMock), errorTermParmMock, totalNumberOfRowsDumbVal,
      headersDumbVal, filePathDumbVal, basicFileDumbVal)
    dataGeneratorVal.setBatchSize(10)
    assert(dataGeneratorVal.getBatchSize == 10)
  }

  "Passing totalNumberOfRows into constructor" should "throw illegalArgumentException when given number of rows smaller than zero " in {
    assertThrows[IllegalArgumentException] {
      val dataGeneratorVal = dataGenerator(
        List(dataGenerationParmMock), errorTermParmMock, 0,
        headersDumbVal, filePathDumbVal, basicFileDumbVal)
    }
  }

  "setTotalNumberOfRows" should "throw invalidRowSize exception when given number of rows smaller than zero " in {
    val dataGeneratorVal = dataGenerator(
      List(dataGenerationParmMock), errorTermParmMock, totalNumberOfRowsDumbVal,
      headersDumbVal, filePathDumbVal, basicFileDumbVal)
    assertThrows[invalidRowSize] { // Result type: Assertion
      dataGeneratorVal.setTotalNumberOfRows(0)
    }
  }
  it should "change the number totalNumberOfRows when given Int larger than one" in {
    val dataGeneratorVal = dataGenerator(
      List(dataGenerationParmMock), errorTermParmMock, totalNumberOfRowsDumbVal,
      headersDumbVal, filePathDumbVal, basicFileDumbVal)
    dataGeneratorVal.setTotalNumberOfRows(100)
    assert(dataGeneratorVal.totalNumberOfRows==100)
  }

  "generateNormalDistInteger" should "call nextGaussian and return result" in {
    val mockRandomDataGenerator = mock(classOf[randomDataGenerator])
      when(mockRandomDataGenerator.nextGaussian(0,1)).thenReturn(10000.00)

    val dataGeneratorTest = dataGenerator(
      List(dataGenerationParmMock), errorTermParmMock, totalNumberOfRowsDumbVal,
      headersDumbVal, filePathDumbVal, basicFileDumbVal, mockRandomDataGenerator)

    assert(dataGeneratorTest.generateNormalDistInteger(0,1) == 10000)
  }
  it should "pass mean and standard deviation to nextGaussian" in {
    val mockRandomDataGenerator = mock(classOf[randomDataGenerator])
    val meanCaptor = ArgumentCaptor.forClass(classOf[Int])
    val stdDevCaptor = ArgumentCaptor.forClass(classOf[Int])

    val dataGeneratorTest = dataGenerator(
      List(dataGenerationParmMock), errorTermParmMock, totalNumberOfRowsDumbVal,
      headersDumbVal, filePathDumbVal, basicFileDumbVal, mockRandomDataGenerator)

    dataGeneratorTest.generateNormalDistInteger(11,3)
    verify(mockRandomDataGenerator).nextGaussian(meanCaptor.capture(), stdDevCaptor.capture())

    assert(meanCaptor.getValue.equals(11.0) & stdDevCaptor.getValue.equals(3.0))
  }
  "generateUniformDistInteger" should "call nextUniform and return result" in {
    val mockRandomDataGenerator = mock(classOf[randomDataGenerator])
    when(mockRandomDataGenerator.nextUniform(0,1)).thenReturn(10000.00)

    val dataGeneratorTest = dataGenerator(
      List(dataGenerationParmMock), errorTermParmMock, totalNumberOfRowsDumbVal,
      headersDumbVal, filePathDumbVal, basicFileDumbVal, mockRandomDataGenerator)

    assert(dataGeneratorTest.generateUniformDistInteger(0,1) == 10000)
  }
  it should "pass lower bound and upper bound to nextUniform" in {
    val mockRandomDataGenerator = mock(classOf[randomDataGenerator])
    val lowerBoundCaptor = ArgumentCaptor.forClass(classOf[Int])
    val upperBoundCaptor = ArgumentCaptor.forClass(classOf[Int])

    val dataGeneratorTest = dataGenerator(
      List(dataGenerationParmMock), errorTermParmMock, totalNumberOfRowsDumbVal,
      headersDumbVal, filePathDumbVal, basicFileDumbVal, mockRandomDataGenerator)

    dataGeneratorTest.generateUniformDistInteger(11,3)
    verify(mockRandomDataGenerator).nextUniform(lowerBoundCaptor.capture(), upperBoundCaptor.capture())

    assert(lowerBoundCaptor.getValue.equals(11.0) & upperBoundCaptor.getValue.equals(3.0))
  }

  "generateInputVarAndScaledInputVar" should "generate uniform distributed variable when passed a uniformDistributionVariableParameters" in {
    val testUniformDistVar = uniformDistributionVariableParameters(sqft(), 0, 1, 1000)
    val mockRandomDataGenerator = mock(classOf[randomDataGenerator])
    when(mockRandomDataGenerator.nextUniform(0,1)).thenReturn(10000.00)

    val dataGeneratorTest = dataGenerator(
      List(dataGenerationParmMock), errorTermParmMock, totalNumberOfRowsDumbVal,
      headersDumbVal, filePathDumbVal, basicFileDumbVal, mockRandomDataGenerator)

    val testInputVarAndScaledInputVar = dataGeneratorTest.generateInputVarAndScaledInputVar(testUniformDistVar)
    println(testInputVarAndScaledInputVar)

    assert(testInputVarAndScaledInputVar.inputVar == 10000)
  }
  "generateInputVarAndScaledInputVar" should "generate appropriately scaled variable when passed a uniformDistributionVariableParameters" in {
    val testUniformDistVar = uniformDistributionVariableParameters(sqft(), 0, 1, 1000)
    val mockRandomDataGenerator = mock(classOf[randomDataGenerator])
    when(mockRandomDataGenerator.nextUniform(0,1)).thenReturn(10000.00)

    val dataGeneratorTest = dataGenerator(
      List(dataGenerationParmMock), errorTermParmMock, totalNumberOfRowsDumbVal,
      headersDumbVal, filePathDumbVal, basicFileDumbVal, mockRandomDataGenerator)

    val testInputVarAndScaledInputVar = dataGeneratorTest.generateInputVarAndScaledInputVar(testUniformDistVar)
    assert(testInputVarAndScaledInputVar.scaledInputVar == 10000000)
  }
  "generateOutputValue" should "Should sum scaled input vars and error term" in {
    val mockRandomDataGenerator = mock(classOf[randomDataGenerator])
    when(mockRandomDataGenerator.nextUniform(0,1)).thenReturn(10000.00)
    when(mockRandomDataGenerator.nextUniform(5,10)).thenReturn(20000.00)
    when(mockRandomDataGenerator.nextUniform(20,30)).thenReturn(30000.00)

    val dataGeneratorTest = dataGenerator(
      List(dataGenerationParmMock), errorTermParmMock, totalNumberOfRowsDumbVal,
      headersDumbVal, filePathDumbVal, basicFileDumbVal, mockRandomDataGenerator)

    assert(dataGeneratorTest.generateOutputValue(testInputVarList, testErrorTerm) == 5 + 3 + 2 + 100)
  }
  "generateRow" should "Should create list of unscaled input vals and output val" in {
    val mockRandomDataGenerator = mock(classOf[randomDataGenerator])
    when(mockRandomDataGenerator.nextUniform(0,1)).thenReturn(10000.00)
    when(mockRandomDataGenerator.nextUniform(5,10)).thenReturn(20000.00)
    when(mockRandomDataGenerator.nextUniform(20,30)).thenReturn(30000.00)

    val dataGeneratorTest = dataGenerator(
      List(dataGenerationParmMock), errorTermParmMock, totalNumberOfRowsDumbVal,
      headersDumbVal, filePathDumbVal, basicFileDumbVal, mockRandomDataGenerator)

    val outputRow = dataGeneratorTest.generateRow(testInputVarList, testOutputVal)
    assert(outputRow == List(10000,20000,30000,250))
  }
  "generateBatchOfRows" should "create an appropriately sized listOfRows" in {
    val mockRandomDataGenerator = mock(classOf[randomDataGenerator])
    when(mockRandomDataGenerator.nextUniform(0,1)).thenReturn(10000.00)
    when(mockRandomDataGenerator.nextUniform(5,10)).thenReturn(20000.00)
    when(mockRandomDataGenerator.nextUniform(20,30)).thenReturn(30000.00)
    when(mockRandomDataGenerator.nextGaussian(0,1)).thenReturn(30000.00)


    val dataGeneratorTest = dataGenerator(
      testdataGenerationParmList, errorTermParmMock, totalNumberOfRowsDumbVal,
      headersDumbVal, filePathDumbVal, basicFileDumbVal, mockRandomDataGenerator)

    val outputRow = dataGeneratorTest.generateRow(testInputVarList, testOutputVal)
    assert(outputRow == List(10000,20000,30000,250))
  }
//  "writeRowsToCSV" should "do soemthing" in {
//    @Mock
//    val mockRandomDataGeneratorWriter = mock(classOf[randomDataGeneratorWriter])
//    val mockRandomDataGenerator = mock(classOf[randomDataGenerator])
//    val listOfRows = List(List(1,2,3), List(4,5,6))
//    verify(mockRandomDataGeneratorWriter, times(1)).write(headersDumbVal)
//
//    @InjectMocks
//    val dataGeneratorTest = dataGenerator(
//      testdataGenerationParmList, errorTermParmMock, totalNumberOfRowsDumbVal,
//      headersDumbVal, filePathDumbVal, basicFileDumbVal, mockRandomDataGenerator)
//
//    dataGeneratorTest.writeRowsToCSV(listOfRows, mockRandomDataGeneratorWriter)
//    println(verify(mockRandomDataGeneratorWriter, times(1)).write(headersDumbVal))
//
//  }
}

