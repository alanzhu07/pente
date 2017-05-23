import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JOptionPane;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * 
 * Structure:
 * pente-7d19d
 * 		CurrentRom - int
 * 		Rooms
 * 			0
 * 				Players
 * 				Black - 0,0
 * 				White - 0,0
 * 
 *
 */


public class PenteFirebase{
	
	
	private GameBoard b;
	private Square[][] board;
	private FirebaseDatabase database;
	private DatabaseReference ref;
	private final AtomicInteger currentRoom=new AtomicInteger(0);
	
	public PenteFirebase(GameBoard board){
		b=board;
		this.board=board.getBoard();
		try {
			InputStream serviceAccount= getClass().getResource("pente-7d19d.json").openStream();
			
			FirebaseOptions options = new FirebaseOptions.Builder()
					  .setCredential(FirebaseCredentials.fromCertificate(serviceAccount))
					  .setDatabaseUrl("https://pente-7d19d.firebaseio.com/")
					  .build();
			FirebaseApp.initializeApp(options);
			database = FirebaseDatabase.getInstance();
			ref = database.getReference();
			final FirebaseDatabase database = FirebaseDatabase.getInstance();
			ref = database.getReference();
		} catch (/*FileNotFoundException | */ IOException e) {
			JOptionPane.showMessageDialog(null, "File Not Found Error");
			e.printStackTrace();
		} 
		board.setFirebase(this);
	}
	
	public void setMove(int room, int turn, int row, int col){
		String turnString=null;
		if(turn==PenteMain.BLACK) turnString="Black";
		else if(turn==PenteMain.WHITE) turnString="White";
		//final AtomicBoolean write = new AtomicBoolean(false);
		ref.child("Rooms").child(String.valueOf(room)).child("Turns").child(turnString).setValue(
				(""+String.valueOf(row)+", "+String.valueOf(col)),
				new DatabaseReference.CompletionListener() {
				@Override
				public void onComplete(DatabaseError firebaseError,
						DatabaseReference firebase) {
					return;
					//write.set(true);
				}
				
			}); //while(!write.get());
			
			//return write.get();
	}

	public Square toSquare(String s){
		int commaIndex=s.indexOf(",");
		int row=Integer.parseInt(s.substring(0, commaIndex));
		int col=Integer.parseInt(s.substring(commaIndex+2));
		return board[row][col];
	}
	
	public void connect(){
		final AtomicBoolean read = new AtomicBoolean(false);
		ref.child("CurrentRoom").addValueEventListener(new ValueEventListener(){
			public void onCancelled(DatabaseError arg0){}
			public void onDataChange(DataSnapshot snapshot){
				currentRoom.set(Integer.parseInt(snapshot.getValue().toString()));
				read.set(true);
			}
		}); while(!read.get());
		DatabaseReference roomRef = database.getReference().child("Rooms");
		listen(roomRef, roomRef.child(String.valueOf(currentRoom.get())).child("Players"));
		
	}
	
	private void listen(DatabaseReference ref1, DatabaseReference ref){
		ref.addValueEventListener(new ValueEventListener(){
			@Override
			public void onCancelled(DatabaseError arg0) {
			}

			@Override
			public void onDataChange(DataSnapshot snapshot) {
				//currentRoom.incrementAndGet();
				int p=Integer.parseInt(snapshot.getValue().toString());
				//JOptionPane.showMessageDialog(null, "players: "+p);
				if (p>=2){
					//JOptionPane.showMessageDialog(null, "room is full"+ref.getPath());
					roomReset(currentRoom.incrementAndGet());
					addPlayer(0, this, ref1.child(String.valueOf(currentRoom.get())).child("Players"));
					//listen(ref1, ref1.child(String.valueOf(currentRoom.get())).child("Players"));
				}
				else addPlayer(p, this, ref);
				
			}
			
		});
	}
	
	private boolean addPlayer(int player, ValueEventListener listener, DatabaseReference playerRef){
		if (player<2){
			//JOptionPane.showMessageDialog(null, "there is space left");
			playerRef.setValue(String.valueOf(player+1), new DatabaseReference.CompletionListener() {
		
				@Override
				public void onComplete(DatabaseError arg0, DatabaseReference arg1) {
						b.setRoom(currentRoom.get());
						ref.child("CurrentRoom").setValue(String.valueOf(currentRoom.get()));
						b.getConnectingFrame().setVisible(false);
						JOptionPane.showMessageDialog(null, "connected!");
						b.setAvailable();
						//b.listenForMove();
							}
					});
			playerRef.removeEventListener(listener);
			return true;
			}
		else return false;
		
	}
	
	public void closeRoom(){
		System.out.println("closing room");

		AtomicBoolean d=new AtomicBoolean(false);
		if(currentRoom.get()==0) {
			roomReset(0);
			ref.child("Rooms").child("0").child("Players").setValue("0", new DatabaseReference.CompletionListener() {
				
				@Override
				public void onComplete(DatabaseError arg0, DatabaseReference arg1) {
					d.set(true);
				}
				}); while(!d.get());
		}
		else {
			ref.child("Rooms").child(String.valueOf(currentRoom.get())).setValue(null);
			ref.child("CurrentRoom").setValue(currentRoom.get()-1, new DatabaseReference.CompletionListener() {
			
			@Override
			public void onComplete(DatabaseError arg0, DatabaseReference arg1) {
				d.set(true);
			}
			}); while(!d.get());
		}
	}
	
	public void roomReset(int room){
		DatabaseReference turnRef = ref.child("Rooms").child(String.valueOf(room)).child("Turns");
		turnRef.child("Black").setValue("init");
		turnRef.child("White").setValue("init");
	}
	
	public void blackReset(int room){
		DatabaseReference turnRef = ref.child("Rooms").child(String.valueOf(room)).child("Turns");
		turnRef.child("Black").setValue("init");
	}
	
	public void whiteReset(int room){
		DatabaseReference turnRef = ref.child("Rooms").child(String.valueOf(room)).child("Turns");
		turnRef.child("White").setValue("init");
	}
	
	public int getRoom(){
		return currentRoom.get();
	}
	
//	public String getMove(int room, int turn){
//		String turnString=null;
//		if(turn==PenteMain.black) turnString="Black";
//		else if(turn==PenteMain.white) turnString="White";
//		final AtomicBoolean read = new AtomicBoolean(false);
//		final AtomicReference<String> s= new AtomicReference<String>("null");
//		ref.child("Rooms").child(String.valueOf(room)).child("Turns").child(turnString).
//			addValueEventListener(new ValueEventListener(){
//
//			@Override
//			public void onCancelled(DatabaseError error) {
//				JOptionPane.showMessageDialog(null, "The read failed: " + error.getCode());
//				read.set(true);
//			}
//
//			@Override
//			public void onDataChange(DataSnapshot snapshot) {
//				s.set(snapshot.getValue(String.class));
//				read.set(true);
//			}
//			
//		}); while(!read.get());
//		return s.get();
//	}
//	
//	public Square getSquare(int room, int turn, Player player){
//		String s=getMove(room, turn);
//		int commaIndex=s.indexOf(",");
//		int row=Integer.parseInt(s.substring(0, commaIndex));
//		int col=Integer.parseInt(s.substring(commaIndex+2));
//		//JOptionPane.showMessageDialog(null, "row:"+row+" col:"+col);
//		return board[row][col];
//		
//	}
	
}
