package cubeDemo;

import java.awt.Color;

import math3D.Point3D;

public class cube {
	double cube_len=30;
	Square ss[]=new Square[6];
	Point3D[]pp=new Point3D[8];
	void loadSquare() {
		ss[0]=new Square(pp[0],pp[1],pp[2],pp[3],Color.yellow);
		ss[1]=new Square(pp[3],pp[2],pp[6],pp[7],Color.green);
		ss[2]=new Square(pp[0],pp[3],pp[7],pp[4],Color.orange);
		ss[3]=new Square(pp[0],pp[4],pp[5],pp[1],Color.blue);
		ss[4]=new Square(pp[1],pp[5],pp[6],pp[2],Color.red);
		ss[5]=new Square(pp[4],pp[7],pp[6],pp[5],Color.white);
	}
	void loadPoint() {
		pp[0]=new Point3D(-cube_len,-cube_len,-cube_len-200);
		pp[1]=new Point3D(-cube_len,-cube_len,cube_len-200);
		pp[2]=new Point3D(cube_len,-cube_len,cube_len-200);
		pp[3]=new Point3D(cube_len,-cube_len,-cube_len-200);
		pp[4]=new Point3D(-cube_len,cube_len,-cube_len-200);
		pp[5]=new Point3D(-cube_len,cube_len,cube_len-200);
		pp[6]=new Point3D(cube_len,cube_len,cube_len-200);
		pp[7]=new Point3D(cube_len,cube_len,-cube_len-200);
	}
	void init() {
		loadPoint();
		loadSquare();
	}
	public cube() {
		init();
	}
	public cube(double cube_len) {
		this.cube_len=cube_len;
		init();
	}
	void rotateXY(double angleX,double angleY) {
		for(Point3D p:pp) {
			p.rotateXY(angleX, angleY);
		}
	}
}
