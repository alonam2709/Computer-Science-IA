import java.util.Scanner;
public class Table {
    private Card[] communityCards;
    private int cardCount;
    private RoundState roundState;

    public Table() {
        setCommunityCards(new Card[5]); // Maximum of 5 community cards
        cardCount = 0;
        setRoundState(RoundState.PRE_FLOP); // first case
    }

    public Card[] getCommunityCards() {
        return communityCards;
    }

    public void setCommunityCards(Card[] communityCards) {
        this.communityCards = communityCards;
    }

    public RoundState getRoundState() {
        return roundState;
    }

    public void setRoundState(RoundState roundState) {
        this.roundState = roundState;
    }

    public enum RoundState {
        PRE_FLOP, FLOP, TURN, RIVER
    }

    private void updateRoundState() {
        switch (getCommunityCards().length) {
            case 0:
                setRoundState(RoundState.PRE_FLOP);
                break;
            case 3:
                setRoundState(RoundState.FLOP);
                break;
            case 4:
                setRoundState(RoundState.TURN);
                break;
            case 5:
                setRoundState(RoundState.RIVER);
                break;
        }
    }
    public void addCommunityCard(Card card) {
        if (cardCount < getCommunityCards().length) {
            getCommunityCards()[cardCount++] = card;
            updateRoundState();
        }
    }
    public void inputCommunityCards(Scanner scanner, Deck deck) {
        System.out.println("Enter community cards (up to 5):");

        // Loop until up to 5 cards are entered or the user decides to stop entering cards
        while (cardCount < 5) {
            System.out.println("Enter card " + (cardCount + 1) + " (format: VALUE SUIT, e.g., ACE HEARTS), or type 'done' to finish:");
            String cardInput = scanner.nextLine().trim().toUpperCase(); //for processing, removing any white space or extra space in input

            // Check if the user decides to stop entering more cards
            if ("DONE".equals(cardInput)) {
                break;
            }

            try {
                // Split the input into value and suit
                String[] parts = cardInput.split(" ");
                Card.Value value = Card.Value.valueOf(parts[0]);
                Card.Suit suit = Card.Suit.valueOf(parts[1]);
                Card card = new Card(value, suit);

                // Attempt to remove the card from the deck and add it to the community cards
                if (deck.removeCard(card)) {
                    communityCards[cardCount++] = card;
                    updateRoundState(); // Update the round state based on the number of community cards
                } else {
                    System.out.println("Card not available or already taken. Please enter a different card.");
                }
            } catch (Exception e) {
                // Handle invalid input (either format or non-existent card value/suit)
                System.out.println("Invalid card input. Please try again.");
            }
        }
    }

    @Override
    public String toString() {
        String result = "Round State: " + roundState + "\nCommunity Cards: ";

        if (cardCount == 0) {
            result += "None";
        } else {
            for (int i = 0; i < cardCount; i++) {
                result += communityCards[i];
                if (i < cardCount - 1) {
                    result += ", ";
                }
            }
        }

        return result;
    }



}
