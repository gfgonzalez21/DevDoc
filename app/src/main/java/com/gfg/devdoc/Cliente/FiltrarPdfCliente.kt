package com.gfg.devdoc.Cliente


import android.widget.Filter
import com.gfg.devdoc.Administrador.ModeloPdf



class FiltrarPdfCliente :Filter {

    var filtroList : ArrayList<ModeloPdf>
    var adaptadorPdfCliente:AdaptadorPdfCliente

    constructor(filtroList: ArrayList<ModeloPdf>, adaptadorPdfCliente: AdaptadorPdfCliente) {
        this.filtroList = filtroList
        this.adaptadorPdfCliente = adaptadorPdfCliente
    }

    override fun performFiltering(libro: CharSequence?): FilterResults {
        var libro:CharSequence?=libro
        var resultados = FilterResults()
        if (libro!=null && libro.isNotEmpty()){
            libro=libro.toString().lowercase()
            val modelofiltrado : ArrayList<ModeloPdf> = ArrayList()
            for(i in filtroList.indices){
                if (filtroList[i].titulo.lowercase().contains(libro)){
                    modelofiltrado.add(filtroList[i])
                }
            }
            resultados.count=modelofiltrado.size
            resultados.values=modelofiltrado
        }
        else{
            resultados.count=filtroList.size
            resultados.values=filtroList
        }
        return resultados
    }

    override fun publishResults(constraint: CharSequence?, resultados: FilterResults?) {
        adaptadorPdfCliente.pdfArrayList= resultados?.values as ArrayList<ModeloPdf>
        adaptadorPdfCliente.notifyDataSetChanged()
    }
}