package fi.knaap.bloom.control;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.content.SharedPreferences;
import android.content.res.XmlResourceParser;
import android.preference.PreferenceManager;
import android.util.Log;
import fi.knaap.bloom.BloomService;
import fi.knaap.bloom.R;
import fi.knaap.bloom.calc.RawStroke;
import fi.knaap.bloom.gesture.BloomView;

public class BloomController  {
	private static final String TAG_MODE = "Mode";
	private static final String TAG_DESCRIPTION = "Description";
	private static final String TAG_CHAR_ACTION = "CharAction";

	private StrokeDescription desc = new StrokeDescription();
	private Config config = null;
	private BloomService bloomService = null;
	private boolean useAutoCapitalisation = false;
	private String language;
	private BloomView bloomView = null;

	public BloomController(BloomService bloomService) {
		this.bloomService = bloomService;
		desc.setBloomController(this);
		updatePreferences();
	}

	public void updatePreferences() {
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(bloomService);
		useAutoCapitalisation = sharedPrefs.getBoolean("auto_capitalization", false);
		language = sharedPrefs.getString("language", "en");
		updateBloomLayOut();
		BloomView view = getBloomView();
		if(view != null){
			view.updatePreferences();
		}
	}	
		
	public Config getConfig() {
		return config;
	}

	boolean prevCharWrittenUpper = false;
	public CharAction processStroke(RawStroke stroke) {
		stroke.normalize();
		desc.setStroke(stroke);
		Log.i("processStroke", "Received stroke: " + desc.toString());
		CharAction action = config.getKeyAction(desc);
		
		if (action != null) {
			if (action.getCodes() == -1) {
				config.incCurrentMode();
				getBloomView().setNeedsRedraw(true);
				action = null;
			} else if (action.getCodes() == -2) {
				config.decCurrentMode();
				getBloomView().setNeedsRedraw(true);
				action = null;
			} else if(useAutoCapitalisation && config.getMode().isUpper()){
				if(!prevCharWrittenUpper){
					config.returnToPreviousMode();
					getBloomView().setNeedsRedraw(true);					
				}
				prevCharWrittenUpper = true;	
			}else{
				prevCharWrittenUpper = false;	
			}
				
		}
		return action;
	}

	public void updateBloomLayOut() {
		int resValue = 0x7f030001;
		try {
			resValue = R.xml.class.getDeclaredField("bloom_layout_" + language).getInt(null);
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
		} catch (SecurityException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (NoSuchFieldException e1) {
			e1.printStackTrace();
		}
		XmlResourceParser parser = bloomService.getResources().getXml(
				resValue);
		Config tempConfig = new Config();
		int modeIndex = 0;
		int event;
		Mode currentMode = null;
		StrokeDescription currentDesc = null;
		try {
			while ((event = parser.next()) != XmlResourceParser.END_DOCUMENT) {
				if (event == XmlResourceParser.START_TAG) {
					String tag = parser.getName();
					if (TAG_MODE.equals(tag)) {
						currentMode = Mode.createMode(parser, modeIndex);
						tempConfig.addMode(currentMode);
						modeIndex++;
					} else if (TAG_DESCRIPTION.equals(tag)) {
						currentDesc = StrokeDescription
								.createDescription(parser);
					} else if (TAG_CHAR_ACTION.equals(tag)) {
						CharAction charAction = CharAction
								.createCharAction(parser);
						currentMode.addBinding(currentDesc, charAction);
					}
				}
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			parser.close();
		}
		config = tempConfig;
	}

	public BloomService getBloomService() {
		return bloomService;
	}

	public BloomView getBloomView() {
		return bloomView;
	}

	public void setBloomView(BloomView bloomView) {
		this.bloomView = bloomView;
	}
}
