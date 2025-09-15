package com.mr.panel;

import com.mr.util.GameMapUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

//主窗体
public class MainFrame extends JFrame {
    private int width = 605;//不可调整大小时的宽度，可调整大小宽度选择616
    private int height = 627;//不可调整大小时的高度，可调整大小高度选择638

    //构造函数
    public MainFrame() {
        setTitle("推箱子");//设置标题
        setSize(width, height);//设置宽高
        setVisible(true);//窗体可见
        setResizable(false);//不可调整大小
        Toolkit toolkit = Toolkit.getDefaultToolkit();//获取系统该默认组件工具包
        Dimension d = toolkit.getScreenSize();//获取屏幕尺寸，赋给一个二维坐标对象
        //主窗口在屏幕中间
        setLocation((d.width- getWidth())/ 2, (d.height - getHeight()) / 2);
//        setBackground(Color.green);// 窗体背景颜色
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);//单击关闭窗体不做任何操作
        addListener();//添加监听器
        setPanel(new StarPanel(this));      //载入开始面板
        GameMapUtil.clearCustomMap();//删除自定义关卡
    }

    //添加监听
    private void addListener() {
        addWindowListener(new WindowAdapter() {     //添加窗体事件监听
            public void windowClosing(WindowEvent e) {      //窗体关闭
                //弹出选择对话框，并记录用户选择
                int closeCode = JOptionPane.showConfirmDialog(MainFrame.this,
                        "是否退出游戏？", "提示！", JOptionPane.YES_NO_OPTION);
                if (closeCode == JOptionPane.YES_OPTION) {      //如果用户选择确定
                    System.exit(0);     //关闭本游戏
                }
            }
        });
    }

    //切换游戏面板
    public void setPanel(JPanel Panel) {
        Container c = getContentPane();       //获取主容器对象
        c.removeAll();        //删除容器中所有组件
        c.add(Panel,BorderLayout.CENTER);     //容器添加面板
        c.validate();     //容器重新验证所有组件
        c.repaint();
    }

}
