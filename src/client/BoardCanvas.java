package client;

import java.awt.Image;

import javax.swing.JPanel;

import common.Whiteboard;

public class BoardCanvas extends JPanel{
    private Image drawingBuffer;
    
    public BoardCanvas(){
        super(Whiteboard.WIDTH, Whiteboard.HEIGHT);
    }
    
    public void setDrawingBuffer(Image newBuffer){
        this.drawingBuffer = newBuffer;
    }
    
    /**
     * The only thing we need to paint is drawingBuffer.
     *
     * Requires the drawingBuffer to be set.
     */
    @Override
    public void paintComponent(Graphics g) {
        assert drawingBuffer != null;
        g.drawImage(drawingBuffer, 0, 0, null);
    }
}
