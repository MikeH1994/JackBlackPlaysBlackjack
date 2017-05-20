package utils;

import java.awt.*;
import utils.Run;
/**
 * Created by user on 18/05/17.
 */

public class MouseDebug implements Runnable{
    Run _parent;
    public MouseDebug(Run parent){
        _parent = parent;
    }
    public void run(){
        Point point;
        try{
            while (!_parent._cancelFlag) {
                point = MouseInfo.getPointerInfo().getLocation();
                System.out.println("x = " + point.x + ", y = " + point.y);
                Thread.sleep(1000);
            }

        }catch(InterruptedException e) {
            System.out.println("arg");
        }
    }
}
