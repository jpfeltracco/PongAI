package com.jeremyfeltracco.core.entities;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.jeremyfeltracco.core.Sim;
import com.jeremyfeltracco.core.Textures;

public class Paddle extends Entity{
	public enum State {
		STOP, LEFT, RIGHT
	}
	private float vel;
	private float boundY;
	private float boundX;
	private State state;
	protected float curveSideLength = 128f;
	
	public Paddle(Side side) {
		super(Textures.paddle);
		this.side = side;
		this.state = State.STOP;
		
		switch(side) {
		case UP:
			setOriginPosition(0, Sim.maxY - sprite.getOriginY());
			break;
		case DOWN:
			setOriginPosition(0, -Sim.maxY + sprite.getOriginY());
			break;
		case LEFT:
			sprite.rotate(90);
			initRotation = 90;
			setOriginPosition(-Sim.maxX + sprite.getOriginY(), 0);
			break;
		case RIGHT:
			sprite.rotate(90);
			initRotation = 90;
			setOriginPosition(Sim.maxX - sprite.getOriginY(), 0);
			break;
		}
		float size = Textures.corner.getTexture().getHeight()/2;
		boundX = Sim.maxX - sprite.getWidth() / 2 - size * 2;
		boundY = Sim.maxY - sprite.getWidth() / 2 - size * 2;
		this.updateSides();
		setInitPos();
		
	}
	
	@Override
	public void update(float delta) {
		Vector2 pos = getOriginPosition();
		
		switch(state) {
		case STOP:
			vel = 0;
			break;
		case LEFT:
			vel = -100;
			break;
		case RIGHT:
			vel = 100;
			break;
		}
		
		switch(side) {
		case UP:
			pos.x += -vel * delta;
			velocity.x = -vel;
			break;
		case DOWN:
			pos.x += vel * delta;
			velocity.x = vel;
			break;
		case LEFT:
			pos.y += -vel * delta;
			velocity.y = -vel;
			break;
		case RIGHT:
			pos.y += vel * delta;
			velocity.y = vel;
			
			break;
		}
	
		//Check whether the position is invalid
		if (side == Side.UP || side == Side.DOWN) {
			if (pos.x >= boundX){
				pos.x = boundX;
				velocity.x = 0;
			}
			if (pos.x < -boundX){
				pos.x = -boundX;
				velocity.x = 0;
			}
		}
		if (side == Side.LEFT || side == Side.RIGHT) {
			if (pos.y > boundY){
				pos.y = boundY;
				velocity.y = 0;
			}
			if (pos.y < -boundY){
				pos.y = -boundY;
				velocity.y = 0;
			}
		}
		
		setOriginPosition(pos.x, pos.y);
		this.updateSides();
	}
	
	public float getVel() {
		return vel;
	}
	
	public void setState(State s) {
		this.state = s;
	}
	
	public State getState() {
		return this.state;
	}
	
	public String toString(){
		return side.name() + " " + getID();
	}

	
}
