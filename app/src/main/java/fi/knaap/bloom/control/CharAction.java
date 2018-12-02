package fi.knaap.bloom.control;

import android.content.res.XmlResourceParser;

public class CharAction {
	int codes = -99999;
	String charActionLabel;
	String charDisplay;
	private CharAction(int codes, String keyLabel, String charDisplay) {
		super();
		this.codes = codes;
		this.charActionLabel = keyLabel;
		this.charDisplay = charDisplay;
	}
	public static CharAction createCharAction(XmlResourceParser parser) {
		String codesStr = parser.getAttributeValue(null, "codes");
		int codes = -999999;
		if(codesStr != null){
			codes = Integer.parseInt(codesStr);
		}
		String charActionLabel = parser.getAttributeValue(null, "CharActionLabel");
		String charDisplay = parser.getAttributeValue(null, "CharDisplay");
		return new CharAction(codes, charActionLabel, charDisplay);
	}
	public int getCodes() {
		return codes;
	}
	public String getCharActionLabel() {
		return charActionLabel;
	}
	public char getCharDisplay() {
		if(charDisplay != null){
			return charDisplay.charAt(0);
		}else if (charActionLabel != null) {
			return charActionLabel.charAt(0);
		}else
			return 'x';
	}
	public char asChar(){
		if(charActionLabel != null){
			return charActionLabel.charAt(0);
		}else{
			return (char) codes;
		}
	}
	
	public String toString(){
		return "" + getCharDisplay();
	}
	
}
