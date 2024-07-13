package com.example.mynotesapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UpdateProfile extends AppCompatActivity {
       Button logout,Update;
       FirebaseAuth auth;
       EditText UserName;
       ImageView ProfilePick;
     StorageReference storageReference;
    ActivityResultLauncher<Intent> resultLauncher;
    Uri imageUri;
    String Profilepick,Username;
    String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_profile);
        auth=FirebaseAuth.getInstance();
        logout=findViewById(R.id.btn_logout);
        UserName=findViewById(R.id.Update_profile_userName);
        ProfilePick=findViewById(R.id.Update_profile_pick);
        Update=findViewById(R.id.btn_update_profile);
         Username=getIntent().getStringExtra("username");
         Profilepick=getIntent().getStringExtra("profilePick");
         imageUrl=Profilepick;


         register();
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent intent= new Intent(UpdateProfile.this,MainActivity.class);
                startActivity(intent);
            }
        });
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Username=UserName.getText().toString();
                if (imageUri==null)
                {
                    Update_Profile();

                }
                else {
                    upload_to_firebase_storage();
                    Update_Profile();

                }


            }
        });
        ProfilePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               pickimage();
            }
        });



    }
    public void upload_to_firebase_storage()
    {
        SimpleDateFormat current_date_time=new SimpleDateFormat("yyyy_MMdd_HH_mm_ss", Locale.CANADA);
        Date now=new Date();
        String filename=current_date_time.format(now);
        storageReference= FirebaseStorage.getInstance().getReference("images/"+filename);
        storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        imageUrl=uri.toString();

                    }
                });
            }
        });
    }
    void Update_Profile()
    {
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("user");
        databaseReference.child(auth.getCurrentUser().getUid()).child("username").setValue(Username);
        databaseReference.child(auth.getCurrentUser().getUid()).child("imageUrl").setValue(imageUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent intent= new Intent(UpdateProfile.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }
    public void pickimage() {
        Intent intent_gallry = new Intent(MediaStore.ACTION_PICK_IMAGES);
        resultLauncher.launch(intent_gallry);
    }
    public void register() {
        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null && data.getData() != null) {
                        imageUri = data.getData();
                        ProfilePick.setImageURI(imageUri);
                    } else {
                        Toast.makeText(UpdateProfile.this, "Image not selected", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(UpdateProfile.this, "Image selection canceled", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Glide.with(UpdateProfile.this).load(Profilepick).into(ProfilePick);
        UserName.setText(Username);

    }
}