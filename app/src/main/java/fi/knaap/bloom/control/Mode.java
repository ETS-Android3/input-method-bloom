package fi.knaap.bloom.control;

import java.util.Map;
import java.util.TreeMap;

import android.content.res.XmlResourceParser;
import android.util.Log;

public class Mode {
	private int index;
	private String name;
	private String type;
	TreeMap<StrokeDescription, CharAction> bindings = new TreeMap<StrokeDescription, CharAction>();
	CharAction defaultAction = null;;

	private Mode(int index, String name, String type) {
		super();
		this.index = index;
		this.name = name;
		this.type = type;
	}

	public int getIndex() {
		return index;
	}
	
	public void addBinding(StrokeDescription desc, CharAction action){
		Log.i("createDescription", "Read from XML: " + desc.toString() + " binding to " + action.toString());
		bindings.put(desc, action);
	}
	
	public CharAction getKeyAction(StrokeDescription desc){
		
		CharAction retAction = bindings.get(desc);
		if(retAction == null){
			retAction = defaultAction;
		}
		return retAction;
	}

	public static Mode createMode(XmlResourceParser parser, int index, Mode prevMode) {
		String name = parser.getAttributeValue(null, "name"); 
		String type = parser.getAttributeValue(null, "type"); 
		Log.i("processStroke", "Processing mode: " + name + ", type: " + type);
		Mode newMode =  new Mode(index, name, type);
		if(prevMode != null && prevMode.isLower() && newMode.isUpper()){
			copyUpperMode(prevMode, newMode);
		}
		return newMode;
	}
	
	private static void copyUpperMode(Mode prevMode, Mode newMode) {
		for(StrokeDescription desc : prevMode.getBindings().keySet()){
			CharAction lowerAction = prevMode.getBindings().get(desc);
			String upperActionLabel = lowerAction.charActionLabel;
			if(upperActionLabel != null){
				upperActionLabel = upperActionLabel.toUpperCase();
			}
			String upperCharDisplay = lowerAction.charDisplay;
			if(upperCharDisplay != null){
				upperCharDisplay = upperCharDisplay.toUpperCase();
			}
			CharAction upperAction = new CharAction(lowerAction.codes,upperActionLabel, upperCharDisplay);
			newMode.addBinding(desc, upperAction);
		}
	}

	public Map<StrokeDescription, CharAction> getBindings(){
		return bindings;
	}

	public String getName() {
		return name;
	}

	public boolean isUpper() {
		return "UPPER".equals(type);
	}
	public boolean isLower() {
		return "LOWER".equals(type);
	}
	public boolean isNum() {
		return "NUM".equals(type);
	}
}
