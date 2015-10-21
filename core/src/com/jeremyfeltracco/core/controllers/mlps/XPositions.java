package com.jeremyfeltracco.core.controllers.mlps;

import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.util.TransferFunctionType;
import org.neuroph.util.random.GaussianRandomizer;

import com.jeremyfeltracco.core.Sim;
import com.jeremyfeltracco.core.entities.Ball;
import com.jeremyfeltracco.core.entities.Paddle;
import com.jeremyfeltracco.core.entities.Paddle.State;
import com.jeremyfeltracco.core.entities.Side;

public class XPositions extends MLPControl {

	public XPositions(Side side, Paddle[] pads, Ball ball) {
		super(side, pads, ball);
		setNet(new MultiLayerPerceptron(TransferFunctionType.TANH, 1, 2, 2, 1));
//		GaussianRandomizer r = new GaussianRandomizer(0, 1f);
//		getNet().randomizeWeights(r);
	}

	@Override
	public State controlPaddle() {
		float ballX = this.getBallPos().x;
		float padX = this.getPaddlePos().x;
		
		float in = (ballX - padX) / (2 * Sim.maxX); // 0 = farthest left, 1 =
													// farthest right
		getNet().setInput(in);
		getNet().calculate();
		double output = getNet().getOutput()[0];
		if (output < .4)
			return State.LEFT;
		else if (output > .4)
			return State.RIGHT;
		return State.STOP;
	}
}
