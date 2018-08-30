import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;

public class Tank {
    //设置坦克速度
    public static final int XSpeed=5;
    public static final int YSpeed=5;

    //设置坦克大小
    public static  final  int WIDTH=30;
    public static  final  int HEIGHT=30;

    //坦克火力
    public static  int  fire=95;

    //是否活着
    private boolean live =true;

    //血条
    private  BloodBar bb=new BloodBar();

    //"管家"
    TankFrame tf=null;

    //生命值
    private int life=100;

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    private boolean good;

    //设置坦克出生位置
    private int x,y;

    //坦克old位置
    private int oldX,oldY;

    //随机方向
    private static Random r =new Random();

    //设置坦克方向
    private boolean BL=false,BU=false,BD=false,BR=false;

    //枚举坦克的方向
    enum  Direction{L,U,R,D,STOP}

    //坦克默认位置为停(向上)
    private Direction dir =Direction.STOP;

    //坦克炮塔方向(默认朝下)
    private Direction ptDir=Direction.D;

    private int step =r.nextInt(12) +4;

    public Tank(int x, int y,boolean good) {
        this.x = x;
        this.y = y;
        this.oldX=x;
        this.oldY=y;
        this.good=good;
    }

    //控制着坦克的开火
    public Tank(int x,int y,boolean good,Direction dir,TankFrame tf){
        this(x,y,good);
        this.dir=dir;
        this.tf=tf;
    }

    //画一个坦克!!!
    public void draw(Graphics g){
        //先判断坦克是否活着
        if(!live) {
            //敌人坦克移除
            if(!good){
                tf.tanks.remove(this);
            }
            return;
        }
        Color c =g.getColor();
        int s;
        //判断坦克是好的还是坏人
        if(good){s=0;}
        else {s=1;}
        this.drawTank(x,y,g,ptDir,s);
        g.setColor(c);

        if(good) bb.draw(g);

        move();
    }

    //坦克移动
    void move(){
        //上一步坦克所在的位置
        this.oldX=x;
        this.oldY=y;

        switch(dir){
            case L:
                x-=XSpeed;
                break;
            case U:
                y-=YSpeed;
                break;
            case R:
                x+=XSpeed;
                break;
            case D:
                y+=YSpeed;
                break;
            case STOP:
                break;
        }
        if(this.dir!=Direction.STOP){
            this.ptDir=this.dir;
        }

        //使得坦克无法出界
        if(x<0) x=0;
        if(y<30) y=30;
        if(x+Tank.WIDTH>TankFrame.GAME_WIDTH) x=TankFrame.GAME_WIDTH-Tank.WIDTH;
        if(y+Tank.HEIGHT>TankFrame.GAME_HEIGHT) y=TankFrame.GAME_HEIGHT-Tank.HEIGHT;

        //改变方向
        if(!good){
            Direction[] dirs = Direction.values();
            if(step==0){
                step=r.nextInt(12)+4;

                //转换方向,当随机产生的step减到0时,转换方向
                int rn=r.nextInt(dirs.length);
                dir=dirs[rn];
            }
            step--;
            //坦克火力程度
            if(r.nextInt(100)>fire)  this.fire();
        }


    }

    //图形组合画一个坦克(2种状态,4个方向)
    public void drawTank(int x,int y,Graphics g,Direction direction,int type){
        //设置敌我颜色
        switch(type){
            case 0:
                g.setColor(Color.cyan);
                break;
            case 1:
                g.setColor(Color.yellow);
                break;
        }
        switch(direction){
            case STOP:
                //1.画出左边的履带
                g.fill3DRect(x,y, 7, 30,false);
                //2.画出右边的矩形
                g.fill3DRect(x+23,y, 7, 30,false);
                //3.画出中间的矩形
                g.fill3DRect(x+7,y+7, 16, 20,false);
                //4.画出中间的圆型
                g.fillOval(x+8,y+10, 13, 13);
                //5.画炮管
                g.drawLine(x+15,y,x+15,y+15);
                break;
            case U:
                //1.画出左边的履带
                g.fill3DRect(x,y, 7, 30,false);
                //2.画出右边的履带
                g.fill3DRect(x+23,y, 7, 30,false);
                //3.画出中间的底盘
                g.fill3DRect(x+7,y+7, 16, 20,false);
                //4.画出中间的炮台
                g.fillOval(x+8,y+10, 13, 13);
                //5.画炮管
                g.drawLine(x+15,y,x+15,y+15);
                break;
            case R:
                //1.画出上面的履带
                g.fill3DRect(x,y, 30,7,false);
                //2.画出下面的履带
                g.fill3DRect(x,y+23, 30, 7,false);
                //3.画出中间的底盘
                g.fill3DRect(x+3,y+7, 20, 16,false);
                //4.画出中间的炮台
                g.fillOval(x+5,y+8, 13, 13);
                //5.画炮管
                g.drawLine(x+15,y+14,x+30,y+14);
                break;
            case D:
                //1.画出左边的履带
                g.fill3DRect(x,y, 7, 30,false);
                //2.画出右边的履带
                g.fill3DRect(x+23,y, 7, 30,false);
                //3.画出中间的底盘
                g.fill3DRect(x+7,y+3, 16, 20,false);
                //4.画出中间的炮台
                g.fillOval(x+8,y+5, 13, 13);
                //5.画炮管
                g.drawLine(x+14,y+15,x+14,y+30);
                break;
            case L:
                //1.画出上面的履带
                g.fill3DRect(x,y, 30,7,false);
                //2.画出下面的履带
                g.fill3DRect(x,y+23, 30, 7,false);
                //3.画出中间的底盘
                g.fill3DRect(x+7,y+7, 20, 16,false);
                //4.画出中间的炮台
                g.fillOval(x+10,y+8, 13, 13);
                //5.画炮管
                g.drawLine(x+15,y+14,x,y+14);
                break;

        }
    }

