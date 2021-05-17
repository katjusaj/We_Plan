package si.uni_lj.fe.tnuv.we_plan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ToDoListActivity extends AppCompatActivity {

    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView listView;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);

        listView = findViewById(R.id.listView);
        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem(view);
            }
        });

        items = new ArrayList<>();
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(itemsAdapter);
        setUpListViewListener();
    }

    private void setUpListViewListener() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int i, long l) {
                Context context = getApplicationContext();
                Toast.makeText(context, "Izbrisano", Toast.LENGTH_LONG).show();

                items.remove(i);
                itemsAdapter.notifyDataSetChanged();
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Context context = getApplicationContext();
                Toast.makeText(context, "Opravljeno", Toast.LENGTH_LONG).show();

                for (int i = 0; i < listView.getChildCount(); i++) {
                    if(position == i ) {
                        listView.getChildAt(i).setBackgroundColor(Color.rgb(255, 226,162));
                    }
                }

        }
        });
    }


    private void addItem(View view) {
        EditText input = findViewById(R.id.editText2);
        EditText inputOseba = findViewById(R.id.editTextOseba);
        String izpis = input.getText().toString() + "  |  " + inputOseba.getText().toString();
        String itemText = izpis;

        if(!(itemText.equals(""))){
            itemsAdapter.add(itemText);
            input.setText("");
            inputOseba.setText("");
        }
        else {
            Toast.makeText(getApplicationContext(), "Vpišite zadolžitev", Toast.LENGTH_LONG).show();
        }
    }

    public void naNakupovalniSeznam(View view){
        Intent intent = new Intent(ToDoListActivity.this, ShoppingActivity.class);
        startActivity(intent);
    }
}