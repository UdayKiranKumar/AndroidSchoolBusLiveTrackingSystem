package com.example.capstoneproject;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FeedbackForm extends AppCompatActivity {
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_form);

        final EditText your_email       = (EditText) findViewById(R.id.txtTo);
        final EditText your_subject     = (EditText) findViewById(R.id.txtSubject);
        final EditText your_message     = (EditText) findViewById(R.id.txtMessage);

        btnBack = (Button) findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FeedbackForm.this, MainActivity.class);
                FeedbackForm.this.startActivity(intent);
            }
        });

        ((Button) findViewById(R.id.btnOK)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String to     = your_email.getText().toString();
                String sub   = your_subject.getText().toString();
                String mess   = your_message.getText().toString();


                Boolean onError = false;
                if (!isValidEmail(to)) {
                    onError = true;
                    your_email.setError("Invalid Email");
                    return;
                }

                if (TextUtils.isEmpty(sub)){
                    your_subject.setError("Enter Subject");
                    your_subject.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(mess)){
                    your_message.setError("Enter Message");
                    your_message.requestFocus();
                    return;
                }

                Intent mail = new Intent(Intent.ACTION_SEND);
                mail.putExtra(Intent.EXTRA_EMAIL,new String[]{"udaykumar287855@gmail.com"});
                mail.putExtra(Intent.EXTRA_SUBJECT, sub);
                mail.putExtra(Intent.EXTRA_TEXT, mess);
                mail.setType("message/rfc822");
                startActivity(Intent.createChooser(mail, "Send email via:"));
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        //Get a Tracker (should auto-report)


    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }


    // validating email id

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
