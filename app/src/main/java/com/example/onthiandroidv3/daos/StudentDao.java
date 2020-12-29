package com.example.onthiandroidv3.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.onthiandroidv3.model.Student;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface StudentDao {
    @Insert(onConflict = REPLACE)
    void addStudent(Student student);

    @Delete
    void deleteStudent(Student student);

    @Query("UPDATE Student SET ten=:name WHERE mssv=:id")
    void updateStudent(String name, int id);

    @Query("SELECT * FROM Student WHERE mssv=:mssv")
    Student getSVByMSSV(String mssv);

    @Query("SELECT * FROM Student")
    List<Student> getAllStudent();
}
