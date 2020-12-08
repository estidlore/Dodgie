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
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class F_SignIn extends Fragment {

    private EditText inp_alias, inp_pass;
    private Button btn_signIn;
    FirebaseAuth auth;

    private Droid.Lang lang;
    private final String invalidAlias = "invalidAlias",
            invalidPassword = "invalidPass",
            requiredField = "required";

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.f_sign_in, container, false);

        inp_alias = v.findViewById(R.id.signIn_alias);
        inp_pass = v.findViewById(R.id.signIn_pass);
        btn_signIn = v.findViewById(R.id.signIn_btn);

        init();
        return v;
    }

    private void init() {
        initLang();
        auth = FirebaseAuth.getInstance();
        clickListen();
    }

    private void clickListen() {
        View.OnClickListener onclick = v -> {
            if(v == btn_signIn) {
                signIn();
            }
        };
        btn_signIn.setOnClickListener(onclick);
    }

    private boolean signIn() {
        String alias = require(inp_alias);
        if(alias == null) return false;
        String pass = require(inp_pass);
        if(pass == null) return false;
        final boolean[] completed = {false};
        if(validate(alias, pass)) {

        }
        return completed[0];
    }

    private boolean validate(@NotNull String alias, String pass) {
        if(alias.length() > 12) {
            inp_alias.setError(lang.getText(invalidAlias));
            inp_alias.requestFocus();
            return false;
        } else if(pass.length() > 15) {
            inp_pass.setError(lang.getText(invalidPassword));
            inp_pass.requestFocus();
            return false;
        }
        inp_alias.setError(null);
        inp_pass.setError(null);
        return true;
    }

    @Nullable
    private String require(@NotNull EditText txt) {
        String val = txt.getText().toString().trim();
        if(val.isEmpty()) {
            txt.setError(requiredField);
            txt.requestFocus();
            return null;
        }
        txt.setError(null);
        return val;
    }

    private void initLang() {
        int english = Droid.Lang.indexOf(A_Options.ENGLISH),
                spanish = Droid.Lang.indexOf(A_Options.SPANISH);
        String alias = "alias",
                password = "pass",
                signIn = "signIn";
        lang = new Droid.Lang();

        lang.addText(alias, english, "Alias");
        lang.addText(alias, spanish, "Alias");
        lang.addText(password, english, "Password");
        lang.addText(password, spanish, "Contraseña");
        lang.addText(signIn, english, "Sign in");
        lang.addText(signIn, spanish, "Ingresar");

        lang.addText(invalidAlias, english, "Invalid alias");
        lang.addText(invalidAlias, spanish, "Alias inválido");
        lang.addText(invalidPassword, english, "Invalid password");
        lang.addText(invalidPassword, spanish, "Contraseña inválida");
        lang.addText(requiredField, english, "Required field");
        lang.addText(requiredField, spanish, "Campo requerido");

        inp_alias.setHint(lang.getText(alias));
        inp_pass.setHint(lang.getText(password));
        btn_signIn.setText(lang.getText(signIn));

    }

}
