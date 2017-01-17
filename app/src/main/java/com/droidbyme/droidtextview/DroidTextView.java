package com.droidbyme.droidtextview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

/**
 * Created by Sagar on 14-12-2016.
 */

public class DroidTextView extends TextView implements Runnable, ValueAnimator.AnimatorUpdateListener {

    private boolean isUnderline;
    private boolean isBold;
    private String highLightText;
    private int highlightTextColor;
    private int dots;
    private String currentText;
    private int bigTimes, bigLength;
    private String font;
    private int typeSpeed;

    private boolean executed = false;
    private int counter = 0;
    private Handler handler;
    private int animationDuration = 0;
    private double[] alphas;
    private int red;
    private int green;
    private int blue;

    public DroidTextView(Context context) {
        super(context);
    }

    public DroidTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {

            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DroidTextView);

            // blink
            if (a.getBoolean(R.styleable.DroidTextView_isBlink, false)) {
                setBlink();
            }

            // reveal
            if (a.getInt(R.styleable.DroidTextView_reveal, 0) != 0) {
                animationDuration = a.getInt(R.styleable.DroidTextView_reveal, 0);
                setReveal(animationDuration);
            }

            // typeSpeed
            this.typeSpeed = a.getInt(R.styleable.DroidTextView_typeSpeed, 0);
            setTypingSpeed(typeSpeed);

            // highlight
            this.highLightText = a.getString(R.styleable.DroidTextView_hlText);
            if (!TextUtils.isEmpty(this.highLightText)) {
                highlightTextColor = a.getInt(R.styleable.DroidTextView_hlTextColor, 0);
                isUnderline = a.getBoolean(R.styleable.DroidTextView_hlUnderline, false);
                isBold = a.getBoolean(R.styleable.DroidTextView_hlBold, false);
                setHighLightText(highLightText, highlightTextColor, isUnderline, isBold);
            }

            // dots
            dots = a.getInt(R.styleable.DroidTextView_endDots, 0);
            if (dots != 0) {
                setEndDots(dots);
            }

            // bigText
            bigLength = a.getInt(R.styleable.DroidTextView_bigLength, 0);
            if (bigLength > 0) {
                bigTimes = a.getInt(R.styleable.DroidTextView_bigTimes, 0);
                setBigTextByIndex(bigLength, bigTimes);
            }

            // font
            font = a.getString(R.styleable.DroidTextView_font);
            if (!TextUtils.isEmpty(font)) {
                setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/" + font));
            }
            a.recycle();
        }
    }

    public void setReveal(int animationDuration) {
        this.animationDuration = animationDuration;
        currentText = getText().toString().trim();
        alphas = new double[currentText.length()];
        for (int i = 0; i < currentText.length(); i++) {
            alphas[i] = Math.random() - 1.0f;
        }

        setText(currentText);

        replayAnimation();
    }

    private void replayAnimation() {
        if (null != currentText) {
            post(this);
        }
    }

    private int clamp(double v) {
        return (int) (255f * Math.min(Math.max(v, 0f), 1f));
    }

    public void setBlink() {
        Animation blink = AnimationUtils.loadAnimation(getContext(), R.anim.blink);
        setAnimation(blink);
    }

    public void setTypingSpeed(final int typeSpeed) {
        currentText = getText().toString().trim();
        if (typeSpeed > 0) {
            setText("");
            if (!executed) {
                executed = true;
                counter = 0;
                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setText(currentText.substring(0, counter));
                        counter++;
                        if (currentText.length() >= counter) {
                            postDelayed(this, typeSpeed);
                        } else {
                            executed = false;
                        }
                    }
                }, typeSpeed);
            }
        }
    }

    public void setHighLightText(String text, int textColor, boolean isUnderline, boolean isBold) {
        if (!TextUtils.isEmpty(text)) {
            currentText = getText().toString().trim();
            this.isUnderline = isUnderline;
            this.isBold = isBold;
            if (!TextUtils.isEmpty(currentText)) {
                currentText = currentText.replaceAll(text, doColor(text, textColor));
            }
            if (!TextUtils.isEmpty(currentText)) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    setText(Html.fromHtml(currentText, Html.FROM_HTML_MODE_LEGACY));
                } else {
                    setText(Html.fromHtml(currentText));
                }
            }
        }

    }

    private String doColor(String text, int textColor) {
        if (isUnderline && isBold)
            return "<u><b><font color=\"" + textColor + "\">" + text + "</font></b></u>";
        else if (isUnderline)
            return "<u><font color=\"" + textColor + "\">" + text + "</font></u>";
        else if (isBold)
            return "<b><font color=\"" + textColor + "\">" + text + "</font></b>";
        else
            return "<font color=\"" + textColor + "\">" + text + "</font>";
    }

    public void setEndDots(int maxLines) {
        setMaxLines(maxLines);
        setEllipsize(TextUtils.TruncateAt.END);
    }

    public void setBigTextByIndex(int charLength, int bigTimes) {
        this.bigTimes = bigTimes;
        currentText = getText().toString().trim();
        String subString = currentText.substring(0, charLength);
        currentText = currentText.substring(charLength, currentText.length());
        subString = doBig(subString);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            setText(Html.fromHtml(subString + currentText, Html.FROM_HTML_MODE_LEGACY));
        } else {
            setText(Html.fromHtml(subString + currentText));
        }
    }

    private String doBig(String subString) {
        String fString = "";
        String eString = "";
        for (int i = 0; i < bigTimes; i++) {
            fString = fString + "<big>";
            eString = eString + "</big>";
        }
        return fString + subString + eString;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        final float value = (float) valueAnimator.getAnimatedValue();
        SpannableStringBuilder builder = new SpannableStringBuilder(currentText);
        for (int i = 0; i < currentText.length(); i++) {
            builder.setSpan(new ForegroundColorSpan(Color.argb(clamp(value + alphas[i]), red, green, blue)), i, i + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        setText(builder);
    }

    @Override
    public void run() {
        final int color = getCurrentTextColor();

        red = Color.red(color);
        green = Color.green(color);
        blue = Color.blue(color);

        ValueAnimator animator = ValueAnimator.ofFloat(0f, 2f);
        animator.setDuration(animationDuration);
        animator.addUpdateListener(this);
        animator.start();
    }
}
