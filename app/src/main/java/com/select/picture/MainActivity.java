package com.select.picture;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import frame.android.com.selectpicturelibrary.img.PictureBean;
import frame.android.com.selectpicturelibrary.img.SelectPictureActivity;

public class MainActivity extends AppCompatActivity {
    private TextView mText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mText = (TextView) findViewById(R.id.text);
    }

    public void click(View view) {
        Intent intent = new Intent(this, SelectPictureActivity.class);
        intent.putExtra("maxCount", 9);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == SelectPictureActivity.RESULT_CODE_SELECT_FINISH) {
            if (data != null) {
                ArrayList<PictureBean> imgs = data.getParcelableArrayListExtra("imgs");
                Log.d("MainActivity", "imgs:" + imgs);

                mText.setText(imgs.toString());
            }
        }
    }
}
