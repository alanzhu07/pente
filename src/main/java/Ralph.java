


import java.util.ArrayList;

public class Ralph { //AI
	private final int random=(int)(Math.random()*98)+3;
	//private final int difficulty=1;
	private int turn;
	private int bWidthSquares;
	private GameBoard gameboard;
	private Square[][] board;
	
	private ArrayList<OpponentGroup> opDisc = new ArrayList<OpponentGroup>();
	private ArrayList<OpponentGroup> offDisc = new ArrayList<OpponentGroup>();
	private ArrayList<OpponentGroup> opGroup4 = new ArrayList<OpponentGroup>();
	private ArrayList<OpponentGroup> opGroup3 = new ArrayList<OpponentGroup>();
	private ArrayList<OpponentGroup> opGroup2 = new ArrayList<OpponentGroup>();
	private ArrayList<OpponentGroup> opGroup1 = new ArrayList<OpponentGroup>();
	private ArrayList<OpponentGroup> offGroup4 = new ArrayList<OpponentGroup>();
	private ArrayList<OpponentGroup> offGroup3 = new ArrayList<OpponentGroup>();
	private ArrayList<OpponentGroup> offGroup2 = new ArrayList<OpponentGroup>();
	private ArrayList<OpponentGroup> offGroup1 = new ArrayList<OpponentGroup>();

	private ArrayList<OpponentGroup> opCap = new ArrayList<OpponentGroup> ();
	private ArrayList<OpponentGroup> offCap = new ArrayList<OpponentGroup> ();
	
	private ArrayList<OpponentGroup> all = new ArrayList<OpponentGroup>();
	
	
	private int ownCapture=0;
	private int opCapture=0;
	
	public Ralph(GameBoard b, int s){
		turn=s;
		bWidthSquares=b.getBoardWidthSquares();
		gameboard=b;
		board=b.getBoard();	
	}
	
	public void move(){
		ownCapture=gameboard.getCapture(turn);
		opCapture=gameboard.getCapture(turn*(-1));
		assessBoard();
		
//		System.out.println("ownCapture: "+ownCapture+" opCapture: "+opCapture);
		int moveRow, moveCol;
		Square sq=blockSquare();
//		System.out.println(sq);
		if(sq!=null) {
			gameboard.placeSquare(sq);
			return;
		}
		
		else do {
			moveRow=2+(int) (Math.random()*(bWidthSquares-4));
			moveCol=2+(int) (Math.random()*(bWidthSquares-4));
		} while(board[moveRow][moveCol].getState()!=Main.EMPTY);
		
		gameboard.placeSquare(board[moveRow][moveCol]);
	}
	
	public void reset(){
		ownCapture=0;
		opCapture=0;
	}
	
	public void addCapture(int state){
		if (state==turn)
			ownCapture++;
		else
			opCapture++;
		
	}
	
	private void assessBoard(){
		checkDisc();
		check4();
		check3();
		check2();
		check1();
		checkCap();
		totalSort(); 
	}
	
