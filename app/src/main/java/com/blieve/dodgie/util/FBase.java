package com.blieve.dodgie.util;

import androidx.annotation.NonNull;

import com.blieve.dodgie.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class FBase {

    private final static String REF_USERS = "users";

    private FBase() {
    }

    @NotNull
    private static DatabaseReference getRef(String ref) {
        return FirebaseDatabase.getInstance().getReference(ref);
    }

    public static boolean saveUserData(@NotNull User user) {
        DatabaseReference ref = getRef(REF_USERS);
        return ref.child("alias").setValue(user).isSuccessful();
    }

    public static User getUserData(final String username) {
        DatabaseReference ref = getRef(REF_USERS);
        Query getData = ref.orderByChild(User.ALIAS).equalTo(username);
        final User[] user = new User[1];
        getData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    user[0] = dataSnapshot.child(username).getValue(User.class);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                user[0] = null;
            }
        });
        return user[0];
    }

    /*public static void signInWithEmail(String email, String password,
            OnCompleteListener<AuthResult> onCompleteListener) {
        FirebaseAuthbaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(a, onCompleteListener);
    }

    public static void signUpWithEmail(Activity a, String email, String password,
           OnCompleteListener<AuthResult> onCompleteListener) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(a, onCompleteListener);
    }

    public static void signOut() {
        FirebaseAuth.getInstance().signOut();
    }

    @NotNull
    public static String getErrorKind(@NotNull Task<AuthResult> task) {
        String error = Objs.nonNull(task.getException()).getMessage() + "";
        if(error.contains("network")) return "network";
        if(error.contains("format")) return "format";
        if(error.contains("no user")) return "email";
        if(error.contains("password")) return "password";
        return "unknown";
    }*/

}
