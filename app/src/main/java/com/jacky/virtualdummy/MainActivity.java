package com.jacky.virtualdummy;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.math.Vector3;
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
    private ModelRenderable couchRenderable, setRenderable, set1Renderable, lampRenderable, lamp1Renderable, bedRenderable, counterRenderable, tableRenderable,
    soaf3Renderable, sofa4Renderable, sofa5Renderable, sofa6Renderable, sofa7Renderable, sofa8Renderable, coffeeRenderable;
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
    @BindView(R.id.lamp1)
    ImageView lamp1;
    @BindView(R.id.lamp2)
    ImageView lamp2;
    @BindView(R.id.bed)
    ImageView bed;
    @BindView(R.id.counter)
    ImageView counter;
    @BindView(R.id.coffee)
    ImageView coffee;
    @BindView(R.id.red3)
    ImageView sofa3;
    @BindView(R.id.sofa4)
    ImageView sofa4;
    @BindView(R.id.sofa5)
    ImageView sofa5;
    @BindView(R.id.sofa6)
    ImageView sofa6;
    @BindView(R.id.sofa7)
    ImageView sofa7;
    @BindView(R.id.sofa8)
    ImageView sofa8;
    @BindView(R.id.image)
    ImageView image;


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

        image.setOnClickListener(this);


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
                .thenAccept(renderable -> set1Renderable = renderable)
                .exceptionally(throwable -> {
                    Toast toast =
                            Toast.makeText(this, "Unable to load lamp renderable", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return null;
                });

        ModelRenderable.builder()
                .setSource(this, R.raw.armchair_01)
                .build()
                .thenAccept(renderable -> setRenderable = renderable)
                .exceptionally(throwable -> {
                    Toast toast =
                            Toast.makeText(this, "Unable to load sofa renderable", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.lamp)
                .build()
                .thenAccept(renderable -> lampRenderable = renderable)
                .exceptionally(throwable -> {
                    Toast toast =
                            Toast.makeText(this, "Unable to load sofa renderable", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.lamppost)
                .build()
                .thenAccept(renderable -> lamp1Renderable = renderable)
                .exceptionally(throwable -> {
                    Toast toast =
                            Toast.makeText(this, "Unable to load sofa renderable", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return null;
                });

        ModelRenderable.builder()
                .setSource(this, R.raw.bedroom)
                .build()
                .thenAccept(renderable -> bedRenderable = renderable)
                .exceptionally(throwable -> {
                    Toast toast =
                            Toast.makeText(this, "Unable to load sofa renderable", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return null;
                });

        ModelRenderable.builder()
                .setSource(this, R.raw.model)
                .build()
                .thenAccept(renderable -> counterRenderable = renderable)
                .exceptionally(throwable -> {
                    Toast toast =
                            Toast.makeText(this, "Unable to load sofa renderable", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.desk)
                .build()
                .thenAccept(renderable -> tableRenderable = renderable)
                .exceptionally(throwable -> {
                    Toast toast =
                            Toast.makeText(this, "Unable to load sofa renderable", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.soaf3)
                .build()
                .thenAccept(renderable -> soaf3Renderable = renderable)
                .exceptionally(throwable -> {
                    Toast toast =
                            Toast.makeText(this, "Unable to load sofa renderable", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.sofa4)
                .build()
                .thenAccept(renderable -> sofa4Renderable = renderable)
                .exceptionally(throwable -> {
                    Toast toast =
                            Toast.makeText(this, "Unable to load sofa renderable", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return null;
                });

        ModelRenderable.builder()
                .setSource(this, R.raw.sofa5)
                .build()
                .thenAccept(renderable -> sofa5Renderable = renderable)
                .exceptionally(throwable -> {
                    Toast toast =
                            Toast.makeText(this, "Unable to load sofa renderable", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.sofa6)
                .build()
                .thenAccept(renderable -> sofa6Renderable = renderable)
                .exceptionally(throwable -> {
                    Toast toast =
                            Toast.makeText(this, "Unable to load sofa renderable", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.sofa7)
                .build()
                .thenAccept(renderable -> sofa7Renderable = renderable)
                .exceptionally(throwable -> {
                    Toast toast =
                            Toast.makeText(this, "Unable to load sofa renderable", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.coffee)
                .build()
                .thenAccept(renderable -> coffeeRenderable = renderable)
                .exceptionally(throwable -> {
                    Toast toast =
                            Toast.makeText(this, "Unable to load sofa renderable", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return null;
                });
        ModelRenderable.builder()
                .setSource(this, R.raw.sofa8)
                .build()
                .thenAccept(renderable -> sofa8Renderable = renderable)
                .exceptionally(throwable -> {
                    Toast toast =
                            Toast.makeText(this, "Unable to load sofa renderable", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return null;
                });



    }


    private void createModel(AnchorNode anchorNode, int selected) {
        if (selected == 1) {

            TransformableNode couch = new TransformableNode(arFragment.getTransformationSystem()); //MAKES MODEL TO ROTATE AND MOVE
            couch.setParent(anchorNode);
            couch.setRenderable(couchRenderable);
            couch.select();
            addname(anchorNode,couch,"x");

        } else if (selected == 2) {
            TransformableNode sofa1 = new TransformableNode(arFragment.getTransformationSystem()); //MAKES MODEL TO ROTATE AND MOVE
            sofa1.setParent(anchorNode);
            sofa1.setRenderable(setRenderable);
            sofa1.select();
            addname(anchorNode,sofa1,"x");

        }
        else if (selected == 3) {
            TransformableNode sofa2 = new TransformableNode(arFragment.getTransformationSystem()); //MAKES MODEL TO ROTATE AND MOVE
            sofa2.setParent(anchorNode);
            sofa2.setRenderable(set1Renderable);
            sofa2.select();
            addname(anchorNode,sofa2,"x");

        }
        else if (selected == 4) {
            TransformableNode lamp = new TransformableNode(arFragment.getTransformationSystem()); //MAKES MODEL TO ROTATE AND MOVE
            lamp.setParent(anchorNode);
            lamp.setRenderable(lampRenderable);
            lamp.select();
            addname(anchorNode,lamp,"x");

        }
        else if (selected == 5) {
            TransformableNode lamp1 = new TransformableNode(arFragment.getTransformationSystem()); //MAKES MODEL TO ROTATE AND MOVE
            lamp1.setParent(anchorNode);
            lamp1.setRenderable(lamp1Renderable);
            lamp1.select();
            addname(anchorNode,lamp1,"x");

        }
        else if (selected == 6) {
            TransformableNode bed = new TransformableNode(arFragment.getTransformationSystem()); //MAKES MODEL TO ROTATE AND MOVE
            bed.setParent(anchorNode);
            bed.setRenderable(bedRenderable);
            bed.select();
            addname(anchorNode,bed,"x");

        }
        else if (selected == 7) {
            TransformableNode counter = new TransformableNode(arFragment.getTransformationSystem()); //MAKES MODEL TO ROTATE AND MOVE
            counter.setParent(anchorNode);
            counter.setRenderable(counterRenderable);
            counter.select();
            addname(anchorNode,counter,"x");

        }
        else if (selected == 8) {
            TransformableNode table = new TransformableNode(arFragment.getTransformationSystem()); //MAKES MODEL TO ROTATE AND MOVE
            table.setParent(anchorNode);
            table.setRenderable(coffeeRenderable);
            table.select();
            addname(anchorNode, table,"x");

        }


        else if (selected == 9) {
            TransformableNode sofa3 = new TransformableNode(arFragment.getTransformationSystem()); //MAKES MODEL TO ROTATE AND MOVE
            sofa3.setParent(anchorNode);
            sofa3.setRenderable(soaf3Renderable);
            sofa3.select();
            addname(anchorNode, sofa3,"x");

        }
        else if (selected == 10) {
            TransformableNode sofa4 = new TransformableNode(arFragment.getTransformationSystem()); //MAKES MODEL TO ROTATE AND MOVE
            sofa4.setParent(anchorNode);
            sofa4.setRenderable(sofa4Renderable);
            sofa4.select();
            addname(anchorNode, sofa4,"x");

        }
        else if (selected == 11) {
            TransformableNode sofa5 = new TransformableNode(arFragment.getTransformationSystem()); //MAKES MODEL TO ROTATE AND MOVE
            sofa5.setParent(anchorNode);
            sofa5.setRenderable(sofa5Renderable);
            sofa5.select();
            addname(anchorNode, sofa5,"x");

        }
        else if (selected == 12) {
            TransformableNode sofa6 = new TransformableNode(arFragment.getTransformationSystem()); //MAKES MODEL TO ROTATE AND MOVE
            sofa6.setParent(anchorNode);
            sofa6.setRenderable(sofa6Renderable);
            sofa6.select();
            addname(anchorNode, sofa6,"x");

        }
        else if (selected == 13) {
            TransformableNode sofa7 = new TransformableNode(arFragment.getTransformationSystem()); //MAKES MODEL TO ROTATE AND MOVE
            sofa7 .setParent(anchorNode);
            sofa7 .setRenderable(sofa7Renderable);
            sofa7 .select();
            addname(anchorNode, sofa7 ,"x");


        }

        else if (selected == 14) {
            TransformableNode sofa8 = new TransformableNode(arFragment.getTransformationSystem()); //MAKES MODEL TO ROTATE AND MOVE
            sofa8 .setParent(anchorNode);
            sofa8  .setRenderable( sofa8Renderable);
            sofa8  .select();
            addname(anchorNode,  sofa8  ,"x");


        }



    }

    private void setClickListener() {
        for (int i = 0; i < arrayView.length; i++) {
            arrayView[i].setOnClickListener(this);
        }
    }


    private void setArrayView() {
        arrayView = new View[]{couch, modell, sofa_01, lamp1, lamp2, bed, counter, coffee, sofa3, sofa4, sofa5, sofa6, sofa7, sofa8};
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

        if (v.getId() == R.id.couch) {
            Log.d("selected", "one");
            selected = 1;
            setBackground(v.getId());

        } else if (v.getId() == R.id.set) {
            Log.d("selected", "two");
            selected = 2;
            setBackground(v.getId());

        } else if (v.getId() == R.id.set1) {
            Log.d("selected", "three");
            selected = 3;
            setBackground(v.getId());

        }
        else if (v.getId() == R.id.lamp1) {
            Log.d("selected", "three");
            selected = 4;
            setBackground(v.getId());

        }
        else if (v.getId() == R.id.lamp2) {
            Log.d("selected", "three");
            selected = 5;
            setBackground(v.getId());

        }
        else if (v.getId() == R.id.bed) {
            Log.d("selected", "three");
            selected = 6;
            setBackground(v.getId());

        }
        else if (v.getId() == R.id.counter) {
            Log.d("selected", "three");
            selected = 7;
            setBackground(v.getId());

        }
        else if (v.getId() == R.id.coffee) {
            Log.d("selected", "three");
            selected = 8;
            setBackground(v.getId());

        }
        else if (v.getId() == R.id.red3) {
            Log.d("selected", "three");
            selected = 9;
            setBackground(v.getId());

        }
        else if (v.getId() == R.id.sofa4) {
            Log.d("selected", "three");
            selected = 10;
            setBackground(v.getId());

        }
        else if (v.getId() == R.id.sofa5) {
            Log.d("selected", "three");
            selected = 11;
            setBackground(v.getId());

        }
        else if (v.getId() == R.id.sofa6) {
            Log.d("selected", "three");
            selected = 12;
            setBackground(v.getId());

        }
        else if (v.getId() == R.id.sofa7) {
            Log.d("selected", "three");
            selected = 13;
            setBackground(v.getId());

        }
        else if (v.getId() == R.id.sofa8) {
            Log.d("selected", "three");
            selected = 14;
            setBackground(v.getId());

        }
        if (v == image){
            Intent intent = new Intent();
            intent.setAction(android.content.Intent.ACTION_VIEW);
            intent.setType("image/*");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }
    }

    private void addname(AnchorNode anchorNode, TransformableNode model, String name) {


        ViewRenderable.builder()
                .setView(this,R.layout.name_furniture)
                .build()
                .thenAccept(viewRenderable -> {

                    TransformableNode nameView=new TransformableNode(arFragment.getTransformationSystem());
                    nameView.setLocalPosition(new Vector3(0f,model.getLocalPosition().y+1.0f,0));
                    nameView.setParent(anchorNode);
                    nameView.setRenderable(viewRenderable);
                    nameView.select();

                    TextView txt_name=(TextView)viewRenderable.getView();
                    txt_name.setText(name);

                    txt_name.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            anchorNode.setParent(null);

                        }

                    });

                });

    }

    private void setBackground(int id) {
        for (int i=0;i<arrayView.length;i++){

            if(arrayView[i].getId()==id)
                arrayView[i].setBackgroundColor(Color.parseColor("#999999"));
            else
                arrayView[i].setBackgroundColor(Color.TRANSPARENT);
        }

    }


}
