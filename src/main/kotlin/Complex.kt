import kotlin.math.*

class Complex(
    arg1: Double = 0.0,
    arg2: Double = 0.0,
    type: ComplexType = ComplexType.Cartesian
) : Comparable<Complex> {

    // not testable
    // useful for comparing doubles
    private val delta = 0.0001

    // the way that the user wants to use to create a Complex numbers
    enum class ComplexType {
        Cartesian,
        PolarDegree,
        PolarRadian
    }

    // the way that the user wants to sort Complex numbers
    enum class SortingState {
        Length,
        Phi,
        Real,
        Imaginary
    }

    // the direction of the rotation that the user wants to make
    enum class Direction(val rawValue: Int) {
        Positive(1),
        Negative(-1)
    }

    // class-level object, because it does not make sense for 1-1 Complex number
    companion object {
        var sortingState = SortingState.Length
    }

    // the real and imaginary part of the Complex number
    val real: Double
    val imaginary: Double

    init {
        when (type) {
            ComplexType.Cartesian -> {
                real = arg1
                imaginary = arg2
            }
            ComplexType.PolarDegree -> {
                require(arg1 >= 0) { "The length of a complex number cannot be negative, was ${arg1}!" }
                real = arg1 * cos(arg2.toRadian().normalize())
                imaginary = arg1 * sin(arg2.toRadian().normalize()) + if (real < 0) PI else 0.0
            }
            ComplexType.PolarRadian -> {
                require(arg1 >= 0) { "The length of a complex number cannot be negative, was ${arg1}!" }
                real = arg1 * cos(arg2.normalize())
                imaginary = arg1 * sin(arg2.normalize())
            }
        }
    }

    constructor(complex: Complex) : this(complex.real, complex.imaginary)

    val r: Double
        get() = sqrt(real * real + imaginary * imaginary)
    val abs: Double
        get() = r
    val phiInRadian: Double
        get() = atan(imaginary / real)
    val phiInDegree: Double
        get() = phiInRadian.toDegree()

    val conjugate
        get() = Complex(real, -imaginary)

    // steepness of the complex number
    // it is not very useful... but i want something over the basics
    val derivative = tan(phiInRadian)

    // area under the complex number
    // it is not very useful... but i want something over the basics
    val integral = real * imaginary / 2.0

    private fun Double.toRadian() = this / 180 * PI
    private fun Double.toDegree() = this * 180 / PI
    private fun Double.normalize() = this % (2 * PI)
    private fun Double.equality(other: Double) = abs(this - other) < delta

    override fun equals(other: Any?) =
        !(other == null || other !is Complex || !real.equality(other.real) || !imaginary.equality(other.imaginary))

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = (prime * result + real.hashCode() + imaginary.hashCode())
        return result
    }

    override fun toString() = printInCartesian()

    override operator fun compareTo(other: Complex): Int {
        val value1: Double
        val value2: Double
        when (sortingState) {
            SortingState.Length -> {
                value1 = r
                value2 = other.r
            }
            SortingState.Real -> {
                value1 = real
                value2 = other.real
            }
            SortingState.Imaginary -> {
                value1 = imaginary
                value2 = other.imaginary
            }
            SortingState.Phi -> {
                value1 = phiInRadian
                value2 = other.phiInRadian
            }
        }

        return when {
            (value1 - value2) < 0 -> {
                -1
            }
            (value1 - value2) > 0 -> {
                1
            }
            else -> {
                0
            }
        }
    }

    fun copy(
        arg1: Double = real,
        arg2: Double = imaginary,
        type: ComplexType = ComplexType.Cartesian
    ) = Complex(arg1, arg2, type)

    fun printInCartesian() = "Real part is $real, and imaginary part is $imaginary"
    fun printInPolarDegree() = "Length is $r, and angle is ${phiInRadian.toDegree()}"
    fun printInPolarRadian() = "Length is $r, and angle is $phiInRadian"

    operator fun unaryMinus() = Complex(-real, -imaginary)

    operator fun unaryPlus() = this

    // doesn't make sense
    // operator fun not() = ???

    operator fun inc() = this + 1.toComplex()

    operator fun dec() = this - 1.toComplex()

    operator fun component1() = real
    operator fun component2() = imaginary

    operator fun plus(increment: Complex) = Complex(real + increment.real, imaginary + increment.imaginary)
    operator fun plus(increment: Double) = plus(increment.toComplex())
    operator fun plus(increment: Float) = plus(increment.toComplex())
    operator fun plus(increment: Int) = plus(increment.toComplex())
    operator fun plus(increment: Long) = plus(increment.toComplex())
    operator fun plus(increment: Byte) = plus(increment.toComplex())
    operator fun plus(increment: Short) = plus(increment.toComplex())

    operator fun minus(decrement: Complex) = Complex(real - decrement.real, imaginary - decrement.imaginary)
    operator fun minus(decrement: Double) = minus(decrement.toComplex())
    operator fun minus(decrement: Float) = minus(decrement.toComplex())
    operator fun minus(decrement: Int) = minus(decrement.toComplex())
    operator fun minus(decrement: Long) = minus(decrement.toComplex())
    operator fun minus(decrement: Byte) = minus(decrement.toComplex())
    operator fun minus(decrement: Short) = minus(decrement.toComplex())

    operator fun times(multiplier: Complex) =
        Complex(r * multiplier.r, phiInRadian + multiplier.phiInRadian, ComplexType.PolarRadian)

    operator fun times(multiplier: Double) = times(multiplier.toComplex())
    operator fun times(multiplier: Float) = times(multiplier.toComplex())
    operator fun times(multiplier: Int) = times(multiplier.toComplex())
    operator fun times(multiplier: Long) = times(multiplier.toComplex())
    operator fun times(multiplier: Byte) = times(multiplier.toComplex())
    operator fun times(multiplier: Short) = times(multiplier.toComplex())

    operator fun div(divider: Complex) =
        Complex(r / divider.r, phiInRadian - divider.phiInRadian, ComplexType.PolarRadian)

    operator fun div(divider: Double) = div(divider.toComplex())
    operator fun div(divider: Float) = div(divider.toComplex())
    operator fun div(divider: Int) = div(divider.toComplex())
    operator fun div(divider: Long) = div(divider.toComplex())
    operator fun div(divider: Byte) = div(divider.toComplex())
    operator fun div(divider: Short) = div(divider.toComplex())

    // doesn't make sense
    // operator fun rem(rem: Complex)
    // operator fun rangeTo(max: Complex)
    // operator fun contains(other: Complex)
    // operator fun get(index: Int)
    // operator fun set(index: Int)
    // operator fun invoke(index: Int)

    // doesn't make sense (immutability)
    // operator fun plusAssign(other: Complex)
    // operator fun minusAssign(other: Complex)
    // operator fun timesAssign(other: Complex)
    // operator fun divAssign(other: Complex)
    // operator fun remAssign(other: Complex)

    fun power(exponent: Int) = Complex(r.pow(exponent), phiInRadian * exponent, ComplexType.PolarRadian)
    fun power(exponent: Long) = power(exponent.toInt())
    fun power(exponent: Short) = power(exponent.toInt())
    fun power(exponent: Byte) = power(exponent.toInt())

    fun sqrt(n: Int): Array<Complex> {
        var array = emptyArray<Complex>()
        val length = r.pow(1 / n.toDouble())
        for (k in 0 until n) {
            array = array.plus(Complex(length, phiInRadian / n + k * 2 * PI / n, ComplexType.PolarRadian))
        }
        return array
    }

    fun rotateByDegree(degree: Double, direction: Direction) = rotateByRadian(degree.toRadian(), direction)
    fun rotateByRadian(radian: Double, direction: Direction) =
        Complex(r, phiInRadian + direction.rawValue * radian, ComplexType.PolarRadian)
}

