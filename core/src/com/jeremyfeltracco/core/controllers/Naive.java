package com.jeremyfeltracco.core.controllers;

import com.badlogic.gdx.math.Vector2;
import com.jeremyfeltracco.core.entities.Ball;
import com.jeremyfeltracco.core.entities.Paddle;
import com.jeremyfeltracco.core.entities.Paddle.State;
import com.jeremyfeltracco.core.entities.Side;

public class Naive extends Controller {

	public Naive(Side s, Paddle[] pads, Ball b) {
		super(s, pads, b);
	}
	
	@Override
	public State controlPaddle() {
		State state = this.getPadState();
		Vector2 ballPos = this.getBallPos();
		Vector2 padPos = this.getPaddlePos();
		if (ballPos.x > padPos.x) {
			state = State.RIGHT;
		} else if (ballPos.x < padPos.x) {
			state = State.LEFT;
		}
		return state;
	}
	
}
