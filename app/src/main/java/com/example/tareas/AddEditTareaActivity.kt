package com.example.tareas

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddEditTareaActivity : AppCompatActivity() {

    private lateinit var editTextNombre: EditText
    private lateinit var editTextDescripcion: EditText
    private lateinit var editTextFecha: EditText
    private lateinit var buttonGuardar: Button
    private lateinit var dbHandler: DBHelper
    private var tareaId: Int = -1
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_tarea)

        editTextNombre = findViewById(R.id.editTextNombre)
        editTextDescripcion = findViewById(R.id.editTextDescripcion)
        editTextFecha = findViewById(R.id.editTextFecha)
        buttonGuardar = findViewById(R.id.buttonGuardar)
        dbHandler = DBHelper(this)

        // Configura el DatePickerDialog
        editTextFecha.setOnClickListener {
            mostrarDatePickerDialog()
        }

        // Verifica si estamos editando una tarea existente
        if (intent.hasExtra("TAREA_ID")) {
            tareaId = intent.getIntExtra("TAREA_ID", -1)
            val tarea = dbHandler.obtenerTarea(tareaId)
            tarea?.let {
                editTextNombre.setText(it.nombre)
                editTextDescripcion.setText(it.descripcion)
                editTextFecha.setText(it.fecha)
            }
        }

        // Guarda o actualiza la tarea
        buttonGuardar.setOnClickListener {
            val nombre = editTextNombre.text.toString()
            val descripcion = editTextDescripcion.text.toString()
            val fecha = editTextFecha.text.toString()

            if (tareaId == -1) {
                dbHandler.agregarTarea(Tarea(0, nombre, descripcion, fecha))
            } else {
                dbHandler.actualizarTarea(Tarea(tareaId, nombre, descripcion, fecha))
            }
            finish()
        }
    }

    private fun mostrarDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val fechaSeleccionada = Calendar.getInstance()
                fechaSeleccionada.set(year, month, dayOfMonth)
                editTextFecha.setText(dateFormat.format(fechaSeleccionada.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }
}
