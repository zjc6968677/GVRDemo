package com.example.gvrdemo;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.liangmayong.text2speech.OnText2SpeechListener;
import com.liangmayong.text2speech.Text2Speech;
import com.sunfusheng.marqueeview.MarqueeView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button button;
    private Button button1;
    private MarqueeView button2;
    private Intent intent;
    /**
     * 传感器
     */
    private SensorManager sensorManager;
    private ShakeSensorListener shakeListener;

    /**
     * 判断一次摇一摇动作
     */
    private boolean isShake = false;

    private ImageView imgHand;
    /**
     * 摇一摇动画
     */
    private ObjectAnimator anim;
    private Button button3;
    private EditText shuru;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        final ScaleAnimation scaleanimation = new ScaleAnimation(1f, 1.2f, 1f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        scaleanimation.setDuration(500);
        scaleanimation.setRepeatCount(ValueAnimator.INFINITE);
        scaleanimation.setRepeatMode(ValueAnimator.INFINITE);
        scaleanimation.setInterpolator(new AccelerateInterpolator());
        button.setOnClickListener(this);
        button1.setOnClickListener(this);
        button3.setOnClickListener(this);
        List<String> info = new ArrayList<>();
        info.add("11111111111111");
        info.add("22222222222222");
        info.add("33333333333333");
        info.add("44444444444444");
        info.add("55555555555555");
        info.add("66666666666666");
        button2.startWithList(info);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        shakeListener = new ShakeSensorListener();


        anim = ObjectAnimator.ofFloat(imgHand, "rotation", 0f, 45f, -30f, 0f);
        anim.setDuration(500);
        anim.setRepeatCount(ValueAnimator.INFINITE);
        Text2Speech.setOnText2SpeechListener(new OnText2SpeechListener() {
            @Override
            public void onCompletion() {
                Log.i("speak", "onCompletion");
                scaleanimation.cancel();
            }

            @Override
            public void onPrepared() {
                Log.i("speak", "onPrepared");
            }

            @Override
            public void onError(Exception e, String s) {
                Log.i("speak", "onError");
                scaleanimation.cancel();
            }

            @Override
            public void onStart() {
                Log.i("speak", "onStart");
            }

            @Override
            public void onLoadProgress(int i, int i1) {
                Log.i("speak", "onLoadProgress");
            }

            @Override
            public void onPlayProgress(int i, int i1) {
                Log.i("speak", "onPlayProgress---->" + i);
            }
        });
    }

    private void initView() {
        button = (Button) findViewById(R.id.button);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (MarqueeView) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        shuru = (EditText) findViewById(R.id.shuru);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                intent = new Intent(this, VROneActivity.class);
                startActivity(intent);
                break;
            case R.id.button1:
                intent = new Intent(this, VRTwoActivity.class);
                startActivity(intent);
                break;
            case R.id.button3:
                if (shuru.getText().toString().length()!=0) {
                    Text2Speech.speech(MainActivity.this,shuru.getText().toString() , true);
                }else {
                    Text2Speech.speech(MainActivity.this, "您没有输入", true);
                }
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        button2.startFlipping();
    }

    @Override
    public void onStop() {
        super.onStop();
        button2.stopFlipping();
    }

    @Override
    protected void onResume() {
        //注册监听加速度传感器
        sensorManager.registerListener(shakeListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_FASTEST);
        super.onResume();
    }

    @Override
    protected void onPause() {
        /**
         * 资源释放
         */
        sensorManager.unregisterListener(shakeListener);
        super.onPause();

        if (Text2Speech.isSpeeching())
            Text2Speech.pause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Text2Speech.isSpeeching())
            Text2Speech.shutUp(this);
    }

    private class ShakeSensorListener implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent event) {
            //避免一直摇
            if (isShake) {
                return;
            }
            // 开始动画
            anim.start();
            float[] values = event.values;
            /*
             * x : x轴方向的重力加速度，向右为正
             * y : y轴方向的重力加速度，向前为正
             * z : z轴方向的重力加速度，向上为正
             */
            float x = Math.abs(values[0]);
            float y = Math.abs(values[1]);
            float z = Math.abs(values[2]);
            //加速度超过80，摇一摇成功
            if (x > 50 || y > 50 || z > 50) {
                isShake = true;
                //播放声音
                playSound(MainActivity.this);
                //震动，注意权限
                vibrate(500);
                //仿网络延迟操作，这里可以去请求服务器...
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //弹框
                        showDialog();
                        //动画取消
                        anim.cancel();
                    }
                }, 1000);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    }

    private void playSound(Context context) {
        MediaPlayer player = MediaPlayer.create(context, R.raw.shake_sound);
        player.start();
    }

    @SuppressLint("MissingPermission")
    private void vibrate(long milliseconds) {
        Vibrator vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        vibrator.vibrate(milliseconds);
    }

    private void showDialog() {
        final AlertDialog mAlertDialog = new AlertDialog.Builder(this).show();
        View view = LayoutInflater.from(this).inflate(R.layout.layout_dialog, null);
        mAlertDialog.setContentView(view);
        mAlertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                //这里让弹框取消后，才可以执行下一次的摇一摇
                isShake = false;
                mAlertDialog.cancel();
            }
        });
        Window window = mAlertDialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(0x00000000));
    }

}
