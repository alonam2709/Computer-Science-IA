public class ProbabilityCalculator {
    private Player player;
    private Table table;
    private Card[] playerHand;
    private Card[] communityCards;
    private Deck deck;

    public ProbabilityCalculator(Player player, Table table, Deck deck) {
        this.playerHand = player.hand;
        this.communityCards = table.getCommunityCards();
        this.deck = deck;
    }

    public Fraction calculateHandProbability(HandType handType) {
        switch(handType) {
            case HIGH_CARD:
                return calculateHighCardProbability();
            case PAIR:
                return calculatePairProbability();
            case TWO_PAIR:
                return calculateTwoPairProbability();
            case THREE_OF_A_KIND:
                return calculateThreeOfAKindProbability();
            case STRAIGHT:
                return calculateStraightProbability();
            case FLUSH:
               return calculateFlushProbability();
            case FULL_HOUSE:
                return calculateFullHouseProbability();
            case FOUR_OF_A_KIND:
                return calculateFourOfAKindProbability();
            case STRAIGHT_FLUSH:
                return calculateStraightFlushProbability();
            case ROYAL_FLUSH:
                return calculateRoyalFlushProbability();
            default:
                return new Fraction(0, 1); // Return 0/1 for unknown types
        }
    }

    private Fraction calculatePairProbability() {
        // Calculate the total number of possible outcomes from the remaining cards
        long totalPossibleOutcomes = Combination.calculateCombinations(deck.remainingCards(), 5 - communityCards.length);

        // Array to count occurrences of each card value in the player's hand and on the table
        int[] valueCounts = new int[Card.Value.values().length];
        for (Card card : playerHand) {
            valueCounts[Card.getValueIndex(card.getCardValue())]++;
        }
        for (Card card : communityCards) {
            valueCounts[Card.getValueIndex(card.getCardValue())]++;
        }

        // Count of favorable outcomes for getting a pair
        int favorableOutcomes = 0;

        // Loop through each card value to calculate the number of ways to draw a pair
        for (int i = 0; i < valueCounts.length; i++) {
            if (valueCounts[i] == 0) {
                // Count the remaining cards of the same value in the deck
                int sameValueCards = deck.countCardsWithValue(Card.Value.values()[i]);
                // Add the combinations to the favorable outcomes
                favorableOutcomes += Combination.calculateCombinations(sameValueCards, 1);
            }
        }

        // Correct for overcounting if the player already holds a pair
        if (playerHand[0].getCardValue() == playerHand[1].getCardValue()) {
            int sameValueCards = deck.countCardsWithValue(playerHand[0].getCardValue());
            favorableOutcomes -= Combination.calculateCombinations(sameValueCards - 2, 1);
        }

        // Return the probability of forming a pair as a fraction
        return new Fraction(favorableOutcomes, totalPossibleOutcomes);
    }

    private Fraction calculateTwoPairProbability() {
        // Calculate the total number of possible outcomes
        long totalPossibleOutcomes = Combination.calculateCombinations(deck.remainingCards(), 5 - communityCards.length);

        // Array to count occurrences of each card value in the player's hand and on the table
        int[] valueCounts = new int[Card.Value.values().length];
        for (Card card : playerHand) {
            valueCounts[Card.getValueIndex(card.getCardValue())]++;
        }
        for (Card card : communityCards) {
            valueCounts[Card.getValueIndex(card.getCardValue())]++;
        }

        // Count of favorable outcomes for getting exactly two pairs
        int favorableOutcomes = 0;

        // Calculate the number of ways to form two pairs
        for (int i = 0; i < valueCounts.length; i++) {
            if (valueCounts[i] == 1) {
                // Count how many cards of the same value are still in the deck
                int sameValueCards = deck.countCardsWithValue(Card.Value.values()[i]);
                // Add to favorable outcomes for each single card
                favorableOutcomes += Combination.calculateCombinations(sameValueCards, 1);
            }
        }

        // Adjust for overcounting if the player already has one pair
        for (int i = 0; i < valueCounts.length; i++) {
            if (valueCounts[i] == 2) {
                int sameValueCards = deck.countCardsWithValue(Card.Value.values()[i]);
                // Subtract combinations for the existing pair
                favorableOutcomes -= Combination.calculateCombinations(sameValueCards, 1);
            }
        }

        // Return the probability of forming exactly two pairs as a fraction
        return new Fraction(favorableOutcomes, totalPossibleOutcomes);
    }

    private Fraction calculateThreeOfAKindProbability() {
        // Calculate the total number of possible outcomes from the remaining cards
        long totalPossibleOutcomes = Combination.calculateCombinations(deck.remainingCards(), 5 - communityCards.length);

        // Array to count occurrences of each card value in the player's hand and on the table
        int[] valueCounts = new int[Card.Value.values().length];
        for (Card card : playerHand) {
            valueCounts[Card.getValueIndex(card.getCardValue())]++;
        }
        for (Card card : communityCards) {
            valueCounts[Card.getValueIndex(card.getCardValue())]++;
        }

        int favorableOutcomes = 0;

        // Loop through each card value to calculate the number of ways to draw three of a kind
        for (int i = 0; i < valueCounts.length; i++) {
            if (valueCounts[i] > 0) {
                int sameValueCards = deck.countCardsWithValue(Card.Value.values()[i]);
                if (valueCounts[i] == 1 && sameValueCards >= 2) {
                    // If one card is already in hand, need to draw 2 more
                    favorableOutcomes += Combination.calculateCombinations(sameValueCards, 2);
                } else if (valueCounts[i] == 2 && sameValueCards >= 1) {
                    // If two cards are already in hand, need to draw 1 more
                    favorableOutcomes += Combination.calculateCombinations(sameValueCards, 1);
                }
            }
        }

        // Return the probability of forming exactly three of a kind as a fraction
        return new Fraction(favorableOutcomes, totalPossibleOutcomes);
    }
    private Fraction calculateStraightProbability() {
        long totalPossibleOutcomes = Combination.calculateCombinations(deck.remainingCards(), 5 - communityCards.length);

        // Create an array to represent a simplified deck with just the card values
        boolean[] simplifiedDeck = new boolean[Card.Value.values().length];
        for (Card card : deck.getCards()) {
            simplifiedDeck[Card.getValueIndex(card.getCardValue())] = true;
        }

        // Mark cards in player's hand and community cards as unavailable
        for (Card card : playerHand) {
            simplifiedDeck[Card.getValueIndex(card.getCardValue())] = false;
        }
        for (Card card : communityCards) {
            simplifiedDeck[Card.getValueIndex(card.getCardValue())] = false;
        }

        int favorableOutcomes = 0;

        // Loop through the simplified deck to find possible straights
        for (int i = 0; i <= simplifiedDeck.length - 5; i++) {
            if (checkForStraightPossibility(simplifiedDeck, i)) {
                favorableOutcomes += countStraightCombinations(simplifiedDeck, i, deck);
            }
        }

        return new Fraction(favorableOutcomes, totalPossibleOutcomes);
    }

    private boolean checkForStraightPossibility(boolean[] deck, int start) {
        // Check if there's a potential straight starting at 'start'
        for (int i = start; i < start + 5; i++) {
            if (!deck[i]) {
                return false;
            }
        }
        return true;
    }

    private int countStraightCombinations(boolean[] deck, int start, Deck fullDeck) {
        // Count the number of combinations to complete a straight starting at 'start'
        int combinations = 1;
        for (int i = start; i < start + 5; i++) {
            if (!deck[i]) {
                combinations *= fullDeck.countCardsWithValue(Card.Value.values()[i]);
            }
        }
        return combinations;
    }

    private Fraction calculateFlushProbability() {
        // Calculate total possible outcomes from the remaining cards
        long totalPossibleOutcomes = Combination.calculateCombinations(deck.remainingCards(), 5 - communityCards.length);

        // Array to count occurrences of each suit in player's hand and community cards
        int[] suitCounts = new int[Card.Suit.values().length];
        for (Card card : playerHand) {
            suitCounts[card.getCardSuit().ordinal()]++;
        }
        for (Card card : communityCards) {
            suitCounts[card.getCardSuit().ordinal()]++;
        }

        int favorableOutcomes = 0;

        // Loop through each suit to calculate the number of ways to draw a flush
        for (int i = 0; i < suitCounts.length; i++) {
            if (suitCounts[i] > 0) {
                int remainingCardsOfSuit = deck.countCardsWithSuit(Card.Suit.values()[i]);
                int cardsNeededForFlush = 5 - suitCounts[i];
                if (cardsNeededForFlush <= remainingCardsOfSuit) {
                    // Calculate combinations of drawing the needed cards for a flush from the remaining cards of the same suit
                    favorableOutcomes += Combination.calculateCombinations(remainingCardsOfSuit, cardsNeededForFlush);
                }
            }
        }

        // Return the probability of forming a flush as a fraction
        return new Fraction(favorableOutcomes, totalPossibleOutcomes);
    }

    private Fraction calculateFullHouseProbability() {
        long totalPossibleOutcomes = Combination.calculateCombinations(deck.remainingCards(), 5 - communityCards.length);

        // Array to count occurrences of each card value in player's hand and community cards
        int[] valueCounts = new int[Card.Value.values().length];
        for (Card card : playerHand) {
            valueCounts[Card.getValueIndex(card.getCardValue())]++;
        }
        for (Card card : communityCards) {
            valueCounts[Card.getValueIndex(card.getCardValue())]++;
        }

        int favorableOutcomes = 0;

        // Loop through each card value to calculate the probability of full house
        for (int i = 0; i < valueCounts.length; i++) {
            for (int j = 0; j < valueCounts.length; j++) {
                if (i != j) {
                    long threeOfAKindCombinations = calculateCombinationsForThreeOfAKind(i, valueCounts);
                    long pairCombinations = calculateCombinationsForPair(j, valueCounts);

                    favorableOutcomes += threeOfAKindCombinations * pairCombinations;
                }
            }
        }

        return new Fraction(favorableOutcomes, totalPossibleOutcomes);
    }
//source?
    private long calculateCombinationsForThreeOfAKind(int valueIndex, int[] valueCounts) {
        int sameValueCards = deck.countCardsWithValue(Card.Value.values()[valueIndex]);
        if (valueCounts[valueIndex] == 1 && sameValueCards >= 2) {
            return Combination.calculateCombinations(sameValueCards, 2);
        } else if (valueCounts[valueIndex] == 2 && sameValueCards >= 1) {
            return Combination.calculateCombinations(sameValueCards, 1);
        }
        return valueCounts[valueIndex] == 3 ? 1 : 0;
    }
//Source
    private long calculateCombinationsForPair(int valueIndex, int[] valueCounts) {
        int sameValueCards = deck.countCardsWithValue(Card.Value.values()[valueIndex]);
        if (valueCounts[valueIndex] == 1 && sameValueCards >= 1) {
            // Cast sameValueCards to int as the number of cards will not exceed int range
            return Combination.calculateCombinations( sameValueCards, 1);
        }
        return valueCounts[valueIndex] >= 2 ? 1 : 0;
}
    private Fraction calculateFourOfAKindProbability() {
        long totalPossibleOutcomes = Combination.calculateCombinations(deck.remainingCards(), 5 - communityCards.length);

        // Array to count occurrences of each card value in player's hand and community cards
        int[] valueCounts = new int[Card.Value.values().length];
        for (Card card : playerHand) {
            valueCounts[Card.getValueIndex(card.getCardValue())]++;
        }
        for (Card card : communityCards) {
            valueCounts[Card.getValueIndex(card.getCardValue())]++;
        }

        int favorableOutcomes = 0;

        // Loop through each card value to calculate the number of ways to draw four of a kind
        for (int i = 0; i < valueCounts.length; i++) {
            if (valueCounts[i] > 0) {
                int remainingCardsOfValue = deck.countCardsWithValue(Card.Value.values()[i]);
                if (valueCounts[i] < 4) {
                    // Calculate combinations to draw the remaining cards needed for four of a kind
                    favorableOutcomes += Combination.calculateCombinations(remainingCardsOfValue, 4 - valueCounts[i]);
                }
            }
        }

        // Return the probability of forming exactly four of a kind as a fraction
        return new Fraction(favorableOutcomes, totalPossibleOutcomes);
    }
    private Fraction calculateStraightFlushProbability() {
        long totalPossibleOutcomes = Combination.calculateCombinations(deck.remainingCards(), 5 - communityCards.length);

        int favorableOutcomes = 0;

        // Check for straight flush in each suit
        for (Card.Suit suit : Card.Suit.values()) {
            favorableOutcomes += calculateStraightFlushForSuit(suit);
        }

        return new Fraction(favorableOutcomes, totalPossibleOutcomes);
    }

    private int calculateStraightFlushForSuit(Card.Suit suit) {
        // Create a simplified deck indicating presence of cards in the specified suit
        boolean[] suitDeck = new boolean[Card.Value.values().length];
        for (Card card : deck.getCards()) {
            if (card.getCardSuit() == suit) {
                suitDeck[Card.getValueIndex(card.getCardValue())] = true;
            }
        }

        // Mark cards in player's hand and community cards as unavailable
        for (Card card : playerHand) {
            if (card.getCardSuit() == suit) {
                suitDeck[Card.getValueIndex(card.getCardValue())] = false;
            }
        }
        for (Card card : communityCards) {
            if (card.getCardSuit() == suit) {
                suitDeck[Card.getValueIndex(card.getCardValue())] = false;
            }
        }

        int count = 0;

        // Check for potential straight flushes in the suit
        for (int i = 0; i <= suitDeck.length - 5; i++) {
            if (isStraightFlushPossible(suitDeck, i)) {
                count++;
            }
        }

        return count;
    }

    private boolean isStraightFlushPossible(boolean[] suitDeck, int start) {
        // Check if a straight flush is possible starting from 'start' index
        for (int i = start; i < start + 5; i++) {
            if (!suitDeck[i]) {
                return false;
            }
        }
        return true;
    }
    private Fraction calculateRoyalFlushProbability() {
        long totalPossibleOutcomes = Combination.calculateCombinations(deck.remainingCards(), 5 - communityCards.length);

        int favorableOutcomes = 0;

        // Check for royal flush in each suit
        for (Card.Suit suit : Card.Suit.values()) {
            favorableOutcomes += calculateRoyalFlushForSuit(suit);
        }

        return new Fraction(favorableOutcomes, totalPossibleOutcomes);
    }

    private int calculateRoyalFlushForSuit(Card.Suit suit) {
        // Check if the required cards for a royal flush in this suit are still available
        Card.Value[] requiredValues = {Card.Value.TEN, Card.Value.JACK, Card.Value.QUEEN, Card.Value.KING, Card.Value.ACE};
        boolean isPossible = true;

        for (Card.Value value : requiredValues) {
            if (!isCardAvailable(value, suit)) {
                isPossible = false;
                break;
            }
        }

        return isPossible ? 1 : 0;
    }

    private boolean isCardAvailable(Card.Value value, Card.Suit suit) {
        // Check if a specific card is still in the deck or not already in the player's hand or community cards
        for (Card card : playerHand) {
            if (card.getCardValue() == value && card.getCardSuit() == suit) {
                return false; // Card is in player's hand
            }
        }
        for (Card card : communityCards) {
            if (card.getCardValue() == value && card.getCardSuit() == suit) {
                return false; // Card is in community cards
            }
        }
        // Check if the card is still in the deck
        return deck.containsCard(new Card(value, suit));
    }

    private Fraction calculateHighCardProbability() {
        long totalPossibleOutcomes = Combination.calculateCombinations(deck.remainingCards(), 5 - communityCards.length);

        // Calculate the probabilities of forming each hand type that is better than high card
        Fraction pairProb = calculatePairProbability();
        Fraction twoPairProb = calculateTwoPairProbability();
        Fraction threeOfAKindProb = calculateThreeOfAKindProbability();
        Fraction straightProb = calculateStraightProbability();
        Fraction flushProb = calculateFlushProbability();
        Fraction fullHouseProb = calculateFullHouseProbability();
        Fraction fourOfAKindProb = calculateFourOfAKindProbability();
        Fraction straightFlushProb = calculateStraightFlushProbability();
        Fraction royalFlushProb = calculateRoyalFlushProbability();

        // Sum up all these probabilities
        long sumOfOtherHandProbs = pairProb.getNumerator() + twoPairProb.getNumerator() +
                threeOfAKindProb.getNumerator() + straightProb.getNumerator() +
                flushProb.getNumerator() + fullHouseProb.getNumerator() +
                fourOfAKindProb.getNumerator() + straightFlushProb.getNumerator() +
                royalFlushProb.getNumerator();

        // The probability of a high card hand is the complement of the sum of all other hand probabilities
        long highCardNumerator = totalPossibleOutcomes - sumOfOtherHandProbs;
        return new Fraction(highCardNumerator, totalPossibleOutcomes);
    }

    public enum HandType {
        HIGH_CARD,PAIR, TWO_PAIR, THREE_OF_A_KIND, STRAIGHT, FLUSH, FULL_HOUSE, FOUR_OF_A_KIND, STRAIGHT_FLUSH, ROYAL_FLUSH
    }
    @Override
    public String toString() {
        String result = "Based of Input:\n";
        result += "Player Hand: " + player.toString() + "\n";
        result += "Community Cards: " + table.toString() + "\n";
        result += "\nTexas Hold'em Hand Probabilities:\n";
        result += "High Card: " + calculateHighCardProbability() + "\n";
        result += "One Pair: " + calculatePairProbability() + "\n";
        result += "Two Pair: " + calculateTwoPairProbability() + "\n";
        result += "Three of a Kind: " + calculateThreeOfAKindProbability() + "\n";
        result += "Straight: " + calculateStraightProbability() + "\n";
        result += "Flush: " + calculateFlushProbability() + "\n";
        result += "Full House: " + calculateFullHouseProbability() + "\n";
        result += "Four of a Kind: " + calculateFourOfAKindProbability() + "\n";
        result += "Straight Flush: " + calculateStraightFlushProbability() + "\n";
        result += "Royal Flush: " + calculateRoyalFlushProbability() + "\n";

        return result;
    }
}