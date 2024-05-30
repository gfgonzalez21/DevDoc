package com.gfg.devdoc.Administrador

import android.app.Application
import android.app.ProgressDialog
import android.content.Context
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.github.barteksc.pdfviewer.PDFView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.util.Calendar
import java.util.Locale

class MisFunciones: Application() {

    override fun onCreate() {
        super.onCreate()
    }
    companion object{
        fun formatoTiempo (tiempo : Long) : String{
            val cal = Calendar.getInstance(Locale.ENGLISH)
            cal.timeInMillis = tiempo
            //dd/MM/yyyy
            return  android.text.format.DateFormat.format("dd/MM/yyyy",cal).toString()

        }
        fun CargarPdfUrl(pdfUrl: String, pdfTitulo: String, pdfView: PDFView, progressBar: ProgressBar) {


            val ref = FirebaseStorage.getInstance().getReferenceFromUrl(pdfUrl)

            ref.getBytes(Constantes.Maximo_bytes_pdf)
                .addOnSuccessListener { bytes ->
                    pdfView.fromBytes(bytes)
                        .pages(0)
                        .spacing(0)
                        .swipeHorizontal(false)
                        .enableSwipe(false)
                        .onError { t ->
                            progressBar.visibility = View.INVISIBLE
                        }
                        .onPageError { page, pageCount ->
                            progressBar.visibility = View.INVISIBLE
                        }
                        .onLoad { pagina ->

                        }
                        .load()
                }
                .addOnFailureListener { e ->

                }
        }
        fun CargarCategoria (categoriaId : String,categoriaTv : TextView){
            val ref= FirebaseDatabase.getInstance().getReference("Categorias")
            ref.child(categoriaId)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val barrio ="${snapshot.child("categoria").value}"
                        categoriaTv.text= barrio
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
        }


        fun EliminarLibro(context: Context, idLibro : String, urlLibro:String, tituloLibro:String){

            val progressDialog = ProgressDialog(context)
            progressDialog.setTitle("Espere por favor")
            progressDialog.setMessage("Eliminado $tituloLibro")
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.show()

            val storageReference =FirebaseStorage.getInstance().getReferenceFromUrl(urlLibro)
            storageReference.delete()
                .addOnSuccessListener {
                    val ref=FirebaseDatabase.getInstance().getReference("Libros")
                    ref.child(idLibro)
                        .removeValue()
                        .addOnSuccessListener {
                            progressDialog.dismiss()
                            Toast.makeText(context,"la documentacion se ha eliminado con exito ", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {e->
                            Toast.makeText(context,"Fallo la eliminacion debido a ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
                .addOnFailureListener {e->
                    progressDialog.dismiss()
                    Toast.makeText(context,"Fallo la eliminacion debido a ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
}
}