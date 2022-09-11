package cubeDemo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import cubeDemo.blockPanel.Client;
import math3D.Point3D;
import math3D.Vector2D;

public class blockPanel extends JPanel implements MouseInputListener,MouseWheelListener,KeyListener{
	Robot robot=new Robot();
	public Point[][]btn=new Point[6][4];
	boolean showBtn=false,changeStaLock=true;
	class Client{
		Socket socket=null;
        BufferedReader br;
        PrintWriter out;
        public Client(Socket socket) {
            this.socket = socket;
            try {
                br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
            } catch (Exception e) {
                System.out.println("connect wrong!");
            }
            send("type 2\r\n");
            new Thread(new Reader()).start();
        }
        
        public void send(String msg) {
        	try {
                out.print(msg);
                out.flush();
                // out.writeUTF(msg);
            } catch (Exception e) {
                System.out.println("send err");
            }
        }
        String read() throws IOException {
            return br.readLine();
        }
        public class Reader implements Runnable {
            @Override
            public void run() {
            	System.out.println("reader running");
                while (true) {
                    try {
                        if (br.ready()) {
                            String str = br.readLine();
                            String[] all = str.split(" ");
                        	System.out.println("recv server data:");
                        	System.out.println(str);
                            
                            if (all[0].equals("pic")) {
                            	int[]clo=new int[6];
                            	int[][]sta=new int[6][9];
                            	for(int i=1;i<=6;i++) {
                            		clo[i-1]=Integer.parseInt(all[i]);
                            	}
                            	char[]ch=all[7].toCharArray();
                            	int k=0;
                            	for(int i=0;i<6;i++)
                            	{
                            		for(int j=0;j<9;j++)
                            		{
                            			sta[i][j]=ch[k++]-'0';
                            		}
                            	}
                            	for(int i=0;i<6;i++) {
                            		magicCube.col[i]=new Color(clo[i]);
                            	}
                            	for(int i=0;i<6;i++) {
                            		for(int j=0;j<9;j++) {
                            			magicCube.mode[i][j]=sta[i][j];
                            		}
                            	}
                            	magicCube.updataColor();
                            } else if (all[0].equals("udlr")) {
                            	robot.runUdlr(all[1]);
                            }else if(all[0].equals("do")) {
                            	int k=Integer.parseInt(all[1]);
                            	if(k==-1) {
                            		robot.udlrManager.mcrCnt=0;
                            	}else {
                            		int len=k-robot.udlrManager.mcrCnt+1;
                            		if(len>0) {
                            			for(int i=0;i<len;i++)
                            			{
                            				addToControlList(robot.udlrManager.mcr[robot.udlrManager.mcrCnt++]);
                            			}
                            		}
                            	}
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("read err");
                        e.printStackTrace();
                    }
                }
            }
        }
	}
	Client client;
	String host="192.168.216.39";
	int port=14514;
	class Link implements Runnable{

