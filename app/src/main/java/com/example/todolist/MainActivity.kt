package com.example.todolist

import android.content.Context
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.io.OutputStreamWriter
import java.io.PrintStream
import java.util.*

const val SEPARATE = " --> "

class MainActivity : AppCompatActivity() {

    lateinit var userTask : EditText
    val list = mutableListOf<String>()//Creacion de una lista vacia
    lateinit var arrayAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        //val arrayAdapter: ArrayAdapter<*> //Todas las listas necesitan un adaptador -
        list.addAll(showFile())
        val listView = findViewById<ListView>(R.id.listView)
        listView.setOnItemLongClickListener { adapterView, view, i, l ->

            delTask(i)
            true
        }
        val buttom = findViewById<Button>(R.id.btnAdd)
        userTask = findViewById(R.id.task)

        buttom.setOnClickListener {
             addTask()
        }


        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
        listView.adapter = arrayAdapter


    }

    fun addTask(){

        val newText = userTask.getText().toString()

        if(newText.isNotEmpty()){

            list.add(newText)//Agregar a la lista que se creo (list)
            arrayAdapter.notifyDataSetChanged()

            val file = openFileOutput("Tareas.txt", MODE_APPEND)
            val out = PrintStream(file)
            out.print(SEPARATE + newText)
            out.close()

        }

    }

    fun showFile() : List<String> {


        try {
            val file = openFileInput("Tareas.txt")
            val inp = Scanner(file)
            var listTask = listOf<String>()//una lista vacia
            if(inp.hasNextLine()){
                listTask = inp.nextLine().split(SEPARATE)
            }
            inp.close()

            return listTask

        }catch (e : Exception){

            Toast.makeText(applicationContext,"Bienvenido a la aplicación, empieza guardando tus tareas!", Toast.LENGTH_SHORT).show()

            return listOf()
        }

    }

    fun delTask(i : Int){



        list.removeAt(i)//El index es la posicion que voy a eliminar
        arrayAdapter.notifyDataSetChanged()


        val file = openFileOutput("Tareas.txt", MODE_PRIVATE)
      /*  val out = PrintStream(file) //los objetos se crean en mayuscula la primera
        out.print(SEPARATE + newText)
        out.close()*/

        var text=""
       list.map {
           text += it + SEPARATE
       }
        val osw = OutputStreamWriter(file)

        osw.append(text)
        osw.flush() //limpia la memoria
        osw.close()


    }




}