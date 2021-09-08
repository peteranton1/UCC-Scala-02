package com.example.testing

import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.propspec.AnyPropSpec
import org.scalatest.refspec.RefSpec
import org.scalatest.wordspec.AnyWordSpec

object ScalaTestingStyles

// JUnit style
class CalculatorSuite extends AnyFunSuite {

  val calculator = new Calculator

  test("multiplication by 0 " +
    "should always be 0") {
    assert(calculator.multiply(653278, 0) == 0)
    assert(calculator.multiply(-653278, 0) == 0)
    assert(calculator.multiply(0, 0) == 0)
  }

  test("dividing by zero should " +
    "throw some math error") {
    assertThrows[ArithmeticException](
      calculator.divide(653278, 0))
  }
}

// BDD
class CalculatorSpec extends AnyFunSpec {

  val calculator = new Calculator

  describe("multiplication") {
    it("should give back 0 if multiplying by 0") {
      assert(calculator.multiply(653278, 0) == 0)
      assert(calculator.multiply(-653278, 0) == 0)
      assert(calculator.multiply(0, 0) == 0)
    }
  }

  describe("division") {
    it("should throw a math error if dividing by 0") {
      assertThrows[ArithmeticException](
        calculator.divide(653278, 0))
    }
  }
}

class CalculatorWordSpec extends AnyWordSpec {
  val calculator = new Calculator

  "A calculator" should {
    "give back zero if multiplying by 0" in {
      assert(calculator.multiply(653278, 0) == 0)
      assert(calculator.multiply(-653278, 0) == 0)
      assert(calculator.multiply(0, 0) == 0)
    }
    "throw a math error if dividing by 0" in {
      assertThrows[ArithmeticException](
        calculator.divide(653278, 0))
    }
  }
}

class CalculatorFreeSpec extends AnyFreeSpec {
  val calculator = new Calculator

  "A calculator" - { // Anything you want
    "give back zero if multiplying by 0" in {
      assert(calculator.multiply(653278, 0) == 0)
      assert(calculator.multiply(-653278, 0) == 0)
      assert(calculator.multiply(0, 0) == 0)
    }
    "throw a math error if dividing by 0" in {
      assertThrows[ArithmeticException](
        calculator.divide(653278, 0))
    }
  }
}

// property style checking
class CalculatorPropSpec extends AnyPropSpec {
  val calculator = new Calculator

  val multiplyByZeroExamples = List(
    (653278, 0),
    (-653278, 0),
    (0, 0)
  )

  property("calculator multiply by 0 should be 0") {
      assert(multiplyByZeroExamples.forall {
        case (a,b) => calculator.multiply(a, b) == 0
      })
    }

  property("calculator dividing by 0 " +
    "should throw some math error") {
    assertThrows[ArithmeticException](
      calculator.divide(653278, 0))
  }
}

// based on reflection
class CalculatorRefSpec extends RefSpec {
  object `A calculator` {
    // test suite
    val calculator = new Calculator

    def `multiply by 0 should be 0`(): Unit = {
      assert(calculator.multiply(653278, 0) == 0)
      assert(calculator.multiply(-653278, 0) == 0)
      assert(calculator.multiply(0, 0) == 0)
    }

    def `should throw a math error when dividing by 0`(): Unit = {
      assertThrows[ArithmeticException](
        calculator.divide(653278, 0))
    }
  }

}


class Calculator {
  def add(a: Int, b: Int): Int = a + b

  def subtract(a: Int, b: Int): Int = a - b

  def multiply(a: Int, b: Int): Int = a * b

  def divide(a: Int, b: Int): Int = a / b
}