		@Override
		public void run() {
			try {
				File file=new File("./ip.txt");
				Scanner in=new Scanner(new FileInputStream(file));
				host=in.next();
				port=in.nextInt();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				System.out.println("没事，报错是正常的，这部分是用来连服务器的，有机器人的时候才有用。");
			}
			try {
				Socket socket=new Socket(host,port);
				client=new Client(socket);
				System.out.println("连接服务器成功"+socket.getInetAddress()+":"+socket.getPort());
			} catch (IOException e) {
				System.out.println("连接服务器失败");
			}
		}
		
	}
	double len=2000;
	final int fps=60;
	long timed=1000/fps;
	long pre=0;
	public int Window_Width=900;
	public int Window_Height=700;
	public cube cube=new cube();
	public MagicCube magicCube=new MagicCube();
	ArrayList<Square>a=new ArrayList<Square>();
	ArrayList<Point3D>allP=new ArrayList<Point3D>();
	public blockPanel() {
		init();
		rotateXY(0, Math.PI/4);
		rotateXY(-Math.PI/6,0);
		setBounds(0, 0, 900,700);
		setVisible(true);
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		addKeyListener(this);
		setBackground(Color.black);
		setFocusable(true);
		new Thread(new Link()).start();
		new Thread(new loop()).start();
		new Thread(new CommandController()).start();
	}
	void rotateXY(double a,double b){
		for(Point3D p:allP) {
			p.rotateXY(a, b);
		}
	}
	void initBtn() {
		int siz=40;
		int sp=10;
		int x1=Window_Width-((siz+sp)*2+20);
		int y1=siz;
		for(int k=0;k<2;k++,x1+=siz+sp)
		for(int i=k;i<6;i+=2)
		{
			btn[i][0]=new Point(x1,y1+i/2*(siz+sp));
			btn[i][1]=new Point(x1+siz,y1+i/2*(siz+sp));
			btn[i][2]=new Point(x1+siz,y1+i/2*(siz+sp)+siz);
			btn[i][3]=new Point(x1,y1+i/2*(siz+sp)+siz);
		}
	}
	void init() {
		initBtn();
		initudlrKey();
		for(Square ss[]:magicCube.ss) {
			for(Square s:ss) {
				a.add(s);
				for(Point3D p:s.p)
				{
					allP.add(p);
				}
			}
		}
//		for(Square s:cube.ss)
//		{
//			a.add(s);
//		}
//		for(Point3D p:cube.pp) {
//			allP.add(p);
//		}
	}
	class PaintSquare{
		Polygon polygon;
		Color color;
		public PaintSquare(Polygon polygon,Color color) {
			this.polygon=polygon;
			this.color=color;
			
		}
		
	}
	boolean wrongPaint=false;
	Color choseColor=Color.white;
	int[]choseRgba= {
			125,125,125,225
	};
	int ChoseColorcnt=0,ChoseColork=1;
	int addColor(int a,int x) {
		if(x>0) {
			a+=x;
			if(a>255) {
				int k=a-255;
				a=255;
				a-=k;
			}
		}else {
			a+=x;
			if(a<0) {
				a=-a;
			}
		}
		return a;
	}
	void randomChangergba(int k) {
		choseRgba[ChoseColorcnt]+=ChoseColork*k;
		choseRgba[ChoseColorcnt]=Math.max(Math.min(255, choseRgba[ChoseColorcnt]), 0);
		if(choseRgba[ChoseColorcnt]==255||choseRgba[ChoseColorcnt]==0) {
			Random ran=new Random();
			ChoseColorcnt=ran.nextInt(3);
			if(choseRgba[ChoseColorcnt]<125)ChoseColork=1;
			else ChoseColork=-1;
		}
	}
	@Override
	public void paintComponent(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, 900, 700);
		if(magicCube.choseSquareId!=-1&&changeStaLock) {
			for(int i=0;i<6;i++)
			{
				Polygon polygon=new Polygon();
				for(int j=0;j<4;j++)
				{
					polygon.addPoint((int)btn[i][j].getX(), (int)btn[i][j].getY());
				}
				g.setColor(magicCube.col[i]);
				g.fillPolygon(polygon);
			}
		}
		
