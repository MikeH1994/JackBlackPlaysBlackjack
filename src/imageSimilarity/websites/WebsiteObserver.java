package imageSimilarity.websites;

import utils.Utilities;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import imageSimilarity.ImageMatrix;
import blackjackExceptions.QuitException;

/**
 * Created by user on 17/05/17.
 */
public class WebsiteObserver extends ScreenObserver {
    public static String root;
    public WebsiteObserver(String folderPath,String configFilepath)
                                throws AWTException, FileNotFoundException, QuitException {
        super(folderPath);
        loadConfigFile(configFilepath);
        initialise();
    }
    public WebsiteObserver(String folderPath) throws AWTException, QuitException {
        super(folderPath);
        initialiseWithUserPrompts();
        initialise();
    }
    public void initialiseWithUserPrompts() throws QuitException{
        System.out.println("Running setup. See readme for more details.");
        //get player coordinates
        String prompt = "Place cursor to the top left of player card icon and press return";
        int[] coords = getCoordinatesFromUserPrompt(prompt);
        System.out.println("" + coords[0] + ", " + coords[1]);
        _playerHandCoords.add(coords);

        //get player split coordinates
        prompt = "Place cursor to the top left of player split card icon and press return";
        coords = getCoordinatesFromUserPrompt(prompt);
        _playerHandCoords.add(coords);

        //get dealer coordinates
        prompt = "Place cursor to the top left of dealer card icon and press return";
        coords = getCoordinatesFromUserPrompt(prompt);
        _dealerHandCoords.add(coords);
        initialise();
        adjustCoords(_playerHandCoords,_playerROI);
        adjustCoords(_dealerHandCoords,_dealerROI);
    }

    /**
     * Adjust coordinates supplied by the user to be more accurate
     */
    public void adjustCoords(ArrayList<int[]> coordsList, ArrayList<ArrayList<Rectangle>> roiList){
        initialise();
        //change x and y error to find most accurate position
        int previousXError = ImageMatrix._comXError;
        int previousYError = ImageMatrix._comYError;
        ImageMatrix._comXError = 4;
        ImageMatrix._comYError = 4;
        int[] coords;
        double accuracy = 0;
        for(int i = 0; i<roiList.size(); i++){
            update(roiList.get(i).get(0));
            coords = coordsList.get(i);
            coords[0]+=_lastXOffset;
            coords[1]+=_lastYOffset;
            System.out.println("" + _lastXOffset + ", " + _lastYOffset);
            accuracy+=_lastCardConfidence/roiList.size();
        }
        System.out.println("" + accuracy);
        //set x and y error back to previous values
        ImageMatrix._comXError = previousXError;
        ImageMatrix._comYError = previousYError;
        ////////////////////////////////////////////
        initialise();
    }
    public int[] getCoordinatesFromUserPrompt(String initialPrompt) throws QuitException{
        String userInput;
        String confirmationPrompt;
        boolean validInputFlag = false;
        int[] coords;
        while (!validInputFlag) {
            userInput = Utilities.getUserInput(initialPrompt);
            coords = Utilities.getCursorLocationAsArray();
            confirmationPrompt = "Use coords (" + coords[0] + "," + coords[1] + ")? (y/n)";
            if (Utilities.getYesNoQuitFromUser(confirmationPrompt)){
                return coords;
            }
        }
        return null;
    }
    public void loadConfigFile(String configFilepath){
        BufferedReader br = null;
        try{
            br = new BufferedReader(new FileReader(configFilepath));
            String line = br.readLine();
            Scanner s;
            String key,value;
            int x,y;
            while(line!=null){
                s = new Scanner(line);
                s.useDelimiter("\t");
                key = s.next();
                if (key=="playerCoords"){
                    x = Integer.parseInt(s.next());
                    y = Integer.parseInt(s.next());
                    _playerHandCoords.add(new int[]{x,y});
                }
                else if(key == "dealerCoords"){
                    x = Integer.parseInt(s.next());
                    y = Integer.parseInt(s.next());
                    _dealerHandCoords.add(new int[]{x,y});
                }
                else if(key == "iconWidth"){
                    _cardIconWidth = Integer.parseInt(s.next());
                }
                else if(key == "iconHeight"){
                    _cardIconHeight = Integer.parseInt(s.next());
                }
                else if(key == "iconDistance"){
                    _widthToNextCardIcon = Integer.parseInt(s.next());
                }
                line = br.readLine();
            }
            System.out.println("Config file found. Loading up " + configFilepath);
        }catch (IOException e) {
            System.out.println("Using default parameters.");
        }
        catch(NumberFormatException e){
            System.out.println("Config file " + configFilepath + " inproperly formatted.");
        }finally{
            try{
                br.close();
            }catch(Exception e){

            }
        }
    }
    public void serialiseThis(String filename) {
        File serialisedFile = new File(filename);
        if (serialisedFile.exists()) {
            System.out.println("Overwriting serialised file at " + filename);
        }
        try {
            FileOutputStream fileOut =
                    new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            out.close();
            fileOut.close();
            System.out.println("Serialized file saved at " + filename);
        } catch (IOException i) {
            i.printStackTrace();
        }
    }
    public void printCurrentParams(){

    }
}
