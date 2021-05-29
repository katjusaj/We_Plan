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
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ToDoListActivity extends AppCompatActivity {


    public static final String IME2 = "ime";
    private ArrayAdapter<String> groupsAdapter;
    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView listView;
    private Button button;
    private String toDoList;
    private ArrayList<String> opravila;

    private String imeSkupine;
    private String nakupovalniSeznam;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);

        Intent intent = getIntent();
        imeSkupine = intent.getStringExtra(GroupsActivity.IME);
        nakupovalniSeznam = imeSkupine.substring(0, imeSkupine.length() - 5);
        nakupovalniSeznam = nakupovalniSeznam + "2.txt";
        System.out.println(nakupovalniSeznam);

        toDoList = preberiIzDatoteke(imeSkupine);

        opravila = new ArrayList<String>(Arrays.asList(toDoList.split("\\s*,\\s*")));


        listView = findViewById(R.id.listView);
        System.out.println(opravila);
        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem(view);
            }
        });

        //groups = new ArrayList<>();
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, opravila);
        listView.setAdapter(itemsAdapter);
        setUpListViewListener();


//        listView = findViewById(R.id.listView);
//        button = findViewById(R.id.button);
//
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                addItem(view);
//            }
//        });
//
//        items = new ArrayList<>();
//        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
//        listView.setAdapter(itemsAdapter);
//        setUpListViewListener();
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
                Toast.makeText(context, "Izbrisano", Toast.LENGTH_LONG).show();

//                items.remove(i);
//                itemsAdapter.notifyDataSetChanged();

//                izbris podatka
//                String b = items.get(i);
//
//                String kopija = toDoList;
//                kopija.replace(b, "");
//                byte[] izbris = kopija.getBytes();
//                System.out.println(kopija);
//
//                try {
//                    FileOutputStream os = openFileOutput(imeSkupine, Context.MODE_PRIVATE);
//                    os.write(izbris);
//                    os.close();
//                    System.out.println("Sesuje se");
////                    finish();
////                    startActivity(getIntent());
//
//                } catch (IOException e) {
//                    System.out.println("Ne dela");
//                    e.printStackTrace();
//                }


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

            shraniItem(izpis);
//            finish();
//            startActivity(getIntent());
        }
        else {
            Toast.makeText(getApplicationContext(), "Vpišite zadolžitev", Toast.LENGTH_LONG).show();
        }
        System.out.println(opravila);
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

    public void naNakupovalniSeznam(View view){

        Intent intent = new Intent(ToDoListActivity.this, ShoppingActivity.class);
        intent.putExtra(IME2, nakupovalniSeznam);
        startActivity(intent);
    }
}