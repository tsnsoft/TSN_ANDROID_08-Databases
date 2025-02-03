package kz.talipovsn.room_database_demo;

import android.app.Application;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.Room;
import androidx.annotation.NonNull;

import java.util.List;


// ОПИСАНИЕ СТРУКТУРЫ ROOM-БД
@Entity
class DataModel {
    @NonNull
    @PrimaryKey
    private String title; // Название записи
    private String description; // Описание записи

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}


// ИНТЕРФЕЙС ДОСТУПА К ROOM-БД
@Dao
interface IDao {
    @Insert
    void insert(DataModel dataModel); // Добавление записи в БД

    @Delete
    void delete(DataModel dataModel); // Удаление записи из БД

    @Query("SELECT * FROM DataModel")
        // Получение всех записей из БД
    List<DataModel> getAllData();
}


// КЛАСС ДОСТУПА К ROOM-БД
@androidx.room.Database(entities = {DataModel.class}, version = 1, exportSchema = false)
abstract class RoomDatabase extends androidx.room.RoomDatabase {
    public abstract IDao getDao();
}


// ГЛОБАЛЬНЫЙ КЛАСС, ДОСТУПНЫЙ ВСЕМ АКТИВНОСТЯМ ПРОГРАММЫ, ДЛЯ РАБОТЫ С ROOM-БД
public class Database extends Application {
    final String DATABASE_NAME = "my-database"; // Имя файла базы данных в памяти устройства  формата SQLite

    private static Database instance; // Ссылка на данный класс для доступа из других классов
    private RoomDatabase roomDatabase; // Класс для работы с БД ROOM формата SQLite

    // Метод для получения ссылки на данный класс
    public static Database getInstance() {
        return instance;
    }

    // Метод, вызываемый при запуске программы
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this; // Сохраняем ссылку на данный класс
        roomDatabase = Room.databaseBuilder(getApplicationContext(), RoomDatabase.class, DATABASE_NAME)
                .allowMainThreadQueries().fallbackToDestructiveMigration().build(); // Создаем объект для работы с БД
    }

    // Метод для получения объекта для работы с БД
    public RoomDatabase getDatabaseInstance() {
        return roomDatabase;
    }

}
