package com.example.onthiandroidv3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

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

    //bonus
    private Button btnAddPopUp;

    //request code
    private static final int ADD = 999;

    private static final int EDIT = 888;

    //db
    //DBManager dbManager;

    //db-room
    private static RoomDB roomDB;

    private static StudentDao dbManager;

    //bonus
    private EditText edtMssv, edtTen;

    private Spinner spLop;

    private Button btnSave;

    static String[] listLopSpinner = new String[]{
            "DHKTPM13ATT", "DHKTPM13BTT", "DHKTPM13CTT"
    };

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
        btnAddPopUp = findViewById(R.id.btnAddPopUp);
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

        btnAddPopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(v.getContext());
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.activity_form, null);
                builder.setView(dialogView);

                spLop = dialogView.findViewById(R.id.spLop);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(v.getContext(),
                        R.array.type_array, R.layout.support_simple_spinner_dropdown_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spLop.setAdapter(adapter);

                edtMssv = dialogView.findViewById(R.id.edtMSSV);

                edtTen = dialogView.findViewById(R.id.edtHoTen);

                spLop = dialogView.findViewById(R.id.spLop);

                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        String mssv = edtMssv.getText().toString();

                        String ten = edtTen.getText().toString();

//                        System.out.println(edtMssv.getText().toString());

                        checkInFormation(mssv, ten);

                        Student student = new Student(Integer.parseInt(mssv), ten, spLop.getSelectedItem().toString());

                        dbManager.addStudent(student);

                        resetList();

                        dialog.dismiss();
                    }
                })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });

                builder.show();
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

    //bonus
    public void checkInFormation(String mssv, String ten) {
        if (mssv.equals("")) {

            showDialog("Please enter MSSV");

            edtMssv.requestFocus();

            return;

        }

        //check MSSV
        try {
            Integer.parseInt(edtMssv.getText().toString());
        } catch (Exception e) {
            showDialog("MSSV invalid");
            edtMssv.requestFocus();
            return;
        }

        if (ten.equals("")) {

            showDialog("Please enter Ten");

            edtTen.requestFocus();

            return;
        }
    }
}