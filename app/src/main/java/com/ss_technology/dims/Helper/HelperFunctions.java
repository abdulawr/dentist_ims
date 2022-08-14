package com.ss_technology.dims.Helper;


import android.graphics.Bitmap;
import android.util.Base64;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HelperFunctions {

    public static String stringToImage(Bitmap bi)
    {
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        bi.compress(Bitmap.CompressFormat.JPEG,80,outputStream);
        byte[] imageByte=outputStream.toByteArray();
        String encodeimage= Base64.encodeToString(imageByte,Base64.DEFAULT);
        return  encodeimage;

    }

    public static byte[] ImageToByte(Bitmap bi)
    {
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        bi.compress(Bitmap.CompressFormat.JPEG,80,outputStream);
        byte[] imageByte=outputStream.toByteArray();
        return  imageByte;

    }

    public static String currentDate()
    {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        return formattedDate;
    }


}
