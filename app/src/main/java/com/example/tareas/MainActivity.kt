package com.example.tareas

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var tareaAdapter: TareaAdapter
    private lateinit var fabAgregarTarea: FloatingActionButton
    private lateinit var dbHandler: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerViewTareas)
        fabAgregarTarea = findViewById(R.id.fabAgregarTarea)
        dbHandler = DBHelper(this)

        // Configurar el RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        tareaAdapter = TareaAdapter(dbHandler.obtenerTareas(), this)
        recyclerView.adapter = tareaAdapter

        // Configurar el FloatingActionButton
        fabAgregarTarea.setOnClickListener {
            val intent = Intent(this, AddEditTareaActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        // Actualiza la lista de tareas cuando se retorne a la actividad principal
        tareaAdapter.actualizarTareas(dbHandler.obtenerTareas())
    }

    // actualiza la lista de tarea
    fun actualizarListaTareas() {
        tareaAdapter.actualizarTareas(dbHandler.obtenerTareas())
    }
}
