package pokerBase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.UUID;

import javax.xml.bind.annotation.XmlElement;

import pokerBase.Card;
import pokerBase.Deck;
import pokerBase.Hand;
import pokerEnums.eCardNo;
import pokerEnums.eHandStrength;
import pokerEnums.eRank;

public class Hand {
	private UUID playerID;
	@XmlElement
	private ArrayList<Card> CardsInHand;
	private ArrayList<Card> BestCardsInHand;
	private ArrayList<Hand> WildHands = new ArrayList<Hand>();
	

	@XmlElement
	private int HandStrength;
	@XmlElement
	private int HiHand;
	@XmlElement
	private int LoHand;
	@XmlElement
	private ArrayList<Card> Kickers = new ArrayList<Card>();
	@XmlElement
	private ArrayList<Card> WildCards = new ArrayList<Card>();


	private boolean bScored = false;

	private boolean Flush;
	private boolean Straight;
	private boolean Ace;
	private static Deck dJoker = new Deck(2);

	public Hand()
	{
		
	}
	public void  AddCardToHand(Card c)
	{
		if (this.CardsInHand == null)
		{
			CardsInHand = new ArrayList<Card>();
		}
		this.CardsInHand.add(c);
	}
	
	public Card  GetCardFromHand(int location)
	{
		return CardsInHand.get(location);
	}
	
	public Hand(Deck deck) {
		Deck d = new Deck();
		ArrayList<Card> newHand = new ArrayList<Card>();
		for (int x = 0; x < 5; x++) {
			Card c = deck.draw();
			newHand.add(c);
			if (c.getRank() == eRank.JOKER) {
				WildCards.add(c);
			}
			Hand h = new Hand(newHand);
			/** stupid dumb fucking code
		if (WildCards != null) {
			ArrayList<Hand> firstRound = new ArrayList<Hand>();
			ArrayList<Hand> secondRound = new ArrayList<Hand>();
			Collections.sort(newHand, Card.CardRank);
			if (WildCards.size() == 3) {
				for (int i = 0; i <= d.getTotaldeck(); i++) {
					h.getDeck().set(0, d.getdeck().get(i));
					firstRound.add(h);
				}
				for (int i = 0, j = 0; i <= firstRound.size(); i++) {
					if (j <= d.getTotaldeck()) {
						Hand k = firstRound.get(i);
						firstRound.get(i).getDeck().set(1, d.getdeck().get(j));
						secondRound.add(k);
						j++;
						}
					for (int n = 0, l = 0; n <= firstRound.size(); n++) {
						if (l <= d.getTotaldeck()) {
							Hand e = firstRound.get(i);
							firstRound.get(i).getDeck().set(2, d.getdeck().get(j));
							secondRound.add(e);
							l++;
							}
						}
						
					}
				}
			}
		} */
		}
		CardsInHand = newHand;
	}

	public Hand(ArrayList<Card> setCards) {
		this.CardsInHand = setCards;
	}

	public ArrayList<Card> getDeck() {
		return CardsInHand;
	}

	public ArrayList<Card> getBestHand() {
		return BestCardsInHand;
	}

	public void setPlayerID(UUID playerID)
	{
		this.playerID = playerID;
	}
	public UUID getPlayerID()
	{
		return playerID;
	}
	public void setBestHand(ArrayList<Card> BestHand) {
		this.BestCardsInHand = BestHand;
	}

	public int getHandStrength() {
		return HandStrength;
	}

	public Hand PickBestHand(ArrayList<Hand> handList) throws SameHandException {
		Collections.sort(handList, Hand.HandRank);
		return handList.get(0);
	}
	
	public ArrayList<Hand> getWildHands() {
		return this.WildHands;
	}
	
	public ArrayList<Card> getKicker() {
		return Kickers;
	}

	public int getHighPairStrength() {
		return HiHand;
	}

	public int getLowPairStrength() {
		return LoHand;
	}

	public boolean getAce() {
		return Ace;
	}

	public static Hand EvalHand(ArrayList<Card> SeededHand) {
		
		Deck d = new Deck();
		Hand h = new Hand(d);
		h.CardsInHand = SeededHand;

		return h;
	}