	private Square blockSquare(){
		Square toBlock=null;
		int index=0;
		while(/*(int)Math.random()*5<difficulty &&*/
				(toBlock==null || toBlock.getState()!=Main.EMPTY) && index<all.size()){
			if(all.get(index).getGroupType()==OpponentGroup.Defensive && checkCapPriority(all.get(index))!=null)
				toBlock=getBlockSquare(checkCapPriority(all.get(index)));
			else if(containGroup(offDisc,all.get(index)) || containGroup(opDisc,all.get(index)))
				toBlock=all.get(index).getDiscSpace();
			else
				toBlock=getBlockSquare(all.get(index));
			index++;
			}
			
//		System.out.println(toBlock);
		if ((int)(Math.random()*random)==0 && all.get(index-1).getGroupRanking()<=4)
			toBlock=null;
		if (toBlock!=null && toBlock.getState()==Main.EMPTY) 
			return toBlock;
		else return null;
	}
	

	
	private void check1(){
		opGroup1.clear();
		offGroup1.clear();
		for (Square[] column : board)
			for (Square s: column) {
				if(s.getState()==turn*(-1)){
					if(!checkNeighbor(s)){
						OpponentGroup g1=new OpponentGroup(-1);
						g1.addSquare(s);
						g1.setEnd1Square(s);
						g1.setEnd2Square(s);
						g1.setEnd1Block(false);
						g1.setEnd2Block(false);
						g1.setDirection(getNeighborOp(s));
						opGroup1.add(g1);
					}
				}
				else if(s.getState()==turn){
					if(!checkNeighbor(s)){
						OpponentGroup g1=new OpponentGroup(1);
						g1.addSquare(s);
						g1.setEnd1Square(s);
						g1.setEnd2Square(s);
						g1.setEnd1Block(false);
						g1.setEnd2Block(false);
						g1.setDirection(getNeighborOp(s));
						offGroup1.add(g1);
						}
				}
				}
//		checkRanking(opGroup1);
//		checkRanking(offGroup1);
//		System.out.println("opGroup1\n"+opGroup1);
//		System.out.println("offGroup1\n"+offGroup1);
	}
	
	
	private void check2(){
		opGroup2.clear();
		offGroup2.clear();
		for (Square[] column : board)
			for (Square s: column) {
				if(s.getState()==turn*(-1)){
					checkContinuous(s,-1,2,1,0,opGroup2); 
					checkContinuous(s,-1,2,0,1,opGroup2);
					checkContinuous(s,-1,2,1,1,opGroup2);
					checkContinuous(s,-1,2,-1,1,opGroup2);
				}
				else if (s.getState()==turn){
					checkContinuous(s,1,2,1,0,offGroup2); 
					checkContinuous(s,1,2,0,1,offGroup2);
					checkContinuous(s,1,2,1,1,offGroup2);
					checkContinuous(s,1,2,-1,1,offGroup2);
				}
			}
//		checkRanking(opGroup2);
//		checkRanking(offGroup2);
//		System.out.println("opGroup2\n"+opGroup2);
//		System.out.println("offGroup2\n"+offGroup2);
	}
	
	private void check3(){
		opGroup3.clear();
		offGroup3.clear();
		for (Square[] column : board)
			for (Square s: column) {
				if(s.getState()==turn*(-1)){
					checkContinuous(s,-1,3,1,0,opGroup3);
					checkContinuous(s,-1,3,0,1,opGroup3);
					checkContinuous(s,-1,3,1,1,opGroup3);
					checkContinuous(s,-1,3,-1,1,opGroup3);
				}
				else if(s.getState()==turn){
					checkContinuous(s,1,3,1,0,offGroup3);
					checkContinuous(s,1,3,0,1,offGroup3);
					checkContinuous(s,1,3,1,1,offGroup3);
					checkContinuous(s,1,3,-1,1,offGroup3);
				}
			}
//		checkRanking(opGroup3);
//		checkRanking(offGroup3);
//		System.out.println("opGroup3\n"+opGroup3);
//		System.out.println("offGroup3\n"+offGroup3);
	}
	
	private void check4(){
		opGroup4.clear();
		offGroup4.clear();
		for (Square[] column : board)
			for (Square s: column) {
				if(s.getState()==turn*(-1)){
					checkContinuous(s,-1,4,1,0,opGroup4);
					checkContinuous(s,-1,4,0,1,opGroup4);
					checkContinuous(s,-1,4,1,1,opGroup4);
					checkContinuous(s,-1,4,-1,1,opGroup4);
				}
				else if(s.getState()==turn){
					checkContinuous(s,1,4,1,0,offGroup4);
					checkContinuous(s,1,4,0,1,offGroup4);
					checkContinuous(s,1,4,1,1,offGroup4);
					checkContinuous(s,1,4,-1,1,offGroup4);
				}
			}
//		checkRanking(opGroup4);
//		checkRanking(offGroup4);
//		System.out.println("opGroup4\n"+opGroup4);
//		System.out.println("offGroup4\n"+offGroup4);
	}
	
	private void checkDisc(){
		opDisc.clear();
		offDisc.clear();
		for (Square[] column : board)
			for (Square s: column) {
				if(s.getState()==Main.EMPTY){
				checkDisc(s,-1,1,0,opDisc);
				checkDisc(s,-1,0,1,opDisc);
				checkDisc(s,-1,1,1,opDisc);
				checkDisc(s,-1,-1,1,opDisc);
				checkDisc(s,1,1,0,offDisc);
				checkDisc(s,1,0,1,offDisc);
				checkDisc(s,1,1,1,offDisc);
				checkDisc(s,1,-1,1,offDisc);
				}
			}
//		sort(opDisc);
//		sort(offDisc);
//		System.out.println("opDisc\n"+opDisc);
//		System.out.println("offDisc\n"+offDisc);
	}
	
