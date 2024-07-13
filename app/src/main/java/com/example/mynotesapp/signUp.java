package com.example.mynotesapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class signUp extends AppCompatActivity {
    Button signUp;
    EditText username, email, password;
    ImageView profile_pic;
    Intent intent;
    FirebaseAuth auth;
    String Email, Password, UserName;
    ActivityResultLauncher<Intent> resultLauncher;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    EachNotes eachNotes;
    Uri imageUri;
    String imageUrl;
    StorageReference storageReference;
    ArrayList<EachNotes> All;

    ArrayList<EachNotes> Important;
    ArrayList<EachNotes> Fitness;
    ArrayList<EachNotes> Work;
    ArrayList<EachNotes> Improvement;
    ArrayList<EachNotes> Others;
    private final  int REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        set_ids();
        register();
        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickimage();
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Email = email.getText().toString();
                UserName = username.getText().toString();
                Password = password.getText().toString();
                if (!Email.isEmpty() && !UserName.isEmpty() && !Password.isEmpty()&&imageUri!=null) {

                    auth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                upload_to_firebase_storage();
                            } else {
                                Toast.makeText(signUp.this, "Check Connection", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(signUp.this, "Please fill all fields", Toast.LENGTH_SHORT).show();

                }
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
                       create_user();
                   }
               });
            }
        });
    }
    public void create_user()
    {
        HashMap<String, ArrayList<EachNotes>> notes = new HashMap<>();


        User user = new User(imageUrl, notes,UserName);
        databaseReference.child(auth.getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(signUp.this, "User Created", Toast.LENGTH_SHORT).show();
                Intent to_login=new Intent(signUp.this,MainActivity.class);
                startActivity(to_login);
                finish();
            }
        });
    }
    public void pickimage() {
        Intent intent_gallry = new Intent(MediaStore.ACTION_PICK_IMAGES);
        resultLauncher.launch(intent_gallry);
    }
    public void set_ids() {
        signUp = findViewById(R.id.sign_up);
        username = findViewById(R.id.et_username);
        email = findViewById(R.id.et_email);
        password = findViewById(R.id.et_password);
        auth = FirebaseAuth.getInstance();
        profile_pic = findViewById(R.id.profile_image);
        databaseReference=firebaseDatabase.getInstance().getReference("user");

    }

    public void register() {
        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null && data.getData() != null) {
                        imageUri = data.getData();
                        profile_pic.setImageURI(imageUri);
                    } else {
                        Toast.makeText(signUp.this, "Image not selected", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(signUp.this, "Image selection canceled", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
