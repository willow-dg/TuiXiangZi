package com.mr.model;

import javax.swing.*;
import java.util.ArrayList;

//关卡类
public class Map extends JPanel {
    private RigidBody matrix[][];//用于保存关卡中所以元素的数组
    private Player player;//关卡中的玩家对象
    private ArrayList<com.mr.model.Box> boxes;//关卡中包含的箱子列表
    //构造方法
    public Map(RigidBody matrix[][]) {
        this.matrix = matrix;
        player = new Player();
        boxes = new ArrayList<>();
        init();//关卡初始化
    }
    //关卡初始化
    private void init() {
        //遍历关卡数组
        for(int i=0,ilength=matrix.length;i<ilength;i++) {
            for (int j=0,jlength=matrix[i].length;j<jlength;j++) {
                RigidBody rb = matrix[i][j];//读出模型对象
                if (rb instanceof Player) {//如果是玩家
                    player.x=i;//记录用户横坐标索引
                    player.y=j;//记录用户纵坐标索引
                    matrix[i][j] = null;//将该索引下的模型对象清除
                } else if (rb instanceof com.mr.model.Box) {
                    com.mr.model.Box box =new com.mr.model.Box(i,j);//创建对应该横纵坐标索引的箱子对象
                    boxes.add(box);//箱子列表保存此箱子
                    matrix[i][j] = null;//将该索引下的模型对象清除
                }
            }
        }
    }


    //获取关卡中的所有元素
    public Player getPlayer() {
        return player;
    }
    public ArrayList<Box> getBoxes() {
        return boxes;
    }
    public RigidBody[][] getMapData() {
        return matrix;
    }


}
