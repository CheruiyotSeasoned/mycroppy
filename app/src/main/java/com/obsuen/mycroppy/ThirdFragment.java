package com.obsuen.mycroppy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;

public class ThirdFragment extends Fragment {

    private static final int RESULT_OK = -1;

    public ThirdFragment(){
        // require a empty public constructor
    }
    Context contexts;
    TextView logout;
    TextView email,email2;
    CircleImageView img,profile_image;
    public Uri filePath;
    StorageReference mStorageRef;
    FirebaseStorage mStorage;
    PrefManager pfmanager;
    String filename;
    String userid;
    ProgressBar progressbar;
    TextView imagname,edit_tv,username,username1,address,phone_;
    Boolean valid = false;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private Realm realm;
    List<ProfileModel> dataModals;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_third, container, false);
        logout = view.findViewById(R.id.logout);
        email  =view.findViewById(R.id.emailtv);
        email2  =view.findViewById(R.id.emailtv2);
        img = view.findViewById(R.id.d_img);
        edit_tv = view.findViewById(R.id.edittv);
        username1 = view.findViewById(R.id.username1);
        userid = user.getUid();
        realm = Realm.getDefaultInstance();
        Log.d("ejf",userid);
        dataModals = new ArrayList<>();

        username = view.findViewById(R.id.username);
        phone_ = view.findViewById(R.id.phone);
        profile_image = view.findViewById(R.id.profile_image);
        address = view.findViewById(R.id.address);

        getProfileDetails(userid);
        pfmanager = new PrefManager(contexts);
        email.setText(pfmanager.getEmail());
        mStorage = FirebaseStorage.getInstance();
        mStorageRef = mStorage.getReference();
        edit_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showeditAlertDialogButtonClicked(view);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialogButtonClicked(view);
            }
        });
        return view;
    }
    public Boolean checkemail(EditText emaill){
        String user_email = emaill.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        emaill.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                if (user_email.matches(emailPattern) && s.length() > 0)
                {
                    // or
                    emaill.setTooltipText("valid email");
                    Toast.makeText(contexts, "Valid email", Toast.LENGTH_SHORT).show();
                    valid = true;
                }
                else
                {
                    Toast.makeText(contexts,"Invalid email address",Toast.LENGTH_SHORT).show();
                    //or
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // other stuffs
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // other stuffs
            }
        });
        return valid;
    }

    public void showeditAlertDialogButtonClicked(View view)
    {

        // Create an alert builder
        AlertDialog.Builder builder
                = new AlertDialog.Builder(contexts);
//        builder.setTitle("Contribute Add More Diseases");

        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.edit_info,null);
        builder.setView(customLayout);
        Button btnsend = customLayout.findViewById(R.id.btnSend);
        Button btncancel = customLayout.findViewById(R.id.btncancel);
        CircleImageView img = customLayout.findViewById(R.id.d_img);
        progressbar = customLayout.findViewById(R.id.progressbar);
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
                EditText email = customLayout.findViewById(R.id.emailed);
                EditText address = customLayout.findViewById(R.id.txtfname);
                EditText phone = customLayout.findViewById(R.id.txtMob);


                if (name.getText().toString().isEmpty())
                {
                    name.setError("Enter Username");
                    name.requestFocus();
                }
                else if(name.getText().toString().length()<3){
                    name.setError("Name must not be less than 3 charaters");
                    name.requestFocus();
                }
                else if (email.getText().toString().isEmpty())
                {
                    email.setError("Enter Email");
                    email.requestFocus();
                }


                else if (address.getText().toString().isEmpty())
                {
                    address.setError("Enter Address Name");
                    address.requestFocus();
                }
                else if (phone.getText().toString().isEmpty())
                {
                    phone.setError("Enter Your Phone");
                    phone.requestFocus();
                }
                else if (phone.getText().toString().length()<10){
                    phone.setError("10 characters needed");
                    phone.requestFocus();
                }
                else {
                    progressbar.setVisibility(View.VISIBLE);
                    uploadImage(progressbar,filePath,name.getText().toString(),email.getText().toString(),address.getText().toString(),phone.getText().toString(),dialog);
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
    public void showAlertDialogButtonClicked(View view)
    {

        // Create an alert builder
        AlertDialog.Builder builder
                = new AlertDialog.Builder(contexts);
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
    private void getProfileDetails(String id)
    {
        DatabaseReference mRef;
        mRef = FirebaseDatabase.getInstance().getReference();
        mRef.child("Users").child(id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String groupName = dataSnapshot.child("name").getValue(String.class);
                        String Email = dataSnapshot.child("Email").getValue(String.class);
                        String Address = dataSnapshot.child("Address").getValue(String.class);
                        String phone = dataSnapshot.child("phone number").getValue(String.class);
                        String profileimage = dataSnapshot.child("profile_image").getValue(String.class);
                        //Get Data

                        //Set Data

                        username.setText(groupName);
                        username1.setText(groupName);
                        email.setText(Email);
                        email2.setText(Email);
                        try {
                            //Picasso.get().load(groupImage).into(groupImageIcon);
                            Glide.with(contexts)
                                    .load(profileimage)
                                    .placeholder(R.drawable.profile_round)
                                    .into(profile_image);
                        }
                        catch (Exception e )
                        {
                            // Picasso.get().load(R.drawable.group_image).into(groupImageIcon);
                        }
                        address.setText(Address);
                        phone_.setText(phone);

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        dataModals = realm.where(ProfileModel.class).findAll();
//                        ProfileModel model = dataModals.get(0);
//                        Toast.makeText(contexts, "ghfugb"+model.getName(), Toast.LENGTH_LONG).show();

                        username.setText(pfmanager.getName());
                        username1.setText(pfmanager.getName());
                        email.setText(pfmanager.getEmailProfile());
                        email2.setText(pfmanager.getEmailProfile());
                        address.setText(pfmanager.getAddress());
                        phone_.setText(pfmanager.getPhone());
                        try {
                            //Picasso.get().load(groupImage).into(groupImageIcon);
                            Glide.with(contexts)
                                    .load(pfmanager.getImageUrl())
                                    .placeholder(R.drawable.profile_round)
                                    .into(profile_image);
                        }
                        catch (Exception e )
                        {
                            // Picasso.get().load(R.drawable.group_image).into(groupImageIcon);
                        }

                    }

                });
    }


    private void sendDialogDataToActivity(ProgressBar progressBar,String imageDownloadUrl, String d_name, String d_desc, String f_name, String phone, AlertDialog dialog)
    {
        DatabaseReference mChatRef = FirebaseDatabase.getInstance().getReference("Users");
        String postId = userid;
        HashMap<String,Object> map = new HashMap<>();
        map.put("name",d_name);
        map.put("Email",d_desc);
        map.put("profile_image",imageDownloadUrl);
        map.put("timestamp",String.valueOf(System.currentTimeMillis()));
        map.put("Address",f_name);
        map.put("phone number",phone);
        mChatRef.child(postId).setValue(map);
//        addDataToDatabase(d_name, d_desc, imageDownloadUrl, f_name,phone);
        new PrefManager(contexts).saveProfileDetails(d_name,d_desc,f_name,phone,imageDownloadUrl);
        progressbar.setVisibility(View.GONE);
        Toast.makeText(contexts, "Disease Posted",Toast.LENGTH_SHORT).show();
        dialog.dismiss();

    }


    private void senddataDialogDataToActivity(ProgressBar progressBar,String imageDownloadUrl, String d_name, String d_desc, String f_name, String phone, AlertDialog dialog)
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

        Toast.makeText(contexts, "Disease Posted",Toast.LENGTH_SHORT).show();
        dialog.dismiss();

    }
    private void chooseImage()
    {
        Intent intent = new Intent();
        intent.setAction(intent.ACTION_PICK);intent.setType("image/*");
        startActivityForResult(intent,100);
    }
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        filePath = data.getData();

        if (requestCode == 100 && resultCode==RESULT_OK && data!=null)
        {
            filename = getFileName(contexts,filePath);
            imagname.setText(filename);
        }
//        if(requestCode==200 && resultCode==RESULT_OK && data!=null){
//            Toast.makeText(contexts
//                    , "PDF Choosen", Toast.LENGTH_SHORT).show();
//            uploadDocuments(filePath);
//        }
        if(requestCode==100 && data==null){
            Toast.makeText(contexts, "No document selected", Toast.LENGTH_SHORT).show();
        }
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
    private void uploadImage(ProgressBar progressBar,Uri filePath,String d_name, String d_desc, String f_name, String phone, AlertDialog dialog)
    {
        String pathAndImageName = "Diseases/posted/images_"+System.currentTimeMillis();
        mStorageRef.child(pathAndImageName).putFile(filePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful())
                {
                    task.getResult().getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageDownloadUrl = uri.toString();
                            sendDialogDataToActivity(progressBar,imageDownloadUrl,d_name,d_desc,f_name,phone,dialog);
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
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        contexts= context;
    }
}
