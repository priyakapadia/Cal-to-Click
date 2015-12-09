package jayangel.camtest2;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.CalendarContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.util.Calendar;

//Made using this tutorial: https://www.youtube.com/watch?v=k-3zXb7GteU

public class CamActivity extends AppCompatActivity {

    private static String logtag = "CameraApp8";
    private static int TAKE_PICTURE = 1;
    private Uri imageUri;
    public int initialCounter = 0;

    //protected void onTrialForHomePage(int requestCode, int resultCode,Intent intent) {

   // }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cam);
        //opens the camera app on initial startup only
        if(initialCounter < 1)
        {
            takePhoto();
        }

        //button for taking another picture
            ImageButton cameraButton = (ImageButton)findViewById(R.id.button_camera);
            cameraButton.setOnClickListener(cameraListener);


        // button for exit
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
        initialCounter++;
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
        super.onActivityResult(requestCode, resultCode, intent);


        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2016, 0, 19, 7, 30);
                Calendar endTime = Calendar.getInstance();
                endTime.set(2016, 0, 19, 8, 30);
                Intent intent5 = new Intent(Intent.ACTION_INSERT);
        intent5.setData(CalendarContract.Events.CONTENT_URI);
        intent5.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis());
        intent5.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis());
        intent5.putExtra(CalendarContract.Events.TITLE, "Hey Now");
        //intent5.putExtra(CalendarContract.Events.
                         startActivity(intent5);

        //.putExtra("Title", "Yoga")
                       // .putExtra(CalendarContract.DESCRIPTION, "Group class")
                       // .putExtra(Events.EVENT_LOCATION, "The gym")
                      //  .putExtra(Events.AVAILABILITY, Events.AVAILABILITY_BUSY)
                      //  .putExtra(Intent.EXTRA_EMAIL, "rowan@example.com,trevor@example.com");




// If image was taken and saved successfully you go to main screen
        if(resultCode == Activity.RESULT_OK) {

            Uri selectedImage = imageUri;
            getContentResolver().notifyChange(selectedImage, null);
            try {

                Toast.makeText(CamActivity.this, selectedImage.toString(), Toast.LENGTH_LONG).show();

            }catch(Exception e) {
                Log.e(logtag, e.toString());

            }//end of catch
        }// end of if
    } //end of onActivityResult

}//end of class




































