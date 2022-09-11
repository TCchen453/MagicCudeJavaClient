package cubeDemo;

import java.util.ArrayList;

public class Robot {
	class UdlrManager {
		char[]udlr= {'U','D','L','R','F','B'};
		int[]udlrInd=new int[200];
		private String udlrStr;
		public char[][]mcr;
		public int mcrCnt=Integer.MAX_VALUE;
		private void init() {
			for(int i=0;i<udlr.length;i++)
			{
				udlrInd[udlr[i]]=i;
			}
		}
		private void initMcr() {
			if(udlrStr=="-1")return;
			ArrayList<char[]>list=new ArrayList<char[]>();
			char[]ch=udlrStr.toCharArray();
			for(int i=0;i<ch.length;i+=2)
			{
				int k=udlrInd[ch[i]];
				if(ch[i+1]=='1') {
					list.add(new char[] {(char) (k+'0')});
				}else if(ch[i+1]=='2') {
					list.add(new char[] {(char) (k+'0')});
					list.add(new char[] {(char) (k+'0')});
				}else {
					list.add(new char[] {(char) (k+'0'),(char) (k+'0'),(char) (k+'0')});
				}
			}
			mcr=new char[list.size()][];
			for(int i=0;i<mcr.length;i++)
			{
				mcr[i]=list.get(i);
			}
			mcrCnt=0;
		}
		public UdlrManager(String udlrStr) {
			this.udlrStr=udlrStr;
			init();
			initMcr();
		}
	}
	UdlrManager udlrManager;
	void runUdlr(String udlrStr) {
		udlrManager=new UdlrManager(udlrStr);
	}
	
}
