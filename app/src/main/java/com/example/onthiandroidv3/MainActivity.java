package com.example.onthiandroidv3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.example.onthiandroidv3.model.Student;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {
   private ListStudentAdapter listStudentAdapter;

   private RecyclerView rclView;

   private ArrayList<Student> listStudent;

   private EditText edtSearch;

   private Button btnThem;

   private Button btnTim;

   private static final int ADD = 999;

   private static final int EDIT = 888;

   DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        dbManager = new DBManager(this);

        init();

        initData();

        createEvent();

        registerForContextMenu(rclView);
    }

    private void createEvent() {
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FormActivity.class);
                startActivityForResult(intent,ADD);
                listStudentAdapter.notifyDataSetChanged();
            }
        });

        btnTim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void init() {
        edtSearch = findViewById(R.id.edtTimKiem);
        btnThem = findViewById(R.id.btnThem);
        btnTim = findViewById(R.id.btnTim);
        rclView = findViewById(R.id.rclView);
    }

    private void initData(){

        listStudent = new ArrayList<>();

        listStudent =  (ArrayList<Student>) dbManager.getAllStudent();

        listStudentAdapter = new ListStudentAdapter(this,listStudent);

        rclView.setAdapter(listStudentAdapter);
        //cẩn thận
        rclView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADD){
            if(resultCode == RESULT_OK){
                Bundle bundle = data.getBundleExtra("add");
                Student student = (Student) bundle.getSerializable("student");
                dbManager.addStudent(student);
                listStudent.add(student);
                listStudentAdapter.notifyDataSetChanged();
            }
        }else if(requestCode == EDIT){
            if(resultCode == RESULT_OK){
                Bundle bundle = data.getBundleExtra("edit");
                int pos = bundle.getInt("pos");
                Student student = (Student) bundle.getSerializable("student");
                Student studentEdit = listStudent.get(pos);
                studentEdit.setTen(student.getTen());
                studentEdit.setLop(student.getLop());
                dbManager.updateStudent(studentEdit);
                listStudentAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case 121:
                Intent intent = new Intent(MainActivity.this, FormActivity.class);
                startActivityForResult(intent,ADD);
                break;
            case 122:
                int pos = item.getGroupId();
                Student student = listStudent.get(pos);
                Intent intent1 = new Intent(MainActivity.this, FormActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("student",student);
                bundle.putInt("pos",pos);
                intent1.putExtra("edit",bundle);
                startActivityForResult(intent1,EDIT);
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