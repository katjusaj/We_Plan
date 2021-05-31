package si.uni_lj.fe.tnuv.we_plan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
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

public class CostActivity extends AppCompatActivity {

    private ArrayList<String> stroski;
    private ArrayAdapter<String> stroskiAdapter;
    private ListView listView;
    private Button button;
    private String stroskiList;

    private String imeSkupine;
    private String nakupovalniSeznam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cost);

        Intent intent = getIntent();
        imeSkupine = intent.getStringExtra("ime");
        nakupovalniSeznam = imeSkupine.substring(0, imeSkupine.length() - 5);
        nakupovalniSeznam = nakupovalniSeznam + "2.txt";

        stroskiList = preberiIzDatoteke(imeSkupine);

        stroski = new ArrayList<String>(Arrays.asList(stroskiList.split("\\s*,\\s*")));

        listView = findViewById(R.id.listView);
        button = findViewById(R.id.button6);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem(view);
            }
        });

        stroskiAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, stroski);
        listView.setAdapter(stroskiAdapter);
        setUpListViewListener();
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
                String izbris = ", "+ stroski.get(i);
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

                    stroski.remove(i);
                    stroskiAdapter.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                System.out.println(novaVS);
                return true;
            }
        });
    }


    private void addItem(View view) {
        EditText inputOseba = findViewById(R.id.editTextTextPersonName);
        EditText input = findViewById(R.id.editTextTextPersonName2);
        String izpis = inputOseba.getText().toString() + ": " + input.getText().toString() + "€";
        String itemText = izpis;

        String cena = input.getText().toString();
        String oseba = inputOseba.getText().toString();

        if(!(itemText.equals(""))){
            stroskiAdapter.add(itemText);

            shraniStroske(cena, oseba);
            input.setText("");
            inputOseba.setText("");
        }
        else {
            Toast.makeText(getApplicationContext(), "Vpišite ceno", Toast.LENGTH_LONG).show();
        }
    }

    public void shraniStroske(String cena, String oseba){
        String vsebina = preberiIzDatoteke(imeSkupine);
        String novaVsebina;

        System.out.println(vsebina);

        if(vsebina.contains(oseba)) {
            System.out.println("Obstaja");
            // najdem osebo v datoteki, zamenjam ceno z novo ceno ali pa celo replacam z novo
            int stringIndex = vsebina.indexOf(oseba);
            System.out.println(stringIndex);
            int stringLength = oseba.length();
            System.out.println(stringLength);

            char euro = '€';
            int i = stringIndex+stringLength+2;
            int iZadnji = 0;
            int cenaStara = 0;
            char prebrani = '1';
            while(vsebina.charAt(i) != euro) {
                if(vsebina.charAt(i) == euro) {
                    iZadnji = i;
                    break;
                }
                prebrani = vsebina.charAt(i);
                System.out.println(prebrani);
                cenaStara = cenaStara*10 + Character.getNumericValue(prebrani);
                i++;
            }
            System.out.println(cenaStara);

            int cenaNova = cenaStara + Integer.parseInt(cena);
            System.out.println(cenaNova);

            String odstrani = ", "+oseba + ": "+ String.valueOf(cenaStara)+"€";
            String nov = ", "+oseba + ": " + String.valueOf(cenaNova)+"€";

            System.out.println(odstrani);
            System.out.println(nov);

            novaVsebina = vsebina.replace(odstrani, nov);
            System.out.println(novaVsebina);

            try {
                //ustvarimo izhodni tok
                FileOutputStream os = openFileOutput(imeSkupine, MODE_PRIVATE);
                os.write(novaVsebina.getBytes());
                //sprostimo izhodni tok
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }


        } else {
            String novZapis = oseba + ": " + cena + "€";
            try {
                //ustvarimo izhodni tok
                FileOutputStream os = openFileOutput(imeSkupine, Context.MODE_PRIVATE | Context.MODE_APPEND);
                //zapisi vejico in presledek v datoteko
                String vejica = ", ";
                os.write(vejica.getBytes());
                //zapisemo posredovano vsebino v datoteko
                os.write(novZapis.getBytes());
                //sprostimo izhodni tok
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

//        int novaCena = Integer.parseInt(cena);
//        int stringIndex = vsebina.indexOf(oseba);
//        int stringDolzina = oseba.length() + 2;
//        int dodatek = 1;

//        char cenaS = vsebina.charAt(stringIndex+stringDolzina+1);
//        int staraCena = Character.getNumericValue(cenaS);
//
//        if (vsebina.charAt(stringIndex+stringDolzina+2) != ',') dodatek = 2;
//        String odstrani = vsebina.substring(stringIndex, stringIndex + stringDolzina + dodatek);
//
//
//        if(vsebina.contains(oseba)) {
//            int izpisCena = novaCena + staraCena;
//            String nov = oseba + "  |  " + izpisCena;
//            vsebina.replace(odstrani, nov);
//
//            try {
//                //ustvarimo izhodni tok
//                FileOutputStream os = openFileOutput(imeSkupine, MODE_PRIVATE);
//                os.write(vsebina.getBytes());
//                //sprostimo izhodni tok
//                os.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        } else {
//            String nov = oseba + "  |  " + cena;
//            try {
//                //ustvarimo izhodni tok
//                FileOutputStream os = openFileOutput(imeSkupine, Context.MODE_PRIVATE | Context.MODE_APPEND);
//                //zapisi vejico in presledek v datoteko
//                String vejica = ", ";
//                os.write(vejica.getBytes());
//                //zapisemo posredovano vsebino v datoteko
//                os.write(nov.getBytes());
//                //sprostimo izhodni tok
//                os.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
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

    private String preberiIzDatoteke(String imeSkupine) {
        // ustvarimo vhodni podatkovni tok
        FileInputStream inputStream;

        //ugotovimo, koliko je velika datoteka
        File file = new File(getFilesDir(), imeSkupine);
        int length = (int) file.length();

        //pripravimo spremenljivko, v katero se bodo prebrali podatki
        byte[] bytes = new byte[length];

        //preberemo podatke
        try {
            inputStream = openFileInput(imeSkupine);
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

    public void naNakSeznam(View view) {
        Intent intent = new Intent(CostActivity.this, ShoppingActivity.class);
        intent.putExtra("ime", "" + nakupovalniSeznam);
        startActivity(intent);
    }

}