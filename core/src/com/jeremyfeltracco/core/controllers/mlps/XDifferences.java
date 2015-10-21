package com.jeremyfeltracco.core.controllers.mlps;

import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.util.TransferFunctionType;

import com.jeremyfeltracco.core.Sim;
import com.jeremyfeltracco.core.entities.Ball;
import com.jeremyfeltracco.core.entities.Paddle;
import com.jeremyfeltracco.core.entities.Paddle.State;
import com.jeremyfeltracco.core.entities.Side;

public class XDifferences extends MLPControl {
	public XDifferences(Side side, Paddle[] pads, Ball ball) {
		super(side, pads, ball);
		this.setNet(new MultiLayerPerceptron(TransferFunctionType.TANH, 2, 7, 7, 7, 1));
	}

	@Override
	public State controlPaddle() {
		float ballX = this.getBallPos().x;
		float padX = this.getPaddlePos().x;
		float inBallX = (ballX + Sim.maxX) / (2 * Sim.maxX);
		float inPadX = (padX + Sim.maxX) / (2 * Sim.maxX);
		
		getNet().setInput(inPadX, inBallX);
		getNet().calculate();
		double output = getNet().getOutput()[0];
		if (output < .4)
			return State.LEFT;
		else if (output > .4)
			return State.RIGHT;
		
		return State.STOP;
	}

}
