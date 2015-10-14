package com.jeremyfeltracco.core;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class Ball {
	Sprite sprite;
	public Ball() {
		sprite = new Sprite(Textures.ball);
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
		setOriginPosition(0, 0);
	}
	
	private void setOriginPosition(float x, float y) {
		sprite.setPosition(x - sprite.getOriginX(), y - sprite.getOriginY());
	}
	
	public Sprite getSprite() {
		return sprite;
	}
}
