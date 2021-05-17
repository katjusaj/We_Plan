package si.uni_lj.fe.tnuv.we_plan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class OpenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open);
    }

    public void prijaviUporabnika(View view){
        Intent intent = new Intent(OpenActivity.this, ToDoListActivity.class);
        startActivity(intent);
    }
}