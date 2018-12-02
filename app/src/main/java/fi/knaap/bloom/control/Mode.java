package fi.knaap.bloom.control;

import java.util.Map;
import java.util.TreeMap;

import android.content.res.XmlResourceParser;
import android.util.Log;

public class Mode {
	int index;
	String name;
	TreeMap<StrokeDescription, CharAction> bindings = new TreeMap<StrokeDescription, CharAction>();
	CharAction defaultAction = null;;

	private Mode(int index, String name) {
		super();
		this.index = index;
		this.name = name;
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

	public static Mode createMode(XmlResourceParser parser, int index) {
		String name = parser.getAttributeValue(null, "name"); 
		Log.i("processStroke", "Processing mode: " + name);
		return new Mode(index, name);
	}
	
	public Map<StrokeDescription, CharAction> getBindings(){
		return bindings;
	}

	public String getName() {
		return name;
	}

	public boolean isUpper() {
		return "UPPER".equals(name);
	}
}
