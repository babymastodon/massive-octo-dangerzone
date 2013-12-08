package client;

import java.awt.Image;

import canvas.Canvas;

public class BoardCanvas extends Canvas{
    private Image drawingBuffer;
    
    public BoardCanvas(int width, int height){
        super(width, height);
    }
    
    public void setDrawingBuffer(Image newBuffer){
        this.drawingBuffer = newBuffer;
    }
    
    /**
     * The only thing we need to pain is drawingBuffer
     */
    @Override
    public void repaint(){
        super.paintComponent(drawingBuffer.getGraphics());
    }
}