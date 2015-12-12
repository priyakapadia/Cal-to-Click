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

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
//import org.apache.http.entity.mime.HttpMultipartMode;
//import org.apache.http.entity.mime.MultipartEntityBuilder;
//import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.net.URI;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


//Made using this tutorial: https://www.youtube.com/watch?v=k-3zXb7GteU

public class CamActivity extends AppCompatActivity
{

    private static String logtag = "CameraApp8";
    private static int TAKE_PICTURE = 1;
    private Uri imageUri;
    public boolean initialCounter = true;

    CommsEngine commsEngine;
    private  final  String idol_ocr_service = "http://api.idolondemand.com/1/api/sync/ocrdocument/v1";
    private String jobID = "";

    //protected void onTrialForHomePage(int requestCode, int resultCode,Intent intent) {

    // }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        System.out.println("BITCH");

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

        /*
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
**/


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

            /*if(selectedImage == null)
            {
                Log.e("Fuckin rip", "idk man ):");
            }
            Log.e(logtag, selectedImage.getPath().toString());
            // getContentResolver().notifyChange(selectedImage, null);
            try
            {
                Map<String,String> map =  new HashMap<String,String>();
                map.put("file", selectedImage.getPath().toString());
                String fileType = "image/jpeg";
                map.put("mode", "document_scan");
                commsEngine.ServicePostRequest(idol_ocr_service, fileType, map, new OnServerRequestCompleteListener()
                {
                    @Override
                    public void onServerRequestComplete(String response) {
                        System.out.println(response);
                        try {
                            JSONObject mainObject = new JSONObject(response);
                            if (!mainObject.isNull("jobID")) {
                                jobID = mainObject.getString("jobID");
                            } else
                                ParseSyncResponse(response);
                        } catch (Exception ex)
                        {
                            Log.e("STRING IS NULL",ex.toString());
                        }
                    }
                    @Override
                    public void onErrorOccurred(String error)
                    {
                        Log.e("Some error", error);
                    }
                });
            }

            catch(Exception e)
            {
                Log.e("????", e.toString());

            }//end of catch*/
            try
            {
                URI uri = new URI(idol_ocr_service);
                HttpPost httpPost = new HttpPost();
                httpPost.setURI(uri);
                MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();
                reqEntity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                reqEntity.addPart("apikey", new StringBody("9246b321-e229-41bb-a5a2-62a323897ce8", ContentType.TEXT_PLAIN));
                reqEntity.addBinaryBody("file", new File(selectedImage.getPath().toString()));
                reqEntity.addPart("mode", new StringBody("document_photo", ContentType.TEXT_PLAIN));
                httpPost.setEntity(reqEntity.build());
                //HTTPClient httpClient = new DefaultHttpClient();
                CloseableHttpClient httpclient = HttpClients.createDefault();

                HttpResponse response = httpclient.execute(httpPost);
                Log.e(logtag,"Successs?");
            }
            catch(Exception e)
            {
                Log.e(logtag,"fml");
            }
            //parsing code for data taken from picture
            int[] result = DataParsing.main();

            //Adding event to calendar
            Calendar beginTime = Calendar.getInstance();
            // beginTime.set(result[2], result[0]-1, result[1], result[3], result[4]);
            beginTime.set(result[2], 2, 18, 14, 30);
            Calendar endTime = Calendar.getInstance();
            //endTime.set(result[2], result[0]-1, result[1], result[3]+1, result[4]);
            endTime.set(result[2], 2, 18, 15, 30);
            Intent intent5 = new Intent(Intent.ACTION_INSERT);
            intent5.setData(CalendarContract.Events.CONTENT_URI);
            intent5.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis());
            intent5.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis());
            intent5.putExtra(CalendarContract.Events.TITLE, "Hey Now");
            startActivity(intent5);


        }// end of if
    }//end of onActivityResult

    private String ParseSyncResponse(String response) {
        String retText = "";
        if (response == null) {
            Toast.makeText(this, "Unknown error occurred. Try again", Toast.LENGTH_LONG).show();
            return "";
        }
        try {
            JSONObject mainObject = new JSONObject(response);
            JSONArray textBlockArray = mainObject.getJSONArray("text_block");
            int count = textBlockArray.length();
            if (count > 0) {
                for (int i = 0; i < count; i++) {
                    JSONObject texts = textBlockArray.getJSONObject(i);
                    String text = texts.getString("text");
                    retText += text;
                }
                System.out.println("OCR'D BITCH\n" + retText);
                return retText;
            }
            else
                Toast.makeText(this, "Not available", Toast.LENGTH_LONG).show();
        } catch (Exception ex)
        {
            Log.e(getApplicationContext().toString(),ex.toString());
        }
        return "";
    }
}//end of class
