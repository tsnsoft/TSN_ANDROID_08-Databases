package kz.proffix4.firebase_firestore;

import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore db; // База данных Firestore
    private final String collectionPath = "my_table"; // Имя таблицы в БД Firestore

    private TextView textView; // Текстовый компонент для вывода результата
    private String lastId; // Последний ID записи в таблице БД

    /**
     * Запуск программы
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);

        db = FirebaseFirestore.getInstance(); // Подключение к БД

        textView = findViewById(R.id.textView); // Находим наш текстовый компонент
    }

    /**
     * Добавление новой записи в таблицу БД
     */
    public void onAdd(View view) {
        textView.setText("   добавление данных в облако...");

        Map<String, Object> record = new HashMap<>(); // Переменная - запись для таблицы БД

        // Добавляем в запись параметр "currentData" с текущей датой
        record.put("currentDate", new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()));

        // Добавляем в запись параметр "currentTime" с текущим временем
        record.put("currentTime", new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date()));

        // Добавляем в базу данных запись с параметрами
        db.collection(collectionPath)
                .add(record)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    // При успешном добавлении записи в таблицу БД
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        textView.setText(String.format("Добавлена запись в таблицу БД с ID: %s", documentReference.getId()));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    // При ошибке добавления записи в таблицу БД
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        textView.setText(String.format("Ошибка при добавлении записи в БД: %s", e.getMessage()));
                    }
                });

    }


    /**
     * Загрузка данных с таблицы БД
     */
    public void onLoad(View view) {
        textView.setText("   загрузка базы данных с облака...");

        db.collection(collectionPath)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            textView.setText(""); // Очищаем текстовый компонент
                            // цикл по записям таблицы БД
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                lastId = document.getId(); // Считываем ID текущей записи
                                textView.append(lastId + " => " + document.getData() + "\n");
                            }
                        } else {
                            textView.setText(String.format("Ошибка получения записей: %s", task.getException().getMessage()));
                        }
                    }
                });
    }


    /**
     * Удаление последней записи в таблице БД
     */
    public void onDelete(View view) {
        db.document(collectionPath + "/" + lastId).delete(); // Удаление последней записи в таблице БД

        Toast.makeText(MainActivity.this, "Последняя запись удалена", Toast.LENGTH_SHORT).show();

        onLoad(null); // Повторная загрузка данных
    }

    /**
     * Изменение последней записи в таблице БД
     */
    public void onModify(View view) {
        Map<String, Object> record = new HashMap<>(); // Переменная - запись для таблицы БД
        record.put("name", "TSN"); // Добавляем в запись параметр "name" со значением "TSN"
        record.put("os", "linux"); // Добавляем в запись параметр "os" со значением "linux"

        db.document(collectionPath + "/" + lastId).set(record); // Так заменяются все данные на новые
        // db.document(collectionPath+"/" + lastId).update(record); // Так изменяются старые/добавляются новые данные

        Toast.makeText(MainActivity.this, "Последняя запись изменена", Toast.LENGTH_SHORT).show();

        onLoad(null); // Повторная загрузка данных
    }


    /**
     * Выход из программы
     */
    public void onExit(View view) {
        finish();
    }

}
