public class Deck {
    private Card[] cards; //aggregation
    private int cardCount; // population not the whole size of the array

    public Deck() {
        setCards(new Card[52]); // maximum of 52 cards in the array
        cardCount = 0; // intializing the card count
        // Initialize the deck within the constructor
        int index = 0;
        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Value value : Card.Value.values()) {
                getCards()[index++] = new Card(value, suit);
            }
        }
        cardCount = 52; // Set the card count to the full deck size
    }
    public Card[] getCards() {
        return cards;
    }

    public void setCards(Card[] cards) {
        this.cards = cards;
    }
    public void addCard(Card card) { // adding a card to the end of the deck
        if (cardCount < 52) {
            getCards()[cardCount] = card;
            cardCount++;
        } else {
            System.out.println("Deck is full. Cannot add more cards.");
        }
    }

    public boolean removeCard(Card card) { // removing a specific card
        int index = findCardIndex(card);
        if (index != -1) {
            for (int i = index; i < cardCount - 1; i++) {
                getCards()[i] = getCards()[i + 1];
            }
            cardCount--;
            return true;
        }
        return false; // invalidation
    }

    private int findCardIndex(Card card) { //linear search
        for (int i = 0; i < cardCount; i++) {
            if (getCards()[i].getCardValue() == card.getCardValue() && getCards()[i].getCardSuit() == card.getCardSuit()) {
                return i;
            }
        }
        return -1; // invalidation
    }
    public int remainingCards() { // returns the number of cards  that are still in the deck
        return 52 - cardCount;
    }

    public int countCardsWithValue(Card.Value value) { // returns the count of how many cards of a given value are left in the deck
        int count = 0;
        for (int i = 0; i < cardCount; i++) {
            if (getCards()[i].getCardValue() == value) {
                count++;
            }
        }
        return count;
    }
    /*
    @Override
    public String toString() {
        if (cardCount == 0) {
            return "Deck is empty.";
        }

        String deckString = "Deck: ";
        for (int i = 0; i < cardCount; i++) {
            deckString += getCards()[i];
            if (i < cardCount - 1) {
                deckString += ", ";
            }
        }
        return deckString;
    }

     */

    public int countCardsWithSuit(Card.Suit suit) {
        // Returns the count of how many cards of a given suit are left in the deck
        int count = 0;
        for (int i = 0; i < cardCount; i++) {
            if (cards[i].getCardSuit() == suit) {
                count++;
            }
        }
        return count;
    }
    public boolean containsCard(Card card) {
        // Check if the deck contains a specific card
        for (int i = 0; i < cardCount; i++) {
            if (cards[i].equals(card)) {
                return true;
            }
        }
        return false;
    }

}