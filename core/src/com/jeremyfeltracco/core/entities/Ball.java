package com.jeremyfeltracco.core.entities;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.jeremyfeltracco.core.Sim;
import com.jeremyfeltracco.core.Textures;

public class Ball extends Entity {
	private Vector2 pos;
	float radius;
	private Box2DDebugRenderer debugRenderer;
	private float maxVelocity = 1000;
	
	public Ball(float x, float y) {
		super(Textures.ball,new Vector2(500,100));
		setOriginPosition(x, y);
		pos = this.getOriginPosition();
		radius = this.sprite.getWidth()/2;
	}
	
	@Override
	public void update(float delta) {
		if(velocity.len() > maxVelocity){
			velocity.nor().scl(maxVelocity);
			System.out.println("NEW VELOCITY VALUE: " + velocity);
		}
		pos = pos.add(velocity.cpy().scl(delta));
		checkPosition(pos, delta);
		this.setOriginPosition(pos.x, pos.y);
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
		
		for (Entity c : Entity.entities){
			if(c == this){
				continue;
			}
			if(c.getOriginPosition().dst(pos) > c.maxSize + 30)
				continue;
			
			Vector2[] cor = c.getCorners().clone();
			float[] segDists = new float[4];
			int index = -1;
			int altIndex = -1;
			for(int i = 0; i < 4; i++){
				segDists[i]=Intersector.distanceSegmentPoint(cor[i], cor[i+1], pos);
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
			
			double dist = Double.MAX_VALUE; 
			for(int i = 0; i < 4; i++){
				//System.out.print(segDists[i] + "\t");
				if(segDists[i] < dist){
					dist = segDists[i];
					index = i;
				}
			}
			//System.out.println();
			
			boolean contactCorner = false;
			if(contact && contactPoints > 1){
				for(int i = 0; i < 4; i++){
					if(i!=index){
						if(segDists[i] == segDists[index]){
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

			//---------------------------------------------------
			
			
			
			
			if (contact){
				Vector2 corners[] = c.getCorners();
				Vector2 side;
				Vector2 n;
				if(contactCorner){
					System.out.println("----------Corner Collision--------");
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
					System.out.println("----------------------------------\n");
				}else{
				
					
					
					
					
					//Tracing values
					/*for(Vector2 v : corners){
						System.out.print(v + "\t");
					}
					System.out.println("\n");
					//top, right, bottom, left
					String text[] = {"Top","Right","Bottom","Left"};
					for(int i = 0; i< 4; i++){
						distances[i] = Intersector.distanceSegmentPoint(corners[i], corners[i+1], p);
						
						System.out.println(i + "\t" + distances[i] + "\t" + text[i]);
					}
					//System.out.println("\n");
					System.out.println("Min Index: " + index);*/
					
					side = simplify(corners[index+1].cpy().sub(corners[index]).cpy()).nor();
					n = new Vector2(-side.y, side.x).nor();
				}
				
				
					
				Vector2 addVel = simplify(proj(c.getVelocity(),n));
				if(Math.abs(addVel.x)>0 || Math.abs(addVel.y)>0){
					Vector2 compare = simplify(addVel.cpy().nor().scl(-1)).sub(simplify(velocity.cpy().nor()));
					
					System.out.println("------------ACCELERATION----------");
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
					System.out.println("----------NORMAL COLLISION--------");
					Vector2 velocityPrevious = velocity.cpy();
					Vector2 v = simplify(velocity.cpy());	
					velocity = v.cpy().sub(n.cpy().scl(2*v.cpy().dot(n)));
					System.out.println("----------------------------------\n");
				}
				Vector2 u = velocity.cpy().nor();
				//System.out.println(u + "\tSCALE: " + (segDists[index] - radius + velocity.cpy().scl(delta).len()));
				pos = p.cpy().add(u.scl(segDists[index] - radius + velocity.cpy().scl(delta).len() + addVel.scl(delta).len()));
				
				//Adjust position to keep the ball from getting stuck in the shape
				
				
				
			}
		}
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
