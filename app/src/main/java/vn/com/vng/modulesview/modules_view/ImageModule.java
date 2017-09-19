package vn.com.vng.modulesview.modules_view;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by HungNQ on 08/09/2017.
 */

public class ImageModule extends Module {
    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
    private static final int COLORDRAWABLE_DIMENSION = 1;

    /**
     * Options for scaling the bounds of an image to the bounds of this view.
     */

    public static final int FIT_CENTER = 0;
    public static final int FIT_XY = 2;
    public static final int CENTER = 3;
    public static final int CENTER_CROP = 4;
    public static final int CENTER_INSIDE = 5;

    @IntDef({FIT_CENTER, FIT_XY, CENTER, CENTER_CROP, CENTER_INSIDE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ScaleType {
    }

    public static final int ROUND_CIRCLE = -1;
    public static final int ROUND_NONE = 0;

    public ImageModule() {
        init();
    }

    private void init() {
    }


    //----------stuff-------------------------
    private Matrix mDrawMatrix;
    private Matrix mMatrix = new Matrix();
    private final Paint mBitmapPaint = new Paint();
    private BitmapShader mBitmapShader;

    //draw region
    private int mDrawWidth, mDrawHeight;
    private int mDrawTranslateX, mDrawTranslateY;
    private final RectF mCLipRect = new RectF();
    private final Path mClipPath = new Path();

    //properties
    private Bitmap mBitmap;
    private
    @ScaleType
    int mScaleType;
    private float mRoundCorner;


    //-------------getter & setter----------------------


    public
    @ScaleType
    int getScaleType() {
        return mScaleType;
    }

    public void setScaleType(@ScaleType int scaleType) {
        if (mScaleType != scaleType) {
            mScaleType = scaleType;
            configureImageBounds();
        }
    }

    public float getRoundCorner() {
        return mRoundCorner;
    }

    public void setRoundCorner(float roundCorner) {
        if (mRoundCorner != roundCorner) {
            mRoundCorner = roundCorner;
            configureDrawRegionPath();
        }
    }

    public void setImageDrawable(Drawable drawable) {
        setBitmap(getBitmapFromDrawable(drawable));
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
        if(mBitmap != null) {
            mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            mBitmapPaint.setShader(mBitmapShader);
        }else{
            mBitmapShader = null;
            mBitmapPaint.setShader(null);
        }
    }

    //-------------endregion------------------


    @Override
    public void configModule() {
        super.configModule();
        configureImageBounds();
        configureDrawRegionPath();
        configureBitmapPaint();
    }


    //copy a part of ImageView.configureBounds(()
    private void configureImageBounds() {
        if (mBitmap == null) {
            return;
        }

        final int iWidth = mBitmap.getWidth();
        final int iHeight = mBitmap.getHeight();

        final int vWidth = mBoundRight - mBoundLeft - mPaddingLeft - mPaddingRight;
        final int vHeight = mBoundBottom - mBoundTop - mPaddingTop - mPaddingBottom;

        final boolean fits = vWidth == iWidth && vHeight == iHeight;

        if (FIT_XY == mScaleType) {
            /* If the drawable has no intrinsic size, or we're told to
                scaletofit, then we just fill our entire view.
            */
            mDrawMatrix = mMatrix;
            float scaleX = vWidth / (float) iWidth;
            float scaleY = vHeight / (float) iHeight;
            mDrawMatrix.setScale(scaleX, scaleY);

            //determine draw region
            mDrawWidth = vWidth;
            mDrawHeight = vHeight;
            mDrawTranslateX = mPaddingLeft;
            mDrawTranslateY = mPaddingTop;

        } else {
            // We need to do the scaling ourself, so have the drawable
            // use its native size.
            if (fits) {
                // The bitmap fits exactly, no transform needed.
                mDrawMatrix = null;

                //determine draw region
                mDrawWidth = vWidth;
                mDrawHeight = vHeight;
                mDrawTranslateX = mPaddingLeft;
                mDrawTranslateY = mPaddingTop;
            } else if (mScaleType == CENTER) {
                // Center bitmap in view, no scaling.
                mDrawMatrix = mMatrix;
                mDrawMatrix.setTranslate(Math.round((vWidth - iWidth) * 0.5f),
                        Math.round((vHeight - iHeight) * 0.5f));

                //determine draw region
                mDrawWidth = vWidth;
                mDrawHeight = vHeight;
                mDrawTranslateX = mPaddingLeft;
                mDrawTranslateY = mPaddingTop;

            } else if (mScaleType == CENTER_CROP) {
                mDrawMatrix = mMatrix;

                float scale;
                float dx = 0, dy = 0;

                if (iWidth * vHeight > vWidth * iHeight) {
                    scale = (float) vHeight / (float) iHeight;
                    dx = (vWidth - iWidth * scale) * 0.5f;
                } else {
                    scale = (float) vWidth / (float) iWidth;
                    dy = (vHeight - iHeight * scale) * 0.5f;
                }

                mDrawMatrix.setScale(scale, scale);
                mDrawMatrix.postTranslate(Math.round(dx), Math.round(dy));

                //determine draw region
                mDrawWidth = vWidth;
                mDrawHeight = vHeight;
                mDrawTranslateX = mPaddingLeft;
                mDrawTranslateY = mPaddingTop;

            } else if (mScaleType == CENTER_INSIDE) {
                mDrawMatrix = mMatrix;
                float scale;
                float dx;
                float dy;

                if (iWidth <= vWidth && iHeight <= vHeight) {
                    scale = 1.0f;
                } else {
                    scale = Math.min((float) vWidth / (float) iWidth,
                            (float) vHeight / (float) iHeight);
                }

                dx = Math.round((vWidth - iWidth * scale) * 0.5f);
                dy = Math.round((vHeight - iHeight * scale) * 0.5f);

                mDrawMatrix.setScale(scale, scale);
//                mDrawMatrix.postTranslate(dx, dy);

                //determine draw region
                mDrawWidth = (int) (iWidth * scale);
                mDrawHeight = (int) (iHeight * scale);
                mDrawTranslateX = mPaddingLeft + (int) dx;
                mDrawTranslateY = mPaddingTop + (int) dy;
            } else { //FIT_CENTER
                // Generate the required transform.
                float scale = Math.min((float) vWidth / (float) iWidth,
                        (float) vHeight / (float) iHeight);
                int dx = Math.round((vWidth - iWidth * scale) * 0.5f);
                int dy = Math.round((vHeight - iHeight * scale) * 0.5f);

                mDrawMatrix = mMatrix;
                mDrawMatrix.setScale(scale, scale);
//                mDrawMatrix.postTranslate(dx, dy);

                //determine draw region
                mDrawWidth = (int) (iWidth * scale);
                mDrawHeight = (int) (iHeight * scale);
                mDrawTranslateX = mPaddingLeft + dx;
                mDrawTranslateY = mPaddingTop + dy;
            }
        }
    }

    private void configureDrawRegionPath() {
        if (mBitmap != null) {
            mClipPath.reset();
            if (mRoundCorner == ROUND_CIRCLE) {
                float halfWidth = mDrawWidth / 2f;
                float halfHeight = mDrawHeight / 2f;
                float radius = Math.min(halfWidth, halfHeight);
                mClipPath.addCircle(halfWidth, halfHeight, radius, Path.Direction.CW);
            } else if (mRoundCorner > 0) {
                mCLipRect.set(0, 0, mDrawWidth, mDrawHeight);
                mClipPath.addRoundRect(mCLipRect, mRoundCorner, mRoundCorner, Path.Direction.CW);
            } else {
                mCLipRect.set(0, 0, mDrawWidth, mDrawHeight);
                mClipPath.addRect(mCLipRect, Path.Direction.CW);
            }
        }
    }


    private void configureBitmapPaint() {
        if (mBitmap != null) {
            if (mDrawMatrix != null)
                mBitmapShader.setLocalMatrix(mDrawMatrix);
            mBitmapPaint.setAntiAlias(needToAntialias());
        }
    }

    private boolean needToAntialias() {
        return mRoundCorner != ROUND_NONE;
    }


    @Override
    protected void draw(Canvas canvas) {
        super.draw(canvas);

        if (mBitmap == null) {
            return;
        }
        if (mBitmap.getHeight() == 0 || mBitmap.getHeight() == 0) {
            return;
        }

//        translate if needed
        int translateLeft = mBoundLeft + mDrawTranslateX;
        int translateTop = mBoundTop + mDrawTranslateY;
        boolean needToSave = translateLeft > 0 || translateTop > 0;

        if (needToSave) {
            canvas.save();
            canvas.translate(translateLeft, translateTop);
        }

        canvas.drawPath(mClipPath, mBitmapPaint);

        if (needToSave)
            canvas.restore();
    }


    private boolean needAntiAlias() {
        return mRoundCorner > 0 || mRoundCorner == ROUND_CIRCLE;
    }


    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        try {
            Bitmap bitmap;
            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION, COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);
            }

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
