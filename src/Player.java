import java.util.Scanner;

public class Player {
    private Card[] hand;

    public Player() {
        hand = new Card[2]; // Player's hand (two cards in Texas Hold'em)
    }

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

    @Override
    public String toString() { //Change the hand to two string
        String handString = "Hand: ";
        if (hand[0] != null) {
            handString += hand[0].toString();
        }
        if (hand[1] != null) {
            handString += ", " + hand[1].toString();
        }
        return handString;
    }

}