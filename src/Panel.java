import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.Arrays;

//panel class inherits from JPanel and implements the ActionListener Interface( this gets triggered each 160 ms and calls the actionperformed function)
public class Panel extends JPanel implements ActionListener {
    static int width = 1200;   // width of panel
    static int height = 600; // height of panel
    static int unit= 50; // size of one box in grid
    boolean flag = false; // flag==false , game is in game over condition , vice versa game is playing
    int score = 0;
    Timer timer; // variable of timer type

    Random random; // variable of random type

    int fx, fy; // variable to generate random coordinates of food
    int length = 3; // initial length of snake on starting

    char dir = 'R'; // starting direction of snake is right



    //to store x and y coordinate in separate arrays for each body part of the snake
    int xsnake[] = new int[(width*height)/(unit*unit)]; // max size of snakex, aka 288

    int ysnake[] = new int[(width*height)/(unit*unit)]; // max size of snakey ,aka 288


    Panel(){
        this.setPreferredSize(new Dimension(width, height)); // panel dimensions being set for a specific grid size
        this.setBackground(Color.black);  // setting background color
        this.addKeyListener(new key()); // key listener will listen to the keyboard key input
        this.setFocusable(true);
        random = new Random(); // for random spawning of food for snake
        gamestart();
    }

    //sets the initial parameters for the game and spawns the food
    public void gamestart(){
        spawnfood();
        flag = true; // game is running
        timer = new Timer(160, this); //  will call actionPerformed every 160ms, 160 ms is the sweetspot for the game to appear normal speed and moving
        timer.start(); // starting game timer
    }
    public void spawnfood(){
        // Next Int will give random integer for the coordinates of food which will be a multiple of 50 between 0 and 1200
        fx = random.nextInt(width/unit)*unit; // setting bound for random int for max 24*50,so it is a multiple of 50
        fy = random.nextInt(height/unit)*unit; // setting bound for random int for max 12*50 , so it is a multiple of 50
    }

    public void paintComponent(Graphics graphic){
        super.paintComponent(graphic); // we want to call paintComponent from Jframe class , so we use super
        draw(graphic); //  calling draw function defined below
    }

    public void draw(Graphics graphic){
        if(flag) // game is in running state using flag counter
             {
            //for the food particle
            graphic.setColor(Color.red); // food particle color
            graphic.fillOval(fx, fy, unit, unit);   //food particle drawing,giving coordinates and size using nextint randomised values
                 //max length of snake will be 24*12 boxes of size 50 and shape oval
            for(int i = 0; i < length; i++)// body of snake
            {
                graphic.setColor(Color.orange);
                graphic.fillRect(xsnake[i], ysnake[i], unit, unit); // to create rectangle of snake
            }
            graphic.setColor(Color.cyan); // setting the color of the score string
            graphic.setFont(new Font("Comic Sans", Font.BOLD, 40)); // setting the size of the string and the style
            FontMetrics fme = getFontMetrics(graphic.getFont()); // to get the size of the font ( height and the width) in pixels
            graphic.drawString("Score: "+ score, (width-fme.stringWidth("Score:"+score))/2, graphic.getFont().getSize()); // (width-fme.stringWidth("Score:"+score))/2 gives the starting point of the of the score string which we get by subtracting the length of the string from the width and dividing by 2 to place it in the top middle of the panel ,
                 // graphic.getFont().getSize() is used to get the height of the font
        }
        else{ gameover(graphic); // calling gamever function to display score and stuff since game has ended
        }
    }

    public void gameover(Graphics graphic){
        // to display final score
        graphic.setColor(Color.cyan); // color
        graphic.setFont(new Font("Comic Sans", Font.BOLD, 40)); //size and font style
        FontMetrics fme = getFontMetrics(graphic.getFont());
        graphic.drawString("Score: "+ score, (width-fme.stringWidth("Score:"+score))/2, graphic.getFont().getSize()); // (width-fme.stringWidth("Score:"+score))/2 gives the starting point of the of the score string which we get by subtracting the length of the string from the width and dividing by 2 to place it in the top middle of the panel ,
        // graphic.getFont().getSize() is used to get the height of the font

        // gameover text
        graphic.setColor(Color.red); // setting the color of the score string
        graphic.setFont(new Font("Comic Sans", Font.BOLD, 80)); // setting the size of the string and the style
         fme = getFontMetrics(graphic.getFont()); // to get the size of the font ( height and the width) in pixels
        graphic.drawString("GAME OVER ", (width-fme.stringWidth("GAMEOVER"))/2, height/2); // (width-fme.stringWidth("Score:"+score))/2 gives the starting point of the of the score string which we get by subtracting the length of the string from the width and dividing by 2 to place it in the top middle of the panel ,
        // graphic.getFont().getSize() is used to get the height of the font

        // replay text
        graphic.setColor(Color.green    ); // setting the color of the score string
        graphic.setFont(new Font("Comic Sans", Font.BOLD, 40)); // setting the size of the string and the style
         fme = getFontMetrics(graphic.getFont()); // to get the size of the font ( height and the width) in pixels
        graphic.drawString("Press R to replay", (width-fme.stringWidth("Press R to replay"))/2, 3*height/4); // (width-fme.stringWidth("Score:"+score))/2 gives the starting point of the of the score string which we get by subtracting the length of the string from the width and dividing by 2 to place it in the top middle of the panel ,
        // graphic.getFont().getSize() is used to get the height of the font
    }

