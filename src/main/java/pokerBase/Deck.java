package pokerBase;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import pokerBase.Card;
import pokerEnums.eRank;
import pokerEnums.eSuit;

@XmlRootElement
public class Deck {
	
	@XmlElement (name="Remaining Card")
	private ArrayList<Card> deck;

		public Deck() {
		
		// Create an ArrayList of Cars, add each card
		ArrayList<Card> MakingDeck = new ArrayList<Card>();
		for(short i = 0; i <= 3; i++) 
		{
			eSuit SuitValue= eSuit.values()[i];
			for (short y = 0; y <= 12; y++)
			{
				eRank RankValue = eRank.values()[y];
				Card NewCard = new Card(SuitValue, RankValue);
				MakingDeck.add(NewCard);
			}
			}
		// set the instance variable
		deck = MakingDeck;
		Shuffle();
	}
	
	/** creates a deck with jokers
	 * @param NumJokers
	 */
	public Deck(int NumJokers) {

		this();
		
		for (short i = 1; i <= NumJokers; i++) {
			deck.add(new Card(eSuit.JOKER, eRank.JOKER));//,53));
		}
		Shuffle();
	}
	
	

	
	private void Shuffle()
	{
		//	Shuffle the deck
		Collections.shuffle(deck);
	}

	public Card draw() {
		// Removes the first card from the deck and return the card
		Card FirstCard = deck.get(0);
		deck.remove(0);
		return FirstCard;
	}

	public int getTotaldeck() {
		// Returns the total number of deck still in the deck
		return deck.size();
	}
	
	public ArrayList<Card> getdeck()
	{
		return this.deck;
	}
	
	public StringWriter SerializeMe()
	{
	    StringWriter sw = new StringWriter();
		try
		{
		    //Write it
		    JAXBContext ctx = JAXBContext.newInstance(Deck.class);
		    Marshaller m = ctx.createMarshaller();
		    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		    m.marshal(this, sw);
		    sw.close();			
		}
		catch (Exception ex)
		{
			
		}
    
    return sw;
	}
}