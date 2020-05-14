package com.example.aiintegration;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import captcha.Captcha;
import captcha.TextCaptcha;

public class MainActivity extends AppCompatActivity {
    ImageView im1, im2;
    Button btn1, btn2, btn3;
    TextView ans1, ans2;
    private Bitmap inputBitmap;
    private DigitClassifier digitClassifier;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captcha_test);
        im1 = (ImageView)findViewById(R.id.imageView1);
        btn1 = (Button)findViewById(R.id.button1);
        ans1 = (TextView)findViewById(R.id.textView1);
        im2 = (ImageView)findViewById(R.id.imageView2);
        btn2 = (Button)findViewById(R.id.button2);
        ans2 = (TextView)findViewById(R.id.textView2);
        btn3 = (Button)findViewById(R.id.button3);
        btn1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //package1.Captcha c = new package1.MathCaptcha(300, 100, package1.MathCaptcha.MathOptions.PLUS_MINUS_MULTIPLY);
                Captcha c = new TextCaptcha(300, 100, 5, TextCaptcha.TextOptions.NUMBERS_ONLY);
                im1.setImageBitmap(c.image);
                im1.setLayoutParams(new LinearLayout.LayoutParams(c.width *2, c.height *2));
                ans1.setText(c.answer);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //package1.Captcha c = new package1.MathCaptcha(300, 100, package1.MathCaptcha.MathOptions.PLUS_MINUS_MULTIPLY);
                Captcha c = new TextCaptcha(300, 100, 1, TextCaptcha.TextOptions.NUMBERS_ONLY);
                inputBitmap = c.image;//Store the captcha as an input for later use in tflite
                im2.setImageBitmap(inputBitmap);
                im2.setLayoutParams(new LinearLayout.LayoutParams(c.width *2, c.height *2));
                //ans2.setText(c.answer);
            }
        });

        digitClassifier = new DigitClassifier(this);
        digitClassifier.isInitialized = true;
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get the current bitmap from the ImageView
                Bitmap bitmapOrginal = ((BitmapDrawable) im2.getDrawable()).getBitmap();
                System.out.println("bitmapOriginal.width: " + bitmapOrginal.getWidth() + "-height: " + bitmapOrginal.getHeight());
                if ((bitmapOrginal != null) && (digitClassifier.isInitialized)) {
                    digitClassifier.initialize();
                }
            }
        });
    }
    @Override
    protected void onDestroy(){
        digitClassifier.close();
        super.onDestroy();
    }
}
