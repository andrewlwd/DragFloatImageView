package com.library.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.util.Log;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * 可将文本分成多段分别设置样式
 * 最多分四段
 *
 * @author mly
 * @date: 2020/4/8 20:57
 */
public class RichTextView extends AppCompatTextView {
    private static final String TAG = "RichTextView";

    private String text_1;
    private int textSize_1;
    private int textColor_1;
    private int textStyle_1;
    private String text_2;
    private int textSize_2;
    private int textColor_2;
    private int textStyle_2;
    private String text_3;
    private int textSize_3;
    private int textColor_3;
    private int textStyle_3;
    private String text_4;
    private int textSize_4;
    private int textColor_4;
    private int textStyle_4;

    private int start;
    private int end;

    public RichTextView(Context context) {
        super(context);
    }

    public RichTextView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public RichTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RichTextView, defStyleAttr, 0);

        int defaultStyle = ta.getInt(R.styleable.RichTextView_android_textStyle, Typeface.NORMAL);

        text_1 = ta.getString(R.styleable.RichTextView_rtvText_1);
        textSize_1 = (int) ta.getDimension(R.styleable.RichTextView_rtvTextSize_1, getTextSize());
        textColor_1 = ta.getColor(R.styleable.RichTextView_rtvTextColor_1, getCurrentTextColor());
        textStyle_1 = ta.getInt(R.styleable.RichTextView_rtvTextStyle_1, defaultStyle);

        text_2 = ta.getString(R.styleable.RichTextView_rtvText_2);
        textSize_2 = (int) ta.getDimension(R.styleable.RichTextView_rtvTextSize_2, getTextSize());
        textColor_2 = ta.getColor(R.styleable.RichTextView_rtvTextColor_2, getCurrentTextColor());
        textStyle_2 = ta.getInt(R.styleable.RichTextView_rtvTextStyle_2, defaultStyle);

        text_3 = ta.getString(R.styleable.RichTextView_rtvText_3);
        textSize_3 = (int) ta.getDimension(R.styleable.RichTextView_rtvTextSize_3, getTextSize());
        textColor_3 = ta.getColor(R.styleable.RichTextView_rtvTextColor_3, getCurrentTextColor());
        textStyle_3 = ta.getInt(R.styleable.RichTextView_rtvTextStyle_3, defaultStyle);

        text_4 = ta.getString(R.styleable.RichTextView_rtvText_4);
        textSize_4 = (int) ta.getDimension(R.styleable.RichTextView_rtvTextSize_4, getTextSize());
        textColor_4 = ta.getColor(R.styleable.RichTextView_rtvTextColor_4, getCurrentTextColor());
        textStyle_4 = ta.getInt(R.styleable.RichTextView_rtvTextStyle_4, defaultStyle);

        ta.recycle();

        richText();
    }

    private void richText() {
        start = 0;
        end = 0;
        SpannableStringBuilder s = new SpannableStringBuilder();

        setSpan(s, text_1, textSize_1, textColor_1, textStyle_1);
        setSpan(s, text_2, textSize_2, textColor_2, textStyle_2);
        setSpan(s, text_3, textSize_3, textColor_3, textStyle_3);
        setSpan(s, text_4, textSize_4, textColor_4, textStyle_4);

        if (TextUtils.isEmpty(s)) {
            s.append(getText());
        }

        setText(s);
    }

    private void setSpan(SpannableStringBuilder s, String text, int textSize, int textColor, int textStyle) {
        Log.i(TAG, String.format("%s, %s, %s, %s", text, textSize, textColor, textStyle));

        if (!TextUtils.isEmpty(text)) {
            start = end;
            end += text.length();
            s.append(text);
            s.setSpan(new AbsoluteSizeSpan(textSize), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            s.setSpan(new ForegroundColorSpan(textColor), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            s.setSpan(new StyleSpan(textStyle), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    public void setText_1(String text) {
        this.text_1 = text;
        richText();
    }

    public void setText_2(String text) {
        this.text_2 = text;
        richText();
    }

    public void setText_3(String text) {
        this.text_3 = text;
        richText();
    }

    public void setText_4(String text) {
        this.text_4 = text;
        richText();
    }
}
