


import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class Player{
	
	private Square[][] board;
	private GameBoard gameboard;
	private int turn;
	private int row;
	private int col;
	private int room;
	private boolean clicking;
	private PenteFirebase firebase;
	
	public Player(GameBoard b, int t, int r, boolean c){
		gameboard=b;
		board=b.getBoard();
		turn=t;
		row=-1;
		col=-1;
		room=r;
		clicking=c;
		firebase=b.getFirebase();
	}
	
	public void updateFirebase(PenteFirebase fb){
		firebase=fb;
//		JOptionPane.showMessageDialog(null, "update successful "+firebase.toString());
	}
	public void setRoom(int r){
		room=r;
	}
	
	public void getClickedSquare(MouseEvent e){
		for (Square[] column : board)
			for (Square s : column)  
				if(s.youClickedMe(e.getX(), e.getY())){
					if(s.getState()==Main.EMPTY){
						row=s.getRow();
						col=s.getCol();
						gameboard.placeSquare(s);
						return;
					}
					else
						JOptionPane.showMessageDialog(null, "You can't click on a space with a stone.");
				}
		return;
	}

	public void listenForMove(){
		String turnString="";
		if(turn==Main.BLACK) turnString="Black";
		else if(turn==Main.WHITE) turnString="White";
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
				if(gameboard.getTurn()==turn){
					String string=snapshot.getValue().toString();
					if(!string.equals("init")){
						Square sq=firebase.toSquare(string);
						gameboard.placeSquare(sq);
					}
				}
			}
			
		});
	}
	
	public void start(){
		if(!clicking) this.listenForMove();
	}
	
	public Square getSquare(){
		return board[row][col];
	}
	
	public String toString(){
		return ""+row+" "+col;
	}

}
