package com.example.filmdizi.ui.auth;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.filmdizi.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    // YENİ DEĞİŞKENLER BURADA
    private EditText editFirstName, editLastName, editUsername, editDob, editEmail, editPassword, editPasswordConfirm;
    private Button btnRegister;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // XML BAĞLANTILARI
        editFirstName = findViewById(R.id.edit_reg_first_name);
        editLastName = findViewById(R.id.edit_reg_last_name);
        editUsername = findViewById(R.id.edit_reg_username);
        editDob = findViewById(R.id.edit_reg_dob); // Doğum Tarihi
        editEmail = findViewById(R.id.edit_reg_email);
        editPassword = findViewById(R.id.edit_reg_password);
        editPasswordConfirm = findViewById(R.id.edit_reg_password_confirm);
        btnRegister = findViewById(R.id.btn_register);

        btnRegister.setOnClickListener(v -> createAccount());
    }

    private void createAccount() {
        String firstName = editFirstName.getText().toString().trim();
        String lastName = editLastName.getText().toString().trim();
        String username = editUsername.getText().toString().trim();
        String dob = editDob.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        String passwordConfirm = editPasswordConfirm.getText().toString().trim();

        // 1. KONTROL: Boş alan var mı?
        if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(username) ||
                TextUtils.isEmpty(dob) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(passwordConfirm)) {
            Toast.makeText(this, "Lütfen tüm alanları doldurun!", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. KONTROL: Şifreler eşleşiyor mu?
        if (!password.equals(passwordConfirm)) {
            editPasswordConfirm.setError("Şifreler uyuşmuyor!");
            return;
        }

        // 3. KONTROL: Şifre uzunluğu
        if (password.length() < 6) {
            editPassword.setError("Şifre en az 6 karakter olmalı!");
            return;
        }

        // Firebase Kayıt İşlemi
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {

                            // YENİ VERİLERİ FİRESTORE'A KAYDET
                            Map<String, Object> userData = new HashMap<>();
                            userData.put("firstName", firstName);
                            userData.put("lastName", lastName);
                            userData.put("username", username);
                            userData.put("dob", dob); // Doğum tarihi eklendi
                            userData.put("email", email);

                            db.collection("Users").document(user.getUid()).set(userData)
                                    .addOnCompleteListener(task1 -> {
                                        // Onay Maili Gönder
                                        user.sendEmailVerification();
                                        Toast.makeText(this, "Kayıt Başarılı! Lütfen e-postanızı kontrol edip onaylayın.", Toast.LENGTH_LONG).show();

                                        // Çıkış yaptır ve geri dön
                                        mAuth.signOut();
                                        finish();
                                    });
                        }
                    } else {
                        Toast.makeText(this, "Hata: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}