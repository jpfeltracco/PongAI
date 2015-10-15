package com.jeremyfeltracco.core.entities;

import java.awt.Polygon;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.jeremyfeltracco.core.Sim;
import com.jeremyfeltracco.core.Textures;

public class Ball extends Entity {
	private Vector2 pos;
	float radius;
	private Box2DDebugRenderer debugRenderer;
	private float maxVelocity = 350;
	private int lastIDContact = -1;
	private boolean error = false;
	private String errorString;
	
	public Ball(float x, float y) {
		super(Textures.ball,new Vector2(250,70));
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
		checkPosition(pos, delta);
		if(handleErrors()){
			this.setOriginPosition(pos.x, pos.y);
		}
	}
	
	public void checkPosition(Vector2 p, float delta){

		if(p.x + radius > Sim.maxX){
			//System.out.println("PAST RIGHT EDGE");
			velocity.x = -Math.abs(velocity.x);
		}
		if(p.x - radius < -Sim.maxX){
			//System.out.println("PAST LEFT EDGE");
			velocity.x = Math.abs(velocity.x);
		}
		if(p.y + radius > Sim.maxY){
			//System.out.println("PAST TOP EDGE");
			velocity.y = -Math.abs(velocity.y);
		} 
		if(p.y - radius < -Sim.maxY){
			//System.out.println("PAST BOTTOM EDGE");
			velocity.y = Math.abs(velocity.y);
		}
		int delay = 0;
		for (Entity c : Entity.entities){
			
			if(c == this || c.getOriginPosition().dst(pos) > c.maxSize + velocity.cpy().scl(delta).len() * 2)
				continue;
			double dist = c.getOriginPosition().dst(pos);
			double maxAdj = c.maxSize + 40;
			int delayAmt;
			delayAmt = (int)(maxAdj / dist * 35);
			if(delayAmt > delay){
				delay = delayAmt;
			}
			Vector2[] corners = c.getCorners().clone();
			float[] segDists = new float[4];
			int index = -1;
			int altIndex = -1;
			for(int i = 0; i < 4; i++){
				segDists[i]=Intersector.distanceSegmentPoint(corners[i], corners[i+1], pos);
			}
			
			//----------------------CONTACT----------------------
			boolean contact = false;
			
			int contactPoints = 0;
			for(int i = 0; i < 4; i++){
				if(segDists[i] <= radius){
					contactPoints++;
					contact = true;
				}
			}
			
			dist = Double.MAX_VALUE; 
			for(int i = 0; i < 4; i++){
				//System.out.print(segDists[i] + "\t");
				if(segDists[i] < dist){
					dist = segDists[i];
					index = i;
				}
			}
			
			boolean contactCorner = false;
			if(contact && contactPoints > 1){
				for(int i = 0; i < 4; i++){
					if(i!=index){
						if(Math.abs(segDists[i] - segDists[index]) < 0.01){
							contactCorner = true;
							altIndex = i;
						}
					}
				}
			}
			
			if(contactCorner){
				for(float f : segDists){
					//System.out.println(f);
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
			
			
			
			if (contact){
				if(lastIDContact == c.id){
					error = true;
					errorString = "The ball hit the same object (ID: " + c.id + ") twice.";
				}
				lastIDContact = c.id;
				Vector2 side;
				Vector2 n;
				if(contactCorner){
					System.out.println("----------Corner Collision--------  ID: " + c.id + "\tTime: " + Sim.systemTime);
					String text[] = {"Top","Right","Bottom","Left"};
					for(int i = 0; i< 4; i++){
						if(i == index || i == altIndex){
							System.out.println("\t[" + i + "]\t" + segDists[i] + "\tPixels Away\t" + corners[i] + "," + corners[i+1] + "\t:" + corners[i+1].cpy().sub(corners[i].cpy()));
						}else{
							System.out.println("\t " + i + "\t" + segDists[i] + "\tPixels Away");
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
					System.out.println("Ball Pos: " + p + "\tNormal: " + n);
					
					
				}else{
					System.out.println("-----------Side Collision---------  ID: " + c.id + "\tTime: " + Sim.systemTime);
					String text[] = {"Top","Right","Bottom","Left"};
					for(int i = 0; i< 4; i++){
						if(i == index){
							System.out.println("\t[" + i + "]\t" + segDists[i] + "\tPixels Away");
						}else{
							System.out.println("\t " + i + "\t" + segDists[i] + "\tPixels Away");
						}
					}
					
					side = simplify(corners[index+1].cpy().sub(corners[index]).cpy());
					System.out.println("Calculated Side: " + side + "\tCorners: " + corners[index] + "  " + corners[index+1]);
					side = side.nor();
					n = new Vector2(-side.y, side.x).nor();
					System.out.println("Ball Pos: " + p +"\tNormal: " + n);
				}
				
				
					
				Vector2 addVel = simplify(proj(c.getVelocity(),n));
				if(Math.abs(addVel.x)>0 || Math.abs(addVel.y)>0){
					Vector2 compare = simplify(addVel.cpy().nor().scl(-1)).sub(simplify(velocity.cpy().nor()));
					
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
					}
					
					System.out.println("----------------------------------\n");
					
				}else{
					System.out.println("---------Collision Physics--------");
					System.out.println("Old Velocity: " + velocity + "\tNormal: " + n);
					Vector2 v = simplify(velocity.cpy());	
					velocity = v.cpy().sub(n.cpy().scl(2*v.cpy().dot(n)));
					System.out.println("New Velocity: " + velocity);
					System.out.println("----------------------------------\n");
				}
				Vector2 u = velocity.cpy().nor();
				//System.out.println(u + "\tSCALE: " + (segDists[index] - radius + velocity.cpy().scl(delta).len()));
				pos = p.cpy().add(u.scl(segDists[index] - radius + velocity.cpy().scl(delta).len() + addVel.scl(delta).len()));
				
				//Adjust position to keep the ball from getting stuck in the shape
				
				
				
			}
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
