package com.mr.model;
import com.mr.util.GameImageUtil;

//箱子
public class Box extends RigidBody {
    private boolean arrived=false;//是否到达目的地
    public Box() {
        super(GameImageUtil.boxImage1);
    }
    public Box(int x,int y) {
        super(x,y);
        setImage(GameImageUtil.boxImage1);
    }
    public void arrive() {//到达
        setImage(GameImageUtil.boxImage2);
        arrived=true;
    }
    public void leave() {//离开
        setImage(GameImageUtil.boxImage1);
    }
    public boolean isArrived() {
        return arrived;
    }
}
