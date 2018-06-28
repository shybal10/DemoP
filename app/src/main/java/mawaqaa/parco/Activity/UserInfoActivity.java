package mawaqaa.parco.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import mawaqaa.parco.Others.AndroidMultiPartEntity;
import mawaqaa.parco.Others.SharedPrefsUtils;
import mawaqaa.parco.Others.UrlClass;
import mawaqaa.parco.Others.appConstant;
import mawaqaa.parco.R;
import mawaqaa.parco.Volley.CommandFactory;
import mawaqaa.parco.Volley.VolleyUtils;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class UserInfoActivity extends MainBaseActivity implements View.OnClickListener {
    private static String TAG = "UserInfoActivity";
    Button bunChangePass, update;
    SharedPrefsUtils myPref;
    String CustomerID;
    private ProgressDialog pDialog;
    EditText fName, lName, phone;
    TextView heading;
    TextView email;
    private ProgressBar progressBar;
    TextView change_img_text;
    String ImageFromServer = "";
    private Uri filePath;
    private Uri fileUri;
    long totalSize = 0;
    private static final int PICK_IMAGE_REQUEST = 99;
    RequestQueue requestQueue;
    ImageView user_img;
    Dialog dialog;
    String fNameS, lNameS, phoneS;

    Bitmap bitmap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info_activity);
        defineView();


        GetCustomerProfile();
    }


    private void defineView() {
        requestQueue = Volley.newRequestQueue(this);
        pDialog = new ProgressDialog(UserInfoActivity.this);
        pDialog.setMessage(UserInfoActivity.this.getResources().getString(R.string.wait));
        pDialog.setCancelable(true);
        fName = findViewById(R.id.fName);
        lName = findViewById(R.id.lName);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        change_img_text = findViewById(R.id.change_img_text);
        user_img = findViewById(R.id.user_img);

        progressBar = findViewById(R.id.progressBar);

        bunChangePass = findViewById(R.id.bunChangePass);
        CustomerID = myPref.getStringPreference(this, appConstant.CustomerID);
        bunChangePass.setOnClickListener(this);
        change_img_text.setOnClickListener(this);
        update.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bunChangePass:
//                Intent i = new Intent(UserInfoActivity.this, ForgetPasswordActivity.class);
//                startActivity(i);
                showDialogForgetchangePass();
                break;

            case R.id.change_img_text:
                showFileChooser();
                break;

                //checkValue();
        }
    }


    private void showDialogForgetchangePass() {
        dialog = new Dialog(this);
        heading = (TextView) findViewById(R.id.textView8);
        //heading.setTypeface(Typeface.createFromAsset(getAssets(),"corbel.ttf"));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.change_pass_layout);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final EditText old_pass_edit = dialog.findViewById(R.id.old_pass_edit);
        final EditText new_pass_edit = dialog.findViewById(R.id.new_pass_edit);
        final EditText confirmPassword = dialog.findViewById(R.id.confirm_password);
        final Button change_pass_Btn = dialog.findViewById(R.id.change_pass_Btn);
        final Button cancel =  dialog.findViewById(R.id.cancel_password_btn);
        //old_pass_edit.setTypeface(Typeface.createFromAsset(getAssets(),"corbel.ttf"));
       // new_pass_edit.setTypeface(Typeface.createFromAsset(getAssets(),"corbel.ttf"));
       // confirmPassword.setTypeface(Typeface.createFromAsset(getAssets(),"corbel.ttf"));

        //change_pass_Btn.setTypeface(Typeface.createFromAsset(getAssets(),"corbel.ttf"));
       // cancel.setTypeface(Typeface.createFromAsset(getAssets(),"corbel.ttf"));


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });


        change_pass_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String old_pass = old_pass_edit.getText().toString();
                String new_pass = new_pass_edit.getText().toString();
                String conf_pass = confirmPassword.getText().toString();

                if (old_pass.matches("")) {
                    Toast.makeText(UserInfoActivity.this, getString(R.string.old_pass_empty), Toast.LENGTH_SHORT).show();

                }else if(new_pass.matches("")) {
                    Toast.makeText(UserInfoActivity.this, getString(R.string.new_pass_empty), Toast.LENGTH_SHORT).show();
                }else if(!(new_pass.matches(conf_pass))) {
                    Toast.makeText(UserInfoActivity.this, R.string.pass_not_match, Toast.LENGTH_SHORT).show();
                }else{
                    JSONObject jo = new JSONObject();
                    try {
                        jo.put(appConstant.APIKey, 123456);
                        jo.put(appConstant.LanguageID, 1);
                        jo.put("CustomerID", CustomerID);
                        jo.put("OldPassword",old_pass);
                        jo.put("NewPassword",new_pass);

                        ChangePassword(UrlClass.ChangePassword , jo);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        dialog.show();
    }

    private void ChangePassword(String changePassword, JSONObject jo) {
        showDialog();
        JsonObjectRequest postRequest = new JsonObjectRequest(
                Request.Method.POST,
                changePassword,

                jo,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        hideDialog();
                        try {
                          int  IsSuccess = response.getInt("APIStatus");
                          String  msg = response.getString("APIMessage");
                            if (IsSuccess == 1) {
                                Toast.makeText(UserInfoActivity.this, msg, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            } else {
                                Toast.makeText(UserInfoActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //  Toast.makeText(MainActivity.this,response.toString(),Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //   Handle Error
                        hideDialog();
                        Log.e("Wt", error.toString());
                    }
                });
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 20,
                2,
                2));
        requestQueue.add(postRequest);
    }

    private void checkValue() {
        if (validateFName() == false) {
            Toast.makeText(this,
                    getString(R.string.err_msg_fname), Toast.LENGTH_LONG)
                    .show();

        } else if (validateLName() == false) {
            Toast.makeText(this,
                    getString(R.string.err_msg_lname), Toast.LENGTH_LONG)
                    .show();

        } else if (validatePhone() == false) {

            Toast.makeText(this,
                    getString(R.string.err_msg_mobile), Toast.LENGTH_SHORT)
                    .show();
        } else {
            updateDataUser();
        }
    }

    private void updateDataUser() {
        JSONObject jo = new JSONObject();
        try {
            jo.put(appConstant.APIKey, 123456);
            jo.put(appConstant.LanguageID, 1);
            jo.put("CustomerID", CustomerID);
            jo.put("FirstName", fNameS);
            jo.put("LastName", lNameS);
            jo.put("Mobile", phoneS);
            jo.put("Image", ImageFromServer);

            Log.d(TAG, jo.toString());

            showDialog();
            JsonObjectRequest postRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    UrlClass.UpdateCustomerProfile,
                    jo,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, response.toString());
                            hideDialog();
                            try {
                              int  IsSuccess = response.getInt("APIStatus");
                              String  msg = response.getString("APIMessage");

                                if (IsSuccess == 1) {

                                    Toast.makeText(UserInfoActivity.this, msg, Toast.LENGTH_SHORT).show();

                                    // Log.e("Wt",response.getString(AppConfig.SUCCESS));
                                } else {

                                    Toast.makeText(UserInfoActivity.this, msg, Toast.LENGTH_SHORT).show();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //  Toast.makeText(MainActivity.this,response.toString(),Toast.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //   Handle Error
                            hideDialog();
                            Log.e("Wt", error.toString());
                        }
                    });
            postRequest.setRetryPolicy(new DefaultRetryPolicy(
                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 20,
                    2,
                    2));
            requestQueue.add(postRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean validatePhone() {
        phoneS = phone.getText().toString();
        if (phoneS.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    private boolean validateLName() {
        lNameS = lName.getText().toString();
        if (lNameS.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    private boolean validateFName() {
        fNameS = fName.getText().toString();
        if (fNameS.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_image)), PICK_IMAGE_REQUEST);
    }

    private void GetCustomerProfile() {
        if (isNetworkAvailable()) {
            JSONObject jo = new JSONObject();
            try {
                jo.put(appConstant.APIKey, 123456);
                jo.put("CustomerID", CustomerID);

                Log.d(TAG, jo.toString());

                if (VolleyUtils.volleyEnabled) {
                    showDialog();
                    CommandFactory commandFactory = new CommandFactory();
                    commandFactory.sendPostCommand(UrlClass.GetCustomerProfile, jo);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(UserInfoActivity.this, getString(R.string.alert_no_internet), Toast.LENGTH_LONG).show();
        }
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            Log.d(TAG, data.getData().toString());
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filePath);
                Toast.makeText(this, "" + bitmap, Toast.LENGTH_SHORT).show();

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                Glide.with(this)
                        .load(stream.toByteArray())
                        .apply(RequestOptions.circleCropTransform())
                        .into(user_img);

                Log.d(TAG, filePath.getPath());
                Log.d(TAG, getRealPathFromURI(filePath));

                uploaduserimage();
                //Setting the Bitmap to ImageView
//                imageView_profile.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getRealPathFromURI(Uri contentUri) {

        // can post image
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = this.getContentResolver().query(contentUri,
                proj, // Which columns to return
                null,       // WHERE clause; which rows to return (all rows)
                null,       // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }

    private void uploaduserimage() {
        new UploadFileToServer().execute();
    }

    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            progressBar.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible
            progressBar.setVisibility(View.VISIBLE);
            // updating progress bar value
            progressBar.setProgress(progress[0]);
            // updating percentage value
//            txtPercentage.setText(String.valueOf(progress[0]) + "%");
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(UrlClass.UploadImage);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });


                File sourceFile = new File(getRealPathFromURI(filePath));

                // Adding file data to http body
                entity.addPart("image", new FileBody(sourceFile));

                // Extra parameters if you want to pass to server
                entity.addPart("website",
                        new StringBody("www.parco.com"));
                entity.addPart("email", new StringBody("parco@gmail.com"));

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }
            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e(TAG, "Response from server: " + result);
            try {
                JSONObject jo = new JSONObject(result);

                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "Response from server: " + jo.toString());
                int APIStatus = jo.getInt("APIStatus");
                if (APIStatus == 1) {
                    ImageFromServer = jo.getString("APIMessage");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // showing the server response in an alert dialog
            super.onPostExecute(result);
        }

    }

    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                appConstant.IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create "
                        + appConstant.IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onGetCustomerProfileFailed(JSONObject jsonObject) {
        super.onGetCustomerProfileFailed(jsonObject);
        hideDialog();
    }

    @Override
    public void onGetCustomerProfileSucessfully(JSONObject jsonObject) {
        super.onGetCustomerProfileSucessfully(jsonObject);
        hideDialog();
        Log.d(TAG, jsonObject.toString());

        JSONObject oCustomer = new JSONObject();
        try {
            oCustomer = jsonObject.getJSONObject("oCustomer");
            fName.setText(oCustomer.getString("FirstName"));
            lName.setText(oCustomer.getString("LastName"));
            email.setText(oCustomer.getString("Email"));
            phone.setText(oCustomer.getString("Mobile"));

            ImageFromServer = oCustomer.getString("Image");

            if (ImageFromServer == null) {

            } else {
                Glide.with(this)
                        .load(UrlClass.ImageURL + ImageFromServer)
                        .apply(RequestOptions.circleCropTransform())
                        .into(user_img);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}