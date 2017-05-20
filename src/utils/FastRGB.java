package utils;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class FastRGB
{
    private int width;
    private int height;
    private boolean hasAlphaChannel;
    private int pixelLength;
    private byte[] pixels;

    public FastRGB(BufferedImage image)
    {

        pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        width = image.getWidth();
        height = image.getHeight();
        hasAlphaChannel = image.getAlphaRaster() != null;
        pixelLength = 3;
        if (hasAlphaChannel)
        {
            pixelLength = 4;
        }

    }

    public int getInvertedGrayscale(int x, int y)
    {
        int pos = (y * pixelLength * width) + (x * pixelLength);

        float argb = 0; // 255 alpha
        if (hasAlphaChannel)
        {
            argb = (((int) pixels[pos++] & 0xff) << 24); // alpha
        }

        argb += ((int) pixels[pos++] & 0xff)*0.0722; // blue
        argb += ((int) pixels[pos++] & 0xff)*0.7152; // green
        argb += ((int) pixels[pos++] & 0xff)*0.2126; // red
        return (int) (255-argb);
    }
}