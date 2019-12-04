package com.example.capstoneproject;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class Ratingbar extends AppCompatActivity {
    Button button,btnBack;
    RatingBar ratingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratingbar);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Ratingbar.this, MainActivity.class);
                Ratingbar.this.startActivity(intent);
            }
        });
        addListenerOnButtonClick();
    }
    public void addListenerOnButtonClick(){
        ratingbar=(RatingBar)findViewById(R.id.ratingBar);
        button=(Button)findViewById(R.id.rtbutton);
        //Performing action on Button Click
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                //Getting the rating and displaying it on the toast
                String rating=String.valueOf(ratingbar.getRating());
              //  Toast.makeText(getApplicationContext(), rating, Toast.LENGTH_LONG).show();
                //Toast.makeText(getApplicationContext(), rating +"\n Thankyou For Your Valuable Feedback. ", Toast.LENGTH_LONG).show();
                Toast toast=Toast.makeText(getApplicationContext(),rating+"\n Thankyou For Your Valuable Feedback.",Toast.LENGTH_LONG);
                View view=toast.getView();
                view.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_IN);
                TextView text =view.findViewById(android.R.id.message);
                text.setTextColor(Color.MAGENTA);
                toast.show();
            }

        });
    }
}
