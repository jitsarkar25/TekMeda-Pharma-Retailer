package retailer.tekmeda.com.tekmedaretailer;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import retailer.tekmeda.com.tekmedaretailer.bean.Orders;
import retailer.tekmeda.com.tekmedaretailer.util.AcceptedOrdersAdapter;
import retailer.tekmeda.com.tekmedaretailer.util.OrdersAdapter;

public class OrdersOnDeliveryDetailsActivity extends AppCompatActivity {

    private String stockistId = "";
    private ListView listView;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    public static List<Orders> ordersList;
    private AcceptedOrdersAdapter ordersAdapter;
    private boolean showOrderNumber = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_on_delivery_details);
        listView = (ListView) findViewById(R.id.lvMedsAccepted);
        stockistId=getIntent().getStringExtra("stockistId");
        user = FirebaseAuth.getInstance().getCurrentUser();
        ordersList =new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("AcceptedOrders").child("Retailers").child(user.getUid()).child(stockistId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    showOrderNumber=true;
                    for(DataSnapshot dss:ds.getChildren()) {
                        Orders orders = dss.getValue(Orders.class);
                        if(showOrderNumber==true)
                        {
                            orders.setShowOrderNumber(true);
                            showOrderNumber=false;
                        }
                        else
                            orders.setShowOrderNumber(false);

                        ordersList.add(orders);
                    }
                }
                ordersAdapter= new AcceptedOrdersAdapter(OrdersOnDeliveryDetailsActivity.this,ordersList);
                listView.setAdapter(ordersAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
