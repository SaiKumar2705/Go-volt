package com.quadrant.govolt;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.quadrant.govolt.Others.AppUtils;

import java.io.File;
import java.io.IOException;

public class KickPreviewActivity extends AppCompatActivity  implements View.OnClickListener {

    ImageView img, remove, right;
    private String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imagepopup_upload);
        img = (ImageView) findViewById(R.id.imageView);
        img.setOnClickListener(this);
        remove = (ImageView) findViewById(R.id.remove);
        remove.setOnClickListener(this);
        right = (ImageView) findViewById(R.id.right);
        right.setOnClickListener(this);
        String Filepath=getIntent().getStringExtra("filepath");
        File imgFile = new  File(Filepath);

        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            Uri tempUri = Uri.fromFile(imgFile);
            try {
                Bitmap rotatedBitMap = AppUtils.rotateImageIfRequired(this,myBitmap,tempUri);
                Uri finalURI = AppUtils.getImageUri(getApplicationContext(), rotatedBitMap);
                File finalFile = new File(AppUtils.getRealPathFromURI(finalURI,this));
                AppUtils.deleteImage(imgFile);
                imagePath = finalFile.getPath();
//                img.setImageBitmap(compressedBitmap);
                Glide.with(this)
                        .load(rotatedBitMap)
                        .error(R.drawable.ic_launcher)
                        .into(img);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.remove:
                finish();
                break;

            case R.id.right:
                Intent intent = new Intent();
                intent.putExtra("imagePath",imagePath);
                setResult(RESULT_OK,intent);
                finish();
                break;

        }
    }
}