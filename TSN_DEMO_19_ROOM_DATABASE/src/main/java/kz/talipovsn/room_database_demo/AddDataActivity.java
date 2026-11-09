package kz.talipovsn.room_database_demo;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

// ЭКРАН ДОБАВЛЕНИЯ НОВОЙ ЗАПИСИ В БАЗУ ДАННЫХ
public class AddDataActivity extends AppCompatActivity {

    EditText title;       // Поле ввода названия
    EditText description; // Поле ввода описания

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        title = findViewById(R.id.title); // Находим поле ввода названия по его ID
        description = findViewById(R.id.description); // Находим поле ввода описания по его ID

        // По нажатию на кнопку "Сохранить" вызываем метод onSaveClick()
        findViewById(R.id.save).setOnClickListener(v -> onSaveClick());
    }

    // Сохранение записи в базу данных
    void onSaveClick() {
        DataModel item = new DataModel(); // Создаем объект записи
        item.title = title.getText().toString(); // Заполняем его полями из формы
        item.description = description.getText().toString(); // Заполняем его полями из формы

        try {
            Database.getDao().insert(item); // Добавляем запись в БД
            finish();                       // Закрываем экран и возвращаемся к списку
        } catch (Exception e) {
            // Ошибка возникает, если запись с таким названием уже есть (название — первичный ключ)
            Toast.makeText(this, "Нельзя создавать записи с одинаковым названием!",
                    Toast.LENGTH_LONG).show();
        }
    }
}
