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

    public int countCardsWithValue(Card.Value value) {
        int count = 0;
        for (int i = 0; i < cardCount; i++) {
            if (cards[i].getCardValue() == value) {
                count++;
            }
        }
        return count;
    }

    public int countCardsOfSuit(Card.Suit suit) {
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

    public int countCardsOfValueAndSuit(Card.Value value, Card.Suit suit) {
        int count = 0;
        for (int i = 0; i < cardCount; i++) {
            if (cards[i].getCardValue() == value && cards[i].getCardSuit() == suit) {
                count++;
            }
        }
        return count;
    }

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


}

/*
public class Deck {
    private final static int MAX_CARDS = 52; // Maximum number of cards in a deck
    private Card[] cards; // Aggregation of Card objects
    private int cardCount; // Number of cards currently in the deck

    public Deck() {
        cards = new Card[MAX_CARDS]; // Initialize the array to hold the maximum number of cards
        initializeDeck();
    }

    private void initializeDeck() {
        // Populate the deck with cards of each suit and value
        int index = 0;
        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Value value : Card.Value.values()) {
                cards[index++] = new Card(value, suit);
            }
        }
        cardCount = MAX_CARDS; // All cards are now in the deck
    }

    public Card[] getCards() {
        return cards;
    }

    public void setCards(Card[] cards) {
        this.cards = cards;
        this.cardCount = cards.length; // Update card count accordingly
    }

    public boolean addCard(Card card) {
        if (cardCount < MAX_CARDS) {
            cards[cardCount++] = card; // Add the card and increment the count
            return true;
        }
        System.out.println("Deck is full. Cannot add more cards.");
        return false;
    }

    public boolean removeCard(Card card) {
        int index = findCardIndex(card);
        if (index != -1) {
            // Move the last card in the deck to the place of the removed card to maintain continuous array
            cards[index] = cards[cardCount - 1]; // Move last card to the removed card's place
            cards[cardCount - 1] = null; // Nullify the last position
            cardCount--; // Decrement the count of cards in the deck
            return true;
        }
        return false; // Card was not found in the deck
    }

    private int findCardIndex(Card card) {
        if (card == null) {
            return -1; // Early return if the card to find is null
        }

        for (int i = 0; i < cardCount; i++) {
            if (cards[i] != null && cards[i].equals(card)) {
                return i;
            }
        }
        return -1; // Card not found
    }

    public int remainingCards() {
        return MAX_CARDS - cardCount; // Calculate remaining cards
    }

    public int countCardsWithValue(Card.Value value) {
        int count = 0;
        for (int i = 0; i < cardCount; i++) {
            if (cards[i].getCardValue() == value) {
                count++;
            }
        }
        return count;
    }

    public int countCardsOfSuit(Card.Suit suit) {
        int count = 0;
        for (int i = 0; i < cardCount; i++) {
            if (cards[i].getCardSuit() == suit) {
                count++;
            }
        }
        return count;
    }

    public boolean containsCard(Card card) {
        // Validate the input card is not null
        if (card == null) {
            return false;
        }

        for (int i = 0; i < cardCount; i++) {
            // Check if current card in the deck matches the card we're looking for
            if (cards[i] != null && cards[i].equals(card)) {
                return true; // Found the card in the deck
            }
        }
        return false; // Card not found in the deck
    }

    public int countCardsOfValueAndSuit(Card.Value value, Card.Suit suit) {
        int count = 0;
        for (int i = 0; i < cardCount; i++) {
            if (cards[i].getCardValue() == value && cards[i].getCardSuit() == suit) {
                count++;
            }
        }
        return count;
    }

 */
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