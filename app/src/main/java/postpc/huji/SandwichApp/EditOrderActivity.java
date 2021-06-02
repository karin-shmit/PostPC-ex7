package postpc.huji.SandwichApp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

public class EditOrderActivity extends AppCompatActivity {
    private CheckBox hummus;
    private CheckBox tahini;
    private EditText nameText;
    private EditText commentText;
    private EditText picklesText;
    private Order order;

    App app;
    FirebaseFirestore db;
    ListenerRegistration status;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_order_activity);

        app = new App();
//        FirebaseApp.initializeApp(this);

        db = FirebaseFirestore.getInstance();

        hummus = findViewById(R.id.editHummusCheckBox);
        tahini = findViewById(R.id.editTahiniCheckBox);
        nameText = findViewById(R.id.editNameText);
        commentText = findViewById(R.id.editCommentText);
        picklesText = findViewById(R.id.editPicklesText);
        picklesText.setFilters(new InputFilter[]{new InputFilterPickles(0, 10)});
        Button editPlaceOrderButton = findViewById(R.id.editPlaceOrderButton);
        Button deleteButton = findViewById(R.id.deleteOrderButton);

        Intent intent = getIntent();
        order = (Order) intent.getSerializableExtra("order");

        updateUI();

        picklesText.setOnClickListener(v -> picklesText.getText().clear());

        editPlaceOrderButton.setOnClickListener(v-> updateOrder());
        deleteButton.setOnClickListener(v-> deleteOrder());

        status = db.collection("orders").document(order.getId())
                .addSnapshotListener((val, error) -> {
                    if (val != null && val.exists()){
                        Order order = val.toObject(Order.class);
                        if (order.getStatus().equals("done")){
                            startNewOrderActivity();
                            return;
                        }
                        if (order.getStatus().equals("in progress")){
                            startActivityWithOrder(OrderInProgressActivity.class, order);
                            return;
                        }
                        if (order.getStatus().equals("ready")){
                            startActivityWithOrder(OrderIsReadyActivity.class, order);
                        }
                    }
                    else{
                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
                        sp.edit().putString("orderId", null).apply();
                        startActivity(new Intent(EditOrderActivity.this, MainActivity.class));
                        finish();
                    }
                }
                );

    }

    private void updateUI(){
        hummus.setChecked(order.isHummus());
        tahini.setChecked(order.isTahini());
        nameText.setText(order.getCustomerName());
        commentText.setText(order.getComment());
        picklesText.setText(String.valueOf(order.getPickles()));
    }

    private void updateOrder(){
        if (TextUtils.isEmpty(nameText.getText())){
            nameText.setError("Please enter your name");
            return;
        }
        if (picklesText.getText().toString().equals("")){
            picklesText.setText("0");
        }
        order.setHummus(this.hummus.isChecked());
        order.setTahini(this.tahini.isChecked());
        order.setPickles(Integer.parseInt(picklesText.getText().toString()));
        order.setCustomerName(this.nameText.getText().toString());
        order.setComment(this.commentText.getText().toString());

        db.collection("orders").document(order.getId()).update("customerName", nameText.getText().toString());
        db.collection("orders").document(order.getId()).update("hummus", hummus.isChecked());
        db.collection("orders").document(order.getId()).update("tahini", tahini.isChecked());
        db.collection("orders").document(order.getId()).update("pickles", Integer.parseInt(picklesText.getText().toString()));
        db.collection("orders").document(order.getId()).update("comment", commentText.getText().toString());

        Toast toast = Toast.makeText(this,"Your changes have been saved", Toast.LENGTH_SHORT);
        toast.show();
    }

    private void deleteOrder(){
        db.collection("orders").document(order.getId()).
                delete().addOnSuccessListener(u -> startNewOrderActivity()).
                addOnFailureListener(e -> {Toast.makeText(this, "Error deleting order:\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
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

