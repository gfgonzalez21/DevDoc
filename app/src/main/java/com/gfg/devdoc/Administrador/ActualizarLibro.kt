package com.gfg.devdoc.Administrador

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.gfg.devdoc.R
import com.gfg.devdoc.databinding.ActivityActualizarLibroBinding
import com.gfg.devdoc.databinding.ActivityListaPdfAdminBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ActualizarLibro : AppCompatActivity() {


    private lateinit var binding: ActivityActualizarLibroBinding
    private var idLibro =""
    private lateinit var progressDialog: ProgressDialog

    private lateinit var catTituloArrayList: ArrayList<String>
    private lateinit var catIdArrayList: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityActualizarLibroBinding.inflate(layoutInflater)
        setContentView(binding.root)



        idLibro=intent.getStringExtra("idLibro")!!


        progressDialog=ProgressDialog(this)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)

        cargarCategorias()
        cargarInformacion()

        binding.IbRegresar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.TvCategoriaPdf.setOnClickListener {
            dialogCategoria()
        }
        binding.BtnActualizarLibro.setOnClickListener {
            validarInformacion()
        }
    }

    private fun cargarInformacion() {
        val ref= FirebaseDatabase.getInstance().getReference("Libros")
        ref.child(idLibro)
            .addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val titulo =snapshot.child("titulo").value.toString()
                    val descripcion =snapshot.child("descripcion").value.toString()
                    id_seleccionado=snapshot.child("categoria").value.toString()

                    binding.EtTituloLibro.setText(titulo)
                    binding.EtDescripcionLibro.setText(descripcion)
                    val refCategoria =FirebaseDatabase.getInstance().getReference("Categorias")
                    refCategoria.child(id_seleccionado)
                        .addListenerForSingleValueEvent(object :ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val categoria = snapshot.child("categoria").value
                                binding.TvCategoriaPdf.text=categoria.toString()
                            }
                            override fun onCancelled(error: DatabaseError) {
                            }
                        })
                }
                override fun onCancelled(error: DatabaseError) {
                }

            })

    }
    private var titulo=""
    private var descripcion=""
    private fun validarInformacion() {
        titulo=binding.EtTituloLibro.text.toString().trim()
        descripcion = binding.EtDescripcionLibro.text.toString().trim()

        if (titulo.isEmpty()){
            Toast.makeText(this,"Ingrese un titulo ", Toast.LENGTH_SHORT).show()
        }
        else if (descripcion.isEmpty()){
            Toast.makeText(this,"Ingrese una descripcion ", Toast.LENGTH_SHORT).show()
        }
        else if (id_seleccionado.isEmpty()) {
            Toast.makeText(this, "Seleccione una categoria ", Toast.LENGTH_SHORT).show()
        }
        else{
            actualizarInformacion()
        }
    }
    private fun actualizarInformacion() {
        progressDialog.setMessage("Actualizando la informacion")
        progressDialog.show()
        val hashMap=HashMap<String,Any>()
        hashMap["titulo"]="$titulo"
        hashMap["descripcion"]="$descripcion"
        hashMap["categoria"]="$id_seleccionado"

        val ref=FirebaseDatabase.getInstance().getReference("Libros")
        ref.child(idLibro)
            .updateChildren(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(this,"Actualizacion subida con exito",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e->
                progressDialog.dismiss()
                Toast.makeText(this,"La actualizacion fallo debido a ${e.message}",Toast.LENGTH_SHORT).show()

            }
    }
    private var id_seleccionado =""
    private var titulo_seleccionado=""
    private fun dialogCategoria() {
        val categoriaArray = arrayOfNulls<String>(catTituloArrayList.size)
        for (i in catTituloArrayList.indices){
            categoriaArray[i]=catTituloArrayList[i]
        }
        val buider = AlertDialog.Builder(this)
        buider.setTitle("Seleccione una categoria")
            .setItems(categoriaArray){dialog,posicion->
                id_seleccionado=catIdArrayList[posicion]
                titulo_seleccionado=catTituloArrayList[posicion]

                binding.TvCategoriaPdf.text=titulo_seleccionado

            }
            .show()
    }

    private fun cargarCategorias() {
        catTituloArrayList= ArrayList()
        catIdArrayList= ArrayList()

        val ref= FirebaseDatabase.getInstance().getReference("Categorias")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                catTituloArrayList.clear()
                catIdArrayList.clear()
                for (ds in snapshot.children){
                    val id =""+ds.child("id").value
                    val categoria =""+ds.child("categoria").value

                    catTituloArrayList.add(categoria)
                    catIdArrayList.add(id)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}