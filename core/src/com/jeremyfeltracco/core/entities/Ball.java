package com.jeremyfeltracco.core.entities;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.jeremyfeltracco.core.Sim;
import com.jeremyfeltracco.core.Textures;

public class Ball extends Entity {
	private Vector2 pos;
	Vector2 velocity = new Vector2(5, 5);
	float radius;
	private Box2DDebugRenderer debugRenderer;
	
	public Ball(float x, float y) {
		super(Textures.ball,x,y);
		setOriginPosition(x, y);
		pos = this.getOriginPosition();
		radius = this.sprite.getWidth()/2;
	}
	
	@Override
	public void update(float delta) {
		pos = pos.add(velocity.cpy().scl(delta));
		checkPosition(pos, delta);
		this.setOriginPosition(pos.x, pos.y);
	}
	
	public Vector2 getVelocity() {
		return velocity;
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
		
		Vector2 cornerPos;
		for (Entity c : Entity.entities){
			if(c == this){
				continue;
			}
			cornerPos = c.getOriginPosition();
			
			if(c.getOriginPosition().dst(pos) > c.maxSize + 5)
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
				if(segDists[i] < dist){
					dist = segDists[i];
					index = i;
				}
			}
			
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
					System.out.println(f);
				}
			}

			//---------------------------------------------------
			
			
			
			
			if (contact){
				System.out.println("contact");
				Vector2 corners[] = c.getCorners();
				Vector2 side;
				if(contactCorner){
					System.out.println("----------------CORNER------------------");
					Vector2 side1 = simplify(corners[index+1].cpy().sub(corners[index]).cpy());
					Vector2 side2 = simplify(corners[altIndex+1].cpy().sub(corners[altIndex]).cpy());
					
					side = side1.nor().add(side2.nor());
					
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
					
				}
				
				Vector2 n = new Vector2(-side.y, side.x).nor();
				Vector2 v = simplify(velocity.cpy());				
				velocity = v.cpy().sub(n.cpy().scl(2*v.cpy().dot(n)));
				
				//System.out.println("V: " + v + "\tN: " + n + "\nRESULT: " + result);
				//System.out.println("VEL: " + velocity);
				
				Vector2 u = velocity.cpy().nor();
				//System.out.println(velocity.cpy().scl(delta).len());
				p = p.cpy().add(u.scl(segDists[index] - radius + velocity.cpy().scl(delta).len()));
				
				//Adjust position to keep the ball from getting stuck in the shape
				
				
				
			}
		}
	}
	
	private Vector2 simplify(Vector2 v){
		return new Vector2(((Math.abs(v.x)<0.0001)?0:v.x),((Math.abs(v.y)<0.0001)?0:v.y));
	}
	
	private int getBiggestIndex(Vector2 a, Vector2 b){
		double doubles[] = {a.x-b.x, b.x-a.x, a.y-b.y, b.y-a.y};
		double size = -1;
		int index = -1;
		for(int i = 0; i < 4; i++){
			if (Math.abs(doubles[i]) > size) {
				size = doubles[i];
				index = i;
			}
		}
		System.out.println(index);
		return index;
	}

	
	/*private boolean contactCorner(Entity c, float[] segs){
		for (int i=0; i<4; i++){
	        for (int j=i+1; j<4; j++){
	            if (Math.abs(segs[i]-segs[j])<0.0001f){
	                return true;
	            }
	        }
	    }
		return false;
	}*/
	
	private Vector2 overlapsCorner(Entity c, Vector2 p){
		//if(!contact(c))
			//return null;
		
		Vector2[] cor = c.getCorners().clone();
		for(int i = 0; i < 4; i++){
			Vector2 edge = new Vector2(cor[i+1].cpy().sub(cor[i])).nor();
			
			Vector2 intersectPoint = proj(p,edge);
			
			return intersectPoint;
		}
		
		return null;
	}
	
	private Vector2 proj(Vector2 ina, Vector2 inb){
		Vector2 a = ina.cpy();
		Vector2 b = inb.cpy();
		
		return b.nor().scl(b.dot(a)/b.len());
	}

}
