package kz.talipovsn.room_database_demo;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.widget.EditText;
import android.widget.Toast;

// АКТИВНОСТЬ ДЛЯ ДОБАВЛЕНИЯ НОВОЙ ЗАПИСИ В ROOM-БД
public class AddDataActivity extends AppCompatActivity {

    EditText title;
    EditText description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        findViewById(R.id.save).setOnClickListener(v -> onSaveClick());
        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);
    }

    public void onSaveClick() {
        RoomDatabase database = Database.getInstance().getDatabaseInstance();

        try {
            DataModel model = new DataModel();
            model.setTitle(title.getText().toString());
            model.setDescription(description.getText().toString());
            database.getDao().insert(model);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    "Нельзя создавать записи с одинаковым названием!", Toast.LENGTH_LONG).show();
        }

        finish();
    }
}
