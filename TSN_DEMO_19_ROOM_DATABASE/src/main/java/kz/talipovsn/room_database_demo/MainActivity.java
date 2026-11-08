package kz.talipovsn.room_database_demo;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView; // Ссылка на компонент RecyclerView

    private RoomDatabase database; // Ссылка на ROOM-БД

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);

        // Инициализируем RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        database = Database.getInstance().getDatabaseInstance(); // Получаем экземпляр ROOM-БД
    }

    // МЕТОД ДЛЯ ОТОБРАЖЕНИЯ МЕНЮ
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_button, menu);
        return true;
    }

    // МЕТОД ДЛЯ ОБРАБОТКИ НАЖАТИЙ НА КНОПКИ МЕНЮ
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            // Добавление новой записи
            startActivity(new Intent(this, AddDataActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // МЕТОД ДЛЯ ОБНОВЛЕНИЯ СПИСКА ПРИ ВОЗВРАТЕ НА ЭКРАН
    @Override
    protected void onResume() {
        super.onResume();
        recyclerView.setAdapter(new RecyclerAdapter(database));
    }

}
