package processor;

public class Main {
    public static void main(String[] args) {
        intMatrix a = intMatrix.getMatrix();
        intMatrix b = intMatrix.getMatrix();
        try {
            intMatrix.add(a, b).printMatrix();
        } catch (IllegalArgumentException e) {
            System.out.println("ERROR");
        }
    }
}
