package com.example.tareas

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TareaAdapter(private var listaTareas: List<Tarea>, private val mainActivity: MainActivity) : RecyclerView.Adapter<TareaAdapter.TareaViewHolder>() {

    class TareaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewNombre: TextView = itemView.findViewById(R.id.textViewNombre)
        val textViewDescripcion: TextView = itemView.findViewById(R.id.textViewDescripcion)
        val textViewFecha: TextView = itemView.findViewById(R.id.textViewFecha)
        val buttonEditar: ImageButton = itemView.findViewById(R.id.buttonEditar)
        val buttonEliminar: ImageButton = itemView.findViewById(R.id.buttonEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareaViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_tarea, parent, false)
        return TareaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TareaViewHolder, position: Int) {
        val tarea = listaTareas[position]
        holder.textViewNombre.text = tarea.nombre
        holder.textViewDescripcion.text = tarea.descripcion
        holder.textViewFecha.text = tarea.fecha

        holder.buttonEditar.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, AddEditTareaActivity::class.java).apply {
                putExtra("TAREA_ID", tarea.id)
            }
            context.startActivity(intent)
        }

        holder.buttonEliminar.setOnClickListener {
            val context = holder.itemView.context
            val dbHandler = DBHelper(context)
            dbHandler.eliminarTarea(tarea.id)
            mainActivity.actualizarListaTareas()  // Llamar al nuevo método para actualizar la lista
        }
    }

    override fun getItemCount(): Int {
        return listaTareas.size
    }

    fun actualizarTareas(nuevasTareas: List<Tarea>) {
        listaTareas = nuevasTareas
        notifyDataSetChanged()
    }
}
