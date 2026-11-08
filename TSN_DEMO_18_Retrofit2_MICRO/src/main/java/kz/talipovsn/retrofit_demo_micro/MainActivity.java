package kz.talipovsn.retrofit_demo_micro;

import android.os.StrictMode;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

import static kz.talipovsn.retrofit_demo_micro.MainActivity.ApiInterface.URL;

public class MainActivity extends AppCompatActivity {

    private TextView textView; // Текстовый компонент для вывода результата

    // Наш интерфейс передачи параметров сайту
    interface ApiInterface {
        String URL = "https://blockchain.info/"; // Сайт, с которого берем данные (обязательно со слешем в конце)
        String API = "ticker"; // Путь к данным на сайте

        @GET(API)
        Call<DataModel> getData(); // Метод получения данных с сайта
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Установка темы
        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);

        // Разрешаем запуск в общем потоке выполнения длительных задач (например, чтение с сети)
        // ЭТО ТОЛЬКО ДЛЯ ПРИМЕРА, ПО-НОРМАЛЬНОМУ НАДО ВСЕ В ОТДЕЛЬНЫХ ПОТОКАХ
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        textView = findViewById(R.id.textView); // Находим наш текстовый компонент

        onClick(null); // Нажмем на кнопку "Обновить" при старте
    }

    // Кнопка "Обновить"
    public void onClick(View view) {
        try {
            // Подключаем технологию Retrofit2 для работы с сайтом
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            // Делаем обращение к сайту через Retrofit2 синхронным доступом
            Response<DataModel> response = retrofit.create(ApiInterface.class).getData().execute();

            // Считываем полученные с сайта данные в java-форме от Retrofit2
            DataModel data = response.body();

            if (data != null && data.getUSD() != null) {
                // Считываем нужные поля данных
                Double rate = data.getUSD().getLast(); // Курс биткоина в USD
                String symbol = data.getUSD().getSymbol(); // Символ валюты ($)

                // Выводим результат в текстовый компонент
                textView.setText(""); // Очищаем текстовый компонент
                textView.append("1 BTC = " + rate + " " + symbol + " (USD)\n");
                textView.append("1 BTC = " + data.getEUR().getLast() + " " + data.getEUR().getSymbol() + " (EUR)\n");
            } else {
                textView.setText(R.string.error);
            }
        } catch (Exception e) {
            textView.setText(R.string.error);
        }
    }
}