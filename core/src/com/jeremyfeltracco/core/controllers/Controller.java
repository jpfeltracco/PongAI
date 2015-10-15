package com.jeremyfeltracco.core.controllers;

import java.util.EnumMap;

import com.badlogic.gdx.math.Vector2;
import com.jeremyfeltracco.core.entities.Ball;
import com.jeremyfeltracco.core.entities.Paddle;
import com.jeremyfeltracco.core.entities.Paddle.State;
import com.jeremyfeltracco.core.entities.Side;

public abstract class Controller {
	EnumMap<Side, Integer> rotMap = new EnumMap<Side, Integer>(Side.class);
	
	Side side;
	Paddle[] pads;
	Ball ball;
	
	public Controller(Side s, Paddle[] pads, Ball b) {
		this.side = s;
		this.pads = pads;
		this.ball = b;
		
		rotMap.put(Side.TOP, 180);
		rotMap.put(Side.BOTTOM, 0);
		rotMap.put(Side.LEFT, 90);
		rotMap.put(Side.RIGHT, -90);
	}
	
	protected void setPadState(State s) {
		pads[side.ordinal()].setState(s);
	}
	
	protected Vector2 getPaddlePos() {
		return getOtherPaddlePos(side);
	}
	
	protected Vector2 getOtherPaddlePos(Side s) {
		return pads[s.ordinal()].getOriginPosition().cpy().rotate(rotMap.get(side));
	}
	
	protected Vector2 getBallPos() {
		return ball.getOriginPosition().cpy().rotate(rotMap.get(side));
	}
	
	protected Vector2 getBallVel() {
		return ball.getVelocity().cpy().rotate(rotMap.get(side));		
	}
	
	public abstract void updatePaddle();
}
