import javax.swing.JFrame;

public class frame extends JFrame {
    frame(){
        this.setTitle("Snake Game"); // name of the frame
        this.add(new Panel()); // calling panel constructor to make panel and everything else with it
        this.setVisible(true); // setting visibility to true so the frame is visible
        this.setResizable(false); // to not make the frame  resizeable
        this.pack();//gives authority to set size of the frame to the os layout manager
    }
}
