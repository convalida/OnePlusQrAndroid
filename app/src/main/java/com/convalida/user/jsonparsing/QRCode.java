package com.convalida.user.jsonparsing;

import android.content.Intent;
import android.graphics.Bitmap;
import android.renderscript.Sampler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class QRCode extends AppCompatActivity {
    String number;
    Bitmap bitmap;
    ImageView img;
    public static final int QRcodeWidth=500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        img= (ImageView) findViewById(R.id.qrImage);
        Intent intent=getIntent();
        if(intent.hasExtra("qrCode")){
          number=intent.getStringExtra("qrCode");
        }

        try {
            bitmap=textToImageEncode(number);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        img.setImageBitmap(bitmap);

    }

    private Bitmap textToImageEncode(String number) throws WriterException {
        BitMatrix bitMatrix;

        bitMatrix=new MultiFormatWriter().encode(number, BarcodeFormat.DATA_MATRIX.QR_CODE,QRcodeWidth,QRcodeWidth,null);
        int bitMatrixWidth=bitMatrix.getWidth();
        int bitMatrixHeight=bitMatrix.getHeight();
        int[] pixels=new int[bitMatrixWidth*bitMatrixHeight];
        for(int y=0;y<bitMatrixHeight;y++){
            int offset=y*bitMatrixWidth;
            for(int x=0;x<bitMatrixWidth;x++){
                pixels[offset+x]=bitMatrix.get(x,y)?getResources().getColor(R.color.QRCodeBlackColor):getResources().getColor(R.color.QRCodeWhiteColor);
            }
        }
        Bitmap bmap=Bitmap.createBitmap(bitMatrixWidth,bitMatrixHeight,Bitmap.Config.ARGB_4444);
        bmap.setPixels(pixels,0,500,0,0,bitMatrixWidth,bitMatrixHeight);
        return bmap;
    }
}
