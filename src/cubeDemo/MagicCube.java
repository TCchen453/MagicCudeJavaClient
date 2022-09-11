package cubeDemo;

import java.awt.Color;

import math3D.Point3D;

public class MagicCube {
	public static void main(String[]args) {
		
	}
	final double w=300;
	final double c=0.8;
	int nxtAndPre[][]= {
			{},
			{},
			{},
			{},
			{},
			{},
	};
	int choseSquareId=-1;
	char[]udlr= {'U','D','L','R','F','B'};
	
	Color[]col= {
			Color.white,Color.yellow,
			Color.red,Color.orange,
			Color.blue,Color.green
			};
	void initColor() {
		col[0]=Color.white;
		col[1]=Color.yellow;
		col[2]=Color.red;
		col[3]=Color.orange;
		col[4]=Color.blue;
		col[5]=Color.green;
		for(int i=0;i<6;i++)
		{
			for(int j=0;j<9;j++)
			{
				ss[i][j].color=col[mode[i][j]];
			}
		}
	}
	int[][]mode= {
			{0,0,0,0,0,0,0,0,0},
			{1,1,1,1,1,1,1,1,1},
			{2,2,2,2,2,2,2,2,2},
			{3,3,3,3,3,3,3,3,3},
			{4,4,4,4,4,4,4,4,4},
			{5,5,5,5,5,5,5,5,5},
	};
	final int[][][]paint= {
			{
				{0,3,6},
				{1,4,7},
				{2,5,8}
			},{
				{6,3,0},
				{7,4,1},
				{8,5,2}
			},{
				{0,1,2},
				{3,4,5},
				{6,7,8}
			},{
				{2,1,0},
				{5,4,3},
				{8,7,6}
			},{
				{0,3,6},
				{1,4,7},
				{2,5,8}
			},{
				{2,5,8},
				{1,4,7},
				{0,3,6}
			},
	};
	public MagicCube(){
		initCube();
	}
	Square ss[][]=new Square[6][9];
	void initCube() {
		double pp=w/2;
		double pre=w/3;
		double prec=pre*c;
		double black=(pre-prec)/2;
		for(int i=0;i<3;i++)
		{
			for(int j=0;j<3;j++)
			{
				double a=i*pre+black-pp;
				double b=j*pre+black-pp;
				ss[0][paint[0][i][j]]=new Square(new Point3D(a,-pp,b), new Point3D(a,-pp,b+prec), new Point3D(a+prec,-pp,b+prec), new Point3D(a+prec,-pp,b), col[mode[0][paint[0][i][j]]]);
				ss[1][paint[1][i][j]]=new Square(new Point3D(a, pp,b), new Point3D(a, pp,b+prec), new Point3D(a+prec, pp,b+prec), new Point3D(a+prec, pp,b), col[mode[1][paint[1][i][j]]]);
				ss[2][paint[2][i][j]]=new Square(new Point3D(-pp,a,b), new Point3D(-pp,a,b+prec), new Point3D(-pp,a+prec,b+prec), new Point3D(-pp,a+prec,b), col[mode[2][paint[2][i][j]]]);
				ss[3][paint[3][i][j]]=new Square(new Point3D( pp,a,b), new Point3D( pp,a,b+prec), new Point3D( pp,a+prec,b+prec), new Point3D( pp,a+prec,b), col[mode[3][paint[3][i][j]]]);
				ss[4][paint[4][i][j]]=new Square(new Point3D(a,b, pp), new Point3D(a,b+prec, pp), new Point3D(a+prec,b+prec, pp), new Point3D(a+prec,b, pp), col[mode[4][paint[4][i][j]]]);
				ss[5][paint[5][i][j]]=new Square(new Point3D(a,b,-pp), new Point3D(a,b+prec,-pp), new Point3D(a+prec,b+prec,-pp), new Point3D(a+prec,b,-pp), col[mode[5][paint[5][i][j]]]);
				for(int k=0;k<6;k++) {
					ss[k][paint[k][i][j]].ind=k*9+paint[k][i][j];
				}
			}
		}
	}
	void updataColor() {
		for(int i=0;i<6;i++)
		{
			for(int j=0;j<9;j++)
			{
				ss[i][j].color=col[mode[i][j]];
			}
		}
	}
	boolean check() {
        int[] to = new int[6];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 9; j++) {
                to[mode[i][j]]++;
            }
        }
        for (int i = 0; i < 6; i++) {
            if (to[i] != 9)
                return false;
        }
        return true;
    }
	String[]getStrState(int[][]mode) {
		String[]ans=new String[20];
		int[][]nxt= {
				{ 7,41},//UF
				{ 5,31},//UR
				{ 1,51},//UB
				{ 3,21},//UL
				{11,47},//DF
				{15,37},//DR
				{17,57},//DB
				{13,27},//DL
				{45,33},//FR
				{43,25},//FL
				{53,35},//BR
				{55,23},//BL
				{ 8,42,30},//UFR
				{ 2,32,50},//URB
				{ 0,52,20},//UBL
				{ 6,22,40},//ULF
				{12,36,48},//DRF
				{10,46,28},//DFL
				{16,26,58},//DLB
				{18,56,38},//DBR
		};
		int u,v,i,j,k;
		for(i=0;i<20;i++)
		{
			ans[i]="";
			for(j=0;j<nxt[i].length;j++)
			{
				u=nxt[i][j]/10;
				v=nxt[i][j]%10;
				k=mode[u][v];
				ans[i]=ans[i]+udlr[k];
			}
		}
		return ans;
	}
	void changeState(int x,int y,int k) {
		mode[x][y]=k;
		ss[x][y].color=col[k];
	}
	String[] getStrState() {
		String[]ans=new String[20];
		int[][]nxt= {
				{ 7,41},//UF
				{ 5,31},//UR
				{ 1,51},//UB
				{ 3,21},//UL
				{11,47},//DF
				{15,37},//DR
				{17,57},//DB
				{13,27},//DL
				{45,33},//FR
				{43,25},//FL
				{53,35},//BR
				{55,23},//BL
				{ 8,42,30},//UFR
				{ 2,32,50},//URB
				{ 0,52,20},//UBL
				{ 6,22,40},//ULF
				{12,36,48},//DRF
				{10,46,28},//DFL
				{16,26,58},//DLB
				{18,56,38},//DBR
		};
		int u,v,i,j,k;
		for(i=0;i<20;i++)
		{
			ans[i]="";
			for(j=0;j<nxt[i].length;j++)
			{
				u=nxt[i][j]/10;
				v=nxt[i][j]%10;
				k=mode[u][v];
				ans[i]=ans[i]+udlr[k];
			}
		}
		return ans;
	}
	final int[][][]ro= {
			{
				{ 0, 2, 8, 6},
				{ 1, 5, 7, 3},
				{32,42,22,52},
				{31,41,21,51},
				{30,40,20,50}
			},{
				{10,12,18,16},
				{11,15,17,13},
				{56,26,46,36},
				{57,27,47,37},
				{58,28,48,38},
			},{
				{20,22,28,26},
				{21,25,27,23},
				{10,58, 0,40},
				{13,55, 3,43},
				{16,52, 6,46}
			},{
				{30,32,38,36},
				{31,35,37,33},
				{18,48, 8,50},
				{15,45, 5,53},
				{12,42, 2,56}
			},{
				{40,42,48,46},
				{41,45,47,43},
				{12,28, 6,30},
				{11,25, 7,33},
				{10,22, 8,36},
			},{
				{50,52,58,56},
				{51,55,57,53},
				{16,38, 2,20},
				{17,35, 1,23},
				{18,32, 0,26}
			}
	};
	double t[][];
	int x,page=-1,allPage=30;
	double perPageAngle;
	Point3D l,r;
	void changeAllpage(int num) {
		while(page!=-1)System.out.print("");
		allPage=num;
	}
	void rotate(int x,int t) {
		boolean flag=choseSquareId!=-1;
		int k=-1;
		k*=t;
		int[][]nxt=ro[x];
		int j,tmp,ind,u,v,pu,pv,preind,nowind,tmpind;
		Color tmpc;
		for(int i=0;i<nxt.length;i++)
		{
			pu=u=nxt[i][0]/10;
			pv=v=nxt[i][0]%10;
			tmp=mode[u][v];
			tmpc=ss[u][v].color;
			preind=nowind=tmpind=ss[u][v].ind;
			for(j=1,ind=((0+k)+nxt[i].length)%nxt[i].length;j<nxt[i].length;j++,ind=((ind+k)+nxt[i].length)%nxt[i].length) {
				u=nxt[i][ind]/10;
				v=nxt[i][ind]%10;
				nowind=ss[u][v].ind;
				if(flag&&nowind==choseSquareId) {
					choseSquareId=preind;
					flag=false;
				}
				mode[pu][pv]=mode[u][v];
				ss[pu][pv].color=ss[u][v].color;
				pu=u;
				pv=v;
				preind=nowind;
			}
			if(flag&&tmpind==choseSquareId) {
				choseSquareId=preind;
				flag=false;
			}
			mode[pu][pv]=tmp;
			ss[pu][pv].color=tmpc;
			
		}
		this.x=x;
		initAnimate(x,t);
	}
	void animateNextPage() {
		l=getMid(ss[x][4].p[0],ss[x][4].p[2]);
		r=getMid(ss[x^1][4].p[0],ss[x^1][4].p[2]);
		t=getT(l,r,perPageAngle);
		changeAllPoint(x);
		page++;
		if(page==allPage)page=-1;
	}
	void changeAllPoint(int x) {
		int[][]nxt=ro[x];
		int u,v;
		Point3D to;
		for(int i=0;i<nxt.length;i++)
		{
			for(int j=0;j<nxt[i].length;j++)
			{
				u=nxt[i][j]/10;
				v=nxt[i][j]%10;
				for(Point3D p:ss[u][v].p) {
					to=rotateByT(t, p);
					p.x=to.x;
					p.y=to.y;
					p.z=to.z;
				}
			}
		}
		for(Point3D p:ss[x][4].p) {
			to=rotateByT(t, p);
			p.x=to.x;
			p.y=to.y;
			p.z=to.z;
		}
	}
	void initAnimate(int x,int time) {
		this.x=x;
		l=getMid(ss[x][4].p[0],ss[x][4].p[2]);
		r=getMid(ss[x^1][4].p[0],ss[x^1][4].p[2]);
		double angle=90*time;
		t=getT(l,r,angle);
		changeAllPoint(x);
		angle=-90*time;
		perPageAngle=angle/allPage;
		page=0;
	}
	double[][] getT(Point3D l,Point3D r,double dp) {
		Point3D n = new Point3D(r.x-l.x,r.y-l.y,r.z-l.z);
		double cosa=Math.cos(Math.PI*dp/180);
		double sina=Math.sin(Math.PI*dp/180);
		double K=1-cosa;
		double p=Math.sqrt(n.x*n.x+n.y*n.y+n.z*n.z);
		n.x/=p;
		n.y/=p;
		n.z/=p;
		double t[][]= {
				{n.x*n.x*K+cosa,n.x*n.y*K-n.z*sina,n.x*n.z*K+n.y*sina,0},
				{n.x*n.y*K+n.z*sina,n.y*n.y*K+cosa,n.y*n.z*K-n.x*sina,0},
				{n.z*n.x*K-n.y*sina,n.y*n.z*K+n.x*sina,n.z*n.z*K+cosa,0},
		};
		return t;
	}
	Point3D rotateByT(double [][]t,Point3D from) {
		return new Point3D(
				from.x*t[0][0]+from.y*t[0][1]+from.z*t[0][2],
				from.x*t[1][0]+from.y*t[1][1]+from.z*t[1][2],
				from.x*t[2][0]+from.y*t[2][1]+from.z*t[2][2]);
	}
	Point3D getMid(Point3D a,Point3D b) {
		return new Point3D((a.x+b.x)/2,(a.y+b.y)/2,(a.z+b.z)/2);
	}
	private char[]co= {'0','1','2','3','4','5'};
	private int[]nxt= {0,2,4,3,5,1};
	char[] getChar() {
		char[]ch=new char[54];
		int ind=0;
		for(int i=0;i<nxt.length;i++)
		{
			for(int j=0;j<mode[i].length;j++)
			{
				ch[ind++]=co[mode[i][j]];
			}
		}
		return ch;
	}
}
