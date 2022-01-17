package com.example.currency;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.jjoe64.graphview.series.Series;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public int point = 0;
    Button closePopupBtn;
    PopupWindow popupWindow;


    FirebaseStorage fStorage;
    FirebaseFirestore fstore;
    StorageReference srefer;

    String[] cur1 = { "Taka"};
    String[] cur = {"Euro", "Dollar", "Rupee", "Yen", "Pound"};
    GraphView graphView;

    private RecyclerView courseVR;

    private ArrayList<countrylist> coursesrrayList;
    private countrylist_adapter courseRVdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fstore = FirebaseFirestore.getInstance();
        fStorage = FirebaseStorage.getInstance();
        srefer = fStorage.getReference();

        loadcountry();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navigation_more) {
                    Intent i = new Intent(MainActivity.this, more.class);
                    startActivity(i);
                    return true;
                }
                return false;
            }
        });

        graphView = findViewById(R.id.chart);
        LinearLayout gtt = findViewById(R.id.gtt);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(0, 1),
                new DataPoint(1, 3),
                new DataPoint(2, 4),
                new DataPoint(3, 9),
                new DataPoint(4, 6),
                new DataPoint(5, 3),
                new DataPoint(6, 6),
                new DataPoint(7, 1),
                new DataPoint(8, 12)
        });


        series.setColor(R.color.black);
        series.setDrawBackground(true);
        series.setBackgroundColor(getResources().getColor(R.color.grey));
        graphView.addSeries(series);


        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMaxX(15);
        graphView.getViewport().setMinY(0);
        graphView.getViewport().setMaxY(15);

        graphView.getViewport().setMaxXAxisSize(1);
        graphView.getViewport().setMaxXAxisSize(1);
        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getViewport().setXAxisBoundsManual(true);

        graphView.getGridLabelRenderer().setVerticalAxisTitle("Cap");
        graphView.getGridLabelRenderer().setHorizontalAxisTitle("Month");


        PointsGraphSeries<DataPoint> seriesq = new PointsGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(0, 1),
                new DataPoint(1, 3),
                new DataPoint(2, 4),
                new DataPoint(3, 9),
                new DataPoint(4, 6),
                new DataPoint(5, 3),
                new DataPoint(6, 6),
                new DataPoint(7, 1),
                new DataPoint(8, 12)
        });
        graphView.addSeries(seriesq);
        seriesq.setSize(5);
        seriesq.setColor(R.color.black);

        seriesq.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                LayoutInflater layoutInflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View customView = layoutInflater.inflate(R.layout.popup,null);

                TextView gx = findViewById(R.id.gx);
                TextView gc = findViewById(R.id.gc);
                closePopupBtn = (Button) customView.findViewById(R.id.closePopupBtn);
                popupWindow = new PopupWindow(customView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                popupWindow.showAsDropDown(graphView,(int) dataPoint.getX(), (int) dataPoint.getY());


                gx.setText((int) dataPoint.getX());
                gc.setText((int) dataPoint.getY());
                closePopupBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
            }
        });
        Spinner spin1 = findViewById(R.id.sp1);
        spin1.setOnItemSelectedListener(this);


        Spinner spin2 = findViewById(R.id.sp2);
        spin2.setOnItemSelectedListener(this);


        ArrayAdapter<String> ad1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cur1);
        ArrayAdapter<String> ad = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cur);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin1.setAdapter(ad1);
        spin2.setAdapter(ad);


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        FrameLayout flout = (FrameLayout) findViewById(R.id.converter);
            flout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    EditText ctwo = (EditText) findViewById(R.id.ctwo);
                    EditText cone = (EditText) findViewById(R.id.cone);
                    double result = 00.00;
                    String p;
                    p = cone.getText().toString();
                    double amount = Double.parseDouble(p);
                    if(point == 1){
                        result = (double) (amount * 0.01000);
                        String stringdouble= Double.toString(result);
                        ctwo.setText(stringdouble);
                    }else if(point == 2){
                        result = (int) (amount * 0.01200);
                        String stringdouble= Double.toString(result);
                        ctwo.setText(stringdouble);
                    }else if(point == 3){
                        result = (int) (amount * 0.87000);
                        String stringdouble= Double.toString(result);
                        ctwo.setText(stringdouble);
                    }else if(point == 4){
                        result = (int) (amount * 1.34000);
                        String stringdouble= Double.toString(result);
                        ctwo.setText(stringdouble);
                    }else if(point == 5){
                        result = (int) (amount * 0.00880);
                        String stringdouble= Double.toString(result);
                        ctwo.setText(stringdouble);
                    }
                }
            });




            courseVR =findViewById(R.id.rview);
        coursesrrayList = new ArrayList<>();
        courseVR.setHasFixedSize(true);
        courseVR.setLayoutManager(new LinearLayoutManager(this));
        courseRVdapter = new countrylist_adapter(coursesrrayList, this);
        courseVR.setAdapter(courseRVdapter);


    }

    private void loadcountry() {
        fstore.collection("country").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d : list) {
                        countrylist c = d.toObject(countrylist.class);
                        coursesrrayList.add(c);
                    }
                    courseRVdapter.notifyDataSetChanged();
                }
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id){
        if(position == 0){
            point = 1;
        } else if(position == 1){
            point = 2;
        }else if(position == 2){
            point = 3;
        }else if(position == 3){
            point = 4;
        }else if(position == 4){
            point = 5;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}