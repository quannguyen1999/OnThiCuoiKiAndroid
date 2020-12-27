package com.example.onthiandroidv3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.onthiandroidv3.model.Student;

import java.util.ArrayList;
import java.util.List;

public class DBManager extends SQLiteOpenHelper{
    public static final String DATABASE_NAME ="manageStudent";

    private static final String TABLE_NAME ="student";
    private static final String MSSV ="mssv";
    private static final String TEN ="ten";
    private static final String LOP = "lop";

    private Context context;

    public DBManager(Context context) {
        super(context, DATABASE_NAME,null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlQuery = "CREATE TABLE "+TABLE_NAME +" (" +
                MSSV+" String primary key, "+
                TEN + " TEXT, "+
                LOP + " TEXT )";
        db.execSQL(sqlQuery);
    }

    public void addStudent(Student student){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MSSV, student.getMssv());
        values.put(TEN, student.getTen());
        values.put(LOP, student.getLop());
        //Neu de null thi khi value bang null thi loi

        db.insert(TABLE_NAME,null,values);

        db.close();
    }

    public List<Student> getAllStudent() {
        List<Student> listStudent = new ArrayList<Student>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Student product = new Student();

                product.setMssv(cursor.getInt(0));
                product.setTen(cursor.getString(1));
                product.setLop(cursor.getString(2));

                listStudent.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return listStudent;
    }



    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public int updateStudent(Student student){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TEN,student.getTen());

        values.put(LOP,student.getLop());

        return db.update(TABLE_NAME,values,MSSV +"=?",new String[] { String.valueOf(student.getMssv())});
    }

    public void deleteStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, MSSV + " = ?",
                new String[] { String.valueOf(student.getMssv()) });
        db.close();
    }
}