	private void checkCap(){
		opCap.clear();
		offCap.clear();
		for(OpponentGroup op:offGroup2)
			if((op.getEnd1Block() && !op.getEnd2Block()) || (!op.getEnd1Block() && op.getEnd2Block())){
				 if(checkCapSides(op))
					opCap.add(op);
				//offGroup2.remove(op);
			}
		for(OpponentGroup off:opGroup2)
			if((off.getEnd1Block() && !off.getEnd2Block()) || (!off.getEnd1Block() && off.getEnd2Block())){
				if(checkCapSides(off))
					offCap.add(off);
				//opGroup2.remove(off);
			}
		
//		sort(opCap);
//		sort(offCap);
//		System.out.println("opCap\n"+opCap);
//		System.out.println("offCap\n"+offCap);
	}
	
	private boolean checkCapSides(OpponentGroup group){
		if(group.getDirection()==OpponentGroup.Horizontal)
			if(group.getEnd1Square().getCol()>0 && group.getEnd2Square().getCol()<bWidthSquares-1)
				return true;
		if(group.getDirection()==OpponentGroup.Vertical)
			if(group.getEnd1Square().getRow()>0 && group.getEnd2Square().getRow()<bWidthSquares-1)
				return true;
		if(group.getDirection()==OpponentGroup.DiagRight)
			if(group.getEnd1Square().getCol()>0 && group.getEnd1Square().getRow()>0 &&
				group.getEnd2Square().getCol()<bWidthSquares-1 && group.getEnd2Square().getRow()<bWidthSquares-1)
				return true;
		if(group.getDirection()==OpponentGroup.DiagLeft)
			if(group.getEnd1Square().getCol()<bWidthSquares-1 && group.getEnd1Square().getRow()>0 &&
				group.getEnd2Square().getCol()>0 && group.getEnd2Square().getRow()<bWidthSquares-1)
				return true;
		return false;
	}
	
	private OpponentGroup checkCapPriority(OpponentGroup group){
		ArrayList<Square> squareList=group.getGroupList();
		for(Square s:squareList)
			for(OpponentGroup capGroup:offCap)
				if(capGroup.containSquare(s))
					return capGroup;
		return null;
		
	}
	
	private void checkDisc(Square s, int type, int dx, int dy, ArrayList<OpponentGroup> OpList){
		int currentState=turn*type; 
		int row=s.getRow(); 
		int col=s.getCol(); 
		boolean l=true, r=true;
		int count=1, i=0; 
		int direction=0;
		if (dy==0) direction=OpponentGroup.Horizontal;
		else if (dx==0) direction=OpponentGroup.Vertical;
		else if (dx==1) direction=OpponentGroup.DiagRight;
		else if (dx==-1) direction=OpponentGroup.DiagLeft;
		
		OpponentGroup og=new OpponentGroup(type);
		og.setDiscSpace(s);
		og.setDirection(direction);
		
		if((dx>=0 && row-dy>=0 && col-dx>=0 && row+dy<bWidthSquares && col+dx<bWidthSquares) ||
			(dx<0 && row-dy>=0 && col-dx<bWidthSquares && row+dy<bWidthSquares && col+dx>=0)){
			if(checkSameState(board[row-dy][col-dx],currentState,true) &&
				checkSameState(board[row+dy][col+dx],currentState,true) ){
		Square s1, s2;
		while(i<=4 && count>i){
			i++;
			//left
			
			if((dx>=0 && row-dy*i>=0 && col-dx*i>=0) ||
				(dx<0 && row-dy*i>=0 && col-dx*i<bWidthSquares)){
				s1=board[row-dy*i][col-dx*i];
				l=checkSameState(s1, currentState, l); 
				if(s1.getState()!=currentState && og.getEnd1Square()==null) {
					og.setEnd1Square(board[row-dy*(i-1)][col-dx*(i-1)]);
					og.setEnd1Block(s1.getState()==currentState*(-1));
				}
				if(l && !containSquare(OpList,s1,direction)) {
					count++; 
					og.addSquare(s1);
					}
			}
			//right
			if((dx>=0 && row+dy*i<bWidthSquares && col+dx*i<bWidthSquares) ||
				(dx<0 && row+dy*i<bWidthSquares && col+dx*i>=0)){
				s2=board[row+dy*i][col+dx*i];
				r=checkSameState(s2, currentState, r); 
				if(s2.getState()!=currentState && og.getEnd2Square()==null) {
					og.setEnd2Square(board[row+dy*(i-1)][col+dx*(i-1)]);
					og.setEnd2Block(s2.getState()==currentState*(-1));
				}
				if(r && !containSquare(OpList,s2,direction)) {
					count++; 
					og.addSquare(s2);
					}
			}
		}
			}
		}
		if(og.getGroupLength()==3 && (!og.getEnd1Block() && !og.getEnd2Block()) && !OpList.contains(og)) 
			OpList.add(og);
		else if (og.getGroupLength()==4 && (!og.getEnd1Block() || !og.getEnd2Block()) && !OpList.contains(og))
			OpList.add(og);
		else if (og.getGroupLength()==5 && !OpList.contains(og))
			OpList.add(og);
		
	}
	
