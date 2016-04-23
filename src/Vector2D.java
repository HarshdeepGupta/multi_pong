/**
 * Created by hd on 14/4/16.
 */
import java.lang.*;

public class Vector2D {

    int X;
    int Y;

    public Vector2D(){
        this.X = 0;
        this.Y = 0;
    }


    public Vector2D(int x, int y){
        this.X = x;
        this.Y = y;
    }

    public Vector2D add(Vector2D V) {
        return new Vector2D(this.X + V.X, this.Y + V.Y);
    }

    public Vector2D sub( Vector2D V) {
        return new Vector2D(this.X - V.X,this.Y - V.Y);
    }

    public int dot( Vector2D V){
        return this.X*V.X + this.Y*V.Y;
    }
    Vector2D scalarMult(int l){
        return new Vector2D(this.X*l,this.Y*l);
    }


    public double length(){

        return Math.sqrt(this.dot(this)) ;
    }



}
