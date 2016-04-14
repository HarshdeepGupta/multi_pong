/**
 * Created by hd on 14/4/16.
 */
import java.lang.*;

public class Vector2D {

    public int X;
    public int Y;

    public Vector2D(){
        this.X = 0;
        this.Y = 0;
    }


    public Vector2D(int x, int y){
        this.X = x;
        this.Y = y;
    }

    public Vector2D add(Vector2D x, Vector2D y){
        return new Vector2D(x.getX()+y.getX(),x.getY()+y.getY());
    }

    public Vector2D sub(Vector2D x, Vector2D y){
        return new Vector2D(x.getX()-y.getX(),x.getY()-y.getY());
    }

    public double dot(Vector2D x, Vector2D y){
        return x.getX()*y.getX()+x.getY()*y.getY();
    }


    public double length(Vector2D x){
        return (Math.sqrt(x.getX()*x.getX()+x.getY()*x.getY()));
    }


    private int getX(){
        return this.X;
    }

    private int getY(){
        return this.Y;
    }

}
