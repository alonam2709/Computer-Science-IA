public class ProbabilityCalculator {
    private final Player player;
    private final Table table;
    private Card[] playerHand;
    private Card[] communityCards;
    private final Deck deck;

    public ProbabilityCalculator(Player player, Table table, Deck deck) {
        if (player == null || table == null || deck == null) {
            throw new IllegalArgumentException("Player, Table, and Deck cannot be null.");
        }
        this.player = player;
        this.table = table;
        this.deck = deck;
        updateHands();
    }

    private void updateHands() {
        // Ensure that the player and table objects are not null
        if (this.player == null || this.player.hand == null) {
            throw new IllegalStateException("Player object or player's hand is not properly initialized.");
        }
        if (this.table == null) {
            throw new IllegalStateException("Table object or community cards are not properly initialized.");
        }
        this.playerHand = this.player.hand;
        this.communityCards = this.table.getCommunityCards();
    }

    public Fraction calculateHandProbability(HandType handType) {
        // Make sure to update hands to reflect the current state
        updateHands();
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
        System.out.println("Calculating Pair Probability...");

        // Update playerHand and communityCards to the latest
        updateHands();

        // Determine the total number of possible outcomes - drawing 5 cards from the remaining cards in the deck
        long totalPossibleOutcomes = Combination.calculateCombinations(deck.remainingCards(), 5 - communityCards.length);

        // Check if the player already has a pair, if so, the probability is 1/1 as they already achieved the hand
        if (playerHasPair()) {
            return new Fraction(1, 1);
        }

        // Initialize a variable to count the number of favorable outcomes for forming a pair
        long favorableOutcomes = 0;

        // Count occurrences of each card value in the player's hand and community cards
        int[] valueCounts = new int[Card.Value.values().length];
        for (Card card : playerHand) {
            valueCounts[Card.getValueIndex(card.getCardValue())]++;
        }
        for (Card card : communityCards) {
            valueCounts[Card.getValueIndex(card.getCardValue())]++;
        }

        // Calculate the favorable outcomes for each card value
        for (int i = 0; i < valueCounts.length; i++) {
            if (valueCounts[i] == 1) {
                // If exactly one card of this value is present, we can form a pair with any of the remaining cards of the same value
                int remainingCardsOfSameValue = deck.countCardsWithValue(Card.Value.values()[i]);
                favorableOutcomes += Combination.calculateCombinations(remainingCardsOfSameValue, 1);
            } else if (valueCounts[i] == 0) {
                // If no card of this value is present, two cards of this value can still form a pair from the deck
                int remainingCardsOfSameValue = deck.countCardsWithValue(Card.Value.values()[i]);
                // We need at least 2 cards of the same value to form a new pair
                if (remainingCardsOfSameValue >= 2) {
                    favorableOutcomes += Combination.calculateCombinations(remainingCardsOfSameValue, 2);
                }
            }
        }

        // Return the probability of forming a pair as a fraction
        return new Fraction(favorableOutcomes, totalPossibleOutcomes);
    }

    private boolean playerHasPair() {
        // Check the player's hand for any pair
        for (int i = 0; i < playerHand.length - 1; i++) {
            for (int j = i + 1; j < playerHand.length; j++) {
                if (playerHand[i] != null && playerHand[j] != null &&
                        playerHand[i].getCardValue() == playerHand[j].getCardValue()) {
                    return true; // Found a pair
                }
            }
        }
        // Check if there is a pair in community cards
        for (int i = 0; i < communityCards.length - 1; i++) {
            for (int j = i + 1; j < communityCards.length; j++) {
                if (communityCards[i] != null && communityCards[j] != null &&
                        communityCards[i].getCardValue() == communityCards[j].getCardValue()) {
                    return true; // Found a pair
                }
            }
        }
        return false; // No pair found
    }

    private Fraction calculateTwoPairProbability() {
        System.out.println("Calculating Two Pair Probability...");

        // Update playerHand and communityCards to the latest
        updateHands();

        // Calculate the total number of possible outcomes
        long totalPossibleOutcomes = Combination.calculateCombinations(deck.remainingCards(), 5 - communityCards.length);

        // Count occurrences of each card value in the player's hand and community cards
        int[] valueCounts = new int[Card.Value.values().length];
        for (Card card : playerHand) {
            valueCounts[Card.getValueIndex(card.getCardValue())]++;
        }
        for (Card card : communityCards) {
            valueCounts[Card.getValueIndex(card.getCardValue())]++;
        }

        // Initialize variables to count the number of single cards and pairs already present
        int singles = 0;
        int pairs = 0;
        for (int count : valueCounts) {
            if (count == 1) singles++;
            if (count == 2) pairs++;
        }

        // If already have two pairs, the probability is 1 (as they already have two pairs)
        if (pairs >= 2) {
            return new Fraction(1, 1);
        }

        long favorableOutcomes = 0;

        // Calculate the favorable outcomes based on the number of singles and pairs
        if (pairs == 1) {
            // If there's already one pair, need to form another pair from the remaining singles
            for (int i = 0; i < valueCounts.length; i++) {
                if (valueCounts[i] == 1) {
                    int remainingCardsOfSameValue = deck.countCardsWithValue(Card.Value.values()[i]);
                    favorableOutcomes += Combination.calculateCombinations(remainingCardsOfSameValue, 1);
                }
            }
        } else if (pairs == 0) {
            // If there are no pairs yet, need to form two pairs from the singles
            for (int i = 0; i < valueCounts.length; i++) {
                if (valueCounts[i] == 1) {
                    int remainingCardsOfSameValue = deck.countCardsWithValue(Card.Value.values()[i]);
                    // Need at least one more card of the same value to form a pair
                    if (remainingCardsOfSameValue >= 1) {
                        favorableOutcomes += Combination.calculateCombinations(remainingCardsOfSameValue, 1) * singles;
                        singles--; // Decrease count of singles as one is used to form a pair
                    }
                }
            }
            // Correct for double counting each pair formation
            favorableOutcomes = favorableOutcomes / 2;
        }

        return new Fraction(favorableOutcomes, totalPossibleOutcomes);
    }
    private Fraction calculateThreeOfAKindProbability() {
        System.out.println("Calculating Three of a Kind Probability...");

        // Update playerHand and communityCards to the latest
        updateHands();

        // Calculate the total number of possible outcomes
        long totalPossibleOutcomes = Combination.calculateCombinations(deck.remainingCards(), 5 - communityCards.length);

        // Count occurrences of each card value in the player's hand and community cards
        int[] valueCounts = new int[Card.Value.values().length];
        for (Card card : playerHand) {
            valueCounts[Card.getValueIndex(card.getCardValue())]++;
        }
        for (Card card : communityCards) {
            valueCounts[Card.getValueIndex(card.getCardValue())]++;
        }

        long favorableOutcomes = 0;

        // Calculate the favorable outcomes for each card value
        for (int i = 0; i < valueCounts.length; i++) {
            int count = valueCounts[i];
            int remainingCardsOfSameValue = deck.countCardsWithValue(Card.Value.values()[i]);

            if (count == 2 && remainingCardsOfSameValue >= 1) {
                // If there are already two cards of this value, we need one more for Three of a Kind
                favorableOutcomes += Combination.calculateCombinations(remainingCardsOfSameValue, 1);
            } else if (count == 1 && remainingCardsOfSameValue >= 2) {
                // If there is only one card of this value, we need two more for Three of a Kind
                favorableOutcomes += Combination.calculateCombinations(remainingCardsOfSameValue, 2);
            } else if (count == 0 && remainingCardsOfSameValue >= 3) {
                // If there are no cards of this value, we need all three for Three of a Kind
                favorableOutcomes += Combination.calculateCombinations(remainingCardsOfSameValue, 3);
            }
        }

        // Return the probability of forming exactly three of a kind as a fraction
        return new Fraction(favorableOutcomes, totalPossibleOutcomes);
    }

    private Fraction calculateStraightProbability() {
        System.out.println("Calculating Straight Probability...");

        // Update playerHand and communityCards to the latest
        updateHands();

        // Calculate the total number of possible outcomes
        long totalPossibleOutcomes = Combination.calculateCombinations(deck.remainingCards(), 5 - communityCards.length);

        // Create a boolean array representing the presence of card values in the hand and table
        boolean[] presentValues = new boolean[Card.Value.values().length];
        for (Card card : playerHand) {
            if (card != null) {
                presentValues[Card.getValueIndex(card.getCardValue())] = true;
            }
        }
        for (Card card : communityCards) {
            if (card != null) {
                presentValues[Card.getValueIndex(card.getCardValue())] = true;
            }
        }

        long favorableOutcomes = 0;

        // Check for possible straights by looking for sequences of five consecutive cards
        for (int i = 0; i <= presentValues.length - 5; i++) {
            if (canFormStraight(presentValues, i)) {
                favorableOutcomes += countStraightCombinations(presentValues, i);
            }
        }

        return new Fraction(favorableOutcomes, totalPossibleOutcomes);
    }

    private boolean canFormStraight(boolean[] presentValues, int start) {
        // Check if a sequence of five consecutive cards is possible starting from 'start'
        for (int i = start; i < start + 5; i++) {
            if (!presentValues[i]) {
                return false;
            }
        }
        return true;
    }

    private long countStraightCombinations(boolean[] presentValues, int start) {
        // Count the combinations that can complete the straight
        long combinations = 1;
        for (int i = start; i < start + 5; i++) {
            if (!presentValues[i]) {
                // Count the remaining cards of this value in the deck
                int remainingCardsOfValue = deck.countCardsWithValue(Card.Value.values()[i]);
                combinations *= remainingCardsOfValue;
            }
        }
        return combinations;
    }
    private Fraction calculateFlushProbability() {
        System.out.println("Calculating Flush Probability...");

        // Update playerHand and communityCards to the latest
        updateHands();

        // Calculate the total number of possible outcomes
        long totalPossibleOutcomes = Combination.calculateCombinations(deck.remainingCards(), 5 - communityCards.length);

        // Count occurrences of each suit in the player's hand and community cards
        int[] suitCounts = new int[Card.Suit.values().length];
        for (Card card : playerHand) {
            if (card != null) {
                suitCounts[card.getCardSuit().ordinal()]++;
            }
        }
        for (Card card : communityCards) {
            if (card != null) {
                suitCounts[card.getCardSuit().ordinal()]++;
            }
        }

        long favorableOutcomes = 0;

        // Calculate the favorable outcomes for each suit
        for (int suitCount : suitCounts) {
            int cardsNeeded = 5 - suitCount; // Number of additional cards of the same suit needed for a flush
            if (cardsNeeded <= 0) {
                // Already have a flush or more than a flush
                return new Fraction(1, 1);
            } else if (cardsNeeded <= deck.remainingCards()) {
                // Calculate the number of ways to draw the needed cards of the same suit from the deck
                int remainingCardsOfSuit = deck.countCardsOfSuit(Card.Suit.values()[suitCount]);
                favorableOutcomes += Combination.calculateCombinations(remainingCardsOfSuit, cardsNeeded);
            }
        }
        return new Fraction(favorableOutcomes, totalPossibleOutcomes);
    }
    private Fraction calculateFullHouseProbability() {
        System.out.println("Calculating Full House Probability...");

        // Update playerHand and communityCards to the latest
        updateHands();

        // Calculate the total number of possible outcomes
        long totalPossibleOutcomes = Combination.calculateCombinations(deck.remainingCards(), 5 - communityCards.length);

        // Count occurrences of each card value in the player's hand and community cards
        int[] valueCounts = new int[Card.Value.values().length];
        for (Card card : playerHand) {
            valueCounts[Card.getValueIndex(card.getCardValue())]++;
        }
        for (Card card : communityCards) {
            valueCounts[Card.getValueIndex(card.getCardValue())]++;
        }

        long favorableOutcomes = 0;

        // Calculate favorable outcomes for each pair of values (one for three of a kind, one for a pair)
        for (int i = 0; i < valueCounts.length; i++) {
            for (int j = 0; j < valueCounts.length; j++) {
                if (i != j) {
                    // Combinations for the 'three of a kind' part of the full house
                    long threeKindCombinations = calculateCombinationsForThreeOfAKind(i, valueCounts);
                    // Combinations for the 'pair' part of the full house
                    long pairCombinations = calculateCombinationsForPair(j, valueCounts);

                    // Multiply the combinations for the 'three of a kind' and 'pair' to get total combinations for this pair of values
                    favorableOutcomes += threeKindCombinations * pairCombinations;
                }
            }
        }

        return new Fraction(favorableOutcomes, totalPossibleOutcomes);
    }

    private long calculateCombinationsForThreeOfAKind(int valueIndex, int[] valueCounts) {
        int cardsOfValue = valueCounts[valueIndex];
        int remainingCardsOfValue = deck.countCardsWithValue(Card.Value.values()[valueIndex]);

        if (cardsOfValue == 2 && remainingCardsOfValue >= 1) {
            // Need one more card to complete three of a kind
            return Combination.calculateCombinations(remainingCardsOfValue, 1);
        } else if (cardsOfValue == 1 && remainingCardsOfValue >= 2) {
            // Need two more cards to complete three of a kind
            return Combination.calculateCombinations(remainingCardsOfValue, 2);
        } else if (cardsOfValue == 0 && remainingCardsOfValue >= 3) {
            // Need all three cards for three of a kind
            return Combination.calculateCombinations(remainingCardsOfValue, 3);
        }
        return 0;
    }

    private long calculateCombinationsForPair(int valueIndex, int[] valueCounts) {
        int cardsOfValue = valueCounts[valueIndex];
        int remainingCardsOfValue = deck.countCardsWithValue(Card.Value.values()[valueIndex]);

        if (cardsOfValue == 1 && remainingCardsOfValue >= 1) {
            // Need one more card to complete a pair
            return Combination.calculateCombinations(remainingCardsOfValue, 1);
        } else if (cardsOfValue == 0 && remainingCardsOfValue >= 2) {
            // Need two cards for a pair
            return Combination.calculateCombinations(remainingCardsOfValue, 2);
        }
        return 0;
    }
    private Fraction calculateFourOfAKindProbability() {
        System.out.println("Calculating Four of a Kind Probability...");

        // Update playerHand and communityCards to the latest
        updateHands();

        // Calculate the total number of possible outcomes
        long totalPossibleOutcomes = Combination.calculateCombinations(deck.remainingCards(), 5 - communityCards.length);

        // Count occurrences of each card value in the player's hand and community cards
        int[] valueCounts = new int[Card.Value.values().length];
        for (Card card : playerHand) {
            if (card != null) {
                valueCounts[Card.getValueIndex(card.getCardValue())]++;
            }
        }
        for (Card card : communityCards) {
            if (card != null) {
                valueCounts[Card.getValueIndex(card.getCardValue())]++;
            }
        }

        long favorableOutcomes = 0;

        // Calculate the favorable outcomes for each card value to form a Four of a Kind
        for (int i = 0; i < valueCounts.length; i++) {
            int count = valueCounts[i];
            int remainingCardsOfValue = deck.countCardsWithValue(Card.Value.values()[i]);

            if (count == 1 && remainingCardsOfValue >= 3) {
                // If there is one card of this value, need three more for Four of a Kind
                favorableOutcomes += Combination.calculateCombinations(remainingCardsOfValue, 3);
            } else if (count == 2 && remainingCardsOfValue >= 2) {
                // If there are two cards of this value, need two more for Four of a Kind
                favorableOutcomes += Combination.calculateCombinations(remainingCardsOfValue, 2);
            } else if (count == 3 && remainingCardsOfValue >= 1) {
                // If there are three cards of this value, need one more for Four of a Kind
                favorableOutcomes += Combination.calculateCombinations(remainingCardsOfValue, 1);
            }
        }

        return new Fraction(favorableOutcomes, totalPossibleOutcomes);
    }
    private Fraction calculateStraightFlushProbability() {
        System.out.println("Calculating Straight Flush Probability...");

        // Update playerHand and communityCards to the latest
        updateHands();

        // Calculate the total number of possible outcomes
        long totalPossibleOutcomes = Combination.calculateCombinations(deck.remainingCards(), 5 - communityCards.length);

        // A Straight Flush requires a sequence of 5 cards of the same suit
        long favorableOutcomes = 0;

        // Iterate through all suits
        for (Card.Suit suit : Card.Suit.values()) {
            boolean[] presentValues = new boolean[Card.Value.values().length];
            for (Card card : playerHand) {
                if (card != null && card.getCardSuit() == suit) {
                    presentValues[Card.getValueIndex(card.getCardValue())] = true;
                }
            }
            for (Card card : communityCards) {
                if (card != null && card.getCardSuit() == suit) {
                    presentValues[Card.getValueIndex(card.getCardValue())] = true;
                }
            }

            // Check for possible straight flushes for this suit
            for (int i = 0; i <= presentValues.length - 5; i++) {
                if (canFormStraightFlush(presentValues, i)) {
                    favorableOutcomes += countStraightFlushCombinations(presentValues, i, suit);
                }
            }
        }

        return new Fraction(favorableOutcomes, totalPossibleOutcomes);
    }

    private boolean canFormStraightFlush(boolean[] presentValues, int start) {
        // Check if a sequence of five consecutive cards of the same suit is possible starting from 'start'
        for (int i = start; i < start + 5; i++) {
            if (!presentValues[i]) {
                return false;
            }
        }
        return true;
    }

    private long countStraightFlushCombinations(boolean[] presentValues, int start, Card.Suit suit) {
        // Count the combinations that can complete the straight flush
        long combinations = 1;
        for (int i = start; i < start + 5; i++) {
            if (!presentValues[i]) {
                // Count the remaining cards of this value and suit in the deck
                int remainingCardsOfValueAndSuit = deck.countCardsOfValueAndSuit(Card.Value.values()[i], suit);
                combinations *= remainingCardsOfValueAndSuit;
            }
        }
        return combinations;
    }
    private Fraction calculateRoyalFlushProbability() {
        System.out.println("Calculating Royal Flush Probability...");

        // Update playerHand and communityCards to the latest
        updateHands();

        // Calculate the total number of possible outcomes
        long totalPossibleOutcomes = Combination.calculateCombinations(deck.remainingCards(), 5 - communityCards.length);

        long favorableOutcomes = 0;

        // Check for royal flush in each suit
        for (Card.Suit suit : Card.Suit.values()) {
            favorableOutcomes += calculateRoyalFlushForSuit(suit);
        }

        return new Fraction(favorableOutcomes, totalPossibleOutcomes);
    }

    private int calculateRoyalFlushForSuit(Card.Suit suit) {
        // The specific cards needed for a Royal Flush of the given suit
        Card.Value[] requiredValues = {Card.Value.TEN, Card.Value.JACK, Card.Value.QUEEN, Card.Value.KING, Card.Value.ACE};

        // Check if the required cards of the specific suit are still available in the deck
        for (Card.Value value : requiredValues) {
            if (!isCardAvailable(value, suit)) {
                return 0; // If any one card is not available, a Royal Flush is not possible
            }
        }

        // All required cards are available, therefore 1 favorable outcome
        return 1;
    }

    private boolean isCardAvailable(Card.Value value, Card.Suit suit) {
        // Check if a specific card is still in the deck, not in the player's hand or community cards
        for (Card card : playerHand) {
            if (card != null && card.getCardValue() == value && card.getCardSuit() == suit) {
                return false; // Card is in player's hand
            }
        }

        for (Card card : communityCards) {
            if (card != null && card.getCardValue() == value && card.getCardSuit() == suit) {
                return false; // Card is in community cards
            }
        }

        return deck.containsCard(new Card(value, suit));
    }
    private Fraction calculateHighCardProbability() {
        System.out.println("Calculating High Card Probability...");

        long totalPossibleOutcomes = Combination.calculateCombinations(deck.remainingCards(), 5 - communityCards.length);
        // Calculate the probability of all other hands
        Fraction pairProb = calculatePairProbability();
        Fraction twoPairProb = calculateTwoPairProbability();
        Fraction threeOfAKindProb = calculateThreeOfAKindProbability();
        Fraction straightProb = calculateStraightProbability();
        Fraction flushProb = calculateFlushProbability();
        Fraction fullHouseProb = calculateFullHouseProbability();
        Fraction fourOfAKindProb = calculateFourOfAKindProbability();
        Fraction straightFlushProb = calculateStraightFlushProbability();
        Fraction royalFlushProb = calculateRoyalFlushProbability();

        // The probability of high card is 1 minus all the probabilities of the other hands
        long numerator = totalPossibleOutcomes - (pairProb.getNumerator() + twoPairProb.getNumerator() +
                threeOfAKindProb.getNumerator() + straightProb.getNumerator() +
                flushProb.getNumerator() + fullHouseProb.getNumerator() +
                fourOfAKindProb.getNumerator() + straightFlushProb.getNumerator() +
                royalFlushProb.getNumerator());

        return new Fraction(numerator, totalPossibleOutcomes);
    }

    public enum HandType {
        HIGH_CARD,PAIR, TWO_PAIR, THREE_OF_A_KIND, STRAIGHT, FLUSH, FULL_HOUSE, FOUR_OF_A_KIND, STRAIGHT_FLUSH, ROYAL_FLUSH
    }

    @Override
    public String toString() {
        System.out.println("CalculatingTo String");
        String result = "Based of Input:\n";
        result = result + "Player Hand: " + player.toString() + "\n";
        result = result + "Community Cards: " + table.toString() + "\n";
        result = result + "\nTexas Hold'em Hand Probabilities:\n";
        result = result + "High Card: " + calculateHandProbability(HandType.HIGH_CARD) + "\n";
        result = result + "One Pair: " + calculateHandProbability(HandType.PAIR) + "\n";
        result = result + "Two Pair: " + calculateHandProbability(HandType.TWO_PAIR) + "\n";
        result = result + "Three of a Kind: " + calculateHandProbability(HandType.THREE_OF_A_KIND) + "\n";
        result = result + "Straight: " + calculateHandProbability(HandType.STRAIGHT) + "\n";
        result = result + "Flush: " + calculateHandProbability(HandType.FLUSH) + "\n";
        result = result + "Full House: " + calculateHandProbability(HandType.FULL_HOUSE) + "\n";
        result = result + "Four of a Kind: " + calculateHandProbability(HandType.FOUR_OF_A_KIND) + "\n";
        result = result + "Straight Flush: " + calculateHandProbability(HandType.STRAIGHT_FLUSH) + "\n";
        result = result + "Royal Flush: " + calculateHandProbability(HandType.ROYAL_FLUSH) + "\n";
        return result;
    }


}