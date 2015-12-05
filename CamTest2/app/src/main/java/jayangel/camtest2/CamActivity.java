package jayangel.camtest2;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.CalendarContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.Calendar;

//Made using this tutorial: https://www.youtube.com/watch?v=k-3zXb7GteU

public class CamActivity extends AppCompatActivity {

    private static String logtag = "CameraApp8";
    private static int TAKE_PICTURE = 1;
    private Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_cam);
        takePhoto();
        //Button cameraButton = (Button)findViewById(R.id.button_camera);
        //cameraButton.setOnClickListener(cameraListener);
    }; //end of onCreate
/*
    private OnClickListener cameraListener = new OnClickListener(){
        public void onClick(View v){
            takePhoto();
        }
    }; //end of onClickListener
*/
    /* ----------------------------------------------------------------*/


    private void takePhoto(){
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File photo;
        photo = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"picture_" + DateDemo.main()+ ".jpg");
        imageUri = Uri.fromFile(photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PICTURE);
    }

    /* ----------------------------------------------------------------*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent intent){
        super.onActivityResult(requestCode, resultCode,intent);


        Calendar cal = Calendar.getInstance();
        Intent intent5 = new Intent(Intent.ACTION_EDIT);
        intent5.setData(CalendarContract.Events.CONTENT_URI);
        intent5.putExtra("beginTime", cal.getTimeInMillis());
        intent5.putExtra("allDay", true);
        intent5.putExtra("rrule", "FREQ=YEARLY");
        intent5.putExtra("endTime", cal.getTimeInMillis()+60*60*1000);
        intent5.putExtra("title", "A Test Event from android app");
        startActivity(intent5);

       // takePhoto();

        if(resultCode == Activity.RESULT_OK) {
            Uri selectedImage = imageUri;
            getContentResolver().notifyChange(selectedImage,null);

            ImageView imageView = (ImageView)findViewById(R.id.image_camera);
            ContentResolver cr = getContentResolver();
            Bitmap bitmap;



            try {
                bitmap = MediaStore.Images.Media.getBitmap(cr,selectedImage);
                imageView.setImageBitmap(bitmap);
                Toast.makeText(CamActivity.this, selectedImage.toString(),Toast.LENGTH_LONG).show();
            }catch(Exception e) {
                Log.e(logtag, e.toString());
            }//end of catch

        }// end of if

    } //end of onActivityResult

}//end of class




































