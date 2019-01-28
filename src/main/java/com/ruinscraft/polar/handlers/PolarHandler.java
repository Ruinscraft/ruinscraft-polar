package com.ruinscraft.polar.handlers;

public interface PolarHandler<T> {

	void handlePositive(T t, double c);

	void handleNegative(T t, double c);

}
