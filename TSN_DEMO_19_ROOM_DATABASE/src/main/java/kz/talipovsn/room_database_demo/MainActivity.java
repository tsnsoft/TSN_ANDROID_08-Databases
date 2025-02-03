package kz.talipovsn.room_database_demo;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;

// ИСПОЛЬЗУЕМ ТЕХНОЛОГИЮ BUTTERKNIFE ДЛЯ БИНДИНГА КОМПОНЕНТ С ПЕРЕМЕННЫМИ
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recyclerView) // Аннотация для привязки переменной к компоненту
    RecyclerView recyclerView; // Ссылка на компонент RecyclerView

    private RoomDatabase database; // Ссылка на ROOM-БД

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);

        ButterKnife.bind(this); // Привязываем компоненты к переменным
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
        switch (item.getItemId()) {
            // Добавление новой записи
            case R.id.action_add: {
                startActivity(new Intent(this, AddDataActivity.class));
                break;
            }
        }
        return false;
    }

    // МЕТОД ДЛЯ ОБНОВЛЕНИЯ СПИСКА ПРИ ВОЗВРАТЕ НА ЭКРАН
    @Override
    protected void onResume() {
        super.onResume();
        recyclerView.setAdapter(new RecyclerAdapter(database));
    }

}
