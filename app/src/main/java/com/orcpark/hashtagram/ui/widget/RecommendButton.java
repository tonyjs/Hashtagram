package com.orcpark.hashtagram.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import com.orcpark.hashtagram.util.UiUtils;

/**
 * Created by orcpark on 2014. 7. 2..
 */
public class RecommendButton extends TextView implements View.OnClickListener{
    public static final String GRADIENT_COLOR = "#000000";
    public static final int DEFAULT_FONT_SIZE = 15;

    public RecommendButton(Context context) {
        super(context);
        init();
    }

    public RecommendButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RecommendButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private static final int PADDING_HORIZONTAL = 12;
    private static final int PADDING_VERTICAL = 6;

    private void init() {
        int verticalPadding = UiUtils.getDPFromPixelSize(getContext(), PADDING_VERTICAL);
        int horizontalPadding = UiUtils.getDPFromPixelSize(getContext(), PADDING_HORIZONTAL);

        setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding);
        setGravity(Gravity.CENTER);
        setTextColor(Color.WHITE);
        setTextSize(TypedValue.COMPLEX_UNIT_SP, DEFAULT_FONT_SIZE);
//        setBackground(getResources().getDrawable(R.drawable.selector_btn_recommend));
        setOnClickListener(this);
    }
//
    @Override
    public void onClick(View v) {
        boolean selected = v.isSelected();
        Object tag = v.getTag();
//        if (tag != null && tag instanceof Recommends) {
//            Recommends recommends = (Recommends) tag;
//            int growth = selected ? -1 : +1;
//            recommends.setRecommended(!selected);
//            recommends.setCount(recommends.getCount() + growth);
//
//            recommendPost(recommends.getPostNo(), selected);
//
//            setValues(recommends);
//        }
    }
//
//    private void recommendPost(int postNo, boolean selected) {
//        if (!selected) {
//            RequestUtils.recommendPost(getContext(), postNo, null,
//                    mListener);
//        } else {
//            RequestUtils.unRecommendPost(getContext(), postNo, null,
//                    mListener);
//        }
//    }
//
//    private JSONObjectListener mListener = new JSONObjectListener() {
//        @Override
//        public void onResponse(JSONObject jsonObject) {
//            Log.e("jsp", jsonObject != null ? jsonObject.toString() : "null");
//        }
//    };
//
//    private void setValues(Recommends recommends) {
//        boolean recommended = recommends.isRecommended();
//        setSelected(recommended);
//        int recommendCount = recommends.getCount();
//        String count = recommended ?
//                                        Integer.toString(recommendCount) : "+" + recommendCount;
//        setText(count);
//    }
//
//    public void setAlpha() {
//        setAlpha(0.75f);
//    }
//
//    private Recommends mRecommends;
//
//    public void setRecommends(Recommends recommends) {
//        if (recommends == null) {
//            return;
//        }
//
//        mRecommends = recommends;
//        setTag(mRecommends);
//        setValues(mRecommends);
//    }
//
//    @Override
//    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        super.onLayout(changed, left, top, right, bottom);
//        if (mRecommends != null) {
//            setSelected(mRecommends.isRecommended());
//        } else {
//            setSelected(false);
//        }
//    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        Shader shader = new Shader();
//        Shader shader = new LinearGradient(0, 0, 0, getBottom(),
//                Color.TRANSPARENT, Color.parseColor(GRADIENT_COLOR),
//                Shader.TileMode.CLAMP);
//        Paint paint = new Paint();
//        paint.setColor(getResources().getColor(R.color.swipe_color_scheme_1));
//        paint.setShader(shader);
//        canvas.drawLine(0, 0, getRight(), getBottom(), paint);
//        canvas.drawRect(new Rect(0, 0, getRight(), getBottom()), paint);
    }
}
