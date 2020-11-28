package solver;

public class Solver {

    //Solves an equation of the form ax = b
    public static double linearSolver(double a, double b) {
        return b / a;
    }

    //Solves a system of two equations in two variables
    public static double[] linear22Solver(double a, double b, double c, double d, double e, double f) {
        final double y = (f - c * d  / a) / (e - b * d / a);
        final double x = (c - b * y) / a;
        return new double[]{x, y};
    }

}