	private void checkContinuous(Square s, int type, int n, int dx, int dy, ArrayList<OpponentGroup> OpList){
		int currentState=s.getState(); 
		int row=s.getRow(); 
		int col=s.getCol(); 
		boolean l=true, r=true;
		int count=1, i=0; 
		int direction=0;
		if (dy==0) direction=OpponentGroup.Horizontal;
		else if (dx==0) direction=OpponentGroup.Vertical;
		else if (dx==1) direction=OpponentGroup.DiagRight;
		else if (dx==-1) direction=OpponentGroup.DiagLeft;
		
		OpponentGroup og=new OpponentGroup(type);
		og.addSquare(s);
		og.setDirection(direction);
		
		Square s1, s2;
		while(i<=4 && count>i){
			i++;
			//left
			//check if on 4 sides
			if(!og.getEnd1Block() && ((dx>=0 && (row-dy*i==-1 || col-dx*i==-1)) ||
					(dx<0 && (row-dy*i==-1 || col-dx*i==bWidthSquares)))){
				og.setEnd1Square(board[row-dy*(i-1)][col-dx*(i-1)]);
				og.setEnd1Block(true);
			}
			//not on 4 sides
			if((dx>=0 && row-dy*i>=0 && col-dx*i>=0) ||
				(dx<0 && row-dy*i>=0 && col-dx*i<bWidthSquares)){
				s1=board[row-dy*i][col-dx*i];
				l=checkSameState(s1, currentState, l); 
				if(s1.getState()!=currentState && og.getEnd1Square()==null) {
					og.setEnd1Square(board[row-dy*(i-1)][col-dx*(i-1)]);
					og.setEnd1Block(s1.getState()==currentState*(-1));
				}
				if(l && !containSquare(OpList,s1,direction)) {
					count++; 
					og.addSquare(s1);
					}
			}
			//right
			if(!og.getEnd2Block() && ((dx>=0 && (row+dy*i==bWidthSquares || col+dx*i==bWidthSquares)) ||
					(dx<0 && (row+dy*i==bWidthSquares || col+dx*i==-1)))){
				og.setEnd2Square(board[row+dy*(i-1)][col+dx*(i-1)]);
				og.setEnd2Block(true);
			}
			if((dx>=0 && row+dy*i<bWidthSquares && col+dx*i<bWidthSquares) ||
				(dx<0 && row+dy*i<bWidthSquares && col+dx*i>=0)){
				s2=board[row+dy*i][col+dx*i];
				r=checkSameState(s2, currentState, r); 
				if(s2.getState()!=currentState && og.getEnd2Square()==null) {
					og.setEnd2Square(board[row+dy*(i-1)][col+dx*(i-1)]);
					og.setEnd2Block(s2.getState()==currentState*(-1));
				}
				if(r && !containSquare(OpList,s2,direction)) {
					count++; 
					og.addSquare(s2);
					}
			}
		}
		
		if(og.getGroupLength()==n && !OpList.contains(og)) OpList.add(og);
		
	}
	
	
	private Square getBlockSquare(OpponentGroup group){
		if(group.getGroupRanking()<=0) return null; 

		else {
		int direction=group.getDirection();
		Square e1=group.getEnd1Square();
		Square e2=group.getEnd2Square();
		int e1Row=e1.getRow(), e1Col=e1.getCol();
		int e2Row=e2.getRow(), e2Col=e2.getCol();
		//check 4 directions to place the block
		if(direction==OpponentGroup.Horizontal) {
			if(!group.getEnd1Block())
				return board[e1Row][e1Col-1];
			else if(!group.getEnd2Block())
				return board[e2Row][e2Col+1];
				
		}
		else if(direction==OpponentGroup.Vertical) {
			if(!group.getEnd1Block())
				return board[e1Row-1][e1Col];
			else if(!group.getEnd2Block())
				return board[e2Row+1][e2Col];
				
		}
		else if(direction==OpponentGroup.DiagRight) {
			if(!group.getEnd1Block())
				return board[e1Row-1][e1Col-1];
			else if(!group.getEnd2Block())
				return board[e2Row+1][e2Col+1];
				
		}
		else if(direction==OpponentGroup.DiagLeft) {
			if(!group.getEnd1Block())
				return board[e1Row-1][e1Col+1];
			else if(!group.getEnd2Block())
				return board[e2Row+1][e2Col-1];
				
		}
		}
		return board[0][0];
	}
	