	public void EvalHand() {
		// Evaluates if the hand is a flush and/or straight then figures out
		// the hand's strength attributes
		ArrayList<Card> remainingCards = new ArrayList<Card>();
		
		// Sort the cards!
		Collections.sort(CardsInHand, Card.CardRank);
		
		// Ace Evaluation
		if (CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank() == eRank.ACE) {
			Ace = true;
		}

		// Flush Evaluation
		if (CardsInHand.get(eCardNo.FirstCard.getCardNo()).getSuit() == CardsInHand
				.get(eCardNo.SecondCard.getCardNo()).getSuit()
				&& CardsInHand.get(eCardNo.FirstCard.getCardNo()).getSuit() == CardsInHand
						.get(eCardNo.ThirdCard.getCardNo()).getSuit()
				&& CardsInHand.get(eCardNo.FirstCard.getCardNo()).getSuit() == CardsInHand
						.get(eCardNo.FourthCard.getCardNo()).getSuit()
				&& CardsInHand.get(eCardNo.FirstCard.getCardNo()).getSuit() == CardsInHand
						.get(eCardNo.FifthCard.getCardNo()).getSuit()) {
			Flush = true;
		} else {
			Flush = false;
		}



		// Straight Evaluation
		if (Ace) {
			// Looks for Ace, King, Queen, Jack, 10
			if (CardsInHand.get(eCardNo.SecondCard.getCardNo()).getRank() == eRank.KING
					&& CardsInHand.get(eCardNo.ThirdCard.getCardNo()).getRank() == eRank.QUEEN
					&& CardsInHand.get(eCardNo.FourthCard.getCardNo())
							.getRank() == eRank.JACK
					&& CardsInHand.get(eCardNo.FifthCard.getCardNo()).getRank() == eRank.TEN) {
				Straight = true;
				// Looks for Ace, 2, 3, 4, 5
			} else if (CardsInHand.get(eCardNo.FifthCard.getCardNo()).getRank() == eRank.TWO
					&& CardsInHand.get(eCardNo.FourthCard.getCardNo())
							.getRank() == eRank.THREE
					&& CardsInHand.get(eCardNo.ThirdCard.getCardNo()).getRank() == eRank.FOUR
					&& CardsInHand.get(eCardNo.SecondCard.getCardNo())
							.getRank() == eRank.FIVE) {
				Straight = true;
			} else {
				Straight = false;
			}
			// Looks for straight without Ace
		} else if (CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
				.getRank() == CardsInHand.get(eCardNo.SecondCard.getCardNo())
				.getRank().getRank() + 1
				&& CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
						.getRank() == CardsInHand
						.get(eCardNo.ThirdCard.getCardNo()).getRank().getRank() + 2
				&& CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
						.getRank() == CardsInHand
						.get(eCardNo.FourthCard.getCardNo()).getRank()
						.getRank() + 3
				&& CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
						.getRank() == CardsInHand
						.get(eCardNo.FifthCard.getCardNo()).getRank().getRank() + 4) {
			Straight = true;
		} else {
			Straight = false;
		}

		// Evaluates the hand type
		if (Straight == true
				&& Flush == true
				&& CardsInHand.get(eCardNo.FifthCard.getCardNo()).getRank() == eRank.TEN
				&& Ace) {
			ScoreHand(eHandStrength.RoyalFlush, 0, 0, null);
		}

		// Straight Flush
		else if (Straight == true && Flush == true) {
			remainingCards = null;
			ScoreHand(eHandStrength.StraightFlush,
					CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
							.getRank(), 0, remainingCards);
		}
		
		// five of a Kind

		else if (CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank() == CardsInHand
				.get(eCardNo.FifthCard.getCardNo()).getRank()) {
			remainingCards = null;
			ScoreHand(eHandStrength.FiveOfAKind,
					CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
							.getRank(), 0, remainingCards);
		}
		
		
		// Four of a Kind

		else if (CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank() == CardsInHand
				.get(eCardNo.SecondCard.getCardNo()).getRank()
				&& CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank() == CardsInHand
						.get(eCardNo.ThirdCard.getCardNo()).getRank()
				&& CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank() == CardsInHand
						.get(eCardNo.FourthCard.getCardNo()).getRank()) {
						
			remainingCards.add(CardsInHand.get(eCardNo.FifthCard.getCardNo()));
			ScoreHand(eHandStrength.FourOfAKind,
					CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
							.getRank(), 0,
							remainingCards);
		}

		else if (CardsInHand.get(eCardNo.FifthCard.getCardNo()).getRank() == CardsInHand
				.get(eCardNo.SecondCard.getCardNo()).getRank()
				&& CardsInHand.get(eCardNo.FifthCard.getCardNo()).getRank() == CardsInHand
						.get(eCardNo.ThirdCard.getCardNo()).getRank()
				&& CardsInHand.get(eCardNo.FifthCard.getCardNo()).getRank() == CardsInHand
						.get(eCardNo.FourthCard.getCardNo()).getRank()) {
			
			remainingCards.add(CardsInHand.get(eCardNo.FirstCard.getCardNo()));
			ScoreHand(eHandStrength.FourOfAKind,
					CardsInHand.get(eCardNo.FifthCard.getCardNo()).getRank()
							.getRank(), 0,
							remainingCards);
		}

		// Full House
		else if (CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank() == CardsInHand
				.get(eCardNo.ThirdCard.getCardNo()).getRank()
				&& CardsInHand.get(eCardNo.FourthCard.getCardNo()).getRank() == CardsInHand
						.get(eCardNo.FifthCard.getCardNo()).getRank()) {
			remainingCards = null;
			ScoreHand(eHandStrength.FullHouse,
					CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
							.getRank(),
					CardsInHand.get(eCardNo.FourthCard.getCardNo()).getRank()
							.getRank(), remainingCards);
		}

		else if (CardsInHand.get(eCardNo.ThirdCard.getCardNo()).getRank() == CardsInHand
				.get(eCardNo.FifthCard.getCardNo()).getRank()
				&& CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank() == CardsInHand
						.get(eCardNo.SecondCard.getCardNo()).getRank()) {
			remainingCards = null;
			ScoreHand(eHandStrength.FullHouse,
					CardsInHand.get(eCardNo.ThirdCard.getCardNo()).getRank()
							.getRank(),
					CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
							.getRank(), remainingCards);
		}

		// Flush
		else if (Flush) {
			remainingCards = null;
			ScoreHand(eHandStrength.Flush,
					CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
							.getRank(), 0, remainingCards);
		}

		// Straight
		else if (Straight) {
			remainingCards = null;
			ScoreHand(eHandStrength.Straight,
					CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
							.getRank(), 0, remainingCards);
		}

		// Three of a Kind
		else if (CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank() == CardsInHand
				.get(eCardNo.ThirdCard.getCardNo()).getRank()) {
			
			remainingCards.add(CardsInHand.get(eCardNo.FourthCard.getCardNo()));
			remainingCards.add(CardsInHand.get(eCardNo.FifthCard.getCardNo()));			
			ScoreHand(eHandStrength.ThreeOfAKind,
					CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
							.getRank(), 0,
							remainingCards);
		}

		else if (CardsInHand.get(eCardNo.SecondCard.getCardNo()).getRank() == CardsInHand
				.get(eCardNo.FourthCard.getCardNo()).getRank()) {
			remainingCards.add(CardsInHand.get(eCardNo.FirstCard.getCardNo()));
			remainingCards.add(CardsInHand.get(eCardNo.FifthCard.getCardNo()));			
			
			ScoreHand(eHandStrength.ThreeOfAKind,
					CardsInHand.get(eCardNo.SecondCard.getCardNo()).getRank()
							.getRank(), 0,
							remainingCards);
		} else if (CardsInHand.get(eCardNo.ThirdCard.getCardNo()).getRank() == CardsInHand
				.get(eCardNo.FifthCard.getCardNo()).getRank()) {
			remainingCards.add(CardsInHand.get(eCardNo.FirstCard.getCardNo()));
			remainingCards.add(CardsInHand.get(eCardNo.SecondCard.getCardNo()));				
			ScoreHand(eHandStrength.ThreeOfAKind,
					CardsInHand.get(eCardNo.ThirdCard.getCardNo()).getRank()
							.getRank(), 0,
							remainingCards);
		}

		// Two Pair
		else if (CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank() == CardsInHand
				.get(eCardNo.SecondCard.getCardNo()).getRank()
				&& (CardsInHand.get(eCardNo.ThirdCard.getCardNo()).getRank() == CardsInHand
						.get(eCardNo.FourthCard.getCardNo()).getRank())) {
			
			remainingCards.add(CardsInHand.get(eCardNo.FifthCard.getCardNo()));
			
			ScoreHand(eHandStrength.TwoPair,
					CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
							.getRank(),
					CardsInHand.get(eCardNo.ThirdCard.getCardNo()).getRank()
							.getRank(),
							remainingCards);
		} else if (CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank() == CardsInHand
				.get(eCardNo.SecondCard.getCardNo()).getRank()
				&& (CardsInHand.get(eCardNo.FourthCard.getCardNo()).getRank() == CardsInHand
						.get(eCardNo.FifthCard.getCardNo()).getRank())) {
			
			remainingCards.add(CardsInHand.get(eCardNo.ThirdCard.getCardNo()));
			
			ScoreHand(eHandStrength.TwoPair,
					CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
							.getRank(),
					CardsInHand.get(eCardNo.FourthCard.getCardNo()).getRank()
							.getRank(),
							remainingCards);
		} else if (CardsInHand.get(eCardNo.SecondCard.getCardNo()).getRank() == CardsInHand
				.get(eCardNo.ThirdCard.getCardNo()).getRank()
				&& (CardsInHand.get(eCardNo.FourthCard.getCardNo()).getRank() == CardsInHand
						.get(eCardNo.FifthCard.getCardNo()).getRank())) {
			
			remainingCards.add(CardsInHand.get(eCardNo.FirstCard.getCardNo()));
			ScoreHand(eHandStrength.TwoPair,
					CardsInHand.get(eCardNo.SecondCard.getCardNo()).getRank()
							.getRank(),
					CardsInHand.get(eCardNo.FourthCard.getCardNo()).getRank()
							.getRank(),
							remainingCards);
		}

		// Pair
		else if (CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank() == CardsInHand
				.get(eCardNo.SecondCard.getCardNo()).getRank()) {
			
			remainingCards.add(CardsInHand.get(eCardNo.ThirdCard.getCardNo()));
			remainingCards.add(CardsInHand.get(eCardNo.FourthCard.getCardNo()));
			remainingCards.add(CardsInHand.get(eCardNo.FifthCard.getCardNo()));
			ScoreHand(eHandStrength.Pair,
					CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
							.getRank(), 0,
							remainingCards);
		} else if (CardsInHand.get(eCardNo.SecondCard.getCardNo()).getRank() == CardsInHand
				.get(eCardNo.ThirdCard.getCardNo()).getRank()) {
			remainingCards.add(CardsInHand.get(eCardNo.FirstCard.getCardNo()));
			remainingCards.add(CardsInHand.get(eCardNo.FourthCard.getCardNo()));
			remainingCards.add(CardsInHand.get(eCardNo.FifthCard.getCardNo()));
			ScoreHand(eHandStrength.Pair,
					CardsInHand.get(eCardNo.SecondCard.getCardNo()).getRank()
							.getRank(), 0,
							remainingCards);
		} else if (CardsInHand.get(eCardNo.ThirdCard.getCardNo()).getRank() == CardsInHand
				.get(eCardNo.FourthCard.getCardNo()).getRank()) {
			
			remainingCards.add(CardsInHand.get(eCardNo.FirstCard.getCardNo()));
			remainingCards.add(CardsInHand.get(eCardNo.SecondCard.getCardNo()));
			remainingCards.add(CardsInHand.get(eCardNo.FifthCard.getCardNo()));
			
			ScoreHand(eHandStrength.Pair,
					CardsInHand.get(eCardNo.ThirdCard.getCardNo()).getRank()
							.getRank(), 0,
							remainingCards);
		} else if (CardsInHand.get(eCardNo.FourthCard.getCardNo()).getRank() == CardsInHand
				.get(eCardNo.FifthCard.getCardNo()).getRank()) {
			
			remainingCards.add(CardsInHand.get(eCardNo.FirstCard.getCardNo()));
			remainingCards.add(CardsInHand.get(eCardNo.SecondCard.getCardNo()));
			remainingCards.add(CardsInHand.get(eCardNo.ThirdCard.getCardNo()));
			
			ScoreHand(eHandStrength.Pair,
					CardsInHand.get(eCardNo.FourthCard.getCardNo()).getRank()
							.getRank(), 0,
							remainingCards);
		}

		else {
			remainingCards.add(CardsInHand.get(eCardNo.SecondCard.getCardNo()));
			remainingCards.add(CardsInHand.get(eCardNo.ThirdCard.getCardNo()));
			remainingCards.add(CardsInHand.get(eCardNo.FourthCard.getCardNo()));
			remainingCards.add(CardsInHand.get(eCardNo.FifthCard.getCardNo()));
			
			ScoreHand(eHandStrength.HighCard,
					CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
							.getRank(), 0,
							remainingCards);
		}
	}


