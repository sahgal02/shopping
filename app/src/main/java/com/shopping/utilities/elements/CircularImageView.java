package com.shopping.utilities.elements;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;

import com.shopping.R;

public class CircularImageView extends AppCompatImageView {

    private static final ScaleType SCALE_TYPE = ScaleType.CENTER_CROP;

    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
    private static final int COLOR_DIMENSION = 2;

    private static final int DEFAULT_BORDER_WIDTH = 0;
    private static final int DEFAULT_BORDER_COLOR = Color.BLACK;
    private static final boolean DEFAULT_BORDER_OVERLAY = false;

    private final RectF dRect = new RectF();
    private final RectF bRect = new RectF();

    private final Matrix sMatrix = new Matrix();
    private final Paint bitmapPaint = new Paint();
    private final Paint bPaint = new Paint();

    private int bColor = DEFAULT_BORDER_COLOR;
    private int bWidth = DEFAULT_BORDER_WIDTH;

    private Bitmap bitmap;
    private BitmapShader bShader;
    private int bitmapWidth;
    private int bitmapHeight;

    private float mDR;
    private float mBR;

    private ColorFilter mCF;

    private boolean mR;
    private boolean mSP;
    private boolean mBO;

    public CircularImageView(@NonNull Context context) {
        super(context);
        init();
    }

    public CircularImageView(@NonNull Context context, @NonNull AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircularImageView(@NonNull Context context, @NonNull AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircularImageView, defStyle, 0);
        bWidth = a.getDimensionPixelSize(R.styleable.CircularImageView_border_width, DEFAULT_BORDER_WIDTH);
        bColor = a.getColor(R.styleable.CircularImageView_border_color, DEFAULT_BORDER_COLOR);
        mBO = a.getBoolean(R.styleable.CircularImageView_border_overlay, DEFAULT_BORDER_OVERLAY);
        a.recycle();
        init();
    }

    private void init() {
        super.setScaleType(SCALE_TYPE);
        mR = true;
        if (mSP) {
            setup();
            mSP = false;
        }
    }

    @Override
    public ScaleType getScaleType() {
        return SCALE_TYPE;
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        if (scaleType != SCALE_TYPE) {
            throw new IllegalArgumentException(String.format("ScaleType %s not supported.", scaleType));
        }
    }

    @Override
    public void setAdjustViewBounds(boolean adjustViewBounds) {
        if (adjustViewBounds) {
            throw new IllegalArgumentException("adjustViewBounds not supported.");
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getDrawable() == null) {
            return;
        }

        canvas.drawCircle(getWidth() / 2, getHeight() / 2, mDR, bitmapPaint);
        if (bWidth != 0) {
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, mBR, bPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setup();
    }

    public int getBorderColor() {
        return bColor;
    }

    public void setBorderColor(int borderColor) {
        if (borderColor == bColor) {
            return;
        }

        bColor = borderColor;
        bPaint.setColor(bColor);
        invalidate();
    }

    public void setBorderColorResource(@ColorRes int borderColorRes) {
        setBorderColor(getContext().getResources().getColor(borderColorRes));
    }

    public int getBorderWidth() {
        return bWidth;
    }

    public void setBorderWidth(int borderWidth) {
        if (borderWidth == bWidth) {
            return;
        }

        bWidth = borderWidth;
        setup();
    }

    public boolean isBorderOverlay() {
        return mBO;
    }

    public void setBorderOverlay(boolean borderOverlay) {
        if (borderOverlay == mBO) {
            return;
        }

        mBO = borderOverlay;
        setup();
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        bitmap = bm;
        setup();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        bitmap = getBitmapFromDrawable(drawable);
        setup();
    }

    @Override
    public void setImageResource(@DrawableRes int resId) {
        super.setImageResource(resId);
        bitmap = getBitmapFromDrawable(getDrawable());
        setup();
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        bitmap = getBitmapFromDrawable(getDrawable());
        setup();
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        if (cf == mCF) {
            return;
        }

        mCF = cf;
        bitmapPaint.setColorFilter(mCF);
        invalidate();
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        try {
            Bitmap bitmap = null;

            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(COLOR_DIMENSION, COLOR_DIMENSION, BITMAP_CONFIG);
            } else {
                if (drawable.getIntrinsicWidth() > 0 && drawable.getIntrinsicHeight() > 0)
                    bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);
            }
            if (bitmap == null)
                return bitmap;
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

    private void setup() {
        if (!mR) {
            mSP = true;
            return;
        }

        if (bitmap == null) {
            return;
        }

        bShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        bitmapPaint.setAntiAlias(true);
        bitmapPaint.setShader(bShader);

        bPaint.setStyle(Paint.Style.STROKE);
        bPaint.setAntiAlias(true);
        bPaint.setColor(bColor);
        bPaint.setStrokeWidth(bWidth);

        bitmapHeight = bitmap.getHeight();
        bitmapWidth = bitmap.getWidth();

        bRect.set(0, 0, getWidth(), getHeight());
        mBR = Math.min((bRect.height() - bWidth) / 2, (bRect.width() - bWidth) / 2);

        dRect.set(bRect);
        if (!mBO) {
            dRect.inset(bWidth, bWidth);
        }
        mDR = Math.min(dRect.height() / 2, dRect.width() / 2);

        updateShaderMatrix();
        invalidate();
    }

    private void updateShaderMatrix() {
        float scale;
        float dx = 0;
        float dy = 0;

        sMatrix.set(null);

        if (bitmapWidth * dRect.height() > dRect.width() * bitmapHeight) {
            scale = dRect.height() / (float) bitmapHeight;
            dx = (dRect.width() - bitmapWidth * scale) * 0.5f;
        } else {
            scale = dRect.width() / (float) bitmapWidth;
            dy = (dRect.height() - bitmapHeight * scale) * 0.5f;
        }

        sMatrix.setScale(scale, scale);
        sMatrix.postTranslate((int) (dx + 0.5f) + dRect.left, (int) (dy + 0.5f) + dRect.top);

        bShader.setLocalMatrix(sMatrix);
    }

}
