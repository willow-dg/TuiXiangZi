package com.mr.model;

import java.awt.*;



//模型抽象类
public abstract class RigidBody {
    public int x;//横坐标索引
    public int y;//纵坐标索引
    private Image image;//图片

    //第一种构造方法
    RigidBody(Image image) {
        this.image = image;
    }
    //第二种构造方法
    RigidBody(int x, int y) {
        this.x = x;
        this.y = y;
    }

    //获取模型图片，重设模型图片
    public Image getImage() {
        return image;
    }
    public void setImage(Image image) {
        this.image = image;
    }

    //重写equals()方法
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj==null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RigidBody other = (RigidBody) obj;
        if (x != other.x)
            return false;
        if (y != other.y)
            return false;
        return true;
    }



}
