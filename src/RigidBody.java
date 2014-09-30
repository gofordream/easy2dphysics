import java.awt.Color;
import java.awt.Graphics2D;

public class RigidBody{
	//default as a circle
	public Vector position;
	public Vector velocity;
	public Vector angularVelocity;
	public double rotation;
	public double mass;
	public double I;//×ª¶¯¹ßÁ¿
	public Vector F;
	public double r;//radius of circle
	public RigidBody(){
		rotation=0;
		position=new Vector();
		velocity=new Vector(0,0,0);
		angularVelocity=new Vector(0,0,0);
		F=new Vector(0,0,0);
	}
	public void setposition(double x,double y,double z){
		position.x=x;
		position.y=y;
		position.z=z;
	}
	public void setVelocity(double x,double y,double z){
		velocity.x=x;
		velocity.y=y;
		velocity.z=z;
	}
	public void setangularV(double x,double y,double z){
		angularVelocity.x=x;
		angularVelocity.y=y;
		angularVelocity.z=z;
	}
	public void update(double dt){
		position=Game.vecplus(position,Game.vecdotd(velocity, dt));
		rotation+=angularVelocity.z*dt;//don't replace angularVelocity.z with angularVelocity.magnitude	
	}
	public void back(double dt){
		position=Game.vecminus(position,Game.vecdotd(velocity, dt));
		rotation-=angularVelocity.magnitude()*dt;	
	}
	public void draw(Graphics2D g2){
		g2.setColor(Color.WHITE);
		g2.fillOval((int)(position.x-r),(int)(position.y-r), (int)r*2, (int)r*2);
		g2.setColor(Color.BLUE);
		int x=(int)position.x;
		int y=(int)position.y;
		g2.drawLine(x, y, (int)(Math.sin(rotation*Math.PI/180)*r+x),(int)(Math.cos(rotation*Math.PI/180)*r+y));
	}
}