    //设置坦克按键操作 移动和开火
    public void KeyPressed(KeyEvent e){
        int key=e.getKeyCode();
        switch (key){
            case KeyEvent.VK_F2:
                    if(!this.live){
                        this.live=true;
                        this.life=100;
                    }
                    break;
            case KeyEvent.VK_SPACE:
                    fire();
                break;
            case KeyEvent.VK_LEFT:
                BL=true;
                break;
            case KeyEvent.VK_UP:
                BU=true;
                break;
            case KeyEvent.VK_RIGHT:
                BR=true;
                break;
            case KeyEvent.VK_DOWN:
                BD=true;
                break;
            case KeyEvent.VK_CONTROL:
                superFire();
                break;
        }
        locateDirection();

    }

    //为了可以使坦克碰撞后转向
    private  void  stay(){
        x=oldX;
        y=oldY;
    }

    public void keyReleased(KeyEvent e) {
        int key=e.getKeyCode();
        switch (key){
            case KeyEvent.VK_LEFT:
                BL=false;
                break;
            case KeyEvent.VK_UP:
                BU=false;
                break;
            case KeyEvent.VK_RIGHT:
                BR=false;
                break;
            case KeyEvent.VK_DOWN:
                BD=false;
                break;
        }
        locateDirection();
    }

    //设置坦克当前方向
    void locateDirection(){
        if(BL && !BU && !BR && !BD) dir=Direction.L;
        else if(!BL && BU && !BR && !BD) dir=Direction.U;
        else if(!BL && !BU && BR && !BD) dir=Direction.R;
        else if(!BL && !BU && !BR && BD) dir=Direction.D;
        else if(!BL && !BU && !BR && !BD) dir=Direction.STOP;

    }

    public boolean isGood() {
        return good;
    }

    //设置炮弹
    public Missile fire(){
        if(!live) return null;
        int x = this.x + Tank.WIDTH/2 -Missile.WIDTH/2;
        int y = this.y + Tank.HEIGHT/2 - Missile.HEIGHT/2;
        Missile m =new Missile(x,y,ptDir,good,this.tf); //ptDir炮筒方向  tf大管家(坦克 炮弹 爆炸..)
        tf.missileList.add(m);
        return m;
    }

    public Missile fire(Direction dir){
        if(!live) return null;
        int x = this.x + Tank.WIDTH/2 -Missile.WIDTH/2;
        int y = this.y + Tank.HEIGHT/2 - Missile.HEIGHT/2;
        Missile m =new Missile(x,y,dir,good,this.tf); //ptDir炮筒方向  tf大管家(坦克 炮弹 爆炸..)
        tf.missileList.add(m);
        return m;
    }
    //超级开火
    public void superFire(){
        Direction[] dirs=Direction.values();
        for(int i=0;i<4;i++){
            fire(dirs[i]);
        }
    }

    //设置坦克方块(碰撞检测用)
    public Rectangle getRect(){
        return  new Rectangle(x,y,WIDTH,HEIGHT);
    }

    //坦克是否活着
    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    //坦克撞墙检测
    public boolean collidesWithWall(Wall w){
        if(this.live && this.getRect().intersects(w.getRect())){
            this.stay();
            return  true;
        }
        return  false;
    }

    //坦克之间碰撞检测
    public  boolean collidesWithTanks(List<Tank> tank){
        for(int i=0;i<tank.size();i++){
            Tank t=tank.get(i);
            if(this!=t){
                //坦克活着并且敌人活着并且俩坦克碰撞
                if(this.live && t.isLive() && this.getRect().intersects(t.getRect())){
                    this.stay();
                    t.stay();
                    return  true;
                }
            }
        }

        return  false;
    }

    //坦克血条
    private class BloodBar{
        public void draw(Graphics g){
            Color c=g.getColor();
            g.setColor(Color.red);
            g.drawRect(x,y-11,WIDTH,5);
            int  w = WIDTH * life/100;
            g.fillRect(x,y-11,w,5);
            g.setColor(c);
        }
    }

    //坦克吃道具(血)
    public boolean eat(BloodModule b){
        if(this.live && b.isLive()&& this.getRect().intersects(b.getRect())){
            this.life=100;
            b.setLive(false);
            return  true;
        }
        return  false;
    }


}
