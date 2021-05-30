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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class GroupsActivity extends AppCompatActivity {

    private ArrayAdapter<String> groupsAdapter;
    private ListView listView;
    private Button button;
    private String filename;

    private ArrayList<String> skupine;
    private String skupineVsebina;
    private String[] temp;

    public static final String IME = "si.uni_lj.fe.tnuv.IME";
    public static final String ACT = "si.uni_lj.fe.tnuv.ACT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        //za datoteko
        filename = getResources().getString(R.string.datotekaSkupine);

        //skupine = preberiIzDatoteke(filename);
        skupineVsebina = preberiIzDatoteke(filename);
        //System.out.println(skupineVsebina);
        //temp = skupineVsebina.split(" ");
        //skupine = Arrays.asList(temp);
        skupine = new ArrayList<String>(Arrays.asList(skupineVsebina.split("\\s*,\\s*")));
        //List<String> skupine = Arrays.asList(skupineVsebina.split("\\s*,\\s*"));

        listView = findViewById(R.id.listView);
        System.out.println(skupine);
        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem(view);
            }
        });

        //groups = new ArrayList<>();
        groupsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, skupine);
        listView.setAdapter(groupsAdapter);
        setUpListViewListener();


        /*
        ArrayAdapter arrayAdapter =
                new ArrayAdapter(this, android.R.layout.simple_list_item_1, skupine);
        listView.setAdapter(arrayAdapter);
        setUpListViewListener();
        */
    }

    // private ArrayList<String> preberiIzDatoteke(String filename) {

    private String preberiIzDatoteke(String filename){

//            try {
//                Scanner myReader = new Scanner(filename);
//                while (myReader.hasNextLine()) {
//                    skupineVsebina = myReader.nextLine();
//
//                    System.out.println(skupineVsebina);
//
//                }
//                myReader.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            return skupineVsebina;

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

                String vs = preberiIzDatoteke(filename);
                System.out.println("Pred izbrisom: "+ vs);
                String izbris = ", "+ skupine.get(i);
                System.out.println(izbris);

                String novaVS = vs.replace(izbris, "");
                System.out.println("Po izbrisu: "+ novaVS);

//                 ZAPIS NOVEGA DOKUMENTA
                try {
                    //ustvarimo izhodni tok
                    FileOutputStream os = openFileOutput(filename, MODE_PRIVATE);
                    //zapisi vejico in presledek v datoteko
//                    String vejica = ", ";
//                    os.write(vejica.getBytes());
                    //zapisemo posredovano vsebino v datoteko
                    os.write(novaVS.getBytes());
                    //sprostimo izhodni tok
                    os.close();

                    skupine.remove(i);
                    groupsAdapter.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                System.out.println(novaVS);

                Toast.makeText(context, "Izbrisano", Toast.LENGTH_LONG).show();
                return true;
            }
        });

        // klik na ime skupine - vrže na skupino
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String imeSkupine = "file"+position+"-1.txt";
                System.out.println(imeSkupine);

                Intent intent = new Intent(GroupsActivity.this, ToDoListActivity.class);
                intent.putExtra("ime", imeSkupine);
                intent.putExtra("activity", "GroupsActivity");
                startActivity(intent);

                //spremeni barvo
//                Context context = getApplicationContext();
//                Toast.makeText(context, "Dodana nova skupina", Toast.LENGTH_LONG).show();

//                for (int i = 0; i < listView.getChildCount(); i++) {
//                    if(position == i ) {
//                        listView.getChildAt(i).setBackgroundColor(Color.rgb(255, 226,162));
//                    }
//                }

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
            intent.putExtra("ime", itemText);
            startActivity(intent);

            //klici shraniSkupino
            shraniSkupino(view, itemText);
        }
        else {
            Toast.makeText(getApplicationContext(), "Vpišite ime skupine", Toast.LENGTH_LONG).show();
        }

        System.out.println(skupine);
    }


    public void shraniSkupino(View view, String vsebina) {
        //shranim v datoteko
//
//        try {
//            BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true));
//            writer.append(",");
//            writer.append(vsebina);
//            writer.close();
//        } catch (Exception e) {
//           e.printStackTrace();
//       }


        try {
            //ustvarimo izhodni tok
            FileOutputStream os = openFileOutput(filename, Context.MODE_PRIVATE | Context.MODE_APPEND);
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
}