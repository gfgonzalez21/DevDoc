package com.gfg.devdoc.Cliente

import android.widget.Filter
import com.gfg.devdoc.Administrador.AdaptadorCategoria
import com.gfg.devdoc.Administrador.ModeloCategoria

class FiltrarCategoria_Cliente :Filter{


    private var filtroLista : ArrayList<ModeloCategoria>
    private var adaptadorCategoriaCliente: AdaptadorCategoria_cliente

    constructor(filtroLista: ArrayList<ModeloCategoria>, adaptadorCategoriaCliente: AdaptadorCategoria_cliente) {
        this.filtroLista = filtroLista
        this.adaptadorCategoriaCliente = adaptadorCategoriaCliente
    }

    override fun performFiltering(categoria: CharSequence?): Filter.FilterResults {
        var categoria = categoria
        var resultados= Filter.FilterResults()

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

    override fun publishResults(p0: CharSequence?, resultados: Filter.FilterResults) {
        adaptadorCategoriaCliente.categoriaArrayList=resultados.values as ArrayList<ModeloCategoria>
        adaptadorCategoriaCliente.notifyDataSetChanged()

    }
}