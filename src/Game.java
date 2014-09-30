import java.awt.*;

import javax.swing.*;
class MyPanel extends JPanel{
	RigidBody bodies[];
	public void paint(Graphics g){
		super.paint(g);
		Graphics2D g2=(Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.GREEN);
		g2.fillRect(100, 100, 400,400);
		g2.setColor(Color.WHITE);
	
		if(bodies!=null){
			int i;
			for(i=0;i<12;i++){
				bodies[i].draw(g2);
			}
		}
	}
}
class MyFrame extends JFrame{
	MyFrame(){
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(0, 0, 800,600);
	}
}
public class Game{
	private static MyFrame myframe;
	private static MyPanel mypanel;
	private static RigidBody bodies[];
	private static Contact contacts[];
	public static void main(String []args) throws InterruptedException{
		System.out.print("hello world!\n");
		int i;
		bodies=new RigidBody[12];
		contacts=new Contact[100];
		for(i=0;i<6;i++){
			bodies[i]=new RigidBody();
			bodies[i].setposition(130+i*60, 200+Math.random()*10, 0);
			bodies[i].r=20+Math.random()*7;
			bodies[i].mass=1;
			bodies[i].I=10;
			bodies[i].rotation=Math.random()*360-180;
			bodies[i].setangularV(0, 0, 0);
			bodies[i].setVelocity(Math.random()*6-3, Math.random()*6-3, 0);
		}
		for(i=0;i<6;i++){
			bodies[6+i]=new RigidBody();
			bodies[6+i].setposition(130+i*60, 380+Math.random()*10, 0);
			bodies[6+i].r=20+Math.random()*7;
			bodies[6+i].mass=1;
			bodies[6+i].I=10;
			bodies[6+i].rotation=Math.random()*360-180;
			bodies[6+i].setangularV(0, 0, 0);
			bodies[6+i].setVelocity(Math.random()*6-3, Math.random()*6-3, 0);
		}
		
		myframe=new MyFrame();
		mypanel=new MyPanel();
		mypanel.bodies=bodies;
		mypanel.setLayout(null);
		mypanel.setBounds(0, 0, 400,400);
		myframe.add(mypanel);
		
		while(true){
			double dt=1.0;
			stepSimulation(dt);
			myframe.repaint();
			Thread.sleep(20);//50fps
		}
	}
	private static void stepSimulation(double dt){
		boolean again=true;
		int i;
		while(again){	
			for(i=0;i<12;i++)
				bodies[i].update(dt);
			int a=checkforcollision();
			if(a>0){
				for(i=0;i<a;i++){
					RigidBody body1=contacts[i].body1;
					RigidBody body2=contacts[i].body2;
					applyImpulse(body1,body2);
				}
				
				again=false;
			}else if(a<0){		
				for(i=0;i<12;i++)
					bodies[i].back(dt);
				dt=dt/2;
				System.out.println("penetrate");
			}else{
				again=false;
			}
		}
		for(i=0;i<12;i++){
			RigidBody body=bodies[i];
			if(body.position.x-body.r<100){
				body.velocity.x*=-1;
				body.position.x=100+body.r;
			}
			if(body.position.x+body.r>500){
				body.velocity.x*=-1;
				body.position.x=500-body.r;
			}
			if(body.position.y-body.r<100){
				body.velocity.y*=-1;
				body.position.y=100+body.r;
			}
			if(body.position.y+body.r>500){
				body.velocity.y*=-1;
				body.position.y=500-body.r;
			}
		}
	}
	
	private static void applyImpulse(RigidBody body1,RigidBody body2){
		//normal impulse
		double j=0;
		double e=1.0;
		Vector vr=vecminus(body1.velocity, body2.velocity);
		Vector n=vecminus(body1.position, body2.position);
		n.normalize();
		Vector r1=vecdotd(n,-body1.r);
		Vector r2=vecdotd(n,body2.r);
		j=-vecdot(vr, n)*(e+1)/(body1.mass+body2.mass+vecdot(n,vecdotd(vecproc(vecproc(r1,n),r1),1/body1.I))+vecdot(n,vecdotd(vecproc(vecproc(r2,n),r2),1/body2.I)));
		body1.velocity=vecplus(body1.velocity,vecdotd(n,j/body1.mass));
		body2.velocity=vecminus(body2.velocity,vecdotd(n,j/body2.mass));
		//tangent impulse 
		//t and vr are point to same side of n
		double u=0.01;
		Vector t=vecproc(vecproc(n, vr),n);
		body1.velocity=vecminus(body1.velocity, vecdotd(t,u*j/body1.mass));
		body2.velocity=vecplus(body2.velocity,vecdotd(t,u*j/body2.mass));
		
		Vector v1=vecdotd(vecproc(r1,vecplus(vecdotd(n,j),vecdotd(t,-j*u))),1/body1.I);
		Vector v2=vecdotd(vecproc(r2,vecplus(vecdotd(n,-j),vecdotd(t,j*u))),1/body2.I);
		//System.out.println(v1.z);
		//System.out.println(v2.z);
		body1.angularVelocity=vecplus(body1.angularVelocity,vecdotd(vecproc(r1,vecplus(vecdotd(n,j),vecdotd(t,-j*u))),1/body1.I));
		body2.angularVelocity=vecplus(body2.angularVelocity,vecdotd(vecproc(r2,vecplus(vecdotd(n,-j),vecdotd(t,j*u))),1/body2.I));		
		
	}
	private static int checkforcollision(){
		//circle to circle	
		int i,j;
		int count=0;
		for(i=0;i<11;i++){
			for(j=i+1;j<12;j++){
				RigidBody body1=bodies[i];
				RigidBody body2=bodies[j];
				Vector r=vecminus(body1.position, body2.position);
				double len=r.magnitude()-(body1.r+body2.r);
				Vector v=vecminus(body1.velocity,body2.velocity);
				double vn=vecdot(v,r);
				if(len<=0 && len >=-5 && vn<0){		
					//considered as collision
					Contact c=new Contact(body1,body2);
					contacts[count++]=c;					
				}else if(len<-5){
					//considered as penetrate
					return -1;
				}
			}
		}
		return count;
		
	}

	public static Vector vecdotd(Vector v,double d){
		Vector r = new Vector();
		r.x=v.x*d;
		r.y=v.y*d;
		r.z=v.z*d;
		return r;
	}
	public static double vecdot(Vector v1,Vector v2){
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