package com.mr.model;

import com.mr.util.GameImageUtil;

//墙块类
public class Wall extends RigidBody {
    public Wall() {
        super(GameImageUtil.wallImage);
    }
}