   public void eat(){
        if(fx== xsnake[0] && fy== ysnake[0]) // if the coordinates of the head and the food are the same then it means the food is eaten
        {
            length++; // length of the snake gets incremented
            score++; // score gets updated
            spawnfood(); // new food gets spawned after being eaten
        }
   }

   public void hit() // to check if you hit own body of the snake
   { for(int i=length;i>0;i--){ // if the coordinates of the head and any part of the snake are the same we have hit the snake
       if(xsnake[0]==xsnake[i] && ysnake[0]==ysnake[i]){
           flag=false;
       } }
       // to check for the snake going out of bounds by checking for the coordinates of the head staying between 0 and height , 0 and width
       if(xsnake[0]<0 || xsnake[0]>width || ysnake[0]<0 || ysnake[0]>height){
           flag=false;
       }
       if(flag==false){
           timer.stop(); // to stop the timer once being hit to avoid speeding up after each iteration of play
       }
   }
    public void move(){
        // updating the coordinates of the body except the head
        for(int i = length; i>0;i--){ //using length -1 led to tail getting updated late so we use length
            xsnake[i]=xsnake[i-1]; // the x coordinates get left shifted so the snake moves
            ysnake[i]=ysnake[i-1]; // the y coordinates also get up shifted to make the snake move
        }
        // now we move the head to new coordinates
        switch (dir){
            case 'U' :
                ysnake[0]= ysnake[0]-unit; // to move the head up we subtract unit to move one unit up
                break;
            case 'D' :
                ysnake[0]= ysnake[0]+unit; // to move the head down we add unit to move one unit down
                break;
            case 'R' :
                xsnake[0]= xsnake[0]+unit; // to move the head to the right  we add  unit to move one unit to the right
                break;
            case 'L' :
                xsnake[0]= xsnake[0]-unit; // to move the head to the left  we subtract unit to move one unit to the left
                break;
        }
    }

    public class key extends KeyAdapter{  // making key class inherit the keyadapter class
            // using Keyadapter class to record the keyboard input
        public void keyPressed(KeyEvent e){  // overriding the keypressed function in keyadapter class
            switch(e.getKeyCode()){
                case KeyEvent.VK_UP :
                    if(dir !='D') // snake cannot go into its own body so we check for if the input is opposite of current direction
                    {
                        dir='U'; // making dir the current input from keyboard
                    }
                    break;

                case KeyEvent.VK_DOWN:
                    if(dir !='U'){ // snake cannot go into its own body so we check for if the input is opposite of current direction
                        dir='D'; // making dir the current input from keyboard
                    }
                    break;

                case KeyEvent.VK_LEFT:
                    if(dir !='R'){ // snake cannot go into its own body so we check for if the input is opposite of current direction
                        dir='L'; // making dir the current input from keyboard
                    }
                    break;

                case KeyEvent.VK_RIGHT:
                    if(dir !='L'){ // snake cannot go into its own body so we check for if the input is opposite of current direction
                        dir='R'; // making dir the current input from keyboard
                    }
                    break;

                case KeyEvent.VK_R: //checking for R input from keyboard
                    if(flag==false) {// checking if game is in game over condition
                        score = 0; // setting to initial score
                        length = 3; // setting to initial length
                        dir = 'R'; // setting to initial direction
                        Arrays.fill(xsnake, 0); // dumping all the previous values of the xsnake and filling with 0
                        Arrays.fill(ysnake, 0);// dumping all the previous values of the ysnake and filling with 0
                        gamestart(); // restarting the game again
                    }
                        break;

              }
            }
        }
            // Action listener calls this method every 160ms to move the snake or to give the illusion that the snake is working
        public void actionPerformed(ActionEvent e){
        if(flag){
            move(); // calling move function
            hit(); // calling hit function
            eat(); // calling eat function

        }
        repaint(); // to repaint the snake to appear moving
    }

}










