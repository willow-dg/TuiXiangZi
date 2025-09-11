import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;




public class MainFrame extends JFrame {
    private int width=605,height=627;
    //构造函数
    public MainFrame() {
        setTitle("推箱子");
        setResizable(false);
        setSize(width,height);
        Toolkit toolkit = Toolkit.getDefaultToolkit();//获取屏幕尺寸
        Dimension screenSize = toolkit.getScreenSize();
        //主窗口在屏幕中间
        setLocation(screenSize.width/2-width/2,screenSize.height/2-height/2);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addListener();
        setPanel(new StarPanel(this));      //载入开始面板
        GameMapUtil.clearCustomMap();
    }
    //添加监听
    private void addListener() {
        addWindowListener(new WindowAdapter() {     //添加窗体事件监听
            public void windowClosing(WindowEvent e) {      //窗体关闭
                int closeCode=JOptionPane.showConfirmDialog(MainFrame.this,
                        "是否退出游戏？","提示！",JOptionPane.YES_NO_OPTION);
                if(closeCode==JOptionPane.YES_OPTION){      //如果用户选择确定
                    System.exit(0);     //关闭本游戏
                }
            }
        });
    }
    //切换游戏场景
    public void setPanel(JPanel Panel) {
        Container contentPane = getContentPane();       //获取主容器对象
        contentPane.removeAll();        //删除容器中所有组件
        contentPane.add(Panel);     //容器添加面板
        contentPane.validate();     //容器重新验证所有组件
        contentPane.repaint();
    }






    public static void main(String[] args) {
        MainFrame a = new MainFrame();
    }
}
