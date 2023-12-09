package com.projeto2.env_station_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.navigation.NavigationView;
import com.projeto2.env_station_app.Model.DAO.StationDAO;
import com.projeto2.env_station_app.Model.User;
import com.projeto2.env_station_app.Model.DAO.UserDAO;
import com.projeto2.env_station_app.Session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Dashboard extends AppCompatActivity {
    private User user;
    User loggedInUser;
    String hello_name, user_id;
    List<String> station;

    LineChart lineChart, mqChart;
    TextView tv_hour_difference, tv_last_temperature, tv_last_humidity, tv_last_mq135;
    TextView tv_hello_user, tv_temperature_evaluation, tv_humidity_evaluation, tv_light_evaluation, tv_air_evaluation;
    SwipeRefreshLayout swipeRefreshLayout;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    String Errorresponse;
    String inputFormat = "yyyy-MM-dd HH:mm:ss.SSSSSS";
    String outputFormat = "HH:mm";
    SessionManager sessionManager;

    SimpleDateFormat inputDateFormat = new SimpleDateFormat(inputFormat);
    SimpleDateFormat outputDateFormat = new SimpleDateFormat(outputFormat);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        sessionManager = new SessionManager(getApplicationContext());
        loggedInUser = sessionManager.checkLogin();

        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();

        layoutParams.systemUiVisibility |= View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        window.setAttributes(layoutParams);
        user = (User) getIntent().getSerializableExtra("user");

        String id, name;
        String [] device = {"0"};
        if (user != null) {
            user_id = user.getId();
            hello_name = user.getName();
            device = user.getDevice();
        } else if (loggedInUser != null){
            user_id = loggedInUser.getId();
            UserDAO userDAO = new UserDAO();
            userDAO.get_user(Dashboard.this, user_id, new UserDAO.VolleyCallback() {
                @Override
                public void onSuccess(String response) throws JSONException {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String name = jsonObject.getString("name");
                            JSONArray stationArray = jsonObject.getJSONArray("station");
                            station = new ArrayList<>();
                            for (int j = 0; j < stationArray.length(); j++) {
                                station.add(stationArray.getString(j));
                            }
                            hello_name = name;
                            update_name(hello_name);
                            if (station.size() > 0){
                                load_data(station.get(0));
                            }
                            else{
                                settings();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onError(String error) {
                }
            });
        } else {
            System.out.println("User object is empty or not provided.");
        }

        drawerLayout = findViewById(R.id.my_drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mqChart = findViewById(R.id.mqvalues_chart);
        lineChart = findViewById(R.id.line_chart);
        tv_hour_difference = findViewById(R.id.tv_hour_difference);
        tv_last_temperature = findViewById(R.id.tv_last_temperature);
        tv_last_humidity = findViewById(R.id.tv_last_humidity);
        tv_last_mq135 = findViewById(R.id.tv_last_mq135);
        tv_hello_user = findViewById(R.id.tv_hello_user);
        tv_temperature_evaluation = findViewById(R.id.tv_temperature_evaluation);
        tv_humidity_evaluation = findViewById(R.id.tv_humidity_evaluation);
        tv_light_evaluation = findViewById(R.id.tv_light_evaluation);
        tv_air_evaluation = findViewById(R.id.tv_air_evaluation);

        update_name(hello_name);

        if (device[0] != null && device[0].equals("0")){
            settings();
        }
        else{
            load_data(device[0]);
        }

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                    UserDAO userDAO = new UserDAO();
                    userDAO.get_user(Dashboard.this, user_id, new UserDAO.VolleyCallback() {
                        @Override
                        public void onSuccess(String response) throws JSONException {
                            // Handle success and response code
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String name = jsonObject.getString("name");
                                    JSONArray stationArray = jsonObject.getJSONArray("station");
                                    List<String> station = new ArrayList<>();
                                    for (int j = 0; j < stationArray.length(); j++) {
                                        station.add(stationArray.getString(j));
                                    }
                                    hello_name = name;
                                    update_name(hello_name);
                                    if (station.size() > 0){
                                        load_data(station.get(0));
                                    }
                                    else{
                                        settings();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onError(String error) {
                        }
                    });

                swipeRefreshLayout.setRefreshing(false);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                switch (itemId) {
                    case R.id.nav_new_device:
                        Intent intent = new Intent(Dashboard.this, RegisterDevice.class);
                        intent.putExtra("user_id", user_id);
                        startActivity(intent);
                        break;
                    case R.id.nav_edit_profile:
                        Intent intent2 = new Intent(Dashboard.this, UpdateUser.class);
                        intent2.putExtra("user_id", user_id);
                        startActivity(intent2);
                        break;
                    case R.id.nav_logout:
                        sessionManager.logoutUser(Dashboard.this);
                        break;
                    default:
                        return false;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }

    public void create_charts(ArrayList<Entry> temperatureEntries, ArrayList<Entry> humidityEntries, String[] timestamps, double [] mqValues){
        LineDataSet temperatureDataSet = new LineDataSet(temperatureEntries, "Temperatura");
        LineDataSet humidityDataSet = new LineDataSet(humidityEntries, "Umidade");
        temperatureDataSet.setColor(Color.RED);
        humidityDataSet.setColor(Color.BLUE);
        humidityDataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(timestamps));
        LineData lineData = new LineData();
        lineData.addDataSet(humidityDataSet);
        lineData.addDataSet(temperatureDataSet);
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setLabelCount(10, true);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setLabelCount(10, true);
        rightAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        lineChart.getDescription().setEnabled(false);
        lineChart.setData(lineData);
        lineChart.invalidate();


        mqChart.setDrawGridBackground(false);
        mqChart.getDescription().setEnabled(false);
        ArrayList<Entry> entries = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            float value = (float) mqValues[i];
            entries.add(new Entry(i, value));
        }
        LineDataSet lineDataSet2 = new LineDataSet(entries, "Medições MQ 135");
        lineDataSet2.setColor(Color.argb(255, 0, 128, 0));
        lineDataSet2.setCircleColor(Color.argb(255, 0, 128, 0));
        lineDataSet2.setLineWidth(2f);
        lineDataSet2.setCircleRadius(4f);
        lineDataSet2.setDrawValues(true);
        LineData lineDatamq = new LineData(lineDataSet2);
        mqChart.setData(lineDatamq);
        XAxis xAxismq = mqChart.getXAxis();
        xAxismq.setValueFormatter(new IndexAxisValueFormatter(timestamps));
        mqChart.getDescription().setTextColor(Color.BLUE);
        mqChart.invalidate();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void settings(){
        String warning = "Cadastre um dispositivo.";
        tv_hour_difference.setText(warning);
    }

    public void update_name(String name){
        tv_hello_user.setText("Olá, " + name + "!");
    }

    public void load_data(String device){
        StationDAO stationDAO = new StationDAO();
        stationDAO.retrieve_station_data(Dashboard.this, device, new StationDAO.VolleyCallback() {
            @Override
            public void onSuccess(String response) throws JSONException {

                JSONArray jsonArray = new JSONArray(response);
                String[] timestamps = new String[jsonArray.length()];
                double[] degrees = new double[jsonArray.length()];
                double[] humidityValues = new double[jsonArray.length()];
                double[] mqValues = new double[jsonArray.length()];
                String last_date = "";
                double last_temp = 0;
                double last_humidity = 0;
                double last_mq135 = 0;
                int last_ldr = 0;

                for (int i = 0; i < 20; i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String timestamp = jsonObject.getString("timestamp");
                    double degreeC = jsonObject.getDouble("degree_c");
                    double humidity = jsonObject.getInt("humidity");
                    double mq135 = jsonObject.getInt("ppm_mq135");
                    int ldr = jsonObject.getInt("voltage_ldr");

                    try {
                        if (i == 0) {
                            last_date = timestamp;
                            last_temp = degreeC;
                            last_humidity = humidity;
                            last_mq135 = mq135;
                            last_ldr = ldr;
                        }
                        Date date = inputDateFormat.parse(timestamp);
                        timestamps[i] = outputDateFormat.format(date);
                        degrees[i] = degreeC;
                        humidityValues[i] = humidity;
                        mqValues[i] = mq135;
                    } catch (java.text.ParseException e) {
                        e.printStackTrace();
                    }
                }

                int start = 0;
                int end = 20 - 1;
                while (start < end) {
                    String temp = timestamps[start];
                    timestamps[start] = timestamps[end];
                    timestamps[end] = temp;

                    double degree = degrees[start];
                    degrees[start] = degrees[end];
                    degrees[end] = degree;

                    double humidityValue = humidityValues[start];
                    humidityValues[start] = humidityValues[end];
                    humidityValues[end] = humidityValue;

                    double mq135 = mqValues[start];
                    mqValues[start] = mqValues[end];
                    mqValues[end] = mq135;

                    start++;
                    end--;
                }

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
                LocalDateTime pastDateTime = LocalDateTime.parse(last_date, formatter);
                LocalDateTime currentDateTime = LocalDateTime.now();
                Duration duration = Duration.between(pastDateTime, currentDateTime);
                long hoursDifference = duration.toHours();
                long minutesDifference = duration.toMinutes();

                String time_diff;
                if (minutesDifference >= 60){
                    if (minutesDifference == 60){
                        time_diff = "Última medição há " + hoursDifference + " hora";
                    }
                    else{
                        time_diff = "Última medição há " + hoursDifference + " horas";
                    }
                }
                else if (minutesDifference == 1){
                    time_diff = "Última medição há " + minutesDifference + " minuto";
                }
                else{
                    time_diff = "Última medição há " + minutesDifference + " minutos";
                }

                tv_hour_difference.setText(time_diff);
                tv_last_temperature.setText(Double.toString(last_temp) + "°C");
                tv_last_humidity.setText((int) Math.round(last_humidity) + "%");
                tv_last_mq135.setText((int) Math.round(last_mq135) + " PPM");

                if (last_temp < 15) {
                    tv_temperature_evaluation.setText("Muito baixa");
                    tv_temperature_evaluation.setTextColor(Color.argb(255, 0, 64, 128));
                } else if (last_temp >= 15 && last_temp < 20) {
                    tv_temperature_evaluation.setText("Baixa");
                    tv_temperature_evaluation.setTextColor(Color.argb(255, 0, 64, 128));
                } else if (last_temp >= 20 && last_temp <= 25) {
                    tv_temperature_evaluation.setText("Confortável");
                    tv_temperature_evaluation.setTextColor(Color.argb(255, 0, 128, 0));
                } else if (last_temp > 25 && last_temp <= 30) {
                    tv_temperature_evaluation.setText("Alta");
                    tv_temperature_evaluation.setTextColor(Color.argb(255, 223, 32, 47));
                } else {
                    tv_temperature_evaluation.setText("Muito alta");
                    tv_temperature_evaluation.setTextColor(Color.argb(255, 223, 32, 47));
                }

                if (last_humidity < 30) {
                    tv_humidity_evaluation.setText("Ambiente muito seco");
                    tv_humidity_evaluation.setTextColor(Color.argb(255, 223, 32, 47));
                } else if (last_humidity >= 30 && last_humidity < 40) {
                    tv_humidity_evaluation.setText("Ambiente seco");
                    tv_humidity_evaluation.setTextColor(Color.argb(255, 255, 128, 26));
                } else if (last_humidity >= 40 && last_humidity < 50) {
                    tv_humidity_evaluation.setText("Ambiente um pouco seco");
                    tv_humidity_evaluation.setTextColor(Color.argb(255, 255, 128, 26));
                } else if (last_humidity >= 50 && last_humidity < 60) {
                    tv_humidity_evaluation.setText("Umidade normal");
                    tv_humidity_evaluation.setTextColor(Color.argb(255, 0, 128, 0));
                } else if (last_humidity >= 60 && last_humidity < 70) {
                    tv_humidity_evaluation.setText("Ambiente um pouco úmido");
                    tv_humidity_evaluation.setTextColor(Color.argb(255, 255, 128, 26));
                } else if (last_humidity >= 70 && last_humidity < 80) {
                    tv_humidity_evaluation.setText("Ambiente úmido");
                    tv_humidity_evaluation.setTextColor(Color.argb(255, 255, 128, 26));
                } else {
                    tv_humidity_evaluation.setText("Ambiente muito úmido");
                    tv_humidity_evaluation.setTextColor(Color.argb(255, 223, 32, 47));
                }

                if (last_mq135 < 100) {
                    tv_air_evaluation.setText("Qualidade do ar muito boa");
                    tv_air_evaluation.setTextColor(Color.argb(255, 0, 128, 0));
                } else if (last_mq135 >= 100 && last_mq135 < 200) {
                    tv_air_evaluation.setText("Boa qualidade do ar");
                    tv_air_evaluation.setTextColor(Color.argb(255, 0, 128, 0));
                } else if (last_mq135 >= 200 && last_mq135 < 300) {
                    tv_air_evaluation.setText("Qualidade do ar regular");
                    tv_air_evaluation.setTextColor(Color.argb(255, 255, 128, 26));
                } else if (last_mq135 >= 300 && last_mq135 < 400) {
                    tv_air_evaluation.setText("Qualidade do ar ruim");
                    tv_air_evaluation.setTextColor(Color.argb(255, 223, 32, 47));
                } else {
                    tv_air_evaluation.setText("Qualidade do ar muito ruim");
                    tv_air_evaluation.setTextColor(Color.argb(255, 223, 32, 47));
                }

                if (last_ldr < 100) {
                    tv_light_evaluation.setText("Iluminação muito boa");
                } else if (last_ldr >= 100 && last_ldr < 300) {
                    tv_light_evaluation.setText("Iluminação boa");
                } else if (last_ldr >= 300 && last_ldr < 700) {
                    tv_light_evaluation.setText("Iluminação regular");
                } else if (last_ldr >= 700 && last_ldr < 900) {
                    tv_light_evaluation.setText("Iluminação ruim");
                } else {
                    tv_light_evaluation.setText("Iluminação muito ruim");
                }

                ArrayList<Entry> temperatureEntries = new ArrayList<>();
                ArrayList<Entry> humidityEntries = new ArrayList<>();
                for (int i = 0; i < 20; i++) {
                    temperatureEntries.add(new Entry(i, (float) degrees[i]));
                    humidityEntries.add(new Entry(i, (float) humidityValues[i]));
                }
                create_charts(temperatureEntries, humidityEntries, timestamps, mqValues);
            }
            @Override
            public void onError(String error) {
                Errorresponse = error;
            }
        });
    }
}