package com.example.registrationapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditActivity extends AppCompatActivity {

    public static int user_id = 0;
    DBHandler db;
    Button btnSaveChanges;
    EditText etName, etAddress, etGender, etBirthdate, etContact, etEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        db = new DBHandler(this);

        etName = findViewById(R.id.etName);
        etAddress = findViewById(R.id.etAddress);
        etGender = findViewById(R.id.etGender);
        etBirthdate = findViewById(R.id.etBirthdate);
        etContact = findViewById(R.id.etContact);
        etEmail = findViewById(R.id.etEmail);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);

        //Getting & initializing clicked ID to variable id(Integer)
        Bundle values = getIntent().getExtras();
        if(values == null) { return; }
        int id = values.getInt("value");
        user_id = id;

        //Find specific row in database table user
        SQLiteDatabase db = getApplicationContext().openOrCreateDatabase("dbUser.db", Context.MODE_PRIVATE, null);
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery("SELECT * FROM tblUser WHERE id=" + id, null);
        while (cursor.moveToNext()) {
            int index2 = cursor.getColumnIndex("name");
            int index3 = cursor.getColumnIndex("address");
            int index4 = cursor.getColumnIndex("gender");
            int index5 = cursor.getColumnIndex("birthdate");
            int index6 = cursor.getColumnIndex("contact");
            int index7 = cursor.getColumnIndex("email");

            String name = cursor.getString(index2);
            String address = cursor.getString(index3);
            String gender = cursor.getString(index4);
            String birthdate = cursor.getString(index5);
            String contact = cursor.getString(index6);
            String email = cursor.getString(index7);

            //Populate EditText from found row's data in user table
            etName.setText(name);
            etAddress.setText(address);
            etGender.setText(gender);
            etBirthdate.setText(birthdate);
            etContact.setText(contact);
            etEmail.setText(email);
        }
    }

    //To go back to MainActivity (Landing Page)
    public void OnClickBack(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    //Function for displaying message
    public void toastMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    //Perform Onclick for saving the changes/updates of user data
    //I use Onclick in xml file (activity.xml)
    public void  OnClickSaveChanges(View view) {
        try {
            User user = new User(user_id,
                    etName.getText().toString(),
                    etAddress.getText().toString(),
                    etGender.getText().toString(),
                    etBirthdate.getText().toString(),
                    etContact.getText().toString(),
                    etEmail.getText().toString());
            db.update(user);
            OnClickBack(view);
            toastMessage("Data Successfully Updated");

        } catch (Exception e) { toastMessage(e.getMessage()); }
    }
}