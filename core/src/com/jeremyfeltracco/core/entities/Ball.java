package com.jeremyfeltracco.core.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.jeremyfeltracco.core.Position;
import com.jeremyfeltracco.core.Textures;

public class Ball extends Entity {
	Sprite sprite;
	private Vector2 velocity;
	private Vector2 pos;
	
	public Ball() {
		super(Textures.ball);
		setOriginPosition(0, 0);
		pos = this.getPosition();
		
		//Initial test velocity
		velocity = new Vector2(10,10);
	}
	
	@Override
	public void update(float delta) {
		
		// TODO Auto-generated method stub
		
		pos = pos.add(duplicate(velocity).scl(delta));
		System.out.println(velocity.x);
		this.setOriginPosition(pos.x, pos.y);
		
	}
	

}
