package utils; /**
 * Created by user on 14/05/17.
 */
import java.lang.System;
import java.io.IOException;
import java.lang.Thread;
import java.awt.AWTException;
import utils.Utilities;
import blackjackExceptions.QuitException;

import imageSimilarity.websites.WebsiteObserver;


public class Run{
    public boolean _cancelFlag = false;
    protected String _casinoUKFolderPath = "/Resources/Images/casino_com_uk/";
    protected String _casinoUKConfigPath = "/casino.com.config";
    protected String _casinoUKSerialisedFilename = "/casino.com.ser";
    public static void main(String[] args){
        new Run().start();
    }
    public Run(){

    }
    public void start(){
        while(true){
            String option = Utilities.getUserInput("Enter command: ");
            try{
                runCommand(option);
            }catch (IOException e){

            }catch (InterruptedException e){

            }
        }
    }

    private void runCommand(String option) throws IOException, InterruptedException{
        String[] tokens = option.split("\\s");
        if (tokens.length==0){
            return;
        }
        if (tokens[0].equals("mouseDebug")) {
            mouseDebug();
        }
        else if (tokens[0].equals("run")){
            System.out.println("Running with casino.com/uk templates.");
            runBlackjack(_casinoUKSerialisedFilename,_casinoUKConfigPath,_casinoUKFolderPath);

        }
        else if (tokens[0].equals("q")){
            System.out.println("Exiting");
            System.exit(0);
        }
        else if (tokens.length>1 && tokens[1].equals("-h")){
            help();
        } else {
            System.out.println("Unknown command. Enter '-h' for help.");
        }
    }
    public void mouseDebug() throws IOException, InterruptedException{
        boolean runDebug = true;
        System.out.println("Enter 'q' to quit");
        MouseDebug md = new MouseDebug(this);
        Thread t1 = new Thread(md,"MouseDebug thread");
        t1.start();
        while(runDebug){
            while (System.in.available()>0){
                if (Utilities.getUserInput().equals("q")){
                    System.out.println("Exiting");
                    runDebug = false;
                    break;
                }
            }
        }
        _cancelFlag = true;
        t1.join();
        _cancelFlag = false;
    }
    public void help(){

    }
    public void runBlackjack(String serialisedFilename,String configFilepath, String folderPath){
        WebsiteObserver we;
        try{
            we = getWebsiteObserver(serialisedFilename,configFilepath,folderPath);

        } catch(AWTException e){

        } catch(QuitException e){
            System.out.println("Quitting");
        } catch(ClassNotFoundException e){
            System.out.println("Class not found");
        } catch(IOException e){
            System.out.println("IOException thrown when loading serialised file");
        }
    }
    public WebsiteObserver getWebsiteObserver(String serialisedFilename, String configFilepath, String folderpath)
                                    throws AWTException, QuitException, ClassNotFoundException, IOException{
        WebsiteObserver we;
        int loadOption = -1;
        if (Utilities.fileExists(serialisedFilename)) {
            if (Utilities.getYesNoQuitFromUser("Load saved object? (y/n/q)")) {
                return Utilities.loadSerialisedWebsiteObserver(serialisedFilename);
            }
        }
        if (Utilities.fileExists(serialisedFilename)) {
            if (Utilities.getYesNoQuitFromUser("Load from config file? (y/n/q)")) {
                return new WebsiteObserver(folderpath, configFilepath);
            }
        }
        return new WebsiteObserver(folderpath);
    }

}
