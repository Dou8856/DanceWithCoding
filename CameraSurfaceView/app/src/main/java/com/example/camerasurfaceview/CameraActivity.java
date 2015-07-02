package com.example.camerasurfaceview;

import android.app.Activity;
import android.content.Intent;
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
import android.media.Image;
import android.media.ImageReader;
import android.media.ImageReader.OnImageAvailableListener;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CameraActivity extends Activity{

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
    private ImageReader mImageReader;
    Handler mBackgroundHandler;
    CameraCharacteristics mCameraCharacteristics;

    private StateCallback mStateCallback = new StateCallback() {
        @Override
        public void onOpened(CameraDevice cameraDevice) {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Button for capturing image
        Button captureButton = (Button) findViewById(R.id.button_capture);
        captureButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                //take picture
                takePicture();
            }
        });

        //Button for start DisplayFaceInfoActivity
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

        //Create SurfaceView to hold the camera preview
        mSurfaceView = (SurfaceView)findViewById(R.id.surface_preview);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(new Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                //Once the surface is created, open the camera
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

    //takePicture function is called when capture button is clicked
    protected void takePicture() {
        if(mCameraDevice == null) {
            return;
        }
        try {
            mJpegSizes = null;
            if(mCameraCharacteristics != null) {
                //Get all the available sizes for jpeg format
                mJpegSizes = mCameraCharacteristics.get(CameraCharacteristics
                        .SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.JPEG);
            }
            if(mJpegSizes != null || mJpegSizes.length >0) {
                //set the size for captured jpeg
                mJpegSize = mJpegSizes[0];
            }

            //Set the width, height, format and max number of images for this image reader
            //Here we only allow 1 image each time
            mImageReader = ImageReader.newInstance(mJpegSize.getWidth(), mJpegSize.getHeight(),
                    ImageFormat.JPEG, 1);

            List<Surface> outputSurface = new ArrayList<Surface>(2);
            outputSurface.add(mImageReader.getSurface());
            outputSurface.add(mSurfaceHolder.getSurface());

            //Create a CaptureRequest
            final Builder captureBuilder = mCameraDevice.createCaptureRequest(CameraDevice
                    .TEMPLATE_STILL_CAPTURE);

            //CaptureRequest setup
            captureBuilder.addTarget(mImageReader.getSurface());
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);

            //Initialize the file where the image will  be stored
            final File file = new File(Environment.getExternalStorageDirectory()+"/DCIM",
                    "pic.jpg");

            // Create listener when image is available
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
            //Create CaptureCallback listener whe the capture is completed
            final CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(CameraCaptureSession session,
                        CaptureRequest request, TotalCaptureResult result) {
                    super.onCaptureCompleted(session, request, result);
                    Toast.makeText(CameraActivity.this, "Saved" + file, Toast.LENGTH_SHORT).show();
                    //After taking picture, we start preview again
                    startPreview();

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


    private void openCamera() {
        mCameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
        try {
            //Get list of available cameras
            String[] camIds = mCameraManager.getCameraIdList();
            //Open the camera 0, in this case is the main camera
            String openCamId = camIds[0];
            //Get the CameraCharacteristics, and find the best suit your need.
            //Here we just simply choose the size for preview
            mCameraCharacteristics = mCameraManager.getCameraCharacteristics(openCamId);
            if(mCameraCharacteristics != null) {
                StreamConfigurationMap configMap = mCameraCharacteristics.get(CameraCharacteristics
                        .SCALER_STREAM_CONFIGURATION_MAP);

                //Get previewSize
                mPreviewSizes = configMap.getOutputSizes(SurfaceTexture.class);
                mPreviewSize = mPreviewSizes[0];
            }

            if(mSurfaceHolder == null) {
                return;
            }

            //Set the size for mSurfaceHolder
            mSurfaceHolder.setFixedSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            mCameraManager.openCamera(openCamId, mStateCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void startPreview() {
        if(null == mCameraDevice || mPreviewSize == null) {
            return;
        }
        //Get Surface
        Surface surface = mSurfaceHolder.getSurface();

        try {
            //Create a CaptureRequest
            mPreviewBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        //Add the surface as target
        mPreviewBuilder.addTarget(surface);

        try {
            //Create a capture session and update preview
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
        if(mCameraDevice == null) {
            return;
        }
        mPreviewBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        HandlerThread thread = new HandlerThread("CameraPreview");
        thread.start();
        Handler backgroundHandler = new Handler(thread.getLooper());
        mBackgroundHandler = backgroundHandler;
        try {
            //Update preview by set a repeating request to mCameraCaptureSession
            mCameraCaptureSession.setRepeatingRequest(mPreviewBuilder.build(), null,
                    backgroundHandler);
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

    @Override
    public void onResume(){
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
}
