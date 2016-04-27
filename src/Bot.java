/**
 * Created by hd on 22/4/16.
 */
public class Bot {

    Paddle paddle;
    Ball ball;
    int difficulty;



    public Bot( Ball ball, int difficulty) {
        this.ball = ball;
        this.difficulty = difficulty*100;
    }

    void attach(Paddle paddle){
        if(paddle != null){
            this.paddle = paddle;
            paddle.setIsbot(true);
        }
        else{
            System.out.println("Cannot attach bot to null paddle");
        }


    }
    void detach(){
        this.paddle = null;

    }

    void updateBot(){
        int c = 20 - difficulty/100*5;
        if(paddle.move_x && Math.abs(ball.position.Y-paddle.position.Y) < difficulty  ){
            if(ball.position.X-paddle.position.X >c ){
                paddle.paddleVelocity.X = paddle.paddleSpeed;
            }
            if(ball.position.X-paddle.position.X <-c ){
                paddle.paddleVelocity.X = -paddle.paddleSpeed;
            }
        }
        else if(paddle.move_y && Math.abs(ball.position.X-paddle.position.X) < difficulty  ){
            if(ball.position.Y-paddle.position.Y >c ){
                paddle.paddleVelocity.Y = paddle.paddleSpeed;
            }
            if(ball.position.Y-paddle.position.Y <-c ){
                paddle.paddleVelocity.Y = -paddle.paddleSpeed;
            }
        }
        else{
            paddle.setPaddleVelocity(0,0);
        }
    }

    public boolean is_attached(){
        if(paddle==null){
            return false;
        }
        else{
            return true;
        }
    }



}
