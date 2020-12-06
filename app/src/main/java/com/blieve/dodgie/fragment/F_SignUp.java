package com.blieve.dodgie.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.blieve.dodgie.R;
import com.blieve.dodgie.activity.A_Options;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static android.content.Context.MODE_PRIVATE;
import static com.blieve.dodgie.activity.A_Options.PREF_CONFIG;

public class F_SignUp extends Fragment {

    private EditText inp_alias, inp_email, inp_pass, inp_pass2;
    private Button btn_signUp;
    private int lang;
    FirebaseAuth auth;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.f_sign_up, container, false);

        inp_alias = v.findViewById(R.id.signUp_alias);
        inp_email = v.findViewById(R.id.signUp_email);
        inp_pass = v.findViewById(R.id.signUp_pass);
        inp_pass2 = v.findViewById(R.id.signUp_pass2);
        btn_signUp = v.findViewById(R.id.signUp_btn);

        init();
        return v;
    }

    private void init() {
        SharedPreferences pref = getActivity().getSharedPreferences(PREF_CONFIG, MODE_PRIVATE);
        lang = pref.getInt(A_Options.LANGUAGE, 0);
        setTextsLang();
        auth = FirebaseAuth.getInstance();
        clickListen();
    }

    private void clickListen() {
        View.OnClickListener onclick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v == btn_signUp) {
                    signUp();
                }
            }
        };
        btn_signUp.setOnClickListener(onclick);
    }

    private boolean signUp() {
        String alias = require(inp_alias);
        if(alias == null) return false;
        String email = require(inp_email);
        if(email == null) return false;
        String pass = require(inp_pass);
        if(pass == null) return false;
        String pass2 = require(inp_pass2);
        if(pass2 == null) return false;
        final boolean[] completed = {false};
        if(validate(alias, pass, pass2)) {
            auth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(getActivity(),
                    new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        completed[0] = true;
                    }
                }
            });
        }
        return completed[0];
    }

    private boolean validate(@NotNull String alias, String pass, String pass2) {
        if(alias.length() > 12) {
            String[] msg = new String[]{
                    "Alias up to 12 characters",
                    "Alias hasta 12 caracteres"
            };
            inp_alias.setError(msg[lang]);
            inp_alias.requestFocus();
            return false;
        } else if(pass.length() > 15) {
            String[] msg = new String[]{
                    "Password up to 12 characters",
                    "Contraseña hasta 12 caracteres"
            };
            inp_pass.setError(msg[lang]);
            inp_pass.requestFocus();
            return false;
        } else if(!pass2.equals(pass)) {
            String[] msg = new String[]{
                    "The passwords do not match",
                    "Las contraseñas no coinciden"
            };
            inp_pass2.setError(msg[lang]);
            inp_pass.requestFocus();
            return false;
        }
        inp_alias.setError(null);
        inp_pass.setError(null);
        inp_pass2.setError(null);
        return true;
    }

    @Nullable
    private String require(@NotNull EditText txt) {
        String val = txt.getText().toString().trim();
        if(val.isEmpty()) {
            String[] msg = new String[]{
                    "Field required",
                    "Campo requerido"
            };
            txt.setError(msg[lang]);
            txt.requestFocus();
            return null;
        }
        txt.setError(null);
        return val;
    }

    private void setTextsLang() {
        inp_alias.setHint(new String[]{
                "Alias",
                "Alias"
        }[lang]);
        inp_email.setHint(new String[]{
                "Email",
                "Correo"
        }[lang]);
        inp_pass.setHint(new String[]{
                "Password",
                "Contraseña"
        }[lang]);
        inp_pass2.setHint(new String[]{
                "Confirm password",
                "Confirmar contraseña"
        }[lang]);
        btn_signUp.setText(new String[]{
                "Sign up",
                "Registrarse"
        }[lang]);
    }

}