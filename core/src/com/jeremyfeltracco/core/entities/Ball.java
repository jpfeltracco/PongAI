package com.jeremyfeltracco.core.entities;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.jeremyfeltracco.core.Sim;
import com.jeremyfeltracco.core.Textures;

public class Ball extends Entity {
	private Vector2 pos;
	Vector2 velocity = new Vector2(0, 0);
	float radius;
	
	public Ball() {
		super(Textures.ball);
		setOriginPosition(200, 0);
		pos = this.getOriginPosition();
		
		//Initial test velocity
		velocity = new Vector2(200,177);
		radius = this.sprite.getWidth()/2;
	}
	
	@Override
	public void update(float delta) {
		pos = pos.add(velocity.cpy().scl(delta));
		if(checkPosition(pos)){
			this.setOriginPosition(pos.x, pos.y);
		}
	}
	
	public Vector2 getVelocity() {
		return velocity;
	}
	
	public boolean checkPosition(Vector2 p){

		boolean out = true;
		if(p.x + radius > Sim.maxX){
			//System.out.println("PAST RIGHT EDGE");
			velocity.x = -Math.abs(velocity.x);
			out = false;
		}
		if(p.x - radius < -Sim.maxX){
			//System.out.println("PAST LEFT EDGE");
			velocity.x = Math.abs(velocity.x);
			out = false;
		}
		if(p.y + radius > Sim.maxY){
			//System.out.println("PAST TOP EDGE");
			velocity.y = -Math.abs(velocity.y);
			out = false;
		} 
		if(p.y - radius < -Sim.maxY){
			//System.out.println("PAST BOTTOM EDGE");
			velocity.y = Math.abs(velocity.y);
			out = false;
		}
		
		Vector2 cornerPos;
		for (Entity c : Entity.entities){
			if(c == this){
				continue;
			}
			cornerPos = c.getOriginPosition();
			if (this.overlaps(c,p)){
				out = true;
				System.out.println("contact");
				float distances[] = new float[4];
				Vector2 corners[] = c.getCorners();
				
				int index = -1;
				double dist = Double.MAX_VALUE; 
				for(int i = 0; i < 4; i++){
					if(distances[i] < dist){
						dist = distances[i];
						index = i;
					}
				}
				
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
				
				Vector2 side = simplify(corners[index+1].cpy().sub(corners[index]).cpy());
				Vector2 n = new Vector2(-side.y, side.x).nor();
				Vector2 v = simplify(velocity.cpy());				
				velocity = v.cpy().sub(n.cpy().scl(2*v.cpy().dot(n)));
				
				//System.out.println("V: " + v + "\tN: " + n + "\nRESULT: " + result);
				//System.out.println("VEL: " + velocity);
				
				moveToEdge(p, velocity);
				
			}
		}
		return out;
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
	
	private void moveToEdge(Vector2 p, Vector2 v){
		
	}
	
	private boolean overlaps(Entity c, Vector2 p){
		if(c.getOriginPosition().dst(p) > c.maxSize + 5)
			return false;
		Vector2[] cor = c.getCorners().clone();
		for(int i = 0; i < 4; i++){
			if(Intersector.distanceSegmentPoint(cor[i], cor[i+1], p) <= radius)
				return true;
		}
		return false;
	}

}
