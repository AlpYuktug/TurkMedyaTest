package com.example.alperenyukselaltugtest;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

class LinePagerIndicatorDecoration extends RecyclerView.ItemDecoration {

        public int colorActive = 0xFFFFFFFF;
        public int colorInactive = 0x66FFFFFF;

    public static final float DP = Resources.getSystem().getDisplayMetrics().density;

    public final int mIndicatorHeight = (int) (DP * 16);


    public final float mIndicatorStrokeWidth = DP * 2;


    public final float mIndicatorItemLength = DP * 16;

    public final float mIndicatorItemPadding = DP * 4;


    public final Interpolator mInterpolator = new AccelerateDecelerateInterpolator();

    public final Paint mPaint = new Paint();

        public LinePagerIndicatorDecoration() {
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            mPaint.setStrokeWidth(mIndicatorStrokeWidth);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setAntiAlias(true);
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            super.onDrawOver(c, parent, state);

            int itemCount = parent.getAdapter().getItemCount();

            float totalLength = mIndicatorItemLength * itemCount;
            float paddingBetweenItems = Math.max(0, itemCount - 1) * mIndicatorItemPadding;
            float indicatorTotalWidth = totalLength + paddingBetweenItems;
            float indicatorStartX = (parent.getWidth() - indicatorTotalWidth) / 2F;

            float indicatorPosY = parent.getHeight() - mIndicatorHeight / 2F;

            drawInactiveIndicators(c, indicatorStartX, indicatorPosY, itemCount);


            LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
            int activePosition = layoutManager.findFirstVisibleItemPosition();
            if (activePosition == RecyclerView.NO_POSITION) {
                return;
            }

            final View activeChild = layoutManager.findViewByPosition(activePosition);
            int left = activeChild.getLeft();
            int width = activeChild.getWidth();

            float progress = mInterpolator.getInterpolation(left * -1 / (float) width);

            drawHighlights(c, indicatorStartX, indicatorPosY, activePosition, progress, itemCount);
        }

    public void drawInactiveIndicators(Canvas c, float indicatorStartX, float indicatorPosY, int itemCount) {
            mPaint.setColor(colorInactive);

            final float itemWidth = mIndicatorItemLength + mIndicatorItemPadding;

            float start = indicatorStartX;
            for (int i = 0; i < itemCount; i++) {
                c.drawLine(start, indicatorPosY, start + mIndicatorItemLength, indicatorPosY, mPaint);
                start += itemWidth;
            }
        }

    public void drawHighlights(Canvas c, float indicatorStartX, float indicatorPosY,
                                    int highlightPosition, float progress, int itemCount) {
            mPaint.setColor(colorActive);

            final float itemWidth = mIndicatorItemLength + mIndicatorItemPadding;

            if (progress == 0F) {
                float highlightStart = indicatorStartX + itemWidth * highlightPosition;
                c.drawLine(highlightStart, indicatorPosY,
                        highlightStart + mIndicatorItemLength, indicatorPosY, mPaint);
            } else {
                float highlightStart = indicatorStartX + itemWidth * highlightPosition;
                float partialLength = mIndicatorItemLength * progress;

                c.drawLine(highlightStart + partialLength, indicatorPosY,
                        highlightStart + mIndicatorItemLength, indicatorPosY, mPaint);

                if (highlightPosition < itemCount - 1) {
                    highlightStart += itemWidth;
                    c.drawLine(highlightStart, indicatorPosY,
                            highlightStart + partialLength, indicatorPosY, mPaint);
                }
            }
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.bottom = mIndicatorHeight;
        }
}
