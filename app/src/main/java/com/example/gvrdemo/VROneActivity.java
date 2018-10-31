package com.example.gvrdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.zph.glpanorama.GLPanorama;

public class VROneActivity extends AppCompatActivity {

    private GLPanorama mGLPanorama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vrone);
        initView();
        mGLPanorama.setGLPanorama(R.drawable.dd);
    }

    private void initView() {
        mGLPanorama = (GLPanorama) findViewById(R.id.mGLPanorama);
    }
}
