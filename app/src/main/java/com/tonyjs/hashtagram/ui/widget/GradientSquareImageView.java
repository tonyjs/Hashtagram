package com.tonyjs.hashtagram.ui.widget;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;

/**
 * Created by orcpark on 14. 11. 9..
 */
public class GradientSquareImageView extends SquareImageView {

    public static final String GRADIENT_COLOR = "#991d1d1d";

    public GradientSquareImageView(Context context) {
        super(context);
    }

    public GradientSquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GradientSquareImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        Log.i("jsp", "onDraw - " + mShowingNull);
        if (getDrawable() != null) {
            Shader shader = new LinearGradient(0, 0, 0, getHeight(),
                    Color.TRANSPARENT, Color.parseColor(GRADIENT_COLOR),
                    Shader.TileMode.CLAMP);
            Paint paint = new Paint();
            paint.setShader(shader);
            canvas.drawRect(new RectF(0, 0, getRight(), getBottom()), paint);
        } else {
            Paint paint = new Paint();
            paint.setColor(Color.TRANSPARENT);
            canvas.drawRect(new RectF(0, 0, getRight(), getBottom()), paint);
        }
    }

}
