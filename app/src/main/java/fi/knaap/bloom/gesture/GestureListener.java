package fi.knaap.bloom.gesture;

import fi.knaap.bloom.calc.RawPoint;
import fi.knaap.bloom.calc.RawStroke;
import fi.knaap.bloom.control.BloomController;
import fi.knaap.bloom.control.CharAction;
import android.gesture.GestureOverlayView;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class GestureListener implements GestureOverlayView.OnGestureListener {
	RawStroke rawStroke = new RawStroke();
	BloomController controller;

	public GestureListener(BloomController controller) {
		this.controller = controller;
	}

	public void onGesture(GestureOverlayView overlay, MotionEvent event) {
		rawStroke.add(new RawPoint(event.getRawX(), event.getRawY()));
	}

	public void onGestureEnded(GestureOverlayView overlay, MotionEvent event) {
		rawStroke.add(new RawPoint(event.getRawX(), event.getRawY()));
		CharAction action = controller.processStroke(rawStroke);
		if (action != null) {
			if (action.getCodes() == -3) {
				controller.getBloomService().keyDownUp(KeyEvent.KEYCODE_DEL);
			} else {
				controller.getBloomService().sendKeyChar(action.asChar());
			}
		}
	}

	public void onGestureCancelled(GestureOverlayView overlay, MotionEvent event) {
	}

	@Override
	public void onGestureStarted(GestureOverlayView overlay, MotionEvent event) {
		rawStroke.clear();
		rawStroke.add(new RawPoint(event.getRawX(), event.getRawY()));
	}
}
