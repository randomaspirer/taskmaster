package jaihind.gobblessamerica.taskmaster.mvp.authentication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import jaihind.gobblessamerica.taskmaster.mvp.view.BaseActivity;
import jaihind.gobblessamerica.taskmaster.mvp.view.Navigationview;
import jaihind.gobblessamerica.taskmaster.R;
import jaihind.gobblessamerica.taskmaster.di.ApplicationComponent;

public class GoogleSignin extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener {


    private static final String TAG = "firebase_debug";
    @Inject
    FirebaseAuth mAuth;
    //private FirebaseAuth mAuth;

    @Inject
    @Named("CheckConnection")
    boolean isOnline;

    @Inject
    GoogleSignInOptions gso;
    // [START declare_auth]
    // [END declare_auth]
    @BindView(R.id.googlesignin_bt)
    SignInButton Googlesignin_bt;


    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_signin);
        ApplicationComponent.getDi().inject(this);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();

        Googlesignin_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplicationComponent.getDi().inject(GoogleSignin.this);
                if (isOnline) {
                    signIn();
                } else {
                    Toast.makeText(GoogleSignin.this, "Check Network connection", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    // [START signin]
    private void signIn() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, 9001);
    }
    // [END signin]

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        updateUI(currentUser);
    }
    // [END on_start_check_user]


    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 9001) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // [START_EXCLUDE]
                result.getStatus();
                updateUI(null);
                // [END_EXCLUDE]
            }
        }
    }
    // [END onactivityresult]


    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("firebase", "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(GoogleSignin.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }


                });
    }

    // [END auth_with_google]
    private void updateUI(FirebaseUser user) {
        hideProgressDialog();

        if (user != null) {
            Intent signIntent = new Intent(this, Navigationview.class);
            startActivity(signIntent);
           /* mStatusTextView.setText(getString(R.string.google_status_fmt, user.getEmail()));
            mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));

            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);*/
        } else {
           /* mStatusTextView.setText(R.string.signed_out);
            mDetailTextView.setText(null);

            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);*/
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        String con_result = connectionResult.getErrorMessage();

    }


}
