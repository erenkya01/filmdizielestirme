package com.example.filmdizi.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.filmdizi.R;
import com.example.filmdizi.ui.main.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText editEmail, editPassword;
    private Button btnLogin;
    private TextView textGoToRegister, textForgotPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Firebase Auth Başlatma
        mAuth = FirebaseAuth.getInstance();

        // XML Bağlantıları
        editEmail = findViewById(R.id.edit_login_email);
        editPassword = findViewById(R.id.edit_login_password);
        btnLogin = findViewById(R.id.btn_login);
        textGoToRegister = findViewById(R.id.text_go_to_register);

        // Not: Eğer activity_login.xml içine eklemediysen bu ID hata verebilir, eklemeni öneririm.
        textForgotPassword = findViewById(R.id.text_forgot_password);

        // OTOMATİK GİRİŞ: Kullanıcı zaten giriş yapmışsa ve e-postası onaylıysa direkt içeri al
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null && currentUser.isEmailVerified()) {
            goToMainActivity();
        }

        // Giriş Butonu
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        // Kayıt Ol Sayfasına Git
        textGoToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        // Şifremi Unuttum Sayfasına Git
        if (textForgotPassword != null) {
            textForgotPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                }
            });
        }
    }

    private void loginUser() {
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            editEmail.setError("E-posta adresi giriniz");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editPassword.setError("Şifre giriniz");
            return;
        }

        // Firebase ile Giriş Yapma
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();

                        // E-POSTA ONAY KONTROLÜ: Kullanıcı mailine gelen linke tıkladı mı?
                        if (user != null && user.isEmailVerified()) {
                            Toast.makeText(LoginActivity.this, "Hoş geldiniz!", Toast.LENGTH_SHORT).show();
                            goToMainActivity();
                        } else {
                            // Onaylanmamışsa uyar ve çıkış yaptır
                            Toast.makeText(LoginActivity.this, "Lütfen önce e-postanızı onaylayın!", Toast.LENGTH_LONG).show();
                            mAuth.signOut();
                        }
                    } else {
                        // Giriş başarısızsa hata mesajı göster
                        Toast.makeText(LoginActivity.this, "Giriş başarısız: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void goToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Geri tuşuna basınca login ekranına dönmemesi için
    }
}