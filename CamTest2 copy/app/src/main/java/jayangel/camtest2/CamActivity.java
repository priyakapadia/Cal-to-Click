package jayangel.camtest2;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileObserver;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;


import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;

//Made using this tutorial: https://www.youtube.com/watch?v=k-3zXb7GteU



public class CamActivity extends AppCompatActivity
{
    private static String logtag = "CameraApp8";
    private static int TAKE_PICTURE = 1;
    private Uri imageUri;
    private OnClickListener cameraListener = new OnClickListener(){
        public void onClick(View v){
            takePhoto(v);
        }
    };

@Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cam);

        Button cameraButton = (Button)findViewById(R.id.button_camera);
        cameraButton.setOnClickListener(cameraListener);
    }

    private void takePhoto(View v)
    {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File photo;
        photo = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"picture.jpg");
        imageUri = Uri.fromFile(photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PICTURE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);

        if(resultCode == Activity.RESULT_OK)
        {
            Uri selectedImage = imageUri;
            getContentResolver().notifyChange(selectedImage,null);

            ImageView imageView = (ImageView)findViewById(R.id.image_camera);
            ContentResolver cr = getContentResolver();
            Bitmap bitmap;

            try
            {
                bitmap = MediaStore.Images.Media.getBitmap(cr,selectedImage);
                imageView.setImageBitmap(bitmap);
                Toast.makeText(CamActivity.this, selectedImage.toString(),Toast.LENGTH_LONG).show();

                /****************/
                /***** EDIT ****/
                /**************/
                //need path location of the selectedImage object
                //processPictureWhenReady(picturePath); //picturePath = selectedImage.<path>
            }



            catch(Exception e)
            {
                Log.e(logtag, e.toString());
            }//end of catch

        }// end of if

    } //end of onActivityResult


    public void toastResult(String result)
    {
        Toast.makeText(getApplicationContext(), result,
                Toast.LENGTH_LONG).show();
    }


    private static class IODOCRTask extends AsyncTask<File,Void,String>
    {
        /*private MainActivity activity;

        protected IODOCRTask(MainActivity activity)
        {
            this.activity = activity;
        }*/

        @Override
        protected String doInBackground(File... params)
        {
            File file = params[0];
            String result="";

            try
            {

                HttpResponse<JsonNode> response = Unirest
                        .post("http://api.idolondemand.com/1/api/sync/ocrdocument/v1")
                        .field("file",file)
                        .field("mode", "scene_photo")
                        .field("apikey","9246b321-e229-41bb-a5a2-62a323897ce8")
                        .asJson();

                JSONObject textblock =(JSONObject) response.getBody().getObject().getJSONArray("text_block").get(0);
                result=textblock.getString("text");
            }

            catch (Exception e)
            {
                // keeping error handling simple
                e.printStackTrace();
            }

            //Log.i("MARTIN"+file.getName(),result);

            return result;

        }
        @Override
        protected void onPostExecute(String result)
        {
            // TODO Auto-generated method stub
            /*activity.*///toastResult(result);
            System.out.println(result);
        }
    }


    private void processPictureWhenReady(final String picturePath)
    {
        final File pictureFile = new File(picturePath);


        if (pictureFile.exists())
        {
            //Log.i("MARTIN", "FILE EXISTS");
            Toast.makeText(getApplicationContext(), "FILE IS WRITTEN",
                    Toast.LENGTH_SHORT).show();

            File dir = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            Bitmap b = BitmapFactory.decodeFile(picturePath);
            Bitmap out = Bitmap.createScaledBitmap(b, 640, 960, false);

            File file = new File(dir, "resize.png");
            FileOutputStream fOut;
            try
            {
                fOut = new FileOutputStream(file);
                out.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                fOut.flush();
                fOut.close();
                b.recycle();
                out.recycle();

                new IODOCRTask().execute(pictureFile);
                new IODOCRTask().execute(file);

            }

            catch (Exception e)
            { // TODO
                e.printStackTrace();
            }


            // The picture is ready; process it.
        }

        else
        {
            // The file does not exist yet. Before starting the file observer, you
            // can update your UI to let the user know that the application is
            // waiting for the picture (for example, by displaying the thumbnail
            // image and a progress indicator).


            final File parentDirectory = pictureFile.getParentFile();
            //	Toast.makeText(getApplicationContext(), parentDirectory.getPath(),
            //		Toast.LENGTH_SHORT).show();

            FileObserver observer = new FileObserver(parentDirectory.getPath(),
                    FileObserver.CLOSE_WRITE | FileObserver.MOVED_TO)
            {
                private boolean isFileWritten;

                @Override
                public void onEvent(int event, String path)
                {
                    //Log.i("MARTINEVENT", event+","+path);

                    if (!isFileWritten)
                    {
                        //Log.i("MARTINEVENT", isFileWritten+",");

                        File affectedFile = new File(parentDirectory, path);
                        isFileWritten = affectedFile.equals(pictureFile);

                        if (isFileWritten)
                        {
                            stopWatching();

                            runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    processPictureWhenReady(picturePath);
                                }
                            });
                        }
                    }
                }
            };
            observer.startWatching();
        }

    }

}//end of class




