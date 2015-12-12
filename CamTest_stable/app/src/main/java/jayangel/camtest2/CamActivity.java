/*

CamActivity.java

- Camera intent ---> take photo
- Async task threading begins as soon as photo is saved ---> prevent UI blockage
-----------------------------------------------------------------------------------------
- Use hardcoded file path of savedPhoto (due to poor file resolution)
- Use savedPhoto as the file sent to HPE Haven OnDemand OCR
- Connect to OCR using Unirest 1.3.2 HTTP lib (latest ver caused fatal errors)
- The Async task thread handles the HTTP Post, which ends when the HP server replies w/the OCR response
- The Async task thread joins back w/the main thread and executes the next intent
-----------------------------------------------------------------------------------------
- onPostExecute runs OCR data parsing (Regex) from DataParsing.java on OCR response
- Calendar intent ---> auto-populates the Event Title, Begin Date, and Begin Time from the OCR response

Created by Dawn of the Planet of the Apps on 12/11/2015

Made using this tutorial: https://www.youtube.com/watch?v=k-3zXb7GteU

 */


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


public class CamActivity extends AppCompatActivity
{
    private static String logtag = "CameraApp8";
    private static int TAKE_PICTURE = 1;
    private Uri imageUri;
    public boolean initialCounter = true;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cam);
        getSupportActionBar().hide();
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
    };

    private View.OnClickListener cameraListener = new View.OnClickListener()
    {
        public void onClick(View v)
        {

            takePhoto();
        }
    }; //end of onClickListener

    /* ----------------------------------------------------------------*/

    //take photo and save into directory of app photos
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

            try
            {
                //start Async task thread
                new OCRAsync().execute(selectedImage.getPath());
                Toast.makeText(CamActivity.this, selectedImage.toString(), Toast.LENGTH_LONG).show();
            }

            catch(Exception e)
            {
                Log.e(logtag, e.toString());

            }
        }
    }

    //Async task thread handles HTTP POST to prevent request from blocking UI
    class OCRAsync extends AsyncTask<String, Integer, HttpResponse<JsonNode>>
    {
        protected HttpResponse<JsonNode> doInBackground(String... msg)
        {
            HttpResponse<JsonNode> request = null;
            String relPath = msg[0];
            System.out.println("relPath is: ");
            System.out.println(relPath);

            try
            {
                String path = new File(relPath).getAbsolutePath();
                //System.out.println("path is: ");
                //System.out.println(path);
                Log.e("PIC: ", path);

                //HTTP POST to OCR
                request = Unirest.post("https://api.havenondemand.com/1/api/sync/ocrdocument/v1")
                        .field("apikey", "9246b321-e229-41bb-a5a2-62a323897ce8")
                        //Real path
                        //.field("file", new File(relPath))
                        //Hardcoded/saved path ---> edit file path to phone's savedPhoto path
                        .field("file", new File("/storage/emulated/0/Pictures/Messenger/received_10206600199338007.jpeg"))
                        .field("mode", "document_scan")
                        .asJson();

                String res= request.getBody().toString();

                System.out.println(new File(path).getAbsolutePath());
            }

            catch (Exception e)
            {
                Log.e("Trying to send", e.toString());
            }

            return request;
        }

        //uses OCR response as input for DataParsing ---> autopopulate the Calendar event
        protected void onPostExecute(HttpResponse<JsonNode> response)
        {
            String result = "";

            try
            {
                JSONObject textblock =(JSONObject) response.getBody().getObject().getJSONArray("text_block").get(0);
                result=textblock.getString("text");

                Log.e("Got response", result);

                //parsing code for data taken from picture
                int[] parseResult = DataParsing.main(result);

                //Adding event to calendar
                Calendar beginTime = Calendar.getInstance();
                beginTime.set(parseResult[2], parseResult[0]-1, parseResult[1], parseResult[3], parseResult[4]);
                //beginTime.set(parseResult[2], 2, 18, 14, 30);
                Calendar endTime = Calendar.getInstance();
                endTime.set(parseResult[2], parseResult[0]-1, parseResult[1], parseResult[3]+1, parseResult[4]);
                //endTime.set(parseResult[2], 2, 18, 15, 30);
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
