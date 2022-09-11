package math3D;

import java.awt.Point;

public class Vector2D {
	Point a,b;
	Point ve;
	public Vector2D(Point a,Point b)
	{
		this.a=a;
		this.b=b;
		initVe();
	}
	void initVe(){
		ve=new Point(b.x-a.x,b.y-a.y);
	}
	public long crose(Vector2D y) {
		return ve.x*1l*y.ve.y-ve.y*1l*y.ve.x;
	}
}
