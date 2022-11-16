package com.obsuen.mycroppy.helpers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.obsuen.mycroppy.activities.LoginActivity;
import com.obsuen.mycroppy.activities.NewDashBoard;
import com.obsuen.mycroppy.R;

public class SharedAnimationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_animation);

        final Button button = findViewById(R.id.button);

        button.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bottom_up));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user!=null){
                    Intent intent = new Intent(SharedAnimationActivity.this, NewDashBoard.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(SharedAnimationActivity.this, LoginActivity.class);
                    startActivity(intent);
                }


            }
        });
    }
}