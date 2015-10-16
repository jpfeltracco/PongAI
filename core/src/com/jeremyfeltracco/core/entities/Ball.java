package com.jeremyfeltracco.core.entities;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.jeremyfeltracco.core.Sim;
import com.jeremyfeltracco.core.Textures;

public class Ball extends Entity {
	private Vector2 pos;
	float radius;
	private float maxVelocity = 1000;
	private int lastIDContact = -1;
	private boolean error = false;
	private String errorString;
	
	
	/*ERRORS:
	 * 1: super(Textures.ball,new Vector2(250,-72));
	 * 2: super(Textures.ball,new Vector2(200,-72));
	 * 3: super(Textures.ball,new Vector2(300,-177)); Weird Error
	 */
	
	public Ball(float x, float y) {
		super(Textures.ball,new Vector2(-300,-177f));//(73.1226f,244.36066f));
		setOriginPosition(x, y);
		pos = this.getOriginPosition();
		radius = this.sprite.getWidth()/2;
	}
	
	@Override
	public void update(float delta) {
		if(velocity.len() > maxVelocity){
			velocity.nor().scl(maxVelocity);
			System.out.println("NEW VELOCITY VALUE: " + velocity + " :" + velocity.cpy().len() +"\tTime: " + Sim.systemTime + "\n");
		}
		pos = pos.add(velocity.cpy().scl(delta));
		checkPosition(pos.cpy(), delta);
		if(handleErrors()){
			this.setOriginPosition(pos.x, pos.y);
		}
	}
	
