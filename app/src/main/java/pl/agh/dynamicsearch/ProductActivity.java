package pl.agh.dynamicsearch;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleExpandableListAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.agh.dynamicsearch.models.Contains;
import pl.agh.dynamicsearch.models.Product;

public class ProductActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference myRef;
    ListView listView;
    HashMap<String, ArrayList<String>> contain;
    ArrayList<String> arrayList;
    ArrayAdapter<String> adapter;
    boolean toDelete = false;
    CollapsingToolbarLayout toolbarLayout;
    Product thisProduct;
    EditText editName;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        database = FirebaseDatabase.getInstance();
        listView = (ListView) findViewById(R.id.contains_list);
        contain = new HashMap<>();
        arrayList = new ArrayList<>();
        contain = new HashMap<>();
        editName = findViewById(R.id.edit_name);
        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        final Bundle extras = getIntent().getExtras();
        fab = (FloatingActionButton) findViewById(R.id.fab);

        if (extras == null && extras.getString("") == null) {
            finish();
        }
        myRef = database.getReference("produkty").child(extras.getString(""));

        adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (arrayList.get(position).equalsIgnoreCase("<Dodaj skład produktu>")) {
                    Intent i = new Intent(getApplicationContext(), AddProductActivity.class);
                    i.putExtra("key", extras.getString(""));
                    startActivity(i);
                } else {
                    if (toDelete) {
                        AlertDialog dialog = new AlertDialog.Builder(ProductActivity.this)
                                .setTitle("Usuń: " + arrayList.get(position))
                                .setMessage("Czy chcesz usunąć tą pozycję?")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        contain.remove(arrayList.get(position));
                                        arrayList.remove(position);
                                        adapter.notifyDataSetChanged();
                                    }
                                }).create();
                        dialog.show();

                    } else {
                        String message = "";
                        if (!contain.isEmpty()) {
                            for (String i : contain.get(arrayList.get(position))) {
                                message += i;
                                message += "\n";
                            }
                        }

                        AlertDialog dialog = new AlertDialog.Builder(ProductActivity.this)
                                .setTitle(arrayList.get(position))
                                .setMessage(message)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }).create();
                        dialog.show();
                    }
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!toDelete) {
                    toDelete = true;
                    fab.setImageResource(R.drawable.ic_check);
                    editName.setVisibility(View.VISIBLE);
                    editName.setEnabled(toDelete);
                    editName.setText(thisProduct.getmName());
                    toolbarLayout.setVisibility(View.GONE);
                    arrayList.add("<Dodaj skład produktu>");
                } else {
                    toDelete = false;
                    editName.setVisibility(View.GONE);
                    editName.setEnabled(toDelete);
                    fab.setImageResource(R.drawable.ic_edit);
                    toolbarLayout.setVisibility(View.VISIBLE);
                    thisProduct.setmName(editName.getText().toString());
                    myRef.setValue(thisProduct);
                    myRef.child("sklad").setValue(contain);
                    arrayList.remove("<Dodaj skład produktu>");
                }
                adapter.notifyDataSetChanged();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot productsSnapshot : dataSnapshot.child("sklad").getChildren()) {
                    arrayList.add(productsSnapshot.getKey());
                }
                contain = (HashMap<String, ArrayList<String>>)dataSnapshot.child("sklad").getValue();
                thisProduct = dataSnapshot.getValue(Product.class);
                adapter.notifyDataSetChanged();

                if(thisProduct != null) {
                    editName.setText(thisProduct.getmName());
                    toolbarLayout.setTitle(editName.getText().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {

        if (toDelete) {
            toDelete = false;
            editName.setVisibility(View.GONE);
            editName.setEnabled(toDelete);
            fab.setImageResource(R.drawable.ic_edit);
            toolbarLayout.setVisibility(View.VISIBLE);
            arrayList.remove("<Dodaj skład produktu>");
        }
        else
            super.onBackPressed();
    }
}
