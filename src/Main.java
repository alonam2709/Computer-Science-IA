import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner clientinput = new Scanner(System.in);
        Deck gamedeck = new Deck(); // Initialize the deck
        Player client = new Player();
        Table table = new Table();

        // Input player's hand
        client.inputHand(clientinput, gamedeck);

        // Input community cards
        table.inputCommunityCards(clientinput, gamedeck);

        // Initialize ProbabilityCalculator with the latest data
        ProbabilityCalculator calculator = new ProbabilityCalculator(client, table, gamedeck);

        // For example, print out the probabilities:
        System.out.println(calculator);
        clientinput.close(); // Close the scanner when done
    }
}
