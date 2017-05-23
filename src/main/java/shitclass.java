public class shitclass {
	
//
//	
//	public shitclass(){
//		
//
//		try {
//
//			FileInputStream serviceAccount = new FileInputStream("pente-7d19d.json");
//			
//			FirebaseOptions options = new FirebaseOptions.Builder()
//					  .setCredential(FirebaseCredentials.fromCertificate(serviceAccount))
//					  .setDatabaseUrl("https://pente-7d19d.firebaseio.com/")
//					  .build();
//
//			FirebaseApp.initializeApp(options);
//				
//			try{
//			final AtomicBoolean write = new AtomicBoolean(false);
//			final AtomicBoolean read=new AtomicBoolean(false);
//			final FirebaseDatabase database = FirebaseDatabase.getInstance();
//			DatabaseReference ref = database.getReference();
//			
//			
////			ref.setValue(123412341,
////					new DatabaseReference.CompletionListener() {
////						@Override
////						public void onComplete(DatabaseError firebaseError,
////								DatabaseReference firebase) {
////							//throw firebaseError.toException();
////							//System.out.println(System.currentTimeMillis());
////							write.set(true);
////						}
////
////						
////					}); while(!write.get());
////					
//			ref.child("CurrentRoom").addValueEventListener(new ValueEventListener(){
//
//				@Override
//				public void onCancelled(DatabaseError error) {
//					System.out.println("The read failed: " + error.getCode());
//					JOptionPane.showMessageDialog(null, "The read failed " + error.getCode());
//					read.set(true);
//				}
//
//				@Override
//				public void onDataChange(DataSnapshot snapshot) {
//					//System.out.println(snapshot.getValue(String.class));
//					JOptionPane.showMessageDialog(null, snapshot.getValue(String.class));
//					read.set(true);
//					
//				}
//				
//			}); while(!read.get());
//			//JOptionPane.showMessageDialog(null, "read complete");
//					
//			//JOptionPane.showMessageDialog(null, done);
//			} catch (Exception e){
//				JOptionPane.showMessageDialog(null, "Set Value Error");
//				e.printStackTrace();
//			}
//
//			
//			
//			
//		} catch (FileNotFoundException e) {
//			JOptionPane.showMessageDialog(null, "File Not Found Error");
//			e.printStackTrace();
//		}
//	}
//		


}
