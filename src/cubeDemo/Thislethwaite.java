package cubeDemo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.TreeMap;

/*
 * G0 U  D  L  R  F  B  43,252,003,274,489,856,000 总状态数
 * G1 U  D  L  R  F2 B2 21,119,142,223,872,000 棱块色相
 * G2 U  D  L2 R2 F2 B2 19,508,428,800 角块色相+棱块位置
 * G3 U2 D2 L2 R2 F2 B2 663,552 
 */
public class Thislethwaite {
	String []goal= { "UF", "UR", "UB", "UL", "DF", "DR", "DB", "DL", "FR", "FL", "BR", "BL",
		    "UFR", "URB", "UBL", "ULF", "DRF", "DFL", "DLB", "DBR" };
	String []input={"UR","UB","UL","BL","DL","DF","DR","FL","FR","UF","BR","DB","URB","UBL","RDB","LFU","DFL","RUF","LBD","DRF",};
	int applicableMoves[] = { 0, 262143, 259263, 74943, 74898 };
	int affectedCubies[][] = {
			  {  0,  1,  2,  3,  0,  1,  2,  3 },   // U
			  {  4,  7,  6,  5,  4,  5,  6,  7 },   // D
			  {  0,  9,  4,  8,  0,  3,  5,  4 },   // F
			  {  2, 10,  6, 11,  2,  1,  7,  6 },   // B
			  {  3, 11,  7,  9,  3,  2,  6,  5 },   // L
			  {  1,  8,  5, 10,  1,  0,  4,  7 },   // R
			};
	public static void main(String[]args)
	{
		String []input={"UR","UB","UL","BL","DL","DF","DR","FL","FR","UF","BR","DB","URB","UBL","RDB","LFU","DFL","RUF","LBD","DRF",};
		new Thislethwaite().run(input);
	}
	int inverse ( int move ) {
		return move + 2 - 2 * (move % 3);
	}
	int find(String str,String[]strs) {
		for(int i=0;i<strs.length;i++)
		{
			if(str.equals(strs[i]))return i;
		}
		return -1;
	}
	int[]copy(int[]state,int st,int ed){
		int[]ans=new int[ed-st];
		for(int i=0;st<ed;i++,st++)
		{
			ans[i]=state[st];
		}
		return ans;
	}
	int[] applyMove(int move,int[] stat) {
		int turns=move%3+1;
		int face=move/3;
		int[]state=stat.clone();
		while(turns-->0)
		{
			int[] oldState = state.clone();
		    for( int i=0; i<8; i++ ){
				int isCorner = i > 3?1:0;
				int target = affectedCubies[face][i] + isCorner*12;
				int killer = affectedCubies[face][(i&3)==3 ? i-3 : i+1] + isCorner*12;
				int orientationDelta = (i<4) ? ((face>1 && face<4)?1:0) : (face<2) ? 0 : 2 - (i&1);
				state[target] = oldState[killer];
				//state[target+20] = (oldState[killer+20] + orientationDelta) % (2 + isCorner);
				state[target+20] = oldState[killer+20] + orientationDelta;
				if( turns==0 )
					state[target+20] %= 2 + isCorner;
		    }
		}
		return state;
	}
	long id(int[] state){
		if(phase<2) {
			long ans=0;
			for(int i=20;i<32;i++)
			{
				ans<<=1;
				ans+=state[i]%2;
			}
			return ans;
		}
		else if(phase<3) {
			long ans=0;
			for(int i=0;i<12;i++)
			{
				ans|=(state[i]/8)<<i;
			}
			for(int i=32;i<40;i++)
			{
				ans*=3;
				ans+=state[i];
			}
			return ans;
		}else if(phase<4) {
			long ans=0;
			for( int e=0; e<12; e++ )
				ans|=((state[e] > 7) ? 2 : (state[e] & 1)) << (2*e);
			ans<<=24;
			for( int c=0; c<8; c++ )
			{
				ans|= ((state[c+12]-12) & 5) << (3*c);
			}
			ans<<=1;
			int k=0;
			for( int i=12; i<20; i++ )
				for( int j=i+1; j<20; j++ )
					k ^= state[i] > state[j]?1:0;
			ans+=k;
			return ans;
		}
		long ans=0;
		for(int i=0;i<8;i++)
		{
			ans<<=2;
			ans+=state[i]/2;
		}
		for(int i=8;i<12;i++)
		{
			ans<<=2;
			ans+=state[i]-8;
		}
		for(int i=12;i<20;i++)
		{
			ans<<=2;
			ans+=(state[i]-12)/2;
		}
		return ans;
	}
	int phase=0;
	public String[] run(String[]str) {
		input=str;
		String[]ans=new String[5];
		char[]udlr= {'U','D','F','B','L','R'};
		int[] currentState=new int[40];
		int[] goalState=new int[40];
		int i;
		for(i=0;i<20;i++)
		{
			goalState[i]=i;
			String cubie=input[i];
			while((currentState[i]=find(cubie,goal))==-1) {
				cubie=cubie.substring(1)+cubie.charAt(0);
				currentState[i+20]++;
				if (currentState[i + 20] > 4) {
					System.out.println("wrong cube");
					return null;
				}
			}
		}
		phase=0;
		ans[0]="";
		nextPhasePlease:while(++phase<5) {
			StringBuffer sb=new StringBuffer("");
			long currentId = id(currentState), goalId = id(goalState);
			if(currentId==goalId) 
				continue;
			Queue<int[]>q=new LinkedList<int[]>();
			q.add(currentState.clone());
			q.add(goalState.clone());
			TreeMap<Long,Long>predecessor=new TreeMap<Long,Long>();
			TreeMap<Long,Integer>direction=new TreeMap<Long,Integer>(),
					lastMove=new TreeMap<Long,Integer>();
			
			direction.put(currentId,1);
			direction.put(goalId,2);
			while(true) {
				if (q.isEmpty()) {
					System.out.println("wrong cube");
					return null;
				}
				int[] oldState=q.poll();
				long oldId=id(oldState);
				Integer oldDir=direction.get(oldId);
				for(int move=0;move<18;move++)
				{
					if((applicableMoves[phase]&(1<<move))!=0) {
						int[]newState=applyMove(move, oldState);
						long newId=id(newState);
						Integer newDir=direction.get(newId);
						if(newDir==null) {
							q.add(newState.clone());
							newDir=oldDir;
							lastMove.put(newId,move);
							predecessor.put(newId,oldId);
							direction.put(newId,oldDir);
						}
						if(newDir!=null&&newDir!=oldDir) {
							if(oldDir!=null&&oldDir>1) {
								long tmp=newId;
								newId=oldId;
								oldId=tmp;
								move=inverse(move);
							}
							ArrayList<Integer>algorithm=new ArrayList<Integer>();
							algorithm.add(move);
							while(oldId!=currentId)
							{
								algorithm.add(0, lastMove.get(oldId));
								oldId=predecessor.get(oldId);
							}
							while(newId!=goalId) {
								algorithm.add(inverse(lastMove.get(newId)));
								newId=predecessor.get(newId);
							}
							
							for(int a:algorithm) {
								String per=udlr[a/3]+""+(a%3+1);
								System.out.print(per);
								sb.append(per);
								currentState=applyMove(a, currentState);
							}
							ans[phase]=sb.toString();
							continue nextPhasePlease;
						}
					}
				}
			}
		}
		System.out.println();
		return ans;
	}
}
