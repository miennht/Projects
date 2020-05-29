package fantasticmassage.com;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ImageButton;

import java.io.Serializable;

import dto.User;

public class ForgetPasswordFinishActivity extends AppCompatActivity {
    public static final String EXTRA_EMAIL = "EMAIL";
    public static final String EXTRA_USERPROFILE = "USERPROFILE";
    ImageButton forget_password_finish_btnBack,
            forget_password_finish_btnNext;
    String mEmail;
    User mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password_finish);
        mEmail = getIntent().getStringExtra(ForgetPasswordActivity.EXTRA_EMAIL);
        mUser = (User) getIntent().getSerializableExtra(ForgetPasswordActivity.EXTRA_USERPROFILE);
        goBack();
        goNext();
        //goToLogin();
    }
    private void goBack() {
        forget_password_finish_btnBack = (ImageButton)findViewById(R.id.forget_password_finish_btnBack);
        forget_password_finish_btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void goToLogin(){
        forget_password_finish_btnNext = (ImageButton)findViewById(R.id.forget_password_finish_btnNext);
        forget_password_finish_btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(ForgetPasswordFinishActivity.this, LoginActivity.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(loginIntent);
                //finishAffinity(); //NOT going back to login screen.
            }
        });
    }
    private void goNext(){
        forget_password_finish_btnNext = (ImageButton)findViewById(R.id.forget_password_finish_btnNext);

        forget_password_finish_btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tokenValidation = new Intent(ForgetPasswordFinishActivity.this, ValidateTokenActivity.class);
                if(mEmail!=null){
                    tokenValidation.putExtra(EXTRA_EMAIL,mEmail);
                    tokenValidation.putExtra(EXTRA_USERPROFILE,(Serializable) mUser);
                }
                startActivity(tokenValidation);
            }
        });
    }
}
