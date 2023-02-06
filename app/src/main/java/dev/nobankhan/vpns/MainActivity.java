package dev.nobankhan.vpns;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private int imnum = 0;
    private Uri imguri;
    private String filename = "imcount.dat";
    private String cordinate, front, left, right;
    EditText ecordinate, efront, eleft, eright;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Textinit();
        if(checkpermission()){
            String fileContents = Integer.toString(imnum);
            if(ReadFile(filename) == null){
                WriteFile(filename, fileContents);
            }
            else{
                fileContents = ReadFile(filename);
                imnum = Integer.parseInt(fileContents);
            }
            Button bt = findViewById(R.id.take);

            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    Uri imgpath = CreatImage(Textoperate());
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imgpath);
                    startActivityForResult(intent, 1017);
                }
            });

        }
        else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 100);
        }
    }

    public boolean checkpermission() {
        return ((ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) && (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) && (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED));
    }


    private String ReadFile(String filename){
        String ret = null;
        try {
            File file = new File(this.getFilesDir(), filename);
            FileReader fr = new FileReader(file);
            int i;
            StringBuilder mem = new StringBuilder();
            while ((i = fr.read()) != -1)
                mem.append((char) i);
            fr.close();
            ret = mem.toString();
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
        return ret;
    }

    private void WriteFile(String filename, String fileContents){
        try (FileOutputStream fos = this.openFileOutput(filename, this.MODE_PRIVATE)) {
            fos.write(fileContents.getBytes());


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private Uri CreatImage(String subfolder){
        Uri uri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        ContentResolver resolver = getContentResolver();
        String imgname = Integer.toString(imnum);
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, imgname+".jpg");
        contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/"+"Modeldata/"+subfolder+"/");
        Uri finaluri = resolver.insert(uri, contentValues);
        imguri = finaluri;
        return finaluri;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1017 && resultCode == Activity.RESULT_OK){
            imnum += 1;
            WriteFile(filename, Integer.toString(imnum));
        }
    }

    void Textinit(){
        ecordinate = findViewById(R.id.cordinate);
        efront = findViewById(R.id.front);
        eleft = findViewById(R.id.eleft);
        eright = findViewById(R.id.eright);
    }
    String Textoperate(){
        StringBuilder sb = new StringBuilder();

        if(!ecordinate.getText().toString().equals("null")) {
            cordinate = ecordinate.getText().toString();
            String[] temp = cordinate.split("/");
            sb.append("x:").append(temp[0]).append(":y:").append(temp[1]);
        }
        sb.append("_");
        if(!efront.getText().toString().equals("null")) {
            front = efront.getText().toString();
            String[] temp = front.split("/");
            sb.append("x:").append(temp[0]).append(":y:").append(temp[1]);
        }
        else{
            sb.append("null");
        }

        sb.append("_");
        if(!eleft.getText().toString().equals("null")) {
            left = eleft.getText().toString();
            String[] temp = left.split("/");
            sb.append("x:").append(temp[0]).append(":y:").append(temp[1]);
        }
        else{
            sb.append("null");
        }

        sb.append("_");
        if(!eright.getText().toString().equals("null")) {
            right = eright.getText().toString();
            String[] temp = right.split("/");
            sb.append("x:").append(temp[0]).append(":y:").append(temp[1]);
        }
        else{
            sb.append("null");
        }

        return sb.toString();
    }
}