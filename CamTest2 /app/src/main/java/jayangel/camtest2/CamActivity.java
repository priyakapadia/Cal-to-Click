package jayangel.camtest2;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
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

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

import org.json.JSONObject;

import java.io.File;
import java.util.Calendar;

/************/
//OCR
/*
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.json.JSONObject;

import com.mashape.unirest.http.*;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.*;
import com.mashape.unirest.*;
import org.json.*;
*/
/***********/

//Made using this tutorial: https://www.youtube.com/watch?v=k-3zXb7GteU

public class CamActivity extends AppCompatActivity
{

    private static String logtag = "CameraApp8";
    private static int TAKE_PICTURE = 1;
    private Uri imageUri;
    public boolean initialCounter = true;

    //protected void onTrialForHomePage(int requestCode, int resultCode,Intent intent) {

   // }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cam);
        //opens the camera app on initial startup only
        if(initialCounter)
        {
            initialCounter = false;
            takePhoto();
        }

        //button for taking another picture
            ImageButton cameraButton = (ImageButton)findViewById(R.id.button_camera);
            cameraButton.setOnClickListener(cameraListener);


        // button for exit program
        Button btn1 = (Button) findViewById(R.id.button_exit);
        btn1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                finish();
                System.exit(0);
            }
        });



    }; //end of onCreate

    private View.OnClickListener cameraListener = new View.OnClickListener()
    {
        public void onClick(View v)
        {

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
    protected void onActivityResult(int requestCode, int resultCode,Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);


        // If image was taken and saved successfully you go to main screen
        if(resultCode == Activity.RESULT_OK)
        {
            Uri selectedImage = imageUri;
            // getContentResolver().notifyChange(selectedImage, null);
            try
            {
                new OCRAsync().execute(selectedImage.getPath());
                Toast.makeText(CamActivity.this, selectedImage.toString(), Toast.LENGTH_LONG).show();
            }

            catch(Exception e)
            {
                Log.e(logtag, e.toString());

            }//end of catch
        }// end of if
    }//end of onActivityResult

    class OCRAsync extends AsyncTask<String, Integer, HttpResponse<JsonNode>>
    {
        protected HttpResponse<JsonNode> doInBackground(String... msg)
        {
            HttpResponse<JsonNode> request = null;
            String relPath = msg[0];

            try
            {
                String path = new File(relPath).getAbsolutePath();
                Log.e("PIC: ", path);
                request = Unirest.post("https://api.havenondemand.com/1/api/sync/ocrdocument/v1")//"https://apicloud-facemark.p.mashape.com/process-file.json")
                        //.header("X-Mashape-Key", "O5pPl3KTaVmshRGGD5FykeKF31gXp15vSBMjsnfMHFofluIQtP")
                        .field("apikey", "9246b321-e229-41bb-a5a2-62a323897ce8")
                        //Real path
                        .field("file", new File( path ) )
                        //Hardcoded/saved path ---> edit file path to phone's path
                        //.field("file", new File("/storage/emulated/0/Pictures/Messenger/received_10206599225873671.jpeg"))
                        .field("mode", "document_scan")
                        .asJson();

                //JSONObject textblock =(JSONObject) request.getBody().getObject().getJSONArray("text_block").get(0);
                String res= request.getBody().toString();//textblock.getString("text");
                //System.out.print(result);
                //Log.e("Got response", res);
            }
            catch (Exception e)
            {
                Log.e("Trying to send", e.toString());
            }

            return request;
        }

        protected void onPostExecute(HttpResponse<JsonNode> response)
        {
            String result = "";

            try
            {
                JSONObject textblock =(JSONObject) response.getBody().getObject().getJSONArray("text_block").get(0);
                result=textblock.getString("text");
                //System.out.print(result);
                Log.e("Got response", result);

                //parsing code for data taken from picture
                int[] parseResult = DataParsing.main(result);

                //Adding event to calendar
                Calendar beginTime = Calendar.getInstance();
                // beginTime.set(result[2], result[0]-1, result[1], result[3], result[4]);
                beginTime.set(parseResult[2], 2, 18, 14, 30);
                Calendar endTime = Calendar.getInstance();
                //endTime.set(result[2], result[0]-1, result[1], result[3]+1, result[4]);
                endTime.set(parseResult[2], 2, 18, 15, 30);
                Intent intent5 = new Intent(Intent.ACTION_INSERT);
                intent5.setData(CalendarContract.Events.CONTENT_URI);
                intent5.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis());
                intent5.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis());
                intent5.putExtra(CalendarContract.Events.TITLE, "Hey Now");
                startActivity(intent5);
            }
            catch(Exception e)
            {
                Log.e("Already sent: ", e.toString());
            }
            System.out.println(result);
        }
    }
}//end of class




































