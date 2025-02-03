package kz.talipovsn.retrofit_demo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// КЛАСС СОЗДАТЕЛЯ СПИСКА RecyclerView
public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private List<DataModel> dataList; // Список с данными

    // Конструктор класса
    DataAdapter(List<DataModel> dataList) {
        this.dataList = dataList; // Сохраняем список данных в поле класса
    }

    // Класс для отображения одного элемента списка RecyclerView
    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name; // Компонент для отображения названия
        private TextView rate; // Компонент для отображения курса

        ViewHolder(View itemView) { // Конструктор класса
            super(itemView);
            // ---- Наши поля ----
            name = itemView.findViewById(R.id.postitem_name); // Поиск компонента для отображения названия
            rate = itemView.findViewById(R.id.postitem_rate);  // Поиск компонента для отображения курса
            // -------------------
        }
    }

    // Метод для отображения данных в элементе списка RecyclerView
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DataModel data = dataList.get(position); // Получаем данные из списка по позиции элемента
        holder.name.setText(String.format("%s %s стоит", data.getSootnowenie(), data.getNameRus())); // Отображаем название
        holder.rate.setText(String.format("%s %s", data.getKurs(), data.getEdinicaIzmerenia())); // Отображаем курс
    }

    // Метод для создания ViewHolder (класса для отображения одного элемента списка)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item,
                parent, false);
        return new ViewHolder(v);
    }

    // Методы выдачи количества элементов списка
    @Override
    public int getItemCount() {
        if (dataList == null) {
            return 0;
        }
        return dataList.size();
    }

}
