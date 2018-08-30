
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class TankFrame extends Frame {
    //游戏区域大小
    public static final int GAME_WIDTH=800;
    public static final int GAME_HEIGHT=600;

    //坦克初始位置(50,50)
    Tank myTank=new Tank(400,550,true,Tank.Direction.STOP,this);

    //两堵墙
    Wall w1=new Wall(320,520,20,80,this), w2=new Wall(480,520,20,80,this),
         w3=new Wall(320,520,160,20,this);

    //
    Explode e=new Explode(70,70,this);

    //存放炮弹的数组
    List<Missile> missileList=new ArrayList<Missile>();

    //存放爆炸的数组
    List<Explode> explodes =new ArrayList<Explode>();

    //存放敌人坦克
    List<Tank> tanks=new ArrayList<Tank>();

    //双重缓存用的
    Image offScreenImage =null;

    //道具血块
    BloodModule b=new BloodModule();

    //加载窗口
    public void launchFrame(){
        for(int i=0;i<10;i++){
            tanks.add(new Tank(50+40*(i+1),50,false,Tank.Direction.D,this));
        }
        //this.setLocation(400,300);
        this.setSize(GAME_WIDTH,GAME_HEIGHT);
        this.setTitle("TankWar");
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }

        });
        //设置窗口大小不可变
        this.setResizable(false);

        //设置背景颜色为白色
        this.setBackground(Color.BLACK);

        //给按键添加监听
        this.addKeyListener(new KeyMonitor());

        //使得窗口可见
        setVisible(true);

        //开启坦克移动线程
        new Thread(new PaintThread()).start();
    }

    //一辆正方形的坦克(蓝色)  不不不 我们要一个牛逼的坦克
        public void paint(Graphics g){
            Color cc= g.getColor();
            g.setColor(Color.WHITE);
            g.drawString("炮弹存活:"+missileList.size(),60,50);
            g.drawString("爆炸存活"+explodes.size(),60,70);
            g.drawString("坦克存活"+tanks.size(),60,90);
            g.drawString("坦克血量"+myTank.getLife(),60,110);
            g.setColor(cc);
            //死光了在加15个
            if(tanks.size()<=0){
                for(int i=0;i<15;i++){
                    b.setLive(true);
                    b.draw(g);
                    tanks.add(new Tank(50+40*(i+1),50,false,Tank.Direction.D,this));
                    Tank.fire=Tank.fire-1;
                }
            }

            for(int i=0;i<missileList.size();i++){
                Missile m=missileList.get(i);
               // if(!m.isLive()) missileList.remove(m);
                //打敌人坦克
                m.hitTanks(tanks);
                //敌人坦克打自己
                m.hitTank(myTank);
                //画坦克
                m.draw(g);
                //两堵墙
                m.hitWall(w1);
                m.hitWall(w2);
                m.hitWall(w3);

            }

            for(int i=0;i<explodes.size();i++){
                Explode e= explodes.get(i);
                e.draw(g);
            }

            for(int i=0;i<tanks.size();i++){
                Tank t=tanks.get(i);
                t.collidesWithWall(w1);
                t.collidesWithWall(w2);
                t.collidesWithWall(w3);

                t.collidesWithTanks(tanks);
                t.draw(g);
            }

            myTank.draw(g);
            myTank.eat(b);
            //两堵蓝色的墙
            Color c2=g.getColor();
            g.setColor(Color.BLUE);
            w1.draw(g);
            w2.draw(g);
            w3.draw(g);
            g.setColor(c2);

            //血块道具
            b.draw(g);



        }



    //双缓冲消除闪烁
    public void update(Graphics g) {
        if(offScreenImage == null){
            offScreenImage = this.createImage(GAME_WIDTH,GAME_HEIGHT);
        }
        //第二张屏幕得到画笔
        Graphics gOffScreen =offScreenImage.getGraphics();

        //得到第二张屏幕的颜色(也就是第一个屏幕的画笔)
        Color c= gOffScreen.getColor();

        //将第二张屏幕颜色改为白色
        gOffScreen.setColor(Color.black);

        //和第一张屏幕一样大
        gOffScreen.fillRect(0,0,GAME_WIDTH,GAME_HEIGHT);

        //设置第二张屏幕颜色为白色
        gOffScreen.setColor(c);

        //重画坦克
        paint(gOffScreen);

        //重画屏幕
        g.drawImage(offScreenImage,0,0,null);

    }

    //坦克线程 移动
    private class PaintThread implements Runnable{

        @Override
        public void run() {
            while (true){
                repaint();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //坦克移动按键
    public class KeyMonitor extends KeyAdapter{
        //按键
        public void keyPressed(KeyEvent e) {
            myTank.KeyPressed(e);
        }

        //抬起
        public void keyReleased(KeyEvent e) {
            myTank.keyReleased(e);
        }
    }


}
