package com.example.camerasurfaceview;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCaptureSession.CaptureCallback;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraDevice.StateCallback;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureRequest.Builder;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.FaceDetector;
import android.media.FaceDetector.Face;
import android.media.Image;
import android.media.ImageReader;
import android.media.ImageReader.OnImageAvailableListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Size;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends Activity{

    CameraManager mCameraManager;
    CameraDevice mCameraDevice;
    CameraCaptureSession mCameraCaptureSession;
    android.util.Size[] mPreviewSizes;
    android.util.Size[] mJpegSizes;
    android.util.Size mJpegSize;
    SurfaceView mSurfaceView;
    android.util.Size mPreviewSize;
    SurfaceHolder mSurfaceHolder;
    private Builder mPreviewBuilder;
    private String mOpenedCamera;
    private ImageReader mImageReader;
    Handler mBackgroundHandler;
    private TextView mFaceInfoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout layout = (LinearLayout) findViewById(R.id.preview_layout);
        Button captureButton = (Button) findViewById(R.id.button_capture);
        mFaceInfoTextView = (TextView) findViewById(R.id.face_info_view);
        mFaceInfoTextView.setText("Hello");
        captureButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                //take picture
                takePicture();


            }
        });

        Button faceInfoButton = (Button) findViewById(R.id.button_face_info);
        faceInfoButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //new RetrieveFaceInfoTask().execute();
                Intent displayFaceInfoIntent = new Intent(getApplicationContext(),
                        DisplayFaceInfoActivity.class);
                startActivity(displayFaceInfoIntent);
            }
        });
        mSurfaceView = (SurfaceView)findViewById(R.id.surface_preview);
        //mSurfaceView = new SurfaceView(getApplicationContext());
        mSurfaceHolder = mSurfaceView.getHolder();
        if(mSurfaceHolder == null) {
            Log.d("Emily", "mSurfaceHolder is null");
        }
        mSurfaceHolder.addCallback(new Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                Log.d("Emily", "onSurfaceCreated");
                openCamera();
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

            }
        });
    }

