package com.example.tareas

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

data class Tarea(val id: Int, val nombre: String, val descripcion: String, val fecha: String)

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "tareas.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_TAREAS = "tareas"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NOMBRE = "nombre"
        private const val COLUMN_DESCRIPCION = "descripcion"
        private const val COLUMN_FECHA = "fecha"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = ("CREATE TABLE $TABLE_TAREAS ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COLUMN_NOMBRE TEXT, "
                + "$COLUMN_DESCRIPCION TEXT, "
                + "$COLUMN_FECHA TEXT)")
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_TAREAS")
        onCreate(db)
    }

    fun agregarTarea(tarea: Tarea): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NOMBRE, tarea.nombre)
            put(COLUMN_DESCRIPCION, tarea.descripcion)
            put(COLUMN_FECHA, tarea.fecha)
        }
        return db.insert(TABLE_TAREAS, null, values)
    }

    fun obtenerTareas(): List<Tarea> {
        val listaTareas = mutableListOf<Tarea>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_TAREAS", null)
        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(COLUMN_ID))
                val nombre = getString(getColumnIndexOrThrow(COLUMN_NOMBRE))
                val descripcion = getString(getColumnIndexOrThrow(COLUMN_DESCRIPCION))
                val fecha = getString(getColumnIndexOrThrow(COLUMN_FECHA))
                listaTareas.add(Tarea(id, nombre, descripcion, fecha))
            }
            close()
        }
        return listaTareas
    }

    fun obtenerTarea(id: Int): Tarea? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_TAREAS WHERE $COLUMN_ID = ?", arrayOf(id.toString()))
        cursor?.moveToFirst()
        return if (cursor != null && cursor.count > 0) {
            val tarea = Tarea(
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPCION)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA))
            )
            cursor.close()
            tarea
        } else {
            cursor?.close()
            null
        }
    }

    fun actualizarTarea(tarea: Tarea): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NOMBRE, tarea.nombre)
            put(COLUMN_DESCRIPCION, tarea.descripcion)
            put(COLUMN_FECHA, tarea.fecha)
        }
        return db.update(TABLE_TAREAS, values, "$COLUMN_ID = ?", arrayOf(tarea.id.toString()))
    }

    fun eliminarTarea(id: Int): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_TAREAS, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }
}
