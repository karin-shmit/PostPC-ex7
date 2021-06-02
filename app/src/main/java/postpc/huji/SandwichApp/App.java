package postpc.huji.SandwichApp;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;


public class App extends Application {

    public FirebaseFirestore firestoreInstance;

    public App(){
//        firestoreInstance = FirebaseFirestore.getInstance();
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }
}