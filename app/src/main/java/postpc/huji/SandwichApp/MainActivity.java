package postpc.huji.SandwichApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;


public class MainActivity extends AppCompatActivity {

    App app;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String orderId = sp.getString("orderId", "");

        app = new App();
        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();

        if (!orderId.equals("")) {
            goToOrderStatus(orderId);
            return;
        }

        startNewOrderActivity();

    }

    private void goToOrderStatus(String orderId){
        db.collection("orders").document(orderId).get().
                addOnSuccessListener(res -> {

                    if (res == null){
                        startNewOrderActivity();
                        return;
                    }
                    Order order = res.toObject(Order.class);
                    if (order == null){
                        startNewOrderActivity();
                        return;
                    }
                    if (order.getStatus().equals("done")){
                        startNewOrderActivity();
                    }
                    if (order.getStatus().equals("waiting")){
                        startActivityWithOrder(EditOrderActivity.class, order);
                        return;
                    }
                    if (order.getStatus().equals("in progress")){
                        startActivityWithOrder(OrderInProgressActivity.class, order);
                        return;
                    }
                    if (order.getStatus().equals("ready")){
                        startActivityWithOrder(OrderIsReadyActivity.class, order);
                    }
                });
    }

    private void startNewOrderActivity(){
        Intent intent = new Intent(this, NewOrderActivity.class);
        startActivity(intent);
        finish();
    }

    private void startActivityWithOrder(Class<?> activity, Order order){
        Intent intent = new Intent(this, activity);
        intent.putExtra("order", order);
        startActivity(intent);
        finish();
    }

}
