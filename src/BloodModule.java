import java.awt.*;

public class BloodModule {
    int x,y,w,h;
    TankFrame tf;

    int step=0;
    private  boolean live=true;

    public BloodModule(){
        x=pos[0][0];
        y=pos[0][1];
        w=h=15;
    }

    private int[][] pos={
            {500,400},{600,400},{700,400},{700,450,},{700,500},{600,500},{500,500},{500,450}
            };

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public  void draw(Graphics g){
        if(!live) return;
        Color c= g.getColor();
        g.setColor(Color.MAGENTA);
        g.fillRect(x,y,w,h);
        g.setColor(c);
    
        move();
    }

    private void move() {
        step++;
        if(step==pos.length){
            step=0;
        }
        x=pos[step][0];
        y=pos[step][1];
    }

    public  Rectangle getRect(){
        return  new Rectangle(x,y,w,h);
    }
}
