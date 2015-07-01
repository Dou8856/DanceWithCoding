package com.example.camerasurfaceview;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;


public class DisplayFaceInfoActivity extends Activity {

    Bitmap mPhotoToDetect;
    Face[] mFaces;
    int mFaceCount;
    TextView mFaceInfoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(new FaceDetectionView(this));
        ImageView backgroundView = (ImageView) findViewById(R.id.background_view);
        Bitmap backgroundImg = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+ "/DCIM/pic.jpg");
        backgroundView.setImageBitmap(backgroundImg);
    }

    private class FaceDetectionView extends View {
        float eyeDistance;
        private int mImgHeight;
        private int mImgWidth;
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
            bitmap_options.inScaled=false;

            mBackgroundImg = BitmapFactory.decodeFile(img, bitmap_options);
            FaceDetector faceDetector = new FaceDetector(mBackgroundImg.getWidth(),
                    mBackgroundImg.getHeight(), MAX_FACE);
            mFaces = new Face[MAX_FACE];


            mFaceCount = faceDetector.findFaces(mBackgroundImg, mFaces);
            Log.d("Emily", "Face count = " + mFaceCount);
        }

        public void onDraw(Canvas canvas) {
            //canvas.drawBitmap(mBackgroundImg,0, 0, null);
            for(int i = 0; i < mFaceCount; i++) {
                FaceDetector.Face face = mFaces[i];
                tmp_paint.setColor(Color.RED);
                tmp_paint.setAlpha(100);
                face.getMidPoint(tmp_point);
                canvas.drawCircle(tmp_point.x, tmp_point.y, face.eyesDistance(), tmp_paint);

            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_face_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
