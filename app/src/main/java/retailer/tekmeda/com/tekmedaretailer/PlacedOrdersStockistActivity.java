package retailer.tekmeda.com.tekmedaretailer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import retailer.tekmeda.com.tekmedaretailer.bean.Connections;
import retailer.tekmeda.com.tekmedaretailer.bean.Stockists;
import retailer.tekmeda.com.tekmedaretailer.util.AcceptedStockistListAdapter;
import retailer.tekmeda.com.tekmedaretailer.util.PlacedOrderStockistAdapter;

public class PlacedOrdersStockistActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private List<Stockists> stockistsList;
    private PlacedOrderStockistAdapter placedOrderStockistAdapter;
    private ListView listView;
    private FirebaseUser user;
    private ProgressDialog progressDialog;
    private ArrayList<String> connectedStockistsIds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placed_orders_stockist);
        listView = (ListView) findViewById(R.id.lvStockistNamesPlaced);
        user= FirebaseAuth.getInstance().getCurrentUser();
        progressDialog = new ProgressDialog(PlacedOrdersStockistActivity.this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Fetching Data");
        progressDialog.show();
        connectedStockistsIds= new ArrayList<>();
        stockistsList= new ArrayList<>();

        loadStockists();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String stockistId = stockistsList.get(i).getId();
                Intent intent = new Intent(getApplicationContext(),PlacedOrderActivity.class);
                intent.putExtra("stockistId",stockistId);
                startActivity(intent);
            }
        });
    }

    private void loadStockists(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Connections").child("Retailers").child(user.getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    Connections connections = ds.getValue(Connections.class);
                    if(connections.getConnectionStatus().equalsIgnoreCase("2"))
                    {
                        connectedStockistsIds.add(connections.getStockistId());
                    }
                }
                Log.d("connected Stockist ids",connectedStockistsIds.toString());

                fetchStockistDetails();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void fetchStockistDetails(){
        Log.d(" fetch Stockist ids",connectedStockistsIds.toString());
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Stockists");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    Stockists stockists =  ds.getValue(Stockists.class);
                    if(connectedStockistsIds.contains(stockists.getId()))
                    {
                        stockistsList.add(stockists);
                    }
                }
                // Log.d(" fetch Stockist detsils",stockistLists.toString());
                placedOrderStockistAdapter = new PlacedOrderStockistAdapter(getApplicationContext(),stockistsList);
                listView.setAdapter(placedOrderStockistAdapter);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
