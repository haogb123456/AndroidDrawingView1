package com.vilyever.drawingview.brush.drawing;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.NonNull;

import com.vilyever.drawingview.model.VDDrawingPath;
import com.vilyever.drawingview.model.VDDrawingPoint;

/**
 * VDRoundedRectangleBrush
 * AndroidDrawingView <com.vilyever.drawingview.brush>
 * Created by vilyever on 2015/10/21.
 * Feature:
 * 圆角矩形绘制
 */
public class VDRoundedRectangleBrush extends VDShapeBrush {
    final VDRoundedRectangleBrush self = this;

    protected float roundRadius;

    /* Constructors */
    public VDRoundedRectangleBrush() {

    }

    public VDRoundedRectangleBrush(float size, int color) {
        this(size, color, FillType.Hollow);
    }

    public VDRoundedRectangleBrush(float size, int color, FillType fillType) {
        this(size, color, fillType, 20.0f);
    }

    public VDRoundedRectangleBrush(float size, int color, FillType fillType, float roundRadius) {
        this(size, color, fillType, false, roundRadius);
    }

    public VDRoundedRectangleBrush(float size, int color, FillType fillType, boolean edgeRounded, float roundRadius) {
        super(size, color, fillType, edgeRounded);
        this.roundRadius = roundRadius;
    }

    /* Public Methods */
    public static VDRoundedRectangleBrush defaultBrush() {
        return new VDRoundedRectangleBrush(5, Color.BLACK);
    }

    /* Properties */
    public float getRoundRadius() {
        return roundRadius;
    }

    public <T extends VDShapeBrush> T setRoundRadius(float roundRadius) {
        this.roundRadius = roundRadius;
        return (T) self;
    }

    /* Overrides */
    @Override
    public boolean isEdgeRounded() {
        return true;
    }

    @NonNull
    @Override
    public Frame drawPath(Canvas canvas, @NonNull VDDrawingPath drawingPath, @NonNull DrawingState state) {
        if (drawingPath.getPoints().size() > 1) {
            VDDrawingPoint beginPoint = drawingPath.getPoints().get(0);
            VDDrawingPoint lastPoint = drawingPath.getPoints().get(drawingPath.getPoints().size() - 1);

            RectF drawingRect = new RectF();
            drawingRect.left = Math.min(beginPoint.getX(), lastPoint.getX());
            drawingRect.top = Math.min(beginPoint.getY(), lastPoint.getY());
            drawingRect.right = Math.max(beginPoint.getX(), lastPoint.getX());
            drawingRect.bottom = Math.max(beginPoint.getY(), lastPoint.getY());

            if ((drawingRect.right - drawingRect.left) < self.getSize()
                    || (drawingRect.bottom - drawingRect.top) < self.getSize()) {
                return Frame.EmptyFrame();
            }

            Frame pathFrame = self.makeFrameWithBrushSpace(drawingRect);

            if (state.isFetchFrame() || canvas == null) {
                return pathFrame;
            }

            float round = self.getRoundRadius() + self.getSize() / 2.0f;
            Path path = new Path();
            path.addRoundRect(drawingRect, round, round, Path.Direction.CW);

            if (state.isCalibrateToOrigin()) {
                path.offset(-pathFrame.left, -pathFrame.top);
            }

            canvas.drawPath(path, self.getPaint());

            return pathFrame;
        }

        return Frame.EmptyFrame();
    }
    
}