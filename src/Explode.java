import java.awt.*;

public class Explode {
    int x,y;

    private boolean live=true;

    private TankFrame tf;

    public Explode(int x, int y, TankFrame tf) {
        this.x = x;
        this.y = y;
        this.tf = tf;
    }

    //爆炸半径
    int[] diameter ={4,7,12,18,26,32,49,30,14,6};
    int step=0;

    public void draw(Graphics g){
        if(!live) {
            //不存在就移除爆炸
            tf.explodes.remove(this);
            return;
        }
        if(step==diameter.length){
            live=false;
            step=0;
            return;
        }

        Color c =g.getColor();
        g.setColor(Color.ORANGE);
        g.fillOval(x,y,diameter[step],diameter[step]);
        g.setColor(c);

        step++;
    }
}
