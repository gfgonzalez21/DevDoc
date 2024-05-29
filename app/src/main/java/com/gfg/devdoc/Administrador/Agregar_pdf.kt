package com.gfg.devdoc.Administrador

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.gfg.devdoc.R
import com.gfg.devdoc.databinding.ActivityAgregarPdfBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class Agregar_pdf : AppCompatActivity() {

    private lateinit var binding: ActivityAgregarPdfBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    private var pdfUri : Uri?=null
    private lateinit var categoriaArraylist: ArrayList<ModeloCategoria>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAgregarPdfBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth=FirebaseAuth.getInstance()

        cargarCategorias()

        progressDialog=ProgressDialog(this)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)



        binding.IbRegresar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.AdjuntarPdfIb.setOnClickListener {
            ElegirPdf()
        }

        binding.TvCategoriaPdf.setOnClickListener {
            SeleccionarCat()
        }

        binding.BtnSubirLibro.setOnClickListener {
            ValidarInformacion()
        }


    }

    private var titulo =""
    private var descripcion =""
    private var categoria =""

    private fun ValidarInformacion() {
        titulo=binding.EtTituloPdf.text.toString().trim()
        descripcion=binding.EtDescripcionPdf.text.toString().trim()
        categoria=binding.TvCategoriaPdf.text.toString().trim()

        if (titulo.isEmpty()){
            Toast.makeText(this,"Ingrese un titulo ",Toast.LENGTH_SHORT).show()
        }
        else if (descripcion.isEmpty()){
            Toast.makeText(this,"Ingrese una descripcion ",Toast.LENGTH_SHORT).show()
        }
        else if (categoria.isEmpty()){
            Toast.makeText(this,"Ingrese una categoria ",Toast.LENGTH_SHORT).show()
        }
        else if (pdfUri == null){
            Toast.makeText(this,"Adjunte el pdf ",Toast.LENGTH_SHORT).show()
        }
        else{
            SubirPdfStore()
        }
    }
    @SuppressLint("SuspiciousIndentation")
    private fun SubirPdfStore() {
        progressDialog.setMessage("Subiendo el pdf")
        progressDialog.show()

        val tiempo =System.currentTimeMillis()
        val ruta_libro = "Libros/$tiempo"
        val storageReference = FirebaseStorage.getInstance().getReference(ruta_libro)
        storageReference.putFile(pdfUri!!)
            .addOnSuccessListener {tarea->
                val uriTask : Task<Uri> = tarea.storage.downloadUrl
                while (!uriTask.isSuccessful);
                val UrlPdfSubido = "${uriTask.result}"
                SubirPdfBd(UrlPdfSubido,tiempo)
            }
            .addOnFailureListener {e->
                progressDialog.dismiss()
                Toast.makeText(this,"Fallo la subida del archivo debido a ${e.message}",Toast.LENGTH_SHORT).show()

            }
    }

    private fun SubirPdfBd(urlPdfSubido: String, tiempo: Long) {
        progressDialog.setMessage("Subiendo el pdf a la BD")
        val uid =firebaseAuth.uid

        val hashMap :HashMap<String , Any> = HashMap()
        hashMap["uid"]="$uid"
        hashMap["id"]="$tiempo"
        hashMap["titulo"]=titulo
        hashMap["descripcion"]=descripcion
        hashMap["categoria"]=id_categoria
        hashMap["url"]=urlPdfSubido
        hashMap["tiempo"]=tiempo
        hashMap["contadorVisitas"]=0
        hashMap["contadorDescargas"]=0

        val ref=FirebaseDatabase.getInstance().getReference("Libros")
        ref.child("$tiempo")
            .setValue(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(this,"Documentacion subida con exito",Toast.LENGTH_SHORT).show()
                binding.EtTituloPdf.setText("")
                binding.EtDescripcionPdf.setText("")
                binding.TvCategoriaPdf.setText("")
                pdfUri=null

            }
            .addOnFailureListener { e->
                progressDialog.dismiss()
                Toast.makeText(this,"Fallo en la publicacion de la documentacion  debido a ${e.message}",Toast.LENGTH_SHORT).show()

            }

    }
    private fun cargarCategorias() {
        categoriaArraylist=ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("Categorias").orderByChild("categoria")
        ref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                categoriaArraylist.clear()
                for (ds in snapshot.children){
                    val modelo= ds.getValue(ModeloCategoria::class.java)
                    categoriaArraylist.add(modelo!!)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private var id_categoria=""
    private var titulo_categoria=""
    private fun SeleccionarCat(){
        val categoriasArraylist = arrayOfNulls<String>(categoriaArraylist.size)
        for (i in categoriasArraylist.indices){
            categoriasArraylist[i]=categoriaArraylist[i].categoria
        }
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Seleccionar barrio")
            .setItems(categoriasArraylist){dialog,which->
                id_categoria=categoriaArraylist[which].id
                titulo_categoria=categoriaArraylist[which].categoria
                binding.TvCategoriaPdf.text=titulo_categoria

            }
            .show()

    }

    private fun ElegirPdf(){
        val intent = Intent()
        intent.type="application/pdf"
        intent.action=Intent.ACTION_GET_CONTENT
        pdfActivityRL.launch(intent)
    }
    val pdfActivityRL =registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> {resultado->
            if(resultado.resultCode== RESULT_OK){
                pdfUri=resultado.data!!.data
            }else{
                Toast.makeText(this,"cancelado",Toast.LENGTH_SHORT).show()
            }

        }
    )
}