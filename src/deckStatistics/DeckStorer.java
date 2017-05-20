package DeckStatistics;

import java.util.Random;
import java.lang.StringBuilder;
import java.text.DecimalFormat;
import org.apache.commons.math3.util.ArithmeticUtils;
import org.apache.commons.math3.util.CombinatoricsUtils;

/**
 * casino.com/uk/blackjack - perfect blackjack
Deck storing class. Used to store the number of cards remaining in the shoe and the
probability of a certain card coming up.
*/

public class DeckStorer{
	private Random _random;
	private int _nDecks = 6;
	private double _nCards;
	private double _deckCardProbabilities[];
	private int _deckCardNumbers[]; 
	private int _deckCardBreakdown[][];
	private boolean _print = false;
	private DecimalFormat _df;
	private String[] _suitStrings = {"H","D","S","C"};
	public int _lastCard = -1;
	public int _lastSuit = -1;
	public static void main(String[] args){

	}
	public DeckStorer(){
		this(6);
	}
	public DeckStorer(int nDecks){
		_nDecks = nDecks;
		_df = new DecimalFormat("##0.0#");
		_deckCardProbabilities = new double[13];
		_deckCardNumbers = new int[13];
		_deckCardBreakdown = new int[13][4];
		_random = new Random();
		rebuildDeck();
	}
	private int getCard(){
		rebuildProbabilities();
		double randNum = _random.nextDouble();
		double cumulativeProb = 0;
		double lastCumulativeProb = 0;
		for(int i = 0; i<13;i++){
			cumulativeProb+=_deckCardProbabilities[i];
			if (randNum>lastCumulativeProb && randNum<=cumulativeProb){
				if (_nDecks!=0){
					_deckCardNumbers[i]--;
				}
				_lastCard = i;
				return i;
			}
			lastCumulativeProb = cumulativeProb;
		}
		return -1;
	}
	private int getSuit(int card){
		double cumulativeProb = 0;
		double lastCumulativeProb = 0;
		double randNum = _random.nextDouble();
		int nCards = 0;
		for(int suit = 0; suit<4; suit++){
			nCards+=_deckCardBreakdown[card][suit];
		}
		for(int suit = 0; suit<4; suit++){
			cumulativeProb+=_deckCardBreakdown[card][suit]*1.0/nCards;
			if (randNum>lastCumulativeProb && randNum<=cumulativeProb){
				if (_nDecks!=0){
					_deckCardBreakdown[card][suit]--;
				}
				_lastSuit = suit;
				return suit;
			}
			lastCumulativeProb = cumulativeProb;
		}
		return -1;
	}
	public int drawCard(){
		int card = getCard();
		int suit = getSuit(card);
		if (_print){
			print(card,suit);
		}
		return card;
	}
	public void print(){	
		StringBuilder strB = new StringBuilder();
		strB.append(_df.format(_deckCardNumbers[0]));
		for(int i = 1 ; i<13; i++){
			strB.append(" | ");
			strB.append(_df.format(_deckCardNumbers[i]));
		}
		strB.append("\n");
		strB.append(_df.format(_deckCardProbabilities[0]));
		for(int i = 1 ; i<13; i++){
			strB.append(" | ");
			strB.append(_df.format(_deckCardProbabilities[i]));
		}
		System.out.println("====================");
		System.out.println(strB.toString());
	}
	public void print(int card, int suit){
		if(card == -1 || suit == -1){
			System.out.println("Card ERR has been drawn");
			return;
		}
		System.out.println("Card " + card  + _suitStrings[suit] + "has been drawn");
	}
	public void rebuildDeck(){
		if (_nDecks==0){
			// if nDecks == 0 (ie infinite shoe)
			_nCards = 52;
			for(int i = 0; i<13;i++){
				_deckCardNumbers[i] = 4;
				_deckCardProbabilities[i] = 1.0/13;
				for(int j = 0; j<4; j++){
					_deckCardBreakdown[i][j]=1;
				}
			}
		}
		else{
			_nCards = _nDecks*52;
			for(int i = 0; i<13; i++){
				_deckCardNumbers[i] = 4*_nDecks;
				for (int j = 0; j<4; j++){
					_deckCardBreakdown[i][j] = _nDecks;
				}
			}	
		}
		rebuildProbabilities();
	}
	public void rebuildProbabilities(){
		if (_nDecks == 0 || _nCards <1){
			return;
		}
		for(int i = 0; i<13; i++){
			_deckCardProbabilities[i] = _deckCardNumbers[i]/_nCards;
		}
	}

	/**
	 * returns the probability of
	 * @param
	 * @return
	 */
	public double probabilityOfBust(int currentScore,int nAces, int nTurns){
        double probability = 0;
        for(int i = nAces; i>0; i++){
			currentScore-=10;
		}
		for(int i = 0; i<nTurns;i++){

		}
		return probability;
	}
}
