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
		velocity = new Vector2(300.0f,92.4f);
		radius = this.sprite.getWidth()/2;
	}
	
	@Override
	public void update(float delta) {
		
		// TODO Auto-generated method stub
		
		pos = pos.add(velocity.cpy().scl(delta));
		if(checkPosition(pos)){
			this.setOriginPosition(pos.x, pos.y);
		}
		
		
		for(Paddle p : Sim.pads){
			//System.out.println(p + " : " + overlap(p));
		}
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
		return out;
	}
	
	boolean overlap(Paddle p){

		return false;
	}
	

}
