
public class FirebaseCleanupThread extends Thread {
	
	private PenteFirebase firebase;
	public FirebaseCleanupThread (PenteFirebase pf){
		firebase=pf;
	}
	
	public void run(){
		firebase.closeRoom();
		System.out.println("closed.");
	}

}
