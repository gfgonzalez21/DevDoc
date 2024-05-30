package com.gfg.devdoc.Administrador

import android.widget.Filter

class FiltoPdfAdmin :Filter{

    var filtroList : ArrayList<ModeloPdf>
    var adaptadorPdfAdmin:AdaptadorPdfAdmin

    constructor(filtroList: ArrayList<ModeloPdf>, adaptadorPdfAdmin: AdaptadorPdfAdmin) {
        this.filtroList = filtroList
        this.adaptadorPdfAdmin = adaptadorPdfAdmin
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
        adaptadorPdfAdmin.pdfArrayList= resultados?.values as ArrayList<ModeloPdf>
        adaptadorPdfAdmin.notifyDataSetChanged()
    }
}