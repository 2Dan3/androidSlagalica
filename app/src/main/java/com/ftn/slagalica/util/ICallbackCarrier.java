package com.ftn.slagalica.util;

import com.google.firebase.auth.FirebaseUser;

public interface ICallbackCarrier {
    void onResult(FirebaseUser foundPlayer);
}
