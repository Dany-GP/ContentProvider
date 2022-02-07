package net.ivanvega.basededatoslocalconrooma.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.InetAddresses;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import net.ivanvega.basededatoslocalconrooma.data.AppDatabase;
import net.ivanvega.basededatoslocalconrooma.data.User;
import net.ivanvega.basededatoslocalconrooma.data.UserDao;

import java.util.List;

public class MiContentProvider extends ContentProvider {
    /*Estructura de mi uri:
        uri -> content://net.ivanvega.basededatoslocalconrooma.provider/user  -> insert y query
        uri -> content://net.ivanvega.basededatoslocalconrooma.provider/user/#  -> update, query y delete
        uri -> content://net.ivanvega.basededatoslocalconrooma.provider/user/*  -> query, update y delete}
                        net.ivanvega.basededatoslocalconrooma.provider
     */

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static
    {

        sURIMatcher.addURI("net.ivanvega.basededatoslocalconrooma.provider","user", 1);
        sURIMatcher.addURI("net.ivanvega.basededatoslocalconrooma.provider","user/#", 2);
        sURIMatcher.addURI("net.ivanvega.basededatoslocalconrooma.provider","user/*", 3);
    }

    @Override
    public boolean onCreate() {

        return false;
    }

    private Cursor listUserToCursorUser( List<User>   usuaarios){
        MatrixCursor cursor = new MatrixCursor(new String[]{
                "uid","first_name","last_name"
        })    ;

        for(User usuario: usuaarios ){
            cursor.newRow().add("uid", usuario.uid)
                    .add("first_name", usuario.firstName)
                    .add("last_name", usuario.lastName);
        }

        return cursor;
    }

    private Cursor UserToCursorUser(User usuario){
        MatrixCursor cursor = new MatrixCursor(new String[]{
                "uid","first_name","last_name"
        })    ;


        cursor.newRow().add("uid", usuario.uid)
                .add("first_name", usuario.firstName)
                .add("last_name", usuario.lastName);


        return cursor;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri,
                        @Nullable String[] strings,
                        @Nullable String s,
                        @Nullable String[] strings1,
                        @Nullable String s1) {

        AppDatabase db =
                AppDatabase.getDatabaseInstance(getContext());

        Cursor cursor= null;

        UserDao dao = db.userDao();
        switch (sURIMatcher.match(uri)){
                case 1:
                    cursor =   listUserToCursorUser( dao.getAll());
                break;
            case 2:
                    // Busca por firstname y lastname
                try {
                    cursor = UserToCursorUser(dao.findByName(strings1[1], strings1[2]));
                }catch (NullPointerException e){

                }
                break;
            case 3:
                    // Busca por id
                    cursor = listUserToCursorUser(
                            dao.loadAllByIds(
                                    new int[]{
                                            Integer.parseInt(strings1[0])
                                    }));
                break;

        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        String typeMime = "";

        switch (sURIMatcher.match(uri)) {
            case 1:
                typeMime = "vnd.android.cursor.dir/vnd.net.ivanvega.basededatoslocalconrooma.provider.user";
                break;
            case 2:
                typeMime = "vnd.android.cursor.item/vnd.net.ivanvega.basededatoslocalconrooma.provider.user";
                break;
            case 3:

                typeMime = "vnd.android.cursor.dir/vnd.net.ivanvega.basededatoslocalconrooma.provider.user";
                break;
        }
        return typeMime;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri,
                      @Nullable ContentValues contentValues) {
        AppDatabase db =
                AppDatabase.getDatabaseInstance(getContext());
        Cursor cursor= null;
        UserDao dao = db.userDao();
        User usuario= new User();;
        switch (sURIMatcher.match(uri)){
            case 1:

                usuario.firstName = contentValues.getAsString(UsuarioContrato.COLUMN_FIRSTNAME);
                usuario.lastName = contentValues.getAsString(UsuarioContrato.COLUMN_LASTNAME);

                long  newid = dao.insert(usuario);
                return  Uri.withAppendedPath(uri, String.valueOf( newid));

        }

        return   Uri.withAppendedPath(uri, String.valueOf( -1))  ;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        AppDatabase db =
                AppDatabase.getDatabaseInstance(getContext());
        UserDao dao = db.userDao();
        int deletedValue = -1;
        User user;
        switch (sURIMatcher.match(uri)){
            case 1:
                    break;
            case 2:
                    user = new User();
                    user.uid=Integer.parseInt(strings[0]);
                    user.firstName=strings[1];
                    user.lastName=strings[2];
                    dao.delete(user);
                    deletedValue=1;
                    break;
            case 3:
                    user = new User();
                    user.uid=Integer.parseInt(strings[0]);
                    user.firstName=strings[1];
                    user.lastName=strings[2];
                    dao.delete(user);
                    deletedValue=1;
                    break;
        }

        return deletedValue;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues,
                          @Nullable String s, @Nullable String[] strings) {

         int id  = Integer.parseInt( uri.getLastPathSegment());

        AppDatabase db =
                AppDatabase.getDatabaseInstance(getContext());
        Cursor cursor= null;
        UserDao dao = db.userDao();

        List<User> usuarioUpdate  =  dao.loadAllByIds(new int[]{id});

        usuarioUpdate.get(0).firstName =
                contentValues.getAsString(UsuarioContrato.COLUMN_FIRSTNAME );
        usuarioUpdate.get(0).lastName =
                contentValues.getAsString(UsuarioContrato.COLUMN_LASTNAME );

        return dao.updateUser(usuarioUpdate.get(0));
    }

}
