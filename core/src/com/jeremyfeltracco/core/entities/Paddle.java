package com.jeremyfeltracco.core.entities;

import com.badlogic.gdx.math.Vector2;
import com.jeremyfeltracco.core.Sim;
import com.jeremyfeltracco.core.Textures;

public class Paddle extends Entity{
	public enum State {
		STOP, LEFT, RIGHT
	}
	private float vel;
	private Position side;
	private State state;
	
	
	public Paddle(Position side) {
		super(Textures.paddle);
		this.side = side;
		
		switch(side) {
		case TOP:
			setOriginPosition(0, Sim.maxY - sprite.getOriginY());
			break;
		case BOTTOM:
			setOriginPosition(0, -Sim.maxY + sprite.getOriginY());
			break;
		case LEFT:
			sprite.rotate(90);
			setOriginPosition(-Sim.maxX + sprite.getOriginY(), 0);
			break;
		case RIGHT:
			sprite.rotate(90);
			setOriginPosition(Sim.maxX - sprite.getOriginY(), 0);
			break;
		}
		
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
		case TOP:
			pos.x += -vel * delta;
			break;
		case BOTTOM:
			pos.x += vel * delta;
			break;
		case LEFT:
			pos.y += -vel * delta;
			break;
		case RIGHT:
			pos.y += vel * delta;
			break;
		}
		
		float boundX = Sim.maxX - sprite.getWidth() / 2;
		float boundY = Sim.maxY - sprite.getWidth() / 2;
	
		//Check whether the position is in valid
		if (side == Position.TOP || side == Position.BOTTOM) {
			if (pos.x >= boundX){
				pos.x = boundX;
			}
			if (pos.x < -boundX)
				pos.x = -boundX;
			
		}
		if (side == Position.LEFT || side == Position.RIGHT) {
			if (pos.y > boundY)
				pos.y = boundY;
			if (pos.y < -boundY)
				pos.y = -boundY;
		}
		
		setOriginPosition(pos.x, pos.y);
		
		bounceBall();
	}
	
	private void bounceBall(){
		Vector2 balPos = Sim.ball.getOriginPosition();
		//Deal with ball bounces!
	}
	
	public String toString(){
		return side.name();
	}
}
