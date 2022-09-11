package math3D;

public class Vector3D {
	Point3D a,b;
	Point3D ve;
	public Vector3D(Point3D a,Point3D b) {
		this.a=a;
		this.b=b;
		initVe();
	}
	void initVe(){
		ve=new Point3D(b.x-a.x,b.y-a.y,b.z-a.z);
	}
	Point3D add(Point3D a) {
		return new Point3D(ve.x+a.x,ve.y+a.y,ve.z+a.z);
	}
	double getd() {
		return Math.sqrt(ve.x*ve.x+ve.y*ve.y+ve.z*ve.z);
	}
	public Point3D getVeByLen(double len) {
		double nowLen=getd();
		return new Point3D(ve.x*len/nowLen, ve.y*len/nowLen,ve.z*len/nowLen);
	}
}
