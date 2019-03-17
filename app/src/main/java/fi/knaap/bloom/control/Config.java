package fi.knaap.bloom.control;

import java.util.ArrayList;
import java.util.TreeMap;

public class Config {
	/**
	 * Mode 0 is the common mode that is always available
	 */
	private int currentMode = 1;
	private ArrayList<Mode> modes = new ArrayList<Mode>();
	private double bloomRadius;

	public void addMode(Mode mode) {
		modes.add(mode);
	}

	private Mode getMode(int index) {
		return modes.get(index);
	}

	public Mode getMode() {
		return getMode(currentMode);
	}
	public Mode getCommonMode() {
		return getMode(0);
	}
	
	public CharAction getKeyAction(StrokeDescription desc){
		CharAction action = getCommonMode().getKeyAction(desc);

		if (action == null) {
			action = getMode().getKeyAction(desc);
		}
		return action;
	}
	
	public TreeMap<StrokeDescription, CharAction> getCurrentBindings(){
		TreeMap<StrokeDescription, CharAction> currentBindings = new TreeMap<StrokeDescription, CharAction>(getCommonMode().getBindings());
		currentBindings.putAll(getMode().getBindings());
		return currentBindings;
	}

	private int previousMode;
	public int getPreviousMode() {
		return previousMode;
	}
	
	public void returnToPreviousMode(){
		currentMode = previousMode;
	}

	public void incCurrentMode() {
		previousMode = currentMode;
		if (currentMode < modes.size() - 1) {
			currentMode++;
		} else {
			currentMode = 1;
		}
	}

	public void setModeTypeNum() {
		for(Mode mode: modes){
			if(mode.isNum()){
				currentMode = mode.getIndex();
			}
		}
	}

	public void decCurrentMode() {
		previousMode = currentMode;
		if (currentMode > 1) {
			currentMode--;
		} else {
			currentMode = modes.size() - 1;
		}
	}


	public void setBloomRadius(double radius) {
		this.bloomRadius = radius;
	}

	public double getBloomRadius() {
		return bloomRadius;
	}
}
