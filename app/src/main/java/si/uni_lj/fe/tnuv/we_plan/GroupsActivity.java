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

public class GroupsActivity extends AppCompatActivity {

    private ArrayList<String> groups;
    private ArrayAdapter<String> groupsAdapter;
    private ListView listView;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        listView = findViewById(R.id.listView);
        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem(view);
            }
        });

        groups = new ArrayList<>();
        groupsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, groups);
        listView.setAdapter(groupsAdapter);
        setUpListViewListener();
    }

    private void setUpListViewListener() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int i, long l) {
                Context context = getApplicationContext();
                Toast.makeText(context, "Izbrisano", Toast.LENGTH_LONG).show();

                groups.remove(i);
                groupsAdapter.notifyDataSetChanged();
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Context context = getApplicationContext();
                Toast.makeText(context, "Dodana nova skupina", Toast.LENGTH_LONG).show();

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
//        EditText inputOseba = findViewById(R.id.editTextOseba);
//        String izpis = input.getText().toString() + "  |  " + inputOseba.getText().toString();
//        String itemText = izpis;
        String itemText = input.getText().toString();

        if(!(itemText.equals(""))){
            groupsAdapter.add(itemText);
            input.setText("");
//            inputOseba.setText("");
            Intent intent = new Intent(GroupsActivity.this, ToDoListActivity.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(getApplicationContext(), "Vpišite ime skupine", Toast.LENGTH_LONG).show();
        }

    }


}