package com.example.nbk;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class QRScanner extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CAMERA = 0;

    private PreviewView previewView;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;

    private ImageView btn;
    private String qrCode = "";
    TextView result;
    ImageView home;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscanner);
        result = findViewById(R.id.Result);
        home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QRScanner.this, DashboardActivity.class));
            }
        });

        previewView = findViewById(R.id.qr_activity);
        btn = findViewById(R.id.btn);
//        qrCodeFoundButton = findViewById(R.id.activity_main_qrCodeFoundButton);
//        qrCodeFoundButton.setVisibility(View.INVISIBLE);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), qrCode, Toast.LENGTH_SHORT).show();
                Log.i(QRScan.class.getSimpleName(), "QR Code Found: " + qrCode);

            }
        });

        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        requestCamera();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void requestCamera() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                ActivityCompat.requestPermissions(QRScanner.this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else {
                Toast.makeText(this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void startCamera() {
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindCameraPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                Toast.makeText(this, "Error starting camera " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void bindCameraPreview(@NonNull ProcessCameraProvider cameraProvider) {
        previewView.setPreferredImplementationMode(PreviewView.ImplementationMode.SURFACE_VIEW);

        Preview preview = new Preview.Builder()
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        preview.setSurfaceProvider(previewView.createSurfaceProvider());

        ImageAnalysis imageAnalysis =
                new ImageAnalysis.Builder()
                        .setTargetResolution(new Size(1280, 720))
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), new QRCodeImageAnalyzer(new QRCodeFoundListener() {
            @Override
            public void onQRCodeFound(String _qrCode) {
                imageAnalysis.clearAnalyzer();
                function(_qrCode);
return;

    }

    @Override
    public void qrCodeNotFound() {
        btn.setVisibility(View.INVISIBLE);
    }
}));

        Camera camera=cameraProvider.bindToLifecycle((LifecycleOwner)this,cameraSelector,imageAnalysis,preview);
        }
public void function (String _qrCode){
    qrCode = _qrCode;
    result.setText(_qrCode);
    String[] splitString = qrCode.split("-");
    if (splitString[0].equals("nbk") && (splitString[1].equals("pay") || splitString[1].equals("recieve"))) {

//        AlertDialog alertDialog = new AlertDialog.Builder(QRScanner.this).create();
//        alertDialog.setTitle("Notification");
//        String typeFormat = (splitString[1] == "pay") ? "pay you " : "recieve from you";
//        alertDialog.setMessage(typeFormat);
//        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Accept", new DialogInterface.OnClickListener() {
//            Backend be = new Backend();
//
//            @RequiresApi(api = Build.VERSION_CODES.O)
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Log.d("pay/recieve", splitString[1]);
//                if (splitString[1].equals("pay")) {
//                    /**/
//                    be.UserOnePaysUser2(splitString[3], FirebaseAuth.getInstance().getCurrentUser().getUid(), Float.parseFloat(splitString[2]));
//                } else if (splitString[1].equals("recieve")) {
//                    be.UserOneRecievesFromUser2(splitString[3], FirebaseAuth.getInstance().getCurrentUser().getUid(), Float.parseFloat(splitString[2]));
//                }
//                be.UserOneRecievesFromUser2(splitString[3], FirebaseAuth.getInstance().getCurrentUser().getUid(), Float.parseFloat(splitString[2]));
//                startActivity(new Intent(QRScanner.this, DashboardActivity.class));
//            }
//        });
//        finish();
//                }
//
//            });
//        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"Reject",new DialogInterface.OnClickListener()
//
//        {
//            @Override
//            public void onClick (DialogInterface dialog,int which){
//                dialog.dismiss();
//                startActivity(new Intent(QRScanner.this, DashboardActivity.class));
////
//            }
////
//        });
//        alertDialog.show();
//////
//            //
            Intent intent = new Intent(QRScanner.this, Notify.class);
                    intent.putExtra("qrArray",splitString);

            startActivity(intent);
                    btn.setVisibility(View.VISIBLE);
    } else{
        Intent intent = new Intent(QRScanner.this, PromotionsActivity.class);
        intent.putExtra("category", qrCode);
        startActivity(intent);
        btn.setVisibility(View.VISIBLE);
    }
}

}