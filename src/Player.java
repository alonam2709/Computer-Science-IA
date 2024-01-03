import java.util.Scanner;

public class Player {
    public Card[] hand;
    private final static int HAND_SIZE = 2;
    public Player() {
        hand = new Card[HAND_SIZE]; // Player's hand (two cards in Texas Hold'em)
    }
/*
    public void inputHand(Scanner scanner, Deck deck) {
        System.out.println("Enter your two cards:");
        int cardIndex = 0;
        while (cardIndex < HAND_SIZE) {
            try {
                System.out.printf("Enter card %d (format: VALUE SUIT, e.g., ACE HEARTS): ", cardIndex + 1);
                String[] input = scanner.nextLine().trim().toUpperCase().split("\\s+");

                if (input.length != 2) {
                    System.out.println("Invalid format. Please try again.");
                    continue;
                }

                Card.Value value = Card.Value.valueOf(input[0]);
                Card.Suit suit = Card.Suit.valueOf(input[1]);
                Card card = new Card(value, suit);

                if (!deck.containsCard(card)) {
                    System.out.println("Card not available or already taken. Please enter a different card.");
                    continue;
                }

                deck.removeCard(card); // Remove the card from the deck
                hand[cardIndex] = card; // Add the card to the player's hand
                cardIndex++; // Move to the next card

            } catch (IllegalArgumentException e) {
                System.out.println("Invalid card. Please check the input and try again.");
            }
        }
    }

 */


    public void inputHand(Scanner scanner, Deck deck) {
        System.out.println("Enter your two cards:");
        for (int i = 0; i < hand.length; i++) {
            System.out.println("Enter card " + (i + 1) + " (format: VALUE SUIT, e.g., ACE HEARTS):");
            try {
                String cardInput = scanner.nextLine().toUpperCase();
                String[] parts = cardInput.split(" ");
                Card.Value value = Card.Value.valueOf(parts[0]);
                Card.Suit suit = Card.Suit.valueOf(parts[1]);
                Card card = new Card(value, suit);

                if (deck.removeCard(card)) {
                    hand[i] = card;
                } else {
                    System.out.println("Card not available or already taken. Please enter a different card.");
                    i--; // Allow the player to re-enter the card
                }
            } catch (IllegalArgumentException e) { //throwing exception so that a Card has to be inputed
                System.out.println("Invalid card input. Please try again.");
                i--; // Allow the player to re-enter the card
            }
        }
    }

/*
    public void inputHand(Scanner scanner, Deck deck) {
        System.out.println("Enter your two cards:");
        for (int i = 0; i < hand.length; ) {
            try {
                System.out.println("Enter card " + (i + 1) + " (format: VALUE SUIT, e.g., ACE HEARTS):");
                String cardInput = scanner.nextLine().trim().toUpperCase();
                String[] parts = cardInput.split("\\s+");
                if (parts.length != 2) {
                    System.out.println("Invalid format. Please use the format VALUE SUIT.");
                    continue;
                }

                Card.Value value = Card.Value.valueOf(parts[0]);
                Card.Suit suit = Card.Suit.valueOf(parts[1]);
                Card card = new Card(value, suit);

                if (!deck.containsCard(card)) {
                    System.out.println("Card does not exist or already taken. Please enter a different card.");
                    continue;
                }

                if (deck.removeCard(card)) {
                    hand[i] = card;
                    i++; // Move to the next card only if successfully added
                } else {
                    System.out.println("Unexpected error removing card. Please enter a different card.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid card input or format. Please try again using the format VALUE SUIT.");
            }
        }
    }

 */
    /*
    public void inputHand(Scanner scanner, Deck deck) {
        System.out.println("Enter your two cards:");
        for (int i = 0; i < hand.length; i++) {
            while (true) {
                try {
                    System.out.println("Enter card " + (i + 1) + " (format: VALUE SUIT, e.g., ACE HEARTS):");
                    String cardInput = scanner.nextLine().trim().toUpperCase();
                    String[] parts = cardInput.split("\\s+");
                    if (parts.length != 2) {
                        throw new IllegalArgumentException("Invalid card format. Please use the format VALUE SUIT.");
                    }

                    Card.Value value = Card.Value.valueOf(parts[0]);
                    Card.Suit suit = Card.Suit.valueOf(parts[1]);
                    Card card = new Card(value, suit);

                    if (deck.removeCard(card)) {
                        hand[i] = card;
                        break; // Successfully added card, break out of the loop to move to next card
                    } else {
                        System.out.println("Card not available or already taken. Please enter a different card.");
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid card input or format. Please try again using the format VALUE SUIT.");
                }
            }
        }
    }

     */

    /*
    public double calculateHandRank() { //adds the ranks out of the two and divides it by the highest possible hand AA
        int rank = 0;
        int maxRankForSingleCard = 14; // Assuming Ace has the highest rank of 14
        int maxHandRank = maxRankForSingleCard * 2; // Maximum rank for a hand (two Aces)
        for (int i = 0; i < hand.length; i++) { // for loop to go through the two hands
            Card card = hand[i];
            if (card != null) {
                switch (card.getCardValue()) { //switch statements for the enum
                    case ACE:
                        rank += 14;
                        break;
                    case KING:
                        rank += 13;
                        break;
                    case QUEEN:
                        rank += 12;
                        break;
                    case JACK:
                        rank += 11;
                        break;
                    case TEN:
                        rank += 10;
                        break;
                    case NINE:
                        rank += 9;
                        break;
                    case EIGHT:
                        rank += 8;
                        break;
                    case SEVEN:
                        rank += 7;
                        break;
                    case SIX:
                        rank += 6;
                        break;
                    case FIVE:
                        rank += 5;
                        break;
                    case FOUR:
                        rank += 4;
                        break;
                    case THREE:
                        rank += 3;
                        break;
                    case TWO:
                        rank += 2;
                        break;
                }
            }
        }

        return (double) rank / maxHandRank; //Normalizing the rank
    }

 */
@Override
    public String toString() { //Change the hand to two string
        String handString = "Hand: ";
        if (hand[0] != null) {
            handString += hand[0].toString();
        }
        if (hand[1] != null) {
            handString += ", " + hand[1];
        }
        return handString;
    }

}