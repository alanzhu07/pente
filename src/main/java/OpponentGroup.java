


import java.util.ArrayList;

public class OpponentGroup {
	
	public static final int Horizontal=1;
	public static final int Vertical=2;
	public static final int DiagRight=3;
	public static final int DiagLeft=4;
	
	public static final int Offensive=1;
	public static final int Defensive=-1;
	
	private ArrayList<Square> groupList;
	private int groupLength=0;
	private int groupRanking=0;
	private int direction=0;
	private int groupType=0;
	private Square end1=null;
	private Square end2=null;
	private Square discSpace=null;
	private boolean end1Block=false;
	private boolean end2Block=false;

	
	public OpponentGroup(int type){
		setGroupType(type);
		groupList=new ArrayList<Square>();
	}
	
	public void addSquare(Square s){
		groupList.add(s);
		groupLength++;
	}
	
	public boolean containSquare(Square s){
		for(Square sq:groupList)
			if (sq.equals(s)) return true;
		return false;
	}

	public ArrayList<Square> getGroupList(){
		return groupList;
	}
	
	public int getGroupLength(){
		return groupLength;
	}
	
	public int getGroupRanking(){
		return groupRanking;
	}
	
	public void setGroupRanking (int newRanking){
		groupRanking=newRanking;
	}
	
	public int getDirection(){
		return direction;
	}
	
	public void setDirection(int d){
		direction=d;
	}
	
	public Square getEnd1Square(){
		return end1;
	}
	
	public void setEnd1Square(Square e1){
		end1=e1;
	}
	
	public Square getEnd2Square(){
		return end2;
	}
	
	public void setEnd2Square(Square e2){
		end2=e2;
	}
	
	public boolean getEnd1Block(){
		return end1Block;
	}
	
	//if no block on one side, ranking+1
	public void setEnd1Block(boolean block){
		end1Block=block;
	}
	
	public boolean getEnd2Block(){
		return end2Block;
	}
	
	public void setEnd2Block(boolean block){
		end2Block=block;
	}
	
	public String toString(){
		String str="[length:"+groupLength+", ranking:"+groupRanking+", type:"+groupType+", direction:"+direction+" ";
		for (Square sq: groupList) 
			str+="<row:"+sq.getRow()+",col:"+sq.getCol()+",state:"+sq.getState()+">";
		if(end1!=null)
			str+=" end1:"+"<row:"+end1.getRow()+",col:"+end1.getCol()+">";
		if(end2!=null)
			str+=" end2:"+"<row:"+end2.getRow()+",col:"+end2.getCol()+">";
		if(discSpace!=null)
			str+=" discSpace:"+"<row:"+discSpace.getRow()+",col:"+discSpace.getCol()+">";
		str+=" e1"+end1Block+" e2"+end2Block+"]";
		return str;
		
	}

	public Square getDiscSpace() {
		return discSpace;
	}

	public void setDiscSpace(Square discSpace) {
		this.discSpace = discSpace;
	}

	public int getGroupType() {
		return groupType;
	}

	public void setGroupType(int groupType) {
		this.groupType = groupType;
	}

	
}
