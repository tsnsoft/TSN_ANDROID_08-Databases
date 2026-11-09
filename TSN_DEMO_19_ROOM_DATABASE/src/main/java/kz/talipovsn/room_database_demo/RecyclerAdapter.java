package kz.talipovsn.room_database_demo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// АДАПТЕР: связывает данные из БД с визуальным списком RecyclerView
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private final List<DataModel> data = Database.getDao().getAllData(); // Читаем все записи из БД

    // ЭЛЕМЕНТ СПИСКА: хранит ссылки на компоненты одной строки списка
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title; // Название записи
        TextView description; // Описание записи

        // Конструктор: находит компоненты строки по их ID
        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title); // Название записи
            description = itemView.findViewById(R.id.description); // Описание записи

            // Нажатие на "Удалить"
            itemView.findViewById(R.id.delete).setOnClickListener(v -> {
                int position = getAdapterPosition();
                Database.getDao().delete(data.get(position)); // Удаляем запись из БД
                data.remove(position);                        // Удаляем запись из списка
                notifyItemRemoved(position);                  // Сообщаем списку, чтобы он обновился
            });
        }
    }

    // Создание новой строки списка из разметки item_data.xml
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data, parent, false);
        return new ViewHolder(view);
    }

    // Заполнение строки данными записи с номером position
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataModel item = data.get(position);
        holder.title.setText(item.title);
        holder.description.setText(item.description);
    }

    // Сколько всего строк в списке
    @Override
    public int getItemCount() {
        return data.size();
    }
}