	public void checkPosition(Vector2 p, float delta){

		if(p.x + radius > Sim.maxX){
			System.out.println("--Right Wall--");
			velocity.x = -Math.abs(velocity.x);
			lastIDContact = -1;
			Sim.setSideHit(Side.RIGHT);
		}
		if(p.x - radius < -Sim.maxX){
			System.out.println("--Left Wall--");
			velocity.x = Math.abs(velocity.x);
			lastIDContact = -1;
			Sim.setSideHit(Side.LEFT);
		}
		if(p.y + radius > Sim.maxY){
			System.out.println("--Top Wall--");
			velocity.y = -Math.abs(velocity.y);
			lastIDContact = -1;
			Sim.setSideHit(Side.UP);
		} 
		if(p.y - radius < -Sim.maxY){
			System.out.println("--Bottom Wall--");
			velocity.y = Math.abs(velocity.y);
			lastIDContact = -1;
			Sim.setSideHit(Side.DOWN);
		}
		int delay = 0;
		
		
		Entity c = this;
		Entity temp;
		int contactCount = 0;
		for (Entity cor : Entity.entities){
			if(cor == this) //CHECK IF POSSIBLE TO ACTUALLY HIT THIS OBJECT
				continue;
			Vector2[] corners = cor.getCorners().clone();
			for(int i = 0; i < 4; i++){
				cor.segDists[i]=Intersector.distanceSegmentPoint(corners[i], corners[i+1], p);
			}
			
			cor.contactPoints = 0;
			cor.contact = false;
			for(int i = 0; i < 4; i++){
				if(cor.segDists[i] <= radius){
					cor.contactPoints++;
					cor.contact = true;
				}
			}
			if(cor.contact){
				contactCount++;
				c = cor;
			}
		}
		
		if(contactCount > 2){
			error = true;
			errorString = "Collision with too many objects (>2)";
			return;
		}
		if(contactCount == 2){
			System.out.println("Contact with mutiple objects");
			Entity mergeObj[] = new Entity[2];
			int i = 0;
			for(Entity cor : Entity.entities){
				if(cor.contact == true){
					mergeObj[i] = cor;
					i++;
				}
				if(i==2)
					break;
			}
			
			Vector2[] e0 = mergeObj[0].getCorners().clone();
			Vector2[] e1 = mergeObj[1].getCorners().clone();
			
			for(i = 0; i < 4; i++){
				for(int j = 0; j < 4; j++){
					if(e1[j] == null)
						continue;
					if(Math.abs(e1[j].x - e0[i].x) < 0.001 && Math.abs(e1[j].y - e0[i].y) < 0.001){
						e1[j] = null;
						e0[i] = null;
						break;
					}
				}
			}
			
			Vector2[] newShape = new Vector2[5];
			int j = 0;
			for(i = 0; i < 4; i++){
				if(e0[i] != null){
					newShape[j++] = e0[i];
				}
			}
			for(i = 0; i < 4; i++){
				if(e1[i] != null){
					newShape[j++] = e1[i];
				}
			}
			newShape[4] = newShape[0].cpy();
			
			System.out.println("NEW:");
			for(Vector2 cor : newShape){
				System.out.print(cor + "\t");
			}
			System.out.println("\n");
			
			System.out.println("OLD:");
			for(Vector2 cor : mergeObj[0].getCorners()){
				System.out.print(cor + "\t");
			}
			System.out.println();
			for(Vector2 cor : mergeObj[1].getCorners()){
				System.out.print(cor + "\t");
			}
			System.out.println("\n");
			
			temp = new VirtualShape(newShape);
			c = temp;
			
			Vector2[] corners = temp.getCorners();
			for(i = 0; i < 4; i++){
				temp.segDists[i]=Intersector.distanceSegmentPoint(corners[i], corners[i+1], p);
			}
			
		}
				
			
		
		
		
		
		//for (Entity c : Entity.entities){
		if(c != this){
			
			//if(c == this)// || c.getOriginPosition().dst(pos) > c.maxSize + velocity.cpy().scl(delta).len() * 2)
				//return;
			/*double dist = c.getOriginPosition().dst(p);
			double maxAdj = c.maxSize + 40;
			int delayAmt;
			delayAmt = (int)(maxAdj / dist * 35);
			if(delayAmt > delay){
				delay = delayAmt;
			}*/
			Vector2[] corners = c.getCorners().clone();
			
			int index = -1;
			int altIndex = -1;
			
			//----------------------CONTACT----------------------
			
			
			
			
			double dist = Double.MAX_VALUE; 
			for(int i = 0; i < 4; i++){
				//System.out.print(c.segDists[i] + "\t");
				if(c.segDists[i] < dist){
					dist = c.segDists[i];
					index = i;
				}
			}
			
			boolean contactCorner = false;
			if(c.contact && c.contactPoints > 1){
				for(int i = 0; i < 4; i++){
					if(i!=index){
						if(Math.abs(c.segDists[i] - c.segDists[index]) < 0.01){
							contactCorner = true;
							altIndex = i;
						}
					}
				}
			}

			float[] vertices = new float[8];
			for(int i = 0; i < 4; i++){
				vertices[i*2] = corners[i].x;
				vertices[i*2+1] = corners[i].y;
			}
			
			com.badlogic.gdx.math.Polygon poly = new com.badlogic.gdx.math.Polygon(vertices);
			if(poly.contains(p.x, p.y)){
				error = true;
				errorString = "The ball has been detected inside another object. (ID: " + c.id + ")";
			}
			
			//---------------------------------------------------
			
			//if (c.contact){
				if(lastIDContact == c.id && lastIDContact != -1){
					error = true;
					errorString = "The ball hit the same object (ID: " + c.id + ") twice.";
				}
				lastIDContact = c.id;
				Vector2 side;
				Vector2 n;
				if(contactCorner){
					System.out.println("----------Corner Collision--------  ID: " + c.id + "\tTime: " + Sim.systemTime);
					for(int i = 0; i< 4; i++){
						if(i == index || i == altIndex){
							System.out.println("\t[" + i + "]\t" + c.segDists[i] + "\tPixels Away\t" + corners[i] + "," + corners[i+1] + "\t:" + corners[i+1].cpy().sub(corners[i].cpy()));
						}else{
							System.out.println("\t " + i + "\t" + c.segDists[i] + "\tPixels Away");
						}
					}
					Vector2 side1 = simplify(corners[index+1].cpy().sub(corners[index]).cpy());
					Vector2 side2 = simplify(corners[altIndex+1].cpy().sub(corners[altIndex]).cpy());
					Vector2 point;
					side = side1.nor().add(side2.nor());
					
					if(corners[index+1]==corners[altIndex]){
						point = corners[altIndex].cpy();
					}else{
						point = corners[index].cpy();
					}
					n = p.cpy().sub(point).nor();
					System.out.println("Calculated Corner Point: " + point);
					System.out.println("Ball Pos: " + p + "\tC Pos: " + c.getOriginPosition() + "\tNormal: " + n);
					
					
				}else{
					System.out.println("-----------Side Collision---------  ID: " + c.id + "\tTime: " + Sim.systemTime);
					for(int i = 0; i< 4; i++){
						if(i == index){
							System.out.println("\t[" + i + "]\t" + c.segDists[i] + "\tPixels Away\t" + corners[i] + "," + corners[i+1] + "\t:" + corners[i+1].cpy().sub(corners[i].cpy()));
						}else{
							System.out.println("\t " + i + "\t" + c.segDists[i] + "\tPixels Away");
						}
					}
					
					side = simplify(corners[index+1].cpy().sub(corners[index]).cpy());
					System.out.println("Calculated Side: " + side + "\tCorners: " + corners[index] + "  " + corners[index+1]);
					side = side.nor();
					n = new Vector2(-side.y, side.x).nor();
					System.out.println("Ball Pos: " + p + "\tC Pos: " + c.getOriginPosition() + "\tNormal: " + n);
				}
				
				
					
				Vector2 addVel = simplify(proj(c.getVelocity(),n));
				if(!(Math.abs(addVel.x)<0.001) || !(Math.abs(addVel.y)<0.001)){
					System.out.println("------------Acceleration----------");
					
					System.out.print("Init Vel: " + velocity);
					Vector2 v = simplify(velocity.cpy());	
					velocity = v.cpy().sub(n.cpy().scl(2*v.cpy().dot(n)));
					System.out.print("\tCol Vel: " + velocity);
					velocity.add(c.getVelocity());//.cpy().scl(1.1f));
					
					System.out.println("\tC Vel: " + c.getVelocity()/*.cpy().scl(1.1f)*/ + "\tFinal Vel: " + velocity);
					
					
					/*Vector2 compare = simplify(addVel.cpy().nor().scl(-1)).sub(simplify(velocity.cpy().nor()));
					System.out.println("------------Acceleration----------");
					System.out.println("Normal Plane: " + n);
					if(Math.abs(compare.x) < 1 && Math.abs(compare.y) < 1){
						//----------TOWARDS----------
						System.out.println("Towards contact detected:\nVel: " + velocity + "\t\tOtherV: " + c.getVelocity());
						System.out.println("Old Velocity: " + velocity.len() + "\t\tAdd Velocity: " + addVel);
						Vector2 v = simplify(velocity.cpy());	
						velocity = v.cpy().sub(n.cpy().scl(2*v.cpy().dot(n)));
						velocity.add(addVel);
						System.out.println("New Velocity: " + velocity.len() + "\t\tVector Form: " + velocity);
					}else{
						//------------AWAY-----------
						System.out.println("Away contact detected:\nVel: " + velocity + "\t\tOtherV: " + c.getVelocity());
						System.out.println("Old Velocity: " + velocity.len() + "\t\tAdd Velocity: " + addVel);
						velocity.add(addVel);
						System.out.println("New Velocity: " + velocity.len() + "\t\tVector Form: " + velocity);
					}*/
					
				}else{
					System.out.println("---------Collision Physics--------");
					System.out.println("Old Velocity: " + velocity + "\tNormal: " + n);
					Vector2 v = simplify(velocity.cpy());	
					velocity = v.cpy().sub(n.cpy().scl(2*v.cpy().dot(n)));
					System.out.println("New Velocity: " + velocity);
					
				}
				Vector2 u = velocity.cpy().nor();
				//System.out.println(u + "\tSCALE: " + (c.segDists[index] - radius + velocity.cpy().scl(delta).len()));
				pos = p.cpy().add(u.scl(c.segDists[index] - radius + velocity.cpy().scl(delta).len() + addVel.scl(delta).len()));
				System.out.println("New Pos: " + pos);
				System.out.println("----------------------------------\n");
				//Adjust position to keep the ball from getting stuck in the shape
				
				
				
			//}
		}
		Sim.setTimeDelay(delay);
	}
	
	private boolean handleErrors(){
		if(error){
			Sim.enable = false;
			System.out.println("\n-----ERROR-----\n"+errorString+"\n---------------");
		}
		return !error;
	}
	
	private Vector2 simplify(Vector2 v){
		return new Vector2(((Math.abs(v.x)<0.0001)?0:v.x),((Math.abs(v.y)<0.0001)?0:v.y));
	}
	
	private Vector2 proj(Vector2 ina, Vector2 inb){
		Vector2 a = ina.cpy();
		Vector2 b = inb.cpy();
		return b.nor().scl(b.dot(a)/b.len());
	}

}
