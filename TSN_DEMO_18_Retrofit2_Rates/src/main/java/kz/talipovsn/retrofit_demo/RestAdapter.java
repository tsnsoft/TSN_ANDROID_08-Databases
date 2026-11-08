package kz.talipovsn.retrofit_demo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static kz.talipovsn.retrofit_demo.UnsafeOkHttpClient.getUnsafeOkHttpClient;

// АДАПТЕР ЧТЕНИЯ С САЙТА ЧЕРЕЗ ТЕХНОЛОГИЮ RETROFIT 2
class RestAdapter {
    // Надежный открытый API без ключей, возвращающий готовый JSON-массив валют на русском языке
    private static final String URL = "https://api.nbrb.by"; // Сайт, с которого берем данные
    private static final String API = "/exrates/rates?periodicity=0"; // Путь к данным на сайте

    private static Retrofit retrofit = null; // Наш объект для работы с сайтом
    private static ApiInterface apiInterface; // Наш интерфейс передачи параметров сайту

    // Наш интерфейс передачи параметров сайту
    interface ApiInterface {
        @GET(API)
        Call<List<DataModel>> getData(@Query("pretty") String param); // Метод передачи параметров сайту
    }

    // Получение объекта для работы с сайтом
    static ApiInterface getApiClient() {
        if (apiInterface == null) {
            try {
                retrofit = new Retrofit.Builder() // Создаем объект для работы с сайтом
                        .baseUrl(URL) // Указываем сайт
                        .client(getUnsafeOkHttpClient().build()) // Указываем клиент для работы с сайтом
                        .addConverterFactory(GsonConverterFactory.create()) // Указываем конвертер json -> java classes
                        .build(); // Создаем объект

            } catch (Exception ignore) {
            }
            apiInterface = retrofit.create(ApiInterface.class); // Создаем интерфейс передачи параметров сайту
        }
        return apiInterface; // Возвращаем объект для работы с сайтом
    }

}