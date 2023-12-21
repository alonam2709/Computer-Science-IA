import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Deck deck = new Deck(); // Initialize the deck
        Player player = new Player();
        Table table = new Table();

        // Input player's hand
        player.inputHand(scanner, deck);

        // Input community cards
        table.inputCommunityCards(scanner, deck);

        // Initialize ProbabilityCalculator with the latest data
        ProbabilityCalculator calculator = new ProbabilityCalculator(player, table, deck);

        // Now you can use the calculator to compute probabilities or other game logic
        // For example, print out the probabilities:
        System.out.println(calculator); // Implicitly calls calculator.toString()A
        scanner.close(); // Close the scanner when done
    }
}
