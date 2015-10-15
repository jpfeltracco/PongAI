package com.jeremyfeltracco.core.entities;

import com.jeremyfeltracco.core.Textures;

public class Corner extends Entity {
	public Corner(float x, float y) {
		super(Textures.corner);
		setOriginPosition(x, y);
	}

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		
	}
}
