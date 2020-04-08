package com.example.dragfloatimageview;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dragfloatimageview.widget.RichTextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RichTextView rtv = findViewById(R.id.rtv);
        rtv.setOnClickListener(v->{
            rtv.setText_1("替换");
        });
    }
}
