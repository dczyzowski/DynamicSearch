package pl.agh.dynamicsearch;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import pl.agh.dynamicsearch.adapters.MainListAdapter;
import pl.agh.dynamicsearch.models.Product;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase mDataBase;
    DatabaseReference myRef;
    boolean litleBlock = false;


    ArrayList<Product> products;
    public static ArrayList<Product> allProducts;
    ArrayAdapter<Product> adapter;
    ListView mainList;
    SearchView searchView;
    ArrayList<String> keysList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDataBase = FirebaseDatabase.getInstance();
        myRef = mDataBase.getReference("produkty");
        allProducts = new ArrayList<Product>();
        keysList = new ArrayList<>();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allProducts.clear();
                keysList.clear();
                for (DataSnapshot productsSnapshot : dataSnapshot.getChildren()){
                    Product product = productsSnapshot.getValue(Product.class);
                    keysList.add(productsSnapshot.getKey());
                    allProducts.add(product);
                }
                products.clear();
                String query = "";
                if (searchView != null){
                    query = searchView.getQuery().toString().toLowerCase();
                }
                for (Product i : allProducts) {
                    if (i.getmName().toLowerCase().contains(query))
                        products.add(i);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), AddProductActivity.class);
                startActivity(intent);
            }
        });

        mainList = (ListView) findViewById(R.id.list);
        allProducts = new ArrayList<Product>();

        products = new ArrayList<>(allProducts);

        adapter = new MainListAdapter(this, R.layout.result_cell, products);
        mainList.setAdapter(adapter);

        mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ProductActivity.class);
                intent.putExtra("", keysList.get(allProducts.indexOf(products.get(position))));
                if(!litleBlock)
                    startActivity(intent);
            }
        });

        mainList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                litleBlock = true;
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Usuń pozycję")
                        .setMessage("Czy chcesz usunąć wybraną pozycję?")
                        .setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                litleBlock = false;
                            }
                        })
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                litleBlock = false;
                                myRef.child(keysList.get(allProducts.indexOf(products.get(position)))).removeValue();
                            }
                        }).create();
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        litleBlock = false;
                    }
                });
                dialog.show();
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setIconifiedByDefault(false);

        updateTable();
        return true;
    }

    private void updateTable() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                products.clear();
                for (Product i : allProducts) {
                    if (i.getmName().toLowerCase().contains(newText.toLowerCase()))
                        products.add(i);
                }

                adapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
