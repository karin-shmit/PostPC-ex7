package postpc.huji.SandwichApp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

public class OrderInProgressActivity extends AppCompatActivity {
    private Order order;
    private ListenerRegistration status;

    App app;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_in_progress_activity);

        app = new App();
        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        order = (Order) intent.getSerializableExtra("order");

        status = db.collection("orders").document(order.getId())
                .addSnapshotListener((val, error) -> {
                    if (val != null && val.exists()){
                        Order order = val.toObject(Order.class);
                        if (order.getStatus().equals("done")){
                            startNewOrderActivity();
                            return;
                        }
                        if (order.getStatus().equals("ready")){
                            startActivityWithOrder(OrderIsReadyActivity.class, order);
                        }
                    }
                });
    }

    private void startNewOrderActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void startActivityWithOrder(Class<?> activity, Order order){
        Intent intent = new Intent(this, activity);
        intent.putExtra("order", order);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        status.remove();
    }
}
