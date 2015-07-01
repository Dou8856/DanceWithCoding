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
import android.util.Log;
import android.view.View;

/**
 * Created by 23060814 on 7/1/15.
 */
public class FaceDetectionView extends View {
    private static final int MAX_FACE = 10;
    private static final String IMG = "pic.jpg";
    private Bitmap mBackgroundImg;
    private Face[] mFaces;
    private int mFaceCount;

    //Preallocate on draw
    private PointF tmp_point = new PointF();
    private Paint tmp_paint = new Paint();
    public FaceDetectionView(Context context) {
        super(context);

       getImage(Environment.getExternalStorageDirectory()+ "/DCIM/pic.jpg");
        //getImage(Environment.getExternalStorageDirectory()+"/DCIM/100ANDRO/DSC_0002.JPG");
    }


    public void getImage(String img) {
        // Set internal configuration
        BitmapFactory.Options bitmap_options = new BitmapFactory.Options();
        bitmap_options.inPreferredConfig = Config.RGB_565;

        mBackgroundImg = BitmapFactory.decodeFile(img, bitmap_options);
        FaceDetector faceDetector = new FaceDetector(mBackgroundImg.getWidth(),
                mBackgroundImg.getHeight(), MAX_FACE);
        mFaces = new Face[MAX_FACE];

        mFaceCount = faceDetector.findFaces(mBackgroundImg, mFaces);
        Log.d("Emily", "Face count = " + mFaceCount);
    }

    public void onDraw(Canvas canvas) {
        canvas.drawBitmap(mBackgroundImg,0, 500, tmp_paint);
        for(int i = 0; i < mFaceCount; i++) {
            FaceDetector.Face face = mFaces[i];
            tmp_paint.setColor(Color.RED);
            tmp_paint.setAlpha(100);
            face.getMidPoint(tmp_point);
            canvas.drawCircle(tmp_point.x, tmp_point.y, face.eyesDistance(), tmp_paint);

        }
    }
}
