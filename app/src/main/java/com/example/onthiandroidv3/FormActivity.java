package com.example.onthiandroidv3;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.onthiandroidv3.model.Student;

public class FormActivity extends AppCompatActivity {

    private EditText edtMssv, edtTen;

    private Spinner spLop;

    private Button btnSave;

    static String[] listLopSpinner = new String[]{
            "DHKTPM13ATT", "DHKTPM13BTT", "DHKTPM13CTT"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_form);

        metaData();

        createEvent();
    }

    private void metaData() {
        edtMssv = findViewById(R.id.edtMSSV);
        edtTen = findViewById(R.id.edtHoTen);
        spLop = findViewById(R.id.spLop);
        btnSave = findViewById(R.id.btnSave);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, listLopSpinner);
        spLop.setAdapter(adapter);
    }

    public void showDialog(String nameError) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Error");
        alert.setMessage(nameError);
        alert.setNegativeButton("Ok", null);
        alert.show();
    }

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

    private void createEvent() {
        Intent intent = getIntent();
        if (!intent.hasExtra("edit")) {
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String mssv = edtMssv.getText().toString();
                    String ten = edtTen.getText().toString();

                    checkInFormation(mssv, ten);

                    Student student = new Student(Integer.parseInt(mssv), ten, spLop.getSelectedItem().toString());

                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("student", student);
                    intent.putExtra("add", bundle);

                    //cẩn thận
                    setResult(RESULT_OK, intent);
                    finish();

                }
            });
        } else {
            Bundle bundle = intent.getBundleExtra("edit");
            Student student = (Student) bundle.getSerializable("student");
            edtMssv.setEnabled(false);
            edtMssv.setText(String.valueOf(student.getMssv()));
            edtTen.setText(student.getTen());
            for (int i = 0; i < listLopSpinner.length; i++) {
                if (student.getLop().equalsIgnoreCase(listLopSpinner[i])) {
                    spLop.setSelection(i);
                    break;
                }
            }
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String mssv = edtMssv.getText().toString();

                    String ten = edtTen.getText().toString();

                    checkInFormation(mssv, ten);

                    student.setMssv(Integer.parseInt(mssv));
                    student.setTen(ten);
                    student.setLop(spLop.getSelectedItem().toString());

                    Intent intent = new Intent();
                    intent.putExtra("edit", bundle);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        }
    }
}

