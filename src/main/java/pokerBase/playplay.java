package pokerBase;
//@Author Michael Cisternino, EJ Nygren
import pokerBase.TestGame;

public class playplay {

	public static void main(String[] args) {
		// created this class to test MyInteger class and methods

		final TestGame newGame = new TestGame();
		newGame.testGame();
		System.out.println("Winner with a " +newGame.getWinnerHand());
		Deck d = new Deck(200);
		Hand h = new Hand(d);
		try {
		h.PickBestHand(h.getWildHands());
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}
}