package com.blieve.dodgie.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.blieve.dodgie.R;
import com.blieve.dodgie.activity.A_Options;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static android.content.Context.MODE_PRIVATE;
import static com.blieve.dodgie.activity.A_Options.PREF_CONFIG;

public class F_SignIn extends Fragment {

    private EditText inp_alias, inp_pass;
    private Button btn_signIn;
    private int lang;
    FirebaseAuth auth;

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
                if(v == btn_signIn) {
                    signIn();
                }
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
            inp_alias.setError(new String[]{
                    "Invalid alias",
                    "Alias inválido"
            }[lang]);
            inp_alias.requestFocus();
            return false;
        } else if(pass.length() > 15) {
            inp_pass.setError(new String[]{
                    "Invalid password",
                    "Contraseña inválida"
            }[lang]);
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
            String[] msg = new String[]{
                    "Required field",
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
        inp_pass.setHint(new String[]{
                "Password",
                "Contraseña"
        }[lang]);
        btn_signIn.setText(new String[]{
                "Sign in",
                "Ingresar"
        }[lang]);
    }

}
