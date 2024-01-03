public class Fraction {
    private long numerator;
    private long denominator;
    public Fraction(long numerator, long denominator) {
        this.setNumerator(numerator);
        this.setDenominator(denominator);
        //this.cancel();
    }
    /*
    public void cancel() // simplifies the fraction

    {
        long gcd = gcd(this.getNumerator(), this.getDenominator());
        this.setNumerator(this.getNumerator() / gcd);
        this.setDenominator(this.getDenominator() / gcd);
    }

     */

    // calculate the GCD to simplify a fraction
    private static long gcd(long n, long m) {
        long gcd;
        if (n == m)
            gcd = n;
        else {
            while (n != m) {
                if (n < m)
                    m = m - n;
                else
                    n = n - m;
            }
        }
        return n;
    }
    // getters and setters
    public long getNumerator() {
        return numerator;
    }

    public void setNumerator(long numerator) {
        this.numerator = numerator;
    }

    public long getDenominator() {
        return denominator;
    }

    public void setDenominator(long denominator) {
        this.denominator = denominator;
    }
    // Operation needed for the combinations class
    public Fraction multiply(Fraction other) { // Creating a new fraction from two fractions
        return new Fraction(this.numerator * other.numerator, this.denominator * other.denominator);
    }

    @Override
    public String toString() {
        return this.numerator + "/" + this.denominator;
    }



}
