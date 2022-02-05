package com.example.clientecontentprovider;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.UserDictionary;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    private void consultarContentProvider(){
        Cursor cursor = getContentResolver().query(
                UsuarioContrato.CONTENT_URI,
                UsuarioContrato.COLUMNS_NAME,
                null,null,null
        );

        if(cursor!=null) {

            while (cursor.moveToNext()) {
                Log.d("CPCliente",
                        cursor.getInt(0) + " - " + cursor.getString(1)
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

        findViewById(R.id.btnConsultar).setOnClickListener(v -> {
            consultarContentProvider();
        });

    }
}