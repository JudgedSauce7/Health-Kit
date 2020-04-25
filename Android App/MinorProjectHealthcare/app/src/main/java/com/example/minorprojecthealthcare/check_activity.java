package com.example.minorprojecthealthcare;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.io.IOException;

public class check_activity extends AppCompatActivity {
    private TextView mTextViewResult;
    private RequestQueue mQueue;
    private void jsonParse(String inp) throws IOException {
        //RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://192.168.43.205:5000/id/"+inp+"+";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mTextViewResult.setText("You Might Have "+ response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mTextViewResult.setText("That didn't work!");
            }
        });
        mQueue.add(stringRequest);
    }

         static class MyAdapter extends RecyclerView.Adapter<MyAdapter.Myholder>{
             Context C;
             ArrayList<symptom> symptoms;
             ArrayList<symptom> selectedSymptoms = new ArrayList<>();

             public MyAdapter(Context c, ArrayList<symptom> symptoms) {
                 C = c;
                 this.symptoms = symptoms;
             }
             @NonNull
             @Override
             public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                 View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.model,null);
                 Myholder holder = new Myholder(v);
                 return holder;
             }
             @Override
             public void onBindViewHolder(Myholder holder,int position ) {
                 symptom s1 = symptoms.get(position);
                 holder.SymptomTV.setText(s1.getSymName());
                 holder.Checkbox1.setChecked(s1.getSelected());
                 holder.setItemClickListener(new Myholder.ItemClickListener(){
                     @Override
                     public void onItemClick(View v , int pos){
                         CheckBox Checkbox1 = (CheckBox) v;
                         symptom current = symptoms.get(pos);
                         if(Checkbox1.isChecked()) {
                             current.setSelected(true);
                             selectedSymptoms.add(current);
                         }
                         else if(!Checkbox1.isChecked()) {
                             current.setSelected(false);
                             selectedSymptoms.remove(current);
                         }
                     }
                 });
             }
             @Override
             public int getItemCount(){
                 return symptoms.size();
             }
             static class Myholder extends RecyclerView.ViewHolder implements View.OnClickListener{
                 TextView SymptomTV;
                 CheckBox Checkbox1;
                 ItemClickListener itemClickListener;
                 public Myholder(View itemView){
                     super(itemView);
                     SymptomTV=itemView.findViewById(R.id.symptomTV);
                     Checkbox1=itemView.findViewById(R.id.checkbox1);
                     Checkbox1.setOnClickListener(this);
                 }
                 public void setItemClickListener (ItemClickListener ic){
                     this.itemClickListener=ic;
                 }
                 @Override
                 public void onClick(View v){
                     this.itemClickListener.onItemClick(v,getLayoutPosition());
                 }
                 interface ItemClickListener{
                     void onItemClick(View v, int pos);
                 }
             }
         }
         public ArrayList<symptom> symptomList = new ArrayList<>();
         public ArrayList<symptom> readData() throws IOException {
             InputStream is = getResources().openRawResource(R.raw.test);
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(is, Charset.forName("UTF-8"))
             );
             String line="";
             while ((line = reader.readLine())!=null) {
                 String[] tokens = line.split(",");
                 symptom s = new symptom(tokens[0]);
                 symptomList.add(s);
             }
             //symptom[] stemp = (symptom[]) symptomList.toArray();
             return symptomList;
    }

         StringBuilder sb = null;
         MyAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        try {
            adapter = new MyAdapter(this,readData());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Button fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sb = new StringBuilder();
                int i = 0;
                do {
                    symptom symptom = adapter.selectedSymptoms.get(i);
                    sb.append(symptom.getSymName());
                    if(i !=adapter.selectedSymptoms.size()-1 ) {
                        sb.append("+");
                    }
                    i++;
                }while (i < adapter.selectedSymptoms.size());
                if(adapter.selectedSymptoms.size()>0){
                    //Toast.makeText(MainActivity.this,sb.toString(),Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //Toast.makeText(MainActivity.this,"Please check an Item first", Toast.LENGTH_SHORT).show();
                }
            }
        });
        RecyclerView rv = findViewById(R.id.Recyler);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setItemAnimator(new DefaultItemAnimator() );
        rv.setAdapter(adapter);


        mTextViewResult = findViewById(R.id.text_view_result);
        Button buttonParse = findViewById(R.id.button_parse);

        mQueue = Volley.newRequestQueue(this);

        buttonParse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("myactivity","test1");
                try {
                    jsonParse(sb.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