// these properties can be done with .i too, but i think it is enough, that would be just copy paste

fun Double.toComplex() = Complex(this)
val Double.j: Complex
    get() = Complex(0.0, this)

operator fun Double.plus(increment: Complex) = toComplex().plus(increment)
operator fun Double.minus(decrement: Complex) = toComplex().minus(decrement)
operator fun Double.times(multiplier: Complex) = toComplex().times(multiplier)
operator fun Double.div(divider: Complex) = toComplex().div(divider)

fun Float.toComplex() = Complex(toDouble())
val Float.j: Complex
    get() = Complex(0.0, toDouble())

operator fun Float.plus(increment: Complex) = toComplex().plus(increment)
operator fun Float.minus(decrement: Complex) = toComplex().minus(decrement)
operator fun Float.times(multiplier: Complex) = toComplex().times(multiplier)
operator fun Float.div(divider: Complex) = toComplex().div(divider)

fun Int.toComplex() = Complex(toDouble())
val Int.j: Complex
    get() = Complex(0.0, toDouble())

operator fun Int.plus(increment: Complex) = toComplex().plus(increment)
operator fun Int.minus(decrement: Complex) = toComplex().minus(decrement)
operator fun Int.times(multiplier: Complex) = toComplex().times(multiplier)
operator fun Int.div(divider: Complex) = toComplex().div(divider)

fun Long.toComplex() = Complex(toDouble())
val Long.j: Complex
    get() = Complex(0.0, toDouble())

operator fun Long.plus(increment: Complex) = toComplex().plus(increment)
operator fun Long.minus(decrement: Complex) = toComplex().minus(decrement)
operator fun Long.times(multiplier: Complex) = toComplex().times(multiplier)
operator fun Long.div(divider: Complex) = toComplex().div(divider)

fun Byte.toComplex() = Complex(toDouble())
val Byte.j: Complex
    get() = Complex(0.0, toDouble())

operator fun Byte.plus(increment: Complex) = toComplex().plus(increment)
operator fun Byte.minus(decrement: Complex) = toComplex().minus(decrement)
operator fun Byte.times(multiplier: Complex) = toComplex().times(multiplier)
operator fun Byte.div(divider: Complex) = toComplex().div(divider)

fun Short.toComplex() = Complex(toDouble())
val Short.j: Complex
    get() = Complex(0.0, toDouble())

operator fun Short.plus(increment: Complex) = toComplex().plus(increment)
operator fun Short.minus(decrement: Complex) = toComplex().minus(decrement)
operator fun Short.times(multiplier: Complex) = toComplex().times(multiplier)
operator fun Short.div(divider: Complex) = toComplex().div(divider)
