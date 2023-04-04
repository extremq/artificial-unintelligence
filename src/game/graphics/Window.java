package game.graphics;

import javax.swing.*;
import java.awt.*;

public class Window {
    private JFrame frame;
    private final String title;
    private final int width;
    private final int height;
    private Canvas canvas;

    public Window(String title, int width, int height){
        this.title = title;
        this.width = width;
        this.height = height;
        this.frame = null;

        build();
    }

    private void build()
    {
        if(frame != null)
        {
            return;
        }

        frame = new JFrame(title);
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(width, height));
        canvas.setMaximumSize(new Dimension(width, height));
        canvas.setMinimumSize(new Dimension(width, height));

        frame.add(canvas);
        frame.pack();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Canvas getCanvas() {
        return canvas;
    }

}
