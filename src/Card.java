public class Card {
    public enum Suit {
        HEARTS, DIAMONDS, CLUBS, SPADES
    }

    public enum Value {
        TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE
    }
    private Value cardValue;
    private Suit cardSuit;

    public Card(Value cardValue, Suit cardSuit) {
        this.cardValue = cardValue;
        this.cardSuit = cardSuit;
    }

    public static int getValueIndex(Value value) {
        switch (value) {
            case TWO:
                return 0;
            case THREE:
                return 1;
            case FOUR:
                return 2;
            case FIVE:
                return 3;
            case SIX:
                return 4;
            case SEVEN:
                return 5;
            case EIGHT:
                return 6;
            case NINE:
                return 7;
            case TEN:
                return 8;
            case JACK:
                return 9;
            case QUEEN:
                return 10;
            case KING:
                return 11;
            case ACE:
                return 12;
            default:
                throw new IllegalArgumentException("Unknown card value");
        }
    }

    public Value getCardValue() {
        return cardValue;
    }

    public void setCardValue(Value cardValue) {
        this.cardValue = cardValue;
    }

    public Suit getCardSuit() {
        return cardSuit;
    }

    public void setCardSuit(Suit cardSuit) {
        this.cardSuit = cardSuit;
    }

    @Override
    public String toString() {
        return cardValue + " of " + cardSuit;
    }

}