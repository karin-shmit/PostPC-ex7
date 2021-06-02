package postpc.huji.SandwichApp;

import android.content.Context;
import android.content.Intent;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)

public class appTests {
    private ActivityController<MainActivity> activityController;
    Order order = new Order();

    @Before
    public void setup() {
        activityController = Robolectric.buildActivity(MainActivity.class);
    }

    @Test
    public void insertToDB_Test()
    {
        String id = order.getId();
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        FirebaseApp.initializeApp(appContext);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("orders").document(id).set(order);
        db.collection("orders").document(id).get().addOnSuccessListener(documentSnapshot -> {
            Order order = documentSnapshot.toObject(Order.class);
            Assert.assertEquals("", documentSnapshot.getString("name"));
            Assert.assertFalse(documentSnapshot.getBoolean("hummus"));
            Assert.assertFalse(documentSnapshot.getBoolean("tahini"));
            Assert.assertEquals(0, documentSnapshot.get("pickles"));
            Assert.assertEquals("", documentSnapshot.getString("comment"));
        }).addOnFailureListener(documentSnapshot -> {
            Assert.assertTrue(false);
        });
    }

    @Test
    public void updateDB_Test()
    {
        order.setHummus(true);
        order.setPickles(5);
        order.setCustomerName("Karin");
        order.setComment("Test");

        String id = order.getId();
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        FirebaseApp.initializeApp(appContext);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("orders").document(id).update("hummus", order.isHummus());
        db.collection("orders").document(id).update("comment", order.getComment());
        db.collection("orders").document(id).update("pickles", order.getPickles());
        db.collection("orders").document(id).update("customerName", order.getCustomerName());
        db.collection("orders").document(id).get().addOnSuccessListener(documentSnapshot -> {
            Assert.assertEquals(order.getCustomerName(), documentSnapshot.getString("customerName"));
            Assert.assertEquals("true", documentSnapshot.getString("hummus"));
            Assert.assertEquals(order.getComment(), documentSnapshot.getString("comment"));
            Assert.assertEquals(order.getPickles(), Integer.parseInt(documentSnapshot.getString("pickles")));
        }).addOnFailureListener(documentSnapshot -> {
            Assert.assertTrue(false);
        });
    }

    @Test
    public void sendOrderWitnIntent_Test() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        FirebaseApp.initializeApp(appContext);
        order.setCustomerName("Karin");
        order.setHummus(true);
        order.setPickles(5);
        order.setComment("Test");

        Intent intent = new Intent();
        intent.putExtra("order", order);
        EditOrderActivity activityUnderTest = Robolectric.buildActivity(EditOrderActivity.class, intent).create().visible().get();

        Assert.assertEquals(order.getCustomerName() ,((EditText) activityUnderTest.findViewById(R.id.editNameText)).getText().toString());
        Assert.assertEquals(order.isHummus() ,((CheckBox) activityUnderTest.findViewById(R.id.editHummusCheckBox)).isChecked());
        Assert.assertEquals(order.isTahini() ,((CheckBox) activityUnderTest.findViewById(R.id.editTahiniCheckBox)).isChecked());
        Assert.assertEquals(String.valueOf(order.getPickles()) ,((TextView) activityUnderTest.findViewById(R.id.editPicklesText)).getText().toString());
        Assert.assertEquals(order.getComment() ,((EditText) activityUnderTest.findViewById(R.id.editCommentText)).getText().toString());
    }



}
