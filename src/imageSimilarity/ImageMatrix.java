package imageSimilarity;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import javax.swing.*;
import utils.FastRGB;

public class ImageMatrix implements Serializable {
    public String _name;
    private boolean _debug = false;
    private int[][] _imageMatrix;
    private int _width = -1;
    private int _height = -1;
    private int _comX = -1;
    private int _comY = -1;
    private int _lastXOffset = 0;
    private int _lastYOffset = 0;
    private double _magnitude = -1;
    private double _sum = -1;

    public static int _comXError = 1;
    public static int _comYError = 1;

    public ImageMatrix(String filepath){
        String[] tokens = filepath.split("/");
        _name = tokens[tokens.length-1];
        BufferedImage bImg = null;
        try {
            bImg = ImageIO.read(new File(filepath));
        } catch (IOException e) {
            System.out.println("Error retrieving file " + filepath);
            _imageMatrix = null;
            return;
        }
        loadBufferedImage(bImg);
    }
    public ImageMatrix(BufferedImage bImg){
        loadBufferedImage(bImg);
    }

    public void loadBufferedImage(BufferedImage bImg){

        FastRGB rgbHandler = new FastRGB(bImg);
        _width = bImg.getWidth();
        _height = bImg.getHeight();
        _imageMatrix = new int[_width][_height];

        double comX = 0;
        double comY = 0;
        _magnitude = 0;
        _sum = 0;
        int val;
        for(int x = 0; x<_width; x++){
            for(int y = 0; y<_height; y++){
                val = rgbHandler.getInvertedGrayscale(x,y);
                _imageMatrix[x][y] = val;
                comX+=val*x;
                comY+=val*y;
                _magnitude+=val*val;
                _sum+=val;
            }
        }
        _magnitude = Math.sqrt(_magnitude);
        comX = comX/_sum;
        comY = comY/_sum;
        _comX = (int) Math.round(comX);
        _comY = (int) Math.round(comY);
    }
    public void print(){
        System.out.println("========================================");
        System.out.println("COM_X = " + _comX + "; COM_Y = " + _comY + "; magnitude = "
                                        + _magnitude);
        System.out.println("========================================");
        String str = "";
        for(int x = 0; x<_width; x++){
            str = "";
            for(int y = 0; y<_height; y++){
                str+=_imageMatrix[x][y] + "\t";
            }
            System.out.println(str);
        }
    }
    public void show(){
        BufferedImage bImg = new BufferedImage(_width,_height,BufferedImage.TYPE_BYTE_GRAY);
        for(int i = 0; i<_width; i++){
            for(int j = 0; j<_height; j++){
                bImg.setRGB(i,j,_imageMatrix[i][j]);
            }
        }
        JFrame frame = new JFrame();
        frame.getContentPane().add(new JLabel(new ImageIcon(bImg)));
        frame.setVisible(true);
    }
    /**
     * returns cos theta similarity through (a.b)/(|a||b|)
     * matrices overlayed such that the centre of mass coordinates
     * for each matrix are on top of each other. 1 = identical,
     * -1 = inverted, 0 = no correlation
     * @param other ImageMatrix instance that this ImageMatrix should be compared against
     * @return
     */
    private double similarity(ImageMatrix other, int comXOther, int comYOther){

        int widthOther = other.getWidth();
        int heightOther = other.getHeight();
        double otherMagnitude = other.getMagnitude();

        int[][] otherMatrix = other.getArray();

        int leftRegion = Math.min(_comX,comXOther);
        int rightRegion = Math.min(_width-_comX,widthOther-comXOther);
        int upRegion = Math.min(_comY,comYOther);
        int downRegion = Math.min(_height-_comY,heightOther-comYOther);

        double cosTheta = 0;

        int otherX,otherY;
        int thisX,thisY;
        for(int x = -leftRegion; x <rightRegion; x++){
            thisX = _comX + x;
            otherX = comXOther + x;
            for(int y = -upRegion; y<downRegion; y++){
                otherY = comYOther + y;
                thisY = _comY + y;
                cosTheta+=_imageMatrix[thisX][thisY]*otherMatrix[otherX][otherY];
            }
        }
        cosTheta/=(_magnitude*otherMagnitude);
        return cosTheta;
    }
    /**
     * Perform a cos theta similarity check against ImageMatrix 'other'. As the center of
     * mass coordinates may be off slightly for 'other' (as this will likely be generated on
     * the fly from screen grabs), adjust the other COM x and y coords by +-1 and return the
     * highest similarity.
     * @param other
     * @return
     */
    public double similarity(ImageMatrix other){
        int comXOther = other.getComX();
        int comYOther = other.getComY();
        int widthOther = other.getWidth();
        int heightOther = other.getHeight();
        double bestSimilarity = -1;
        double similarity = 0;
        int adjustedComX;
        int adjustedComY;

        for(int x = -_comXError; x<=_comXError; x++){
            adjustedComX = comXOther+x;
            for(int y = -_comYError; y<=_comYError; y++) {
                adjustedComY = comYOther + y;
                if (adjustedComX >= 0 && adjustedComX < widthOther) {
                    if (adjustedComY >= 0 && adjustedComY < heightOther) {
                        similarity = similarity(other, adjustedComX, adjustedComY);

                        if (similarity > bestSimilarity) {
                            bestSimilarity = similarity;
                            _lastXOffset = x;
                            _lastYOffset = y;
                        }
                    }
                }
            }
        }
        if (bestSimilarity<0.7){
            _lastXOffset = 0;
            _lastYOffset = 0;
        }
        return bestSimilarity;
    }
    public int[] getBoundingBox(WritableRaster raster){
        int pngWidth = raster.getWidth();
        int pngHeight = raster.getHeight();
        int startX = pngWidth-1;
        int endX = 0;
        int startY = pngHeight-1;
        int endY = 0;
        boolean pixelIsWhite = false;
        int[] rgb = new int[3];

        for(int x = 0; x<pngWidth;x++){
            for(int y = 0; y<pngHeight; y++){
                pixelIsWhite = (raster.getPixel(x,y,rgb)[0]==255);
                if (x<startX){
                    startX = x;
                }
                if (x>endX){
                    endX = x;
                }
                if (y<startY){
                    startY = y;
                }
                if (y>endY) {
                    endY = y;
                }
            }
        }
        System.out.println("Start x: " + startX + "; end x: " + endX);
        System.out.println("Start y: " + startY + "; end y: " + endY);
        return new int[] {startX,endX,startY,endY};
    }
    public int[][] getArray(){
        return _imageMatrix;
    }
    public double getMagnitude(){
        return _magnitude;
    }
    public int getWidth(){
        return _width;
    }
    public int getHeight(){
        return _height;
    }
    public int getComX(){
        return _comX;
    }
    public int getComY(){
        return _comY;
    }
    public int getLastXOffset(){
        return _lastXOffset;
    }
    public int getLastYOffset(){
        return _lastYOffset;
    }
}