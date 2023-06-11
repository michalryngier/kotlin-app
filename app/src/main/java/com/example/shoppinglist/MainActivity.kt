package com.example.shoppinglist

import android.content.Context
import android.os.Bundle
import android.util.SparseBooleanArray
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import android.widget.TextView.OnEditorActionListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val addButton = findViewById<Button>(R.id.add_button)
        val deleteButton = findViewById<Button>(R.id.delete_button)
        val toDoList = findViewById<ListView>(R.id.to_do_list_view)
        val addItemEdit = findViewById<EditText>(R.id.add_item_edit)
        val errorMessageText = findViewById<TextView>(R.id.error_message_text)

        val listItems = arrayListOf<String>()
        val arrayAdapter: ArrayAdapter<String> =
            ArrayAdapter(this, android.R.layout.select_dialog_multichoice, listItems)
        toDoList.adapter = arrayAdapter

//        hide error message when text changes
        addItemEdit.doAfterTextChanged {
            errorMessageText.visibility = View.GONE
        }

        addItemEdit.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                addButton.performClick()
                return@OnEditorActionListener true
            }
            false
        })
//        add item
        addButton.setOnClickListener {
            val itemText: String = addItemEdit.text.toString()
            if (itemText.isNotEmpty()) {
                listItems.add(itemText)
                arrayAdapter.notifyDataSetChanged()
                addItemEdit.setText("")
                errorMessageText.visibility = View.GONE
                Toast.makeText(this, "Dodano: " + itemText, Toast.LENGTH_SHORT).show()
            } else {
                errorMessageText.visibility = View.GONE
                errorMessageText.text = "Nazwa produktu nie może pozostać pusta"
                errorMessageText.visibility = View.VISIBLE
            }
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }

//        delete item
        deleteButton.setOnClickListener {
            val position: SparseBooleanArray = toDoList.checkedItemPositions
            val count = toDoList.count

            for (item in count - 1 downTo 0) {
                if (position[item]) {
                    arrayAdapter.remove(listItems[item])
                }
            }
            position.clear()
            arrayAdapter.notifyDataSetChanged()
        }
    }
}