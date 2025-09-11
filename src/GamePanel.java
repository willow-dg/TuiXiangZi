import javax.print.attribute.standard.Destination;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Map;

public class GamePanel extends JPanel implements KeyListener, MouseListener, MouseMotionListener {

    private MainFrame frame;//主窗体对象
    private int level;//关卡编号
    private RigidBody[][] data = new RigidBody[20][20];//管卡中的所有元素
    private BufferedImage image;//关卡图片
    private Graphics2D g2;//主图片的绘图对象
    private Player player;//玩家对象
    private ArrayList<Box> boxes;//箱子列表


    public GamePanel(MainFrame frame, int level) {
        this.frame = frame;
        this.level = level;
        Map map;        //管卡对象
        if (level < 1) {//如果关卡数小于1
            map = GameMapUtil.readCustomMap();      //读取自定义关卡对象
            this.level = 0;//将关卡数设置0，方便顶一下一关
            if (map == null) {//如果没有自定义关卡文件
                this.level = 1;//将关卡设为第一关
                map = GameMapUtil.readMap(1);//kaishi第一关
            }
        } else {//如果guanqia大于等于1
            map = GameMapUtil.readMap(level);//则读取对应关卡数的关卡对象
        }
        data = map.getMapData();//获取用于保存关卡中所有元素的数组
        player = map.getPlayer();//获取关卡中玩家对象
        boxes = map.getBoxes();//获取关卡中的箱子列表
        //zhutupian为600*600的彩图
        image = new BufferedImage(600, 600, BufferedImage.TYPE_INT_ARGB);
        g2 = image.createGraphics();//获取主图片的绘图对象
        this.frame.addKeyListener(this);//为主窗体添加键盘事件监听器
        this.frame.setFocusable(true);//主窗体获取焦点
        this.frame.setTitle("推箱子（按ESC重新开始）");//修改主窗体标题


    }
    //玩家移动
    private void moveThePlayer(int xNext1, int xNext2, int yNext1, int yNext2) {
        if (data[xNext1][yNext1] instanceof Wall) {//如果玩家前方是墙
            return;//什么都不做
        }
        Box box = new Box(xNext1, yNext1);//在用户前方位置创建箱子对象
        if (boxes.contains(box)) {//ruguo这个箱子在箱子列表中是存在的
            int index = boxes.indexOf(box);//获取该箱子的在列表中的索引
            box = boxes.get(index);//qvchu1列表中该箱子的对象
            if (data[xNext2][yNext2] instanceof Wall) {//如果箱子前面还墙
                return;//啥也不用做
            }
            if (boxes.contains(new Box(xNext2, yNext2))) {//如果箱子钱还是箱子
                return;//也是不用做
            }
            if (data[xNext2][yNext2] instanceof Destination) {//ruguo箱子前方是目的地
                box.arrive();//箱子到达
            } else if (box.isArrived()) {//如果箱子就在目的地上
                box.leave();//箱子离开
            }
            box.x = xNext2;//xiangzi在新的位置上
            box.y = yNext2;
        }
        player.x = xNext1;//玩家在新的位置上
        player.y = yNext1;
    }
   //绘制图片
   private void paintImage() {
        g2.setColor(Color.WHITE);//使用白色
        g2.fillRect(0, 0, getWidth(), getHeight());//绘制一个矩形来填充整张图片
        for (int i=0,ilength=data.length;i<ilength;i++) {//遍历用于保存关卡中所有元素的数组
            for (int j=0,jlength=data[0].length;j<jlength;j++) {
                RigidBody rb=data[i][j];//获取模型对象
                if (rb!=null){
                    Image image=rb.getImage();//获取模型图片
                    g2.drawImage(image,i*30,j*30,30,30,this);//在对应位置上绘制图片
                }
            }
        }
        for (Box box : boxes) {//便利所有箱子
            //在对应位置上绘制图片
            g2.drawImage(box.getImage(),box.x*30,box.y*30,30,30,this);
        }
        //绘制玩家图片
       g2.drawImage(player.getImage(),player.x*3,player.y*30,30,30,this);
   }


    public void paint(Graphics g) {
        paintImage();       //绘制图片
        g.drawImage(image, 0, 0, this);        //将图片绘制到面板中

        boolean finish=true;//用于判断当前关卡是否结束标志
        for (Box box : boxes) {//遍历箱子列表
            finish &=box,isArrived();//将结束标志与箱子到达的状态做与运算
        }
        if (finish &&boxes.size()>0) {//ruguo1所有箱子都到达目的地且箱子的个数大于0
            gotoAnotherLevel(level+1);
        }
    }

    //进入其他关卡
    private void gotoAnotherLevel(int level) {
        frame.removeKeyListener(this);//主窗体删除本类实现的键盘事件
        //创建线程，创建Runnable接口的匿名类
        Thread t=new Thread(()->{
            try{
                Thread.sleep(500);//0.5s之后
            }catch (Exception e){
                e.printStackTrace();
            }
            if(level>GameMapUtil.getLevelCount()){//如果传入的关卡数大于最大关卡数
                frame.setPanel(new starPanel(frame));//进入开始面板
                JOptionPane.showMessageDialog(frame,"通关了！");//弹出通关对话
            }else {
                frame.setPanel(new GamePanel(frame, level));//进入对应关卡
            }
        });
        t.start();//开启线程

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();//获取按下的按键
        int x = player.x;//记录玩家的横坐标索引
        int y = player.y;//记录玩家的纵坐标索引
        switch (key) {
            case KeyEvent.VK_UP:
                moveThePlayer(x, y - 1, x, y - 2);
                break;
            case KeyEvent.VK_DOWN:
                moveThePlayer(x, y + 1, x, y + 2);
                break;
            case KeyEvent.VK_LEFT:
                moveThePlayer(x - 1, y, x - 2, y);
                break;
            case KeyEvent.VK_RIGHT:
                moveThePlayer(x + 1, y, x + 2, y);
                break;
            case KeyEvent.VK_ESCAPE://rugouyonghu按下的是ESC
                gotoAnotherLlevel(level);//重新开始当前关卡
                break;
        }
        repaint();//chonghui面板
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
