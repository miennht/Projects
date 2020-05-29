package fantasticmassage.com;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

import dto.User;

public class ValidateTokenActivity extends AppCompatActivity implements ValidateTokenAsyncResponseInterface {
    public static final String EXTRA_EMAIL = "EMAIL";
    public static final String EXTRA_USERPROFILE = "USERPROFILE";
    public static final String EXTRA_TAG = "EXTRA";
    public static final String EXTRA_VALUE = "FROM_VALIDATE_TOKEN";
    public static final String FORGET_PASSWORD_WRONG_TOKEN = "The validation code you entered is not correct or expired!";
    ImageButton validate_token_btnBack;
    Button      validate_token_btnNext,
                validate_token_btnResend;
    EditText    validate_token_edtToken;
    String      mEmail;
    User        mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate_token);
        mEmail = getIntent().getStringExtra(ForgetPasswordFinishActivity.EXTRA_EMAIL);
        mUser = (User) getIntent().getSerializableExtra(ForgetPasswordFinishActivity.EXTRA_USERPROFILE);
        goBack();
        goNext();
        exitForgetPassword();
    }
    private void goBack() {
        validate_token_btnBack = (ImageButton)findViewById(R.id.validate_token_btnBack);
        validate_token_btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void goNext(){
        validate_token_btnNext = (Button)findViewById(R.id.validate_token_btnNext);
        validate_token_edtToken = (EditText)findViewById(R.id.validate_token_edtToken);
        validate_token_btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean isTokenValid = validateToken(mEmail, validate_token_edtToken.getText().toString());
                System.out.println("Next clicked, isTokenValid: " + isTokenValid);
                //If the token is valid
                if(isTokenValid) {
                    Intent newPasswordIntent = new Intent(ValidateTokenActivity.this, EditPasswordNewPasswordActivity.class);
                    newPasswordIntent.putExtra(EXTRA_TAG, EXTRA_VALUE);
                    newPasswordIntent.putExtra(EXTRA_EMAIL, mEmail);
                    newPasswordIntent.putExtra(EXTRA_USERPROFILE, mUser);
                    startActivity(newPasswordIntent);
                }
                else
                    Toast.makeText(ValidateTokenActivity.this, FORGET_PASSWORD_WRONG_TOKEN, Toast.LENGTH_LONG).show();
            }
        });
    }
    private Boolean validateToken(String emailAddress, String token){
        ValidateTokenAsync validateTokenAsync = new ValidateTokenAsync(this);
        validateTokenAsync.delegate = this;
        boolean isTokenValid = false;
        try {
            isTokenValid = validateTokenAsync.execute(emailAddress, token).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return isTokenValid;
    }
    @Override
    public void processValidateTokenFinish(Boolean result) {

    }
    private void exitForgetPassword(){
        validate_token_btnResend = (Button) findViewById(R.id.validate_token_btnExit);
        validate_token_btnResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLogin();
            }
        });
    }
    private void goToLogin(){
        Intent loginIntent = new Intent(ValidateTokenActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginIntent);
    }
}
