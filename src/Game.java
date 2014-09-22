import java.awt.*;
import javax.swing.*;
class MyPanel extends JPanel{
	RigidBody body1,body2;
	public void paint(Graphics g){
		super.paint(g);
		Graphics2D g2=(Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.GREEN);
		g2.fillRect(100, 100, 400,400);
		g2.setColor(Color.WHITE);
		if(body1!=null)
			g2.fillOval((int)(body1.position.x-body1.r),(int)(body1.position.y-body1.r), (int)body1.r*2, (int)body1.r*2);
		if(body2!=null)
			g2.fillOval((int)(body2.position.x-body2.r),(int)(body2.position.y-body2.r), (int)body2.r*2, (int)body2.r*2);
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
	private static RigidBody body1,body2;
	private static MyFrame myframe;
	private static MyPanel mypanel;
	public static void main(String []args) throws InterruptedException{
		System.out.print("hello world!\n");
		
		body1=new RigidBody();
		body2=new RigidBody();
		body1.setposition(200,300,0);
		body1.setVelocity(5, -6, 0);
		body1.mass=1;
		body1.r=20;
		
		body2.setposition(400,200,0);
		body2.setVelocity(-10, 4, 0);
		body2.mass=1;
		body2.r=20;
		
		myframe=new MyFrame();
		mypanel=new MyPanel();
		mypanel.body1=body1;
		mypanel.body2=body2;
		mypanel.setLayout(null);
		mypanel.setBounds(0, 0, 400,400);
		myframe.add(mypanel);
		
		while(true){
			double dt=1.0;
			stepSimulation(dt);
			myframe.repaint();
			Thread.sleep(50);
		}
	}
	private static void stepSimulation(double dt){
		boolean again=true;
		while(again){
			Vector p1=new Vector(body1.position.x,body1.position.y,body1.position.z);
			Vector p2=new Vector(body2.position.x,body2.position.y,body2.position.z);
			Vector v1=new Vector(body1.velocity.x,body1.velocity.y,body1.velocity.z);
			Vector v2=new Vector(body2.velocity.x,body2.velocity.y,body2.velocity.z);
			Vector w1=new Vector(body1.angularVelocity.x,body1.angularVelocity.y,body1.angularVelocity.z);
			Vector w2=new Vector(body2.angularVelocity.x,body2.angularVelocity.y,body2.angularVelocity.z);
			update(body1,dt);
			update(body2,dt);
			int a=checkforcollision();
			if(a==1){
				applyImpulse();
				again=false;
			}else if(a==2){		
				body1.setposition(p1.x,p1.y,p1.z);
				body2.setposition(p2.x,p2.y,p2.z);
				body1.setVelocity(v1.x,v1.y,v1.z);
				body2.setVelocity(v2.x,v2.y,v2.z);
				body1.setangularV(w1.x,w1.y,w1.z);
				body2.setangularV(w2.x,w2.y,w2.z);
				dt=dt/2;
			}else{
				again=false;
			}
		}
		if(body1.position.x-body1.r<100){
			body1.velocity.x*=-1;
			body1.position.x=100+body1.r;
		}
		if(body1.position.x+body1.r>500){
			body1.velocity.x*=-1;
			body1.position.x=500-body1.r;
		}
		if(body1.position.y-body1.r<100){
			body1.velocity.y*=-1;
			body1.position.y=100+body1.r;
		}
		if(body1.position.y+body1.r>500){
			body1.velocity.y*=-1;
			body1.position.y=500-body1.r;
		}
		if(body2.position.x-body2.r<100){
			body2.velocity.x*=-1;
			body2.position.x=100+body2.r;
		}
		if(body2.position.x+body2.r>500){
			body2.velocity.x*=-1;
			body2.position.x=500-body2.r;
		}
		if(body2.position.y-body2.r<100){
			body2.velocity.y*=-1;
			body2.position.y=100+body2.r;
		}
		if(body2.position.y+body2.r>500){
			body2.velocity.y*=-1;
			body2.position.y=500-body2.r;
		}
	}
	private static void applyImpulse(){
		//only linear impulse
		double j=0;
		double e=0.8;
		Vector vr=MyMath.vecminus(body1.velocity, body2.velocity);
		Vector n=MyMath.vecminus(body1.position, body2.position);
		n.normalize();
		j=-MyMath.vecdotvec(vr, n)*(e+1)/(body1.mass+body2.mass);
		body1.velocity=MyMath.vecplus(body1.velocity,MyMath.vecdotd(n,j/body1.mass));
		body2.velocity=MyMath.vecminus(body2.velocity,MyMath.vecdotd(n,j/body2.mass));
	}
	private static int checkforcollision(){
		//circle to circle
		Vector r=MyMath.vecminus(body1.position, body2.position);
		double len=r.magnitude()-(body1.r+body2.r);
		Vector v=MyMath.vecminus(body1.velocity,body2.velocity);
		double vn=MyMath.vecdotvec(v,r);
		if(len<=3 && len >=-5 && vn<0){		
			//considered as collision
			
			return 1;
		}else if(len<-5){
			//considered as penetrate
			return 2;
		}else{
			//none collision nor penetrate
			return 0;
		}
	}
	private static void update(RigidBody body,double dt){
		body.position=MyMath.vecplus(body.position,MyMath.vecdotd(body.velocity, dt));
		body.rotation+=body.angularVelocity.magnitude()*dt;			
	}
}