package com.sparkers.zatappdriver.Helpers;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sparkers.zatappdriver.Activities.DriverHome;
import com.sparkers.zatappdriver.Common.Common;
import com.sparkers.zatappdriver.Model.Driver;
import com.sparkers.zatappdriver.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONObject;

import dmax.dialog.SpotsDialog;

public class FirebaseHelper {
    AppCompatActivity activity;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference users;

    ConstraintLayout root;

    public FirebaseHelper(AppCompatActivity activity){
        this.activity=activity;
        root=activity.findViewById(R.id.root);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        users=firebaseDatabase.getReference(Common.user_driver_tbl);
        if(firebaseAuth.getUid()!=null)loginSuccess();
    }
    public void showLoginDialog(){
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(activity);
        alertDialog.setTitle(activity.getResources().getString(R.string.login));
        alertDialog.setMessage(activity.getResources().getString(R.string.fill_fields));

        LayoutInflater inflater=LayoutInflater.from(activity);
        View login_layout=inflater.inflate(R.layout.layout_login, null);
        final MaterialEditText etEmail=login_layout.findViewById(R.id.etEmail);
        final MaterialEditText etPassword=login_layout.findViewById(R.id.etPassword);

        alertDialog.setView(login_layout);
        alertDialog.setPositiveButton(activity.getResources().getString(R.string.login), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                //btnLogIn.setEnabled(false);
                if (TextUtils.isEmpty(etEmail.getText().toString())){
                    Snackbar.make(root, activity.getResources().getString(R.string.enter_email), Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(etPassword.getText().toString())){
                    Snackbar.make(root, activity.getResources().getString(R.string.enter_password), Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (etPassword.getText().toString().length()<6){
                    Snackbar.make(root, activity.getResources().getString(R.string.password_short), Snackbar.LENGTH_SHORT).show();
                    return;
                }
                final SpotsDialog waitingDialog=new SpotsDialog(activity);
                waitingDialog.show();
                firebaseAuth.signInWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        waitingDialog.dismiss();
                        goToMainActivity();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        waitingDialog.dismiss();
                        Snackbar.make(root, activity.getResources().getString(R.string.failed)+e.getMessage(), Snackbar.LENGTH_SHORT).show();
                        //btnLogIn.setEnabled(true);
                    }
                });
            }
        });
        alertDialog.setNegativeButton(activity.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }
    public void showRegistrerDialog(){
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(activity);
        alertDialog.setTitle(activity.getResources().getString(R.string.signin));
        alertDialog.setMessage(activity.getResources().getString(R.string.fill_fields));

        LayoutInflater inflater=LayoutInflater.from(activity);
        View registrer_layout=inflater.inflate(R.layout.layout_register, null);
        final MaterialEditText etEmail=registrer_layout.findViewById(R.id.etEmail);
        final MaterialEditText etPassword=registrer_layout.findViewById(R.id.etPassword);
        final MaterialEditText etName=registrer_layout.findViewById(R.id.etName);
        final MaterialEditText etPhone=registrer_layout.findViewById(R.id.etPhone);

        alertDialog.setView(registrer_layout);
        alertDialog.setPositiveButton(activity.getResources().getString(R.string.register), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                if (TextUtils.isEmpty(etEmail.getText().toString())){
                    Snackbar.make(root, activity.getResources().getString(R.string.enter_email), Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(etPassword.getText().toString())){
                    Snackbar.make(root, activity.getResources().getString(R.string.enter_password), Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (etPassword.getText().toString().length()<6){
                    Snackbar.make(root, activity.getResources().getString(R.string.password_short), Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(etName.getText().toString())){
                    Snackbar.make(root, activity.getResources().getString(R.string.enter_name), Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(etPhone.getText().toString())){
                    Snackbar.make(root, activity.getResources().getString(R.string.enter_phone), Snackbar.LENGTH_SHORT).show();
                    return;
                }
                firebaseAuth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Driver driver =new Driver();
                                //user.setEmail(etEmail.getText().toString());
                                driver.setName(etName.getText().toString());
                                //user.setPassword(etPassword.getText().toString());
                                driver.setPhone(etPhone.getText().toString());
                                //user.setVehicle("UberX");

                                users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(driver)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Snackbar.make(root, "Registered", Snackbar.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Snackbar.make(root, activity.getResources().getString(R.string.failed)+e.getMessage(), Snackbar.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(root, activity.getResources().getString(R.string.failed)+e.getMessage(), Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
        });

        alertDialog.setNegativeButton(activity.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }
    public void showRegisterPhone(final Driver driver, final GoogleSignInAccount account){
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(activity);
        alertDialog.setTitle(activity.getResources().getString(R.string.signin));
        alertDialog.setMessage(activity.getResources().getString(R.string.fill_fields));

        LayoutInflater inflater=LayoutInflater.from(activity);
        View register_phone_layout=inflater.inflate(R.layout.layout_register_phone, null);
        final MaterialEditText etPhone=register_phone_layout.findViewById(R.id.etPhone);

        alertDialog.setView(register_phone_layout);
        alertDialog.setPositiveButton(activity.getResources().getString(R.string.login), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                //user.setEmail(account.getEmail());
                driver.setName(account.getDisplayName());
                //user.setPassword(null);
                driver.setPhone(etPhone.getText().toString());
                //user.setVehicle("UberX");
                users.child(account.getId())
                        .setValue(driver)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Snackbar.make(root, activity.getResources().getString(R.string.registered), Snackbar.LENGTH_SHORT).show();
                                loginSuccess();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(root, activity.getResources().getString(R.string.failed)+e.getMessage(), Snackbar.LENGTH_SHORT).show();

                    }
                });
            }
        });
        alertDialog.setNegativeButton(activity.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }
    public void showRegisterPhone(final Driver driver, final String id, final String name, final String email){
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(activity);
        alertDialog.setTitle(activity.getResources().getString(R.string.signin));
        alertDialog.setMessage(activity.getResources().getString(R.string.fill_fields));

        LayoutInflater inflater=LayoutInflater.from(activity);
        View register_phone_layout=inflater.inflate(R.layout.layout_register_phone, null);
        final MaterialEditText etPhone=register_phone_layout.findViewById(R.id.etPhone);

        alertDialog.setView(register_phone_layout);
        alertDialog.setPositiveButton(activity.getResources().getString(R.string.login), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                //user.setEmail(email);
                driver.setName(name);
                //user.setPassword(null);
                driver.setPhone(etPhone.getText().toString());
                //user.setVehicle("UberX");
                users.child(id)
                        .setValue(driver)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Snackbar.make(root, activity.getResources().getString(R.string.registered), Snackbar.LENGTH_SHORT).show();
                                loginSuccess();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(root, activity.getResources().getString(R.string.failed)+e.getMessage(), Snackbar.LENGTH_SHORT).show();

                    }
                });
            }
        });
        alertDialog.setNegativeButton(activity.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                LoginManager.getInstance().logOut();
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }
    public void loginSuccess(){
        goToMainActivity();
    }
    private void goToMainActivity(){
        activity.startActivity(new Intent(activity, DriverHome.class));
        activity.finish();
    }
    public void registerByGoogleAccount(final GoogleSignInAccount account){
        final Driver driver =new Driver();
        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Driver post = dataSnapshot.child(account.getId()).getValue(Driver.class);

                if(post==null) showRegisterPhone(driver, account);
                else loginSuccess();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void registerByFacebookAccount(){
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        final String name=object.optString("name");
                        final String id=object.optString("id");
                        final String email=object.optString("email");
                        final Driver driver =new Driver();
                        users.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Driver post = dataSnapshot.child(id).getValue(Driver.class);

                                if(post==null) showRegisterPhone(driver, id, name, email);
                                else loginSuccess();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });
        request.executeAsync();
    }
}
