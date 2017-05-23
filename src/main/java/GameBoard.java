


import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GameBoard extends JPanel implements MouseListener {
		public int room=0;
		/**
	 * 
	 */
	private static final long serialVersionUID = 2081894653079139310L;
		private PenteFirebase firebase=null;
		private JFrame frame;
		private JFrame connectingFrame;
		private JFrame waiting;
		private JDialog dialog;
		private int bWidthPixels;
		private int bWidthSquares;
		private int bSquareWidth;
		private Square[][] board;
		private int currentTurn = Main.BLACK;
		
		private int blackCapture = 0;
		private int whiteCapture = 0;
		
		private boolean playingAgainstRalph;
		private int ralphStoneColor;
		private Ralph ralph;
		
		private boolean whiteIsClicking=false;
		private Player whitePlayer;
		private boolean blackIsClicking=false;
		private Player blackPlayer;
		
		
		public GameBoard (int bWPixels, int bWSquares, JFrame f) {
			frame=f;
			bWidthPixels = bWPixels;
			bWidthSquares = bWSquares;
			bSquareWidth = bWidthPixels/bWidthSquares;
			this.setSize(bWidthPixels, bWidthPixels);
			this.setVisible(false);
			
			board=new Square[bWSquares][bWSquares];
			
			for (int col=0; col<bWSquares; col++) 
				for (int row=0; row<bWSquares; row++){
					board[row][col]=new Square(col*bSquareWidth, row*bSquareWidth, 
							bSquareWidth, row, col);
				}
			
			board[bWidthSquares/2][bWidthSquares/2].setState(Main.BLACK); 
		
		}
		
		public void startGame(){
			int color=firebase.getPlayerColor();
			if (color==Main.BLACK){
				JOptionPane.showMessageDialog(null, "Your Color is Black");
				blackIsClicking=true;
				blackPlayer=new Player(this, Main.BLACK, room, blackIsClicking);
				whitePlayer=new Player(this, Main.WHITE, room, whiteIsClicking);
				waiting=new JFrame("Message");
				JOptionPane op = new JOptionPane("Waiting for Opponent...", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION,
						null, new Object[]{}, null);
				waiting.add(op);
				waiting.pack();
				waiting.setLocationRelativeTo(null);
				waiting.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
				waiting.setVisible(true);
			} else if(color==Main.WHITE) {
				JOptionPane.showMessageDialog(null, "Your Color is White");
				whiteIsClicking=true;
				blackPlayer=new Player(this, Main.BLACK, room, blackIsClicking);
				whitePlayer=new Player(this, Main.WHITE, room, whiteIsClicking);
			}
			changeTurn();
			addMouseListener(this);
		}
		
		private void setRalph(){
			playingAgainstRalph=true;
			String[] options={"White", "Black"};
			int color=JOptionPane.showOptionDialog(null, "Choose Your Color", "Select an Option", JOptionPane.YES_NO_OPTION, 
					JOptionPane.QUESTION_MESSAGE, null, options, null);
			if (color==0) {
				ralph=new Ralph(this, Main.BLACK);
				ralphStoneColor=Main.BLACK;
				whiteIsClicking=true;
				whitePlayer=new Player(this, Main.WHITE, room, whiteIsClicking);
			} else {
				ralph=new Ralph(this, Main.WHITE);
				ralphStoneColor=Main.WHITE;
				blackIsClicking=true;
				blackPlayer=new Player(this, Main.BLACK, room, whiteIsClicking);
			}
			changeTurn();
			addMouseListener(this);
			
		}
		
		public void setFirebase(PenteFirebase fb){
			int ralphOption=JOptionPane.showConfirmDialog(null, "Do you want to play with Ralph?", "Select an Option",
					JOptionPane.YES_NO_OPTION);
			if(ralphOption==JOptionPane.NO_OPTION){
				connectingFrame = new JFrame ("Message");
				JOptionPane op = new JOptionPane("Loading...", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION,
						null, new Object[]{}, null);
				connectingFrame.add(op);
				connectingFrame.pack();
				connectingFrame.setLocationRelativeTo(null);
				connectingFrame.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
				connectingFrame.setVisible(true);
				//JOptionPane.showMessageDialog(null, "connecting to firebase...");
				fb.connect();
				firebase=fb;
				if(whitePlayer!=null) whitePlayer.updateFirebase(fb);
				if(blackPlayer!=null) blackPlayer.updateFirebase(fb);
			} else {
				setRalph();
				setAvailable();
			}
		}
		
		public void setAvailable(){
			this.setVisible(true);
			frame.setVisible(true);
			if(whitePlayer!=null) whitePlayer.start();
			if(blackPlayer!=null) blackPlayer.start();
		}
		public void setRoom(int x){
			room=x;
			if(whitePlayer!=null) whitePlayer.setRoom(x);
			if(blackPlayer!=null) blackPlayer.setRoom(x);
		}
		
		private void resetGame(){
			blackCapture=0;
			whiteCapture=0;
			
			for (int col=0; col<bWidthSquares; col++) 
				for (int row=0; row<bWidthSquares; row++){
					board[row][col]=new Square(col*bSquareWidth, row*bSquareWidth, 
							bSquareWidth, row, col);
				}
			
			board[bWidthSquares/2][bWidthSquares/2].setState(Main.BLACK); 
			
			currentTurn=Main.BLACK;
			
			if(firebase!=null)
				firebase.roomReset(room);
		}
		
		public void paintComponent (Graphics g){
			for (Square[] column : board)
				for (Square s: column) {
					s.drawMe(g);
				}
		}
		
		private void changeTurn() {
			currentTurn*=-1;
		}
		
		public void placeSquare(Square s){
			doPlay(s);
		}
		
		private void playGame(MouseEvent e){
			if(currentTurn==Main.BLACK && blackIsClicking)
				blackPlayer.getClickedSquare(e);
			else if(currentTurn==Main.WHITE && whiteIsClicking)
				whitePlayer.getClickedSquare(e);
			if(playingAgainstRalph && currentTurn==ralphStoneColor){
				ralph.move();
			}
		}
		
		private void doPlay(Square s){
			if(firebase!=null)
				firebase.setMove(room, currentTurn, s.getRow(),s.getCol());
			s.setState(currentTurn);
			checkCaptures(s); 
			checkForWinOnCaptures();
			checkWin2(s);
			changeTurn();
			requestFocus();
			repaint();
		}
		
		private void checkCaptures(Square s){
			int currentState=s.getState(); 
			int row=s.getRow(); 
			int col=s.getCol(); 
			for (int dx=-1; dx<2 && col+dx*3 <bWidthSquares; dx++) 
				for (int dy=-1; dy<2 && row+dy*3 <bWidthSquares; dy++) { 
					if (col+dx*3<0) dx=0; 
					if (row+dy*3<0) dy=0;
					if (board[row+dy][col+dx].getState()==currentState*(-1) && 
						board[row+dy*2][col+dx*2].getState()==currentState*(-1) && 
						board[row+dy*3][col+dx*3].getState()==currentState){
							if(currentState==Main.BLACK) {
								blackCapture++;
								if(firebase!=null)
									firebase.whiteReset(room);
							}
							else {
								whiteCapture++;
								if(firebase!=null)
									firebase.blackReset(room);
							}
							board[row+dy][col+dx].setState(Main.EMPTY); 
							board[row+dy*2][col+dx*2].setState(Main.EMPTY);
							repaint();
							}
				}
		}
		
		private void checkForWinOnCaptures(){
			if (blackCapture>=5) {
				repaint();
				JOptionPane.showMessageDialog(null, "Black wins with "+blackCapture+" captures!");
				resetGame();
			}
			if (whiteCapture>=5) {
				repaint();
				JOptionPane.showMessageDialog(null, "White wins with "+whiteCapture+" captures!");
				resetGame();
			}
		}
		
		private void checkWin2(Square s){
			//System.out.println("-------------");
			checkWinDirection(s,1,0); //horizontal 
			checkWinDirection(s,0,1); //vertical 
			checkWinDirection(s,1,1); //down diagonal
			checkWinDirection(s,-1,1); //up diagonal
		}
		
		private void checkWinDirection(Square s, int dx, int dy){
			int currentState=s.getState(); 
			int row=s.getRow(); 
			int col=s.getCol(); 
			boolean l=true, r=true;
			int count=1, i=0; 
			while(i<=4 && count>i){
				i++;
				if((dx>=0 && row-dy*i>=0 && col-dx*i>=0) ||
					(dx<0 && row-dy*i>=0 && col-dx*i<bWidthSquares)){
					l=checkSameState(board[row-dy*i][col-dx*i], currentState, l); 
					if(l) count++; 
				}
				if((dx>=0 && row+dy*i<bWidthSquares && col+dx*i<bWidthSquares) ||
					(dx<0 && row+dy*i<bWidthSquares && col+dx*i>=0)){
					r=checkSameState(board[row+dy*i][col+dx*i], currentState, r); 
					if(r) count++;
				}
			}
			if (count>=5)  { 
				repaint();
				if(currentState==Main.BLACK) JOptionPane.showMessageDialog(null, "Black Wins!");
				else JOptionPane.showMessageDialog(null, "White Wins!");
				resetGame();
			}
		}
		
		private boolean checkSameState(Square s, int state, boolean condition){
			if (condition && s.getState()==state)
				return true;
			return false;
		}
		
		
		public int getBoardWidthSquares(){
			return bWidthSquares;
		}
		
		public int getTurn(){
			return currentTurn;
		}
		
		public Square[][] getBoard(){
			return board;
		}
		
		public PenteFirebase getFirebase(){
			return firebase;
		}
		
		public JFrame getConnectingFrame(){
			return connectingFrame;
		}
		
		public JFrame getWaitingFrame(){
			return waiting;
		}
		
		public JDialog getDialog(){
			return dialog;
		}
		
		public int getCapture(int color){
			if(color==Main.BLACK) return blackCapture;
			else if (color==Main.WHITE) return whiteCapture;
			else return -1;
			
		}
		@Override
		public void mouseClicked(MouseEvent e) { 
			playGame(e); 
			repaint();
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			
		}

		
		public void listenForMove(){
			String turnString="";
			if(whiteIsClicking) turnString="Black";
			else if(blackIsClicking) turnString="White";
			final FirebaseDatabase database = FirebaseDatabase.getInstance();
			DatabaseReference ref = database.getReference();
			ref.child("Rooms").child(String.valueOf(room)).child("Turns").child(turnString).
				addValueEventListener(new ValueEventListener(){

				@Override
				public void onCancelled(DatabaseError error) {
					JOptionPane.showMessageDialog(null, "The read failed: " + error.getCode());
				}

				@Override
				public void onDataChange(DataSnapshot snapshot) {
					if((currentTurn==Main.BLACK && whiteIsClicking) 
						|| (currentTurn==Main.WHITE && blackIsClicking)){
						String string=snapshot.getValue().toString();
						if(!string.equals("init")){
							Square sq=firebase.toSquare(string);
							doPlay(sq);
						}
					}
				}
				
			});
		}
		

}
