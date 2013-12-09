package client;

import java.awt.Image;

import canvas.Canvas;
/**
 * 
 * Construct the whiteboard part 
 * Holds the drawing buffer and paint the board
 * 
 */
public class BoardCanvas extends Canvas{
    private Image drawingBuffer;
    
    public BoardCanvas(int width, int height){
        super(width, height);
    }
    
    /**
     * set 
     * @param newBuffer
     */
    public void setDrawingBuffer(Image newBuffer){
        this.drawingBuffer = newBuffer;
    }
    
    /**
     * The only thing we need to paint is drawingBuffer
     */
    @Override
    public void repaint(){
        super.paintComponent(drawingBuffer.getGraphics());
    }
}