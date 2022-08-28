package com.school.parentandroidapp.Adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.Log;
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

import java.io.ByteArrayInputStream;
import java.util.Base64;

import de.hdodenhof.circleimageview.CircleImageView;

public class SelectStudentAdapter extends RecyclerView.Adapter<SelectStudentAdapter.ViewHolder> {

    private JSONArray localDataSet;

    public SelectStudentAdapter(JSONArray localDataSet){
        this.localDataSet = localDataSet;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_select_student, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JSONObject currentJsonObject = localDataSet.getJSONObject(position);

        //Setting Image
        String imgString = currentJsonObject.getString("img");
        byte[] imgArray = Base64.getDecoder().decode(imgString);
        ByteArrayInputStream bais = new ByteArrayInputStream(imgArray);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imgArray,0, imgArray.length);
        holder.getStudentImageView().setImageBitmap(bitmap);

        //Setting Name
        String firstname = currentJsonObject.getString("firstname");
        String lastname = currentJsonObject.getString("lastname");
        holder.getStudentNameTextView().setText(Validator.getFormattedName(firstname,lastname));

        //Setting standard
        int standard = currentJsonObject.getInt("standard");
        holder.getStandardTextView().setText("Standard : "+standard);

        //Setting Division
        String division = currentJsonObject.getString("division");
        holder.getDivisionTextView().setText("Division : "+division);
    }

    @Override
    public int getItemCount() {
        return localDataSet.length();
    }

    public int getSid(int position){
        return localDataSet.getJSONObject(position).getInt("sid");
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView studentImageView;
        private TextView studentNameTextView,standardTextView,divisionTextView;

        public ViewHolder(View view){
            super(view);

            //Getting Views
            studentImageView = view.findViewById(R.id.studentImageView);
            studentNameTextView = view.findViewById(R.id.studentNameTextView);
            standardTextView = view.findViewById(R.id.standardTextView);
            divisionTextView = view.findViewById(R.id.divisionTextView);
        }

        public CircleImageView getStudentImageView() {
            return studentImageView;
        }

        public TextView getStudentNameTextView() {
            return studentNameTextView;
        }

        public TextView getStandardTextView() {
            return standardTextView;
        }

        public TextView getDivisionTextView() {
            return divisionTextView;
        }
    }
}
