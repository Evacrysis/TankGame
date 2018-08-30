import java.awt.*;
import  java.util.List;
public class Missile {
    //炮弹速度
    public static final int XSpeed=15;
    public static final int YSpeed=15;

    //炮弹大小
    public static  final  int WIDTH=10;
    public static  final  int HEIGHT=10;

    //炮弹初始位置
    int x,y;

    //炮口方向
    Tank.Direction dir;

    //
    private boolean good;

    //可以移除多余的炮弹
    private TankFrame tf;

    //炮弹存活判断
    private boolean Live=true;


    public Missile(int x, int y, Tank.Direction dir) {
        this.x = x;
        this.y = y;
        this.dir = dir;
    }

    public Missile (int x,int y,Tank.Direction dir,Boolean good,TankFrame tf){
        this(x,y,dir);
        this.good=good;
        this.tf=tf;
    }


    //画一个炮弹
    public void draw(Graphics g){
        if(!Live){
            tf.missileList.remove(this);
            return;
        }
        Color c =g.getColor();
        if(this.good){
            g.setColor(Color.white);
        }else{
            g.setColor(Color.RED);
        }

        g.fillOval(x,y,WIDTH,HEIGHT);
        g.setColor(c);
        move();
    }

    //炮弹移动
    private void move() {
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
        }
        //判断炮弹出界
        if(x<0||y<0||x>TankFrame.GAME_WIDTH||y>TankFrame.GAME_HEIGHT){
            Live=false;
        }
    }

    public boolean isLive() {
        return Live;
    }

    //子弹外围方块
    public Rectangle getRect(){
        return  new Rectangle(x,y,WIDTH,HEIGHT);
    }

    //打坦克
    public  boolean hitTank(Tank t){
        //判断两个方块是否相交
        if(this.Live&&this.getRect().intersects(t.getRect())&& t.isLive()&&this.good!=t.isGood()){
            //打一发扣20血
            if(t.isGood()){
                t.setLife(t.getLife()-20);
                if(t.getLife()<=0) t.setLive(false);
            }else {
                //设置敌方坦克一下就死亡
                t.setLive(false);
            }

            //设置子弹死亡
            this.Live=false;
            //爆炸产生位置
//
            //产生爆炸
            Explode e=new Explode(x,y,tf);
            //添加爆炸到数组
            tf.explodes.add(e);
            return  true;
        }
        return  false;
    }

    //子弹击中坦克
    public boolean hitTanks(List<Tank> tanks){
        for(int i=0;i<tanks.size();i++){
            if(hitTank(tanks.get(i))){
                return  true;
            }
        }
        return  false;
    }

    //子弹撞墙
    public  boolean hitWall(Wall w){
        if(this.Live && this.getRect().intersects(w.getRect())){
            this.Live=false;
            return  true;
        }
        return  false;
    }

}
