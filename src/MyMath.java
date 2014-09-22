public class MyMath{
	public static Vector vecdotd(Vector v,double d){
		Vector r = new Vector();
		r.x=v.x*d;
		r.y=v.y*d;
		r.z=v.z*d;
		return r;
	}
	public static double vecdotvec(Vector v1,Vector v2){
		double r=0;
		r+=v1.x*v2.x+v1.y*v2.y+v1.z*v2.z;
		return r;
	}
	public static Vector vecproc(Vector a,Vector b){
		Vector c=new Vector();
		c.x=a.y*b.z-a.z*b.y;
		c.y=a.z*b.x-a.x*b.z;
		c.z=a.x*b.y-a.y*b.x;
		return c;
	}
	public static Vector vecplus(Vector v1,Vector v2){
		Vector v=new Vector();
		v.x=v1.x+v2.x;
		v.y=v1.y+v2.y;
		v.z=v1.z+v2.z;
		return v;
	}
	public static Vector vecminus(Vector v1,Vector v2){
		Vector v=new Vector();
		v.x=v1.x-v2.x;
		v.y=v1.y-v2.y;
		v.z=v1.z-v2.z;
		return v;
	}
	
}