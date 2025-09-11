package com.mr.util;

import com.mr.model.*;

import java.io.*;
//关卡工具类
public class GameMapUtil {
    private static final char NULL_CODE = '0';//空白区域使用的占位符
    private static final char WALL_CODE = '1';//墙使用的占位符
    private static final char BOX_CODE = '2';//箱子使用的占位符
    private static final char PLAYER_CODE = '3';//玩家使用的占位符
    private static final char DESTINATION_CODE = '4';//目的地使用的占位符
    private static final String MAP_PATH = "src/com/mr/map";//关卡存放的路径
    public static final String CUSTOM_MAP_NAME = "custom";//自定义关卡名称

    //创建地图方法
    static public void createMap(RigidBody[][] arr, String mapName) {
        StringBuffer data = new StringBuffer();//关卡文件将要写入的内容
        for (int i = 0, ilength = arr.length; i < ilength; i++) {
            for (int j = 0, jlength = arr[i].length; j < jlength; j++) {
                RigidBody rb = arr[i][j];//获取关卡的模型对象
                if (rb == null) {//如果是空白对象
                    data.append(NULL_CODE);//拼接空白区域的占位符
                } else if (rb instanceof Wall) {//如果是墙
                    data.append(WALL_CODE);//拼接墙的占位符
                } else if (rb instanceof Player) {//如果是玩家
                    data.append(PLAYER_CODE);//拼接玩家的占位符
                } else if (rb instanceof Box) {
                    data.append(BOX_CODE);//拼接箱子的占位符
                } else if (rb instanceof Destination) {
                    data.append(DESTINATION_CODE);//拼接目的地的占位符
                }
            }
            data.append("\n");//拼接换行
        }
        File mapFile = new File(MAP_PATH, mapName);//创建关卡文件对象
        //开始文件输出流
        try (FileOutputStream fos = new FileOutputStream(mapFile);) {
            fos.write(data.toString().getBytes());//将字符串写入文件
            fos.flush();//刷新输出流
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //读取文件数据（重载方法）
    public static Map readMap(int mapNum) {
        //将数字转化为字符，调用重载方法
        return readMap(String.valueOf(mapNum));
    }

    //读取关卡文件数据
    public static Map readMap(String mapName) {
        File f = new File(MAP_PATH, mapName);//获取关卡文件对象
        if (!f.exists()) {//如果文件不存在
            System.err.println("关卡不存在：" + mapName);
            return null;
        }
        RigidBody[][] data = new RigidBody[20][20];//关卡数组
        //开启缓冲输入流
        try (FileInputStream fis = new FileInputStream(f);
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader br = new BufferedReader(isr)) {
            String tmp = null;//读取一行数据的临时字符串
            int row = 0;//当前读取的行数
            while ((tmp = br.readLine()) != null) {//循环读取一行包含内容的字符串
                char[] codes = tmp.toCharArray();//将读取的字符串转换为字符数组
                //循环字符数组，并保证读取的行数不超过20
                for (int i = 0; i < codes.length && row < 20; i++) {
                    RigidBody rb = null;//准备保存到关卡数组中的模型对象
                    switch (codes[i]) {//判断读出的文字
                        case WALL_CODE://如果墙是占位符
                            rb = new Wall();//模拟以墙的占位符
                            break;
                        case BOX_CODE://如果箱子是占位符
                            rb = new Box();//模拟以箱子的占位符
                            break;
                        case PLAYER_CODE://如果是玩家的占位符
                            rb = new Player();//模拟以玩家的占位符
                            break;
                        case DESTINATION_CODE://如果是目的地的占位符
                            rb = new Destination();//模拟以目的地的占位符
                            break;
                    }
                    data[row][i] = rb;//将模型对象保存到关卡数组中
                }
                row++;//读取行数递增
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Map(data);//返回包含此关卡对象
    }


    //读取自定义对象
    public static Map readCustomMap() {
        File f = new File(MAP_PATH, CUSTOM_MAP_NAME);//读取自定义关卡文件对象
        if (!f.exists()) {//如果文件不存在
            return null;
        }
        Map map = readMap(CUSTOM_MAP_NAME);//读取自定义文件的数据
        return map;//返回自定义关卡对象
    }

    //获取关卡总数
    public static int getLevelCount() {
        File dir = new File(MAP_PATH);//读取关卡存放路径
        if (dir.exists()) {//如果该路径是文件夹
            File[] maps = dir.listFiles();//获取该文件夹下的所有文件
            for (File f : maps) {//遍历这些文件
                if (CUSTOM_MAP_NAME.equals(f.getName())) {//如果存在自定义文件夹
                    return maps.length - 1;//关卡总数=文件数-1
                }
            }
            return maps.length;//总关卡数=文件数
        }
        return 0;//没有关卡
    }

    //清理自定义关卡
    public static void clearCustomMap() {
        File f = new File(MAP_PATH, CUSTOM_MAP_NAME);//创建自定义关卡文件对象
        if (f.exists()) {//如果文件存在
            f.delete();//删除
        }
    }


}