	private void checkRanking(ArrayList<OpponentGroup> squareList){
		for (OpponentGroup group:squareList){
//ranking 12: offensive disconnected 4, or offensive last capture (already 4 captures)
			if (group.getGroupType()==OpponentGroup.Offensive && group.getGroupLength()==5 && group.getDiscSpace()!=null)
				group.setGroupRanking(12);
			else if (ownCapture==4 && group.getGroupType()==OpponentGroup.Defensive && group.getGroupLength()==2 && (group.getEnd1Block() || group.getEnd2Block()))
				group.setGroupRanking(12);
//ranking 11: defensive disconnected 4, or defensive last capture (already 4 captures)
			else if (group.getGroupType()==OpponentGroup.Defensive && group.getGroupLength()==5 && group.getDiscSpace()!=null)
				group.setGroupRanking(11);
			else if (opCapture==4 && group.getGroupType()==OpponentGroup.Offensive && group.getGroupLength()==2 && (group.getEnd1Block() || group.getEnd2Block()))
				group.setGroupRanking(11);
//lowest ranking 0: exclude all groups blocked in both ends
			else if (group.getEnd1Block() && group.getEnd2Block())
				group.setGroupRanking(0);
//ranking 12: offensive 4 in row
			else if (group.getGroupType()==OpponentGroup.Offensive && group.getGroupLength()==4)
				group.setGroupRanking(12);
//ranking 11: defensive 4 in row
			else if (group.getGroupType()==OpponentGroup.Defensive && group.getGroupLength()==4)
				group.setGroupRanking(11);
//ranking 10: offensive 3 in row unblocked, or disconnected 3 unblocked
			else if (group.getGroupType()==OpponentGroup.Offensive && group.getGroupLength()==3 && (!group.getEnd1Block() && !group.getEnd2Block()))
				group.setGroupRanking(10);
			else if (group.getGroupType()==OpponentGroup.Offensive && group.getGroupLength()==4 && group.getDiscSpace()!=null && 
					(!group.getEnd1Block() && !group.getEnd2Block()))
				group.setGroupRanking(10);
//ranking 9: defensive 3 in row unblocked, or disconnected 3 unblocked
			else if (group.getGroupType()==OpponentGroup.Defensive && group.getGroupLength()==3 && (!group.getEnd1Block() && !group.getEnd2Block()))
				group.setGroupRanking(9);
			else if (group.getGroupType()==OpponentGroup.Defensive && group.getGroupLength()==4 && group.getDiscSpace()!=null && 
					(!group.getEnd1Block() && !group.getEnd2Block()))
				group.setGroupRanking(9);
//ranking 8: offensive capture (opponent 2 in row, one side block)
			else if ((group.getGroupType()==OpponentGroup.Defensive && group.getGroupLength()==2 && (group.getEnd1Block() || group.getEnd2Block())))
					group.setGroupRanking(8);
//ranking 7: defensive capture
			else if (group.getGroupType()==OpponentGroup.Offensive && group.getGroupLength()==2 && (group.getEnd1Block() || group.getEnd2Block()))
				group.setGroupRanking(7);
//ranking 6: offensive 2 in row unblocked
			else if (group.getGroupType()==OpponentGroup.Offensive && group.getGroupLength()==2 && (!group.getEnd1Block() && !group.getEnd2Block()))
				group.setGroupRanking(6);
//ranking 5: defensive 2 in row unblocked
			else if (group.getGroupType()==OpponentGroup.Defensive && group.getGroupLength()==2 && (!group.getEnd1Block() && !group.getEnd2Block()))
				group.setGroupRanking(5);
//ranking 4: offensive 3 in row, one side block
			else if (group.getGroupType()==OpponentGroup.Offensive && group.getGroupLength()==3 && (group.getEnd1Block() || group.getEnd2Block()))
				group.setGroupRanking(4);
//ranking 3: defensive 3 in row, one side block
			else if (group.getGroupType()==OpponentGroup.Defensive && group.getGroupLength()==3 && (group.getEnd1Block() || group.getEnd2Block()))
				group.setGroupRanking(3);
//ranking 2: offensive 1 
			else if (group.getGroupType()==OpponentGroup.Offensive)
				group.setGroupRanking(2);
//ranking 1: defensive 1
			else group.setGroupRanking(1);
		}
		sort(squareList);
	}
	
