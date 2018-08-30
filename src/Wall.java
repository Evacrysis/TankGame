import java.awt.*;

public class Wall {
    int x,y,w,h;
    TankFrame tf;

    public Wall(int x, int y, int w, int h, TankFrame tf) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.tf = tf;
    }

    public void draw(Graphics g){
        g.fillRect(x,y,w,h);
    }

    public Rectangle getRect(){
        return  new Rectangle(x,y,w,h);
    }
}
