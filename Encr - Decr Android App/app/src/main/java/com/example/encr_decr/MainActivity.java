package com.example.encr_decr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.MessageDigest;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {

    String outputString;
    String AES = "AES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button encryption_btn = (Button) findViewById(R.id.encryption_btn);
        final EditText encryption_input = (EditText) findViewById(R.id.encryption_input);
        Button decryption_btn = (Button) findViewById(R.id.decryption_btn);
        final EditText decryption_input = (EditText) findViewById(R.id.decryption_input);
        final EditText key = (EditText) findViewById(R.id.key);

        encryption_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    outputString = encrypt (encryption_input.getText().toString(), key.getText().toString());
                    decryption_input.setText(outputString);

                    ClipboardManager clipboard = (ClipboardManager)
                            getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("encryption_text", decryption_input.getText());
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getApplicationContext(), "Copied to clipboard", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        decryption_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    //outputString = decrypt (outputString, password);
                    outputString = decrypt (decryption_input.getText().toString(), key.getText().toString());
                    decryption_input.setText(outputString);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
}

    private String decrypt(String outputString, String password) throws Exception {
        SecretKeySpec key = generateKey (password);
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedValue = Base64.decode(outputString, Base64.DEFAULT);
        byte[] decValue = c.doFinal(decodedValue);
        String decryptedValue = new String (decValue);
        return decryptedValue;
    }

    private String encrypt(String Data, String password) throws Exception {
        SecretKeySpec key = generateKey (password);
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(Data.getBytes());
        String encryptedValue = Base64.encodeToString(encVal, Base64.DEFAULT);
        return encryptedValue;
    }

    private SecretKeySpec generateKey(String password) throws Exception{
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = password.getBytes("UTF-8");
        digest.update(bytes, 0, bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        return secretKeySpec;
    }
}