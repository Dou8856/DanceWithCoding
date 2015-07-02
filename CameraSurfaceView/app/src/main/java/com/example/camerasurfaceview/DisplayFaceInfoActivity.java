package com.example.camerasurfaceview;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
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
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class DisplayFaceInfoActivity extends Activity {
    Bitmap mBackgroundImg;
    ImageView mBackgroundView;
    TextView mInformationView;
    ArrayList<String> mInformationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display_face_info);
        mInformationView = (TextView) findViewById(R.id.face_info_text);
        // Set Bitmap internal configuration to RGB_565
        Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inPreferredConfig = Config.RGB_565;
        mBackgroundImg = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() +
                "/DCIM/pic.jpg", bitmapOptions);
        mBackgroundView = (ImageView) findViewById(R.id.background_image);
        mBackgroundView.setImageBitmap(mBackgroundImg);
        //Start a task to detect faces
        new RetrieveFaceInfoTask().execute();
        mInformationList = new ArrayList<String>();

    }


    public class RetrieveFaceInfoTask extends AsyncTask<String, String, String> {

        int mFaceCount;
        private PointF midPoint = new PointF();
        private Paint drawPaint = new Paint();
        int MAX_FACE_NUM = 5;

        @Override
        protected String doInBackground(String... strings) {
            float eyeDistance = 0.0f;
            float confidence = 0.0f;
            FaceDetector faceDetector = new FaceDetector(mBackgroundImg.getWidth(),
                    mBackgroundImg.getHeight(), MAX_FACE_NUM);
            Face[] faces = new Face[MAX_FACE_NUM];
            mFaceCount = faceDetector.findFaces(mBackgroundImg, faces);
            if (mFaceCount > 0) {
                for (int i = 0; i < mFaceCount; i++) {
                    faces[i].getMidPoint(midPoint);
                    //Get eyeDistance on face[i]
                    eyeDistance = faces[i].eyesDistance();
                    confidence = faces[i].confidence();
                    String info = new String("FaceDetector" +
                            "Confidence: " + confidence +
                            ", Eye distance: " + eyeDistance +
                            ", Mid Point: (" + midPoint.x + ", " + midPoint.y + ")\n");
                    mInformationList.add(info);
                }
            }

            return null;
        }

        /**
         * After face detection is done, update the UI with displaying the detected face
         * information in string
        * */
        protected void onPostExecute(String file_url) {
            StringBuilder sb = new StringBuilder();
            //Do UI update
            if (mInformationList.size() == 0) {
                mInformationView.setText("Detected face number is " + mFaceCount);
            } else {
                for (int i = 0; i < mInformationList.size(); i++) {
                    sb.append(mInformationList.get(i));
                }
                mInformationView.setText(sb.toString());
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
