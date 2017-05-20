package imageSimilarity;
import java.io.File;
import java.io.Serializable;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;


/**
 * Created by user on 14/05/17.
 */
public abstract class ImageMatrixHandler implements Serializable{
    final protected String[] _imageNames = {"A","2","3","4","5","6","7","8","9","10","J","Q","K"};
    final protected String _imageFileExtension = ".png";
    final protected String dir = System.getProperty("user.dir");
    protected ImageMatrix[] _imageMatrices;
    protected boolean _debug = true;

    protected int _lastCardIdentified = -1;
    protected int _lastXOffset = 0;
    protected int _lastYOffset = 0;
    protected double _lastCardConfidence = -1;

    public ImageMatrixHandler(String subfolder){
        _imageMatrices = new ImageMatrix[13];
        String imagePath;
        for(int i = 0; i<13; i++){
            imagePath = dir + subfolder + _imageNames[i] + _imageFileExtension;
            _imageMatrices[i] = new ImageMatrix(imagePath);
        }
    }
    public int identify(ImageMatrix other){
        double bestSimilarity = -1;
        double similarity;
        int bestCard = -1;
        for(int i = 0; i<13; i++){
            similarity = _imageMatrices[i].similarity(other);
            if(_debug){
                System.out.println("Card: " + _imageMatrices[i]._name + "; Similarity: " + similarity);
            }
            if (similarity>bestSimilarity){
                bestSimilarity = similarity;
                bestCard = i;
                _lastXOffset = _imageMatrices[i].getLastXOffset();
                _lastYOffset = _imageMatrices[i].getLastYOffset();
            }
        }
        _lastCardIdentified = bestCard;
        _lastCardConfidence = bestSimilarity;
        return bestCard;
    }

    public int getLastCardIdentified(){
        return _lastCardIdentified;
    }
    public double getLastCardConfidence(){
        return _lastCardConfidence;
    }
}
