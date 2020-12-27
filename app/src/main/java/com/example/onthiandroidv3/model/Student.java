package com.example.onthiandroidv3.model;

import java.io.Serializable;

public class Student implements Serializable {
    private int mssv;
    private String ten;
    private String lop;

    public Student(int mssv, String ten, String lop) {
        this.mssv = mssv;
        this.ten = ten;
        this.lop = lop;
    }

    public Student() {
    }

    public int getMssv() {
        return mssv;
    }

    public void setMssv(int mssv) {
        this.mssv = mssv;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getLop() {
        return lop;
    }

    public void setLop(String lop) {
        this.lop = lop;
    }

    @Override
    public String toString() {
        return "Student{" +
                "mssv=" + mssv +
                ", ten='" + ten + '\'' +
                ", lop='" + lop + '\'' +
                '}';
    }
}
