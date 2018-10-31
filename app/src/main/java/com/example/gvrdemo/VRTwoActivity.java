package com.example.gvrdemo;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.vr.sdk.widgets.pano.VrPanoramaEventListener;
import com.google.vr.sdk.widgets.pano.VrPanoramaView;

public class VRTwoActivity extends AppCompatActivity {

    private VrPanoramaView mVrPanoramaView;
    private VrPanoramaView.Options paNormalOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vrtwo);
        initView();
        paNormalOptions = new VrPanoramaView.Options();
        paNormalOptions.inputType = VrPanoramaView.Options.TYPE_MONO;
//      mVrPanoramaView.setFullscreenButtonEnabled (false); //隐藏全屏模式按钮
        mVrPanoramaView.setInfoButtonEnabled(false); //设置隐藏最左边信息的按钮
        mVrPanoramaView.setStereoModeButtonEnabled(false); //设置隐藏立体模型的按钮
        mVrPanoramaView.setEventListener(new ActivityEventListener()); //设置监听
        //加载本地的图片源
        mVrPanoramaView.loadImageFromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.aa), paNormalOptions);

    }

    private void initView() {
        mVrPanoramaView = (VrPanoramaView) findViewById(R.id.mVrPanoramaView);
    }
    private class ActivityEventListener extends VrPanoramaEventListener {
        @Override
        public void onLoadSuccess() {//图片加载成功
        }


        @Override
        public void onLoadError(String errorMessage) {//图片加载失败
        }

        @Override
        public void onClick() {//当我们点击了VrPanoramaView 时候触发            super.onClick();
        }

        @Override
        public void onDisplayModeChanged(int newDisplayMode) {
            super.onDisplayModeChanged(newDisplayMode);
        }
    }

}
