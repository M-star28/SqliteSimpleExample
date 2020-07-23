package com.example.sqlitesimpleexample;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DialogTitle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.widget.Toast.LENGTH_LONG;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText  etName, etResult, etScore, etSearch;
    Button btnSelect, btnDelete, btnViewAll, btnUpdate, btnAdd;

    SQLiteDatabase myDB;

    String  sSearch, sName, sResult, sScore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        // create a database

        myDB = openOrCreateDatabase( "LeagueDB", Context.MODE_PRIVATE, null );

        // create table with the name of TeamTable

        myDB.execSQL( "CREATE TABLE IF NOT EXISTS TEAMS (TeamId INTEGER PRIMARY KEY AUTOINCREMENT , TeamName VAR(255) ,TeamResult Text ,TeamScore VARCHAR (255))" );


        etName = findViewById( R.id.editTextName );
        etResult = findViewById( R.id.editTextResult );
        etScore = findViewById( R.id.editTextScore );
        etSearch = findViewById( R.id.editTextSelect );
        btnAdd = findViewById( R.id.buttonAdd );
        btnDelete = findViewById( R.id.buttonDelete );
        btnViewAll = findViewById( R.id.buttonViewAll );
        btnSelect = findViewById( R.id.buttonSleect );
        btnUpdate = findViewById( R.id.buttonUpdate );

        btnAdd.setOnClickListener( this );
        btnViewAll.setOnClickListener( this );
        btnDelete.setOnClickListener( this );
        btnSelect.setOnClickListener( this );
        btnUpdate.setOnClickListener( this );


    }

    private void closeKeyboard(){
        View view= this.getCurrentFocus();

        if (view!= null){
            InputMethodManager imm = (InputMethodManager)getSystemService( Context.INPUT_METHOD_SERVICE );
            imm.hideSoftInputFromWindow( view.getWindowToken(),0 );

        }



    }


    @Override
    public void onClick(View v) {

        closeKeyboard();

        if (v.getId() == R.id.buttonAdd) {

            sName = etName.getText().toString().trim();
            sResult = etResult.getText().toString().trim();
            sScore = etScore.getText().toString().trim();

            if (  sName.equals( "" ) || (sResult.equals( "" ) || (sScore.equals( "" )))) {

                Toast.makeText( this, "Enter data , fields can not be empty", Toast.LENGTH_SHORT ).show();
                return;
            } else

                myDB.execSQL( "Insert Into TEAMS (   TeamName  , TeamResult ,TeamScore )VALUES ( '" + sName + "','" + sResult + "','" + sScore + "');" );

            Toast.makeText( this, "data saved", LENGTH_LONG ).show();


        } else if (v.getId() == R.id.buttonViewAll) {

            @SuppressLint("Recycle") Cursor c = myDB.rawQuery( "Select * From TEAMS ", null );

            if (c.getCount() == 0) {

                Toast.makeText( this, "no data found", LENGTH_LONG ).show();
                return;
            }
            StringBuilder buffer = new StringBuilder();

            while (c.moveToNext()) {


                buffer.append( "Name :" + c.getString( 1 ) + "\n" );
                buffer.append( "Result:" + c.getString( 2 ) + "\n" );
                buffer.append( "Score:" + c.getString( 3 ) + "\n" );
            }

//show message or toast
            Toast.makeText( this, buffer.toString(), LENGTH_LONG ).show();
            // showMessage( "data", buffer.toString() );

        } else if (v.getId() == R.id.buttonSleect) {

            sSearch = etSearch.getText().toString().trim();

            if (sSearch.equals( "" )) {

                Toast.makeText( this, " enter team name ", LENGTH_LONG ).show();
                return;

            }
 Cursor selectC = myDB.rawQuery( " Select* from TEAMS WHERE TeamName =  '" + sSearch + "'" , null );

            if (selectC.moveToFirst()) {




                 etName.setText( selectC.getString( 1 ) );
                etResult.setText( selectC.getString( 2 ) );
                etScore.setText( selectC.getString( 3 ) );


            } else

                Toast.makeText( this, "data Not Found ", LENGTH_LONG ).show();
        } else if (v.getId() == R.id.buttonUpdate) {

            sSearch = etSearch.getText().toString().trim();
            sName = etName.getText().toString().trim();
            sResult = etResult.getText().toString().trim();
            sScore = etScore.getText().toString().trim();


            if (sSearch.equals( " " )) {

                Toast.makeText( this, " please enter data", LENGTH_LONG ).show();
                return;
            }
            @SuppressLint("Recycle") Cursor cursorUpdate = myDB.rawQuery( "SELECT* FROM TEAMS WHERE TeamName = '" + sSearch + "'",null );

            if (cursorUpdate.moveToFirst()) {

                if ( sName.equals( "" ) || (sResult.equals( "" ) || (sScore.equals( "" )))) {

                    Toast.makeText( this, "enter data", LENGTH_LONG ).show();

                }  else{

                    myDB.execSQL( " UPDATE TEAMS SET  TeamName = '"+sName+"' , TeamResult='"+sResult+"',TeamScore='"+sScore+"' WHERE TeamName = '"+sSearch+"';" );
                    Toast.makeText( this, "database updated", LENGTH_LONG ).show();
                }

            }else
                {
                    Toast.makeText( this, "no data found", LENGTH_LONG ).show();
                }




        } else if (v.getId() == R.id.buttonDelete) {

            sSearch = etSearch.getText().toString();

            if (TextUtils.isEmpty( sSearch )) {

                Toast.makeText( this, "you need to enter team to delete", LENGTH_LONG ).show();
                return;

            }
            @SuppressLint("Recycle") Cursor cursorDel = myDB.rawQuery( "SELECT * FROM TEAMS WHERE TeamName ='" + sSearch + "'",null);


            if (cursorDel.moveToFirst()) {

                myDB.execSQL("DELETE FROM TEAMS WHERE TeamName = '" +sSearch+ "'");


                Toast.makeText( this, "record deleted", LENGTH_LONG ).show();

            } else {

                Toast.makeText( this, " no data found ", LENGTH_LONG ).show();


            }
        }
    }


}























