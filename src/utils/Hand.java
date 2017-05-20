package utils;
import java.util.ArrayList;
/**
 * Created by user on 16/05/17.
 */
public class Hand {
    protected int[] _scoreArray;
    protected ArrayList<Integer> _hand = new ArrayList<Integer>();
    protected int _score;
    protected int _nAces;

    public Hand(){

    }

    public int getScore(){
        return 0;
    }
    public void addCard(int... cards){
        for (int i:cards){
            _hand.add(i);
        }
        getScore();
    }
    public void removeCard(int... indexes){
        for (int i:indexes){
            _hand.remove(i);
        }
        getScore();
    }
    public void clearAll(){

    }
    public void set(int index, int card){
        if (getSize()>index){
            addCard(card);
        }
        else{
            _hand.set(index,card);
        }
    }
    public int getSize(){
        return _hand.size();
    }
}
