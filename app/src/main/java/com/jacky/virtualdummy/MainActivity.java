package com.jacky.virtualdummy;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.media.CamcorderProfile;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.BaseArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final double MIN_OPENGL_VERSION = 3.0; //using emulator

    private WritingArFragment arFragment;
    private ModelRenderable couchRenderable, setRenderable, set1Renderable, lampRenderable, lamp1Renderable;
    private ModelLoader modelLoader; //avoid leaking the activity's context
    private VideoRecorder videoRecorder; // enables videos to be recorded. Gives functionality
    private FloatingActionButton recordButton; //UI
    private Uri selectedObject;


    @BindView(R.id.couch)
    ImageView couch;
    @BindView(R.id.set)
    ImageView modell;
    @BindView(R.id.set1)
    ImageView sofa_01;

    View arrayView[];
    int selected = 1; //default couch



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        arFragment = (WritingArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment); //ARFRAGMENT HOLDS PERMISSION TO RUN ARCORE FEATURES

        setArrayView();
       setClickListener();
        setupModel();




        arFragment.setOnTapArPlaneListener(new BaseArFragment.OnTapArPlaneListener() {
            @Override
            public void onTapPlane(HitResult hitResult, Plane plane, MotionEvent motionEvent) {

                    Anchor anchor = hitResult.createAnchor();
                    AnchorNode anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(arFragment.getArSceneView().getScene());
                    createModel(anchorNode, selected);
                }
        });

        // Initialize the VideoRecorder.
        videoRecorder = new VideoRecorder();
        int orientation = getResources().getConfiguration().orientation;
        videoRecorder.setVideoQuality(CamcorderProfile.QUALITY_2160P, orientation);
        videoRecorder.setSceneView(arFragment.getArSceneView());

        recordButton = findViewById(R.id.record);
        recordButton.setOnClickListener(this::toggleRecording);
        recordButton.setEnabled(true);
        recordButton.setImageResource(R.drawable.baseline_videocam_white_24);
        


    }

    private void setupModel() {

        ModelRenderable.builder()
                .setSource(this, R.raw.couch)
                .build()
                .thenAccept(renderable -> couchRenderable = renderable)
                .exceptionally(throwable -> {
                    Toast toast =
                            Toast.makeText(this, "Unable to load chair renderable", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return null;
                });

        ModelRenderable.builder()
                .setSource(this, R.raw.modell)
                .build()
                .thenAccept(renderable -> lampRenderable = renderable)
                .exceptionally(throwable -> {
                    Toast toast =
                            Toast.makeText(this, "Unable to load lamp renderable", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return null;
                });

        ModelRenderable.builder()
                .setSource(this, R.raw.sofa_01)
                .build()
                .thenAccept(renderable -> setRenderable = renderable)
                .exceptionally(throwable -> {
                    Toast toast =
                            Toast.makeText(this, "Unable to load sofa renderable", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return null;
                });


    }


    private void createModel(AnchorNode anchorNode, int selected) {
        if ( selected == 1){

            TransformableNode couch = new TransformableNode(arFragment.getTransformationSystem()); //MAKES MODEL TO ROTATE AND MOVE
            couch.setParent(anchorNode);
            couch.setRenderable(couchRenderable);
            couch.select();

        }

        else if ( selected == 2)
        {
            TransformableNode sofa1 = new TransformableNode(arFragment.getTransformationSystem()); //MAKES MODEL TO ROTATE AND MOVE
            sofa1.setParent(anchorNode);
            sofa1.setRenderable(setRenderable);
            sofa1.select();

        }

        else if ( selected == 3)
        {
            TransformableNode sofa2 = new TransformableNode(arFragment.getTransformationSystem()); //MAKES MODEL TO ROTATE AND MOVE
            sofa2.setParent(anchorNode);
            sofa2.setRenderable(set1Renderable);
            sofa2.select();

        }

    }

    private void setClickListener() {
        for (int i=0;i<arrayView.length;i++){
            arrayView[i].setOnClickListener(this);
        }
   }


  private void  setArrayView() {
        arrayView = new View[]{couch, modell, sofa_01};
  }

    @Override
    protected void onPause() {
        if (videoRecorder.isRecording()) {
            toggleRecording(null);
        }
        super.onPause();
    }


    private void toggleRecording(View unusedView) {
        if (!arFragment.hasWritePermission()) {
            Log.e(TAG, "Video recording requires the WRITE_EXTERNAL_STORAGE permission");
            Toast.makeText(
                    this,
                    "Video recording requires the WRITE_EXTERNAL_STORAGE permission",
                    Toast.LENGTH_LONG)
                    .show();
            arFragment.launchPermissionSettings();
            return;
        }
        boolean recording = videoRecorder.onToggleRecord();
        if (recording) {
            recordButton.setImageResource(R.drawable.baseline_videocam_off_white_24);
        } else {
            recordButton.setImageResource(R.drawable.baseline_videocam_white_24);
            String videoPath = videoRecorder.getVideoPath().getAbsolutePath();
            Toast.makeText(this, "Video saved: " + videoPath, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Video saved: " + videoPath);

            // Send  notification of updated content.
            ContentValues values = new ContentValues();
            values.put(MediaStore.Video.Media.TITLE, "Sceneform Video");
            values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
            values.put(MediaStore.Video.Media.DATA, videoPath);
            getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
        }
    }


    public static boolean checkIsSupportedDeviceOrFinish(final Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Log.e(TAG, "Sceneform requires Android N or later");
            Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG).show();
            activity.finish();
            return false;
        }
        String openGlVersionString =
                ((ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE))
                        .getDeviceConfigurationInfo()
                        .getGlEsVersion();
        if (Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
            Log.e(TAG, "Sceneform requires OpenGL ES 3.0 later");
            Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
                    .show();
            activity.finish();
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.couch){
            Log.d("selected","one");
            selected=1;

        }
        else if(v.getId()==R.id.set){
            Log.d("selected","two");
            selected=2;

        }
        else if(v.getId()==R.id.set1){
            Log.d("selected","three");
            selected=3;

        }




    }
    }
