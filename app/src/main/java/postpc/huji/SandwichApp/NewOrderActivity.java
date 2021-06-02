package postpc.huji.SandwichApp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class NewOrderActivity extends AppCompatActivity {

    CheckBox hummus;
    CheckBox tahini;
    EditText nameText;
    EditText commentText;
    EditText picklesText;
    Order order;
    Button placeOrderButton;

    App app;
    FirebaseFirestore db;

    private SharedPreferences sp;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_order_activity);

        app = new App();
        db = FirebaseFirestore.getInstance();

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        hummus = findViewById(R.id.hummusCheckBox);
        tahini = findViewById(R.id.tahiniCheckBox);
        nameText = findViewById(R.id.nameText);
        commentText = findViewById(R.id.commentText);
        picklesText = findViewById(R.id.picklesText);
        picklesText.setFilters(new InputFilter[]{new InputFilterPickles(0, 10)});
        placeOrderButton = findViewById(R.id.placeOrderButton);

        order = new Order();

        placeOrderButton.setOnClickListener(v-> placeOrder());
    }

    private void placeOrder(){
        if (TextUtils.isEmpty(nameText.getText())){
            nameText.setError("Please enter your name");
            return;
        }
        if (picklesText.getText().toString().equals("")){
            picklesText.setText("0");
        }
        order.setHummus(hummus.isChecked());
        order.setTahini(tahini.isChecked());
        order.setPickles(Integer.parseInt(picklesText.getText().toString()));
        order.setCustomerName(nameText.getText().toString());
        order.setComment(commentText.getText().toString());

        db.collection("orders").document(order.getId()).set(order).addOnSuccessListener(
                documentReference -> {
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                    preferences.edit().putString("orderId", order.getId()).apply();
                    Intent intent = new Intent(this, EditOrderActivity.class);
                    intent.putExtra("order", order);
                    startActivity(intent);
                    finish();
                }
        ).addOnFailureListener(e-> {
            Toast.makeText(this, "Error creating order:\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

}

