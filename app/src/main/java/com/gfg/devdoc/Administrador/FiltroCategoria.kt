package com.gfg.devdoc.Administrador

import android.widget.Filter

class FiltroCategoria : Filter{

    private var filtroLista : ArrayList<ModeloCategoria>
    private var adaptadorCategoria: AdaptadorCategoria

    constructor(filtroLista: ArrayList<ModeloCategoria>, adaptadorCategoria: AdaptadorCategoria) {
        this.filtroLista = filtroLista
        this.adaptadorCategoria = adaptadorCategoria
    }

    override fun performFiltering(categoria: CharSequence?): FilterResults {
        var categoria = categoria
        var resultados=FilterResults()

        if (categoria!=null && categoria.isNotEmpty()){
            categoria=categoria.toString().uppercase()
            val modelofiltrado:ArrayList<ModeloCategoria> = ArrayList()
            for (i in 0 until filtroLista.size){
                if (filtroLista[i].categoria.uppercase().contains(categoria)){
                    modelofiltrado.add(filtroLista[i])
                }
                resultados.count=modelofiltrado.size
                resultados.values=modelofiltrado
            }
        }
        else{
            resultados.count=filtroLista.size
            resultados.values=filtroLista
        }
        return resultados
    }

    override fun publishResults(p0: CharSequence?, resultados: FilterResults) {
        adaptadorCategoria.categoriaArrayList=resultados.values as ArrayList<ModeloCategoria>
        adaptadorCategoria.notifyDataSetChanged()

    }


}