public class Vector{
	public double x,y,z;
	public Vector(){
		x=0;
		y=0;
		z=0;
	}
	public Vector(double a,double b,double c){
		x=a;
		y=b;
		z=c;
	}
	public double magnitude(){
		double d=Math.sqrt(x*x+y*y+z*z);
		return d;
	}
	public void normalize(){
		double d=magnitude();
		if(d!=0){
			x=x/d;
			y=y/d;
			z=z/d;
		}
	}
}