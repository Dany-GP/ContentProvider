package com.example.clientecontentprovider;

import android.net.Uri;

public class UsuarioContrato {
    public static final Uri CONTENT_URI =
            Uri.parse("content://net.ivanvega.basededatoslocalconrooma.provider/user");

    public static final String[] COLUMNS_NAME = new String[]{
            "uid", "first_name","last_name"
    } ;

    public static final String COLUMN_ID = "uid";
    public static final String COLUMN_FIRSTNAME = "first_name";
    public static final String COLUMN_LASTNAME = "last_name";

}
