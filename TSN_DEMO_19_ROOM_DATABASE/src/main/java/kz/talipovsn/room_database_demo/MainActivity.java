package kz.talipovsn.room_database_demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

// ГЛАВНЫЙ ЭКРАН СО СПИСКОМ ЗАПИСЕЙ
public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView; // Визуальный список на экране

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView); // Находим компонент списка на экране по его ID
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Элементы списка идут друг под другом
    }

    // Показываем меню с кнопкой "Добавить"
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_button, menu);
        return true;
    }

    // Нажатие на кнопку меню: открываем экран добавления записи
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            startActivity(new Intent(this, AddDataActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Вызывается каждый раз, когда экран снова виден (в том числе после возврата с экрана добавления).
    // Заново читаем данные из БД, поэтому новая запись сразу появляется в списке.
    @Override
    protected void onResume() {
        super.onResume();
        recyclerView.setAdapter(new RecyclerAdapter());
    }
}
