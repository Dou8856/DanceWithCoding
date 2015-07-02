package com.example.camerasurfaceview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.media.FaceDetector;
import android.media.FaceDetector.Face;
import android.os.Environment;
import android.view.View;

import java.util.List;

/*
*TODO: Delete this file
* */
/**
 * Created by 23060814 on 7/1/15.
 */
public class FaceDetectionView extends View {
    private static final int MAX_FACE_NUM = 5;
    private Bitmap mBackgroundBitmap;
    private Face[] mFaces;
    private int mFaceCount;
    private List<String> mInformationList;

    //Preallocate on draw
    PointF midPoint = new PointF();
    Paint drawPaint = new Paint();

    public FaceDetectionView(Context context) {
        super(context);
       getImage(Environment.getExternalStorageDirectory()+ "/DCIM/pic.jpg");
    }

    public void getImage(String filePath) {
        // Set internal configuration
        BitmapFactory.Options bitmap_options = new BitmapFactory.Options();
        bitmap_options.inPreferredConfig = Config.RGB_565;
        mBackgroundBitmap = BitmapFactory.decodeFile(filePath, bitmap_options);
        FaceDetector faceDetector = new FaceDetector(mBackgroundBitmap.getWidth(),
                mBackgroundBitmap.getHeight(), MAX_FACE_NUM);
        mFaces = new Face[MAX_FACE_NUM];
        mFaceCount = faceDetector.findFaces(mBackgroundBitmap, mFaces);
    }

    public void onDraw(Canvas canvas) {
        float eyeDistance = 0.0f;
        float confidence = 0.0f;
        canvas.drawBitmap(mBackgroundBitmap,0, 0, null);
        for(int i = 0; i < mFaceCount; i++) {
            FaceDetector.Face face = mFaces[i];
            drawPaint.setColor(Color.RED);
            drawPaint.setAlpha(100);
            face.getMidPoint(midPoint);
            String info = new String("FaceDetector"+
                    "Confidence: " + confidence +
                    ", Eye distance: " + eyeDistance +
                    ", Mid Point: (" + midPoint.x + ", " + midPoint.y + ")");
            mInformationList.add(info);
            //canvas.drawCircle(mPoint.x, mPoint.y, face.eyesDistance(),mPaint);
            canvas.drawRect((int)midPoint.x - eyeDistance ,
                    (int)midPoint.y - eyeDistance ,
                    (int)midPoint.x + eyeDistance,
                    (int)midPoint.y + eyeDistance, drawPaint);
        }
    }
}
