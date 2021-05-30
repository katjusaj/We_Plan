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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class ShoppingActivity extends AppCompatActivity {

    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView listView;
    private Button button;
    private String shoppingList;

    private String imeSkupine;
    private String toDoList;

    public static final String IME3 = "si.uni_lj.fe.tnuv.IME3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);

        Intent intent = getIntent();
        imeSkupine = intent.getStringExtra("ime");
        toDoList = imeSkupine.substring(0, imeSkupine.length() - 5);
        toDoList = toDoList + "1.txt";

        shoppingList = preberiIzDatoteke(imeSkupine);

        items = new ArrayList<String>(Arrays.asList(shoppingList.split("\\s*,\\s*")));

        listView = findViewById(R.id.listViewS);
        button = findViewById(R.id.buttonS1);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem(view);
            }
        });

//        items = new ArrayList<>();
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(itemsAdapter);
        setUpListViewListener();
    }

    private String preberiIzDatoteke(String filename){

        // ustvarimo vhodni podatkovni tok
        FileInputStream inputStream;

        //ugotovimo, koliko je velika datoteka
        File file = new File(getFilesDir(), filename);
        int length = (int) file.length();

        //pripravimo spremenljivko, v katero se bodo prebrali podatki
        byte[] bytes = new byte[length];

        //preberemo podatke
        try {
            inputStream = openFileInput(filename);
            inputStream.read(bytes);
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //podatke pretvorimo iz polja bajtov v znakovni niz
        String vsebina = new String(bytes);
        System.out.println(vsebina);
        return vsebina;
    }

    private void setUpListViewListener() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int i, long l) {
                Context context = getApplicationContext();
                Toast.makeText(context, "Opravljeno", Toast.LENGTH_LONG).show();

                //              NOV DOKUMENT
                String vs = preberiIzDatoteke(imeSkupine);
                System.out.println("Pred izbrisom: "+ vs);
                String izbris = ", "+ items.get(i);
                System.out.println(izbris);

                String novaVS = vs.replace(izbris, "");
                System.out.println("Po izbrisu: "+ novaVS);

//                 ZAPIS NOVEGA DOKUMENTA
                try {
                    //ustvarimo izhodni tok
                    FileOutputStream os = openFileOutput(imeSkupine, MODE_PRIVATE);
                    //zapisi vejico in presledek v datoteko
//                    String vejica = ", ";
//                    os.write(vejica.getBytes());
                    //zapisemo posredovano vsebino v datoteko
                    os.write(novaVS.getBytes());
                    //sprostimo izhodni tok
                    os.close();

                    items.remove(i);
                    itemsAdapter.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                System.out.println(novaVS);
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Context context = getApplicationContext();
                Toast.makeText(context, "Označeno", Toast.LENGTH_LONG).show();

                for (int i = 0; i < listView.getChildCount(); i++) {
                    if(position == i ) {
                        listView.getChildAt(i).setBackgroundColor(Color.rgb(255, 226,162));
                    }
                }

            }
        });
    }

    private void addItem(View view) {
        EditText input = findViewById(R.id.editTextS2);
        EditText inputOseba = findViewById(R.id.editTextOsebaS2);
        String izpis = input.getText().toString() + "  |  " + inputOseba.getText().toString();
        String itemText = izpis;

        if(!(itemText.equals(""))){
            itemsAdapter.add(itemText);
            input.setText("");
            inputOseba.setText("");
            shraniItem(izpis);
        }
        else {
            Toast.makeText(getApplicationContext(), "Vpišite izdelek", Toast.LENGTH_LONG).show();
        }
    }

    public void shraniItem(String vsebina) {
        //shranim v datoteko

        try {
            //ustvarimo izhodni tok
            FileOutputStream os = openFileOutput(imeSkupine, Context.MODE_PRIVATE | Context.MODE_APPEND);
            //zapisi vejico in presledek v datoteko
            String vejica = ", ";
            os.write(vejica.getBytes());
            //zapisemo posredovano vsebino v datoteko
            os.write(vsebina.getBytes());
            //sprostimo izhodni tok
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(vsebina);
    }

    public void naToDo(View view){
        Intent intent2 = new Intent(ShoppingActivity.this, ToDoListActivity.class);
        intent2.putExtra("ime", toDoList);
        intent2.putExtra("activity", "ShoppingActivity");
        startActivity(intent2);
    }

}