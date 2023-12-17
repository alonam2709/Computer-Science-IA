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


}
