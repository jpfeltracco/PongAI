package com.jeremyfeltracco.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Paddle extends Sprite {
	private Vector2 velocity;
	private Position pos;
	
	public Paddle(Position pos) {
		this.setTexture(Textures.paddle);
		this.pos = pos;
		velocity = new Vector2(0, 0);
		
		int scr_width = Gdx.graphics.getWidth();
		int scr_height = Gdx.graphics.getHeight();
		
		// Assumes paddle is landscape
		this.setBounds(0, 0, getTexture().getWidth(), getTexture().getHeight());
		
		float height = this.getHeight();
		float width = this.getWidth();
		this.setOrigin(width / 2, height / 2);
		
		switch(pos) {
		case TOP:
			this.setPosition(scr_width / 2 - getOriginX(), scr_height - 2 * getOriginY());
			break;
		case BOTTOM:
			this.setPosition(scr_width / 2 - getOriginX(), 0);
			break;
		case LEFT:
			this.rotate(90);
			this.setPosition(height / 2 - getOriginX(), scr_height / 2 - getOriginY());
			break;
		case RIGHT:
			this.rotate(90);
			this.setPosition(scr_width - (getOriginY() + getOriginX()), scr_height / 2 - getOriginY());
			break;
		}
	}
	
	public void update(float delta) {
		// Get ball position and velocity vector, give to controller
		// Controller outputs velocities
		
		// Assume vel = controller output
		
		Vector2 velocity = new Vector2(0, 0);
		float vel = 4.1820f;
		
		vel = MathUtils.clamp(vel, -5, 5);
		
		switch(pos) {
		
		case TOP:
			velocity.x = -vel;
			break;
		case BOTTOM:
			velocity.x = vel;
			break;
		case LEFT:
			velocity.y = -vel;
			break;
		case RIGHT:
			velocity.y = vel;
			break;
		}
		
		
		Vector2 pos = new Vector2(getX(), getY());
		pos = pos.add(velocity.scl(delta));
		this.setPosition(pos.x, pos.y);
	}
}
