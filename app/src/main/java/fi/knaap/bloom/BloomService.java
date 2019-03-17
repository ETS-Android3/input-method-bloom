/*
 * Copyright (C) 2012 Knaap.
 */

package fi.knaap.bloom;

import android.content.res.Configuration;
import android.gesture.GestureOverlayView;
import android.inputmethodservice.InputMethodService;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.FrameLayout.LayoutParams;
import fi.knaap.bloom.control.BloomController;
import fi.knaap.bloom.gesture.BloomView;
import fi.knaap.bloom.gesture.GestureListener;

/**
 * Bloom Service class.
 */
public class BloomService extends InputMethodService  {
    static final boolean DEBUG = false;
    BloomController bloomController;

    /**
     * Main initialization of the input method component.  Be sure to call
     * to super class.
     */
    @Override public void onCreate() {
        super.onCreate();
        bloomController = new BloomController(this);
    }
    
    public BloomController getBloomController() {
		return bloomController;
	}

	@Override
    public void onConfigurationChanged(Configuration newConfig) {
    	// TODO Auto-generated method stub
    	super.onConfigurationChanged(newConfig);
    }
    
    @Override
    public void onStartInputView(EditorInfo info, boolean restarting) {
    	// TODO Auto-generated method stubs
    	super.onStartInputView(info, restarting);
        int inputType = info.inputType & InputType.TYPE_MASK_CLASS;
        updatePreferences();
        int inputNumbers = inputType & InputType.TYPE_CLASS_DATETIME | inputType & InputType.TYPE_CLASS_NUMBER
                |inputType & InputType.TYPE_CLASS_PHONE;
        if (inputNumbers != 0)
        {
            getBloomController().getConfig().setModeTypeNum();
        }
    }
     
    private void updatePreferences() {
    	getBloomController().updatePreferences();
	}

	/**
     * This is the point where you can do all of your UI initialization.  It
     * is called after creation and any configuration change.
     */
    @Override public void onInitializeInterface() {
    }
    /**
     * Helper to send a key down / key up pair to the current editor.
     */
    public void keyDownUp(int keyEventCode) {
        getCurrentInputConnection().sendKeyEvent(
                new KeyEvent(KeyEvent.ACTION_DOWN, keyEventCode));
        getCurrentInputConnection().sendKeyEvent(
                new KeyEvent(KeyEvent.ACTION_UP, keyEventCode));
    }

    /**
     * Called by the framework when your view for creating input needs to
     * be generated.  This will be called the first time your input method
     * is displayed, and every time it needs to be re-created such as due to
     * a configuration change.
     */
    @Override
    public View onCreateInputView() {
        BloomView bloomView = null;
        GestureOverlayView gestureOverlayView = new GestureOverlayView(this);
        gestureOverlayView.setFocusable(false);
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        gestureOverlayView.setLayoutParams(params);
        bloomView = new BloomView(this);
        getBloomController().setBloomView(bloomView);
		gestureOverlayView.addView(bloomView);
		gestureOverlayView.addOnGestureListener(new GestureListener(getBloomController()));
        return gestureOverlayView;
    }

}