		ArrayList<PaintSquare>paintList=new ArrayList<PaintSquare>();
		if(mouseClicked)magicCube.choseSquareId=-1;
		int listId=-1;
		if(!wrongPaint) {
			PriorityQueue<Square>p=new PriorityQueue<Square>();
			for(Square s:a)p.add(s);
			while(!p.isEmpty())
			{
				Square s=p.poll();
				Polygon polygon=new Polygon();
				Point []p4=new Point[4];
				int k=0;
				for(Point3D point:s.p) {
					Point p2=point.getPoint2D();
					p2.setLocation((int)p2.getX()+Window_Width/2,  (int)p2.getY()+Window_Height/2);
					if(mouseClicked) {
						p4[k++]=p2;
					}
					polygon.addPoint((int)p2.getX(), (int)p2.getY());
				}
				if(mouseClicked) {
					if(PointInSquare(mouseClick, p4)) {
						magicCube.choseSquareId=s.ind;
						listId=paintList.size();
					}
				}else {
					if(magicCube.choseSquareId==s.ind) {
						listId=paintList.size();
					}
				}
				paintList.add(new PaintSquare(polygon, s.color));
			}
		}else {
			for(Square s:a) {
				Polygon polygon=new Polygon();
				Point []p4=new Point[4];
				int k=0;
				for(Point3D point:s.p) {
					Point p2=point.getPoint2D();
					p2.setLocation((int)p2.getX()+Window_Width/2,  (int)p2.getY()+Window_Height/2);
					if(mouseClicked) {
						p4[k++]=p2;
					}
					polygon.addPoint((int)p2.getX(), (int)p2.getY());
				}
				if(mouseClicked) {
					if(PointInSquare(mouseClick, p4)) {
						magicCube.choseSquareId=s.ind;
						listId=paintList.size();
					}
				}else {
					if(magicCube.choseSquareId==s.ind) {
						listId=paintList.size();
					}
				}
				paintList.add(new PaintSquare(polygon, s.color));
			}
		}
		int i=0;
		for(PaintSquare ps:paintList) {
			if(i==listId) {
				Point3D[]pp=magicCube.ss[magicCube.choseSquareId/9][magicCube.choseSquareId%9].outLine();
				Polygon polygon=new Polygon();
				for(Point3D point:pp) {
					Point p2=point.getPoint2D();
					p2.setLocation((int)p2.getX()+Window_Width/2,  (int)p2.getY()+Window_Height/2);
					polygon.addPoint((int)p2.getX(),(int)p2.getY());
				}
				randomChangergba(5);
				g.setColor(new Color(choseRgba[0],choseRgba[1],choseRgba[2],choseRgba[3]));
				g.fillPolygon(polygon);
			}
			i++;
			g.setColor(ps.color);
			g.fillPolygon(ps.polygon);
		}
		if(mouseClicked) {
			mouseClicked=false;
		}
	}
	char[]udlrList;
	int udlrInd=-1;
	public class loop implements Runnable{
		@Override
		public void run() {
			long now;
			while(true) {
				now=System.currentTimeMillis();
				if(now-pre>=timed) {
					if(autoRotate) {
						rotateXY(Math.PI/6,0);
						double p=autoRotateAngle/180*Math.PI;
						p/=fps;
						rotateXY(0, p);
						rotateXY(-Math.PI/6,0);
					}
					if(mouseDragged) {
						angleX=(mouseNow.getY()-mousePre.y)*Math.PI/200;
						angleY=(mouseNow.getX()-mousePre.x)*Math.PI/200;
						rotateXY(-angleX, angleY);
						mousePre=mouseNow;
						mouseDragged=false;
					}
					if(magicCube.page==-1) {
						if(0<=udlrInd&&udlrInd<udlrList.length) {
							int t=1;
							int pre=udlrInd;
							while(++udlrInd<udlrList.length&&udlrList[udlrInd]==udlrList[pre]) {
								t++;
							}
							t%=4;
							if(t==3)t=-1;
							if(t==2) {
								t=1;
								udlrInd--;
							}
							magicCube.rotate(udlrList[pre], t);
							magicCube.animateNextPage();
						}else if(!controlList.isEmpty()){
							udlrList=controlList.poll();
							udlrInd=0;
						}
					}else {
						magicCube.animateNextPage();
					}
					pre=now;
					repaint();
				}
			}
		}
	}
	boolean autoRotate=false;
	double autoRotateAngle=90;
	public class Command{
		String hint;
		String []comm= {};//指令简写
		String command;
		Runnable run;
		public Command(String hint,String command,Runnable run) {
			this.hint=hint;
			this.command=command;
			this.run=run;
		}
		/**
		 * 设置其他指令
		 * @param str
		 * @return
		 */
		public Command setComm(String ...str) {
			comm=str;
			return this;
		}
	}
	int toInt(String str) {
		try {
			int num=Integer.parseInt(str);
			if(num>=0)return num;
		}catch (Exception e) {
		}
		System.out.println("转换数字时出错");
		return -1;
	}
	String toString(String str[]) {
		boolean flag=true;
		String ans="";
		for(String s:str) {
			if(flag)flag=false;
			else ans=ans+" ";
			ans=ans+s;
		}
		return ans;
	}
	String[]commandStr;
	Command[]allCommand= {
		new Command("/help <页数> 显示帮助页","/help",new Runnable() {
			final int limit=5;//最多显示几条指令
			int start=0;
			@Override
			public void run() {
				start=0;
				if(commandStr.length>1) {
					start=toInt(commandStr[1]);
					if(start==-1)return;
					if(start>0) {
						start--;
						start=start*limit;
					}
				}
				System.out.printf("--show help page %d/%d--%n",start/limit+1,allCommand.length/limit+(allCommand.length%limit==0?0:1));
				for(int i=0;i<limit&&i+start<allCommand.length;i++)
				{
					System.out.println(allCommand[i+start].hint+(allCommand[i+start].comm.length==0?(""):(" 简写: "+blockPanel.this.toString(allCommand[i+start].comm))));
				}
			}
		}),
		new Command("/autoRotate [true/false] 开关自动旋转","/autoRotate", new Runnable() {
			@Override
			public void run() {
				if(commandStr.length>1) {
					commandStr[1]=commandStr[1].toLowerCase();
					if(commandStr[1].equals("true")) {
						autoRotate=true;
					}else if(commandStr[1].equals("false")) {
						autoRotate=false;
					}else {
						System.out.println("Wrong word:"+commandStr[1]);
					}
				}else {
					autoRotate^=true;
				}
				System.out.println("change autoRotate to "+autoRotate);
			}
		}).setComm("/ar","/auto"),
		new Command("/rotateAngle <旋转角度> 显示或设置每秒自动旋转角度", "/rotateAngle", new Runnable() {
			@Override
			public void run() {
				if(commandStr.length>1) {
					double angle=0;
					try {
						angle=Double.parseDouble(commandStr[1]);
						autoRotateAngle=angle;
						System.out.println("成功设置每秒旋转角度为:"+autoRotateAngle);
					}catch (Exception e) {
						System.out.println("\""+commandStr[1]+"\"转换失败");
						return;
					}
				}else {
					System.out.println("每秒旋转"+autoRotateAngle+"度");
				}
			}
		}).setComm("/ra"),
		new Command("/wrongPaint [true/false] 切换错误的涂色方式", "/wrongPaint", new Runnable() {
			@Override
			public void run() {
				if(commandStr.length>1) {
					if(commandStr[1].equals("true")) {
						wrongPaint=true;
					}else if(commandStr[1].equals("false")) {
						wrongPaint=false;
					}
				}else {
					wrongPaint^=true;
				}
				System.out.println("wrongPaint:"+wrongPaint);
			}
		}).setComm("/wp"),new Command("/magicCubeRotate [0~5] 顺时针旋转某个面", "/magicCubeRotate", new Runnable() {
			@Override
			public void run() {
				if(commandStr.length>1) {
					String str="";
					for(int i=1;i<commandStr.length;i++) {
						str=str+commandStr[i];
					}
					char[]ch=str.toCharArray();
					addToControlList(ch);
				}else
					System.out.println("缺少必要的传入参数");
			}
		}).setComm("/mcr"),
		new Command("/changeCode \"Srting\" 改变操作序列 UDLRFB", "/changeCode", new Runnable() {
			@Override
			public void run() {
				StringBuffer sb=new StringBuffer("");
				for(int i=1;i<commandStr.length;i++)
				{
					sb.append(commandStr[i]);
				}
				String ans=changeCode(sb.toString());
				System.out.println(ans);
			}
		}).setComm("/cc"),
		new Command("/test [0~5] 测试旋转某个面的动画", "/test", new Runnable() {
			@Override
			public void run() {
				magicCube.rotate(0, 1);
				while(magicCube.page!=-1) {
					magicCube.animateNextPage();
					long pre=System.currentTimeMillis();
					while(System.currentTimeMillis()-pre<10);
					System.out.println("旋转");
				}
			}
		}),new Command("/rotateFrames [2~∞] 显示更改每次旋转所消耗的帧数", "/rotateFrames", new Runnable() {
			@Override
			public void run() {
				if(commandStr.length>1) {
					int k=toInt(commandStr[1]);
					if(k>1) {
						magicCube.changeAllpage(k);
					}else {
						System.out.println("数字范围出现问题");
					}
				}else {
					System.out.println(magicCube.allPage);
				}
			}
		}).setComm("/rf"),
		new Command("/solve (true)降群法+双向bfs解魔方,后输入true则不跳过等待", "/solve", new Runnable() {
			@Override
			public void run() {
				boolean flag=false;
				if(commandStr.length>1) {
					if(commandStr[1].length()>0&&commandStr[1].charAt(0)=='t') {
						flag=true;
					}
				}
				solve(!flag);
			}
		}),new Command("/getState 获取当前魔方情况", "/getState", new Runnable() {
			@Override
			public void run() {
				String[]str=magicCube.getStrState();
				System.out.print("{");
				for(String s:str)
				{
					System.out.print("\"");
					System.out.print(s);
					System.out.print("\",");
				}
				System.out.println("}");
			}
		}),new Command("/rangomRotate [int n]随机顺时针旋转n次默认300次", "/rangomRotate", new Runnable() {
			@Override
			public void run() {
				int k=300;
				if(commandStr.length>1) {
					int x=toInt(commandStr[1]);
					if(x>=1)k=x;
				}
				randomRotate(k);
			}
		}).setComm("/rr"),
		new Command("/autoTest [int n]自动测试n次(打乱，复原) 默认5次", "/autoTest", new Runnable() {
			@Override
			public void run() {
				int k=5;
				if(commandStr.length>1) {
					int x=toInt(commandStr[1]);
					if(x>=1)k=x;
				}
				if(!autoTest)
					autoTest(k);
				else System.out.println("请勿重复调用测试方法");
			}
		}).setComm("/at"),
		new Command("/reverse U1D2L3F2 倒序操作序列", "/reverse", new Runnable() {
			@Override
			public void run() {
				if(commandStr.length>1) {
					String str=reverse(commandStr[1]);
					System.out.println(str);
				}
			}
		}).setComm("/re"),
		new Command("/changeColor x y k 将x面的第y片改成颜色k", "/changeColor", new Runnable() {
			@Override
			public void run() {
				if(commandStr.length>3) {
					try {
						int x=Integer.parseInt(commandStr[1]);
						int y=Integer.parseInt(commandStr[2]);
						int k=Integer.parseInt(commandStr[3]);
						changeColor(x,y,k);
					}catch(Exception e) {
						System.out.println("输入不合法");
					}
				}
			}
		}).setComm("/cco"),
		new Command("/changeColor 开关改变颜色功能 ", "/changeColor", new Runnable() {
			@Override
			public void run() {
				changeStaLock^=true;
				System.out.println(changeStaLock);
			}
		}).setComm("/lock","/col"),
		new Command("/takePhoto 向机器人发送拍照指令", "/tackPhoto", new Runnable() {
			@Override
			public void run() {
				client.send("pic\r\n");
			}
		}).setComm("/pho"),
		new Command("/getAns 向机器人发送获取求解udlr的指令 机器人会开始搜索解法", "/getAns", new Runnable() {
			@Override
			public void run() {
				client.send("ans\r\n");
				
			}
		}).setComm("/ans"),
		new Command("/getMode 获取机器人上次扫描魔方的数据", "/getMode", new Runnable() {
			@Override
			public void run() {
				client.send("mode\r\n");
			}
		}).setComm("/mode"),
		new Command("/robotRotate 根据机器人内部储存的操作序列操作 魔方图像将实时同步", "/robotRotate", new Runnable() {
			@Override
			public void run() {
				client.send("do\r\n");
			}
		}).setComm("/rodo"),
		new Command("/sendSta 用于纠错，向机器人发送当前魔方数据", "/sendSta", new Runnable() {
			@Override
			public void run() {
				StringBuffer sb=new StringBuffer("");
				sb.append("sendSta ");
				sb.append(magicCube.getChar());
				sb.append("\r\n");
				client.send(sb.toString());
			}
		}).setComm("/ssta"),
		new Command("/initColor 重置魔方颜色", "/initColor", new Runnable() {
			@Override
			public void run() {
				magicCube.initColor();
			}
		}).setComm("/initc")
		
	};
	void changeColor(int x,int y,int k) {
		
		if(x>=0&&x<6&&0<=y&&y<9&&0<=k&&k<6) {
			magicCube.changeState(x, y, k);
		}else {
			System.out.println("输入不合法");
		}
	}
	public String reverse(String str) {
		
		char[]ch=str.toCharArray();
		StringBuffer sb=new StringBuffer("");
		for(int i=ch.length-2;i>=0;i--) {
			sb.append(ch[i]);
			sb.append(4-(int)(ch[i+1]-'0'));
		}
		return sb.toString();
	}
	public void waitRotate() {
		
		while(true) {
			int k=0;
			for(int i=0;i<3;i++) {
				long pre=System.currentTimeMillis();
				while(System.currentTimeMillis()-pre<100)System.out.print("");
				if(magicCube.page==-1)
				{
					pre=System.currentTimeMillis();
					while(System.currentTimeMillis()-pre<100)System.out.print("");
					if(controlList.isEmpty())
					{
						pre=System.currentTimeMillis();
						while(System.currentTimeMillis()-pre<100)System.out.print("");
						if(magicCube.page==-1) {
							k++;
						}
					}
				}
			}
			if(k==3)return;
		}
	}
	boolean autoTest=false;
	public void autoTest(int k) {
		class task implements Runnable{
			int k=5;
			public task(int k) {
				this.k=k;
			}
			@Override
			public void run() {
				autoTest=true;
				System.out.println("开始自动测试"+k+"次");
				while(k-->0)
				{
					waitRotate();
					magicCube.changeAllpage(3);
					randomRotate(200);
					waitRotate();
					magicCube.changeAllpage(15);
					solve(true);
				}
				System.out.println("测试完成");
				autoTest=false;
			}
			
		}
		new Thread(new task(k)).start();
		
	}
	public void solve(boolean flag) {
		String[] state=magicCube.getStrState();
		System.out.println("开始求解");
		if(!magicCube.check()) {
			System.out.println("wrong cube");
			return;
		}
		long pre=System.currentTimeMillis();
		String []ans=new Thislethwaite().run(state);
		if(ans==null) {
			return;
		}
		int k=0;
		for(int i=0;i<ans.length;i++) {
			if(ans[i]!=null)
				k+=ans[i].length()/2;
		}
		System.out.println("求解完成，耗时："+(System.currentTimeMillis()-pre)+"ms");
		System.out.println("共花费"+k+"步");
		if(flag) {
			StringBuffer sb=new StringBuffer("");
			for(int i=1;i<5;i++)
			{
				if(ans[i]!=null)
				{
					sb.append(changeCode(ans[i]));
				}
			}
			char[]ch=sb.toString().toCharArray();
			addToControlList(ch);
		}else {
			for(int i=1;i<5;i++)
			{
				if(ans[i]!=null) {
					System.out.println(ans[i]);
					ans[i]=changeCode(ans[i]);
					char[]ch=ans[i].toCharArray();
					addToControlList(ch);
					if(i!=4)
					{
						System.out.println("正在进行第"+i+"次降群,队列将等待,输入任意非空字符以继续");
						in.next();
					}
				}
			}
		}
	}
	public void randomRotate(int n) {
		StringBuffer sb=new StringBuffer("");
		Random ran=new Random();
		for(int i=0;i<n;i++)
		{
			sb.append(ran.nextInt(6));
		}
		System.out.println("随机转动"+n+"次："+sb.toString());
		addToControlList(sb.toString().toCharArray());
	}
	Queue<char[]>controlList=new LinkedList<char[]>();
	void addToControlList(char[]ch) {
		boolean legal=true;
		for(int i=0;i<ch.length;i++) {
			ch[i]-='0';
			if(!(0<=ch[i]&&ch[i]<=5)) {
				legal=false;
				break;
			}
		}
		if(!legal)
		{
			System.out.println("传入参数错误 "+commandStr[1]);
		}else {
			controlList.add(ch);
		}
	}
	public String changeCode(String str){
		boolean[]book=new boolean[200];
		int[]num=new int[200];
		for(int i=0;i<magicCube.udlr.length;i++) {
			book[magicCube.udlr[i]]=true;
			num[magicCube.udlr[i]]=i;
		}
		char[]ch=str.toCharArray();
		StringBuffer sb=new StringBuffer("");
		for(int i=0;i<ch.length;i++)
		{
			if(book[ch[i]]) {
				sb.append(num[ch[i]]);
				if(i+1<ch.length) {
					if(ch[i+1]=='\''||ch[i+1]=='3') {
						sb.append(num[ch[i]]);
						sb.append(num[ch[i]]);
					}else if(ch[i+1]=='2') {
						sb.append(num[ch[i]]);
					}
				}
			}
		}
		return sb.toString();
	}
	public boolean PointInSquare(Point p,Point p4[]) {
		Vector2D a,b,c,d,e,f;
		a=new Vector2D(p4[0], p);
		b=new Vector2D(p4[2], p);
		c=new Vector2D(p4[0], p4[1]);
		d=new Vector2D(p4[0], p4[3]);
		e=new Vector2D(p4[2], p4[1]);
		f=new Vector2D(p4[2], p4[3]);
		long cross=c.crose(d);
		if(cross==0)return false;
		if(c.crose(d)<0) {
			Vector2D tmp=c;
			c=d;
			d=tmp;
			tmp=e;
			e=f;
			f=tmp;
		}
		boolean ans=c.crose(a)>=0&&a.crose(d)>=0&&f.crose(b)>=0&&b.crose(e)>=0;
		return ans;
	}
	public class CommandController implements Runnable{
		HashMap<String,Command>map=new HashMap<String,Command>();
		@Override
		public void run() {
			
			for(Command command:allCommand) {
				map.put(command.command,command);
				for(String com:command.comm) {
					map.put(com,command);
				}
			}
			System.out.println(map.get("/help").hint);
			while(in.hasNext()) {
				commandStr=in.nextLine().split(" +");
				if(map.containsKey(commandStr[0])) {
					map.get(commandStr[0]).run.run();
				}
			}
			
		}
		
	}
	Scanner in=new Scanner(System.in);
	public Point mousePre=new Point(0,0);
	public Point mouseNow=new Point(0,0);
	public Point mouseClick=new Point(0,0);
	boolean mouseDragged=false;
	boolean mousePressed=false;
	boolean mouseClicked=false;
	double angleX,angleY;
	@Override
	public void mouseClicked(MouseEvent mouse) {
		if(mouse.getButton()==1) {
			mouseClick=mouse.getPoint();
			if(magicCube.choseSquareId!=-1&&changeStaLock) {
				for(int i=0;i<6;i++)
				{
					if(PointInSquare(mouseClick, btn[i])) {
						if(magicCube.choseSquareId%9!=4)
							changeColor(magicCube.choseSquareId/9, magicCube.choseSquareId%9, i);
						else System.out.println("你不能改变中心块的颜色  ;)");
						return;
					}
				}
			}
			mouseClicked=true;
		}
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent mouse) {
		mousePre=mouse.getPoint();
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		
	}
	@Override
	public void mouseDragged(MouseEvent mouse) {
		mouseNow=mouse.getPoint();
		mouseDragged=true;
	}
	@Override
	public void mouseMoved(MouseEvent mouse) {
		
	}
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if(magicCube.choseSquareId!=-1) {
			char k=(char) (magicCube.choseSquareId/9+'0');
			if(e.getPreciseWheelRotation()>0) {
				addToControlList(new char[] {k});
			}else {
				addToControlList(new char[] {k,k,k});
			}
		}
	}
	@Override
	public void keyPressed(KeyEvent key) {
		
		
	}
	@Override
	public void keyReleased(KeyEvent key) {
		
	}
	char[][]keyMap=new char[1000000][];
	void initudlrKey() {
		keyMap['1']=new char[] {'0'};
		keyMap['2']=new char[] {'1'};
		keyMap['3']=new char[] {'2'};
		keyMap['4']=new char[] {'3'};
		keyMap['5']=new char[] {'4'};
		keyMap['6']=new char[] {'5'};
		keyMap['!']=keyMap['！']=new char[] {'0','0','0'};
		keyMap['@']=keyMap['@']=new char[] {'1','1','1'};
		keyMap['#']=keyMap['#']=new char[] {'2','2','2'};
		keyMap['$']=keyMap['￥']=new char[] {'3','3','3'};
		keyMap['%']=keyMap['%']=new char[] {'4','4','4'};
		keyMap['^']=new char[] {'5','5','5'};
	}
	@Override
	public void keyTyped(KeyEvent key) {
		if(key.getKeyChar()<keyMap.length&&keyMap[key.getKeyChar()]!=null) {
			addToControlList(keyMap[key.getKeyChar()].clone());
		}
	}
}
