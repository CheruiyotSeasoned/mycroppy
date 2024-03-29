package com.obsuen.mycroppy.activities;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;


import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.obsuen.mycroppy.R;
import com.obsuen.mycroppy.fragments.FeedsFragment;
import com.obsuen.mycroppy.fragments.FirstFragment;
import com.obsuen.mycroppy.fragments.SecondFragment;
import com.obsuen.mycroppy.fragments.ThirdFragment;
import com.obsuen.mycroppy.helpers.Functions;
import com.obsuen.mycroppy.machinelearning.Classifier;

import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewDashBoard extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    FloatingActionButton floatingActionButton;
    ProgressBar progressbar;
    TextView imagname;
    public Uri filePath;
    String filename;
    StorageReference mStorageRef;
    FirebaseStorage mStorage;
    int mInputSize = 224;
    String mModelPath = "plant_disease_model.tflite";
    String mLabelPath = "plant_labels.txt";
    Classifier mClassifier;
    ArrayList<String> diseases = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_new);
        mStorage = FirebaseStorage.getInstance();
        mStorageRef = mStorage.getReference();
        mClassifier = new Classifier(getAssets(), mModelPath, mLabelPath, mInputSize);
        diseases.add("tomato bacterial spot");
        diseases.add("tomato early blight");
        diseases.add("tomato late blight");
        diseases.add("tomato leaf mold");
        diseases.add("tomato septoria leaf spot");
        diseases.add("tomato spider mites two spotted spider mite");
        diseases.add("tomato target spot");
        diseases.add("tomato tomato yellow leaf curl virus");
        diseases.add("tomato tomato mosaic virus");
        diseases.add("tomato healthy");











//
        getSupportActionBar().setTitle("Cropify");
        getSupportActionBar().setSubtitle("Tomato Control Application");
//        String color = "#FF03DAC5";
        String color = "#11CFC5";
        //getSupportActionBar().
//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(color)));
        getSupportActionBar().setBackgroundDrawable(getDrawable(R.drawable.app_bar_layout));
