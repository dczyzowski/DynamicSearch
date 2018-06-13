package pl.agh.dynamicsearch;

import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pl.agh.dynamicsearch.models.Contains;
import pl.agh.dynamicsearch.models.Product;

public class AddProductActivity extends AppCompatActivity {

    EditText productName;
    Spinner typeSpinner;
    EditText priceEdit;
    ListView checkList;

    Button mAddButton;
    Button mCancelButton;

    Product temp_product;
    Map<String, ArrayList<String>> temp_map;

    FirebaseDatabase database;
    DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        setTitle(getString(R.string.add_product));

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("produkty");
        temp_product = new Product();

        // deklaruje obiekty
        productName = (EditText) findViewById(R.id.product_name);
        typeSpinner = (Spinner) findViewById(R.id.type_spinner);
        priceEdit = (EditText) findViewById(R.id.price_edit);
        checkList = (ListView) findViewById(R.id.check_list);

        mAddButton = (Button) findViewById(R.id.button_add);
        mCancelButton = (Button) findViewById(R.id.button_cancel);

        temp_map = new HashMap<>();
        //getString pobiera z values/strings poszczegolne wyrazy
        final ArrayList<String> types = new Contains(getApplicationContext()).getProductTypes();
        final Bundle extras = getIntent().getExtras();


        // adapter generuje liste elementow w spinnerze
        typeSpinner.setAdapter(new ArrayAdapter<>(getBaseContext(),
                android.R.layout.simple_spinner_item, types));

        // elementy ktore wyswietla sie w spinnerze z typem zajec
        final ArrayList<String> contains = new Contains(getApplicationContext()).getContainsGroup();

        // generuje liste elementow w spinerze
        checkList.setAdapter(new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_list_item_1, contains));

        if (extras != null && extras.getString("key") != null) {
            myRef = database.getReference("produkty").child(extras.getString("key"));

            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    temp_map = (HashMap<String, ArrayList<String>>) dataSnapshot.child("sklad").getValue();
                    temp_product = dataSnapshot.getValue(Product.class);

                    if (temp_product != null) {
                        productName.setText(temp_product.getmName());
                        priceEdit.setText(String.valueOf(temp_product.getmPrice()));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    finish();
                }
            });
        } else
            myRef = database.getReference("produkty").push();

        //dodaje nowy obiekt do bazy danych
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean ok = true;

                if (productName.getText().toString().isEmpty()) {
                    productName.setError(getString(R.string.fill_this));
                    ok = false;
                }
                if (priceEdit.getText().toString().isEmpty()) {
                    priceEdit.setError(getString(R.string.fill_this));
                    ok = false;
                }
                if (temp_map.isEmpty()) {
                    final boolean finalOk = ok;
                    AlertDialog dialog = new AlertDialog.Builder(AddProductActivity.this)
                            .setMessage(getString(R.string.message_info))
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (finalOk) {
                                        temp_product.setmName(productName.getText().toString());
                                        temp_product.setmPrice(Double.valueOf(priceEdit.getText().toString()));
                                        myRef.setValue(temp_product);
                                        myRef.child("sklad").setValue(temp_map);
                                        finish();
                                    }
                                }
                            }).setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).create();
                    dialog.show();
                    ok = false;
                }
                if (ok) {
                    temp_product.setmName(productName.getText().toString());
                    temp_product.setmPrice(Double.valueOf(priceEdit.getText().toString()));
                    myRef.setValue(temp_product);
                    myRef.child("sklad").setValue(temp_map);

                    finish();
                }
            }
        });

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (temp_product != null)
                    temp_product.setmType(types.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // nie dodaje nowego obiektu do mojej bazy danych
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        checkList.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final ArrayList<String> toCheck = new Contains(getApplicationContext()).getContains(position);
                final boolean[] tab = new boolean[toCheck.size()];
                ArrayList<String> temp_list;
                if (temp_map != null)
                    temp_list = temp_map.get(contains.get(position));
                else temp_list = new ArrayList<>();

                if (temp_list != null) {
                    for (String i : temp_list) {
                        tab[toCheck.indexOf(i)] = true;
                    }
                }

                CharSequence[] cs = toCheck.toArray(new CharSequence[toCheck.size()]);
                AlertDialog dialog = new AlertDialog.Builder(AddProductActivity.this).setMultiChoiceItems(cs, tab, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        tab[which] = isChecked;
                    }
                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        ArrayList<String> temp = new ArrayList<>();

                        for (int i = 0; i < tab.length; i++) {
                            if (tab[i]) {
                                temp.add(toCheck.get(i));
                            }
                        }

                        if (temp_map == null)
                            temp_map = new HashMap<>();

                        temp_map.put(contains.get(position), temp);

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //  Your code when user clicked on Cancel
                    }
                }).create();
                dialog.show();
            }
        });
    }
}
