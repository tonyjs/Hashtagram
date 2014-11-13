package com.orcpark.hashtagram.ui.widget;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by orcpark on 14. 11. 9..
 */
public class GradientSquareImageView extends ImageView {

    public static final String GRADIENT_COLOR = "#993d3d3d";

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
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(width, width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        Log.i("jsp", "onDraw - " + mShowingNull);
        if (mShowingNull) {
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

    private boolean mShowingNull = false;
    @Override
    public void setImageBitmap(Bitmap bm) {
        mShowingNull = bm != null;
        super.setImageBitmap(bm);
    }
}