	private void sort(ArrayList<OpponentGroup> squareList){
		//selection sort based on ranking
		for (int i=0; i<squareList.size()-1; i++)
			for (int j=i+1; j<squareList.size(); j++) 
				if (squareList.get(i).getGroupRanking()<squareList.get(j).getGroupRanking()){
					OpponentGroup tmp=squareList.get(i);
					squareList.set(i,squareList.get(j));
					squareList.set(j, tmp);
				}
	}
	
	private void totalSort(){
		all.clear();
		for(OpponentGroup g: offDisc)
			all.add(g);
		for(OpponentGroup g: opDisc)
			all.add(g);
		for(OpponentGroup g: offGroup4)
			all.add(g);
		for(OpponentGroup g: opGroup4)
			all.add(g);
		for(OpponentGroup g: offGroup3)
			all.add(g);
		for(OpponentGroup g: opGroup3)
			all.add(g);
		for(OpponentGroup g: offGroup2)
			all.add(g);
		for(OpponentGroup g: opGroup2)
			all.add(g);
		for(OpponentGroup g: offGroup1)
			all.add(g);
		for(OpponentGroup g: opGroup1)
			all.add(g);
		checkRanking(all);
//		System.out.println("all\n"+all);
	}
	
	private boolean checkSameState(Square s, int state, boolean condition){
		if (condition && s.getState()==state)
			return true;
		return false;
	}
	
	private boolean containSquare(ArrayList<OpponentGroup> squareList, Square s, int direction){
		for (OpponentGroup group:squareList)
			if(group.containSquare(s)&&group.getDirection()==direction) return true;
		return false;
	}
	
	private boolean containGroup(ArrayList<OpponentGroup> squareList, OpponentGroup oGroup){
		for (OpponentGroup group:squareList)
			if(group.equals(oGroup)) return true;
		return false;
	}
	
	//check 8 directions for neighbors
	private boolean checkNeighbor(Square s){
		int currentState=s.getState();
		int row=s.getRow();
		int col=s.getCol();
		if (row==0 || row==bWidthSquares-1 || col==0 || col==bWidthSquares-1)
			return true;
		else if(board[row-1][col-1].getState()==currentState || 
				board[row-1][col].getState()==currentState ||
				board[row-1][col+1].getState()==currentState ||
				board[row][col-1].getState()==currentState ||
				board[row][col+1].getState()==currentState ||
				board[row+1][col-1].getState()==currentState ||
				board[row+1][col].getState()==currentState ||
				board[row+1][col+1].getState()==currentState)
			return true;
		else return false;
	}
	
	private int getNeighborOp(Square s){
		ArrayList<Integer> possibleDirections=new ArrayList<Integer>();
		int direction=0;
		possibleDirections.add(1);
		possibleDirections.add(2);
		possibleDirections.add(3);
		possibleDirections.add(4);
		int currentState=s.getState();
		int row=s.getRow();
		int col=s.getCol();
		if((col+1<bWidthSquares && board[row][col+1].getState()==currentState*(-1)) ||
				(col-1>=0 && board[row][col-1].getState()==currentState*(-1)))
			possibleDirections.remove(possibleDirections.indexOf(1));
		if((row+1<bWidthSquares && board[row+1][col].getState()==currentState*(-1)) ||
				(row-1>=0 && board[row-1][col].getState()==currentState*(-1)))
			possibleDirections.remove(possibleDirections.indexOf(2));
		if((col+1<bWidthSquares && row+1<bWidthSquares && board[row+1][col+1].getState()==currentState*(-1)) ||
				(col-1>=0 && row-1>=0 && board[row-1][col-1].getState()==currentState*(-1)))
			possibleDirections.remove(possibleDirections.indexOf(3));
		if((col-1>=0 && row+1<bWidthSquares && board[row+1][col-1].getState()==currentState*(-1)) ||
				(col+1<bWidthSquares && row-1>=0 && board[row-1][col+1].getState()==currentState*(-1)))
			possibleDirections.remove(possibleDirections.indexOf(4));
		if(possibleDirections.size()>0)
			direction=(int) (possibleDirections.get((int)(Math.random()*possibleDirections.size())));
		return direction;
	}
}
