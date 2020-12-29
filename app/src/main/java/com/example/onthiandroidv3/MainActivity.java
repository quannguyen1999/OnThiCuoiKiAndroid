package com.example.onthiandroidv3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import com.example.onthiandroidv3.daos.StudentDao;
import com.example.onthiandroidv3.model.Student;
import com.example.onthiandroidv3.room.RoomDB;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //recycle view : adpter
    private ListStudentAdapter listStudentAdapter;

    private RecyclerView rclView;

    //array list
    private static ArrayList<Student> listStudent;

    //metadata
    private EditText edtSearch;

    private Button btnThem;

    private Button btnTim;

    private Button btnReset;

    //request code
    private static final int ADD = 999;

    private static final int EDIT = 888;

    //db
    //DBManager dbManager;

    //db-room
    private static RoomDB roomDB;

    private static StudentDao dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //hide bar
        getSupportActionBar().hide();

        //connect db
        //dbManager = new DBManager(this);

        //room db
        roomDB = RoomDB.getInstance(this);
        dbManager = roomDB.studentDao();


        metaData();

        initData();

        createEvent();

        //register menu
        registerForContextMenu(rclView);
    }

    private void metaData() {
        edtSearch = findViewById(R.id.edtTimKiem);
        btnThem = findViewById(R.id.btnThem);
        btnTim = findViewById(R.id.btnTim);
        rclView = findViewById(R.id.rclView);
        btnReset = findViewById(R.id.btnReset);
    }

    private void initData() {

        listStudent = new ArrayList<>();

        listStudent = (ArrayList<Student>) dbManager.getAllStudent();

        listStudentAdapter = new ListStudentAdapter(this, listStudent);

        rclView.setAdapter(listStudentAdapter);

        //cẩn thận
        rclView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void resetList() {
        listStudent.clear();
        List<Student> listStudentFind = dbManager.getAllStudent();
        for (Student student : listStudentFind) {
            listStudent.add(student);
        }
        listStudentAdapter.notifyDataSetChanged();
    }

    public void showDialog(String nameError) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Error");
        alert.setMessage(nameError);
        alert.setNegativeButton("Ok", null);
        alert.show();
    }

    private void createEvent() {
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FormActivity.class);
                startActivityForResult(intent, ADD);
                listStudentAdapter.notifyDataSetChanged();
            }
        });

        btnTim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String search = edtSearch.getText().toString().trim();

                if (search.isEmpty()) {
                    showDialog("Plese enter search");
                    return;
                }

                Student student = dbManager.getSVByMSSV(search);
                if (student == null) {
                    showDialog("Not found");
                    return;
                }
                listStudent.clear();
                listStudent.add(student);
                listStudentAdapter.notifyDataSetChanged();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetList();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getBundleExtra("add");
                Student student = (Student) bundle.getSerializable("student");
                dbManager.addStudent(student);
                resetList();
            }
        } else if (requestCode == EDIT) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getBundleExtra("edit");
                int pos = bundle.getInt("pos");
                Student student = (Student) bundle.getSerializable("student");
                Student studentEdit = listStudent.get(pos);
                studentEdit.setTen(student.getTen());
                studentEdit.setLop(student.getLop());
                //no-room
                //dbManager.updateStudent(studentEdit);

                //room db
                dbManager.updateStudent(student.getTen(),student.getMssv());
                resetList();
            }
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case 121:
                Intent intent = new Intent(MainActivity.this, FormActivity.class);
                startActivityForResult(intent, ADD);
                break;
            case 122:
                int pos = item.getGroupId();
                Student student = listStudent.get(pos);
                Intent intent1 = new Intent(MainActivity.this, FormActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("student", student);
                bundle.putInt("pos", pos);
                intent1.putExtra("edit", bundle);
                startActivityForResult(intent1, EDIT);
                listStudentAdapter.notifyDataSetChanged();
                break;
            case 123:
                Student student1 = listStudent.get(item.getGroupId());
                dbManager.deleteStudent(student1);
                listStudent.remove(item.getGroupId());
                listStudentAdapter.notifyDataSetChanged();
                break;
        }
        return super.onContextItemSelected(item);
    }
}