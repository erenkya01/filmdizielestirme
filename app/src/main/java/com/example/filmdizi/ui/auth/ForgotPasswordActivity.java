package com.example.filmdizi.ui.auth;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.filmdizi.R;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText editEmail;
    private Button btnReset;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password); // Bu XML'i de benzer şekilde oluşturabilirsin

        mAuth = FirebaseAuth.getInstance();
        editEmail = findViewById(R.id.edit_forgot_email);
        btnReset = findViewById(R.id.btn_reset_password);

        btnReset.setOnClickListener(v -> {
            String email = editEmail.getText().toString().trim();
            if (!email.isEmpty()) {
                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(this, "Şifre sıfırlama linki gönderildi!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
            }
        });
    }
}