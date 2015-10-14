package com.jeremyfeltracco.core.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.jeremyfeltracco.core.Position;
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
		velocity = new Vector2(100,60);
		radius = this.sprite.getWidth()/2;
	}
	
	@Override
	public void update(float delta) {
		
		// TODO Auto-generated method stub
		
		pos = pos.add(duplicate(velocity).scl(delta));
		//System.out.println(velocity.x);
		this.setOriginPosition(pos.x, pos.y);
		checkPosition();
	}
	
	public void checkPosition(){
		Vector2 pos = this.getOriginPosition();
		
		if(pos.x + radius >= Sim.maxX){
			System.out.println("PAST RIGHT EDGE");
			velocity.x = -velocity.x;
		}
		if(pos.x - radius <= -Sim.maxX){
			System.out.println("PAST LEFT EDGE");
			velocity.x = -velocity.x;
		}
		if(pos.y + radius >= Sim.maxY){
			System.out.println("PAST TOP EDGE");
			velocity.y = -velocity.y;
		} 
		if(pos.y - radius <= -Sim.maxY){
			System.out.println("PAST BOTTOM EDGE");
			velocity.y = -velocity.y;
		}
	}
	

}
