package com.example.minorprojecthealthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class Barcode_activity extends AppCompatActivity {

    CameraView cameraView;
    Button btnDetect;
    AlertDialog waitingDailog;
    Bitmap bitmap;
    public static String info, tmp;
    int flag;

    @Override
    protected void onResume() {
        super.onResume();
        cameraView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraView.stop();
    }


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_activity);
        Log.i("btndetect","btndetect");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        cameraView = findViewById(R.id.cameraview);
        btnDetect = findViewById(R.id.btn_detect);
        waitingDailog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("please wait")
                .setCancelable(false)
                .build();
        btnDetect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraView.start();
                cameraView.captureImage();
            }
        });
        Log.i("reminder", "Just Created");





        cameraView.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {


            }

            @Override
            public void onError(CameraKitError cameraKitError) {

            }

            @Override
            public void onImage(CameraKitImage cameraKitImage) {

                waitingDailog.show();
                bitmap=cameraKitImage.getBitmap();
                bitmap=Bitmap.createScaledBitmap(bitmap,cameraView.getWidth(),cameraView.getHeight(),false);
                cameraView.stop();

                Log.i("reminder", "bitmap");
                runDetector(bitmap);


            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {

            }
        });




    }

    private void runDetector(Bitmap bitmap) {
        Log.i("reminder", "rundetector");


        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
        FirebaseVisionBarcodeDetectorOptions options = new FirebaseVisionBarcodeDetectorOptions.Builder()
                .setBarcodeFormats(
                        FirebaseVisionBarcode.FORMAT_ALL_FORMATS
                )
                .build();
        FirebaseVisionBarcodeDetector detector = FirebaseVision.getInstance().getVisionBarcodeDetector(options);
        detector.detectInImage(image)
                .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionBarcode>>() {
                    @Override
                    public void onSuccess(List<FirebaseVisionBarcode> firebaseVisionBarcodes) {
                        processResult(firebaseVisionBarcodes);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Barcode_activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

    }

    private void processResult(List<FirebaseVisionBarcode> firebaseVisionBarcodes) {

        for(FirebaseVisionBarcode item : firebaseVisionBarcodes)
        {
            int value_type = item.getValueType();
            switch (value_type)
            {
                case FirebaseVisionBarcode.TYPE_TEXT:
                {
                    Log.i("reminder", "type_text");
                    info = item.getRawValue();
                    assert info != null;
                    //tmp = info.substring(1,11);
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
                    builder.setMessage(info);
                    builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                Log.i("reminder", "text readdrug data");

                                readDrugData();
                                Log.i("reminder", "text  11 readdrug data");

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    androidx.appcompat.app.AlertDialog dialog = builder.create();
                    dialog.show();

                }

                break;



                case FirebaseVisionBarcode.TYPE_URL:
                {
                    Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse(item.getRawValue()));
                    startActivity(intent);
                }

                break;

                case FirebaseVisionBarcode.TYPE_PRODUCT:
                {
                    Log.i("reminder", "type_product");

                    info = item.getRawValue();
                    assert info != null;
                    Log.i("reminder", "info " + info);
                    if(info.length()==14) {
                        tmp = info.substring(1,13);
                        Log.i("reminder", "temp1 " + tmp);
                    }
                    else if (info.length()==12) {
                        tmp = info.substring(1, 11);
                        Log.i("reminder", "temp2 " + tmp);
                    }
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
                    builder.setMessage(tmp);
                    builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                Log.i("reminder", "Readdrugdata");

                                readDrugData();
                            } catch (IOException e) {
                                Log.i("reminder", "error");

                                e.printStackTrace();
                            }
                        }
                    });
                    androidx.appcompat.app.AlertDialog dialog = builder.create();
                    dialog.show();
                }
                break;

                default:
                    break;


            }
        }
        waitingDailog.dismiss();


    }



    public List<DrugSample> drugSamples = new ArrayList<>();
    public void readDrugData() throws IOException {
        InputStream Is = getResources().openRawResource(R.raw.sample);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(Is, Charset.forName("UTF-8"))
        );
        String line="";

        while ((line = reader.readLine())!=null) {

            String[] tokens = line.split(",");
            DrugSample sample = new DrugSample();
            sample.setDrugId(tokens[0]);
            sample.setDrugName(tokens[1]);
            sample.setTargetDisease(tokens[2]);
            String str1 = sample.getDrugId();
            drugSamples.add(sample);
            Log.i("reminder", "Sample   " + sample);
            if (str1.equals(tmp)) {
                flag=1;
                Log.i("reminder", "str1  " + sample);
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
                builder.setMessage(sample.toString());
                builder.setPositiveButton("done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){}
                });
                androidx.appcompat.app.AlertDialog dialog = builder.create();
                dialog.show();


            }

            Log.i("reminder", "outif");
            waitingDailog.dismiss();

        }
        if(flag==0)
        {
            Log.i("reminder", "no match");
            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
            builder.setMessage("Id not found in dataset");
            builder.setPositiveButton("done", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            androidx.appcompat.app.AlertDialog dialog = builder.create();
            dialog.show();
        }
        waitingDailog.dismiss();


    }

}
