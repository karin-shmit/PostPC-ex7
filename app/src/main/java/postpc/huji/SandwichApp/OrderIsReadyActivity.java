package postpc.huji.SandwichApp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class OrderIsReadyActivity extends AppCompatActivity {
    private Button doneButton;
    private Order order;

    App app;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_is_ready_activity);

        app = new App();
        db = FirebaseFirestore.getInstance();

        doneButton = findViewById(R.id.doneButton);

        Intent intent = getIntent();
        order = (Order) intent.getSerializableExtra("order");

        doneButton.setOnClickListener(v -> doneOrder());

    }

    private void doneOrder(){
        order.setStatus("done");
        db.collection("orders").document(order.getId()).
                update("status", order.getStatus()).addOnSuccessListener(u -> {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
            sp.edit().putString("orderId", null).apply();
            Toast toast = Toast.makeText(this,"Goodbye", Toast.LENGTH_LONG);
            toast.show();
            Intent intent = new Intent(OrderIsReadyActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
