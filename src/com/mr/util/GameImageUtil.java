package com.mr.util;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
//图片工具类
public class GameImageUtil {
    private static final String IMAGE_PATH = "src/com/mr/image";//图片存放相对路径
    public static BufferedImage playerImage;//玩家图片
    public static BufferedImage boxImage1;//箱子未到达目的地的图片
    public static BufferedImage boxImage2;//箱子已到达目的地的图片
    public static BufferedImage wallImage;//墙图片
    public static BufferedImage destinationImage;//目的地图片
    public static BufferedImage backgroundImage;//背景图片

    static {
        try{
            playerImage= ImageIO.read(new File(IMAGE_PATH,"pic5.png"));
            boxImage1=ImageIO.read(new File(IMAGE_PATH,"pic2.png"));
            boxImage2=ImageIO.read(new File(IMAGE_PATH,"pic3.png"));
            wallImage=ImageIO.read(new File(IMAGE_PATH,"pic1.png"));
            destinationImage=ImageIO.read(new File(IMAGE_PATH,"pic4.png"));
            backgroundImage=ImageIO.read(new File(IMAGE_PATH,"pic-back.png"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
