package com.gfg.devdoc.Cliente

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gfg.devdoc.Administrador.MisFunciones
import com.gfg.devdoc.Administrador.ModeloPdf
import com.gfg.devdoc.LeerLibro
import com.gfg.devdoc.databinding.ItemLibroFavoritoBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdaptadorPdfFavoritos : RecyclerView.Adapter<AdaptadorPdfFavoritos.HolderPdfFavorito>{

    private val context : Context
    private var arrayListlibros:ArrayList<ModeloPdf>
    private lateinit var binding:ItemLibroFavoritoBinding

    constructor(context: Context, arrayListlibros: ArrayList<ModeloPdf>) {
        this.context = context
        this.arrayListlibros = arrayListlibros
    }

    inner class HolderPdfFavorito(itemView : View):RecyclerView.ViewHolder(itemView){

        var visualizadorPdf=binding.VisualizadorPDF
        var progressBar = binding.progressBar
        var titulo = binding.TxtTituloLibroItem
        var descripcion = binding.TxtDescripcionLibroItem
        var categoria = binding.TxtCategoriaLibroAdmin
        var fecha = binding. TxtFechaCasaItem
        var ib_eliminar_fav=binding.IbEliminarFav
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderPdfFavorito {

        binding= ItemLibroFavoritoBinding.inflate(LayoutInflater.from(context),parent,false)
        return HolderPdfFavorito(binding.root)
    }

    override fun getItemCount(): Int {
        return arrayListlibros.size
    }

    override fun onBindViewHolder(holder: HolderPdfFavorito, position: Int) {
       var modelo = arrayListlibros[position]
        cargarDetalleLibro(modelo,holder)

        holder.itemView.setOnClickListener {
            val intent= Intent(context,DetalleLibro_cliente::class.java)
            intent.putExtra("idLibro",modelo.id)
            context.startActivity(intent)
        }
        holder.ib_eliminar_fav.setOnClickListener {
            MisFunciones.EliminarFavoritos(context,modelo.id)
        }
    }

    private fun cargarDetalleLibro(modelo: ModeloPdf, holder: AdaptadorPdfFavoritos.HolderPdfFavorito) {
        val idLibro = modelo.id

        val ref = FirebaseDatabase.getInstance().getReference("Libros")
            ref.child(idLibro)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val categoria ="${snapshot.child("categoria").value}"
                    val contadorVisitas ="${snapshot.child("contadorVisitas").value}"
                    val contadorDescargas ="${snapshot.child("contadorDescargas").value}"
                    val descripcion="${snapshot.child("descripcion").value}"
                    val tiempo ="${snapshot.child("tiempo").value}"
                    val titulo ="${snapshot.child("titulo").value}"
                    val url ="${snapshot.child("url").value}"
                    val uid ="${snapshot.child("uid").value}"

                    modelo.esfavorito=true
                    modelo.titulo=titulo
                    modelo.descripcion=descripcion
                    modelo.categoria=categoria
                    modelo.titulo=titulo
                    modelo.uid=uid
                    modelo.url=url
                    modelo.contadorVisitas=contadorVisitas.toLong()
                    modelo.contadorDescargas=contadorDescargas.toLong()


                    val fecha = MisFunciones.formatoTiempo(tiempo.toLong())
                    MisFunciones.CargarCategoria("$categoria",holder.categoria)

                    MisFunciones.CargarPdfUrl("$url","$titulo",binding.VisualizadorPDF,binding.progressBar)

                    holder.titulo.text=titulo
                    holder.descripcion.text=descripcion
                    holder.fecha.text=fecha



                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

    }

}