package fi.knaap.bloom.gesture;

import java.util.Set;
import java.util.Map.Entry;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout.LayoutParams;
import fi.knaap.bloom.BloomService;
import fi.knaap.bloom.calc.RawPoint;
import fi.knaap.bloom.calc.Util;
import fi.knaap.bloom.control.BloomController;
import fi.knaap.bloom.control.CharAction;
import fi.knaap.bloom.control.StrokeDescription;
import fi.knaap.bloom.control.StrokeDescription.Curve;
import fi.knaap.bloom.control.StrokeDescription.Direction;
import fi.knaap.bloom.control.StrokeDescription.Length;

public class BloomView extends View {
	private int bloomSize = 800;
	public BloomView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public BloomView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BloomView(Context context) {
		super(context);
        updateLayout();
	}

	private void updateLayout() {
		LayoutParams params2 = new LayoutParams(bloomSize, bloomSize);
		params2.gravity = Gravity.CENTER;
        setLayoutParams(params2);
	}
	
	public BloomService getBloomService(){
		return (BloomService)getContext();
	}

	public BloomController getController(){
		return getBloomService().getBloomController();
	}

	private Paint paint = new Paint();

	private boolean needsRedraw = true;

	public void setNeedsRedraw(boolean needsRedraw) {
		this.needsRedraw = needsRedraw;
		if(needsRedraw) {
			this.invalidate();
		}
	}

	private Bitmap cachedBitmap;

