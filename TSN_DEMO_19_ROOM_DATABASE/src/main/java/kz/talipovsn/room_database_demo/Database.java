package kz.talipovsn.room_database_demo;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.List;


// 1. ТАБЛИЦА БАЗЫ ДАННЫХ
// Класс = таблица, поля класса = столбцы таблицы.
// Поля сделаны public, поэтому геттеры и сеттеры не нужны.
@Entity
class DataModel {
    @PrimaryKey
    @NonNull
    public String title;       // Название записи (первичный ключ, не может повторяться)
    public String description; // Описание записи
}


// 2. ИНТЕРФЕЙС ДЛЯ РАБОТЫ С ТАБЛИЦЕЙ (DAO)
// Мы только описываем методы, а код для них Room создаст сам.
@Dao
interface IDao {
    @Insert
    void insert(DataModel item);   // Добавить запись

    @Delete
    void delete(DataModel item);   // Удалить запись

    @Query("SELECT * FROM DataModel")
    List<DataModel> getAllData();  // Получить все записи
}


// 3. САМА БАЗА ДАННЫХ
// Здесь перечисляем все таблицы (entities) и версию базы.
@androidx.room.Database(entities = {DataModel.class}, version = 1, exportSchema = false)
abstract class AppDatabase extends RoomDatabase {
    public abstract IDao getDao();
}


// 4. КЛАСС ПРИЛОЖЕНИЯ
// Запускается один раз при старте программы и создаёт базу данных.
// Из любой активности доступ к базе: Database.getDao()
public class Database extends Application {

    private static IDao dao;

    public static IDao getDao() {
        return dao;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppDatabase db = Room.databaseBuilder(this, AppDatabase.class, "my-database")
                .allowMainThreadQueries() // Разрешаем работу с БД в главном потоке (только для учебного примера!)
                .build();
        dao = db.getDao();
    }
}
