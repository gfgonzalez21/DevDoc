package com.gfg.devdoc.Cliente

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.gfg.devdoc.Administrador.ActualizarLibro
import com.gfg.devdoc.Administrador.AdaptadorPdfAdmin
import com.gfg.devdoc.Administrador.DetalleLibro
import com.gfg.devdoc.Administrador.FiltoPdfAdmin
import com.gfg.devdoc.Administrador.MisFunciones
import com.gfg.devdoc.Administrador.ModeloPdf
import com.gfg.devdoc.databinding.ItemLibroAdminBinding
import com.gfg.devdoc.databinding.ItemLibroClienteBinding

class AdaptadorPdfCliente : RecyclerView.Adapter<AdaptadorPdfCliente.HolderPdfCliente>,Filterable{
    private lateinit var binding: ItemLibroClienteBinding

    private var m_context : Context
    public var pdfArrayList : ArrayList<ModeloPdf>
    private var filtroLibro:ArrayList<ModeloPdf>
    private var filtro: FiltrarPdfCliente?=null

    constructor(m_context: Context, pdfArrayList: ArrayList<ModeloPdf>) : super() {
        this.m_context = m_context
        this.pdfArrayList = pdfArrayList
        this.filtroLibro=pdfArrayList
    }


    inner class HolderPdfCliente (itemView: View) : RecyclerView.ViewHolder(itemView){


        val VisualizadorPDF =binding.VisualizadorPDF
        val progressBar =binding.progressBar
        val Txt_titulo_libro_item = binding.TxtTituloLibroItem
        val Txt_descripcion_libro_item=binding.TxtDescripcionLibroItem
        val Txt_categoria_libro_admin =binding.TxtCategoriaLibroAdmin
        val Txt_fecha_libro_item=binding.TxtFechaCasaItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderPdfCliente {
        binding= ItemLibroClienteBinding.inflate(LayoutInflater.from(m_context),parent,false)
        return HolderPdfCliente(binding.root)
    }

    override fun getItemCount(): Int {
        return pdfArrayList.size
    }

    override fun onBindViewHolder(holder: HolderPdfCliente, position: Int) {

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
        MisFunciones.CargarPdfUrl(pdfUrl,titulo,holder.VisualizadorPDF,holder.progressBar)


        holder.itemView.setOnClickListener {
            val intent=Intent(m_context,DetalleLibro_cliente::class.java)
            intent.putExtra("idLibro",pdfId)
            m_context.startActivity(intent)
        }
    }

    override fun getFilter(): Filter {
        if (filtro== null){
            filtro= FiltrarPdfCliente(filtroLibro, this)
        }
        return filtro as FiltrarPdfCliente
    }

}