	@Override
	protected void onDraw(Canvas origCanvas) {
		if (needsRedraw) {
			int textSize = Math.round(bloomSize/8);
			cachedBitmap = Bitmap.createBitmap(getWidth(), getHeight(),
					android.graphics.Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(this.cachedBitmap);
			RawPoint center = new RawPoint(getWidth() / 2, getHeight() / 2);
			canvas.translate(center.getX(), center.getY());
			double radius = Math.min(center.getX(), center.getY()) - textSize;
			getController().getConfig().setBloomRadius(radius);
			drawGreyBackGround(textSize, canvas, radius);
			drawBlackBackGround(textSize, canvas, radius);
			drawLetters(textSize, canvas, radius);
			needsRedraw = false;
		}
		origCanvas.drawBitmap(this.cachedBitmap, 0, 0, new Paint());
		super.onDraw(origCanvas);
	}
	
	private void drawLetters(int textSize, Canvas canvas, double radius) {
		Typeface basicFont = Typeface.create(fontPref, 0);
		Typeface bloomFont = Typeface.createFromAsset(getContext().getAssets(),"fonts/Bloom.ttf");
		paint.setTextSize(textSize);		
		paint.setTypeface(basicFont);
		paint.setAntiAlias(true);
		paint.setTextAlign(Align.CENTER);
		Set<Entry<StrokeDescription, CharAction>> bindings = getController().getConfig().getCurrentBindings().entrySet();
		for (Entry<StrokeDescription, CharAction> binding : bindings) {
			if(binding.getKey().getDirection().equals(Direction.ALL)){
				continue;
			}
			double rotateAngle = Math.PI + Math.PI
					* binding.getKey().getDirection().ordinal() / 4;
			RawPoint point = findCharLocation(textSize, radius, binding);
			if(binding.getKey().getCurve() == Curve.NONE){
				paint.setColor(Color.WHITE);				
			}else{
				paint.setColor(Color.LTGRAY);
			}
			String charString = "" + (char) binding.getValue().getCharDisplay();
			Util.rotatePoint(rotateAngle, point);
			float x = point.getX();
			float y = point.getY() + Math.round(0.3 * textSize);
			if(binding.getValue().isCharDisplay()){
				 paint.setTypeface(bloomFont);
			}
			canvas.drawText(charString, x, y, paint);
			if(binding.getValue().isCharDisplay()){
				 paint.setTypeface(basicFont);
			}
		}
	}

	private void drawBlackBackGround(int textSize, Canvas canvas, double radius) {
		Paint blackPaint = new Paint();
		blackPaint.setAntiAlias(true);
		blackPaint.setColor(Color.BLACK);
		for (Direction dir : StrokeDescription.Direction.values()) {
			if (dir.compareTo(Direction.ALL) == 0) {
				break;
			}
			
			RawPoint leftTop = new RawPoint((float) (-textSize * 1.3), Math
					.round(radius + textSize * 0.6));
			RawPoint rightBottom = new RawPoint((float) (textSize * 1.3),
					Math.round(radius - textSize * 0.6));
			RectF rect = new RectF(leftTop.getX(), leftTop.getY(),
					rightBottom.getX(), rightBottom.getY());
			canvas.drawOval(rect, blackPaint);

			leftTop = new RawPoint((float) (-textSize * 0.6), Math
					.round(radius*0.8 + textSize * 1));
			rightBottom = new RawPoint((float) (textSize * 0.6),
					Math.round(radius*0.8 - textSize * 1));
			rect = new RectF(leftTop.getX(), leftTop.getY(),
					rightBottom.getX(), rightBottom.getY());
			canvas.drawOval(rect, blackPaint);
			canvas.rotate(45);
			// break;

		}
	}

	private void drawGreyBackGround(int textSize, Canvas canvas, double radius) {
		Paint greyPaint = new Paint();
		greyPaint.setAntiAlias(true);
		greyPaint.setColor(Color.LTGRAY);
		int greyBorderSize = 2;
		for (Direction dir : StrokeDescription.Direction.values()) {
			if (dir.compareTo(Direction.ALL) == 0) {
				break;
			}
			RawPoint leftTop = new RawPoint((float) (-textSize * 1.3), Math
					.round(radius + textSize * 0.6));
			RawPoint rightBottom = new RawPoint((float) (textSize * 1.3),
					Math.round(radius - textSize * 0.6));
			RectF rect = new RectF(leftTop.getX() - greyBorderSize, leftTop.getY() + greyBorderSize,
					rightBottom.getX() + greyBorderSize, rightBottom.getY() - greyBorderSize);
			canvas.drawOval(rect, greyPaint);

			leftTop = new RawPoint((float) (-textSize * 0.6), Math
					.round(radius*0.8 + textSize * 1));
			rightBottom = new RawPoint((float) (textSize * 0.6),
					Math.round(radius*0.8 - textSize * 1));
			rect = new RectF(leftTop.getX() - greyBorderSize, leftTop.getY() + greyBorderSize,
					rightBottom.getX() + greyBorderSize, rightBottom.getY() - greyBorderSize);
			canvas.drawOval(rect, greyPaint);
			
			canvas.rotate(45);
			// break;

		}
	}

	private RawPoint findCharLocation(int textSize, double radius,
			Entry<StrokeDescription, CharAction> binding) {
		RawPoint point = null;
		if (binding.getKey().getCurve().equals(Curve.NONE)) {
			if (binding.getKey().getLength().equals(Length.SHORT)) {
				point = new RawPoint(0, Math.round(radius - textSize));
			} else {
				point = new RawPoint(0, Math.round(radius));
			}
		} else if (binding.getKey().getCurve().equals(Curve.LEFT)) {
			point = new RawPoint(Math.round(textSize * 0.7), Math
					.round(radius));
		} else if (binding.getKey().getCurve().equals(Curve.RIGHT)) {
			point = new RawPoint(Math.round(-textSize * 0.7), Math
					.round(radius));
		}
		return point;
	}

	String fontPref = "SANS_SERIF";

	public void updatePreferences() {
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getBloomService());
		fontPref = sharedPrefs.getString("font", "SANS_SERIF");		
		bloomSize = Integer.valueOf(sharedPrefs.getString("size", "200"));
        updateLayout();		
		needsRedraw = true;
	}
}
