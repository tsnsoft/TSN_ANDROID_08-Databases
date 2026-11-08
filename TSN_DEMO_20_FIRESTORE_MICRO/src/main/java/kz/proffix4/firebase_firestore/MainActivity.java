package kz.proffix4.firebase_firestore;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.WindowCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Демонстрация базовых операций CRUD (Создание, Чтение, Обновление, Удаление)
 * в облачной NoSQL базе данных Google Cloud Firestore.
 */
public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore db; // Объект для работы с облачной БД Firestore
    private final String collectionPath = "my_table"; // Имя коллекции (аналог таблицы в SQL)

    private TextView textView; // Текстовый компонент для вывода результата
    private String lastId = null; // Хранит ID последнего обработанного документа (записи)

    /**
     * Инициализация приложения при запуске
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);

        // Получаем экземпляр подключения к облачной БД Firestore (настройки берутся из google-services.json)
        db = FirebaseFirestore.getInstance();

        textView = findViewById(R.id.textView); // Находим наш текстовый компонент
    }

    /**
     * CREATE: Добавление новой записи (документа) в коллекцию БД
     */
    public void onAdd(View view) {
        textView.setText("   добавление данных в облако...");

        // В Firestore документ представляется как набор пар "Ключ - Значение" (Map)
        Map<String, Object> record = new HashMap<>();

        // Добавляем в запись поле "currentDate" с текущей датой
        record.put("currentDate", new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()));

        // Добавляем в запись поле "currentTime" с текущим временем
        record.put("currentTime", new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date()));

        // Метод .add() автоматически генерирует уникальный ID для нового документа
        db.collection(collectionPath)
                .add(record)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    // Срабатывает при успешной отправке данных в облако
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        lastId = documentReference.getId(); // Запоминаем ID только что созданной записи
                        textView.setText(String.format("Добавлена запись в таблицу БД с ID: %s", lastId));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    // Срабатывает при ошибке (например, нет интернета или закрыт доступ в Rules)
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        textView.setText(String.format("Ошибка при добавлении записи в БД: %s", e.getMessage()));
                    }
                });
    }

    /**
     * READ: Чтение всех записей (документов) из коллекции БД
     */
    public void onLoad(View view) {
        textView.setText("   загрузка базы данных с облака...");

        db.collection(collectionPath)
                .get() // Асинхронный запрос всех документов коллекции
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            textView.setText(""); // Очищаем текстовый компонент перед выводом
                            lastId = null; // Сбрасываем ID перед перебором

                            // Цикл по всем полученным документам коллекции
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                lastId = document.getId(); // Сохраняем ID текущей записи (в итоге останется последняя)
                                textView.append(lastId + " => " + document.getData() + "\n\n");
                            }

                            if (lastId == null) {
                                textView.setText("Таблица (коллекция) пуста");
                            }
                        } else {
                            String error = (task.getException() != null) ? task.getException().getMessage() : "Неизвестная ошибка";
                            textView.setText(String.format("Ошибка получения записей: %s", error));
                        }
                    }
                });
    }

    /**
     * DELETE: Удаление последней записи в коллекции БД по её ID
     */
    public void onDelete(View view) {
        if (lastId == null) {
            Toast.makeText(this, "Сначала загрузите или добавьте данные!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Обращаемся к конкретному документу по пути "имя_коллекции/ID_документа" и удаляем его
        db.document(collectionPath + "/" + lastId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(MainActivity.this, "Последняя запись удалена", Toast.LENGTH_SHORT).show();
                        onLoad(null); // Обновляем список на экране только ПОСЛЕ успешного удаления в облаке
                    }
                });
    }

    /**
     * UPDATE: Изменение последней записи в коллекции БД по её ID
     */
    public void onModify(View view) {
        if (lastId == null) {
            Toast.makeText(this, "Сначала загрузите или добавьте данные!", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> record = new HashMap<>(); // Новые данные для записи
        record.put("name", "TSN"); // Поле "name" со значением "TSN"
        record.put("os", "linux"); // Поле "os" со значением "linux"

        // ВАРИАНТ 1: Метод .set() полностью перезаписывает документ (старые поля currentDate/currentTime сотрутся)
        db.document(collectionPath + "/" + lastId)
                .set(record)
                // ВАРИАНТ 2: Метод .update(record) обновляет только указанные поля, сохраняя старые поля документа:
                // .update(record)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(MainActivity.this, "Последняя запись изменена", Toast.LENGTH_SHORT).show();
                        onLoad(null); // Обновляем список на экране только ПОСЛЕ успешного изменения
                    }
                });
    }

    /**
     * Выход из программы
     */
    public void onExit(View view) {
        finish();
    }
}