package com.jeremyfeltracco.core.controllers;

import com.jeremyfeltracco.core.entities.Ball;
import com.jeremyfeltracco.core.entities.Paddle;
import com.jeremyfeltracco.core.entities.Side;

public class Naive extends Controller {

	public Naive(Side s, Paddle[] pads, Ball b) {
		super(s, pads, b);
	}
	
	@Override
	public void updatePaddle() {
		super.updatePaddle();
		if (ballPos.x > paddlePos[side.ordinal()].x) {
			p.setState(Paddle.State.RIGHT);
		} else if (ballPos.x < paddlePos[side.ordinal()].x) {
			p.setState(Paddle.State.LEFT);
		} else {
			p.setState(Paddle.State.STOP);
		}
	}
	
}
