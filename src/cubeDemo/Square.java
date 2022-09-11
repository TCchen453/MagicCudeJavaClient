package cubeDemo;

import java.awt.Color;

import math3D.Point3D;
import math3D.Vector3D;

public class Square implements Comparable<Square>{
	Point3D p[]=new Point3D[4];
	double nx,ny,nz;
	Color color;
	int ind;
	public Square(Point3D a,Point3D b,Point3D c,Point3D d,Color color) {
		p[0]=a;
		p[1]=b;
		p[2]=c;
		p[3]=d;
		this.color=color;
	}
	double getMin() {
		double mi=1000000000;
		for(Point3D p:p) {
			mi=Math.min(p.z,mi);
		}
		return mi;
	}
	public String toString() {
		String str=color.toString()+"\n";
		for(Point3D p1:p)
		{
			str=str+p1+"\n";
		}
		return str;
	}
	@Override
	public int compareTo(Square o) {
		return getMin()<o.getMin()?-1:1;
	}
	public Point3D[] outLine() {
		Point3D[]ans=new Point3D[4];
		Point3D mid=p[0].getMid(p[2]);
		for(int i=0;i<4;i++)
		{
			Vector3D ve=new Vector3D(mid,p[i]);
			ans[i]=ve.getVeByLen(7);
			ans[i]=new Point3D(p[i].x+ans[i].x,p[i].y+ans[i].y,p[i].z+ans[i].z);
		}
		return ans;
	}
}