//        getSupportActionBar().hide();

        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                scandialog();
            }
        });

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackground(null);
//        bottomNavigationView.getMenu().getItem(1).setEnabled(false);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
     //   bottomNavigationView.setSelectedItemId(R.id.person);
        loadFrag(new FirstFragment());

    }
    FirstFragment firstFragment = new FirstFragment();
    SecondFragment secondFragment = new SecondFragment();
    ThirdFragment thirdFragment = new ThirdFragment();
    FeedsFragment feedsFragment = new FeedsFragment();

    public void scandialog(){
        final Dialog alertDialog = new Dialog(NewDashBoard.this);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.alert_label_deletechats);
        alertDialog.getWindow().setBackgroundDrawable(NewDashBoard.this.getResources().getDrawable(R.drawable.round_dialog_background));
        LinearLayout btn_camera= alertDialog.findViewById(R.id.archive);
        LinearLayout btn_gallery = alertDialog.findViewById(R.id.Gallery);
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                launchSelectCameraActivity.launch(intent);



            }
        });
        btn_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                launchSelectGalleryActivity.launch(intent);
                alertDialog.dismiss();

            }
        });
        alertDialog.show();
    }
    ActivityResultLauncher<Intent> launchSelectGalleryActivity= registerForActivityResult(new ActivityResultContracts
                    .StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    // do your operation from here....
                    if (data != null && data.getData() != null)
                    {
                        filePath = data.getData();
                        Bitmap selectedImageBitmap=null;
                        try {
                            selectedImageBitmap = MediaStore.Images.Media.getBitmap(NewDashBoard.this.getContentResolver(),filePath);

                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                        List<Classifier.Recognition> results = mClassifier.recognizeImage(selectedImageBitmap);
                        String text = results.get(0).getTitle();
                        String title = diseases.contains(text) ? text : "no prediction";
                        String confidence = String.valueOf(results.get(0).getConfidence());
                        if (!title.equalsIgnoreCase("no prediction")){
                            uploadUserScans(title,confidence,filePath);
                        }
//            String result = Predictor.predictDisease(NewDashBoard.this,photo);
                        showresultdialog(title);

                    }
                }
            });
    ActivityResultLauncher<Intent> launchSelectCameraActivity= registerForActivityResult(new ActivityResultContracts
                    .StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    // do your operation from here....
                    if (data != null && data.getData() != null)
                    {
                        filePath = data.getData();
                        Bitmap selectedImageBitmap=null;
                        try {
                            selectedImageBitmap =BitmapFactory.decodeStream(NewDashBoard.this.getContentResolver().openInputStream(filePath));
//                                    MediaStore.Images.Media.getBitmap(NewDashBoard.this.getContentResolver(),filePath);
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                        List<Classifier.Recognition> results = mClassifier.recognizeImage(selectedImageBitmap);
                        String text = results.get(0).getTitle();
                        String title = diseases.contains(text) ? text : "no prediction";
                        String confidence = String.valueOf(results.get(0).getConfidence());
                        if (!title.equalsIgnoreCase("no prediction")){
                            uploadUserScans(title,confidence,filePath);
                        }
//            String result = Predictor.predictDisease(NewDashBoard.this,photo);
                        showresultdialog(title);

                    }
                }
            });


    // This method will help to retrieve the image
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(resultCode != RESULT_CANCELED) {
//            filePath = data.getData();
//            if (requestCode == 120 && resultCode == RESULT_OK && data != null) {
//                filename = getFileName(NewDashBoard.this, filePath);
//                imagname.setText(filename);
//            }
//            if (requestCode == 100) {
//                // BitMap is data structure of image file which store the image in memory
//                Bitmap photo = (Bitmap) data.getExtras().get("data");
////            classifier.recognizeImage(photo);
//                List<Classifier.Recognition> results = mClassifier.recognizeImage(photo);
//                String text = results.get(0).getTitle();
//                String title = diseases.contains(text) ? text : "no prediction";
//                String confidence = String.valueOf(results.get(0).getConfidence());
//                if (!title.equalsIgnoreCase("no prediction")){
//                    uploadUserScans(title,confidence,filePath);
//                }
////            String result = Predictor.predictDisease(NewDashBoard.this,photo);
//                showresultdialog(title);
//            }
//            if (requestCode == 150 && data != null) {
//                // Get the url of the image from data
//                Uri selectedImageUri = data.getData();
//                Bitmap bitmap = null;//from   w  ww  . j  a  v  a2s.  c om
//                try {
//                    bitmap = BitmapFactory.decodeStream(NewDashBoard.this
//                            .getContentResolver().openInputStream(selectedImageUri));
//                    List<Classifier.Recognition> results = mClassifier.recognizeImage(bitmap);
//                    String text = results.get(0).getTitle();
//                    String title = diseases.contains(text) ? text : "no prediction";
//                    String confidence = String.valueOf(results.get(0).getConfidence());
//                    if (!title.equalsIgnoreCase("no prediction")){
//                        uploadUserScans(title,confidence,filePath);
//                    }
//                    showresultdialog(title);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//            }
//        }else{
//            Toast.makeText(this, "No Image..", Toast.LENGTH_SHORT).show();
//        }
//    }
    private void showresultdialog(String result) {
        final Dialog alertDialog = new Dialog(NewDashBoard.this);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.alert_dialog);
        alertDialog.getWindow().setBackgroundDrawable(this.getResources().getDrawable(R.drawable.d_round_white_background));
        TextView tvMessage_result = alertDialog.findViewById(R.id.textMsg);
        LinearLayout readMore = alertDialog.findViewById(R.id.read_more);
        if (result.equalsIgnoreCase("no prediction")){
            tvMessage_result.setText("Is That a Tomato Leaf?\n Try Again!");
            alertDialog.findViewById(R.id.ivFav).setVisibility(View.VISIBLE);
            readMore.setVisibility(View.GONE);
        }else{
            tvMessage_result.setText(result);
        }
        readMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SuperBottomSheet superBottomSheet = new SuperBottomSheet();
                Bundle bundle = new Bundle();
                bundle.putString("disease_name",result);
                superBottomSheet.setArguments(bundle);
                superBottomSheet.show(getSupportFragmentManager(),"jkkdfifuer");
            }
        });
        alertDialog.show();
    }
    private void uploadUserScans(String title,String accuracy,Uri filePath)
    {
        String pathAndImageName = "Users/Farmscans"+System.currentTimeMillis();
        mStorageRef.child(pathAndImageName).putFile(filePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful())
                {
                    task.getResult().getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageDownloadUrl = uri.toString();
                            sendscans(imageDownloadUrl,accuracy,title);
                        }
                    });
                }
            }
        });
    }

    private void sendscans(String imageDownloadUrl, String accuracy, String title) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String userid = user.getUid();
        assert userid != null;
        DatabaseReference mChatRef = FirebaseDatabase.getInstance().getReference("Farm Scans").child(userid);
        String postId = mChatRef.push().getKey();
        HashMap<String,Object> map = new HashMap<>();
        map.put("predictedname",title);
        map.put("image_url",imageDownloadUrl);
        map.put("timestamp",String.valueOf(System.currentTimeMillis()));
        map.put("accuracy",accuracy);
        mChatRef.child(postId).setValue(map);
    }

    private void showBottomSheetDialog(String result) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog);
        TextView dname = findViewById(R.id.diseasename);
        TextView sysmptoms = findViewById(R.id.symptoms);
        TextView management = findViewById(R.id.management);
