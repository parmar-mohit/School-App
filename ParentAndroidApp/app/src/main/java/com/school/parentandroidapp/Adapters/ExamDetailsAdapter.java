package com.school.parentandroidapp.Adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.school.parentandroidapp.R;
import com.school.parentandroidapp.Static.Validator;
import com.school.parentandroidapp.parser.json.JSONArray;
import com.school.parentandroidapp.parser.json.JSONObject;

import java.util.Base64;

public class ExamDetailsAdapter extends RecyclerView.Adapter<ExamDetailsAdapter.ViewHolder> {

    private JSONArray localDataSet;

    public ExamDetailsAdapter(JSONArray localDataSet){
        this.localDataSet = localDataSet;
    }

    @NonNull
    @Override
    public ExamDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_exam_details, parent, false);
        return new ExamDetailsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExamDetailsAdapter.ViewHolder holder, int position) {
        JSONObject currentJsonObject = localDataSet.getJSONObject(position);

        holder.getExamNameTextView().setText("Exam : " + Validator.getFormattedText(currentJsonObject.getString("exam_name")));
        holder.getSubjectNameTextView().setText("Subject : " + Validator.getFormattedText(currentJsonObject.getString("subject_name")));
        holder.getDateTextView().setText("Date : "+Validator.getFormattedDate(currentJsonObject.getLong("date")));
        holder.getMarksTextView().setText(currentJsonObject.getInt("marks")+"/"+currentJsonObject.getInt("total_marks"));
    }

    @Override
    public int getItemCount() {
        return localDataSet.length();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView examNameTextView,subjectNameTextView,dateTextView,marksTextView;

        public ViewHolder(View view){
            super(view);

            examNameTextView = view.findViewById(R.id.examNameTextView);
            subjectNameTextView = view.findViewById(R.id.subjectNameTextView);
            dateTextView = view.findViewById(R.id.dateTextView);
            marksTextView = view.findViewById(R.id.marksTextView);
        }

        public TextView getExamNameTextView() {
            return examNameTextView;
        }

        public TextView getSubjectNameTextView() {
            return subjectNameTextView;
        }

        public TextView getDateTextView() {
            return dateTextView;
        }

        public TextView getMarksTextView() {
            return marksTextView;
        }
    }
}
