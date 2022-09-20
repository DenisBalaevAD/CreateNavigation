package com.example.imageapi_retrafit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class Adapter(private val images: List<ItemOfList>) : RecyclerView.Adapter<Adapter.ImageViewHolder>() {

    //Создает новый объект ViewHolder всякий раз, когда RecyclerView нуждается в этом
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            LayoutInflater.from(parent.context)
            .inflate(R.layout.image_item, parent, false))
    }

    //Возвращает общее количество элементов списка
    override fun getItemCount(): Int = images.size

    //Принимает объект ViewHolder и устанавливает необходимые данные
    // для соответствующей строки во view-компоненте.
    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bindView(images[position])
    }

    //Класс с меитодом при помощи которого мы будем добавлять картинку по url
    class ImageViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private  val imageItemOfList=view.findViewById<ImageView>(R.id._image_item)
        fun  bindView(image: ItemOfList) =
            Picasso
                //Возвращает глобальный экземпляр Picasso
                .get()
                //Запускает запрос изображения с использованием указанного пути
                .load(image.photo)
                //Уменьшает картинку, этим экономит ресурсы
                .fit()
                //Изображение которое будет отображено в момент загрузки картинки
                .placeholder(R.drawable.ic_launcher_foreground)
                //Картинка отображаемая при ошибки
                .error(R.drawable.ic_launcher_foreground)
                //Указываем ImageView в который будем помещать картинку
                .into(imageItemOfList)
    }
}