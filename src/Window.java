import javax.swing.JFrame;

public class Window extends JFrame {

    public Window() {
        super();
        setSize(600, 600);

        DrawingPanel drawingPanel = new DrawingPanel();

        setContentPane(drawingPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void run() {
        setVisible(true);
    }

}
