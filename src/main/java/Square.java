


import java.awt.Color;
import java.awt.Graphics;

public class Square {
	
	private int myRow, myCol;
	private int xLoc, yLoc; //top left
	private int sWidth;
	private Color boardSquareColor = new Color (252,209,26);
	private Color crossHairColor = new Color (150,111,51);
	//private Color squareSideColor = new Color (255,255,255);
	private int squareState=Main.EMPTY;
	
	public Square () {
		xLoc=-1;
		yLoc=-1;
		sWidth=-1;
		myRow=-1;
		myCol=-1;
	}
	
	public Square (int x, int y, int width, int row, int col) {
		xLoc=x;
		yLoc=y;
		sWidth=width;
		myRow=row;
		myCol=col;
	}
	
	public void drawMe (Graphics g){
		g.setColor(boardSquareColor);
		g.fillRect(xLoc, yLoc, sWidth, sWidth);
		
		g.setColor(crossHairColor);
		g.drawLine(xLoc+sWidth/2, yLoc, xLoc+sWidth/2, yLoc+sWidth);
		g.drawLine(xLoc, yLoc+sWidth/2, xLoc+sWidth, yLoc+sWidth/2);
		
		/*g.setColor(squareSideColor);
		g.drawRect(xLoc, yLoc, sWidth, sWidth);*/
		
		if(squareState==Main.BLACK){
			g.setColor(Color.BLACK);
			g.fillOval(xLoc+3, yLoc+3, sWidth-6, sWidth-6);
		} if(squareState==Main.WHITE){
			g.setColor(Color.WHITE);
			g.fillOval(xLoc+3, yLoc+3, sWidth-6, sWidth-6);
		}
		
	}
	
	public boolean youClickedMe (int mouseX, int mouseY) {
		boolean inBound = false; 
		if (mouseX >= xLoc && mouseX < xLoc+sWidth)			
			if (mouseY >= yLoc && mouseY < yLoc+sWidth)		
				inBound = true;		
		return inBound;
		
	}
	
	public int getDistance(Square other){
		int otherRow=other.getRow();
		int otherCol=other.getCol();
		int distance=(int)(Math.sqrt((Math.pow(Math.abs(myRow-otherRow), 2)+(Math.pow(Math.abs(myCol-otherCol), 2)))));
		return distance;
	}
	
	public int getState(){
		return squareState;
	}
	
	public void setState(int newState){
		squareState=newState;
	}
	
	public int getRow(){
		return myRow;
	}
	
	public int getCol(){
		return myCol;
	}
	
	public String toString(){
		return myRow+" "+myCol+" "+squareState;
		
	}
}
