package kz.talipovsn.room_database_demo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// КЛАСС ВИЗУАЛЬНОГО СПИСКА С ДАННЫМИ
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private RoomDatabase database; // Ссылка на базу данных
    private List<DataModel> data; // Ссылка на данные из ROOM-БД

    RecyclerAdapter(RoomDatabase database) {
        this.database = database;
        this.data = database.getDao().getAllData();
    }

    // ПОДКЛАСС СОЗДАНИЯ ЭЛЕМЕНТА СПИСКА
    class RecyclerViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView description;
        public TextView delete;

        RecyclerViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            delete = itemView.findViewById(R.id.delete);

            delete.setOnClickListener(view -> {
                int numRec = getAdapterPosition();
                database.getDao().delete(data.get(numRec)); // Удаляем данные из БД
                data.remove(numRec); // Удаляем данные из спсиска с БД
                notifyItemRemoved(numRec); // Оповещаем об изменении данных в RecyclerView
            });
        }

    }

    // ЗАГРУЗКА ДАННЫХ В ВИЗУАЛЬНЫЙ СПИСОК ИЗ ROOM-БД
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final RecyclerViewHolder view = (RecyclerViewHolder) holder;
        // Инициализируем компоненты в новом элементе списка
        view.title.setText(data.get(position).getTitle());
        view.description.setText(data.get(position).getDescription());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}
