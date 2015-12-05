package jayangel.camtest2;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

//Made using this tutorial: https://www.youtube.com/watch?v=k-3zXb7GteU

public class CamActivity extends AppCompatActivity {

    private static String logtag = "CameraApp8";
    private static int TAKE_PICTURE = 1;
    private Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cam);
        Button cameraButton = (Button)findViewById(R.id.button_camera);
        cameraButton.setOnClickListener(cameraListener);


        Button btn1 = (Button) findViewById(R.id.button_exit);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
                System.exit(0);
            }
        });



    }; //end of onCreate

    private View.OnClickListener cameraListener = new View.OnClickListener()
    {
        public void onClick(View v){
            takePhoto();
        }
    }; //end of onClickListener

    /* ----------------------------------------------------------------*/

    //
    private void takePhoto(){
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File photo;
        String blindedByTheLight = DateDemo.main() + ".jpg";
        photo = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),blindedByTheLight );
        imageUri = Uri.fromFile(photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PICTURE);
    }

    /* ----------------------------------------------------------------*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent intent){
        super.onActivityResult(requestCode, resultCode,intent);


         /*//adds event to calendar
        Calendar cal = Calendar.getInstance();
        Intent intent5 = new Intent(Intent.ACTION_EDIT);
        intent5.setData(CalendarContract.Events.CONTENT_URI);
        intent5.putExtra("beginTime", cal.getTimeInMillis());
        intent5.putExtra("allDay", true);
        intent5.putExtra("rrule", "FREQ=YEARLY");
        intent5.putExtra("endTime", cal.getTimeInMillis() + 60 * 60 * 1000);
        intent5.putExtra("title", "A Test Event from android app");
        startActivity(intent5); */

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




































