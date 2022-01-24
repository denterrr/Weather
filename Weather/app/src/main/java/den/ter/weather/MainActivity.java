package den.ter.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText user_field;
    private Button main_btn;
    private TextView result_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user_field = findViewById(R.id.user_field);
        main_btn = findViewById(R.id.main_btn);
        result_info = findViewById(R.id.result_info);

        main_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user_field.getText().toString().trim().equals("")){
                    Toast.makeText(MainActivity.this,R.string.no_input,Toast.LENGTH_LONG).show();
                }else{
                    String city = user_field.getText().toString();
                    String key = "a9aa10f4d228c6b67e998497ebfc86ca";
                    String url = "https://api.openweathermap.org/data/2.5/weather?q="+ city +"&appid=a9aa10f4d228c6b67e998497ebfc86ca&units=metric&lang=ru";

                    new GetURLData().execute(url);
                }
            }
        });
    }
    private class GetURLData extends AsyncTask<String,String,String>{

        protected void onPreExecute(){
            super.onPreExecute();
            result_info.setText("Ожидайте...");
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while((line = reader.readLine()) != null){
                    buffer.append(line).append("\n");
                }
                return buffer.toString();

            } catch (MalformedURLException e) {Toast.makeText(MainActivity.this,R.string.noo_input,Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Toast.makeText(MainActivity.this,R.string.noo_input,Toast.LENGTH_LONG).show();;
            } finally{
                if (connection != null){
                    connection.disconnect();

                }
                if (reader != null){
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Toast.makeText(MainActivity.this,R.string.noo_input,Toast.LENGTH_LONG).show();
                    }
                }
            }

            return null;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);

            try {
                JSONObject jsonObject = new JSONObject(result);
                result_info.setText("Температура: " + jsonObject.getJSONObject("main").getDouble("temp")+"\n" +
                        "Влажность: " + jsonObject.getJSONObject("main").getInt("humidity"));
            } catch (JSONException e) {
                Toast.makeText(MainActivity.this,R.string.noo_input,Toast.LENGTH_LONG).show();
            }


        }
    }
}
