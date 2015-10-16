package com.jeremyfeltracco.core.entities;

import com.badlogic.gdx.math.Vector2;
import com.jeremyfeltracco.core.Textures;

public class Corner extends Entity {
	public Corner(float x, float y) {
		super(Textures.corner);
		setOriginPosition(x, y);
		this.updateSides();
	}

	@Override
	public void update(float delta) {

	}
}
