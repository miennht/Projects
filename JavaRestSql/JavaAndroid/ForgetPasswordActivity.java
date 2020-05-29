package fantasticmassage.com;

//import org.mindrot.jbcrypt.BCrypt;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import dto.User;

public class ForgetPasswordActivity extends Activity implements
        SendEmailAsyncResponseInterface,
		TokenGenerateHashStoreAsyncResponseInterface{
    public static final String EXTRA_EMAIL = "EMAIL";
    public static final String EXTRA_USERPROFILE = "USERPROFILE";
    public static final String FORGET_PASSWORD_MISSING_EMAIL = "Please enter the email you registered!";
	public static final String FORGET_PASSWORD_MISSING_TOKEN = "Reset password encounters an error! Please contact Administrator.";
    private static final String URL_FORGET_PASSWORD = "http://10.0.2.2:8080/com.fms.FMSRestfulWS/login/forgetpassword";
    private static final String MSG_FORGET_PASSWORD_PROGRESS = "Your request is being processed. Please wait!";
	private static final Date CURRENT_JAVA_DT = Calendar.getInstance().getTime();//MM-dd-yyyy
	private static final SimpleDateFormat USER_FRIENDLY_SDF_WITH_TIME = new SimpleDateFormat("EEE, d MMM yyyy HH:mm");
	private final String CURRENT_DT = USER_FRIENDLY_SDF_WITH_TIME.format(CURRENT_JAVA_DT);//Fri, 2 Aug 2019 18:21
    private static String EMAIL_PASSWORD_RESET_TEMPLATE = "";
	private static String EMAIL_SENDER_USERNAME = "";
	private static String EMAIL_SENDER_PASSWORD = "";
	private static String EMAIL_SUBJECT = "";
	private static String EMAIL_BODY = "";
	private static String EMAIL_SENDER_ADDRESS = "";
	private static String EMAIL_RECIPIENTS_ADDRESS = "roseng0102@yahoo.com";
	private static final String EMAIL_BANNER_FILE = "23.jpg";//Make sure this file is in your device ../FMSImages folder.
	// Note: This is a banner. It needs to be accessible for every devices, place it on a server or assets folder
	private ArrayList<String> EMAIL_ATTACHMENTS = new ArrayList<String>();

	ImageButton forget_password_btnBack,
				forget_password_btnNext;
	EditText 	forget_password_edtEmail;
	ProgressDialog forget_password_prgDialog;
	User mUser;
	String mToAddress;
	Boolean mIsUserExist;
	String mToken;
	TokenGenerateHashStoreAsync tokenGenerateHashStoreAsync = null;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget_password);
		//Obtain global variables
        EMAIL_PASSWORD_RESET_TEMPLATE = getString(R.string.email_template_forget_password_nobutton);
        EMAIL_SENDER_USERNAME = getString(R.string.email_sender_username);
        EMAIL_SENDER_PASSWORD = getString(R.string.email_sender_password);
        EMAIL_SENDER_ADDRESS = getString(R.string.email_sender_address);
        EMAIL_SUBJECT = getString(R.string.email_subject_change_password);
		//Declare the token generator
		try {
			tokenGenerateHashStoreAsync = new TokenGenerateHashStoreAsync(ForgetPasswordActivity.this);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		tokenGenerateHashStoreAsync.delegate = this;
		goBack();
		goNext();
	}
	private void goBack(){
		forget_password_btnBack = (ImageButton)findViewById(R.id.forget_password_btnBack);
		forget_password_btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	private void goNext(){
		forget_password_btnNext = (ImageButton)findViewById(R.id.forget_password_btnNext);
		forget_password_btnNext.setOnClickListener(new View.OnClickListener() {
			@RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
			public void onClick(View v) {
				forget_password_edtEmail = (EditText)findViewById(R.id.forget_password_edtEmail);
				mToAddress = forget_password_edtEmail.getText().toString();

				if (Utility.isNotNull(mToAddress)&&Utility.isEmailValid(mToAddress)){
                    forget_password_prgDialog = new ProgressDialog(ForgetPasswordActivity.this);
                    forget_password_prgDialog.setMessage(MSG_FORGET_PASSWORD_PROGRESS);
                    forget_password_prgDialog.setCancelable(false);
                    forget_password_prgDialog.show();
					//Note: MienNguyen 03/02/2017 There should have an extra step to check if the email address is associated
					//with the userID from previous screen. If they matches then perform changePass. Otherwise, show an error message
					//changePass process:
					//String token = Utility.TokenGenerator(toAdd);
					//System.out.println("token = " + token);

					//Call AsyncTask to check if the user exists in DB
					mUser = getUser(mToAddress);
					System.out.println("Inside ForgetPasswordActivity, returned mUser: " + mUser);
					if (mUser!=null) {
					    /*If user exists in the system, send an email with reset hyperlink containing the random token and timestamp that associated with the account
                        When the email is sent out, it contains a URL such as “Reset/?id=3ce7854015cd38c862cb9e14a1ae552b”
                        and when the user loads this, the page checks for the existence of the token and consequently confirms the identity of the user and allows the password to be changed.
                        we need to ensure that the URL is loaded over HTTPS. No, posting to HTTPS is not enough, that URL with the token must implement transport layer security so that the new password form cannot be MITM’d and the password the user creates is sent back over a secure connection.
                        Finally, we want to ensure that this is a one-time process. Once the reset process is complete,
                        the token should be deleted so that the reset URL is no longer functional.
						So for example I'd generate/send an email to user@example.com with a link
						http://www.example.com/changePassword?token=uniqueTokenForTheUser
						uniqueTokenForTheUser would be stored in a table that gets checked by a thread in a certain interval and removes the record once the token is expired in case the user did not actually change his password.
						*/
					    //0. Delete any existing reset token for the user (UNDER_CONSTRUCTION)
						//1. Generate a random recovery token (128 bits should be fine)
						//2. store a hash of this token (created using a password hashing algorithm – bcrypt, SHA256).
                        // Token hashing is not as important as password hashing, I still implement it however
                        // also store the token creation timestamp in your database;
                        tokenGenerateHashStoreAsync.execute(mToAddress);
                        mIsUserExist = true;
						//3. The process for validating a reset request is then quite simple: take the user ID and the plaintext token (as supplied in the reset link)
						// and check that they match up in the database by computing H(token) again.
                    }
					else {
						//Send an email saying that user (or somebody else) initialized the change password but the account doesn't exist
                        mIsUserExist = false;
                        //Email body contains the message telling the user that the account does NOT exist in the system.
                        String htmlAsString = getString(R.string.UserNotExistEmail);
                        EMAIL_BODY = htmlAsString;
                        sendEmail();
					}
				} else {
					//Note: It'd better if you implement a TextWatcher to enable the Next button whenever the edit text has value
					Toast.makeText(ForgetPasswordActivity.this, FORGET_PASSWORD_MISSING_EMAIL, Toast.LENGTH_LONG).show();
				}
			}
		});
	}
    //User EXISTS in the system
	@Override
	public void processSaveHashFinish(String result) {
	    mToken = result;
        WebView mWebView = (WebView) findViewById(R.id.webView1); //mWebView is invisible in GUI
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.requestFocusFromTouch();
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.addJavascriptInterface(this, "AndroidLoader");
        mWebView.addJavascriptInterface(this, "HtmlViewer");
        mWebView.loadUrl(EMAIL_PASSWORD_RESET_TEMPLATE);//This method invokes the html file, where the two interface (webConnector and toaster) are invoked.
        mWebView.setWebViewClient(new ForgetPasswordWebViewClient());
	}
    private class ForgetPasswordWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            view.setWebViewClient(null);
            view.loadUrl("javascript:window.HtmlViewer.showHtml('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");//WORKING. Invoke the showHtml method below, pass in the content within the <html> tag
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (URL_FORGET_PASSWORD.equals(Uri.parse(url).getHost())) {
                // This is my website, so do not override; let my WebView load the page
                return false;
            }
            // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
            //Intent changePasswordIntent = new Intent(ForgetPasswordActivity.this, EditPasswordNewPasswordActivity.class);
            //startActivity(changePasswordIntent);
            //ValidateTokenFragment.newInstance(ForgetPasswordActivity.this,url);//Commented on 05/19/2020. This method is invoked when user enters the token provided in the email to login
            return true;
        }
    }
    //JavaScriptInterface methods
    @JavascriptInterface
    public String loadUserId() {
        return mUser.getUserId();
    }
    @JavascriptInterface
    public String loadToken() {
        return mToken;
    }
    @JavascriptInterface
    public String loadURLScheme() {
        return getString(R.string.url_scheme);
    }
    @JavascriptInterface
    public String loadURLHost() {
        return getString(R.string.url_host2);
    }
    @JavascriptInterface
    public String loadURLPathPrefix() {
        return getString(R.string.url_pathPrefix);
    }
    @JavascriptInterface
    public void showHtml(final String message){//NEED the final tag here
        if (mIsUserExist) {
            if (!mToken.contains("HASH_EXCEPTION")) {
                //Email body contains the https hyperlink to reset password
                EMAIL_BODY = "<![CDATA" + message;
                System.out.println("Inside showHtml, EMAIL_BODY: " + EMAIL_BODY);
            }
            else
                //Email body contains the exception of generating token
                Toast.makeText(ForgetPasswordActivity.this, FORGET_PASSWORD_MISSING_TOKEN, Toast.LENGTH_LONG).show();
        }
        sendEmail();
    }
    private void sendEmail(){
        ArrayList parameter = new ArrayList();
        boolean valid = true;
        System.out.println("Inside ForgetPasswordActivity, sendEmail, toEmail:" + mToAddress);
        EMAIL_RECIPIENTS_ADDRESS = mToAddress;
        //EMAIL_ATTACHMENTS.add(0, EMAIL_BANNER_FILE);//First attachment in the list is the email's banner
        //Start sending email
        parameter.add(0,EMAIL_SUBJECT);
        parameter.add(1,EMAIL_BODY);
        parameter.add(2,EMAIL_SENDER_ADDRESS);
        parameter.add(3,EMAIL_RECIPIENTS_ADDRESS);
        parameter.add(4,EMAIL_ATTACHMENTS);
        //It is better to use matcher to validate the sender address and password before sending emails
        if (!(Utility.isNotNull(EMAIL_SENDER_USERNAME) && Utility.isNotNull(EMAIL_SENDER_PASSWORD)))
            valid = false;
        if (valid)
        {
            try {
                SendEmailsAsync sendEmailsAsync = new SendEmailsAsync(this, EMAIL_SENDER_USERNAME, EMAIL_SENDER_PASSWORD);
                sendEmailsAsync.delegate = this;
                sendEmailsAsync.execute(parameter);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void processSendEmailFinish(String result){
        forget_password_prgDialog.hide();
        Intent intentSendEmailFinish = new Intent (ForgetPasswordActivity.this, ForgetPasswordFinishActivity.class);
        if ((mIsUserExist)) {
            intentSendEmailFinish.putExtra(EXTRA_EMAIL, mUser.getUserId());
            intentSendEmailFinish.putExtra(EXTRA_USERPROFILE, (Serializable) mUser);
        }
        //This key to identify the Extra is from the ForgetPasswordActivity
        intentSendEmailFinish.putExtra("EXTRA","FROM_FORGET_PASSWORD");
        startActivity(intentSendEmailFinish);

    }
	//Note 01/04/2020: If the userId doesn't exist, returns User = null
	private User getUser(String userId){
		LoadUserAsync loadUserAsync = new LoadUserAsync();
		User user = null;
		try {
			user = loadUserAsync.execute(userId).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return user;
	}

}
