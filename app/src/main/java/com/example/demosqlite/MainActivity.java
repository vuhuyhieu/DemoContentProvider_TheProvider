package com.example.demosqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private ListView mListViewStudent;
    private ArrayList<Student> mListStudent;
    private ArrayList<String> mListString;
    private ArrayAdapter<String> mAdapterString;
    private DBStudent mDBStudent;
    private EditText mEditTextID;
    private EditText mEditTextName;
    private EditText mEditTextScore;
    private Button mButtonClear;
    private Button mButtonSave;
    private Button mButtonDelete;
    private Student selectedStudent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initEvent();
    }

    private void initEvent() {
        mButtonClear.setOnClickListener(this);
        mButtonSave.setOnClickListener(this);
        mButtonDelete.setOnClickListener(this);
        mListViewStudent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedStudent = mListStudent.get(position);
                fetchData();
            }
        });
    }

    private void fetchData() {
        mEditTextID.setText(String.valueOf(selectedStudent.getId()));
        mEditTextName.setText(selectedStudent.getName());
        mEditTextScore.setText(String.valueOf(selectedStudent.getScore()));
    }

    private void initView() {
        mEditTextID = findViewById(R.id.editTextID);
        mEditTextName = findViewById(R.id.editTextName);
        mEditTextScore = findViewById(R.id.editTextScore);
        mButtonClear = findViewById(R.id.buttonClear);
        mButtonSave = findViewById(R.id.buttonSave);
        mButtonDelete = findViewById(R.id.buttonDelete);
        mListViewStudent = findViewById(R.id.listViewStudent);

        mDBStudent = new DBStudent(this);
        if (mDBStudent.getStudentCount()==0){
            mDBStudent.addThreeStudent();
        }
        mListStudent = mDBStudent.getAllStudent();
        mListString = new ArrayList<>();
        convertToListString();
        mAdapterString = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mListString);
        mListViewStudent.setAdapter(mAdapterString);
    }

    private void convertToListString() {
        mListString.clear();
        for (int i = 0; i< mListStudent.size(); i++){
            mListString.add(mListStudent.get(i).toString());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonClear:
                clear();
                break;
            case R.id.buttonSave:
                save();
                break;
            case R.id.buttonDelete:
                delete();
                break;
        }
    }

    private void delete() {
        int id = Integer.parseInt(mEditTextID.getText().toString());
        mDBStudent.deleteStudent(id);
        refresh();
        clear();
    }

    private void save() {
        if (mEditTextID.getText().toString().equals("")){
            addNewStudent();
        }else {
            updateStudent();
        }
        clear();
    }

    private void updateStudent() {
        Student student = new Student();
        student.setId(Integer.parseInt(mEditTextID.getText().toString()));
        student.setName(mEditTextName.getText().toString());
        student.setScore(Double.parseDouble(mEditTextScore.getText().toString()));
        mDBStudent.updateStudent(student);
        refresh();
    }

    private void addNewStudent() {
        Student student = new Student();
        student.setName(mEditTextName.getText().toString());
        student.setScore(Double.parseDouble(mEditTextScore.getText().toString()));
        mDBStudent.addNewStudent(student);
        refresh();
    }

    private void clear() {
        mEditTextID.setText("");
        mEditTextName.setText("");
        mEditTextScore.setText("");
    }

    private void refresh(){
        mListStudent.clear();
        ArrayList<Student> newList = mDBStudent.getAllStudent();
        mListStudent.addAll(newList);
        convertToListString();
        mAdapterString.notifyDataSetChanged();
    }
}
