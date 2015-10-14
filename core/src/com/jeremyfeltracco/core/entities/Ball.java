package com.jeremyfeltracco.core.entities;

import com.badlogic.gdx.math.Vector2;
import com.jeremyfeltracco.core.Sim;
import com.jeremyfeltracco.core.Textures;

public class Ball extends Entity {
//	static int maxVel = 5;
	//Sprite sprite;
	private Vector2 pos;
	Vector2 velocity = new Vector2(0, 0);
	float radius;
	
	public Ball() {
		super(Textures.ball);
		setOriginPosition(0, 0);
		pos = this.getOriginPosition();
		
		//Initial test velocity
		velocity = new Vector2(300f,120f);
		radius = this.sprite.getWidth()/2;
	}
	
	@Override
	public void update(float delta) {
		
		// TODO Auto-generated method stub
		
		pos = pos.add(velocity.cpy().scl(delta));
		checkPosition(pos);
		this.setOriginPosition(pos.x, pos.y);
		
		
		for(Paddle p : Sim.pads){
			//System.out.println(p + " : " + overlap(p));
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
		for (Corner c : Sim.corners){
			
			cornerPos = c.getOriginPosition();
			if (this.sprite.getBoundingRectangle().overlaps(c.sprite.getBoundingRectangle())){
				out = false;
				System.out.println("contact");
				switch(getBiggestIndex(p,cornerPos)){
				case 1:
					velocity.x = -Math.abs(velocity.x);
					break;
				case 2:
					velocity.y = Math.abs(velocity.y);
					break;
				case 3:
					velocity.y = -Math.abs(velocity.y);
					break;
				case 0:
					velocity.x = Math.abs(velocity.x);
					break;
				}
			}
		}
		return out;
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
	
	
	

}
