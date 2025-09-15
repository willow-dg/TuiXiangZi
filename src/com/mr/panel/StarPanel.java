package com.mr.panel;

import com.mr.util.GameImageUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

//开始面板类（继承面板类，实现键盘事件监听接口）
public class StarPanel extends JPanel implements KeyListener {

    MainFrame frame;            //主窗体
    BufferedImage image;        //面板中显示的图片
    Graphics2D g2;              //图片绘图对象
    int x = 160, y;             //横纵坐标
    final int y1 = 320;         //第一个选项的纵坐标
    final int y2 = 420;         //第二个选项的纵坐标

    //构造方法
    public StarPanel(MainFrame frame) {
        this.frame = frame;
        this.frame.addKeyListener(this);
        this.frame.setTitle("推箱子");
        //使用600*600的彩图
        image = new BufferedImage(600, 600, BufferedImage.TYPE_INT_RGB);
        g2 = image.createGraphics();
        y = y1;    //默认选择第一个选项
    }
    //绘制内容
    private void paintImage() {
    g2.drawImage(GameImageUtil.backgroundImage,0,0,this);
    g2.setColor(Color.white);//使用黑色
    g2.setFont(new Font("黑体",Font.BOLD,40));//字体
    g2.drawString("开始游戏",230,y1+30);//绘制第一个选项的文字
    g2.drawString("关卡编辑器",230,y2+30);//绘制第二个选项的文字
    g2.drawImage(GameImageUtil.playerImage,x,y,this);//将玩家图片作为选择图标
    }
    //将图片显示在到面板上（自动调用）
    @Override
    public void paint(Graphics g) {
        paintImage();       //绘制图片
        g.drawImage(image, 0, 0, this);//将图片绘制到面板中
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    //键盘监听
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();       //获取按键的编码
        switch (key) {       //判断按键
            case KeyEvent.VK_ENTER:     //如果是Enter键
                switch (y) {        //判断图标的坐标
                    case y1:        //如果选中第一个选项
                        frame.removeKeyListener(this);      //删除当前键盘事件
                        frame.setPanel(new GamePanel(frame, 0));        //进入游戏面板
                        break;
                    case y2:        //如果选中第二个选项
                        frame.removeKeyListener(this);      //删除当前键盘事件
                        frame.setPanel(new MapEditPanel(frame));        //进入关卡编辑器面板
                        break;
                }
                break;
            case KeyEvent.VK_UP:        //如果是上箭头，就采用下箭头的逻辑
            case KeyEvent.VK_DOWN:      //如果是下剪头
                if (y == y1) {      //如果图标选中第一个选项
                    y = y2;     //更换选中第二个选项
                } else {
                    y = y1;     //更换选中第一个选项
                }
                break;
        }
        repaint();      //重绘面板

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }


}