//    public class RetrieveFaceInfoTask extends AsyncTask<String, String, String> {
//        int mFaceCount;
//        ProgressDialog mProgressDialog;
//        Context mContext;
//        List<String> mFacesInfo;
//
//        @Override
//        protected String doInBackground(String... strings) {
//            Log.d("Emily", "Do in background...");
//            BitmapFactory.Options bitmap_options = new BitmapFactory.Options();
//            bitmap_options.inPreferredConfig = Config.RGB_565;
//            Bitmap photoToDetect = BitmapFactory.decodeFile(
//                    Environment.getExternalStorageDirectory() + "/DCIM/pic.jpg", bitmap_options);
//
//            FaceDetector faceDetector = new FaceDetector(photoToDetect.getWidth(),
//                    photoToDetect.getHeight(), 10); // 10 is the maximum number of faces
//            Face[] faces = new Face[10];
//
//            for(int i = 0; i < mFaceCount; i++){
//                Face f = faces[i];
//                String faceInfo = "confidence = " + f.confidence() + " Eye Distance = " + f
//                        .eyesDistance();
//                Log.d("Emily", "info = " + faceInfo);
//                mFacesInfo.add(faceInfo);
//
//            }
//            mFaceCount = faceDetector.findFaces(photoToDetect, faces);
//
//            return null;
//        }
//        /**
//         * After completing background task Dismiss the progress dialog
//         * **/
//        protected void onPostExecute(String file_url)  {
//
//            Log.d("Emily", "onPostExecute");
//            mFaceInfoTextView.setText("Face count = " + mFaceCount);
//           // Log.d("Emily", "FaceInfo = " + mFacesInfo.get(0));
//        }
//    }

    protected void takePicture() {
        if(mCameraDevice == null) {
            return;
        }
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(mCameraDevice
                    .getId());
            mJpegSizes = null;
            if(characteristics != null) {
                //Get all the available sizes for jpeg format
                mJpegSizes = characteristics.get(CameraCharacteristics
                        .SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.JPEG);
            }
            if(mJpegSizes != null || mJpegSizes.length >0) {
                //set the size for captured jpeg
                mJpegSize = mJpegSizes[0];
            }
            //Ste the width, height, format and maxi num image for this image reader
            mImageReader = ImageReader.newInstance(mJpegSize.getWidth(), mJpegSize.getHeight(),
                    ImageFormat.JPEG, 1);
            //Two output surfaces, one for preview and one for image
            List<Surface> outputSurface = new ArrayList<Surface>(2);
            outputSurface.add(mImageReader.getSurface());
            outputSurface.add(mSurfaceHolder.getSurface());

            //Capture request for still image capture
            final Builder captureBuilder = mCameraDevice.createCaptureRequest(CameraDevice
                    .TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(mImageReader.getSurface());
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);

            //Initialize the file where the image will  be stored
            final File file = new File(Environment.getExternalStorageDirectory()+"/DCIM",
                    "pic.jpg");

            ImageReader.OnImageAvailableListener readerListener = new OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader imageReader) {
                    Image image = null;
                    try {
                        image = imageReader.acquireLatestImage();
                        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                        byte[] bytes = new byte[buffer.capacity()];
                        buffer.get(bytes);
                        save(bytes);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if(image != null) {
                            image.close();
                        }
                    }
                }

                private void save(byte[] bytes) throws IOException{
                    OutputStream output = null;
                    output = new FileOutputStream(file);
                    output.write(bytes);
                    if(output != null) {
                        output.close();
                    }

                }
            };

            HandlerThread thread = new HandlerThread("CameraPicture");
            thread.start();

            final Handler backgroundHandler = new Handler(thread.getLooper());
            mImageReader.setOnImageAvailableListener(readerListener, backgroundHandler);
            final CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(CameraCaptureSession session,
                        CaptureRequest request, TotalCaptureResult result) {
                    super.onCaptureCompleted(session, request, result);
                    Toast.makeText(MainActivity.this, "Saved" + file, Toast.LENGTH_SHORT).show();
                    //After taking picture, we start preview again
                    // startPreview();
                    //ToDo: Start another activity and display the captured image and the
                    // information

                }
            };

            mCameraDevice.createCaptureSession(outputSurface, new CameraCaptureSession.StateCallback() {

                @Override
                public void onConfigured(CameraCaptureSession cameraCaptureSession) {
                    try {
                        cameraCaptureSession.capture(captureBuilder.build(), captureListener,
                                backgroundHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {

                                    }
            }, backgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    private void openCamera() {
        mCameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
        try {
            String[] camIds = mCameraManager.getCameraIdList();
            //Open the camera 0, in this case is the main camera
            String openCamId = camIds[0];
            mOpenedCamera = openCamId;
            CameraCharacteristics chara = mCameraManager.getCameraCharacteristics(openCamId);
            Size[] jpegSizes = null;
            if(chara != null) {
                StreamConfigurationMap configMap = chara.get(CameraCharacteristics
                        .SCALER_STREAM_CONFIGURATION_MAP);

                //Get previewSize
                mPreviewSizes = configMap.getOutputSizes(SurfaceTexture.class);
                mPreviewSize = mPreviewSizes[0];
            }

            if(mSurfaceHolder == null) {
                Log.d("Emily", "mSurfaceHolder == null");
                return;
            }
            Log.d("Emily", "mSurfaceHolder != null");
            mSurfaceHolder.setFixedSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            mCameraManager.openCamera(openCamId, mStateCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    //Here we should
    private void startPreview() {
        Log.d("Emily", "Start Preview");
        //TODO: CaptureRequest
        if(null == mCameraDevice || mPreviewSize == null) {
            Log.d("Emily", "CameraDevice is null");
            return;
        }
        //We get Surface from the SurfaceView
        Surface surface = mSurfaceHolder.getSurface();

        try {
            mPreviewBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        mPreviewBuilder.addTarget(surface);

        try {
            mCameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {

                @Override
                public void onConfigured(CameraCaptureSession cameraCaptureSession) {
                    mCameraCaptureSession = cameraCaptureSession;
                    updatePreview();
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {

                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }

    private void updatePreview() {
        Log.d("Emily", "update preview");
        if(mCameraDevice == null) {
            Log.d("Emily", "CameraDevice is null");
            return;
        }
        mPreviewBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        HandlerThread thread = new HandlerThread("CameraPreview");
        thread.start();
        Handler backgroundHandler = new Handler(thread.getLooper());
        mBackgroundHandler = backgroundHandler;
        try {
            mCameraCaptureSession.setRepeatingRequest(mPreviewBuilder.build(), null,
                    backgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume(){
        Log.d("Emily", "onResume");
        super.onResume();
        startPreview();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mCameraDevice != null) {
            mCameraDevice.close();
            mCameraDevice = null;
        }
    }

    private StateCallback mStateCallback = new StateCallback() {
        @Override
        public void onOpened(CameraDevice cameraDevice) {
            Log.d("Emily", "Camera is opened");
            mCameraDevice = cameraDevice;
            startPreview();
        }

        @Override
        public void onDisconnected(CameraDevice cameraDevice) {

        }

        @Override
        public void onError(CameraDevice cameraDevice, int i) {

        }
    };
}
