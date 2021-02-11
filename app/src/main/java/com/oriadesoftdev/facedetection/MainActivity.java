package com.oriadesoftdev.facedetection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

public class MainActivity extends AppCompatActivity {

    private ImageView mShowImageView;
    private Button mProgressButton;
    private Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mShowImageView = findViewById(R.id.show_image_view);
        mProgressButton = findViewById(R.id.button_progress);

        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sample_face);
        mShowImageView.setImageBitmap(mBitmap);

        Paint boxPaint = new Paint();
        boxPaint.setStrokeWidth(5.0f);
        boxPaint.setColor(Color.GREEN);
        boxPaint.setStyle(Paint.Style.STROKE);

        Bitmap tempBitmap = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(),
                Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(tempBitmap);
        canvas.drawBitmap(mBitmap, 0, 0, null);

        mProgressButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FaceDetector faceDetector =
                                new FaceDetector.Builder(getApplicationContext())
                                        .setTrackingEnabled(false)
                                        .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                                        .setMode(FaceDetector.FAST_MODE)
                                        .build();

                        if(!faceDetector.isOperational()){
                            Toast.makeText(MainActivity.this,
                                    "Face Detector not setup please, " +
                                            "restart your app and try again",
                                    Toast.LENGTH_LONG).show();
                            return;
                        }

                        Frame frame = new Frame.Builder().setBitmap(mBitmap).build();
                        SparseArray<Face> sparseArray = faceDetector.detect(frame);

                        for(int i=0; i<sparseArray.size(); ++i){
                            Face face = sparseArray.valueAt(i);

                            float x1 = face.getPosition().x;
                            float y1 = face.getPosition().y;
                            float x2 = x1 + face.getWidth();
                            float y2 = y1 + face.getHeight();

                            RectF rectF = new RectF(x1,y1,x2,y2);
                            canvas.drawRoundRect(rectF, x1, y1, boxPaint);
                        }

                        mShowImageView.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
                    }
                }
        );
    }
}