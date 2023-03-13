package com.application.alphacapital.superapp.pms.beans;

public class TotalValue{
	private int InitialValue;
	private int CurrentValue;
	private int Gain;

	public int getInitialValue() {
		return InitialValue;
	}

	public void setInitialValue(int initialValue) {
		InitialValue = initialValue;
	}

	public int getCurrentValue() {
		return CurrentValue;
	}

	public void setCurrentValue(int currentValue) {
		CurrentValue = currentValue;
	}

	public int getGain() {
		return Gain;
	}

	public void setGain(int gain) {
		Gain = gain;
	}
}