	private void ScoreHand(eHandStrength hST, int HiHand, int LoHand, ArrayList<Card> kickers) {
		this.HandStrength = hST.getHandStrength();
		this.HiHand = HiHand;
		this.LoHand = LoHand;
		this.Kickers = kickers;
		this.bScored = true;

	}

	/**
	 * Custom sort to figure the best hand in an array of hands
	 */
	public static Comparator<Hand> HandRank = new Comparator<Hand>() {

		public int compare(Hand h1, Hand h2) {

			int result = 0;

			result = h2.getHandStrength() - h1.getHandStrength();

			if (result != 0) {
				return result;
			}

			result = h2.getHighPairStrength() - h1.getHighPairStrength();
			if (result != 0) {
				return result;
			}

			result = h2.getLowPairStrength() - h1.getLowPairStrength();
			if (result != 0) {
				return result;
			}

		
			if (h2.getKicker().get(eCardNo.FirstCard.getCardNo()) != null)
			{
				if (h1.getKicker().get(eCardNo.FirstCard.getCardNo()) != null)
				{
					result = h2.getKicker().get(eCardNo.FirstCard.getCardNo()).getRank().getRank() - h1.getKicker().get(eCardNo.FirstCard.getCardNo()).getRank().getRank();
				}
				if (result != 0)
				{
					return result;
				}
			}
			
			if (h2.getKicker().get(eCardNo.SecondCard.getCardNo()) != null)
			{
				if (h1.getKicker().get(eCardNo.SecondCard.getCardNo()) != null)
				{
					result = h2.getKicker().get(eCardNo.SecondCard.getCardNo()).getRank().getRank() - h1.getKicker().get(eCardNo.SecondCard.getCardNo()).getRank().getRank();
				}
				if (result != 0)
				{
					return result;
				}
			}
			if (h2.getKicker().get(eCardNo.ThirdCard.getCardNo()) != null)
			{
				if (h1.getKicker().get(eCardNo.ThirdCard.getCardNo()) != null)
				{
					result = h2.getKicker().get(eCardNo.ThirdCard.getCardNo()).getRank().getRank() - h1.getKicker().get(eCardNo.ThirdCard.getCardNo()).getRank().getRank();
				}
				if (result != 0)
				{
					return result;
				}
			}
			
			if (h2.getKicker().get(eCardNo.FourthCard.getCardNo()) != null)
			{
				if (h1.getKicker().get(eCardNo.FourthCard.getCardNo()) != null)
				{
					result = h2.getKicker().get(eCardNo.FourthCard.getCardNo()).getRank().getRank() - h1.getKicker().get(eCardNo.FourthCard.getCardNo()).getRank().getRank();
				}
				if (result != 0)
				{
					return result;
				}
			}			
				return 0;
		}
	};
}
