package com.example.clientecontentprovider;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.UserDictionary;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText txtFirstname,txtLastName, userid;

    private void consultarContentProvider(){
        Cursor cursor = getContentResolver().query(
                UsuarioContrato.CONTENT_URI,
                UsuarioContrato.COLUMNS_NAME,
                null,null,null
        );

        if(cursor!=null) {

            while (cursor.moveToNext()) {
                Log.d("CPCliente",
                        cursor.getInt(0) + " - " + cursor.getString(1) + cursor.getString(2)
                );
            }
        }else{
            Log.d("USUARIOCONTENTPROVIDER",
                    "NO DEVUELVE"
            );
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtFirstname = findViewById(R.id.txtUserFirstNameDelete);
        txtLastName = findViewById(R.id.txtUserLastNameDelete);
        userid = findViewById(R.id.txtUserId);

        Cursor c = getContentResolver().query(UserDictionary.Words.CONTENT_URI,
                new String[] {UserDictionary.Words.WORD,
                        UserDictionary.Words.LOCALE},
                null,null,null
                );
        if(c!=null) {
            while (c.moveToNext()) {
                Log.d("DICCIONARIOUSARUI",
                        c.getString(0) + " - " + c.getString(1)
                );
            }
        }

        consultarContentProvider();

        findViewById(R.id.btnInsert).setOnClickListener(
                view -> {

                    ContentValues cv = new ContentValues();
                    cv.put(UsuarioContrato.COLUMN_FIRSTNAME, "Pedro");
                    cv.put(UsuarioContrato.COLUMN_LASTNAME, "Dominguez");

                    Uri uriInsert = getContentResolver().insert(
                            UsuarioContrato.CONTENT_URI,
                            cv
                    );
                    Log.d("CPCliente", uriInsert.toString() );
                    Toast.makeText(this, "Usuario insert: \n"+
                            uriInsert.toString(), Toast.LENGTH_SHORT).show();


                }
        );


        findViewById(R.id.btnUpdate).setOnClickListener(
                view -> {

                    ContentValues cv = new ContentValues();
                    cv.put(UsuarioContrato.COLUMN_FIRSTNAME, "Pablo");
                    cv.put(UsuarioContrato.COLUMN_LASTNAME, "Herrera");

                    int elemtosAfectados = getContentResolver().update(
                            Uri.withAppendedPath(UsuarioContrato.CONTENT_URI, "15")   ,
                            cv,
                            null, null
                    );

                    Log.d("CPCliente", "Elementos afectados: " +elemtosAfectados );
                    Toast.makeText(this, "Usuario update: \n"+
                            elemtosAfectados, Toast.LENGTH_SHORT).show();


                }
        );

        findViewById(R.id.btnConsultUserByName).setOnClickListener(
                view -> {
                    Cursor cursor = getContentResolver().query(
                            Uri.withAppendedPath(UsuarioContrato.CONTENT_URI, "18"),
                            UsuarioContrato.COLUMNS_NAME,
                            null,
                            new String[]{
                                    userid.getText().toString(),
                                    txtFirstname.getText().toString(),
                                    txtLastName.getText().toString()
                            },null
                    );

                    if(cursor!=null) {

                        while (cursor.moveToNext()) {
                            Log.d("CPCliente",
                                    "\n"+ cursor.getInt(0) + " - " +
                                            cursor.getString(1) + cursor.getString(2)
                            );
                        }
                    }else{
                        Log.d("USUARIOCONTENTPROVIDER",
                                "NO DEVUELVE"
                        );
                    }
                }
        );

        findViewById(R.id.btnDeleteUserByName).setOnClickListener(
                view -> {
                    ContentValues cv = new ContentValues();
                    cv.put(UsuarioContrato.COLUMN_FIRSTNAME, txtFirstname.getText().toString());
                    cv.put(UsuarioContrato.COLUMN_LASTNAME, txtLastName.getText().toString());

                    int elemAf = getContentResolver().
                            delete(
                                    Uri.withAppendedPath(UsuarioContrato.CONTENT_URI, "15"),
                                    null, new String[]{
                                            userid.getText().toString(),
                                            txtFirstname.getText().toString(),
                                            txtLastName.getText().toString()
                                    }
                            );
                    Log.d("CPCliente", "Elementos afectados: " +elemAf );
                    Toast.makeText(this, "Usuario delete: \n"+
                            elemAf, Toast.LENGTH_SHORT).show();
                }
        );

        findViewById(R.id.btnConsultar).setOnClickListener(v -> {
            consultarContentProvider();
        });

    }
}