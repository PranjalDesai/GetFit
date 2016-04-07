package com.pranjal.getfit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class TaskDone extends AppCompatActivity {

    String email;
    TextView finalv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_done);

        email= getIntent().getExtras().getString("email");
        finalv= (TextView) findViewById(R.id.finalView);
        finalv.setText("Request Money from " + email);
    }

    public void donezo(View view)
    {
        Intent done= new Intent(TaskDone.this, UserMainScreen.class);
        startActivity(done);
    }

}
