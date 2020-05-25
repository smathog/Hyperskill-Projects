package flashcards;

public class Main {
    public static void main(String[] args) {
        generateCard("purchase", "buy");
    }
    
    private static void generateCard(String front, String back) {
        System.out.println("Card: ");
        System.out.println(front);
        System.out.println("Definition: ");
        System.out.println(back);
    }
}
