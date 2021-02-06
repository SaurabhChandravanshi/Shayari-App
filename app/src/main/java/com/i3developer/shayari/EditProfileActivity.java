package com.i3developer.shayari;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfileActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    EditText editTextName,editTextEmail,editTextDob;
    Button buttonUpdate;
    DatabaseReference referenceDb;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        allInitialization();
        setUpAppBar();

        String UId = mAuth.getUid();//getting UId for updating user data
        if(UId!= null){
            referenceDb = FirebaseDatabase.getInstance().getReference("Users").child(UId);
        }

        // click on editText for date picking
        editTextDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"date picker");
            }
        });

        // click on updateButton for update data
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String dob = editTextDob.getText().toString().trim();
                if(TextUtils.isEmpty(name)){
                    showToast(getApplicationContext(),"नाम आवश्यक है");
                }else {
                    updateProfileData(name,email,dob);
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    // fetching user data for showing in editText.
        referenceDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                assert user != null;
                editTextName.setText(user.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //update profile data
    private void updateProfileData(String name,String email,String dob) {
        User user = new User(name,email,dob);
        referenceDb.setValue(user);
        showToast(getApplicationContext(),"updated successfully");
    }

    private void setUpAppBar() {
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.app_bar_layout);
        //Change the Title of Action Bar
        TextView appBarTitle = getSupportActionBar().getCustomView()
                .findViewById(R.id.app_bar_title);
        TextView appBarLeft = getSupportActionBar().getCustomView()
                .findViewById(R.id.app_bar_left);
        TextView appBarRight = getSupportActionBar().getCustomView()
                .findViewById(R.id.app_bar_right);
        appBarTitle.setText("प्रोफाइल अपडेट");
        appBarLeft.setText("BACK");
        appBarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }



    private void allInitialization(){
        editTextName = findViewById(R.id.editProfile_name);
        editTextEmail = findViewById(R.id.editProfile_email);
        editTextDob = findViewById(R.id.editProfile_dob);
        buttonUpdate = findViewById(R.id.editProfile_updateButton);
        mAuth = FirebaseAuth.getInstance();
    }

    private void showToast(Context context, String text) {
        ContextThemeWrapper themeWrapper = new ContextThemeWrapper(context,R.style.CustomAlertTheme);
        Toast toast = Toast.makeText(themeWrapper,"",Toast.LENGTH_SHORT);
        toast.setText(text);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    // override method for date set in editText
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int date) {

        String DateString = date+"/"+(month+1)+"/"+year;
        editTextDob.setText(DateString);
    }
}