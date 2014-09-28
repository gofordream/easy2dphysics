public class RigidBody{
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
		rotation+=angularVelocity.magnitude()*dt;	
	}
	public void back(double dt){
		position=Game.vecminus(position,Game.vecdotd(velocity, dt));
		rotation-=angularVelocity.magnitude()*dt;	
	}
}