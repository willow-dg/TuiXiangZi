import javax.print.attribute.standard.Destination;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

public class MapEditPanel extends JPanel {
    private MainFrame frame;//主窗体绘图面板
    private BufferedImage image;//绘制关卡主图片
    private Graphics2D g2;//关卡图片的绘制对象
    private DrawMapPanel editPanel;//绘制面板
    private JToggleButton wall;//墙块按钮，可显示选种状态
    private JToggleButton player;//玩家按钮，可显示选种状态
    private JToggleButton box;//箱子按钮，可显示选种状态
    private JToggleButton destination;//目的地按钮，可显示选种状态
    private JButton save, clear, back;//保存按钮，清空按钮，返回按钮
    private RigidBody data[][];//用与保存关卡所有元素的数组
    private int offsetX = 100, offsetY = 80;//关卡图片在绘制面板上的横纵坐标偏移量

    //构造方法
    public MapEditPanel(MainFrame frame) {
        this.frame = frame;
        this.frame.setTitle("关卡编辑器（鼠标左键绘图，鼠标右键擦除）");
        init();//初始化组件
        addListeners();//添加组件事件监听器
    }

    void paintImage() {
        g2.setColor(Color.WHITE);//使用白色
        //填充一个覆盖整个图片的白色矩形
        g2.fillRect(0, 0, image.getWidth(), image.getHeight());
        for (int i = 0, ilength = data.length; i < ilength; i++) {//遍历用于保存关卡中所有元素的数组
            for (int j = 0, jlength = data[i].length; j < jlength; j++) {
                RigidBody rb = data[i][j];//取出模型对象
                if (rb != null) {//如果不为空
                    Image image = rb.getImage();//获取此模型的图片
                    g2.drawImage(image, i * 20, j * 20, 20, 20, this);//绘制在主图片中
                }
            }
        }
    }

    private void addListeners() {
        frame.addMouseListener(editPanel);//向主窗体中添加绘图面板的鼠标事件
        frame.addMouseMotionListener(editPanel);//向主窗体中添加绘图面板的鼠标拖拽事件
        clear.addMouseListener(e->{//单击清空按钮时
            data=new RigidBody[20][20];//用于保存关卡中所有元素的数组重新赋值
            repaint();//重绘组件
        });
        save.addActionListener(e->{//单击保存按钮时
            String name =GameMapUtil.CUSTOM_MAP_NAME;//设置关卡名称为自定义关卡名称
            GameMapUtil.createMap(data,name);//保存此关卡文件
            JOptionPane.showMessageDialog(MapEditPanel.this,"自定义关卡创建成功！请直接开始游戏。");//弹出对话框并提示创建成功
            back.doClick();//触发返回按钮的单击事件
        });

        back.addActionListener(e->{
            frame.removeMouseListener(editPanel);//从住窗口删除绘图面板的鼠标事件
            frame.removeMouseMotionListener(editPanel);//从住窗口删除绘图面板的鼠标拖拽事件
            frame.setPanel(new StarPanel(frame));//主窗口载入开始面板
        });
    }

    //初始化关卡编辑器中的组件
    private void init() {
        //使用400*400的彩色图片，为本游戏画面为600*600，此处为本游戏画面的缩放版
        image = new BufferedImage(400, 400, BufferedImage.TYPE_INT_RGB);
        g2 = image.getGraphics();//获取图片的绘图对象
        editPanel = new DrawMapPanel();//实例化绘制面板
        data = new RigidBody[20][20];//关卡数组与关卡中行列数保持一致
        setLayout(new BorderLayout());//使用边界布局

        //实例化按钮对象
        player = new JToggleButton("玩家");
        box = new JToggleButton("箱子");
        destination = new JToggleButton("目的地");
        wall = new JToggleButton("墙");
        save = new JButton("保存");
        clear = new JButton("清空");
        back = new JButton("返回");
        wall.setSelected(true);//默认选中墙按钮

        ButtonGroup group = new ButtonGroup();//创建按钮组
        group.add(player);//添加玩家按钮到按钮组中
        group.add(box);//添加箱子按钮到按钮组中
        group.add(destination);//添加目的地按钮到按钮组中
        group.add(wall);//添加墙按钮到按钮组中

        FlowLayout flow = new FlowLayout();//创建流布局
        flow.setHgap(20);//设置水平间隔20像素
        JPanel buttonPanel = new JPanel(flow);//创建按钮面板，采用流布局

        //向按钮面板中依次添加按钮
        buttonPanel.add(player);
        buttonPanel.add(box);
        buttonPanel.add(destination);
        buttonPanel.add(wall);
        buttonPanel.add(save);
        buttonPanel.add(clear);
        buttonPanel.add(back);

        add(editPanel, BorderLayout.CENTER);//将绘图面板放在中央位置
        add(buttonPanel, BorderLayout.SOUTH);//将按钮面板放在南部位置
    }

    //绘制地图
    class DrawMapPanel extends JPanel implements MouseListener, MouseMotionListener {
        boolean paintFlag = false;//绘制墙体标志
        int clickButton;//鼠标单击的按钮

        //图片绘制到面板
        public void paint(Graphics g) {
            paintImage();//绘制主图片
            g.setColor(Color.gray);//使用灰色
            g.fillRect(0, 0, getWidth(), getHeight());//绘制一个举行填充整个面板
            g.drawImage(image, offsetX, offsetY, this);
        }


        //绘制地图元素
        private void drawRigid(MouseEvent e) {
            RigidBody rb;//创建模型对象
            //创建鼠标是否在关卡内标志，由于窗体压缩了绘图面板，因此有些数据需要微调
            boolean inMap = e.getX() > offsetX && e.getX() < offsetX + 400 && e.getY() > offsetY + 20 && e.getY() < 400 + offsetY + 20;
            if (inMap && paintFlag) {//如果鼠标在关卡内的标志和绘制墙体标志都为true
                int x = (e.getX() - offsetX) / 20;//计算鼠标所在区域的模型的横坐标
                int y = (e.getY() - offsetY) / 20;//计算鼠标所在区域的模型的纵坐标
                if (wall.isSelected()) {//如果墙被按钮选中
                    rb = new wall();//模型按照墙进行实例化
                } else if (player.isSelected()) {//如果玩家按钮被选中
                    rb = new Player();//模型按照玩家进行实例化
                } else {//如果目的地按钮被选中
                    rb = new Destination();//模型按照目的地进行实例化
                }
                if (clickButton == MouseEvent.BUTTON1) {//如果按下的是鼠标左键
                    if (data[x][y] == null) {//选中的位置没有任何模型
                        data[x][y] = rb;//填充选中的对应模型
                    }
                } else if (clickButton == MouseEvent.BUTTON3) {//如果按下的说是鼠标右键
                    data[x][y] = null;
                }
                repaint();//重绘组件
            }
        }


        @Override
        public void mouseClicked(MouseEvent e) {

        }

        //按下鼠标
        @Override
        public void mousePressed(MouseEvent e) {
            paintFlag = true;//绘制墙体标志为true
            clickButton = e.getButton();//记录当前被按下的鼠标按键
            drawRigid(e);//绘制墙块
        }

        //释放鼠标
        @Override
        public void mouseReleased(MouseEvent e) {
            paintFlag = false;//绘制墙体标志为false
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

        //拖拽鼠标
        @Override
        public void mouseDragged(MouseEvent e) {
            drawRigid(e);//绘制墙块
        }

        @Override
        public void mouseMoved(MouseEvent e) {

        }
    }


}
