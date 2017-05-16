import javax.swing.JFrame;

public class PenteMain{
	
	public static final int EMPTY = 0;
	public static final int BLACK = 1;
	public static final int WHITE = -1;
	
	public static void main(String[] args){
		//
		//
		
		int boardSquares = 19;
		int boardWidth= (750/boardSquares) *boardSquares;
		
		JFrame frame = new JFrame ("Play Pente!");
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(boardWidth, boardWidth+boardSquares);

		GameBoard board = new GameBoard(boardWidth, boardSquares, frame);
		frame.add(board);
		//frame.setVisible(true);
		
		PenteFirebase penteFirebase=new PenteFirebase(board);
		
		Runtime r=Runtime.getRuntime();
		r.addShutdownHook(new FirebaseCleanupThread(penteFirebase));
//		JOptionPane.showMessageDialog(null, penteFirebase.toString());
//		board.setFirebase(penteFirebase);
//		String s=penteFirebase.getMove(0, 1);
//		JOptionPane.showMessageDialog(null, s);
//		penteFirebase.setMove(room, turn, row, col);
//		board.place(penteFirebase.getSquare(room, turn*-1));
		
//		penteFirebase.setMove(0, 1, 2, 11);
//		penteFirebase.setMove(0, -1, 8, 9);
//		penteFirebase.getSquare(0, 1);
//		String string=penteFirebase.getMove(0, 1, 1);
//		JOptionPane.showMessageDialog(null, string);
		
		//while(true) frame.setVisible(true);
	}

	
}