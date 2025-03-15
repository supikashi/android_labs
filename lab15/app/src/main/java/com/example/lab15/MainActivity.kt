package com.example.lab15

import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var adapter: ArrayAdapter<String>
    private val viewModel: NotesViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return NotesViewModel(AppDatabase.getDatabase(this@MainActivity)) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
        val mainLayout = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.mainLayout)
        val currentColor = sharedPreferences.getInt("background_color", Color.WHITE)
        mainLayout.setBackgroundColor(currentColor)

        val etNote = findViewById<EditText>(R.id.etNote)
        val btnAdd = findViewById<Button>(R.id.btnAdd)
        val lvNotes = findViewById<ListView>(R.id.lvNotes)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1)
        lvNotes.adapter = adapter

        viewModel.allNotes.observe(this) { notes ->
            adapter.clear()
            notes.forEach { note ->
                adapter.add("${note.id}: ${note.text}")
            }
        }

        btnAdd.setOnClickListener {
            val text = etNote.text.toString()
            if (text.isNotEmpty()) {
                viewModel.insert(Note(text = text))
                etNote.text.clear()
            }
        }

        lvNotes.setOnItemClickListener { _, _, position, _ ->
            val note = viewModel.allNotes.value?.get(position) ?: return@setOnItemClickListener

            AlertDialog.Builder(this)
                .setTitle("Выберите действие")
                .setMessage(note.text)
                .setPositiveButton("Редактировать") { _, _ ->
                    showEditDialog(note)
                }
                .setNegativeButton("Удалить") { _, _ ->
                    viewModel.delete(note)
                }
                .show()
        }

        val btnToggleTheme = findViewById<Button>(R.id.btnToggleTheme)


        btnToggleTheme.setOnClickListener {
            val newColor = if (currentColor == Color.WHITE) Color.YELLOW else Color.WHITE
            mainLayout.setBackgroundColor(newColor)
            saveBackgroundColor(newColor)
            recreate()
        }
    }

    private fun showEditDialog(note: Note) {
        val editText = EditText(this)
        editText.setText(note.text)

        AlertDialog.Builder(this)
            .setTitle("Редактировать заметку")
            .setView(editText)
            .setPositiveButton("Сохранить") { _, _ ->
                val newText = editText.text.toString()
                viewModel.update(note.copy(text = newText))
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun saveBackgroundColor(color: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt("background_color", color)
        editor.apply()
    }
}

class NotesViewModel(private val db: AppDatabase) : ViewModel() {
    private val dao = db.noteDao()

    val allNotes = dao.getAllNotes().asLiveData()

    fun insert(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(note)
        }
    }

    fun update(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.update(note)
        }
    }

    fun delete(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.delete(note)
        }
    }
}