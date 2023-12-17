public class Deck {
    private Card[] cards; //aggregation
    private int cardCount; // population not the whole size of the array

    public Deck() {
        cards = new Card[52]; // maximum of 52 cards in the array
        cardCount = 0; // intializing the card count
        // Initialize the deck within the constructor
        int index = 0;
        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Value value : Card.Value.values()) {
                cards[index++] = new Card(value, suit);
            }
        }
        cardCount = 52; // Set the card count to the full deck size
    }
    public void addCard(Card card) { // adding a card to the end of the deck
        if (cardCount < 52) {
            cards[cardCount] = card;
            cardCount++;
        } else {
            System.out.println("Deck is full. Cannot add more cards.");
        }
    }

    public boolean removeCard(Card card) { // removing a specific card
        int index = findCardIndex(card);
        if (index != -1) {
            for (int i = index; i < cardCount - 1; i++) {
                cards[i] = cards[i + 1];
            }
            cardCount--;
            return true;
        }
        return false; // invalidation
    }

    private int findCardIndex(Card card) { //linear search
        for (int i = 0; i < cardCount; i++) {
            if (cards[i].getCardValue() == card.getCardValue() && cards[i].getCardSuit() == card.getCardSuit()) {
                return i;
            }
        }
        return -1; // invalidation
    }

    @Override
    public String toString() {
        if (cardCount == 0) {
            return "Deck is empty.";
        }

        String deckString = "Deck: ";
        for (int i = 0; i < cardCount; i++) {
            deckString += cards[i];
            if (i < cardCount - 1) {
                deckString += ", ";
            }
        }
        return deckString;
    }
}