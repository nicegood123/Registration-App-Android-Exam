package com.example.registrationapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, SearchView.OnQueryTextListener {

    DBHandler db;
    ListView lvUser;
    SearchView svSearch;
    ArrayAdapter userArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvUser = findViewById(R.id.lvUser);
        svSearch = findViewById(R.id.svSearch);

        db = new DBHandler(this);
        displayUsers();

        lvUser.setOnItemClickListener(this);
        svSearch.setOnQueryTextListener(this);
    }

    //Function for displaying list of users to ListView
    public void displayUsers() {
        try {
            List<User> users_list = db.loadToListView();
            userArray = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, users_list);
            lvUser.setAdapter(userArray);
        } catch (Exception e) { toastMessage(e.getMessage()); }
    }

    //OnClick button for user registration to redirect to RegisterActivity class(Registration Page)
    public void OnClickRegister(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    //Function for displaying message
    public void toastMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }


    //ListView onItemClick for showing alert dialog to choose action (Delete, Edit)
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            User user = (User) parent.getItemAtPosition(position);
            AlertDialog.Builder ca= new AlertDialog.Builder(MainActivity.this, R.style.AlertDialog);

            ca.setPositiveButton("EDIT", (dialog, which) -> {

                Intent i = new Intent(MainActivity.this, EditActivity.class);
                i.putExtra("value", user.getId());
                startActivity(i);
            })
                    .setNegativeButton("DELETE", (dialog, which) -> {
                        db.delete(user);
                        toastMessage("Data Successfully Deleted");
                        displayUsers();
                    });


            AlertDialog alert = ca.create();
            alert.setTitle("Choose Action");
            alert.show();
        } catch (Exception e) { toastMessage(e.getMessage()); }
    }

    @Override
    public boolean onQueryTextSubmit(String query) { return false; }

    //Overriding onQueryTextChange Function for user's search
    @Override
    public boolean onQueryTextChange(String newText) {
        try {
            userArray.getFilter().filter(newText);
            return false;
        }catch (Exception e){
            displayUsers();
            return false;
        }
    }
}