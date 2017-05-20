package imageSimilarity.websites;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import imageSimilarity.ImageMatrix;
import imageSimilarity.ImageMatrixHandler;
import utils.Hand;
import java.awt.image.BufferedImage;

/**
 * Class that handles the
 */
public class ScreenObserver extends ImageMatrixHandler {
    protected ArrayList<int[]> _dealerHandCoords = new ArrayList<int[]>();
    protected ArrayList<int[]> _playerHandCoords = new ArrayList<int[]>();
    protected ArrayList<ArrayList<Rectangle>> _dealerROI = new ArrayList<ArrayList<Rectangle>>();
    protected ArrayList<ArrayList<Rectangle>> _playerROI = new ArrayList<ArrayList<Rectangle>>();
    protected ArrayList<Hand> _dealerHands = new ArrayList<Hand>();
    protected ArrayList<Hand> _playerHands = new ArrayList<Hand>();
    protected Robot _screenCapture = new Robot();
    protected int _cardIconWidth = 15;
    protected int _cardIconHeight = 15;
    protected int _widthToNextCardIcon = 21;

    public ScreenObserver(String folderpath) throws AWTException{
        super(folderpath);
    }
    protected void initialise(){
        initialise(_dealerROI,_dealerHands,_dealerHandCoords);
        initialise(_playerROI,_playerHands,_playerHandCoords);
    }
    protected void initialise(ArrayList<ArrayList<Rectangle>> roiListList,ArrayList<Hand> handsList,
                                                                ArrayList<int[]> coords){
        roiListList.clear();
        handsList.clear();

        int x,y;
        for(int i = 0; i<coords.size(); i++){
            handsList.add(new Hand());
            roiListList.add(new ArrayList<Rectangle>());
            x = coords.get(i)[0];
            y = coords.get(i)[1];
            for(int k = 0; k<2; k++){
                roiListList.get(i).add(new Rectangle(x+k*_widthToNextCardIcon,y,
                        _cardIconWidth,_cardIconHeight));
            }
        }
    }
    public void update(){
        update(_dealerROI,_dealerHands);
        update(_playerROI,_playerHands);
    }
    protected void update(Rectangle roi){
        BufferedImage bImg;
        bImg = _screenCapture.createScreenCapture(roi);
        ImageMatrix img = new ImageMatrix(bImg);

    }

    protected void update(ArrayList<ArrayList<Rectangle>> deckROI,ArrayList<Hand> handsList){
        BufferedImage bImg;
        int card;
        Rectangle currentROI;
        ArrayList<Rectangle> currentHandROI;
        Hand currentHand;
        int prevX,prevY;
        for(int handIndex = 0; handIndex<deckROI.size();handIndex++){
            //for each hand
            currentHandROI = deckROI.get(handIndex);
            currentHand = handsList.get(handIndex);
            for(int i = 0; i<currentHandROI.size(); i++){
                //for each region of interest in hand, find card associated with it
                currentROI = currentHandROI.get(i);
                bImg = _screenCapture.createScreenCapture(currentROI);
                ImageMatrix img = new ImageMatrix(bImg);
                card = identify(img);
                if (getLastCardConfidence()>0.9){
                    currentHand.set(i,card);
                }
            }
            if (currentHand.getSize()==currentHandROI.size()){
                //number of ROI should be hand size + 1 (as there should be an extra roi in case
                //an additional card is dealt
                prevX = currentHandROI.get(currentHandROI.size()-1).x;
                prevY = currentHandROI.get(currentHandROI.size()-1).y;
                currentHandROI.add(new Rectangle(prevX+_widthToNextCardIcon,prevY,_cardIconWidth,
                        _cardIconHeight));
            }
        }
    }
}