//        dname.setText(result);
        bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.show();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.dashboard:
                loadFrag(firstFragment);
                return true;

            case R.id.second_fragment:
                loadFrag(secondFragment);
                return true;

            case R.id.fourth_fragment:
                loadFrag(thirdFragment);
                return true;
            case R.id.feeds_fragment:
                loadFrag(feedsFragment);
                return true;
        }
        return false;
    }

    public void loadFrag(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }
    public static class SuperBottomSheet extends SuperBottomSheetFragment
    {
        TextView dname,symptoms,management;
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);
            View view = inflater.inflate(R.layout.bottom_sheet_dialog, container, false);
            return view;

        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            dname = view.findViewById(R.id.diseasename);
            symptoms = view.findViewById(R.id.symptoms);
            management = view.findViewById(R.id.management);
            Bundle bundle = getArguments();
            dname.setText(bundle.getString("disease_name"));

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_app_bar_menu,menu);



        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.favorite:
                loadFrag(new ThirdFragment());
                break;
            case R.id.addrecord:
                showAlertDialogButtonClicked();
                break;
        }
        return super.onOptionsItemSelected(item);

    }
    public void showAlertDialogButtonClicked()
    {

        // Create an alert builder
        AlertDialog.Builder builder
                = new AlertDialog.Builder(NewDashBoard.this);
//        builder.setTitle("Contribute Add More Diseases");

        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.update,null);
        builder.setView(customLayout);
        Button btnsend = customLayout.findViewById(R.id.btnSend);
        Button btncancel = customLayout.findViewById(R.id.btncancel);
        CircleImageView img = customLayout.findViewById(R.id.d_img);
        progressbar = customLayout.findViewById(R.id.progressbar);
        Drawable drawable = getResources().getDrawable(R.drawable.progress_setup);
        progressbar.setProgressDrawable(drawable);
        imagname = customLayout.findViewById(R.id.treename);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
                try{

                }catch (Exception e){
                    Log.d("hfhf",e.getMessage());
                }
            }
        });

        AlertDialog dialog = builder.create();
        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText name = customLayout.findViewById(R.id.txtName);
                EditText desc = customLayout.findViewById(R.id.txtDesc);
                EditText farm = customLayout.findViewById(R.id.txtfname);
                EditText phone = customLayout.findViewById(R.id.txtMob);
                if (name.getText().toString().isEmpty())
                {
                    name.setError("Enter Disease Name");
                    name.requestFocus();
                }
                else if (desc.getText().toString().isEmpty())
                {
                    desc.setError("Enter Description");
                    desc.requestFocus();
                }
                else if (farm.getText().toString().isEmpty())
                {
                    farm.setError("Enter Farm Name");
                    farm.requestFocus();
                }
                else if (phone.getText().toString().isEmpty())
                {
                    phone.setError("Enter Your Phone");
                    phone.requestFocus();
                }
                else {
                    progressbar.setVisibility(View.VISIBLE);
                    Functions.showLoader(NewDashBoard.this,true,false);
                    uploadprofile(progressbar,filePath,name.getText().toString(),desc.getText().toString(),farm.getText().toString(),phone.getText().toString(),dialog);
                }






            }
        });
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        // add a button
//        builder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog,int which)
//                            {
//                                // send data from the
//                                // AlertDialog to the Activity
//                                EditText editText = customLayout.findViewById(R.id.txtName);
//                                sendDialogDataToActivity(editText.getText().toString());
//                            }
//                        });

        // create and show
        // the alert dialog

        dialog.show();
    }
    private void senddataDialogDataToActivity(ProgressBar progressBar, String imageDownloadUrl, String d_name, String d_desc, String f_name, String phone, AlertDialog dialog)
    {
        DatabaseReference mChatRef = FirebaseDatabase.getInstance().getReference("Diseases").child("posted");
        String postId = mChatRef.push().getKey();
        HashMap<String,Object> map = new HashMap<>();
        map.put("name",d_name);
        map.put("description",d_desc);
        map.put("image_url",imageDownloadUrl);
        map.put("timestamp",String.valueOf(System.currentTimeMillis()));
        map.put("farm name",f_name);
        map.put("phone number",phone);
        mChatRef.child(postId).setValue(map);
        progressbar.setVisibility(View.GONE);
        Functions.cancelLoader();

        Toast.makeText(NewDashBoard.this, "Disease Posted",Toast.LENGTH_SHORT).show();
        dialog.dismiss();

    }
    private void chooseImage()
    {
        Intent intent = new Intent();
        intent.setAction(intent.ACTION_PICK);intent.setType("image/*");
        startActivityForResult(intent,120);
    }

    //UPLOAD IMAGE
    private void uploadprofile(ProgressBar progressBar,Uri filePath,String d_name, String d_desc, String f_name, String phone, AlertDialog dialog)
    {
        String pathAndImageName = "Users/profile_images_"+System.currentTimeMillis();
        mStorageRef.child(pathAndImageName).putFile(filePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful())
                {
                    task.getResult().getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageDownloadUrl = uri.toString();
                            senddataDialogDataToActivity(progressBar,imageDownloadUrl,d_name,d_desc,f_name,phone,dialog);
                        }
                    });
                }
            }
        });
    }
    @SuppressLint("Range")
    public static String getFileName(Context context, Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

}

