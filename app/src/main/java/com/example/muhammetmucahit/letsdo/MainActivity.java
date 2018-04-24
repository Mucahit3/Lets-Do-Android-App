package com.example.muhammetmucahit.letsdo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import com.example.muhammetmucahit.letsdo.utilities.Activity;
import com.example.muhammetmucahit.letsdo.utilities.NetworkUtils;
import com.example.muhammetmucahit.letsdo.utilities.OpenActivityJsonUtils;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView mResultsTextView;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private ImageView mResultsImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mResultsTextView = findViewById(R.id.tv_result_json);
        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        mResultsImageView = findViewById(R.id.iv_result_image);
    }

    private void makeApiQuery() {
        URL apiUrl = NetworkUtils.buildUrl("");
        new ApiQueryTask().execute(apiUrl);
    }

    private void showJsonDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mResultsTextView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mResultsTextView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    public class ApiQueryTask extends AsyncTask<URL, Void, Activity> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected Activity doInBackground(URL... params) {
            URL searchUrl = params[0];
            String activityRandomResult;
            try {
                activityRandomResult = NetworkUtils.getResponseFromHttpUrl(searchUrl);

                Activity[] simpleJsonActivityData = OpenActivityJsonUtils.getSimpleActivityStringFromJson(activityRandomResult);

                Date now = Calendar.getInstance().getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
                String nowString = dateFormat.format(now);
                int currentMonth = Integer.parseInt(nowString.substring(0, 2));

                Random rand = new Random();

                if (currentMonth >= 3 && currentMonth <= 8) {
                    ArrayList<Activity> summerActivities = OpenActivityJsonUtils.getActivitiesForSummer(simpleJsonActivityData);
                    int activityNumber = rand.nextInt(summerActivities.size());
                    return summerActivities.get(activityNumber);
                }
                else {
                    ArrayList<Activity> winterActivities = OpenActivityJsonUtils.getActivitiesForWinter(simpleJsonActivityData);
                    int activityNumber = rand.nextInt(winterActivities.size());
                    return winterActivities.get(activityNumber);
                }

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Activity result) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (result != null) {
                showJsonDataView();
                mResultsTextView.setText(result.name);
                new DownLoadImageTask(mResultsImageView).execute(result.image);
            } else {
                showErrorMessage();
            }
        }
    }

    public class DownLoadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownLoadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String...urls) {
            String urlOfImage = urls[0];
            Bitmap image = null;
            try {
                InputStream is = new URL(urlOfImage).openStream();
                image = BitmapFactory.decodeStream(is);
            } catch(Exception e){
                e.printStackTrace();
            }
            return image;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_invoke) {
            makeApiQuery();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

