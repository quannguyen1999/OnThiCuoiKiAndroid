package com.example.onthiandroidv3;

import android.content.Context;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onthiandroidv3.model.Student;

import java.util.ArrayList;


public class ListStudentAdapter extends RecyclerView.Adapter<ListStudentAdapter.StudentViewHolder>{ // implements View.OnCreateContextMenuListener {

    private LayoutInflater layoutInflater;

    private ArrayList<Student> listStudent;

    public ListStudentAdapter(Context context, ArrayList<Student> listStudent) {
        layoutInflater = LayoutInflater.from(context);
        this.listStudent = listStudent;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.lv_student, parent, false);
        return new StudentViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student mCurrent = listStudent.get(position);

        //cẩn thận
        holder.tvMssv.setText(String.valueOf(mCurrent.getMssv()));
        holder.tvLop.setText(mCurrent.getLop());
        holder.tvTen.setText(mCurrent.getTen());
    }


    @Override
    public int getItemCount() {
        return listStudent.size();
    }

    //implements
    //View.OnCreateContextMenuListener
    public class StudentViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView tvMssv, tvTen, tvLop;

        ListStudentAdapter listStudentAdapter;

        LinearLayout linearLayout;

        public StudentViewHolder(@NonNull View itemView, ListStudentAdapter listStudentAdapter) {
            super(itemView);

            linearLayout = itemView.findViewById(R.id.linear);

            tvMssv = itemView.findViewById(R.id.tvMssv);
            tvTen = itemView.findViewById(R.id.tvHoTen);
            tvLop = itemView.findViewById(R.id.tvLop);

            this.listStudentAdapter = listStudentAdapter;

            this.itemView.setOnCreateContextMenuListener(this::onCreateContextMenu);
        }


        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.add(this.getAdapterPosition(),121,0,"add");
            contextMenu.add(this.getAdapterPosition(),122,1,"edit");
            contextMenu.add(this.getAdapterPosition(),123,2,"delete");
        }
    }
}
