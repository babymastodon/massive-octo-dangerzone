package client;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import common.Whiteboard;

public class BoardCanvas extends JPanel{
    private BufferedImage drawingBuffer;
    
    public BoardCanvas(){
        super();
        this.setPreferredSize(new Dimension(Whiteboard.WIDTH, Whiteboard.HEIGHT));
        this.drawingBuffer = Whiteboard.makeBuffer();
    }
    
    /**
     * Return a reference to the buffered image that is painted
     * onto this component.
     *
     * Should only be modified inside the Swing event loop.
     */
    public BufferedImage getDrawingBuffer(){
        return drawingBuffer;
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
