package com.example.sos.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import com.example.sos.modal.Contact;
import com.example.sos.modal.User;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.Subject;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "UserManager.db";

    // User table name
    private static final String TABLE_USER = "user";

    // User Table Columns names
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_EMAIL = "user_email";
    private static final String COLUMN_USER_PASSWORD = "user_password";

    //Contact table name
    private static final String TABLE_CONTACT = "contact";

    // Contact Table Columns names
    private static final String COLUMN_CONTACT_ID = "contact_id";
    private static final String COLUMN_CONTACT_NAME = "contact_name";
    private static final String COLUMN_CONTACT_NUMBER = "contact_number";
    private static final String COLUMN_USER_EMAIL_FK = "user_email_fk";


    // create user table sql query
    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_NAME + " TEXT,"
            + COLUMN_USER_EMAIL + " TEXT," + COLUMN_USER_PASSWORD + " TEXT" + ")";

    // drop  usertable sql query
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;

    //create contact table sql query
    private String CREATE_CONTACT_TABLE = "CREATE TABLE " + TABLE_CONTACT + "("
            + COLUMN_CONTACT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_CONTACT_NAME + " TEXT,"
            + COLUMN_CONTACT_NUMBER + " TEXT," +
            "FOREIGN KEY ("+COLUMN_USER_EMAIL_FK+") REFERENCES " +TABLE_USER+ "(COLUMN_USER_ID) ON UPDATE CASCADE ON DELETE CASCADE " + ")";

    // drop  contacttable sql query
    private String DROP_CONTACT_TABLE = "DROP TABLE IF EXISTS " + TABLE_CONTACT;

    /**
     * Constructor
     *
     * @param context
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_CONTACT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Drop table if exist
        db.execSQL(DROP_USER_TABLE);
        db.execSQL(DROP_CONTACT_TABLE);

        // Create tables again
        onCreate(db);

    }

    // create user record
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());

        // Inserting Row
        db.insert(TABLE_USER, null, values);
        db.close();
    }

    //return user record
    public List<User> getAllUser() {
        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID,
                COLUMN_USER_EMAIL,
                COLUMN_USER_NAME,
                COLUMN_USER_PASSWORD
        };
        // sorting orders
        String sortOrder =
                COLUMN_USER_NAME + " ASC";
        List<User> userList = new ArrayList<User>();

        SQLiteDatabase db = this.getReadableDatabase();

        // fetch records from user table
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));
                user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)));
                // Adding user record to list
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return userList;
    }

    public int getUserID(String userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = COLUMN_USER_EMAIL_FK+" LIKE '%"+COLUMN_USER_ID+"%'";
        Cursor c = db.query(true, TABLE_USER, null,
                where, null, null, null, null, null);
        if(c.getCount()>0)
            return c.getInt(0);
        else
            return 0;
    }

    //update user record
    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());

        // updating row
        db.update(TABLE_USER, values, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    //delete user record
    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by id
        db.delete(TABLE_USER, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    // check existing user
    public boolean checkUser(String email) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?";

        // selection argument
        String[] selectionArgs = {email};

        // fetch record from user table
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

   // check existing user
    public boolean checkUser(String email, String password) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";

        // selection arguments
        String[] selectionArgs = {email, password};

        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }


/** Starting from here is implementation of Contact Table **/
/********************************************************************************************/

// create contact record
// create contact record
public void addContact(Contact contact) {
    SQLiteDatabase db = this.getWritableDatabase();

    ContentValues values = new ContentValues();
    values.put(COLUMN_CONTACT_NAME, contact.getContactName());
    values.put(COLUMN_CONTACT_NUMBER, contact.getContactNumber());

    // Inserting Row
    db.insert(TABLE_CONTACT, null, values);
    db.close();
}

    //return contact record
    public List<Contact> getAllContact() {
        // array of columns to fetch
        String[] columns = {
                COLUMN_CONTACT_ID,
                COLUMN_CONTACT_NAME,
                COLUMN_CONTACT_NUMBER,
        };
        // sorting orders
        String sortOrder =
                COLUMN_CONTACT_NAME + " ASC";
        List<Contact> contactList = new ArrayList<Contact>();

        SQLiteDatabase db = this.getReadableDatabase();

        // fetch records from user table
        Cursor cursor = db.query(TABLE_CONTACT, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setContactId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_ID))));
                contact.setContactName(cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_NAME)));
                contact.setContactNumber(cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_NUMBER)));
                // Adding user record to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return contact list
        return contactList;
    }


    //update user record
    public void updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CONTACT_NAME, contact.getContactName());
        values.put(COLUMN_CONTACT_NUMBER, contact.getContactNumber());

        // updating row
        db.update(TABLE_CONTACT, values, COLUMN_CONTACT_ID + " = ?",
                new String[]{String.valueOf(contact.getContactId())});
        db.close();
    }

    public int getContactCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }


    //delete user record
    public boolean deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by id
        db.delete(TABLE_CONTACT, COLUMN_CONTACT_ID + " = ?",
                new String[]{String.valueOf(contact.getContactId())});
        db.close();
        return true;
    }

    // check existing contact
    public boolean checkContact(String contactNumber) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_CONTACT_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_CONTACT_NUMBER + " = ?";

        // selection argument
        String[] selectionArgs = {contactNumber};

        // fetch record from user table
        Cursor cursor = db.query(TABLE_CONTACT, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }

        return false;
    }
}
