package com.jeremyfeltracco.core.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.jeremyfeltracco.core.Textures;

public class Ball extends Entity {
//	static int maxVel = 5;
	Sprite sprite;
	Vector2 velocity = new Vector2(0, 0);
	
	public Ball() {
		super(Textures.ball);
		setOriginPosition(0, 0);
//		velocity.x = (MathUtils.random() + 1) * 
	}
	
	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
	}
	

}
