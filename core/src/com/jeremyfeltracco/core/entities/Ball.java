package com.jeremyfeltracco.core.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.jeremyfeltracco.core.Textures;

public class Ball extends Entity {
	Sprite sprite;
	public Ball() {
		super(Textures.ball);
		setOriginPosition(0, 0);
	}
	
	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
	}
	

}
