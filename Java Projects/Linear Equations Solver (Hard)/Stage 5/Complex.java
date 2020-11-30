package solver;

import java.util.regex.*;

public class Complex {
    //Static stuff
    private static final Pattern parsePattern = Pattern.compile("((-?\\d+(\\.\\d+)?)(\\+|-)((\\d+(\\.\\d+)?)?i))|(-?(\\d+(\\.\\d+)?)?i)|(-?\\d+(\\.\\d+)?)");
    private static final Pattern imaginaryPartPattern = Pattern.compile("(\\+|-)?(\\d+(\\.\\d+)?)?(?=i)");
    public static final Complex ONE = new Complex(1, 0);
    public static final Complex ZERO = new Complex(0, 0);

    //Instance fields
    private final double real;
    private final double imaginary;

    public Complex add(Complex other) {
        return new Complex(this.real + other.real, this.imaginary + other.imaginary);
    }

    public Complex subtract(Complex other) {
        return new Complex(this.real - other.real, this.imaginary - other.imaginary);
    }

    public Complex conjugate() {
        return new Complex(this.real, -this.imaginary);
    }

    public Complex multiply(Complex other) {
        return new Complex(this.real * other.real - this.imaginary * other.imaginary, this.imaginary * other.real + this.real * other.imaginary);
    }

    public Complex divide(Complex other) {
        return multiply(other.conjugate()).scalarMultiply(1 / (Math.pow(other.real, 2) + Math.pow(other.imaginary, 2)));
    }

    public Complex scalarMultiply(double scalar) {
        return new Complex(this.real * scalar, this.imaginary * scalar);
    }

    @Override
    public String toString() {
        if (real != 0d) {
            if (imaginary != 0d) {
                if (imaginary == 1d)
                    return real + "+i";
                else if (imaginary == -1d)
                    return real + "-i";
                else
                    return Double.toString(real) + (imaginary < 0d ? Double.toString(imaginary) + "i" : "+" + Double.toString(imaginary) + "i");
            } else
                return Double.toString(real);
        } else {
            if (imaginary != 0d) {
                if (imaginary == 1d)
                    return "i";
                else if (imaginary == -1d)
                    return "-i";
                else
                    return imaginary + "i";
            } else
                return "0";
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this)
            return true;
        else if (other.getClass() != this.getClass())
            return false;
        else {
            Complex o = (Complex) other;
            return o.real == this.real && o.imaginary == this.imaginary;
        }
    }

    private Complex(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public Complex(Complex other) {
        this(other.real, other.imaginary);
    }

    public static Complex parseComplex(String complex) throws NumberFormatException {
        if(!parsePattern.matcher(complex).matches())
            throw new NumberFormatException(complex + " is not well-formed complex number!");
        double imaginaryPart;
        double realPart;
        Matcher imMatcher = imaginaryPartPattern.matcher(complex);
        if (imMatcher.find()) {
            String imaginary = imMatcher.group();
            //Only captured an operator +/-, but really represents 1
            if (imaginary.length() == 1 || imaginary.length() == 0)
                imaginary += '1';
            System.out.println("String: " + complex + "Imaginary String: " + imaginary);
            imaginaryPart = Double.parseDouble(imaginary);
            String real = complex.substring(0, imMatcher.start());
            if (real.isEmpty())
                realPart = 0d;
            else
                realPart =  Double.parseDouble(real);
        } else {
            //No match - so no imaginary part!
            imaginaryPart = 0d;
            realPart = Double.parseDouble(complex);
        }
        return new Complex(realPart, imaginaryPart);
    }
}
