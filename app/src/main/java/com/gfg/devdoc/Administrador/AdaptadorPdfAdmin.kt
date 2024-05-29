package com.gfg.devdoc.Administrador

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gfg.devdoc.databinding.ItemLibroAdminBinding

class AdaptadorPdfAdmin :RecyclerView.Adapter<AdaptadorPdfAdmin.HolderPdfAdmin> {

    private lateinit var binding: ItemLibroAdminBinding

    private var m_context : Context
    public var pdfArrayList : ArrayList<ModeloPdf>

    constructor(m_context: Context, pdfArrayList: ArrayList<ModeloPdf>) : super() {
        this.m_context = m_context
        this.pdfArrayList = pdfArrayList
    }


    inner class HolderPdfAdmin (itemView: View) : RecyclerView.ViewHolder(itemView){


        val VisualizadorPDF =binding.VisualizadorPDF
        val progressBar =binding.progressBar
        val Txt_titulo_libro_item = binding.TxtTituloLibroItem
        val Txt_descripcion_libro_item=binding.TxtDescripcionLibroItem
        val Txt_categoria_libro_admin =binding.TxtCategoriaLibroAdmin
        val Txt_fecha_libro_item=binding.TxtFechaCasaItem
        val Ib_mas_opciones=binding.IbMasOpciones
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderPdfAdmin {
        binding= ItemLibroAdminBinding.inflate(LayoutInflater.from(m_context),parent,false)
        return HolderPdfAdmin(binding.root)
    }

    override fun getItemCount(): Int {
        return pdfArrayList.size
    }

    override fun onBindViewHolder(holder: HolderPdfAdmin, position: Int) {

        val modelo=pdfArrayList[position]
        val pdfId =modelo.id
        val categoriaId =modelo.categoria
        val titulo =modelo.titulo
        val descripcion =modelo.descripcion
        val pdfUrl =modelo.url
        val tiempo =modelo.tiempo

        val formatoTiempo = MisFunciones.formatoTiempo(tiempo)

        holder.Txt_titulo_libro_item.text=titulo
        holder.Txt_descripcion_libro_item.text=descripcion
        holder.Txt_fecha_libro_item.text=formatoTiempo

        MisFunciones.CargarCategoria(categoriaId,holder.Txt_categoria_libro_admin)
        //Parte de codigo para mostrar la imagen ,(da error)
        MisFunciones.CargarPdfUrl(pdfUrl,titulo,holder.VisualizadorPDF,holder.progressBar,null)


    }

}