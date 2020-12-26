package com.blieve.dodgie.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.blieve.dodgie.R;
import com.blieve.dodgie.activity.A_Options;
import com.blieve.dodgie.util.Droid;

public class F_SignUp extends Fragment {

    private Button btn_signUp;
    private EditText inp_alias, inp_email, inp_pass, inp_pass2;
    // FirebaseAuth auth;

    private Droid.Lang lang;
    private Droid.Media media;

    private final String invalidAlias = "invalidAlias",
            invalidPassword = "invalidPass",
            invalidConfirmPassword = "invalidPass2",
            requiredField = "required";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
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
        media = Droid.Media.get();

        initLang();
        // auth = FirebaseAuth.getInstance();
        clickListen();
    }

    private void clickListen() {
        View.OnClickListener onclick = v -> {
            media.playSound(Droid.Media.CLICK);
            if(v == btn_signUp) {
                signUp();
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
            /*auth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(getActivity(), task -> {
                if(task.isSuccessful()) {
                    completed[0] = true;
                }
            });*/
        }
        return completed[0];
    }

    private boolean validate(String alias, String pass, String pass2) {
        if(alias.length() > 12) {
            inp_alias.setError(lang.getText(alias));
            inp_alias.requestFocus();
            return false;
        } else if(pass.length() > 15) {
            inp_pass.setError(lang.getText(invalidPassword));
            inp_pass.requestFocus();
            return false;
        } else if(!pass2.equals(pass)) {
            inp_pass2.setError(lang.getText(invalidConfirmPassword));
            inp_pass.requestFocus();
            return false;
        }
        inp_alias.setError(null);
        inp_pass.setError(null);
        inp_pass2.setError(null);
        return true;
    }

    private String require(EditText txt) {
        String val = txt.getText().toString().trim();
        if(val.isEmpty()) {
            txt.setError(lang.getText(requiredField));
            txt.requestFocus();
            return null;
        }
        txt.setError(null);
        return val;
    }

    private void initLang() {
        int english = Droid.Lang.indexOf(Droid.Lang.ENGLISH),
                spanish = Droid.Lang.indexOf(Droid.Lang.SPANISH);
        String alias = "alias",
                email = "email",
                password = "pass",
                confirmPassword = "pass2",
                signUp = "signUp";
        lang = new Droid.Lang();

        lang.addText(alias, english, "Alias");
        lang.addText(alias, spanish, "Alias");
        lang.addText(email, english, "Email");
        lang.addText(email, spanish, "Correo");
        lang.addText(password, english, "Password");
        lang.addText(password, spanish, "Contraseña");
        lang.addText(confirmPassword, english, "Confirm password");
        lang.addText(confirmPassword, spanish, "Confirmar contraseña");
        lang.addText(signUp, english, "Sign up");
        lang.addText(signUp, spanish, "Registrarse");

        lang.addText(invalidAlias, english, "Alias too long");
        lang.addText(invalidAlias, spanish, "Alias demasiado largo");
        lang.addText(invalidPassword, english, "Password too long");
        lang.addText(invalidPassword, spanish, "Contraseña demasiado larga");
        lang.addText(invalidPassword, english, "The passwords do not match");
        lang.addText(invalidPassword, spanish, "Las contraseñas no coinciden");
        lang.addText(requiredField, english, "Required field");
        lang.addText(requiredField, spanish, "Campo requerido");

        inp_alias.setHint(alias);
        inp_email.setHint(email);
        inp_pass.setHint(password);
        inp_pass2.setHint(confirmPassword);
        btn_signUp.setText(signUp);
    }

}