package vn.com.vng.modulesview.modules_view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Created by HungNQ on 08/09/2017.
 */

public class Save_ImageModule extends Module {

    /**
     * Options for scaling the bounds of an image to the bounds of this view.
     */
    public enum ScaleType {
        FIT_XY,
        FIT_CENTER,
        CENTER,
        CENTER_CROP,
        CENTER_INSIDE
    }

    public static final int ROUND_CIRCLE = -1;
    public static final int ROUND_NONE = 0;

    public Save_ImageModule() {
        init();
    }

    private void init() {
        mMatrix = new Matrix();
        mScaleType = ScaleType.FIT_CENTER;
        mAntiAliasPaint.setColor(0xff555555);
    }


    //stuff
    private Drawable mDrawable;
    private Matrix mDrawMatrix, mMatrix;
    private int mDrawableWidth, mDrawableHeight;
    private Bitmap mCachedBitmap;

    // Avoid allocations...
    private final RectF mTempSrc = new RectF();
    private final RectF mTempDst = new RectF();
    private final RectF mCLipRect = new RectF();
    private final Path mClipPath = new Path();
    private final Paint mAntiAliasPaint = new Paint(Paint.ANTI_ALIAS_FLAG);


    //properties
    private ScaleType mScaleType;
    private float mRoundCorner;
    private boolean mShouldCacheBitmap;

    //-------------getter & setter----------------------


    public ScaleType getScaleType() {
        return mScaleType;
    }

    public void setScaleType(ScaleType scaleType) {
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
            configureClipBounds();
        }
    }

    public void setBitmap(Bitmap bitmap) {
        setImageDrawable(new BitmapDrawable(mContext != null ? mContext.getResources() : null, bitmap));
    }

    public void setImageDrawable(Drawable drawable) {
        mDrawable = drawable;
        if (drawable != null) {
            mDrawableWidth = mDrawable.getIntrinsicWidth();
            mDrawableHeight = mDrawable.getIntrinsicHeight();
            configureImageBounds();
        } else {
            mDrawableWidth = -1;
            mDrawableHeight = -1;
        }
    }

    //-------------endregion------------------


    @Override
    public void configModule() {
        super.configModule();
        configureImageBounds();
        configureClipBounds();
    }


    //copy a part of ImageView.configureBounds(()
    private void configureImageBounds() {
        if (mDrawable == null) {
            return;
        }
        final int dwidth = mDrawableWidth;
        final int dheight = mDrawableHeight;

        final int vwidth = mBoundRight - mBoundLeft - mPaddingLeft - mPaddingRight;
        final int vheight = mBoundBottom - mBoundTop - mPaddingTop - mPaddingBottom;

        final boolean fits = (dwidth < 0 || vwidth == dwidth)
                && (dheight < 0 || vheight == dheight);

        if (dwidth <= 0 || dheight <= 0 || ScaleType.FIT_XY == mScaleType) {
            /* If the drawable has no intrinsic size, or we're told to
                scaletofit, then we just fill our entire view.
            */
            mDrawable.setBounds(0, 0, vwidth, vheight);
            mDrawMatrix = null;
        } else {
            // We need to do the scaling ourself, so have the drawable
            // use its native size.
            mDrawable.setBounds(0, 0, dwidth, dheight);

            if (fits) {
                // The bitmap fits exactly, no transform needed.
                mDrawMatrix = null;
            } else if (mScaleType == ScaleType.CENTER) {
                // Center bitmap in view, no scaling.
                mDrawMatrix = mMatrix;
                mDrawMatrix.setTranslate(Math.round((vwidth - dwidth) * 0.5f),
                        Math.round((vheight - dheight) * 0.5f));
            } else if (mScaleType == ScaleType.CENTER_CROP) {
                mDrawMatrix = mMatrix;

                float scale;
                float dx = 0, dy = 0;

                if (dwidth * vheight > vwidth * dheight) {
                    scale = (float) vheight / (float) dheight;
                    dx = (vwidth - dwidth * scale) * 0.5f;
                } else {
                    scale = (float) vwidth / (float) dwidth;
                    dy = (vheight - dheight * scale) * 0.5f;
                }

                mDrawMatrix.setScale(scale, scale);
                mDrawMatrix.postTranslate(Math.round(dx), Math.round(dy));
            } else if (mScaleType == ScaleType.CENTER_INSIDE) {
                mDrawMatrix = mMatrix;
                float scale;
                float dx;
                float dy;

                if (dwidth <= vwidth && dheight <= vheight) {
                    scale = 1.0f;
                } else {
                    scale = Math.min((float) vwidth / (float) dwidth,
                            (float) vheight / (float) dheight);
                }

                dx = Math.round((vwidth - dwidth * scale) * 0.5f);
                dy = Math.round((vheight - dheight * scale) * 0.5f);

                mDrawMatrix.setScale(scale, scale);
                mDrawMatrix.postTranslate(dx, dy);
            } else { //FIT_CENTER
                // Generate the required transform.
                mTempSrc.set(0, 0, dwidth, dheight);
                mTempDst.set(0, 0, vwidth, vheight);

                mDrawMatrix = mMatrix;
                mDrawMatrix.setRectToRect(mTempSrc, mTempDst, Matrix.ScaleToFit.CENTER);
            }
        }
    }

    private void configureClipBounds() {
        if (mDrawable != null) {
            mClipPath.reset();
            int width = mBoundRight - mBoundLeft - mPaddingRight - mPaddingLeft;
            int height = mBoundBottom - mBoundTop - mPaddingBottom - mPaddingTop;
            mCLipRect.set(0, 0, width, height);
            if (mRoundCorner == ROUND_CIRCLE) {
                float halfWidth = width / 2f;
                float halfHeight = height / 2f;
                float radius = Math.min(halfWidth, halfHeight);
                mClipPath.addCircle(halfWidth, halfHeight, radius, Path.Direction.CW);
            } else if (mRoundCorner > 0) {
                mClipPath.addRoundRect(mCLipRect, mRoundCorner, mRoundCorner, Path.Direction.CW);
            } else {
                //...
            }
        }
    }


    @Override
    protected void draw(Canvas canvas) {
        super.draw(canvas);

        if (mDrawable == null) {
            return;
        }
        if (mDrawableWidth == 0 || mDrawableHeight == 0) {
            return;
        }

        //NQH: this action maybe slow down the draw action
        canvas.save();

//        translate if needed
        int translateLeft = mBoundLeft + mPaddingLeft;
        int translateTop = mBoundTop + mPaddingTop;
        if (translateLeft > 0 || translateTop > 0)
            canvas.translate(translateLeft, translateTop);

        //anti alias if needed
        if (needAntiAlias()) {
//            canvas.save();
//            canvas.clipPath(mClipPath, Region.Op.DIFFERENCE);
            canvas.drawPath(mClipPath, mAntiAliasPaint);
//            canvas.restore();
        }

        //clip drawing region
        if (mClipPath.isEmpty())
            canvas.clipRect(mCLipRect);
        else
            canvas.clipPath(mClipPath);

        //draw canvas with matrix
        if (mDrawMatrix != null)
            canvas.concat(mDrawMatrix);

        mDrawable.draw(canvas);

        //NQH: this action will be slow down the draw action
        canvas.restore();






    }


    private boolean needAntiAlias() {
        return mRoundCorner > 0 || mRoundCorner == ROUND_CIRCLE;
    }

}
